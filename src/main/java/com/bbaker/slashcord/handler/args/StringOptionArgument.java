package com.bbaker.slashcord.handler.args;

import org.javacord.api.interaction.SlashCommandInteraction;

public class StringOptionArgument extends AbstractOptionArgument {

    public StringOptionArgument(String name) {
        super(name);
    }

    @Override
    public String apply(SlashCommandInteraction sci) {
        return sci.getOptionStringValueByName(name).orElse(null);
    }

}
