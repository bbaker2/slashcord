package com.bbaker.slashcord.handler.args;

import org.javacord.api.interaction.SlashCommandInteractionOptionsProvider;

public class StringOptionArgument extends AbstractOptionArgument<String> {

    public StringOptionArgument(String name) {
        super(name);
    }


    @Override
    protected String getValue(SlashCommandInteractionOptionsProvider interaction) {
        return interaction.getOptionStringValueByName(name).orElse(null);
    }

}
