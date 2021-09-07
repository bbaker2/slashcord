package com.bbaker.slashcord.structure.entity;

import org.javacord.api.interaction.SlashCommandOptionType;

public class BooleanOption extends InputOption {

    public BooleanOption(String name, String description, boolean required) {
        super(name, description, required, SlashCommandOptionType.BOOLEAN);
    }

}
