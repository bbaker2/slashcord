package com.bbaker.slashcord.dispatcher;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.javacord.api.DiscordApi;

import com.bbaker.slashcord.structure.results.UpsertResult;

public interface SlashCommandDispatcher {

    SlashCommandDispatcher queue(Object... cmd);

    CompletableFuture<List<UpsertResult>> submit();

    public static SlashCommandDispatcher getInstance(DiscordApi api) {
        return new SlashCommandDispatcherImpl(api);
    }

}
