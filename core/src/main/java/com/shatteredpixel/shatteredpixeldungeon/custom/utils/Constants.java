package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import com.watabou.noosa.Game;

public class Constants {
    public static final int MAX_DEPTH = 26;

    public static final int MAX_QUICKSLOTS = 12;
    public static final int MOX_QUICKSLOTS = 8;
    public static final int MIN_QUICKSLOTS = 3;

    public static boolean gameIsAndroid(){
        return Game.platform.getClass().getSimpleName().contains("Android");
    }
}
