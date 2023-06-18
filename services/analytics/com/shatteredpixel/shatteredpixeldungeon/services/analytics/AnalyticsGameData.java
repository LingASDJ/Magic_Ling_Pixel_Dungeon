package com.shatteredpixel.shatteredpixeldungeon.services.analytics;

import java.util.LinkedHashMap;

public class AnalyticsGameData {
    public int ascent;
    public String bestBossBeaten;
    public int challengeMask;
    public boolean cheater = false;
    public LinkedHashMap<Integer, LinkedHashMap<String, Integer>> crafts;
    public int deepest;
    public int depth;
    public float duration;
    public String gameVersion;
    public String heroArmorAbility;
    public String heroCls;
    public int heroLvl;
    public String heroSubCls;
    public boolean ratMogrifyFound;
    public int spawnersAlive;
    public LinkedHashMap<String, Integer> talents;
    public LinkedHashMap<String, Integer> upgrades;
}