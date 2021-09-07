package com.bbaker.slashcord.structure.entity;

import static com.bbaker.slashcord.structure.util.CompareUtil.equalLists;

import java.util.List;
import java.util.Objects;

import org.javacord.api.interaction.SlashCommandOptionType;

public class Option {

    protected String name;
    protected String description;
    protected SlashCommandOptionType type;
    protected boolean required;
    protected List<Option> options;
    protected List<Choice> choices;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public SlashCommandOptionType getType() {
        return type;
    }

    public boolean isRequired() {
        return required;
    }

    public List<Option> getOptions() {
        return options;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Option)) {
            return super.equals(o);
        }

        Option that = (Option)o;

        if(!Objects.equals(this.getName(), that.getName())) {
            return false;
        }

        if(!Objects.equals(this.getDescription(), that.getDescription())) {
            return false;
        }

        if(!Objects.equals(this.getType(), that.getType())) {
            return false;
        }

        if(!equalLists(this.getChoices(), that.getChoices())) {
            return false;
        }

        if(!equalLists(this.getOptions(), that.getOptions())) {
            return false;
        }


        return true;
    }

}
