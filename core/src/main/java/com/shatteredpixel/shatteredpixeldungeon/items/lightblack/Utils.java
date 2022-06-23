package com.shatteredpixel.shatteredpixeldungeon.items.lightblack;

import java.util.Locale;

public class Utils {
    public static String VOWELS = "aoeiu";

    public static String capitalize(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    public static String format(String format, Object... args) {
        return String.format(Locale.ENGLISH, format, args);
    }

    public static String indefinite(String noun) {
        if (noun.length() == 0) {
            return "a";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(VOWELS.indexOf(Character.toLowerCase(noun.charAt(0))) != -1 ? "an " : "a ");
        sb.append(noun);
        return sb.toString();
    }
}