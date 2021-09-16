package com.bbaker.slashcord.examples;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.util.logging.ExceptionLogger;

import com.bbaker.slashcord.dispatcher.SlashCommandDispatcher;
import com.bbaker.slashcord.handler.SlashCommandListener;
import com.bbaker.slashcord.handler.annotation.Slash;
import com.bbaker.slashcord.structure.SlashCommandRegister;
import com.bbaker.slashcord.structure.annotation.CommandDef;
import com.bbaker.slashcord.structure.annotation.GroupCommandDef;
import com.bbaker.slashcord.structure.annotation.SubCommandDef;
import com.bbaker.slashcord.structure.entity.Command;

public class Test {

    public static void main(String...args) {
        DiscordApi api = new DiscordApiBuilder().setToken(args[0]).login().join();

        // just creating/updating commands
//        registerWithClasses(api);
//        registerWithAnnotations(api);

        // just responding to commands
//        registerWithAnnotations(api);

        // for when you and to do both with one method call
        everythingWithTheDispatcher(api);

        api.disconnect();

        // If you were to run this code, we would "register" the same command
        // over and over. Slashcord is actually smart enough to skip redundant
        // updates and will not double-respond to method handlers.

        // We went out of our way to make this library abuse proof

    }

    /**
     * The {@link SlashCommandDispatcher} does everything that {@link SlashCommandRegister} and
     * {@link SlashCommandListener} does. But as one useful class. If you're not sure which approach
     * to use, use this {@link SlashCommandDispatcher}.
     * @param api a live instance of Discord
     */
    public static void everythingWithTheDispatcher(DiscordApi api) {
        SlashCommandDispatcher dispatcher = new SlashCommandDispatcher(api);
        dispatcher.queue(new PingPongCommand());
        dispatcher.queue(new FizzBuzz());
        dispatcher.queue(new QuoteCommand());
        dispatcher.queue(new ModCommand());
        dispatcher.queue(new TodayCommand());
        dispatcher.submit().join().stream().forEach(System.out::println);
    }

    /**
     * This shows some static methods returning instances of the {@link Command}
     * class. Which is a pre-prepared in-memory command definition.
     * @param api a live instance of Discord
     */
    public static void registerWithClasses(DiscordApi api) {
        SlashCommandRegister register = new SlashCommandRegister();
        register.queue(PingPongCommand.createPingPongDef());
        register.queue(FizzBuzz.createFizzBuzzCommand());
        register.queue(QuoteCommand.createQuoteCommand());
        register.queue(ModCommand.createModsCommand());
        register.upsert(api).exceptionally(ExceptionLogger.get());
    }

    /**
     * These classes are scanned for instances of {@link CommandDef},
     * {@link SubCommandDef}, and {@link GroupCommandDef} annotation
     * @param api a live instance of Discord
     */
    public static void registerWithAnnotations(DiscordApi api) {

        SlashCommandRegister register = new SlashCommandRegister();
        register.queue(new PingPongCommand());
        register.queue(new FizzBuzz());
        register.queue(new QuoteCommand());
        register.queue(new ModCommand());
        register.upsert(api).join();

    }

    /**
     * These classes are scanned for instances of the {@link Slash} annotation
     * which are used to handle the {@link SlashCommandCreateEvent}
     * @param api a live instance of Discord
     */
    public static void respondeWithAnnotations(DiscordApi api) {
        SlashCommandDispatcher dispatcher = new SlashCommandDispatcher(api);
        dispatcher.queue(new PingPongCommand());
        dispatcher.queue(new FizzBuzz());
        dispatcher.queue(new QuoteCommand());
        dispatcher.queue(new ModCommand());
        dispatcher.submit().join();
    }





}
