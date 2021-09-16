package com.bbaker.slashcord.structure;

import java.util.Arrays;
import java.util.List;

import org.javacord.api.interaction.SlashCommand;

public class UpsertResults {

    public static UpsertResult updated(final SlashCommand sc) {
        return new UpsertResult() {

            @Override
            public boolean successful() {
                return true;
            }

            @Override
            public Operation getOperartion() {
                return Operation.UPDATE;
            }

            @Override
            public List<String> getMessage() {
                return Arrays.asList();
            }

            @Override
            public SlashCommand getCommand() {
                return sc;
            }
        };
    }

    public static UpsertResult updated(final Throwable t) {
        return new UpsertResult() {

            @Override
            public boolean successful() {
                return false;
            }

            @Override
            public Operation getOperartion() {
                return Operation.UPDATE;
            }

            @Override
            public List<String> getMessage() {
                return BadRequestParser.parseThrowable(t);
            }

            @Override
            public SlashCommand getCommand() {
                return null;
            }
        };
    }

    public static UpsertResult inserted(final SlashCommand sc) {
        return new UpsertResult() {

            @Override
            public boolean successful() {
                return true;
            }

            @Override
            public Operation getOperartion() {
                return Operation.INSERT;
            }

            @Override
            public List<String> getMessage() {
                return Arrays.asList();
            }

            @Override
            public SlashCommand getCommand() {
                return sc;
            }
        };
    }

    public static UpsertResult inserted(final Throwable t) {
        return new UpsertResult() {

            @Override
            public boolean successful() {
                return false;
            }

            @Override
            public Operation getOperartion() {
                return Operation.INSERT;
            }

            @Override
            public List<String> getMessage() {
                return BadRequestParser.parseThrowable(t);
            }

            @Override
            public SlashCommand getCommand() {
                return null;
            }
        };
    }

}
