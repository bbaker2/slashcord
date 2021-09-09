package com.bbaker.slashcord;

import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;

import com.bbaker.slashcord.handler.annotation.Slash;

public class ModCommand {

    @Slash( command = "mod", group = "add", sub = "role")
    public String addRole(User user, Role role) {
        return "Adding " + user.getName() + " to " + role.getName();
    }

    @Slash( command = "mod", group = "add", sub = "channel")
    public String addUser(User user, ServerChannel channel) {
        return "Adding " + user.getName() + " to " + channel.getName();
    }

    @Slash( command = "mod", group = "remove", sub = "role")
    public String removeRole(User user, Role role) {
        return "Removing " + user.getName() + " from " + role.getName();
    }

    @Slash( command = "mod", group = "add", sub = "role")
    public String removeChannel(User user, ServerChannel channel) {
        return "Removing " + user.getName() + " from " + channel.getName();
    }
}
