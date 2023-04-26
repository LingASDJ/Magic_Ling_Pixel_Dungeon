package com.shatteredpixel.shatteredpixeldungeon.services.news;

public class GameNesImpl{

    private static GameUpdateNewsService newsChecker = new GameUpdateNews();

    public static GameUpdateNewsService getNewsService(){
        return newsChecker;
    }

    public static boolean supportsNews(){
        return true;
    }

}
