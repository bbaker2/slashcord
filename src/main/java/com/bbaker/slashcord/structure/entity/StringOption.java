package com.bbaker.slashcord.structure.entity;

import org.javacord.api.interaction.SlashCommandOptionType;

public class StringOption extends ChoicableOption<StringChoice> {

    public StringOption(String name, String description, boolean required) {
        super(name, description, required, SlashCommandOptionType.STRING);
    }

}
