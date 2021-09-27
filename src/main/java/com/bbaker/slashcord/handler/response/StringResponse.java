package com.bbaker.slashcord.handler.response;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.javacord.api.interaction.SlashCommandInteraction;

public class StringResponse implements BiConsumer<CompletableFuture<Object>, SlashCommandInteraction> {

    private final int DELAY = 2750; // 2.75 seconds
    private Function<Throwable, String> exceptionHandler;

    public StringResponse(Function<Throwable, String> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void accept(CompletableFuture<Object> response, SlashCommandInteraction sci) {

        Future<?> responseLater = respondeLater(sci);

        Object val = response.exceptionally(exceptionHandler).join();
        String msg = String.valueOf(val);
        // IF the responseLater() has not yet complete
        // AND we can successfully cancel the responseLater()
        // we assume we can reply with a immediate response
        boolean immediate = !responseLater.isDone() && responseLater.cancel(true);

        if(immediate) {
            sci.createImmediateResponder().append(msg).respond();;
        } else {
            sci.createFollowupMessageBuilder().append(msg).send();
        }
    }

    private Future<?> respondeLater(final SlashCommandInteraction sci){

        ExecutorService executor = sci.getApi().getThreadPool().getExecutorService();

        // Return a future that will respond later after a delay
        // Note: an anonymous class is used instead of a lambda to avoid
        // 		 issues with lexical scoping
        return executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    return; // silently short circuit
                }

                sci.respondLater().join();
            }
        });
    }

}
