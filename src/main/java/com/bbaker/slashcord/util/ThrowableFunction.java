package com.bbaker.slashcord.util;

@FunctionalInterface
public interface ThrowableFunction <INPUT, OUTPUT, THROWS extends Throwable>{

    OUTPUT apply(INPUT input) throws THROWS;

}
