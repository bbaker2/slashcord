package com.bbaker.slashcord.handler.args;

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
        SlashCommandInteractionOption level1 = sci.getFirstOption().get();
        if(level1.isSubcommandOrGroup()) {
            if(level1.getFirstOption().isPresent()) {
                SlashCommandInteractionOption level2 = level1.getFirstOption().get();
                if(level2.isSubcommandOrGroup()) {
                    return getValue(level2);
                }
            }
            return getValue(level1);
        }
        return getValue(sci);
    }

    abstract protected T getValue(SlashCommandInteractionOptionsProvider interaction);

}
