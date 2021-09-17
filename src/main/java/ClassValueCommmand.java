import static org.javacord.api.interaction.SlashCommandOptionType.*;

import com.bbaker.slashcord.structure.annotation.CommandDef;
import com.bbaker.slashcord.structure.entity.InputOption;
import com.bbaker.slashcord.structure.entity.RegularCommand;

@CommandDef(
  name = "my-command",
  description = "the description"
)
public class ClassValueCommmand {


    static class MyCommand extends RegularCommand {
        public MyCommand(String name, String description) {
            super("my-command", "the description");
        }
    }

    static class MyOption extends InputOption {
        protected MyOption() {
            super("my-option", "the description", true, MENTIONABLE);
        }
    }

}
