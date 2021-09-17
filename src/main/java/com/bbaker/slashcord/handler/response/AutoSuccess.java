package com.bbaker.slashcord.handler.response;

import java.util.function.BiConsumer;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.interaction.SlashCommandInteraction;

public class AutoSuccess implements BiConsumer<Object, SlashCommandInteraction> {

    @Override
    public void accept(Object t, SlashCommandInteraction sci) {
        sci.createImmediateResponder()
            .setFlags(MessageFlag.EPHEMERAL)
            .append("/")
            .append(sci.getCommandName())
            .append(" successful").respond();
    }



}
