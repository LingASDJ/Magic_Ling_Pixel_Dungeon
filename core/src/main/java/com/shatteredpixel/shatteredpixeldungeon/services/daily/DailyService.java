package com.shatteredpixel.shatteredpixeldungeon.services.daily;


public abstract class DailyService {
    public abstract void fetchTodaySeed(DailyResultCallback<DailySeedData> callback);
    public abstract void submitScore(com.watabou.utils.Bundle bundle, DailyResultCallback<SubmitResultData> callback);

    /**
     * Backwards-compatible overload: first page, 20 entries.
     */
    public void fetchLeaderboard(String date, DailyResultCallback<LeaderboardData> callback) {
        fetchLeaderboard(date, 1, 20, callback);
    }

    public abstract void fetchLeaderboard(String date, int page, int pageSize,
                                          DailyResultCallback<LeaderboardData> callback);

    /**
     * Weekly board: server-side aggregation of the week containing `weekStart`
     * (Monday..Sunday UTC). Each entry is the player's best run of the week.
     */
    public void fetchWeeklyLeaderboard(String weekStart, DailyResultCallback<LeaderboardData> callback) {
        fetchWeeklyLeaderboard(weekStart, 1, 20, callback);
    }

    public abstract void fetchWeeklyLeaderboard(String weekStart, int page, int pageSize,
                                                DailyResultCallback<LeaderboardData> callback);

    /** 重玩榜：每名玩家每天只保留最高分的一条重玩记录。 */
    public void fetchReplayLeaderboard(String date, DailyResultCallback<LeaderboardData> callback) {
        fetchReplayLeaderboard(date, 1, 20, callback);
    }

    public abstract void fetchReplayLeaderboard(String date, int page, int pageSize,
                                                DailyResultCallback<LeaderboardData> callback);

    public static abstract class DailyResultCallback<T> {
        public abstract void onSuccess(T result);
        public abstract void onFailure(String error);
    }
}
