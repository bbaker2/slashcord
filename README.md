# slashcord
## Summary
Adds annotation and utils for slash commands for Discord

This library is created for/dependent on [Javacord](https://github.com/Javacord/Javacord)

This library accomplishes two tasks:
1. Creating/Updating Slash Commands
2. Handling and Responding to Slash Commands

This library separates the above functionalities so you can use this library to *just* create/update commands or *just* handle/respond to commands.

[![](https://jitpack.io/v/bbaker2/slashcord.svg)](https://jitpack.io/#bbaker2/slashcord)

## Example Usage
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
See the [wiki page](https://bbaker2.github.io/slashcord/) for detailed instructions on how to use this library
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
    <version>1.0.0</version>
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
        implementation 'com.github.bbaker2:slashcord:1.0.0'
}

```