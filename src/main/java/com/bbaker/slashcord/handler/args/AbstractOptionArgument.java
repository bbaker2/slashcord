package com.bbaker.slashcord.handler.args;

import java.util.function.Function;

import org.javacord.api.interaction.SlashCommandInteraction;

public abstract class AbstractOptionArgument implements Function<SlashCommandInteraction, Object> {

    protected String name;

    protected AbstractOptionArgument(String name) {
        this.name = name;
    }

}
