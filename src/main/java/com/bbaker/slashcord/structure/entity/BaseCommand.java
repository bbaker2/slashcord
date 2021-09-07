package com.bbaker.slashcord.structure.entity;

import java.util.ArrayList;

public class BaseCommand<T extends Option> extends Command {

    public BaseCommand(String name, String description) {
        this.name = name;
        this.description = description;
        this.options = new ArrayList<>();
    }

    public BaseCommand<T> addOption(T... options) {
        for(Option o : options) {
            this.options.add(o);
        }
        return this;
    }

}
