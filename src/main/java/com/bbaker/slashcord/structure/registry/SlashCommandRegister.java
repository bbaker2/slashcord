package com.bbaker.slashcord.structure.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.javacord.api.DiscordApi;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandBuilder;

import com.bbaker.slashcord.structure.entity.Command;
import com.bbaker.slashcord.structure.util.ConverterUtil;

public class SlashCommandRegister {

    List<Command> queued = new ArrayList<>();

    public SlashCommandRegister queue(Command command) {
        queued.add(command);
        return this;
    }

    public CompletableFuture<List<SlashCommand>> upsert(DiscordApi api){
        List<SlashCommandBuilder> prepared = preview(api);

        return api.bulkOverwriteGlobalSlashCommands(prepared);
    }

    public List<SlashCommandBuilder> preview(DiscordApi api){
        List<Command> preExisting = api.getGlobalSlashCommands().join().stream()
                .map(ConverterUtil::from)
                .collect(Collectors.toList());


        List<SlashCommandBuilder> toUpsert = new ArrayList<>();

        outer:
        for(Command desired : queued) {
            for(Command existing : preExisting) {
                // If the name matches, lets make sure the structures match
                if(desired.getName().equals(existing.getName())){
                    if(desired.equals(existing)) {
                        // they match. We do not have to queue this command for updates
                        break;
                    } else { // they do not match. Queue this command for an update
                        toUpsert.add(ConverterUtil.from(desired));
                        continue outer; // force continue the outer loop
                    }
                }
            }
            // if we complete the inner loop without a continue or break
            // we can assume the command does not yet exist in discord.
            // So queue this command for insert
            toUpsert.add(ConverterUtil.from(desired));
        }
        return toUpsert;
    }

}
