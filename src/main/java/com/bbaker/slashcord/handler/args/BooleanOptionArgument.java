package com.bbaker.slashcord.handler.args;

import org.javacord.api.interaction.SlashCommandInteractionOptionsProvider;

public class BooleanOptionArgument extends AbstractOptionArgument<Boolean> {

    public BooleanOptionArgument(String name) {
        super(name);
    }

    @Override
    protected Boolean getValue(SlashCommandInteractionOptionsProvider interaction) {
        return interaction.getOptionBooleanValueByName(name).orElse(null);
    }

}
