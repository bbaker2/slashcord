package com.bbaker.slashcord.structure.entity;

import static com.bbaker.slashcord.util.CompareUtil.equalLists;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

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

        if(this.isRequired() != that.isRequired()) {
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

    @Override
    public String toString() {
        return toString("");
    }

    public String toString(String leftPad) {
        StringBuilder sb = new StringBuilder();
        sb.append(format("%s%s (%s, %b)", leftPad, getName(), getType(), isRequired()));

        if(getChoices() != null && !getChoices().isEmpty()) {
            sb.append(" [");
            sb.append(
                getChoices().stream()
                    .map(Choice::toString)
                    .collect(joining(", "))
            );
            sb.append("]");
        }

        if(getOptions() != null && !getOptions().isEmpty()) {
            sb.append(System.lineSeparator());
            sb.append(
                getOptions().stream()
                    .map(o -> o.toString(leftPad + "  "))
                    .collect(joining(System.lineSeparator()))
            );
        }
        return sb.toString();
    }

}
