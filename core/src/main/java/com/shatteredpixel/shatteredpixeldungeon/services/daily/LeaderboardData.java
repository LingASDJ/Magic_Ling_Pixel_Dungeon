package com.shatteredpixel.shatteredpixeldungeon.services.daily;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;

import java.util.List;

public class LeaderboardData {
    public int code;
    public Data data;

    public static class Data {
        public String date;
        public int totalPlayers;
        public int page;
        public int pageSize;
        public int myRank;
        public int myScore;
        public List<Entry> entries;
    }

    public static class Entry {
        public int rank;
        public String playerId = "";
        public String playerName;
        public HeroClass heroClass;
        public int heroLevel;
        public int armorTier;
        public int score;
        public boolean won;
        public int depth;
        public int challenges;
        public int challengeCount;
        public int difficulty;
    }

    public boolean isSuccess() {
        return code == 0;
    }
}
