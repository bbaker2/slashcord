package com.bbaker.slashcord.structure.entity;

import java.util.ArrayList;

import org.javacord.api.interaction.SlashCommandOptionType;

public class InputOption extends Option {

    protected InputOption(String name, String description, boolean required, SlashCommandOptionType type) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.choices = new ArrayList<>();
        this.options = new ArrayList<>();
        this.required = required;
    }

}
