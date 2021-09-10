package com.bbaker.slashcord.examples;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import com.bbaker.slashcord.handler.SlashCommandListener;
import com.bbaker.slashcord.structure.SlashCommandRegister;
import com.bbaker.slashcord.structure.entity.ChannelOption;
import com.bbaker.slashcord.structure.entity.Command;
import com.bbaker.slashcord.structure.entity.CommandTierI;
import com.bbaker.slashcord.structure.entity.CommandTierII;
import com.bbaker.slashcord.structure.entity.CommandTierIII;
import com.bbaker.slashcord.structure.entity.GroupOption;
import com.bbaker.slashcord.structure.entity.InputOption;
import com.bbaker.slashcord.structure.entity.IntOption;
import com.bbaker.slashcord.structure.entity.RoleOption;
import com.bbaker.slashcord.structure.entity.StringOption;
import com.bbaker.slashcord.structure.entity.SubOption;
import com.bbaker.slashcord.structure.entity.UserOption;

public class Test {

    public static void main(String...args) {

        DiscordApi api = new DiscordApiBuilder().setToken(args[0]).login().join();

        SlashCommandRegister register = new SlashCommandRegister();
        register.queue(createPingPong());
        register.queue(createFizzBuzzCommand());
        register.queue(createModsCommand());
        register.queue(createQuoteCommand());
        register.upsert(api).join();

        SlashCommandListener listener = new SlashCommandListener();
        listener.addListener(new PingPongCommand());
        listener.addListener(new FizzBuzz());
        listener.addListener(new QuoteCommand());
        listener.addListener(new ModCommand());
        api.addSlashCommandCreateListener(listener);
    }

    public static Command createPingPong() {
        return new CommandTierI("ping", "Will Ping");
    }

    public static Command createFizzBuzzCommand() {
         return new CommandTierI("fizzbuzz", "Fizz if divisible by 3, Buzz if divisible by 5")
             .addOption(
                 new IntOption("number", "Any whole number", true)
         );
    }

    public static Command createQuoteCommand() {
        CommandTierII quote = new CommandTierII("quote", "For quoting funny things in the server");
        quote.addOption(
            new SubOption("add", "Add a quote").addOptions(
                new StringOption("quote", "The quote itself", true),
                new UserOption("user", "Who said the quote?", false)
            ),
            new SubOption("random", "Output a random quote")
        );

        return quote;
    }


    public static Command createModsCommand() {
        CommandTierIII mod = new CommandTierIII("mod", 		"Useful commands for the server mods");
        InputOption user    = new UserOption("user", 		"The target user", 		true);
        InputOption channel = new ChannelOption("channel", 	"The target channel", 	true);
        InputOption role    = new RoleOption("role", 		"The target role", 		true);

        mod.addOption(
            new GroupOption("add", "Append a role to a user, or a user to a channel")
                .addOptions(new SubOption("role",    "Give a user a role")          .addOptions(user, role))
                .addOptions(new SubOption("channel", "Add a user to a channel")     .addOptions(user, channel))
        );
        mod.addOption(
            new GroupOption("remove", "Remove a role from a user, or a user from a channel")
                .addOptions(new SubOption("role",    "Remove a role from a user")   .addOptions(user, role))
                .addOptions(new SubOption("channel", "Remove a user from a channel").addOptions(user, channel))
        );

        return mod;
    }

}
