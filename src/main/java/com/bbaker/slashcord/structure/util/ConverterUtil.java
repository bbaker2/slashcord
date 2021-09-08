package com.bbaker.slashcord.structure.util;

import static java.util.stream.Collectors.toList;

import java.util.Collection;

import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionBuilder;
import org.javacord.api.interaction.SlashCommandOptionChoice;
import org.javacord.api.interaction.SlashCommandOptionChoiceBuilder;

import com.bbaker.slashcord.structure.entity.Choice;
import com.bbaker.slashcord.structure.entity.Command;
import com.bbaker.slashcord.structure.entity.Option;

public class ConverterUtil {

    public static SlashCommandBuilder from(Command command) {
        SlashCommandBuilder builder = new SlashCommandBuilder();
        builder.setName(command.getName());
        builder.setDescription(command.getDescription());

        if(isNotEmpty(command.getOptions())) {
            for(Option o : command.getOptions()) {
                builder.addOption(from(o));
            }
        }

        return builder;
    }

    public static SlashCommandOption from(Option option) {
        SlashCommandOptionBuilder builder = new SlashCommandOptionBuilder();
        builder.setName(option.getName());
        builder.setDescription(option.getDescription());
        builder.setType(option.getType());
        builder.setRequired(option.isRequired());

        if(isNotEmpty(option.getChoices())) {
            for(Choice c : option.getChoices()) {
                builder.addChoice(from(c));
            }
        }

        if(isNotEmpty(option.getOptions())) {
            for(Option o : option.getOptions()) {
                builder.addOption(from(o));
            }
        }
        return builder.build();
    }

    public static SlashCommandOptionChoice from(Choice choice) {
        SlashCommandOptionChoiceBuilder builder = new SlashCommandOptionChoiceBuilder();

        builder.setName(choice.getName());

        if(choice.getStrVal() == null || choice.getStrVal().isBlank()) {
            builder.setValue(choice.getIntVal());
        } else {
            builder.setValue(choice.getStrVal());
        }

        return builder.build();
    }

    public static boolean isNotEmpty(Collection<?> c) {
        return c != null && !c.isEmpty();
    }

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
            this.type = sco.getType();
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
