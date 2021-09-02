# slashcord
Adds annotation and utils for slash commands for Discord

This library is created for/dependent on [Javacord](https://github.com/Javacord/Javacord)

This library comes with two favors of code:

- Defining a command so that it can be registered (and synced) with Discord
- Annotations for handling the commands themselves

# Command Definitions via Util Classes
Commands in discords have 3 main parts:
1. The application command name
2. Defining the options (groups, sub-commands, and general inputs)
3. Defining choices (if any)

## Define the base command
All commands need a name and description
```java
Command fizzBuzz = new Command("fizzBuzz", "Prints 'foo' if divisible by 3, and prints 'bar' if divisible by 5");
```
## Define the options
It is recommended that you read the official Discord API docs for [options](https://discord.com/developers/docs/interactions/application-commands#application-command-object-application-command-option-structure) and [sub commands/groups](https://discord.com/developers/docs/interactions/application-commands#subcommands-and-subcommand-groups)
And since this is a wrapper around Javacord, it would be wise to review the [wiki related to commands](https://javacord.org/wiki/basic-tutorials/interactions/commands.html)
### Basic Options
A basic option is [anything that is SUB_COMMAND or a SUB_COMMAND_GROUP](https://discord.com/developers/docs/interactions/application-commands#application-command-object-application-command-option-type). 
To specify the type you want your option to be, we use the [SlashCommandOptionType](https://github.com/Javacord/Javacord/blob/v3.3.2/javacord-api/src/main/java/org/javacord/api/interaction/SlashCommandOptionType.java) enum from Javacord.
Each option needs a name, description, and type. Optionally, you can declare that the option is required or not. (Defaults to *not* required)
```java
Command fizzBuzz = new Command("fizzBuzz", "Prints 'foo' if divisible by 3, and prints 'bar' if divisible by 5");
Option number = new Option("number", "Any whole number", INTEGER, false);
fizzBuzz.addOption(number);
```
**Note:** `Option` can only be a child of the `Command` or `SubCommand`
### Sub Commands
Sub Commands are a special type of option that can ONLY be a child of a `Command` or `CommandGroup`. 
Its child options can only be `Option`
```java
Command roleCmd = new Command("role", "Assign or remove yourself from a role");
Option roleOption = new Option("role", "Pick a role", ROLE, true); // you are allowed to "reuse" an option

Option add = new SubCommand("add", "Add yourself to a role");
add.addOption(roleOption);

Option remove = new SubCommand("remove", "Remove yourself from a role");
remove.addOption(roleOption);

roleCmd.addOption(add, remove); // addOption() is a vararg, hence the multiple arguments here
```
### Command Groups
Command Groups are a special type of option that can ONLY be a child of a `Command`. 
```java
Command msgMod = new Command("message-mods", "Inform the server mods");

Option details = new Option("comment", "Please describe the incident", STRING, true);
Option user    = new Option("user", "The user involved", USER, true);
Option channel = new Option("channel", "The channel involved", CHANNEL, true);

// group 1
CommandGroup reportUser = new CommandGroup("report-user", "Report a user");
    // the addOptions() also returns itself, so you can treat it like a builder pattern
    SubCommand spamming = new SubCommand("spamming","Too much irrelevant or unwanted content").addOption(user, details); 
    SubCommand racism   = new SubCommand("racism",  "Negative comments based on skin, religion, or nationality").addOption(user, details);
    SubCommand sexism   = new SubCommand("sexism",  "Negative comments based on gender. Includes homophobia").addOption(user, details);
    SubCommand other    = new SubCommand("other",   "Anything that cannot be categorized as racism, sexism, or spamming").addOption(user, details);
reportUser.addOption(spamming, racism, sexism, other);

// group 2
CommandGroup channelRequest = new CommandGroup("channel-request", "If the channel is going to fast, you can request slow mode");
    SubCommand slowMode     = new SubCommand("slow-mode", "To many people talking at once. Requesting Slow Mode for an hour").addOption(channel);
    SubCommand updateTopic  = new SubCommand("update-topic", "The topic needs updating")
        .addOption(channel, new Option("topic", "The new topic", STRING, true)); // just a fancy example of ad-hoc option creations
    
msgMod.addOption(reportUser, channelRequest);
```
## Register/Update the commands
### Global Commands
// TODO: example for global commands
### Server Commands
// TODO: examples for server commands

# Command Usages via Annotations
## Handling Command
## Handling Options
## Including meta data