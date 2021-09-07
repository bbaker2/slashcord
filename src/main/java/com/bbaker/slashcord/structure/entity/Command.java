package com.bbaker.slashcord.structure.entity;

import static com.bbaker.slashcord.structure.util.CompareUtil.equalLists;

import java.util.List;
import java.util.Objects;

public class Command {

    private String name;
    private String description;
    private List<Option> options;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Option> getOptions() {
        return options;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Command)) {
            return super.equals(o);
        }

        Command that = (Command)o;

        if(!Objects.equals(this.getName(), that.getName())) {
            return false;
        }

         if(!Objects.equals(this.getDescription(), that.getDescription())) {
            return false;
        }

       if(!equalLists(this.getOptions(), that.getOptions())) {
           return false;
       }

       return true;
    }



}
