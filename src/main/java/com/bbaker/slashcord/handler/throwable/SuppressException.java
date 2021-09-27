package com.bbaker.slashcord.handler.throwable;

import java.util.function.Function;

public class SuppressException implements Function<Throwable, String> {

    @Override
    public String apply(Throwable t) {
        // skip all evaluations, just return a default message
        return "An unexpected error occured";
    }

}
