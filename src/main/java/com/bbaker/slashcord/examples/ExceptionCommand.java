package com.bbaker.slashcord.examples;

import static org.javacord.api.interaction.SlashCommandOptionType.*;

import com.bbaker.slashcord.handler.annotation.Slash;
import com.bbaker.slashcord.handler.annotation.SlashException;
import com.bbaker.slashcord.handler.annotation.SlashOption;
import com.bbaker.slashcord.structure.annotation.CommandDef;
import com.bbaker.slashcord.structure.annotation.OptionDef;

@CommandDef(
    name = "divide",
    description = "divide 100 by a given number",
    options = @OptionDef(
        name = "number",
        description = "The number that you will divide 100 againt",
        type = INTEGER,
        required = true
    ))
public class ExceptionCommand {

    @SlashException(ArithmeticException.class)
    @Slash(command = "divide")
    public String divide(@SlashOption("number") Integer val) {
        return String.valueOf(100/val);
    }

}
