package com.bbaker.slashcord.handler.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.jws.soap.SOAPBinding.Use;

import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.SlashCommandOption;

/**
 * Will match an {@link SlashCommandInteractionOption} by its name and pass the value to the parameter. Supports:
 * {@link String}, {@link Integer}, {@link Boolean}, {@link Role}, {@link Use}, {@link Channel}, and {@link Mentionable}.
 * <p/>
 * Any non-required option can pass in {@code null} values<p/>
 * The value passed in here must match the {@link SlashCommandOption#getName() slash command option name}
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface SlashOption {

    String value();

}
