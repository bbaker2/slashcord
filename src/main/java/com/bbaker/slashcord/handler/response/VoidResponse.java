package com.bbaker.slashcord.handler.response;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import org.javacord.api.interaction.SlashCommandInteraction;

public class VoidResponse implements BiConsumer<CompletableFuture<Object>, SlashCommandInteraction> {

    @Override
    public void accept(CompletableFuture<Object> response, SlashCommandInteraction sci) {
        /* no op */
    }


}
