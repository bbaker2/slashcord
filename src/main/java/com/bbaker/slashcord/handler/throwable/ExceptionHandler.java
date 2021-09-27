package com.bbaker.slashcord.handler.throwable;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.function.Function;

public class ExceptionHandler implements Function<Throwable, String> {

    private List<Class<? extends Throwable>> whitelist;

    public ExceptionHandler(List<Class<? extends Throwable>> whitelist) {
        this.whitelist = whitelist;
    }

    @Override
    public String apply(Throwable t) {
        // Check for these common exceptions to "unload" the real exception
        Throwable realError = t;

        // The wrapper exception caused by futures
        if(realError instanceof CompletionException) {
            realError = realError.getCause();
        }

        // The wrapper exception cuased by reflection
        if(realError instanceof InvocationTargetException) {
            realError = realError.getCause();
        }


        Class<?> throwableType = realError.getClass();
        for(Class<? extends Throwable> allowed : whitelist) {
            if(throwableType.isAssignableFrom(allowed)) {
                return realError.getMessage();
            }
        }
        return "An unexpected error occured";
    }

}
