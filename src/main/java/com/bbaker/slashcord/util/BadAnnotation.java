package com.bbaker.slashcord.util;

public class BadAnnotation extends Exception {

    public BadAnnotation(String name, Throwable t) {
        super("Error while constructing: " + name, t);
    }

    public BadAnnotation(String message) {
        super(message);
    }

}
