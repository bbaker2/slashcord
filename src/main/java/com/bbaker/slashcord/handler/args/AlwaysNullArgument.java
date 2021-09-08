package com.bbaker.slashcord.handler.args;

import java.util.function.Function;

import org.javacord.api.interaction.SlashCommandInteraction;

public class AlwaysNullArgument implements Function<SlashCommandInteraction, Object> {

    @Override
    public Object apply(SlashCommandInteraction t) {
        return null;
    }

}
