package com.bbaker.slashcord.handler.args;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.SlashCommandInteractionOptionsProvider;

public class UserOptionArgument extends AbstractOptionArgument<User> {

    public UserOptionArgument(String name) {
        super(name);
    }

    @Override
    protected User getValue(SlashCommandInteractionOptionsProvider interaction) {
        // check to see if the option exists at all
        Optional<SlashCommandInteractionOption> unsafeOption = interaction.getOptionByName(name);
        if(!unsafeOption.isPresent()) {
            return null;
        }

        // If so, attempt to grab the user from the cache
        SlashCommandInteractionOption userOption = unsafeOption.get();
        Optional<User> cachedUser = userOption.getUserValue();

        if(cachedUser.isPresent()) {
            return cachedUser.get();
        }

        // If the user is not in the cache, perform a network call to retrieve the user info
        Optional<CompletableFuture<User>> queriedUser = userOption.requestUserValue();

        if(queriedUser.isPresent()) {
            // perform the network call, return null if the network call fails
            return queriedUser.get().exceptionally(e -> null).join();
        }

        // if we somehow failed to prepare a network call, return null
        return null;
    }

}
