package com.bbaker.slashcord.handler.args;

import org.javacord.api.interaction.SlashCommandInteraction;

public class BooleanOptionArgument extends AbstractOptionArgument {

    public BooleanOptionArgument(String name) {
        super(name);
    }

    @Override
    public Boolean apply(SlashCommandInteraction sci) {
        return sci.getOptionBooleanValueByName(name).orElse(null);
    }

}
