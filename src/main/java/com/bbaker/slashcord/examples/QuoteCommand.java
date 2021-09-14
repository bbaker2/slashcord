package com.bbaker.slashcord.examples;

import static org.javacord.api.interaction.SlashCommandOptionType.STRING;
import static org.javacord.api.interaction.SlashCommandOptionType.USER;

import java.util.Random;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.callback.InteractionImmediateResponseBuilder;

import com.bbaker.slashcord.handler.annotation.Slash;
import com.bbaker.slashcord.handler.annotation.SlashOption;
import com.bbaker.slashcord.structure.annotation.CommandDef;
import com.bbaker.slashcord.structure.annotation.OptionDef;
import com.bbaker.slashcord.structure.annotation.SubCommandDef;
import com.bbaker.slashcord.structure.entity.Command;
import com.bbaker.slashcord.structure.entity.StringOption;
import com.bbaker.slashcord.structure.entity.SubCommand;
import com.bbaker.slashcord.structure.entity.SubOption;
import com.bbaker.slashcord.structure.entity.UserOption;

@SubCommandDef(
    name = "quote",
    description = "For quoting funny things in the server",
    subs = {
        @CommandDef(
            name = "add",
            description = "Add a quote",
            options = {
                @OptionDef(name = "quote", description = "The quote itself",   type = STRING ,required = true),
                @OptionDef(name = "user",  description = "Who said the quote", type = USER, required = false)
            }
        ),
        @CommandDef(
            name = "random",
            description = "Output a random quote"
        )
    }
)
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

    public static Command createQuoteCommand() {
        SubCommand quote = new SubCommand("quote", "For quoting funny things in the server");
        quote.addOption(
            new SubOption("add", "Add a quote").addOptions(
                new StringOption("quote", "The quote itself", true),
                new UserOption("user", "Who said the quote?", false)
            ),
            new SubOption("random", "Output a random quote")
        );

        return quote;
    }

}
