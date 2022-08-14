package com.shatteredpixel.shatteredpixeldungeon;

public class DLC {

    public static final int BOSSRUSH = 1;
    public static final int BACKGO = 2;

    public static final int MAX_VALUE = 65536;

    public static final String[] NAME_IDS = {
            "bossrush",
            "back_go"
    };

    public static final int[] MASKS = {
            BOSSRUSH, BACKGO
    };

    public String name;
}
