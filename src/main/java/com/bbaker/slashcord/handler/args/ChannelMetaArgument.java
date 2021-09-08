package com.bbaker.slashcord.handler.args;

import java.util.function.Function;

import org.javacord.api.interaction.SlashCommandInteraction;

public class ChannelMetaArgument implements Function<SlashCommandInteraction, Object> {

    @Override
    public Object apply(SlashCommandInteraction sci) {
        return sci.getChannel().orElse(null);
    }

}
