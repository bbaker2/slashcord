package com.bbaker.slashcord.structure.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.bbaker.slashcord.structure.entity.RegularCommand;

@Retention(RUNTIME)
@Target(TYPE)
public @interface CommandDef {

    String name() default "";
    String description() default "";
    OptionDef[] options() default {};
    Class<? extends RegularCommand> value() default DefaultRegularCommand.class;

    static class DefaultRegularCommand extends RegularCommand {

        private DefaultRegularCommand() {
            super("default", "default");
        }

    }
}
