# slashcord
## Summary
Adds annotation and utils for slash commands for Discord

This library is created for/dependent on [Javacord](https://github.com/Javacord/Javacord)

This library accomplishes two tasks:
1. Creating/Updating Slash Commands
2. Handling and Responding to Slash Commands

This library separates the above functionalities so you can use this library to *just* create/update commands or *just* handle/respond to commands.

[![](https://jitpack.io/v/bbaker2/slashcord.svg)](https://jitpack.io/#bbaker2/slashcord)

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
        implementation 'com.github.bbaker2:slashcord:Tag'
}

```

See the [wiki page](https://bbaker2.github.io/slashcord/) for detailed instructions on how to use this library