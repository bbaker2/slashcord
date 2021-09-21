package com.bbaker.slashcord.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.interaction.SlashCommandInteraction;

public class SlashCommandHandler implements Consumer<SlashCommandInteraction> {

    private Function<SlashCommandInteraction, Object>[] argHandlers;

    private BiConsumer<Object, SlashCommandInteraction> returnHandler;

    private Object instance;

    private Method method;

    private Function<Throwable, String> exceptionHandler;

    public SlashCommandHandler(Object instance, Method method, BiConsumer<Object, SlashCommandInteraction> resultHandler,
            Function<SlashCommandInteraction, Object>[] argHandlers, Function<Throwable, String> exceptionHandler) {
        this.instance = instance;
        this.method = method;
        this.returnHandler = resultHandler;
        this.argHandlers = argHandlers;
        this.exceptionHandler = exceptionHandler;
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
        } catch (InvocationTargetException e) {
            String error = exceptionHandler.apply(e.getTargetException());
            if(error != null) {
                sci.createImmediateResponder()
                    .append(error)
                    .setFlags(MessageFlag.EPHEMERAL)
                    .respond();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

}
