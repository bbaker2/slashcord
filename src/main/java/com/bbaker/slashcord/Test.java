package com.bbaker.slashcord;

import java.util.List;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.interaction.SlashCommand;

import com.bbaker.slashcord.structure.entity.CommandTierI;
import com.bbaker.slashcord.structure.entity.IntOption;
import com.bbaker.slashcord.structure.registry.SlashCommandRegister;

public class Test {

    public static void main(String...args) {

        CommandTierI fizzbuzz = new CommandTierI("fizzbuzz", "Fizz if divisible by 3, Buzz if divisible by 5");
        IntOption number = new IntOption("number", "Any whole number", true);
        fizzbuzz.addOption(number);

        DiscordApi api = new DiscordApiBuilder().setToken(args[0]).login().join();

        SlashCommandRegister register = new SlashCommandRegister();
        register.queue(fizzbuzz);

        List<SlashCommand> upserted = register.upsert(api).join();
        System.out.print(upserted.size());

    }
}
