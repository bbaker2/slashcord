package com.bbaker.slashcord.structure.results;

public class DiscordError implements Error {

    private String path;
    private String message;
    private Throwable t;

    public DiscordError(String path, String message, Throwable t) {
        this.path = path;
        this.message = message;
        this.t = t;
    }

    @Override
    public Throwable getThrowable() {
        return this.t;
    }

    @Override
    public String getLoc() {
        return this.path;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}

