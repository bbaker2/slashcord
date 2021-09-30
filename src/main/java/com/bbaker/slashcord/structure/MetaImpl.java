package com.bbaker.slashcord.structure;

import com.bbaker.slashcord.structure.entity.Command;

class MetaImpl<B> implements Meta<B> {
    public MetaImpl(B trg, Command src) {
        this.def = src;
        this.builder = trg;
    }
    final B builder;
    final Command def;

    @Override
    public B getBuilder() {
        return builder;
    }
    @Override
    public Command getCommand() {
        return def;
    }
}