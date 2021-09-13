package com.bbaker.slashcord.structure.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.bbaker.slashcord.structure.entity.CommandTierII;

@Retention(RUNTIME)
@Target({ANNOTATION_TYPE, TYPE})
public @interface SubCommandDef {

    String name() default "";
    String description() default "";
    CommandDef[] subs() default {};
    Class<? extends CommandTierII> value() default CommandTierII.class;


}
