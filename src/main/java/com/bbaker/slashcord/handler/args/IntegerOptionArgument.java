package com.bbaker.slashcord.handler.args;

import org.javacord.api.interaction.SlashCommandInteractionOptionsProvider;

public class IntegerOptionArgument extends AbstractOptionArgument<Integer> {


    public IntegerOptionArgument(String name) {
        super(name);
    }

    @Override
    protected Integer getValue(SlashCommandInteractionOptionsProvider interaction) {
        return interaction.getOptionIntValueByName(name).orElse(null);
    }

}
