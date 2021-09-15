package com.bbaker.slashcord.handler.response;

import java.util.function.BiConsumer;

import org.javacord.api.interaction.SlashCommandInteraction;

public class ToStringResponse  implements BiConsumer<Object, SlashCommandInteraction>  {

    @Override
    public void accept(Object value, SlashCommandInteraction sci) {
        sci.createImmediateResponder().append(value).respond();
    }

}
