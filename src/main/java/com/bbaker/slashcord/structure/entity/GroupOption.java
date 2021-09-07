package com.bbaker.slashcord.structure.entity;

import java.util.ArrayList;

import org.javacord.api.interaction.SlashCommandOptionType;

public class GroupOption extends Option {

    public GroupOption(String name, String description) {
        this.name = name;
        this.description = description;
        this.type = SlashCommandOptionType.SUB_COMMAND_GROUP;
        this.choices = null;
        this.options = new ArrayList<>();
        this.required = true;
    }

    public GroupOption addOptions(SubOption...options) {
        for(SubOption sub : options) {
            this.options.add(sub);
        }
        return this;
    }

}
