package com.bbaker.slashcord.handler.args;

import org.javacord.api.entity.Mentionable;
import org.javacord.api.interaction.SlashCommandInteraction;

public class MentionableOptionArgument extends AbstractOptionArgument {

    public MentionableOptionArgument(String name) {
        super(name);
    }

    @Override
    public Mentionable apply(SlashCommandInteraction sci) {
        return sci.getOptionMentionableValueByName(name).orElse(null);
    }

}
