package com.bbaker.slashcord.structure.entity;

import java.util.List;

import org.javacord.api.interaction.SlashCommandOptionType;

public class Option {

    private String name;
    private String description;
    private SlashCommandOptionType type;
    private boolean required;
    private List<Option> options;
    private List<Choice> choices;

}
