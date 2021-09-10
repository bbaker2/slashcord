package com.bbaker.slashcord.handler.args;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.interaction.SlashCommandInteractionOptionsProvider;

public class RoleOptionArgument extends AbstractOptionArgument<Role> {

    public RoleOptionArgument(String name) {
        super(name);
    }

    @Override
    protected Role getValue(SlashCommandInteractionOptionsProvider interaction) {
        return interaction.getOptionRoleValueByName(name).orElse(null);
    }

}
