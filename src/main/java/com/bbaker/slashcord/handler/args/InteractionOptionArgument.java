package com.bbaker.slashcord.handler.args;

import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.SlashCommandInteractionOptionsProvider;

public class InteractionOptionArgument extends AbstractOptionArgument<SlashCommandInteractionOption> {

    public InteractionOptionArgument(String name) {
        super(name);
    }

    @Override
    protected SlashCommandInteractionOption getValue(SlashCommandInteractionOptionsProvider interaction) {
        return interaction.getOptionByName(name).orElse(null);
    }

}
