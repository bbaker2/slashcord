package com.bbaker.slashcord.structure;

import static java.util.function.Function.identity;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandUpdater;

import com.bbaker.slashcord.structure.annotation.CommandDef;
import com.bbaker.slashcord.structure.annotation.GroupCommandDef;
import com.bbaker.slashcord.structure.annotation.SubCommandDef;
import com.bbaker.slashcord.structure.entity.Command;
import com.bbaker.slashcord.structure.results.FailedResult;
import com.bbaker.slashcord.structure.results.Operation;
import com.bbaker.slashcord.structure.results.SuccessfulResult;
import com.bbaker.slashcord.structure.results.UpsertResult;
import com.bbaker.slashcord.util.BadAnnotation;
import com.bbaker.slashcord.util.ConverterUtil;
import com.bbaker.slashcord.util.ThrowableFunction;

public class SlashCommandRegisterImpl implements SlashCommandRegister {
    List<QueuedCommand> queued = new ArrayList<>();
    List<UpsertResult> failedQueued = new ArrayList<>();

    @Override
    public SlashCommandRegister queue(Object command, Server... servers) {
        List<Server> serverList = servers.length == 0 ? null : Arrays.asList(servers);


        // Anything that is already a command gets
        // to be queued instantly
        if(command instanceof Command) {
            Command cmd = (Command)command;
            queued.add(new QueuedCommand(cmd, serverList));
        }

        // then scan for annotation
        scanAndQueue(CommandDef.class, 		command, ConverterUtil::from, serverList);
        scanAndQueue(SubCommandDef.class, 	command, ConverterUtil::from, serverList);
        scanAndQueue(GroupCommandDef.class, command, ConverterUtil::from, serverList);

        return this;
    }

    private <T extends Annotation> void scanAndQueue(Class<T> anotation, Object instance, ThrowableFunction<T, Command, BadAnnotation> converter, List<Server> servers){
        T[] repeatingDefs = instance.getClass().getAnnotationsByType(anotation);

        for(T cmdDef : repeatingDefs) {
            // if one does exist, attempt to convert it into the Command class
            try {
                Command cmd = converter.apply(cmdDef);
                queued.add(new QueuedCommand(cmd, servers));
            } catch (BadAnnotation e) {
                // If there are failures... suppress the exception
                // and short circuit
                failedQueued.add(new FailedResult(e, null, Operation.SKIP));
                continue;
            }
        }
    }


    @Override
    public CompletableFuture<List<UpsertResult>> upsert(DiscordApi api){
        Upsert prepared = previewInsert(api);

        final List<UpsertResult> successful = new ArrayList<>();

        CompletableFuture<List<UpsertResult>> allFutures = CompletableFuture.completedFuture(successful);
        // Perform inserts
        System.out.println("Inserting " + prepared.getInserts().size() + " commands");
        for(Meta<SlashCommandBuilder> insert : prepared.getInserts()) {
            allFutures = allFutures
                .thenCompose(allSuccess -> insert.getBuilder().createGlobal(api)) // insert one command
                .thenApply(SuccessfulResult.insert(insert.getCommand()))
                .exceptionally(FailedResult.insert(insert.getCommand()))
                .thenApply(successful::add)							 // add the command (once created) to the success list
                .thenApply(ignore -> successful);					 // A little trick to convert CompletableFuture<SlashCommand> into CompletableFuture<List<SlashCommand>>

        }

        // Perform updates
        System.out.println("Updating " + prepared.getUpdates().size() + " commands");
        for(Meta<SlashCommandUpdater> update : prepared.getUpdates()) {
            allFutures = allFutures
                .thenCompose(allSuccess -> update.getBuilder().updateGlobal(api)) // update one command
                .thenApply(SuccessfulResult.update(update.getCommand()))
                .exceptionally(FailedResult.update(update.getCommand()))
                .thenApply(successful::add)							 // add the command (once updated) to the success list
                .thenApply(ignore -> successful);					 // A little trick to convert CompletableFuture<SlashCommand> into CompletableFuture<List<SlashCommand>>
        }

        // Auto-complete the skips, for the purposes of logging
        System.out.println("Skipping " + prepared.getSkips().size() + " commands");
        for(Meta<SlashCommand> skip : prepared.getSkips()) {
            successful.add(new SuccessfulResult(skip.getCommand(), skip.getBuilder(), Operation.SKIP));
        }

        successful.addAll(failedQueued);

        // After all the operations are done,
        // flush the queues
        failedQueued.clear();
        queued.clear();

        return allFutures;
    }

    public Upsert previewInsert(DiscordApi api){
        List<SlashCommand> originalList = api.getGlobalSlashCommands().join();
        Map<String, SlashCommand> nameToCmd = originalList.stream().collect(Collectors.toMap(SlashCommand::getName, identity()));
        List<Command> preExisting = originalList.stream()
                .map(ConverterUtil::from)
                .collect(Collectors.toList());


        UpsertImpl upsert = new UpsertImpl();

        outer:
        for(QueuedCommand each : queued) {
            Command desired = each.cmd;
            for(Command existing : preExisting) {
                // If the name matches, lets make sure the structures match
                if(desired.getName().equals(existing.getName())){
                    // they match. We do not have to queue this command for updates
                    if(desired.equals(existing)) {
                        // Add the Slashcommand to the skip list (for logging purposes)
                        SlashCommand discordCmd = nameToCmd.get(desired.getName());
                        if(discordCmd != null) {
                            upsert.skip(desired, discordCmd);
                        }

                        continue outer; // force continue the outer loop

                    // they do not match. Queue this command for an update
                    } else {
                        // I'm aware that we are now performing a n^3 loop, but in practice these lists
                        // will be small and will not be called often.
                        // So little inefficiency will not kill overall performance
                        long commandId = matchByName(originalList, existing.getName());
                        upsert.update(desired, commandId);
                        continue outer; // force continue the outer loop
                    }
                }
            }
            // if we complete the inner loop without a continue or break
            // we can assume the command does not yet exist in discord.
            // So queue this command for insert
            upsert.insert(desired);
        }
        return upsert;
    }

    private long matchByName(List<SlashCommand> commands, String cmdName) {
        for(SlashCommand sc : commands) {
            if(sc.getName().equals(cmdName)) {
                return sc.getId();
            }
        }
        return 0; // pratically speaking, we will never reach here
    }

    private class QueuedCommand {

        public QueuedCommand(Command cmd, List<Server> servers) {
            this.cmd = cmd;
            this.servers = servers;
        }


        Command cmd;
        List<Server> servers;

        public boolean isGlobal() {
            return servers == null; // a non-null, but empty list is STILL declared as a server command
        }
    }

}
