package com.bbaker.slashcord.structure.entity;

import org.javacord.api.interaction.SlashCommandOptionType;

public class MentionalbleOption extends InputOption {

    public MentionalbleOption(String name, String description, boolean required) {
        super(name, description, required, SlashCommandOptionType.MENTIONABLE);
    }

}
