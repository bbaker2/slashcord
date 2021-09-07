package com.bbaker.slashcord.structure.entity;

import java.util.Objects;

public class Choice {

    protected String name;
    protected String strString;
    protected int intVal;

    public String getName() {
        return name;
    }

    public String getStrString() {
        return strString;
    }

    public int getIntVal() {
        return intVal;
    }

    public boolean equals(Object o) {
        if(o instanceof Choice) {
            return super.equals(o);
        }

        Choice that = (Choice)o;

        if(!Objects.equals(this.getName(), that.getName())) {
            return false;
        }

        if(this.getStrString() == null && that.getStrString() == null) {
            return this.getIntVal() == that.getIntVal();
        } else if(this.getStrString() == null ^ that.getStrString() == null) {
            return false;
        } else {
            return this.getStrString().equals(that.getStrString());
        }
    }

}
