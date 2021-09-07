package com.bbaker.slashcord.structure.util;

import static java.util.stream.Collectors.toList;

import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionChoice;

import com.bbaker.slashcord.structure.entity.Choice;
import com.bbaker.slashcord.structure.entity.Command;
import com.bbaker.slashcord.structure.entity.Option;

public class ConverterUtil {


    public static Command from(SlashCommand sc) {
        return new SimpleCommand(sc);
    }



    private static class SimpleCommand extends Command {

        public SimpleCommand(SlashCommand sc) {
            this.name = sc.getName();
            this.description = sc.getDescription();
            this.options = sc.getOptions().stream().map(SimpleOption::new).collect(toList());
        }

    }

    private static class SimpleOption extends Option {

        public SimpleOption(SlashCommandOption sco) {
            this.name = sco.getName();
            this.description = sco.getDescription();
            this.required = sco.isRequired();
            this.options =	sco.getOptions().stream().map(SimpleOption::new).collect(toList());
            this.choices = sco.getChoices().stream().map(SimpleChoice::new).collect(toList());
        }

    }

    private static class SimpleChoice extends Choice {
        public SimpleChoice(SlashCommandOptionChoice scoc) {
            this.name = scoc.getName();
            scoc.getStringValue().ifPresent(val -> this.strVal = val);
            scoc.getIntValue().ifPresent(val -> this.intVal = val);
        }
    }

}
