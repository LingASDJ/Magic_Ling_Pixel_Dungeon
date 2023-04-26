package com.shatteredpixel.shatteredpixeldungeon.services.news;

import java.util.ArrayList;

public abstract class GameUpdateNewsService {
    public static abstract class NewsResultCallback {
        public abstract void onArticlesFound(ArrayList<GameUpdateNewsArticles> articles);
        public abstract void onConnectionFailed();
    }

    public abstract void checkForArticles(boolean useMetered, boolean forceHTTPS, GameUpdateNewsService.NewsResultCallback callback);
}
