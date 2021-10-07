package com.bbaker.slashcord.structure;

import java.util.ArrayList;
import java.util.List;

import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandUpdater;

import com.bbaker.slashcord.structure.entity.Command;
import com.bbaker.slashcord.util.ConverterUtil;

class UpsertImpl implements Upsert {
    private List<MetaImpl<SlashCommandBuilder>> toInsert = new ArrayList<>();
    private List<MetaImpl<SlashCommandUpdater>> toUpdate = new ArrayList<>();
    private List<MetaImpl<SlashCommand>> toSkip   = new ArrayList<>();

    @Override
    public List<Meta<SlashCommandBuilder>> getInserts() {
        return toInsert;
    }

    public void insert(Command cmdDef, List<Server> servers) {
        SlashCommandBuilder insert = ConverterUtil.from(cmdDef);
        toInsert.add(new MetaImpl<SlashCommandBuilder>(insert, cmdDef, servers));
    }

    @Override
    public List<Meta<SlashCommandUpdater>> getUpdates() {
        return toUpdate;
    }

    public void update(Command cmdDef, long commandId, List<Server> servers) {
        SlashCommandUpdater update = ConverterUtil.from(cmdDef, commandId);
        toUpdate.add(new MetaImpl<SlashCommandUpdater>(update, cmdDef, servers));
    }

    @Override
    public List<Meta<SlashCommand>> getSkips() {
        return toSkip;
    }

    public void skip(Command cmdDef, SlashCommand skipped, List<Server> servers) {
        toSkip.add(new MetaImpl<SlashCommand>(skipped, cmdDef, servers));
    }
}