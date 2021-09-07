package com.bbaker.slashcord;

import com.bbaker.slashcord.structure.entity.ChannelOption;
import com.bbaker.slashcord.structure.entity.Command;
import com.bbaker.slashcord.structure.entity.CommandTierI;
import com.bbaker.slashcord.structure.entity.CommandTierII;
import com.bbaker.slashcord.structure.entity.CommandTierIII;
import com.bbaker.slashcord.structure.entity.GroupOption;
import com.bbaker.slashcord.structure.entity.InputOption;
import com.bbaker.slashcord.structure.entity.IntOption;
import com.bbaker.slashcord.structure.entity.RoleOption;
import com.bbaker.slashcord.structure.entity.SubOption;
import com.bbaker.slashcord.structure.entity.UserOption;

public class Test {

    public static void main(String...args) {

        Command regular = new CommandTierI("fizzbuzz", "Prints 3 or 5").addOption(new IntOption("number", "any whole number", true));

        InputOption role = new RoleOption("role", "The desired role", true);
        Command subCommand = new CommandTierII("role", "Add or remove a role for yourself")
                .addOption(
                        new SubOption("add", "Add a role to yourself")			.addOptions(role),
                        new SubOption("remove", "Remove yourself from a role")	.addOptions(role));

        Command nestedCommand = new CommandTierIII("nested", "options").addOption(
                new GroupOption("remove", "remove something").addOptions(
                        new SubOption("user", "remove a user").addOptions(
                                new UserOption("user", "the user to remove", true)
                        ),
                        new SubOption("channel", "remove a channel").addOptions(
                                new ChannelOption("channel", "the target channel", true)
                        )
                )
        );

    }
}
