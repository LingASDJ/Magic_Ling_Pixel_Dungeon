package com.shatteredpixel.shatteredpixeldungeon.android;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;

import java.util.Date;

public class AndroidGameRecords {

    public static FirebaseAnalytics mFirebaseAnalyticsRecords = AndroidLauncher.mFirebaseAnalyticsRecords;
    public static void GameRecordChallenges() {
        Bundle params = new Bundle();
        params.putString("挑战数量", String.valueOf(Challenges.activeChallenges()));
        params.putString("作战时间记录", String.valueOf(new Date()));
        params.putString("所属职业", hero.className());
        params.putString("自定义名字", hero.name());
        params.putString("当前楼层", String.valueOf(Dungeon.depth));
        mFirebaseAnalyticsRecords.logEvent("作战报告", params);
    }
}
