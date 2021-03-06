package com.bbaker.slashcord.structure.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.bbaker.slashcord.structure.entity.Choice;

@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
public @interface ChoiceDef {

    String name() default "";
    String strVal() default "";
    int intVal() default 0;
    Class<? extends Choice> value() default DefaultChoice.class;

    static class DefaultChoice extends Choice {

        private DefaultChoice() { }

    }

}
