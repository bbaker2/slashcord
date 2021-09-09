package com.bbaker.slashcord.structure.entity;

import java.util.ArrayList;

import org.javacord.api.interaction.SlashCommandOptionType;

public class SubOption extends Option {

     public SubOption(String name, String description) {
            this.name = name;
            this.description = description;
            this.type = SlashCommandOptionType.SUB_COMMAND;
            this.choices = null;
            this.options = new ArrayList<>();
            this.required = false;
        }

        public SubOption addOptions(InputOption...options) {
            for(InputOption sub : options) {
                this.options.add(sub);
            }
            return this;
        }

}
