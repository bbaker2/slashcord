package com.bbaker.slashcord.structure.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.bbaker.slashcord.structure.entity.CommandTierIII;

@Retention(RUNTIME)
@Target({ANNOTATION_TYPE, TYPE_USE})
public @interface GroupCommandDef {

    String name() default "";
    String description() default "";
    SubCommandDef[] groups() default {};
    Class<? extends CommandTierIII> value() default CommandTierIII.class;

}
