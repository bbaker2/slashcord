package com.bbaker.slashcord.structure.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.javacord.api.interaction.SlashCommandOptionType;

import com.bbaker.slashcord.structure.entity.InputOption;

@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
public @interface OptionDef {

    String name() default "";
    String description() default "";
    SlashCommandOptionType type() default SlashCommandOptionType.STRING;
    boolean required() default false;
    ChoiceDef[] choices() default {};
    Class<? extends InputOption> value() default DefaultOption.class;

    static class DefaultOption extends InputOption {
        private DefaultOption() {
            super("default", "default", false, SlashCommandOptionType.STRING);
        }
    }

}
