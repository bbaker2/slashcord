package com.bbaker.slashcord.structure.entity;

import org.javacord.api.interaction.SlashCommandOptionType;

public class IntOption extends ChoicableOption<IntChoice> {

    public IntOption(String name, String description, boolean required) {
        super(name, description, required, SlashCommandOptionType.INTEGER);
    }

}
