package com.bbaker.slashcord.structure.util;

import java.util.Comparator;
import java.util.Objects;

import org.javacord.api.interaction.SlashCommandOptionChoice;

public class ChoiceCompare implements Comparator<SlashCommandOptionChoice> {

    @Override
    public int compare(SlashCommandOptionChoice a, SlashCommandOptionChoice b) {
        if(!Objects.equals(a.getName(), b.getName())) {
            return -1;
        }

        if(a.getStringValue().isPresent() ^ b.getStringValue().isPresent()) {
            return -1;
        }

        if(b.getIntValue().isPresent() ^ b.getIntValue().isPresent()) {
            return -1;
        }

        if(!a.getValueAsString().equals(b.getValueAsString())) {
            return -1;
        }
        return 0;
    }




}
