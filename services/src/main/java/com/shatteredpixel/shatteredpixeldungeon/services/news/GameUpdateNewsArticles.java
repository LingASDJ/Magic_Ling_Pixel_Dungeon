package com.shatteredpixel.shatteredpixeldungeon.services.news;

import java.util.Date;

public class GameUpdateNewsArticles {

    public String title;
    public Date date;
    public String summary;

    public int ling;
    public String URL;

    public String DesktopURL;

    //the icon is stored as a string here so it can be decoded to an image later
    //See News.java for supported formats
    public String icon;
}
