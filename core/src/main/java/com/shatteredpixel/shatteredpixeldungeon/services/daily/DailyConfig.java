package com.shatteredpixel.shatteredpixeldungeon.services.daily;

import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import java.io.IOException;

/**
 * Applies the server-provided daily difficulty/challenge configuration through
 * SPDSettings (which Dungeon.init reads), and restores the player's previous
 * settings when the daily run ends or the player returns to the title screen.
 *
 * The previous settings are also written to disk so that an app restart while
 * a daily is active does not lose them.
 */
public class DailyConfig {

    private static final String PREVIOUS_FILE = "mp_daily_prev.dat";

    private static String currentDate = "";
    private static String currentDifficulty = "NORMAL";
    private static int currentChallenges = 0;
    private static int currentChallengeCount = 0;

    public static synchronized void apply(DailySeedData data) {
        if (data == null) return;

        if (!FileUtils.fileExists(PREVIOUS_FILE)) {
            Bundle previous = new Bundle();
            previous.put("dlc", SPDSettings.dlc());
            previous.put("challenges", SPDSettings.challenges());
            previous.put("customSeed", SPDSettings.customSeed());
            try {
                FileUtils.bundleToFile(PREVIOUS_FILE, previous);
            } catch (IOException ignored) {
                // If this fails we can still run the daily; restoring settings
                // after the run will just be a no-op.
            }
        }

        currentDate = data.date == null ? "" : data.date;
        currentDifficulty = "HARD".equalsIgnoreCase(data.difficulty) ? "HARD" : "NORMAL";
        currentChallenges = data.challenges;
        currentChallengeCount = data.challengeCount;

        Conducts.Conduct conduct = "HARD".equals(currentDifficulty)
                ? Conducts.Conduct.HARD
                : Conducts.Conduct.NORMAL;
        SPDSettings.dlc(new Conducts.ConductStorage(conduct));
        SPDSettings.challenges(currentChallenges);
        // Dungeon.init() checks customSeed before the daily branch; clear it so
        // the server-issued daily seed is actually used.
        SPDSettings.customSeed("");
    }

    public static synchronized void restoreIfNeeded() {
        if (FileUtils.fileExists(PREVIOUS_FILE)) {
            restore();
        }
    }

    public static synchronized void restore() {
        currentDate = "";
        currentDifficulty = "NORMAL";
        currentChallenges = 0;
        currentChallengeCount = 0;

        if (!FileUtils.fileExists(PREVIOUS_FILE)) return;

        try {
            Bundle previous = FileUtils.bundleFromFile(PREVIOUS_FILE);
            Conducts.ConductStorage dlc = (Conducts.ConductStorage) previous.get("dlc");
            if (dlc != null) {
                SPDSettings.dlc(dlc);
            }
            SPDSettings.challenges(previous.getInt("challenges"));
            if (previous.contains("customSeed")) {
                SPDSettings.customSeed(previous.getString("customSeed"));
            }
        } catch (Throwable ignored) {
            // Keep the file so a later attempt can try again.
            return;
        }

        try {
            FileUtils.deleteFile(PREVIOUS_FILE);
        } catch (Throwable ignored) {
        }
    }

    public static String getCurrentDate() {
        return currentDate;
    }

    public static String getCurrentDifficulty() {
        return currentDifficulty;
    }

    public static int getCurrentChallenges() {
        return currentChallenges;
    }

    public static int getCurrentChallengeCount() {
        return currentChallengeCount;
    }
}
