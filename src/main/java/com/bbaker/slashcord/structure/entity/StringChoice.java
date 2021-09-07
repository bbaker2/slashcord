package com.bbaker.slashcord.structure.entity;

public class StringChoice extends Choice {

    public StringChoice(String name, String value) {
        this.name = name;
        this.strString = value;
        this.intVal = -1; // magic value to prevent NPE
    }

}
