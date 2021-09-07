package com.bbaker.slashcord.structure.entity;

import org.javacord.api.interaction.SlashCommandOptionType;

public class RoleOption extends InputOption {

    public RoleOption(String name, String description, boolean required) {
        super(name, description, required, SlashCommandOptionType.ROLE);
    }

}
