package com.bbaker.slashcord.handler.args;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;

public class InteractionOptionArgument extends AbstractOptionArgument {

    public InteractionOptionArgument(String name) {
        super(name);
    }

    @Override
    public SlashCommandInteractionOption apply(SlashCommandInteraction sci) {
        return sci.getOptionByName(name).orElse(null);
    }

}
