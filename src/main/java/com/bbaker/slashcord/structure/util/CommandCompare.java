package com.bbaker.slashcord.structure.util;

import static com.bbaker.slashcord.structure.util.CompareUtil.*;

import java.util.Comparator;
import java.util.Objects;

import org.javacord.api.interaction.SlashCommand;

public class CommandCompare implements Comparator<SlashCommand> {

    @Override
    public int compare(SlashCommand a, SlashCommand b) {
        if(!Objects.equals(a.getName(), b.getName())) {
            return -1;
        }

        if(!Objects.equals(a.getDescription(), b.getDescription())) {
            return -1;
        }

       if(!equalLists(a.getOptions(), b.getOptions(), new OptionCompare())) {
           return -1;
       }

       return 0;
    }

}
