package com.bbaker.slashcord.util;

import static com.bbaker.slashcord.util.CommonsUtil.isBlank;
import static com.bbaker.slashcord.util.CommonsUtil.isNotEmpty;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionBuilder;
import org.javacord.api.interaction.SlashCommandOptionChoice;
import org.javacord.api.interaction.SlashCommandOptionChoiceBuilder;
import org.javacord.api.interaction.SlashCommandOptionType;
import org.javacord.api.interaction.SlashCommandUpdater;

import com.bbaker.slashcord.structure.annotation.ChoiceDef;
import com.bbaker.slashcord.structure.annotation.ChoiceDef.DefaultChoice;
import com.bbaker.slashcord.structure.annotation.CommandDef;
import com.bbaker.slashcord.structure.annotation.CommandDef.DefaultRegularCommand;
import com.bbaker.slashcord.structure.annotation.GroupCommandDef;
import com.bbaker.slashcord.structure.annotation.GroupCommandDef.DefaultGroupCommand;
import com.bbaker.slashcord.structure.annotation.OptionDef;
import com.bbaker.slashcord.structure.annotation.OptionDef.DefaultOption;
import com.bbaker.slashcord.structure.annotation.SubCommandDef;
import com.bbaker.slashcord.structure.annotation.SubCommandDef.DefaultSubCommand;
import com.bbaker.slashcord.structure.entity.Choice;
import com.bbaker.slashcord.structure.entity.Command;
import com.bbaker.slashcord.structure.entity.GroupCommand;
import com.bbaker.slashcord.structure.entity.IntChoice;
import com.bbaker.slashcord.structure.entity.Option;
import com.bbaker.slashcord.structure.entity.RegularCommand;
import com.bbaker.slashcord.structure.entity.StringChoice;
import com.bbaker.slashcord.structure.entity.SubCommand;

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

        if(isBlank(choice.getStrVal())) {
            builder.setValue(choice.getIntVal());
        } else {
            builder.setValue(choice.getStrVal());
        }

        return builder.build();
    }

    public static SlashCommandUpdater from(Command command, long commandId) {
        SlashCommandUpdater updater = new SlashCommandUpdater(commandId);
        updater.setName(command.getName());
        updater.setDescription(command.getDescription());

        if(isNotEmpty(command.getOptions())) {
            List<SlashCommandOption> options = new ArrayList<>();
            for(Option o : command.getOptions()) {
                options.add(from(o));
            }

            updater.setSlashCommandOptions(options);
        }

        return updater;
    }

    public static Command from(SlashCommand sc) {
        return new SimpleCommand(sc);
    }

    /**** For Annotation Conversion ****/

    public static Command from(CommandDef cd) throws BadAnnotation {
        RegularCommand cmd = createIfNotDefault(DefaultRegularCommand.class, cd.value(), "@CommandDef");
        if(cmd != null) {
            return cmd;
        }

        return new SimpleCommand(cd);
    }

    public static Command from(SubCommandDef scd) throws BadAnnotation {
        Command cmd = createIfNotDefault(DefaultSubCommand.class, scd.value(), "@SubCommandDef");
        if(cmd != null) {
            return cmd;
        }

        return new SimpleCommand(scd);
    }

    public static Command from(GroupCommandDef gcd) throws BadAnnotation {
        GroupCommand cmd = createIfNotDefault(DefaultGroupCommand.class, gcd.value(), "@GroupCommandDef");
        if(cmd != null) {
            return cmd;
        }

        return new SimpleCommand(gcd);
    }

    public static Option from(OptionDef od) throws BadAnnotation {
        Option option = createIfNotDefault(DefaultOption.class, od.value(), "@OptionDef");
        if(option != null) {
            return option;
        }

        switch(od.type()) {
        case INTEGER:
        case STRING:
        case BOOLEAN:
        case CHANNEL:
        case MENTIONABLE:
        case ROLE:
        case USER:
            return new SimpleOption(od);
        case SUB_COMMAND_GROUP:
        case SUB_COMMAND:
        case UNKNOWN:
        default:
            throw new BadAnnotation("Unsupported option type in @OptionDef: " + od.type());
        }

    }

    public static Choice from(ChoiceDef cd) throws BadAnnotation {
        Choice choice = createIfNotDefault(DefaultChoice.class, cd.value(), "@ChoiceDef");
        if(choice != null) {
            return choice;
        }

        boolean isNumeric = isBlank(cd.strVal());
        return isNumeric
                ? new StringChoice(cd.name(), cd.strVal())
                : new IntChoice(cd.name(), cd.intVal());
    }

    // If a command is nested inside a sub-command, then it is secretly an option
    private static Option convertTo(CommandDef cd) throws BadAnnotation {
        RegularCommand cmd = createIfNotDefault(DefaultRegularCommand.class, cd.value(), "@CommandDef");
        if(cmd != null) {
            return new SimpleOption(cmd);

        }

        return new SimpleOption(cd);
    }

    // If a sub-command is nested inside of a group-command, this it is secretly an option
    private static Option convertTo(SubCommandDef scd) throws BadAnnotation {
        SubCommand cmd = createIfNotDefault(DefaultSubCommand.class, scd.value(), "@SubCommandDef");
        if(cmd != null) {
            return new SimpleOption(cmd);
        }

        return new SimpleOption(scd);
    }

    private static <T> T createIfNotDefault(Class<?> test, Class<T> target, String errorLocation)
            throws BadAnnotation {
        if(test.equals(target)) {
            return null; // no match
        }

        try {
            return target.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new BadAnnotation(errorLocation, e);
        }
    }

    private static class SimpleCommand extends Command {

        public SimpleCommand(SlashCommand sc) {
            this.name = sc.getName();
            this.description = sc.getDescription();
            this.options = sc.getOptions().stream().map(SimpleOption::new).collect(toList());
        }

        public SimpleCommand(CommandDef cd) throws BadAnnotation {
            this.name = cd.name();
            this.description = cd.description();
            this.options = new ArrayList<>();
            for(OptionDef od : cd.options()) {
                options.add(from(od));
            }
        }

        public SimpleCommand(SubCommandDef scd) throws BadAnnotation {
            this.name = scd.name();
            this.description = scd.description();
            this.options = new ArrayList<>();
            for(CommandDef cd : scd.subs()) {
                options.add(convertTo(cd));
            }
        }

        public SimpleCommand(GroupCommandDef gcd) throws BadAnnotation {
            this.name = gcd.name();
            this.description = gcd.description();
            this.options = new ArrayList<>();
            for(SubCommandDef scd : gcd.groups()) {
                options.add(convertTo(scd));
            }
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

        // Convert Options to Option
        public SimpleOption(OptionDef od) throws BadAnnotation {
            this.name = od.name();
            this.description = od.description();
            this.required = od.required();
            this.type = od.type();
            this.options = asList();
            this.choices = new ArrayList<>();
            for(ChoiceDef cd : od.choices()) {
                choices.add(from(cd));
            }
        }

        // Convert sub-command into an option
        // (when a sub-command is nested under a group-command)
        public SimpleOption(SubCommand subCmd) {
            this.name = subCmd.getName();
            this.description = subCmd.getDescription();
            this.type = SlashCommandOptionType.SUB_COMMAND_GROUP;
            this.required = false;
            this.options = subCmd.getOptions();
        }

        // Convert sub-command into an option
        // (when a sub-command is nested under a group-command)
        public SimpleOption(SubCommandDef scd) throws BadAnnotation {
            this.name = scd.name();
            this.description = scd.description();
            this.type = SlashCommandOptionType.SUB_COMMAND_GROUP;
            this.required = false;
            this.options = new ArrayList<>();
            for(CommandDef cd : scd.subs()){
                options.add(convertTo(cd));
            }
        }

        // Convert a command into an option
        // (when a command is nested under a sub-command)
        public SimpleOption(CommandDef cd) throws BadAnnotation {
            this.name = cd.name();
            this.description = cd.description();
            this.type = SlashCommandOptionType.SUB_COMMAND;
            this.required = false;
            this.options = new ArrayList<>();
            for(OptionDef od : cd.options()) {
                options.add(from(od));
            }
        }

        // Convert a command into an option
        // (when a command is nested under a sub-command)
        public SimpleOption(RegularCommand rc) {
            this.name = rc.getName();
            this.description = rc.getDescription();
            this.type = SlashCommandOptionType.SUB_COMMAND;
            this.required = false;
            this.options = rc.getOptions();
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
