package com.bbaker.slashcord.examples;

import com.bbaker.slashcord.handler.annotation.Slash;
import com.bbaker.slashcord.structure.annotation.CommandDef;
import com.bbaker.slashcord.structure.entity.Command;
import com.bbaker.slashcord.structure.entity.RegularCommand;

@CommandDef(name = "ping", description = "Will pong if pinged")
public class PingPongCommand {

    @Slash(command = "ping")
    public String ping() {
        return "pong";
    }

    public static Command createPingPongDef() {
        return new RegularCommand("ping", "Will Ping");
    }

}
