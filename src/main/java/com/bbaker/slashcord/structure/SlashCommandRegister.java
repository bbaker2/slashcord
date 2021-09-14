package com.bbaker.slashcord.structure;

import static com.bbaker.slashcord.util.ConverterUtil.from;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.javacord.api.DiscordApi;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandUpdater;

import com.bbaker.slashcord.structure.annotation.CommandDef;
import com.bbaker.slashcord.structure.annotation.GroupCommandDef;
import com.bbaker.slashcord.structure.annotation.SubCommandDef;
import com.bbaker.slashcord.structure.entity.Command;
import com.bbaker.slashcord.util.BadAnnotation;
import com.bbaker.slashcord.util.ConverterUtil;
import com.bbaker.slashcord.util.ThrowableFunction;

public class SlashCommandRegister {

    List<Command> queued = new ArrayList<>();

    public SlashCommandRegister queue(Object command) {
        // Anything that is already a command gets
        // to be queued instantly
        if(command instanceof Command) {
            queued.add((Command)command);
        }

        // then scan for annotation
        scanAndQueue(CommandDef.class, 		command, ConverterUtil::from);
        scanAndQueue(SubCommandDef.class, 	command, ConverterUtil::from);
        scanAndQueue(GroupCommandDef.class, command, ConverterUtil::from);

        return this;
    }

    private <T extends Annotation> void scanAndQueue(Class<T> anotation, Object instance, ThrowableFunction<T, Command, BadAnnotation> converter){
        T cmdDef = instance.getClass().getAnnotation(anotation); // Grab the annotation, if any exist

        // if not exist, short circuit
        if(cmdDef == null) {
            return;
        }

        // if one does exist, attempt to convert it into the Command class
        try {
            Command cmd = converter.apply(cmdDef);
            queued.add(cmd);
        } catch (BadAnnotation e) {
            // If there are failures... suppress the exception
            // and short circuit
            e.printStackTrace();
            return;
        }
    }


    public CompletableFuture<List<SlashCommand>> upsert(DiscordApi api){
        Upsert prepared = previewInsert(api);

        final List<SlashCommand> successful = new ArrayList<>();

        CompletableFuture<List<SlashCommand>> allFutures = CompletableFuture.completedFuture(Arrays.asList());
        // Perform inserts
        System.out.println("Inserting " + prepared.toInsert.size() + " commands");
        for(SlashCommandBuilder insert : prepared.toInsert) {
            allFutures = allFutures
                .thenCompose(allSuccess -> insert.createGlobal(api)) // insert one command
                .thenApply(successful::add)							 // add the command (once created) to the success list
                .thenApply(ignore -> successful);					 // A little trick to convert CompletableFuture<SlashCommand> into CompletableFuture<List<SlashCommand>>

        }

        // Perform updates
        System.out.println("Updating " + prepared.toUpdate.size() + " commands");
        for(SlashCommandUpdater update : prepared.toUpdate) {
            allFutures = allFutures
                .thenCompose(allSuccess -> update.updateGlobal(api)) // update one command
                .thenApply(successful::add)							 // add the command (once updated) to the success list
                .thenApply(ignore -> successful);					 // A little trick to convert CompletableFuture<SlashCommand> into CompletableFuture<List<SlashCommand>>
        }

        return allFutures;
    }

    public Upsert previewInsert(DiscordApi api){
        List<SlashCommand> originalList = api.getGlobalSlashCommands().join();
        List<Command> preExisting = originalList.stream()
                .map(ConverterUtil::from)
                .collect(Collectors.toList());


        Upsert upsert = new Upsert();

        outer:
        for(Command desired : queued) {
            for(Command existing : preExisting) {
                // If the name matches, lets make sure the structures match
                if(desired.getName().equals(existing.getName())){
                    // they match. We do not have to queue this command for updates
                    if(desired.equals(existing)) {
                        continue outer; // force continue the outer loop

                    // they do not match. Queue this command for an update
                    } else {
                        // I'm aware that we are now performing a n^3 loop, but in practice these lists
                        // will be small and will not be called often.
                        // So little inefficiency will not kill overall performance
                        long commandId = matchByName(originalList, existing.getName());
                        upsert.toUpdate.add(from(desired, commandId));
                        continue outer; // force continue the outer loop
                    }
                }
            }
            // if we complete the inner loop without a continue or break
            // we can assume the command does not yet exist in discord.
            // So queue this command for insert
            upsert.toInsert.add(ConverterUtil.from(desired));
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

    private class Upsert {
        List<SlashCommandBuilder> toInsert = new ArrayList<>();
        List<SlashCommandUpdater> toUpdate = new ArrayList<>();
    }

}
