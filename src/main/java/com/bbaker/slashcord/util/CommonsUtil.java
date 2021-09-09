package com.bbaker.slashcord.util;

public class CommonsUtil {

    public static boolean isBlank(String val) {
        return val == null || val.trim().isEmpty();
    }

    public static boolean isNotBlank(String val) {
        return !isBlank(val);
    }



}
