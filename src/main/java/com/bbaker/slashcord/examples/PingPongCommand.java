package com.bbaker.slashcord.examples;

import com.bbaker.slashcord.handler.annotation.Slash;
import com.bbaker.slashcord.structure.annotation.CommandDef;

@CommandDef(name = "ping", description = "Will pong if pinged")
public class PingPongCommand {

    @Slash(command = "ping")
    public String ping() {
        return "pong";
    }

}
