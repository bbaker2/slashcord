package com.bbaker.slashcord.structure;

import java.util.List;

import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandUpdater;

public interface Upsert {
    public List<Meta<SlashCommandBuilder>> getInserts();
    public List<Meta<SlashCommandUpdater>> getUpdates();
    public List<Meta<SlashCommand>> getSkips();
}
