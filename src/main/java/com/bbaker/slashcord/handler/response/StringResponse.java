package com.bbaker.slashcord.handler.response;

import java.util.function.BiConsumer;

import org.javacord.api.interaction.SlashCommandInteraction;

public class StringResponse implements BiConsumer<Object, SlashCommandInteraction> {

    @Override
    public void accept(Object value, SlashCommandInteraction sci) {
        String response = (String)value;
        if(response == null || response.isBlank()) {
            sci.createImmediateResponder().respond();
        } else {
            sci.createImmediateResponder().append(response).respond();
        }
    }

}
