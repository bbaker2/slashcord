package com.bbaker.slashcord.structure;

import java.util.ArrayList;
import java.util.List;

import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandUpdater;

import com.bbaker.slashcord.structure.entity.Command;
import com.bbaker.slashcord.util.ConverterUtil;

class UpsertImpl implements Upsert {
    private List<Meta<SlashCommandBuilder>> toInsert = new ArrayList<>();
    private List<Meta<SlashCommandUpdater>> toUpdate = new ArrayList<>();
    private List<Meta<SlashCommand>> toSkip   = new ArrayList<>();

    @Override
    public List<Meta<SlashCommandBuilder>> getInserts() {
        return toInsert;
    }

    public void insert(Command cmdDef) {
        SlashCommandBuilder insert = ConverterUtil.from(cmdDef);
        toInsert.add(new MetaImpl<SlashCommandBuilder>(insert, cmdDef));
    }

    @Override
    public List<Meta<SlashCommandUpdater>> getUpdates() {
        return toUpdate;
    }

    public void update(Command cmdDef, long commandId) {
        SlashCommandUpdater update = ConverterUtil.from(cmdDef, commandId);
        toUpdate.add(new MetaImpl<SlashCommandUpdater>(update, cmdDef));
    }

    @Override
    public List<Meta<SlashCommand>> getSkips() {
        return toSkip;
    }

    public void skip(Command cmdDef, SlashCommand skipped) {
        toSkip.add(new MetaImpl<SlashCommand>(skipped, cmdDef));
    }
}