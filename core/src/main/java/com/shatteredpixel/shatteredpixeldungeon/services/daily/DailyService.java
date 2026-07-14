package com.shatteredpixel.shatteredpixeldungeon.services.daily;


public abstract class DailyService {
    public abstract void fetchTodaySeed(DailyResultCallback<DailySeedData> callback);
    public abstract void submitScore(com.watabou.utils.Bundle bundle, DailyResultCallback<SubmitResultData> callback);
    public abstract void fetchLeaderboard(String date, DailyResultCallback<LeaderboardData> callback);

    public static abstract class DailyResultCallback<T> {
        public abstract void onSuccess(T result);
        public abstract void onFailure(String error);
    }
}