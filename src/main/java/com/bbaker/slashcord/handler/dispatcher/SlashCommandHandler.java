package com.bbaker.slashcord.handler.dispatcher;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import org.javacord.api.interaction.SlashCommandInteraction;

public class SlashCommandHandler implements Consumer<SlashCommandInteraction> {

    private Function<SlashCommandInteraction, Object>[] argHandlers;

    private BiConsumer<Object, SlashCommandInteraction> returnHandler;

    private Object instance;

    private Method method;

    public SlashCommandHandler(Object instance, Method method, BiConsumer<Object, SlashCommandInteraction> resultHandler, Function<SlashCommandInteraction, Object>[] argHandlers) {
        this.instance = instance;
        this.method = method;
        this.returnHandler = resultHandler;
        this.argHandlers = argHandlers;
    }

    @Override
    public void accept(SlashCommandInteraction sci) {

        Object[] args = new Object[argHandlers.length];

        for(int i = 0; i < argHandlers.length; i++) {
            args[i] = argHandlers[i].apply(sci);
        }

        try {
            Object result = method.invoke(instance, args);
            returnHandler.accept(result, sci);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

}
