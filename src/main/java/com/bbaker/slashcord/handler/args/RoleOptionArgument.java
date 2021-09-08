package com.bbaker.slashcord.handler.args;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.interaction.SlashCommandInteraction;

public class RoleOptionArgument extends AbstractOptionArgument {

    public RoleOptionArgument(String name) {
        super(name);
    }

    @Override
    public Role apply(SlashCommandInteraction sci) {
        return sci.getOptionRoleValueByName(name).orElse(null);
    }

}
