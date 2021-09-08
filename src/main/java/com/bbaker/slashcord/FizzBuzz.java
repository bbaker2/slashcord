package com.bbaker.slashcord;

import com.bbaker.slashcord.handler.annotation.Slash;
import com.bbaker.slashcord.handler.annotation.SlashOption;

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
