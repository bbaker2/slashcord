# Command Definitions via Annotations

## Declaring the Command
All annotation commands must be declared at the top of the class. They will always have a `name` and a `description`.
There are three types of commands you can declare:

Annotation | Description
---------- | -----------
@CommandDef | Just has [options](#optiondef)
@SubCommandDef | It nests ``@CommandDef`` as a sub-option
@GroupCommandDef | Nests `SubCommandDef` as a sub-option, which in turn nests `@CommandDef` as another sub-option

### @CommandDef
The most common type of command. It can take in user inputs as options ([See @OptionDef](#optiondef))
```java
@CommandDef(name = "ping", description = "Will pong if pinged")
public class PingPongCommand {
    /*
    @Slash(command = "ping") will probably be somewhere in here
    */
}
```
### @SubCommandDef
For when you want to force your user to select a sub-command.

`@SubCommandDef` can only contain an array of `@CommandDef`\
.... which can only contain an array of [`@OptionDef`](#optiondef)
```java
@SubCommandDef(
    name = "quote",
    description = "For quoting funny things in the server",
    subs = {
        @CommandDef(
            name = "add",
            description = "Add a quote",
            options = {
                @OptionDef(name = "quote", description = "The quote itself",   type = STRING ,required = true),
                @OptionDef(name = "user",  description = "Who said the quote", type = USER, required = false)
            }
        ),
        @CommandDef(
            name = "random",
            description = "Output a random quote"
        )
    }
)
public class QuoteCommand {
    /*
    @Slash(command = "quote", sub = "add")    will probably be somewhere in here 
    @Slash(command = "quote", sub = "random") will probably be somewhere in here
    */
}
```
### @GroupCommandDef
For when you have 2-layers of sub-commands

`GroupCommandDef` can only contain an array of `SubCommandDef`,\
.... which can only contain an array of `@CommandDef`,\
........ which can only contain a list of [`@OptionDef`](#optiondef)
```java
@GroupCommandDef(
    name = "mod",
    description = "Useful commands for the server mods",
    groups = {
        @SubCommandDef(
            name = "add",
            description = "Append a role to a user, or a user to a channel",
            subs = {
                @CommandDef(
                    name = "role",
                    description = "Give a user a role",
                    options = {
                        @OptionDef(name = "user", description = "the user who will recieve this role", type = USER, required = true),
                        @OptionDef(name = "role", description = "the the desired role", type = ROLE, required = true)
                    }
                ),
                @CommandDef(
                    name = "channel",
                    description = "Add a user to a channel",
                    options = {
                        @OptionDef(name = "user",    description = "the user who will be added to the channel", type = USER, required = true),
                        @OptionDef(name = "channel", description = "the desired channel", type = CHANNEL, required = true)
                    }
                )
            }
        ),
        @SubCommandDef(
            name = "remove",
            description = "Remove a role from a user, or a user from a channel",
            subs = {
                @CommandDef(
                    name = "role",
                    description = "Remove a role from a user",
                    options = {
                        @OptionDef(name = "user", description = "the user who will be removed from the role", type = USER, required = true),
                        @OptionDef(name = "role", description = "the the desired role", type = ROLE, required = true)
                    }
                ),
                @CommandDef(
                    name = "channel",
                    description = "Remove a user from a channel",
                    options = {
                            @OptionDef(name = "user", description = "the user who will be removed from the channel", type = USER, required = true),
                        @OptionDef(name = "channel",  description = "the desired channel", type = CHANNEL, required = true)
                    }
                )
            }
        )
    }
)
public class ModCommand {
    /*    
    @Slash( command = "mod", group = "add", sub = "role")       will probably be somewhere in here 
    @Slash( command = "mod", group = "add", sub = "channel")    will probably be somewhere in here 
    @Slash( command = "mod", group = "remove", sub = "role")    will probably be somewhere in here 
    @Slash( command = "mod", group = "remove", sub = "channel") will probably be somewhere in here 
    */
}
```
## @OptionDef
These are how you receive inputs from the user. According to discord, you can have a max of [25 Options](https://discord.com/developers/docs/interactions/application-commands#application-command-object-application-command-structure).

There are four values you must populate:

Property | Default | Description
-------- | --------| -----------
`name` | required |The "variable name" used to reference the option
`description` | required | Some helpful text for the user
`type` | `SlashCommandOptionType.STRING` | What data type is this option. This uses Javacord's [SlashCommandOptionType](https://docs.javacord.org/api/v/3.3.2/org/javacord/api/interaction/SlashCommandOptionType.html) enum. 
`required` | false | Determines if the user is required to populate to send slash command

Not all types are allowed. Slashcord artificially enforces which [SlashCommandOptionType](https://docs.javacord.org/api/v/3.3.2/org/javacord/api/interaction/SlashCommandOptionType.html) are allowed as an input option.


Class | Description | Supports @ChoiceDef?
----- | ----------- | -----------------
STRING | String Values | yes
INTEGER | For both [INTEGER and DOUBLE](https://discord.com/developers/docs/interactions/application-commands#application-command-object-application-command-option-type) | yes
BOOLEAN | Boolean Values | no
USER | For referencing a User | no
CHANNEL | For referencing a text channel, voice channel, or channel category | no
ROLE | For referencing a role in a given server | no
MENTIONABLE | For referencing a user, role, text channel, voice channel, or channel category | no

## @ChoiceDef
The `STRING` and `INTEGER` types can also support choices. Good for when you want to force your user to select from a list of values.

According to Discord, you can have a max of [25 Choices](https://discord.com/developers/docs/interactions/application-commands#application-command-object-application-command-option-structure).

- If your `@ChoiceDef` is a child of an `@OptionDef` that is an `INTEGER`, you must populate the `intVal` property.
- If your `@ChoiceDef` is a child of an `@OptionDef` that is an `STRING`, you must populate the `strVal` property.

```java
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
                            @ChoiceDef(name = "Monday",     strVal = "MONDAY"),
                            @ChoiceDef(name = "Tuesday",    strVal = "TUESDAY"),
                            @ChoiceDef(name = "Wednesday",  strVal = "WEDNESDAY"),
                            @ChoiceDef(name = "Thursday",   strVal = "THURSDAY"),
                            @ChoiceDef(name = "Friday",     strVal = "FRIDAY"),
                            @ChoiceDef(name = "Saturday",   strVal = "SATURDAY"),
                            @ChoiceDef(name = "Sunday",     strVal = "SUNDAY")
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
                        @ChoiceDef(name = "---", intVal = 0),
                        @ChoiceDef(name = "--x", intVal = 1),
                        @ChoiceDef(name = "-w-", intVal = 2),
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
    /*
    @Slash( command = "choice-example", sub = "today")         // will probably be somewhere in here
    @Slash( command = "choice-example", sub = "permission")    // will probably be somewhere in here
    */

```

## Class Definitions for code reuse
Each annotation can be populated by referencing a class. The class you create must:
- extend the correct class
- be a static class
- have a the default constructor

Annotation | Class to Extend
---------- | ---------------
@CommandDef | Command
@SubCommandDef | SubCommand
@GroupCommandDef | GroupCommand
@OptionDef | InputOption
@ChoiceDef | Choice

In the below example, ALL the `@CommandDef` define the exact same command.
They just demonstrate the ability to mix and match classes with annotations. 

```java
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

    /** The slash command itself **/
    @Slash(command = "my-command")
    public String doCommand(@SlashOption("my-option") Integer val) {
        // Say hello as many times as the integer value says
        return IntStream.of(val).mapToObj(i -> "hello").collect(Collectors.joining(" "));
    }

}
```
