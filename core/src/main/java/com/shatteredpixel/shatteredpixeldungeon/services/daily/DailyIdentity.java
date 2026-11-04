package com.shatteredpixel.shatteredpixeldungeon.services.daily;

import com.badlogic.gdx.files.FileHandle;
import com.watabou.utils.FileUtils;

import java.util.UUID;

/**
 * Persistent, non-account daily-run identity used to enforce one score per
 * player per UTC day on the server. The value is not shown in leaderboard rows;
 * it is only displayed in the player-details window.
 */
public class DailyIdentity {

    private static final String FILE_NAME = "mp_daily_uuid.txt";
    private static String cached;

    public static synchronized String getPlayerId() {
        if (cached != null && !cached.trim().isEmpty()) {
            return cached;
        }

        try {
            FileHandle file = FileUtils.getFileHandle(FILE_NAME);
            if (file.exists() && !file.isDirectory()) {
                String saved = file.readString("UTF-8");
                if (saved != null && !saved.trim().isEmpty()) {
                    cached = saved.trim();
                    return cached;
                }
            }
            cached = UUID.randomUUID().toString();
            file.writeString(cached, false, "UTF-8");
        } catch (Throwable ignored) {
            if (cached == null || cached.trim().isEmpty()) {
                cached = UUID.randomUUID().toString();
            }
        }
        return cached;
    }
}
