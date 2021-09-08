package com.bbaker.slashcord.structure.entity;

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
        if(o instanceof Choice) {
            return super.equals(o);
        }

        Choice that = (Choice)o;

        if(!Objects.equals(this.getName(), that.getName())) {
            return false;
        }

        if(this.getStrVal() == null && that.getStrVal() == null) {
            return this.getIntVal() == that.getIntVal();
        } else if(this.getStrVal() == null ^ that.getStrVal() == null) {
            return false;
        } else {
            return this.getStrVal().equals(that.getStrVal());
        }
    }

    @Override
    public String toString() {
        return getName();
    }
}
