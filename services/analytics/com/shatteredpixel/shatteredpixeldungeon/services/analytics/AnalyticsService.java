package com.shatteredpixel.shatteredpixeldungeon.services.analytics;

public abstract class AnalyticsService {
    public abstract void disable();

    public abstract void enable();

    public abstract void trackBossBeaten(AnalyticsGameData analyticsGameData, AnalyticsBelongingsData analyticsBelongingsData, String str);

    public abstract void trackException(Throwable th);

    public abstract void trackGameSettings(boolean z, int i, int i2, String str, boolean z2, boolean z3, boolean z4, boolean z5, String str2);

    public abstract void trackRunEnd(AnalyticsGameData analyticsGameData, String str);

    public abstract void trackScreen(String str);
}
