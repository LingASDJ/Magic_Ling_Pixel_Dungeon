package com.shatteredpixel.shatteredpixeldungeon.services.daily;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;

import java.util.List;

public class LeaderboardData {
    public int code;
    public Data data;

    public static class Data {
        public String date;
        /** Weekly boards only: Monday/Sunday of the requested week. */
        public String weekStart = "";
        public String weekEnd = "";
        public int totalPlayers;
        public int page;
        public int pageSize;
        public int myRank;
        public int myScore;
        public List<Entry> entries;
    }

    public static class Entry {
        public int rank;
        public String date = "";
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
        public String version = "";
        /** Full daily-run snapshot JSON (rankings game-data format). */
        public String gameData = "";
        /** Replay board entry marker. */
        public boolean replay;
        /** Weekly boards only: number of days played and podium finishes. */
        public int runs;
        public int gold;
        public int silver;
        public int bronze;
    }

    public boolean isSuccess() {
        return code == 0;
    }
}
