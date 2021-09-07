package com.bbaker.slashcord.structure.entity;

public class IntChoice extends Choice {

    public IntChoice(String name, int value) {
        this.name = name;
        this.intVal = value;
        this.strVal = null;
    }

}
