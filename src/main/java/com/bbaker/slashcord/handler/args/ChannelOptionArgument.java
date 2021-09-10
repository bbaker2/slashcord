package com.bbaker.slashcord.handler.args;

import org.javacord.api.entity.channel.Channel;
import org.javacord.api.interaction.SlashCommandInteractionOptionsProvider;

public class ChannelOptionArgument extends AbstractOptionArgument<Channel> {

    public ChannelOptionArgument(String name) {
        super(name);
    }

    @Override
    protected Channel getValue(SlashCommandInteractionOptionsProvider interaction) {
        return interaction.getOptionChannelValueByName(name).orElse(null);
    }

}
