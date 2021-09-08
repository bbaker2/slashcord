package com.bbaker.slashcord;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import com.bbaker.slashcord.handler.dispatcher.AnnotationCommandListener;
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

        register.upsert(api).join();


        AnnotationCommandListener listener = new AnnotationCommandListener();
        listener.addListener(new FizzBuzz());
        api.addSlashCommandCreateListener(listener);

    }

}
