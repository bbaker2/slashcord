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
Command fizzBuzzCmd = new Command("fizzbuzz", "Prints 'foo' if divisible by 3, and prints 'bar' if divisible by 5");
```
## Define the options
It is recommended that you read the official Discord API docs for [options](https://discord.com/developers/docs/interactions/application-commands#application-command-object-application-command-option-structure) and [sub commands/groups](https://discord.com/developers/docs/interactions/application-commands#subcommands-and-subcommand-groups)
And since this is a wrapper around Javacord, it would be wise to review the [wiki related to commands](https://javacord.org/wiki/basic-tutorials/interactions/commands.html)
### Basic Options
A basic option is [anything that is SUB_COMMAND or a SUB_COMMAND_GROUP](https://discord.com/developers/docs/interactions/application-commands#application-command-object-application-command-option-type). 
To specify the type you want your option to be, we use the [SlashCommandOptionType](https://github.com/Javacord/Javacord/blob/v3.3.2/javacord-api/src/main/java/org/javacord/api/interaction/SlashCommandOptionType.java) enum from Javacord.
Each option needs a name, description, and type. Optionally, you can declare that the option is required or not. (Defaults to *not* required)
```java
Command fizzBuzzCmd = new Command("fizzbuzz", "Prints 'foo' if divisible by 3, and prints 'bar' if divisible by 5");
Option number = new Option("number", "Any whole number", INTEGER, false);
fizzBuzz.addOption(number);
```
**Note:** `Option` can only be a child of the `Command` or `SubCommand`
### Sub Commands
Sub Commands are a special type of option that can ONLY be a child of a `Command` or `CommandGroup`. 
Its child options can only be `Option`
```java
Command role = new Command("role", "Assign or remove yourself from a role");
Option roleOption = new Option("role", "Pick a role", ROLE, true); // you are allowed to "reuse" an option

Option add = new SubCommand("add", "Add yourself to a role");
add.addOption(roleOption);

Option remove = new SubCommand("remove", "Remove yourself from a role");
remove.addOption(roleOption);

role.addOption(add, remove); // addOption() is a vararg, hence the multiple arguments here
```
### Command Groups
Command Groups are a special type of option that can ONLY be a child of a `Command`. 
```java
Command msgModCmd = new Command("message-mods", "Inform the server mods");

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
    
msgModCmd.addOption(reportUser, channelRequest);
```
## Register/Update the commands
A single class is responsible for taking instances of `Command` and upserting them to Discord. This operations usually happens during initial creations and after the dev makes changes to a command.
You always need to start by retrieving any pre-existing commands that the bot may have access to already. See the [official documentation in javacord's wiki](https://javacord.org/wiki/basic-tutorials/interactions/commands.html#get-your-commands) for the best way to accomplish this.

### Global Commands
```java
DiscordApi api; // previously initialized somehow
List<SlashCommand> existingGlobal = api.getGlobalSlashCommands().join();

// prepare the list of commands to send to Discord
SlashCommandRegister cmdRegister = new SlashCommandRegister(existingGlobal);
cmdRegister.queue(fizzBuzzCmd); // supports varargs
List<SlashCommandBuilder> toUpsert = cmdRegister.upserts();

// there is a chance that toUpsert will be empty
// IFF discord already has the commands as-is
if(!toUpsert.isEmpty()){
    api.bulkOverwriteGlobalSlashCommands(toUpsert); // actually sends the commands to Discord
}
```
### Server Commands
```java
DiscordApi api; // previously initialized somehow
Server server; // previously retrieved somehow

List<SlashCommand> existingServer = api.getServerSlashCommands(server).join();

// prepare the list of commands to send to Discord
SlashCommandRegister cmdRegister = new SlashCommandRegister(existingServer);
cmdRegister.queue(rollCmd, msgModCmd); // supports varargs
List<SlashCommandBuilder> toUpsert = cmdRegister.upserts();

// there is a chance that toUpsert will be empty
// IFF discord already has the commands as-is
if(!toUpsert.isEmpty()){
    api.bulkOverwriteServerSlashCommands(server, toUpsert); // actually sends the commands to Discord, for the given server
}
```

# Command Usages via Annotations
## Handling Command
Any public method can be used to receive a command. The method can either be a void or any return type that supports `String.valueOf(Object)`.
See the below for the multiple ways to do the same thing
```java
@SlashCommand(command = "say-hello")
public String sayHello(){
    return "hello world";
}
```
```java
@SlashCommand(command = "say-hello")
public StringBuilder sayHello(){
    return new StringBuilder().append("hello world");
}
```
```java
@SlashCommand(command = "say-hello")
public MessageBuilder sayHello(){
    return new MessageBuilder().append("hello world");
}
```
**Note:** The `MessageBuilder` doesn't actually support `String.valueOf(Object)` but some special logic was introduced to support this
```java
@SlashCommand(command = "say-hello")
public void sayHello(@SlashMeta TextChannel channel){
    new MessageBuilder().append("hello world").send(channel);
}
```
```java
@SlashCommand(command = "say-hello")
public void sayHello(@SlashMeta SlashCommandInteraction interaction){
    interaction.createImmediateResponder().append("hello world").respond();
}
```
See [Including Meta Data](#including-meta-data) on how to use the `@SlashMeta` annotation
## Handling Options
Using our [fizzBuzz](#basic-options) example from before, lets create a method that can read the `number` option. 
As long as the parameter **name and type** match an option found in the slash command, then it will be passed into the method. Otherwise it will default to null.

Because of this primates such as `int`, `long`, `double`, and `boolean` are not allowed. Please use `Integer` or `Boolean`
```java
@SlashCommand(command = "fizzbuzz")
public String handleFizzBuzz(Integer number){
    // because we had previously declared the "number" option to be required,
    // it is pretty safe to assume "number" will NOT be null
    List<String> matches = new ArrayList<String>();
    if(number % 3 == 0) matches.add("fizz");
    if(number % 5 == 0) matches.add("buzz");

    return matches.isEmpty() "No matches" ? String.join(" ", matches);
}

```
### @OptionName
Lets assume you name an option using a reserved keyword like `void`, used a dash in the option name, or simply do not want to be forced to name your parameter exactly like your option name. You can use the `@OptionName` annotation to declare the option name you wish to associate the parameter with.
```java
public String enableVoid(@OptionName("void") Boolean enabled, @OptionName("your-message") String yourMessage) {
    return enabled ? yourMessage ? null; // null will result in NO response
}

```
### Supporting Sub Commands
### Supporting Command Groups
## Including Meta Data
## Subscribing the handlers