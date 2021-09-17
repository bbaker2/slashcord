import static org.javacord.api.interaction.SlashCommandOptionType.INTEGER;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.bbaker.slashcord.handler.annotation.Slash;
import com.bbaker.slashcord.handler.annotation.SlashOption;
import com.bbaker.slashcord.structure.annotation.ChoiceDef;
import com.bbaker.slashcord.structure.annotation.CommandDef;
import com.bbaker.slashcord.structure.annotation.OptionDef;
import com.bbaker.slashcord.structure.entity.InputOption;
import com.bbaker.slashcord.structure.entity.IntChoice;
import com.bbaker.slashcord.structure.entity.IntOption;
import com.bbaker.slashcord.structure.entity.RegularCommand;

@CommandDef(ClassValueCommand.MyCommand.class)
@CommandDef(
    name = "my-command",
    description = "the command description",
    options = {
        @OptionDef(ClassValueCommand.MyOption.class)
    }
)
@CommandDef(
    name = "my-command",
    description = "the command description",
    options = {
        @OptionDef(
            name = "my-option",
            description = "the option description",
            required = true,
            type = INTEGER,
            choices = {
                @ChoiceDef(ClassValueCommand.MyChoiceOne.class),
                @ChoiceDef(ClassValueCommand.MyChoiceTwo.class)
            }
        )
    }
)
@CommandDef(
    name = "my-command",
    description = "the command description",
    options = {
        @OptionDef(
            name = "my-option",
            description = "the option description",
            required = true,
            type = INTEGER,
            choices = {
                @ChoiceDef(
                    name = "one",
                    intVal = 1
                ),
                @ChoiceDef(
                    name = "two",
                    intVal = 2
                )
            }
        )
    }
)
public class ClassValueCommand {

    /** statically defined classes with default constructors **/
    static class MyCommand extends RegularCommand {
        public MyCommand(String name, String description) {
            super("my-command", "the command description");
            addOption(new MyOption());
        }
    }

    static class MyOption extends IntOption {
        public MyOption() {
            super("my-option", "the option description", true);
            appendChoice(new MyChoiceOne(), new MyChoiceTwo());
        }
    }

    static class MyChoiceOne extends IntChoice {
        public MyChoiceOne() {
            super("one", 1);
        }
    }

    static class MyChoiceTwo extends IntChoice {
        public MyChoiceTwo() {
            super("two", 2);
        }
    }


    @Slash(command = "my-command")
    public String doCommand(@SlashOption("my-option") Integer val) {
        // Say hello as many times as the integer value says
        return IntStream.of(val).mapToObj(i -> "hello").collect(Collectors.joining(" "));
    }

}
