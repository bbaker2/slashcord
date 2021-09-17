package com.bbaker.slashcord.structure.results;

public interface Error {

    public Throwable getThrowable();
    public String getLoc();
    public String getMessage();

}
