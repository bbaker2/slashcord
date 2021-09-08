package com.bbaker.slashcord.handler.args;

import org.javacord.api.interaction.SlashCommandInteraction;

public class IntegerOptionArgument extends AbstractOptionArgument {


    public IntegerOptionArgument(String name) {
        super(name);
    }

    @Override
    public Integer apply(SlashCommandInteraction sci) {
        return sci.getOptionIntValueByName(name).orElse(null);
    }

}
