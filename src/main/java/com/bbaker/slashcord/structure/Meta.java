package com.bbaker.slashcord.structure;

import com.bbaker.slashcord.structure.entity.Command;

public interface Meta<B> {
    public B getBuilder();
    public Command getCommand();
}
