package com.bbaker.slashcord.structure.entity;

import static com.bbaker.slashcord.util.CompareUtil.equalLists;
import static java.util.stream.Collectors.joining;

import java.util.List;
import java.util.Objects;

public class Command {

    protected String name;
    protected String description;
    protected List<Option> options;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getName());
        if(getOptions() != null && !getOptions().isEmpty()) {
            sb.append(System.lineSeparator());
            sb.append(
                getOptions().stream()
                    .map(o -> o.toString("  "))
                    .collect(joining(System.lineSeparator()))
            );
        }
        return sb.toString();
    }



}
