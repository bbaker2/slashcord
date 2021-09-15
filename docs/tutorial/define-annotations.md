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
`GroupCommandDef` can only contain an array of `SubCommandDef`, 

.... which can only contain an array of `@CommandDef`,

........ which can only contain a list of `@OptionDef`

<details>
<summary>Example of Group Commands</summary>
<p>

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
</p>
</details>

## @OptionDef

## @ChoiceDef

## Class Definitions for code reuse

Not actually done yet. Will be in release 2 or 3