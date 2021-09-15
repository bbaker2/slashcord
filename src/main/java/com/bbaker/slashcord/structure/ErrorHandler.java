package com.bbaker.slashcord.structure;

import java.util.function.Function;

import org.javacord.api.exception.BadRequestException;

import com.bbaker.slashcord.structure.entity.Command;

public class ErrorHandler implements Function<Exception, UpsertResult> {

    private Command srcCmd;

    @Override
    public UpsertResult apply(Exception e) {
        String errorMsg;
        if(e.getCause() instanceof BadRequestException) {
            errorMsg = parseException((BadRequestException)e);
        } else {
            errorMsg = e.getMessage();
        }
        return null;
    }

    private String parseException(BadRequestException e) {
        return null;
    }

}
