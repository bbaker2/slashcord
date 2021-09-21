# Summary
This is a util library to make it easier to create, manage, and use slash commands with Javacord.

You can create commands as easily as:
```java
@CommandDef(name = "ping",     description = "Will pong if pinged")
@CommandDef(name = "fizzbuzz", description = "Fizz if divisible by 3, Buzz if divisible by 5",
    options = {
        @OptionDef(name = "number", description = "Fizz if divisible by 3, Buzz if divisible by 5",
            type = INTEGER, required = true)
    }
)
public class ExampleCommand {

    @Slash(command = "ping")
    public String ping() {
        return "pong";
    }

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
}

/** During Bot Initialization */
DiscordApi api = getApiSomehow();
SlashCommandDispatcher dispatcher = new SlashCommandDispatcher(api);
dispatcher.queue(new ExampleCommand());
dispatcher.submit(); 
```

# Tutorials
For creating and updating commands, see
* [Managing Commands via Objects](tutorial/define-classes.md)
* [Managing Commands via Annotations](tutorial/define-annotations.md)

For responding to your commands, see
* [Command Listeners](tutorial/responding.md)

It is also worth checking out [SlashCommandDispatcher](tutorial/slashcommanddispatcher.md)


# Installing and Setup
## Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.bbaker2</groupId>
    <artifactId>slashcord</artifactId>
    <version>1.1.0</version>
</dependency>
```
## Gradle
```javascript
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
        implementation 'com.github.bbaker2:slashcord:1.1.0'
}

```

# References
* [Javacord](https://github.com/Javacord/Javacord)
* [Javacord Tutorals](https://javacord.org/wiki/basic-tutorials/interactions/commands.html)
* [Discord APIs for slash commands](https://discord.com/developers/docs/interactions/application-commands)
