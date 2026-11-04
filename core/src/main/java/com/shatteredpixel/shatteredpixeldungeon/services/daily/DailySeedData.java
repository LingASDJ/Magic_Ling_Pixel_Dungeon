package com.shatteredpixel.shatteredpixeldungeon.services.daily;

public class DailySeedData {
    public int code;
    public String date;
    public long seed;
    /** "NORMAL" (正式调查) or "HARD" (直面灾厄). */
    public String difficulty = "NORMAL";
    /** Challenge bit mask, same values as Challenges.MASKS. */
    public int challenges = 0;
    /** Number of selected challenges (1..16). */
    public int challengeCount = 0;
}
