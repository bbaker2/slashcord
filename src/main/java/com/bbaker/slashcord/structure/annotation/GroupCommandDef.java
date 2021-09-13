package com.bbaker.slashcord.structure.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.bbaker.slashcord.structure.entity.GroupCommand;

@Retention(RUNTIME)
@Target({ANNOTATION_TYPE, TYPE})
public @interface GroupCommandDef {

    String name() default "";
    String description() default "";
    SubCommandDef[] groups() default {};
    Class<? extends GroupCommand> value() default DefaultGroupCommand.class;

    static class DefaultGroupCommand extends GroupCommand {

        private DefaultGroupCommand() {
            super("default", "default");
        }

    }

}
