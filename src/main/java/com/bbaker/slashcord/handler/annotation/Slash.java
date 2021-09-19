package com.bbaker.slashcord.handler.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.SlashCommandOptionType;

/**
 * Will wire a public method to a {@link SlashCommandCreateEvent}. The method must either be:
 * <ul>
 * 	<li>Void</li>
 *	<li>String</li>
 *	<li>Any Class that overrides the {@link #toString()}</li>
 * </ul>
 * @see SlashMeta @SlashMeta is used to pass {@link SlashCommandInteractionOption} to method parameters
 * @see SlashOption @slashOption is used to certain values from {@link SlashCommandInteraction} to a method parameters
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Slash {

    /**
     * The name of the command. What will be the "/{command}".
     */
    String command();

    /**
     * If this command has group options ({@link SlashCommandOptionType#SUB_COMMAND_GROUP}),
     * this is how you reference them.
     */
    String group() default "";

    /**
     * If this command has sub-command options ({@link SlashCommandOptionType#SUB_COMMAND}),
     * this is how you reference them.
     */
    String sub() default "";


}
