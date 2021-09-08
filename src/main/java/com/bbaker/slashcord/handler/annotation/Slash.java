package com.bbaker.slashcord.handler.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface Slash {

    String command();
    String group() default "";
    String sub() default "";


}
