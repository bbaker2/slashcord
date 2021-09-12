# Command Definitions via Util Classes
Commands in discords have 3 main parts:
1. The application command name
2. Defining the options (groups, sub-commands, and general inputs)
3. Defining choices (if any)

## Define the base command
All commands need a name and description
```java
Command fizzBuzzCmd = new CommandTierI("ping", "Will Ping");
```
The Command object must be implemented by one of three classes:
CommandTierI | The simplest command that only supports [user-inputted options](#basic-options)
CommandTierII | Supports commands with sub-commands. See [Sub Command](#sub-commands) for usage
CommandTierIII | Supports commands with groups. See [Command Groups](#command-groups) for usage


## Define the options
It is recommended that you read the official Discord API docs for [options](https://discord.com/developers/docs/interactions/application-commands#application-command-object-application-command-option-structure) and [sub commands/groups](https://discord.com/developers/docs/interactions/application-commands#subcommands-and-subcommand-groups)
And since this is a wrapper around Javacord, it would be wise to review the [wiki related to commands](https://javacord.org/wiki/basic-tutorials/interactions/commands.html)
### Basic Options
A basic option is [anything that is *not* a SUB_COMMAND or a SUB_COMMAND_GROUP](https://discord.com/developers/docs/interactions/application-commands#application-command-object-application-command-option-type). 
All basic options extend the `InputOption` class. 
Each option needs a name, description, and wither or not the option is required.
```java
InputOption intOption = new IntOption("number", "Any whole number", true);
Command fizzBuzzCmd = new CommandTierI("fizzbuzz", "Fizz if divisible by 3, Buzz if divisible by 5")
    .addOption(intOption));
```
We are limited to what javacord can support, so the following basic options exist:

Class | Description | Supports Choices?
----- | ----------- | -----------------
StringOption | String Values | yes
IntOption | For both [INTEGER and DOUBLE](https://discord.com/developers/docs/interactions/application-commands#application-command-object-application-command-option-type) | yes
BooleanOption | Boolean Values | no
UserOption | For referencing a User | no
ChanelOption | For referencing a text channel, voice channel, or channel category | no
RoleOption | For referencing a role in a given server | no
MentionalbleOption | For referencing a user, role, text channel, voice channel, or channel category | no

**Note:** `InputOption` can only be a child of the `CommandTierI` or `SubOption`
### Choices
wip
### Sub Commands
The `SubOption` are a special type of option that can ONLY be a child of a `CommandTierII` or `SubGroup`. 
Its child options can only be `InputOption`
```java
CommandTierII quote = new CommandTierII("quote", "For quoting funny things in the server");
quote.addOption(
    new SubOption("add", "Add a quote").addOptions(
        new StringOption("quote", "The quote itself",    true),
        new UserOption(  "user",  "Who said the quote?", false)
    ),
    new SubOption("random", "Output a random quote")
);
```
### Command Groups
`GroupOption` are a special type of option that can ONLY be a child of a `CommandTierIII`. 
```java
CommandTierIII mod = new CommandTierIII("mod",      "Useful commands for the server mods");
InputOption user    = new UserOption("user",        "The target user",      true);
InputOption channel = new ChannelOption("channel",  "The target channel",   true);
InputOption role    = new RoleOption("role",        "The target role",      true);

mod.addOption(
    new GroupOption("add", "Append a role to a user, or a user to a channel")
        .addOptions(new SubOption("role",    "Give a user a role")          .addOptions(user, role))
        .addOptions(new SubOption("channel", "Add a user to a channel")     .addOptions(user, channel))
);
mod.addOption(
    new GroupOption("remove", "Remove a role from a user, or a user from a channel")
        .addOptions(new SubOption("role",    "Remove a role from a user")   .addOptions(user, role))
        .addOptions(new SubOption("channel", "Remove a user from a channel").addOptions(user, channel))
);
```
## Register/Update the commands
The `SlashCommandRegister` is in charge of creating and updating commands. 
Calling the `.queue(Command)` method only prepares the command to be sent to Discord. You still need to call the `.upsert(DiscordApi)` method which will:
1. Query for pre-existing commands
2. Compare the pre-existing commands against what was queued
3. Any commands with a similar name but different descriptions, options, or choices will be updated
4. Any commands not already in Discord will be created

**Note:** This will not remove any commands. Only update and insert. 

### Global Commands
```java
DiscordApi api = getApiSomehow();

SlashCommandRegister registry = new SlashCommandRegister();
registry.queue(createPingPong());
registry.queue(createFizzBuzzCommand());
registry.queue(createModsCommand());
registry.queue(createQuoteCommand());
registry.upsert(api);   // you can call safely call this method multiple times. 
                        // It will only make changes if a difference is detected
```
### Server Commands
** Server commands are not supported yet, but will eventually look like this:**
```java
DiscordApi api = getApiSomehow();
Server serverA = getSomeServer();
Server serverB = getADifferentServer();

SlashCommandRegister registry = new SlashCommandRegister();
registry.queue(createPingPong());                       // global
registry.queue(createFizzBuzzCommand(), serverA);       // created for serverA only
registry.queue(createModsCommand(), serverB);           // created for serverB only
registry.queue(createQuoteCommand(), serverA, serverB); // created for both server A and B
registry.upsert(api);   // you can call safely call this method multiple times. 
                        // It will only make changes if a difference is detected
```