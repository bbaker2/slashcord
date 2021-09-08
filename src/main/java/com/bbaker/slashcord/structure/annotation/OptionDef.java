package com.bbaker.slashcord.structure.annotation;

import org.javacord.api.interaction.SlashCommandOptionType;

public @interface OptionDef {

    String name();
    String description();
    SlashCommandOptionType type();
    boolean required() default false;
    ChoiceDef[] choices() default {};


}
