# SlashCommandDispatcher
So assuming you have classes that can define a command and you have some classes that can respond to a command... 
you _could_ use `SlashCommandRegister` and `SlashCommandListener`.

Or you can use the `SlashCommandDispatcher`. It combines the functionality of `SlashCommandRegister` and `SlashCommandListener` in one easy-to-use class.

```java
public void init(DiscordApi api){
    SlashCommandDispatcher dispatcher = SlashCommandDispatcher.getInstance(api);
    dispatcher.queue(createPingPong(),          new PingPongCommand());
    dispatcher.queue(createFizzBuzzCommand(),   new FizzBuzz());
    dispatcher.queue(createModsCommand(),       new QuoteCommand());
    dispatcher.queue(createQuoteCommand(),      new ModCommand());
    dispatcher.submit().join();
}

private Command createPingPong() {
    return new RegularCommand("ping", "Will Ping");
}

private Command createFizzBuzzCommand() {
     return new RegularCommand("fizzbuzz", "Fizz if divisible by 3, Buzz if divisible by 5")
         .addOption(
             new IntOption("number", "Any whole number", true)
     );
}

private Command createQuoteCommand() {
    SubCommand quote = new SubCommand("quote", "For quoting funny things in the server");
    quote.addOption(
        new SubOption("add", "Add a quote").addOptions(
            new StringOption("quote", "The quote itself", true),
            new UserOption("user", "Who said the quote?", false)
        ),
        new SubOption("random", "Output a random quote")
    );

    return quote;
}


private Command createModsCommand() {
    GroupCommand mod    = new GroupCommand("mod",      "Useful commands for the server mods");
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

    return mod;
}
```