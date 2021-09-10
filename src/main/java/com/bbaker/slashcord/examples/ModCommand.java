package com.bbaker.slashcord.examples;

import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;

import com.bbaker.slashcord.handler.annotation.Slash;
import com.bbaker.slashcord.handler.annotation.SlashOption;

public class ModCommand {

    @Slash( command = "mod", group = "add", sub = "role")
    public MessageBuilder addRole(@SlashOption("user") User user, @SlashOption("role") Role role) {
        return new MessageBuilder()
                .append("Adding ").append(user)
                .append(" to ").append(role);
    }

    @Slash( command = "mod", group = "add", sub = "channel")
    public String addUser(@SlashOption("user") User user, @SlashOption("channel") ServerChannel channel) {
        return "Adding " + user.getName() + " to " + channel.getName();
    }

    @Slash( command = "mod", group = "remove", sub = "role")
    public String removeRole(User user, Role role) {
        return "Removing " + user.getName() + " from " + role.getName();
    }

    @Slash( command = "mod", group = "remove", sub = "role")
    public String removeChannel(User user, ServerChannel channel) {
        return "Removing " + user.getName() + " from " + channel.getName();
    }
}
