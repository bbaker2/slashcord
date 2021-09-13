package com.bbaker.slashcord.structure.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.bbaker.slashcord.structure.entity.SubCommand;

@Retention(RUNTIME)
@Target({ANNOTATION_TYPE, TYPE})
public @interface SubCommandDef {

    String name() default "";
    String description() default "";
    CommandDef[] subs() default {};
    Class<? extends SubCommand> value() default DefaultSubCommand.class;

    static class DefaultSubCommand extends SubCommand {

        private DefaultSubCommand() {
            super("default", "default");
        }

    }


}
