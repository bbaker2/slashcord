package com.bbaker.slashcord.examples;

import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.message.MessageBuilder;

import com.bbaker.slashcord.handler.annotation.Slash;
import com.bbaker.slashcord.handler.annotation.SlashOption;
import com.bbaker.slashcord.structure.annotation.CommandDef;
import com.bbaker.slashcord.structure.annotation.GroupCommandDef;
import com.bbaker.slashcord.structure.annotation.OptionDef;
import com.bbaker.slashcord.structure.annotation.SubCommandDef;
import com.bbaker.slashcord.structure.entity.ChannelOption;
import com.bbaker.slashcord.structure.entity.RoleOption;
import com.bbaker.slashcord.structure.entity.UserOption;

/*
 * While you CAN define a nested command with groups this way....
 * It gets a little tall very quickly. It is recommended you go the class approach
 * with the CommandTierIII instead.
 *
 * But you, the developer, can choose which style you like best. They both do the same thing
 */
@GroupCommandDef(
    name = "mod",
    description = "Useful commands for the server mods",
    groups = {
        @SubCommandDef(
            name = "add",
            description = "Append a role to a user, or a user to a channel",
            subs = {
                @CommandDef(
                    name = "role",
                    description = "Give a user a role",
                    options = {
                        @OptionDef(ModCommand.User.class),
                        @OptionDef(ModCommand.Role.class)
                    }
                ),
                @CommandDef(
                    name = "channel",
                    description = "Add a user to a channel",
                    options = {
                        @OptionDef(ModCommand.User.class),
                        @OptionDef(ModCommand.Channel.class)
                    }
                )
            }
        ),
        @SubCommandDef(
            name = "remove",
            description = "Remove a role from a user, or a user from a channel",
            subs = {
                @CommandDef(
                    name = "role",
                    description = "Remove a role from a user",
                    options = {
                        @OptionDef(ModCommand.User.class),
                        @OptionDef(ModCommand.Role.class)
                    }
                ),
                @CommandDef(
                    name = "channel",
                    description = "Remove a user from a channel",
                    options = {
                        @OptionDef(ModCommand.User.class),
                        @OptionDef(ModCommand.Channel.class)
                    }
                )
            }
        )
    }
)
public class ModCommand {

    private static final String ROLE_OPT = "role";
    private static final String USER_OPT = "user";
    private static final String CHANNEL_OPT = "channel";

    @Slash( command = "mod", group = "add", sub = ROLE_OPT)
    public MessageBuilder addRole(
            @SlashOption(USER_OPT) User user,
            @SlashOption(ROLE_OPT) Role role) {
        return new MessageBuilder()
                .append("Adding ").append(user)
                .append(" to ").append(role);
    }

    @Slash( command = "mod", group = "add", sub = "channel")
    public String addUser(
            @SlashOption(USER_OPT) User user,
            @SlashOption(CHANNEL_OPT) ServerChannel channel) {
        return "Adding " + user.getName() + " to " + channel.getName();
    }

    @Slash( command = "mod", group = "remove", sub = ROLE_OPT)
    public String removeRole(
            @SlashOption(USER_OPT) User user,
            @SlashOption(ROLE_OPT) Role role) {
        return "Removing " + user.getName() + " from " + role.getName();
    }

    @Slash( command = "mod", group = "remove", sub = "channel")
    public String removeChannel(
            @SlashOption(USER_OPT) User user,
            @SlashOption(CHANNEL_OPT) ServerChannel channel) {
        return "Removing " + user.getName() + " from " + channel.getName();
    }

    public static class User extends UserOption {
        // MUST SUPPORT DEFAULT CONSTRUCTOR FOR THIS TO WORK
        public User() {
            super(USER_OPT, "The target user", true);
        }
    }

    public static class Role extends RoleOption {
        // MUST SUPPORT DEFAULT CONSTRUCTOR FOR THIS TO WORK
        public Role() {
            super(ROLE_OPT, "The target role", true);
        }
    }

    public static class Channel extends ChannelOption {
        // MUST SUPPORT DEFAULT CONSTRUCTOR FOR THIS TO WORK
        public Channel() {
            super(CHANNEL_OPT, "The target channel", true);
        }
    }
}
