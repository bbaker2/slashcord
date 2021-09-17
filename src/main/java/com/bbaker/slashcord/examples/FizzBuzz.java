package com.bbaker.slashcord.examples;

import static org.javacord.api.interaction.SlashCommandOptionType.INTEGER;

import com.bbaker.slashcord.handler.annotation.Slash;
import com.bbaker.slashcord.handler.annotation.SlashOption;
import com.bbaker.slashcord.structure.annotation.CommandDef;
import com.bbaker.slashcord.structure.annotation.OptionDef;
import com.bbaker.slashcord.structure.entity.Command;
import com.bbaker.slashcord.structure.entity.IntOption;
import com.bbaker.slashcord.structure.entity.RegularCommand;

@CommandDef(
    name = "fizzbuzz",
    description = "Fizz if divisible by 3, Buzz if divisible by 5",
    options = {
        @OptionDef(
            name = "number",
            description = "Fizz if divisible by 3, Buzz if divisible by 5",
            type = INTEGER,
            required = true
        )
    }
)
public class FizzBuzz {

    @Slash(command = "fizzbuzz")
    public String fizzBuzz(@SlashOption("number") Integer number) {
        StringBuilder sb = new StringBuilder();
        if(number % 3 == 0) sb.append("fizz");
        if(number % 5 == 0) {
            if(sb.length() > 0) sb.append(" ");
            sb.append("buzz");
        }
        return sb.length() == 0 ? "No matches" : sb.toString();
    }

    public static Command createFizzBuzzCommand() {
        return new RegularCommand("fizzbuzz", "Fizz if divisible by 3, Buzz if divisible by 5")
            .addOption(
                new IntOption("number", "Any whole number", true)
        );
   }

}
