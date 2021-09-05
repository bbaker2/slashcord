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
Using our [fizzbuzz](define-classes.md#basic-options) example from before, lets create a method that can read the `number` option. 
As long as the parameter **name and type** match an option found in the slash command, then it will be passed into the method. Otherwise it will default to null.

Because of this primates such as `int`, `long`, `double`, and `boolean` are not allowed. Please use `Integer` or `Boolean`.
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
@SlashCommand(command = "repeat-back")
public String enableVoid(@OptionName("void") Boolean enabled, @OptionName("your-message") String yourMessage) {
    return enabled ? yourMessage ? null; // null will result in NO response
}
```
### Supporting Sub Commands
Using our [role](define-classes.md#sub-commands) example from before, lets create a method that only is called for a specific sub command.
```java
@SlashCommand(command = "role", sub = "add")
public void addRole(@SlashMeta User caller, Role role, InteractionFollowupMessageBuilder response) {
    response.setFlags(MessageFlag.EPHEMERAL); // only visible to the calling user
    caller.addRole(role)
        .thenAccept(success -> {
            response.append("Added to role").send();
        })
        .exceptionally(error -> {
            response.append("Unsuccessful: ").append(error.getMessage()).send();
            return null;
        });
}

@SlashCommand(command = "role", sub = "remove")
public void removeRole(@SlashMeta User caller, Role role, InteractionFollowupMessageBuilder response) {
    response.setFlags(MessageFlag.EPHEMERAL); // only visible to the calling user
    caller.removeRole(role)
        .thenAccept(success -> {
            response.append("Added to role").send();
        })
        .exceptionally(error -> {
            response.append("Unsuccessful: ").append(error.getMessage()).send();
            return null;
        });
}
```

### Supporting Command Groups
Using our [message-mods](define-classes.md#command-groups) example from before, lets create a method that only is called for a specific sub command within groups.
```java
@SlashCommand(command = "message-mods" group = "report-user", sub = "spamming")
public void reportSpamming(@SlashMeta User caller, User spammer,
    String reason, InteractionImmediateResponseBuilder response) {

    reportUserForSpamming(spammer, reason);
    response.setFlags(MessageFlag.EPHEMERAL); // only visible to the calling user
    response.append(spammer).append(" has been reported for spamming.")
    response.respond();
}

@SlashCommand(command = "message-mods" group = "report-user", sub = "other")
public void reportSpamming(@SlashMeta User caller, User offender,
    String reason, InteractionImmediateResponseBuilder response) {

    reportUserGeneric(offender, reason);
    response.setFlags(MessageFlag.EPHEMERAL); // only visible to the calling user
    response.append(offender).append(" has been reported.")
    response.respond();
}

// And repeat for racism and sexism
```
## @SlahMeta
Sometimes you need extra information that is NOT an option. And sometimes those values have naming/class-type conflicts with Options. To avoid this problem, you can use the `@SlashMeta` annotation.

The `@SlahMeta` annotation declares that a given method parameter is NOT an option and will instead come from the `SlashCommandInteraction` class instead. Common parameter types include:

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

## Subscribing the handlers
Now that you have some methods that are able to receive and respond to slash commands, we need subscribe our classes to the event handler.
That may sound scary, but it is actually quite easy thanks to `SlashCommandDispatcher`
```java
DiscordApi api; // previously initialized
SlashCommandDispatcher dispatcher = new SlashCommandDispatcher(api);
dispatcher.subscribe(new FizzBuzz()); // This class is assumed to have methods with @SlashCommand

MessageMods msgModsCmd = new MessageMods();
RoleAssigner roleCmd = RoelAssigner();
dispatcher.subscribe(msgModsCmd, roleCmd); // and we support varags

dispatcher.subscribe(new Object() { // or go crazy and create an ad-hoc method
    @SlashCommand(command = "foo")
    public String ifYouActuallyUseAnAnonymousClassLikeThisIWillKillYou() {
        return "bar";
    }
});
```

That's it.