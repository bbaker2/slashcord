package com.bbaker.slashcord.handler.throwable;

import java.util.List;
import java.util.function.Function;

public class ExceptionHandler implements Function<Throwable, String> {

    private List<Class<? extends Throwable>> whitelist;

    public ExceptionHandler(List<Class<? extends Throwable>> whitelist) {
        this.whitelist = whitelist;
    }

    @Override
    public String apply(Throwable t) {
        Class<?> throwableType = t.getClass();
        for(Class<? extends Throwable> allowed : whitelist) {
            if(throwableType.isAssignableFrom(allowed)) {
                return t.getMessage();
            }
        }
        return null;
    }

}
