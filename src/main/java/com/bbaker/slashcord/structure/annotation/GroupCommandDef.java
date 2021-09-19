package com.bbaker.slashcord.structure.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.bbaker.slashcord.structure.entity.GroupCommand;
import com.bbaker.slashcord.structure.entity.SubCommand;

/**
 * Defines a slash command that has groups -> sub-commands -> options. <p/>
 * This is the annotation representation of {@link GroupCommand}
 * @see GroupCommand
 */
@Retention(RUNTIME)
@Target(TYPE)
@Repeatable(GroupCommandDefs.class)
public @interface GroupCommandDef {

    /**
     * The name of the command itself.<br/>
     * Must be populated if {@link #value()} is not.
     */
    String name() default "";
    /**
     * The description of the command.<br/>
     * Must be populated if {@link #value()} is not.
     */
    String description() default "";
    /**
     * The list of inner groups.<br/>
     *  Must be populated if {@link #value()} is not.
     *  @see SubCommand
     */
    SubCommandDef[] groups() default {};

    /**
     * An alternative to populating {@link #name()}, {@link #description()}, and {@link #groups()}.
     * If populated, will instead create an instance of the class and use that instead.
     */
    Class<? extends GroupCommand> value() default DefaultGroupCommand.class;

    static class DefaultGroupCommand extends GroupCommand {

        // private constructor so that no one can create an instance
        private DefaultGroupCommand() {
            super("default", "default");
        }

    }

}
