package com.bbaker.slashcord.structure.entity;

import org.javacord.api.interaction.SlashCommandOptionType;

public class ChannelOption extends InputOption {

    public ChannelOption(String name, String description, boolean required) {
        super(name, description, required, SlashCommandOptionType.CHANNEL);
    }

}
