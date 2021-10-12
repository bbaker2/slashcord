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
    private List<Meta<SlashCommandBuilder>> toInsert = new ArrayList<>();
    private List<Meta<SlashCommandUpdater>> toUpdate = new ArrayList<>();
    private List<Meta<SlashCommand>> toSkip   = new ArrayList<>();

    @Override
    public List<Meta<SlashCommandBuilder>> getInserts() {
        return toInsert;
    }

    public void insert(Command cmdDef, Server server) {
        if(updateServerList(toInsert, cmdDef, server)) {
            return;
        }

        // otherwise, insert a new entry
        SlashCommandBuilder insert = ConverterUtil.from(cmdDef);
        List<Server> servers = new ArrayList<>(1);
        servers.add(server);
        toInsert.add(new MetaImpl<SlashCommandBuilder>(insert, cmdDef, servers));
    }

    @Override
    public List<Meta<SlashCommandUpdater>> getUpdates() {
        return toUpdate;
    }

    public void update(Command cmdDef, long commandId, Server server) {
        if(updateServerList(toUpdate, cmdDef, server)) {
            return;
        }

        SlashCommandUpdater update = ConverterUtil.from(cmdDef, commandId);
        List<Server> servers = new ArrayList<>(1);
        servers.add(server);
        toUpdate.add(new MetaImpl<SlashCommandUpdater>(update, cmdDef, servers));
    }

    @Override
    public List<Meta<SlashCommand>> getSkips() {
        return toSkip;
    }

    public void skip(Command cmdDef, SlashCommand skipped, Server server) {
        if(updateServerList(toUpdate, cmdDef, server)) {
            return;
        }

        List<Server> servers = new ArrayList<>(1);
        servers.add(server);
        toSkip.add(new MetaImpl<SlashCommand>(skipped, cmdDef, servers));
    }

    private <T> boolean updateServerList(List<Meta<T>> metaList, Command newCmd, Server server) {
        // check if we're just updating a new entry or creating a new entry
        for(Meta<?> each : metaList) {
            // we can assume if the name matches, the rest matches at this point
            if(each.getCommand().getName().equals(newCmd.getName())) {
                // make sure the server does not already exist as an entry
                if(contains(each.getServers(), server)) {
                    return true;
                }
                // otherwise update the server list
                each.getServers().add(server);
                return true;
            }
        }

        return false;
    }

    private boolean contains(List<Server> these, Server that) {
        for(Server each : these) {
            if(each.getId() == that.getId()) {
                return true;
            }
        }
        return false;
    }
}