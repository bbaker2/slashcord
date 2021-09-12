package com.bbaker.slashcord.handler.args;

import java.util.Optional;
import java.util.function.Function;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.SlashCommandInteractionOptionsProvider;

public abstract class AbstractOptionArgument<T> implements Function<SlashCommandInteraction, Object> {

    protected String name;

    protected AbstractOptionArgument(String name) {
        this.name = name;
    }

    @Override
    public Object apply(SlashCommandInteraction sci) {
        Optional<SlashCommandInteractionOption> first = sci.getFirstOption();
        if(!first.isPresent()) {
            return null;
        }
        SlashCommandInteractionOption level1 = first.get();
        if(level1.isSubcommandOrGroup()) {
            Optional<SlashCommandInteractionOption> second = level1.getFirstOption();
            if(second.isPresent()) {
                SlashCommandInteractionOption level2 = second.get();
                if(level2.isSubcommandOrGroup()) {
                    return getValue(level2);
                }
                return getValue(level1);
            } else {
                return null;
            }
        }
        return getValue(sci);
    }

    abstract protected T getValue(SlashCommandInteractionOptionsProvider interaction);

}
