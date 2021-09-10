package com.bbaker.slashcord.handler.args;

import org.javacord.api.entity.Mentionable;
import org.javacord.api.interaction.SlashCommandInteractionOptionsProvider;

public class MentionableOptionArgument extends AbstractOptionArgument<Mentionable> {

    public MentionableOptionArgument(String name) {
        super(name);
    }

    @Override
    protected Mentionable getValue(SlashCommandInteractionOptionsProvider interaction) {
        return interaction.getOptionMentionableValueByName(name).orElse(null);
    }

}
