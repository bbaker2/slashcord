package com.bbaker.slashcord.handler.response;

import static com.bbaker.slashcord.structure.util.CommonsUtil.*;

import java.util.function.BiConsumer;

import org.javacord.api.interaction.SlashCommandInteraction;

public class StringResponse implements BiConsumer<Object, SlashCommandInteraction> {

    @Override
    public void accept(Object value, SlashCommandInteraction sci) {
        String response = (String)value;
        if(isBlank(response)) {
            sci.createImmediateResponder().respond();
        } else {
            sci.createImmediateResponder().append(response).respond();
        }
    }

}
