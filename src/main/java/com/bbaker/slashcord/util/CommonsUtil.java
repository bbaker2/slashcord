package com.bbaker.slashcord.util;

import java.util.Collection;

public class CommonsUtil {

    public static boolean isBlank(String val) {
        return val == null || val.trim().isEmpty();
    }

    public static boolean isNotBlank(String val) {
        return !isBlank(val);
    }

    public static String isBlankElse(String value, String defaultVal) {
        return isBlank(value) ? defaultVal : value;
    }

	public static boolean isNotEmpty(Collection<?> c) {
	    return c != null && !c.isEmpty();
	}



}
