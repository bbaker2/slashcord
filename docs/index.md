# Summary
This is a util library to make it easier to create, manage, and use slash commands with Javacord.

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
    <version>0.1.3</version>
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
        implementation 'com.github.bbaker2:slashcord:0.1.3'
}

```

# References
* [Javacord](https://github.com/Javacord/Javacord)
* [Javacord Tutorals](https://javacord.org/wiki/basic-tutorials/interactions/commands.html)
* [Discord APIs for slash commands](https://discord.com/developers/docs/interactions/application-commands)
