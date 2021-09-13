package com.bbaker.slashcord.util;

import java.lang.reflect.InvocationTargetException;

public class Tester {

    public static void main(String[] args) {

        try {
            System.out.println(Object.class.equals( NoOverride.class.getMethod("toString").getDeclaringClass() ));
            System.out.println(Object.class.equals( OverrideThis.class.getMethod("toString").getDeclaringClass() ));
            System.out.println();


            Class<? extends NoOverride> first = NoOverride.class;
            Class<? extends NoOverride> second = OverrideThis.class;
            System.out.println(
                NoOverride.class.equals(first)
            );

            System.out.println(
                NoOverride.class.equals(second)
            );

            System.out.println();

            System.out.println(
                OverrideThis.class.getConstructor().newInstance()
            );

            System.out.println(
                NoOverride.class.getConstructor().newInstance()
            );

        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }


    }

    private static class NoOverride {
        public NoOverride() {}

    }


    private static class OverrideThis extends NoOverride {

        public OverrideThis() {}

        public String toString() {
            return "neato";
        }
    }

}
