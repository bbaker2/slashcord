package com.bbaker.slashcord.examples;

import com.bbaker.slashcord.handler.annotation.Slash;
import com.bbaker.slashcord.handler.annotation.SlashOption;
import com.bbaker.slashcord.structure.annotation.CommandDef;
import com.bbaker.slashcord.structure.annotation.OptionDef;

@CommandDef(
    name = "fizzbuzz",
    description = "Fizz if divisible by 3, Buzz if divisible by 5",
    options = {
        @OptionDef(name = "number", description = "Fizz if divisible by 3, Buzz if divisible by 5")
    }
)
public class FizzBuzz {

    @Slash(command = "fizzbuzz")
    public String fizzBuzz(@SlashOption("number") Integer number) {
        StringBuilder sb = new StringBuilder();
        if(number % 3 == 0) {
            sb.append("fizz");
        }

        if(number % 5 == 0) {
            if(sb.length() > 0) {
                sb.append(" ");
            }

            sb.append("buzz");
        }
        return sb.length() == 0 ? "No matches" : sb.toString();
    }

}
