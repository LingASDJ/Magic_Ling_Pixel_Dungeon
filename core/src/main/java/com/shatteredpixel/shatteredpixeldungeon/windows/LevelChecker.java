package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;

public class LevelChecker {

    private static final int SSS_SCORE = 10000;
    private static final int SS_SCORE = 8000;
    private static final int S_SCORE = 5000;
    private static final int A_SCORE = 4500;
    private static final int B_SCORE = 6000;
    private static final int C_SCORE = 5000;
    private static final int D_SCORE = 4000;
    private static final int E_SCORE = 3000;

    public String checkLevel() {
        int totalScore = Statistics.totalScore;
        float chalMultiplier = Statistics.chalMultiplier;
        boolean amuletObtained = Statistics.amuletObtained;

        String level;

        if (totalScore >= SSS_SCORE * chalMultiplier * (amuletObtained ? 1 : 5)) {
            level = "SSS";
        } else if (totalScore >= SS_SCORE * chalMultiplier * (amuletObtained ? 1 : 4)) {
            level = "SS";
        } else if (totalScore >= S_SCORE * chalMultiplier * (amuletObtained ? 1 : 3)) {
            level = "S";
        } else if (totalScore >= A_SCORE * chalMultiplier * (amuletObtained ? 1 : 2)) {
            level = "A";
        } else if (totalScore >= B_SCORE * chalMultiplier * (amuletObtained ? 1 : 2)) {
            level = "B";
        } else if (totalScore >= C_SCORE * chalMultiplier * (amuletObtained ? 1 : 2)) {
            level = "C";
        } else if (totalScore >= D_SCORE * chalMultiplier * (amuletObtained ? 1 : 2)) {
            level = "D";
        } else if (totalScore >= E_SCORE * chalMultiplier) {
            level = "E";
        } else {
            level = "F";
        }

        return level;
    }
}