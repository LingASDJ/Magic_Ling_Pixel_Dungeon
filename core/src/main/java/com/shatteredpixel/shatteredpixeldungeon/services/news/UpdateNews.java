package com.shatteredpixel.shatteredpixeldungeon.services.news;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.watabou.noosa.Image;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UpdateNews {

    public static GameUpdateNewsService service;

    public static boolean supportsNews() {
        return service != null;
    }

    private static Date lastCheck = null;
    private static final long CHECK_DELAY = 1000 * 60 * 60; //1 hour

    public static void checkForNews() {
        if (!supportsNews()) return;
        if (lastCheck != null && (new Date().getTime() - lastCheck.getTime()) < CHECK_DELAY) return;

        boolean useHTTPS = true;
        if (Gdx.app.getType() == Application.ApplicationType.Android && Gdx.app.getVersion() < 20) {
            useHTTPS = false; //android versions below 5.0 don't support TLSv1.2 by default
        }
        service.checkForArticles(!SPDSettings.WiFi(), useHTTPS, new GameUpdateNewsService.NewsResultCallback() {
            @Override
            public void onArticlesFound(ArrayList<GameUpdateNewsArticles> articles) {
                lastCheck = new Date();
                UpdateNews.articles = articles;
            }

            @Override
            public void onConnectionFailed() {
                lastCheck = null;
                UpdateNews.articles = null;
            }
        });

    }

    private static ArrayList<GameUpdateNewsArticles> articles;

    public static synchronized boolean articlesAvailable() {
        return articles != null && !articles.isEmpty();
    }

    public static synchronized ArrayList<GameUpdateNewsArticles> articles() {
        return new ArrayList<>(articles);
    }

    public static synchronized int unreadArticles(Date lastRead) {
        int unread = 0;
        if (articles != null) {
            for (GameUpdateNewsArticles article : articles) {
                if (article.date.after(lastRead)) unread++;
            }
        }
        return unread;
    }

    public static synchronized void clearArticles() {
        articles = null;
        lastCheck = null;
    }

    public static Image parseArticleIcon(GameUpdateNewsArticles article) {

        try {

            //recognized formats are:
            //"ICON: <name of enum constant in Icons.java>"
            if (article.icon.startsWith("ICON: ")) {
                return Icons.get(Icons.valueOf(article.icon.replace("ICON: ", "")));
                //"ITEM: <integer constant corresponding to values in ItemSpriteSheet.java>"
            } else if (article.icon.startsWith("ITEM: ")) {
                return new ItemSprite(Integer.parseInt(article.icon.replace("ITEM: ", "")));
                //"<asset filename>, <tx left>, <tx top>, <width>, <height>"
            } else {
                String[] split = article.icon.split(", ");
                return new Image(split[0],
                        Integer.parseInt(split[1]),
                        Integer.parseInt(split[2]),
                        Integer.parseInt(split[3]),
                        Integer.parseInt(split[4]));
            }

            //if we run into any formatting errors (or icon is null), default to the news icon
        } catch (Exception e) {
            if (article.icon != null) ShatteredPixelDungeon.reportException(e);
            return Icons.get(Icons.NEWS);
        }
    }

    public static String parseArticleDate(GameUpdateNewsArticles article) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(article.date);
        return cal.get(Calendar.YEAR)
                + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1)
                + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
    }
}
