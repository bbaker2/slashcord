package com.bbaker.slashcord.handler.args;

import org.javacord.api.entity.channel.Channel;
import org.javacord.api.interaction.SlashCommandInteraction;

public class ChannelOptionArgument extends AbstractOptionArgument {

    public ChannelOptionArgument(String name) {
        super(name);
    }

    @Override
    public Channel apply(SlashCommandInteraction sci) {
        return sci.getOptionChannelValueByName(name).orElse(null);
    }

}
