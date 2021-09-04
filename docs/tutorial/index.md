# Introduction
Before you can use a command, you have to create it first.

This library provides two ways to create the same command. It's just a matter of preference and coding style

Builder Classes | Annotations
--------------- | -----------
Define your command structure with some classes and queue it up to be registered or synced with Discord | Define your command with annotations and queue it up to be registered or synced with Discord
Can programmatically create/update command structures | Statically structured. Requires recompilation to change the command structure
Ideal for large or nested commands | Ideal for quick and simple commands