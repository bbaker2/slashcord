package com.bbaker.slashcord.structure.entity;

import org.javacord.api.interaction.SlashCommandOptionType;

public class UserOption extends InputOption {

    public UserOption(String name, String description, boolean required) {
        super(name, description, required, SlashCommandOptionType.USER);
    }

}
