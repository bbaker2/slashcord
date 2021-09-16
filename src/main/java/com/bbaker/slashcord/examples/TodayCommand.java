package com.bbaker.slashcord.examples;

import static org.javacord.api.interaction.SlashCommandOptionType.*;

import java.time.LocalDate;

import com.bbaker.slashcord.handler.annotation.Slash;
import com.bbaker.slashcord.handler.annotation.SlashOption;
import com.bbaker.slashcord.structure.annotation.ChoiceDef;
import com.bbaker.slashcord.structure.annotation.CommandDef;
import com.bbaker.slashcord.structure.annotation.OptionDef;
import com.bbaker.slashcord.structure.annotation.SubCommandDef;

@SubCommandDef(
    name = "choice-example",
    description = "The user will be forced to select from a list of values",
    subs = {
        @CommandDef(
            name = "today",
            description = "Determin if today matches the provided day of the week",
            options = {
                @OptionDef(
                    name = "day",
                    description = "the day of the week",
                    type = STRING,
                    required = true,
                    choices = {
                            @ChoiceDef(name = "Monday", 	strVal = "MONDAY"),
                            @ChoiceDef(name = "Tuesday", 	strVal = "TUESDAY"),
                            @ChoiceDef(name = "Wednesday",	strVal = "WEDNESDAY"),
                            @ChoiceDef(name = "Thursday", 	strVal = "THURSDAY"),
                            @ChoiceDef(name = "Friday", 	strVal = "FRIDAY"),
                            @ChoiceDef(name = "Saturday", 	strVal = "SATURDAY"),
                            @ChoiceDef(name = "Sunday", 	strVal = "SUNDAY")
                    }
                )
            }
        ),
        @CommandDef(
            name = "permission",
            description = "will set the permissions using chmod numbers",
            options = {
                @OptionDef(
                    name = "chmod",
                    description = "The chmod numeric permission",
                    type = INTEGER,
                    required = true,
                    choices = {
//                      @ChoiceDef(name = "---", intVal = 0),
//                    	@ChoiceDef(name = "--x", intVal = 1),
//                    	@ChoiceDef(name = "-w-", intVal = 2),
                        @ChoiceDef(name = "", intVal = 0),
                        @ChoiceDef(name = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", intVal = 1),
                        @ChoiceDef(name = "-w-", strVal = "omgwtfbbq"),
                        @ChoiceDef(name = "-wx", intVal = 3),
                        @ChoiceDef(name = "r--", intVal = 4),
                        @ChoiceDef(name = "r-x", intVal = 5),
                        @ChoiceDef(name = "rw-", intVal = 6),
                        @ChoiceDef(name = "rwx", intVal = 7)
                    }
                )
            }
        )
    }
)
public class TodayCommand {

    @Slash( command = "choice-example", sub = "today")
    public String today(@SlashOption("day") String day){
        return LocalDate.now().getDayOfWeek().name().equals(day)
                ? "yes"
                : "no";
    }

    @Slash( command = "choice-example", sub = "permission")
    public String setPermissions(@SlashOption("chmod") Integer chmod){
        switch(chmod) {
            case 0:
                return "none";
            case 1:
                return "execute only";
            case 2:
                return "write only";
            case 3:
                return "write and execute";
            case 4:
                return "read only";
            case 5:
                return "read and execute";
            case 6:
                return "read and write";
            case 7:
                return "read, write, and execute";
            default:
                return "?";
        }
    }

}
