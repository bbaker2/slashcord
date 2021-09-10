package com.bbaker.slashcord.handler.args;

import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteractionOptionsProvider;

public class UserOptionArgument extends AbstractOptionArgument<User> {

    public UserOptionArgument(String name) {
        super(name);
    }

    @Override
    protected User getValue(SlashCommandInteractionOptionsProvider interaction) {
        return interaction.getOptionUserValueByName(name).orElse(null);
    }

}
