package com.bbaker.slashcord.handler.response;

import java.util.function.BiConsumer;

import org.javacord.api.interaction.SlashCommandInteraction;

public class VoidResponse implements BiConsumer<Object, SlashCommandInteraction> {

    @Override
    public void accept(Object t, SlashCommandInteraction sci) {
        /* no operation */
    }

}
