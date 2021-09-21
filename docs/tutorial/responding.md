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
**Note:** The `MessageBuilder` doesn't actually support `String.valueOf(Object)` but some special logic was introduced to support this. A standard message will be sent to the channel but a "success" hidden message will be returned to the calling user.
**Warning:** `MessageBuilder` might be removed after beta. This has some weird interactions that I don't like
```java
@SlashCommand(command = "say-hello")
public void sayHello(@SlashMeta SlashCommandInteraction interaction){
    interaction.createImmediateResponder().append("hello world").respond();
}
```
```java
@SlashCommand(command = "say-hello")
public void sayHello(@SlashMeta InteractionImmediateResponseBuilder response){
    response.append("hello world").respond();
}
```
```java
@SlashCommand(command = "say-hello")
public void sayHello(@SlashMeta InteractionImmediateResponseBuilder response){
    response.append("hello world").respond();
}
```
```java
@SlashCommand(command = "say-hello")
public void sayHello(@SlashMeta InteractionFollowupMessageBuilder response){
    // warning: you should probably have some additional code that calls 
    // InteractionImmediateResponseBuilder.respondLater() if you plan on replying after 3 seconds
    response.append("hello world").send();
}
```
See [@SlashMeta](#slashmeta) on how to use the `@SlashMeta` annotation
## Handling Options
Using our [fizzbuzz](define-classes.md#basic-options) example from before, lets create a method that can read the `number` option. 
As long as the parameter **name and type** match an option found in the slash command, then it will be passed into the method. Otherwise it will default to null.

Because of this primates such as `int`, `long`, `double`, and `boolean` are not allowed. Please use `Integer` or `Boolean`.
You must also denote which method parameters are tethered to a command option by using the `@OptionName` annotation. 

```java
@Slash(command = "fizzbuzz")
public String fizzBuzz(@SlashOption("number") Integer number) {
    StringBuilder sb = new StringBuilder();
    if(number % 3 == 0) sb.append("fizz");

    if(number % 5 == 0) {
        if(sb.length() > 0) sb.append(" ");
        sb.append("buzz");
    }
    return sb.length() == 0 ? "No matches" : sb.toString();
}
```
_Why do I need to use `@OptionName`?_
> Java does not guarantee that the parameter name is available during runtime.
> If you read the official docs regarding [Obtaining Names of Method Parameters](https://docs.oracle.com/javase/tutorial/reflect/member/methodparameterreflection.html) you will see that the parameter name is only preserved when "... compile the source file with the `-parameters` option to the `javac` compiler."
> As the developer, you are in control of how you compile your code, so if you *do* use the `-parameters` option, you do *not*need `@SlashOption` annotation.
> But it is recommended that you do so anyways for consistency and stability.
```java
@Slash(command = "fizzbuzz")
public String fizzBuzz(Integer number) {
    return "This will match with the 'number' named option if you use the -parameters option while compiling\n"
    + "otherwise, the name of this parameter will be 'arg0'";
}
```

### Supporting Sub Commands
Using our [quote](define-classes.md#sub-commands) example from before, lets create a method that only is called for a specific sub command.
```java
@Slash( command = "quote", sub = "add")
public void addQuote(@SlashOption("quote") String quote, @SlashOption("user") User user,
        InteractionImmediateResponseBuilder response) {
    // Since the "user" option is optional, we cannot assume it will be populated
    if(user == null) {
        storeAnonymousQuote(quote);
    } else {
        storeNamedQuote(quote, user);
    }

    response.append("Quote added").setFlags(MessageFlag.EPHEMERAL).respond();
}

@Slash( command = "quote", sub = "random")
public void randomQuote(InteractionImmediateResponseBuilder response) {
    String randomQuote = getRandomQuote();
    response.append(randomQuote).respond();
}
```

### Supporting Command Groups
Using our [mods](define-classes.md#command-groups) example from before, lets create a method that only is called for a specific sub command within groups.
```java
@Slash( command = "mod", group = "add", sub = "role")
public MessageBuilder addRole(
        @SlashOption("user") User user,
        @SlashOption("role") Role role) {
    return new MessageBuilder()
            .append("Adding ").append(user)
            .append(" to ").append(role);
}

@Slash( command = "mod", group = "add", sub = "channel")
public String addUser(
        @SlashOption("user") User user,
        @SlashOption("channel") ServerChannel channel) {
    return "Adding " + user.getName() + " to " + channel.getName();
}

@Slash( command = "mod", group = "remove", sub = "role")
public String removeRole(
        @SlashOption("user") User user,
        @SlashOption("role") Role role) {
    return "Removing " + user.getName() + " from " + role.getName();
}

@Slash( command = "mod", group = "remove", sub = "channel")
public String removeChannel(
        @SlashOption("user") User user,
        @SlashOption("channel") ServerChannel channel) {
    return "Removing " + user.getName() + " from " + channel.getName();
}
```
## @SlashMeta
Sometimes you need extra information that is NOT an option. And sometimes those values have naming/class-type conflicts with Options. To avoid this problem, you can use the `@SlashMeta` annotation.

The `@SlashMeta` annotation declares that a given method parameter is NOT an option and will instead come from the `SlashCommandInteraction` class instead. Common parameter types include:

Class | Purpose
----- | -------
User | The user who triggered the slash command
Channel | The channel this command was triggered from
Server | The server this command was triggered from

```java
@SlashCommand(command = "example")
public MessageBuilder optionLessExample(@SlashMeta User caller, @SlashMeta Channel channel, @SlashMeta Server server) {
    MessageBuilder mb = new MessageBuilder();
    mb.append(caller)
    mb.append(" called this command from channel ").append(channel)
    mb.append(" while in server ").append(server.getName());
    return mb;
}
```

Other values derived from the `SlashCommandInteraction` will not conflict with Options and therefor will **not** require the `@SlashMeta` annotation. But there is no consequence if you use them for the following parameter types:
- `DiscordApi`
- `SlashCommandInteraction`
- `InteractionImmediateResponseBuilder`
- `InteractionFollowupMessageBuilder`

## @SlashException
You can whitelist any class that extends `Throwable` to allow the `Throwable.getMessage()` to trickle back to the user. Any exception not declared by `@SlashException` or returns a `null` message will result in failed interaction.
```java
@SlashException(ArithmeticException.class)
@Slash(command = "divide")
public String divide(@SlashOption("number") Integer val) {
    return String.valueOf(100/val);
}

```

## Subscribing the handlers
Now that you have some methods that are able to receive and respond to slash commands, we need subscribe our classes to the event handler.
That may sound scary, but it is actually quite easy thanks to `SlashCommandListener`
```java
DiscordApi api = getApi();

SlashCommandListener listener = new SlashCommandListener();
listener.addListener(new FizzBuzz()); // This class is assumed to have methods with @SlashCommand

api.addSlashCommandCreateListener(listener);
```

That's it.