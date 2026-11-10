package com.shatteredpixel.shatteredpixeldungeon.services.daily;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Small helpers shared by the daily-run UI and the leaderboard.
 */
public class DailyUtils {

    /**
     * Maps the server's difficulty name to the game's localized conduct name.
     */
    public static String difficultyName(String difficulty) {
        try {
            Conducts.Conduct conduct = Conducts.Conduct.valueOf(
                    difficulty == null ? "NORMAL" : difficulty.toUpperCase(Locale.ROOT));
            return Messages.get(Conducts.class, conduct.name());
        } catch (Throwable ignored) {
            return Messages.get(Conducts.class, Conducts.Conduct.NORMAL.name());
        }
    }

    /**
     * Returns a comma-separated list of the selected challenge names.
     */
    public static String challengeNames(int mask) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < Challenges.MASKS.length && i < Challenges.NAME_IDS.length; i++) {
            if ((mask & Challenges.MASKS[i]) != 0) {
                if (result.length() > 0) result.append(", ");
                result.append(Messages.get(Challenges.class, Challenges.NAME_IDS[i]));
            }
        }
        return result.toString();
    }

    /**
     * Parses yyyy-MM-dd as a UTC day. Returns -1 for invalid dates.
     */
    public static long parseUtcDay(String date) {
        if (date == null || date.isEmpty()) return -1L;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            format.setLenient(false);
            Date parsed = format.parse(date);
            return parsed == null ? -1L : parsed.getTime();
        } catch (Throwable ignored) {
            return -1L;
        }
    }

    /**
     * Current UTC date in yyyy-MM-dd form (used only as a fallback; the server
     * remains authoritative for the actual daily date).
     */
    public static String todayUtc() {
        return formatUtcDay(System.currentTimeMillis());
    }

    /** Formats a UTC timestamp as yyyy-MM-dd. */
    public static String formatUtcDay(long millis) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(new Date(millis));
    }

    /**
     * Shifts a yyyy-MM-dd UTC date by a number of days. Invalid input falls back
     * to today's UTC date.
     */
    public static String shiftUtcDay(String date, int days) {
        long day = parseUtcDay(date);
        if (day < 0) {
            day = parseUtcDay(todayUtc());
        }
        if (day < 0) {
            return todayUtc();
        }
        return formatUtcDay(day + days * 86_400_000L);
    }

    /** Returns the Monday of the UTC week containing the given yyyy-MM-dd date. */
    public static String startOfUtcWeek(String date) {
        long day = parseUtcDay(date);
        if (day < 0) {
            day = parseUtcDay(todayUtc());
        }
        if (day < 0) {
            return todayUtc();
        }
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.ROOT);
        calendar.setTimeInMillis(day);
        int offset = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7; // Monday = 0
        return formatUtcDay(day - offset * 86_400_000L);
    }
}
