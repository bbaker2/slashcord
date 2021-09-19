package com.bbaker.slashcord.structure.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.bbaker.slashcord.structure.entity.Command;
import com.bbaker.slashcord.structure.entity.SubCommand;

/**
 * Defines a slash command that has sub-commands -> options<p/>
 * This is the annotation representation of {@link SubCommand}
 * @see SubCommand
 */
@Retention(RUNTIME)
@Target({ANNOTATION_TYPE, TYPE})
@Repeatable(SubCommandDefs.class)
public @interface SubCommandDef {

    /**
     * The name of the command itself.<br/>
     * Or the name of the group if nested inside of of {@link GroupCommandDef#groups()}<br/>
     *  Must be populated if {@link #value()} is not.
     */
    String name() default "";

    /**
     * The description of the command itself.<br/>
     * Or the description of the group if nested inside of a {@link GroupCommandDef#groups()}<br/>
     * Must be populated if {@link #value()} is not.
     */
    String description() default "";


    /**
     * The list of inner commands.<br/>
     * Must be populated if {@link #value()} is not.
     * @see Command
     */
    CommandDef[] subs() default {};

    Class<? extends SubCommand> value() default DefaultSubCommand.class;

    static class DefaultSubCommand extends SubCommand {
        // private constructor so that no one can create an instance
        private DefaultSubCommand() {
            super("default", "default");
        }

    }


}
