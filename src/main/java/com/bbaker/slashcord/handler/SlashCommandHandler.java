package com.bbaker.slashcord.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import org.javacord.api.interaction.SlashCommandInteraction;

public class SlashCommandHandler implements Consumer<SlashCommandInteraction> {

    private Function<SlashCommandInteraction, Object>[] argHandlers;

    private BiConsumer<CompletableFuture<Object>, SlashCommandInteraction> responseHandler;

    private Object instance;

    private Method method;

    public SlashCommandHandler(Object instance, Method method, Function<SlashCommandInteraction, Object>[] argHandlers,
            BiConsumer<CompletableFuture<Object>, SlashCommandInteraction> responseHandler) {
        this.instance = instance;
        this.method = method;
        this.argHandlers = argHandlers;
        this.responseHandler = responseHandler;
    }

    @Override
    public void accept(SlashCommandInteraction sci) {

        Object[] args = new Object[argHandlers.length];

        for(int i = 0; i < argHandlers.length; i++) {
            args[i] = argHandlers[i].apply(sci);
        }

        CompletableFuture<Object> response = new CompletableFuture<>();

        // those the handler in a thread, which will eventually product a result
        sci.getApi().getThreadPool().getExecutorService().submit(() -> {
            try {
                Object value = method.invoke(instance, args);
                response.complete(value);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                response.completeExceptionally(e);
            }

        });

        // pass the async method to the response handler
        responseHandler.accept(response, sci);

    }

}
