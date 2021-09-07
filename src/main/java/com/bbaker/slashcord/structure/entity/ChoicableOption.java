package com.bbaker.slashcord.structure.entity;

import org.javacord.api.interaction.SlashCommandOptionType;

public class ChoicableOption<C extends Choice> extends InputOption {

    protected ChoicableOption(String name, String description, boolean required, SlashCommandOptionType type) {
        super(name, description, required, type);
    }

    public ChoicableOption<C> appendChoice(C... choices) {
        for(C choice : choices) {
            this.choices.add(choice);
        }
        return this;
    }

}
