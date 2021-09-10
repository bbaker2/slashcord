package com.bbaker.slashcord.examples;

import com.bbaker.slashcord.handler.annotation.Slash;

public class PingPongCommand {

    @Slash(command = "ping")
    public String ping() {
        return "pong";
    }

}
