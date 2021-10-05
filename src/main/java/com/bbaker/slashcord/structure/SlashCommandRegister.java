package com.bbaker.slashcord.structure;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.javacord.api.DiscordApi;

import com.bbaker.slashcord.structure.results.UpsertResult;

public interface SlashCommandRegister {

    SlashCommandRegister queue(Object command);

    CompletableFuture<List<UpsertResult>> upsert(DiscordApi api);

    public static SlashCommandRegister getInstance() {
        return new SlashCommandRegisterImpl();
    }

}
