package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import com.watabou.noosa.Game;

public class Constants {

    //Chapter length.
    public static final int CHAPTER_LENGTH = 5;

    //Number of chapters.
    public static final int NUM_CHAPTERS = 5;

    //Bonus floors.
    public static final int BONUS_FLOORS = 1;

    //Number of floors.
    public static final int MAX_DEPTH = CHAPTER_LENGTH * NUM_CHAPTERS + BONUS_FLOORS;

    //
    //############################## UI STUFF ##############################
    //

    public static final int MAX_QUICKSLOTS = 12;
    public static final int MIN_QUICKSLOTS = 3;

    public static boolean gameIsAndroid(){
        return Game.platform.getClass().getSimpleName().contains("Android");
    }
}
