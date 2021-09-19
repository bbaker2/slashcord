package com.bbaker.slashcord.handler.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.javacord.api.interaction.SlashCommandInteraction;

/**
 * Will match a method parameter with available values found in {@link SlashCommandInteraction}.
 * Supports: {@link SlashCommandInteraction}, {@link SlashCommandInteraction#getUser() User}, {@link SlashCommandInteraction#getApi() DiscordApi},
 * {@link SlashCommandInteraction#createImmediateResponder() InteractionImmediateResponseBuilder},
 * {@link SlashCommandInteraction#createFollowupMessageBuilder() InteractionFollowupMessageBuilder}, {@link SlashCommandInteraction#getChannel() TextChannel},
 * and {@link SlashCommandInteraction#getServer() Server}
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface SlashMeta {

}
