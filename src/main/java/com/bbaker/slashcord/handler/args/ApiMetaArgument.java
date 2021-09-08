package com.bbaker.slashcord.handler.args;

import java.util.function.Function;

import org.javacord.api.interaction.SlashCommandInteraction;

public class ApiMetaArgument implements Function<SlashCommandInteraction, Object> {

    @Override
    public Object apply(SlashCommandInteraction sci) {
        return sci.getApi();
    }

}
