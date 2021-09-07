package com.bbaker.slashcord.structure.annotation;

public @interface CommandDef {

    String name();
    String description();
    OptionDef[] options();
}
