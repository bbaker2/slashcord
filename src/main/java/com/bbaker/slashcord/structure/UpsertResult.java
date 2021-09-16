package com.bbaker.slashcord.structure;

import java.util.List;

import org.javacord.api.interaction.SlashCommand;

public interface UpsertResult {

    public SlashCommand getCommand();

    public Operation getOperartion();

    public boolean successful();

    public List<String> getMessage();

    default public boolean unsuccessful() {
        return !successful();
    }

}
