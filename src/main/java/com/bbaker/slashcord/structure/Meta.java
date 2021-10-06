package com.bbaker.slashcord.structure;

import java.util.List;

import org.javacord.api.entity.server.Server;

import com.bbaker.slashcord.structure.entity.Command;

public interface Meta<B> {
    public B getBuilder();
    public Command getCommand();
    public List<Server> getServers();

}
