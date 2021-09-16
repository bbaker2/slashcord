package com.bbaker.slashcord.structure.results;

import java.util.List;

import org.javacord.api.interaction.SlashCommand;

import com.bbaker.slashcord.structure.entity.Command;

public interface UpsertResult {

    public SlashCommand getCommand();

    public Command getDefinition();

    public Operation getOperartion();

    public boolean successful();

    public List<Error> getMessage();

    default public boolean unsuccessful() {
        return !successful();
    }

}
