package com.shatteredpixel.shatteredpixeldungeon.items.lightblack;

import java.util.Locale;

public class Utils {

    public static String capitalize(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    public static String format(String format, Object... args) {
        return String.format(Locale.ENGLISH, format, args);
    }

}