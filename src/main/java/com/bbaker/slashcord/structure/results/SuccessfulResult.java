package com.bbaker.slashcord.structure.results;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.javacord.api.interaction.SlashCommand;

import com.bbaker.slashcord.structure.entity.Command;

public class SuccessfulResult implements UpsertResult {

    private Command def;
    private SlashCommand sc;
    private Operation operation;

    public SuccessfulResult(Command def, SlashCommand sc, Operation operation) {
        this.def = def;
        this.sc = sc;
        this.operation = operation;
    }

    @Override
    public SlashCommand getCommand() {
        return this.sc;
    }

    @Override
    public Command getDefinition() {
        return this.def;
    }

    @Override
    public Operation getOperartion() {
        return this.operation;
    }

    @Override
    public boolean successful() {
        return true;
    }

    @Override
    public List<Error> getMessage() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return String.format("%-16s %8s was successful",
                "["+def.getName()+"]",
                operation);
    }

    public static Function<SlashCommand, UpsertResult> insert(Command def) {
        return sc -> new SuccessfulResult(def, sc, Operation.INSERT);
    }

    public static Function<SlashCommand, UpsertResult> update(Command def) {
        return sc -> new SuccessfulResult(def, sc, Operation.UPDATE);
    }

}
