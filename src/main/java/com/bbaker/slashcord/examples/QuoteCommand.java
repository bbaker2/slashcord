package com.bbaker.slashcord.examples;

import java.util.Random;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.callback.InteractionImmediateResponseBuilder;

import com.bbaker.slashcord.handler.annotation.Slash;
import com.bbaker.slashcord.handler.annotation.SlashOption;

public class QuoteCommand {

    @Slash( command = "quote", sub = "add")
    public void addQuote(@SlashOption("quote") String quote, @SlashOption("user") User user,
            InteractionImmediateResponseBuilder response) {
        // Since the "user" option is optional, we cannot assume it will be populated
        if(user == null) {
            storeAnonymousQuote(quote);
        } else {
            storeNamedQuote(quote, user);
        }

        response.append("Quote added").setFlags(MessageFlag.EPHEMERAL).respond();
    }

    @Slash( command = "quote", sub = "random")
    public void randomQuote(InteractionImmediateResponseBuilder response) {
        String randomQuote = getRandomQuote();
        response.append(randomQuote).respond();
    }

    private String getRandomQuote() {
        return "Random #" + new Random().nextInt();
    }

    private void storeNamedQuote(String quote, User user) {
        // store to the database
    }

    private void storeAnonymousQuote(String quote) {
        // store to the database
    }

}
