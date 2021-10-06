package com.bbaker.slashcord.structure;

import java.util.List;

import org.javacord.api.entity.server.Server;

import com.bbaker.slashcord.structure.entity.Command;

class MetaImpl<B> implements Meta<B> {
    private B builder;
    private Command def;
    private List<Server> servers = null;

    public MetaImpl(B trg, Command src) {
        this.def = src;
        this.builder = trg;
        this.servers = null; // global
    }

    @Override
    public B getBuilder() {
        return builder;
    }
    @Override
    public Command getCommand() {
        return def;
    }
    @Override
    public List<Server> getServers() {
        return servers;
    }
}
