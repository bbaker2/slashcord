package com.bbaker.slashcord.structure.results;

public class GenericError implements Error {

    private Throwable t;

    public GenericError(Throwable t) {
        this.t = t;
    }

    @Override
    public String getLoc() {
        return t.getClass().getSimpleName();
    }

    @Override
    public String getMessage() {
        return t.getMessage();
    }

    @Override
    public Throwable getThrowable() {
        return t;
    }

}
