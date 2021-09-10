package com.bbaker.slashcord.dispatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.javacord.api.DiscordApi;
import org.javacord.api.interaction.SlashCommand;

import com.bbaker.slashcord.handler.SlashCommandListener;
import com.bbaker.slashcord.structure.SlashCommandRegister;
import com.bbaker.slashcord.structure.entity.Command;

public class SlashComandDispatcher {

    DiscordApi api;
    private static SlashCommandListener listener = null;
    List<Object> queuedCommands = new ArrayList<>();


    public SlashComandDispatcher queue(Object... cmd) {
        for(Object each : cmd) {
            queuedCommands.add(each);
        }
        return this;
    }



    public CompletableFuture<List<SlashCommand>> submit() {

        // The listener is a singleton so lazy-create it if
        // it has not yet been created then attach it to the api
        if(listener == null) {
            listener = new SlashCommandListener();
            api.addSlashCommandCreateListener(listener);
        }

        SlashCommandRegister registry = new SlashCommandRegister();
        for(Object queued : queuedCommands) {
            // Find command defs and add it to the registry queue
            if(queued instanceof Command) {
                registry.queue((Command) queued);
            }

            // the listener is a beast and can scan any object without issue
            listener.addListener(queued);;
        }

        queuedCommands.clear(); // remove all that has been queued up to prevent double inserts

        return registry.upsert(api);
    }

}
