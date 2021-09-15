package com.bbaker.slashcord.structure.entity;

import static com.bbaker.slashcord.util.CommonsUtil.isBlank;

import java.util.Objects;

public class Choice {

    protected String name;
    protected String strVal = null;
    protected int intVal = -1;

    public String getName() {
        return name;
    }

    public String getStrVal() {
        return strVal;
    }

    public int getIntVal() {
        return intVal;
    }

    public boolean equals(Object o) {
        if(!(o instanceof Choice)) {
            return super.equals(o);
        }

        Choice that = (Choice)o;

        if(!Objects.equals(this.getName(), that.getName())) {
            return false;
        }

        // If both are blank, then check the numbers
        if(isBlank(this.getStrVal()) && isBlank(that.getStrVal())) {
            return this.getIntVal() == that.getIntVal();

        // If only one is blank, then this is a string value and does not match
        } else if(isBlank(this.getStrVal()) ^ isBlank(that.getStrVal())) {
            return false;

        // Otherwise, assume both string values exist and comapre the two
        } else {
            return this.getStrVal().equals(that.getStrVal());
        }
    }

    @Override
    public String toString() {
        return getName();
    }
}
