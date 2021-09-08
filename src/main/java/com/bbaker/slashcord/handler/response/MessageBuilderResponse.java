package com.bbaker.slashcord.handler.response;

import java.util.function.BiConsumer;

import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

public class MessageBuilderResponse  implements BiConsumer<Object, SlashCommandInteraction>  {

    @Override
    public void accept(Object value, SlashCommandInteraction sci) {
        MessageBuilder mb = (MessageBuilder) value;
        sci.getChannel().ifPresent(channel -> mb.send(channel));
    }

}
