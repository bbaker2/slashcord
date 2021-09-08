package com.bbaker.slashcord.handler.args;

import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;

public class UserOptionArgument extends AbstractOptionArgument {

    public UserOptionArgument(String name) {
        super(name);
    }

    @Override
    public User apply(SlashCommandInteraction sci) {
        return sci.getOptionUserValueByName(name).orElse(null);
    }

}
