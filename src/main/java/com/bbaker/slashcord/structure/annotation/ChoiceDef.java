package com.bbaker.slashcord.structure.annotation;

public @interface ChoiceDef {

    String name();
    String strVal() default "";
    int intVal() default 0;

}
