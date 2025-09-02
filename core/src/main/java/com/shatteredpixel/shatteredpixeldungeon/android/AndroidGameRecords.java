package com.shatteredpixel.shatteredpixeldungeon.android;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;

import java.util.Date;

public class AndroidGameRecords {

    public static FirebaseAnalytics mFirebaseAnalyticsRecords = AndroidLauncher.mFirebaseAnalyticsRecords;
    public static void GameRecordChallenges() {
        Bundle params = new Bundle();
        params.putString("挑战数量", String.valueOf(Challenges.activeChallenges()));
        params.putString("作战时间记录", String.valueOf(new Date()));
        params.putString("总作战回合", String.valueOf(Statistics.duration));
        params.putString("所属职业", hero.className());
        params.putString("自定义名字", hero.name());
        params.putString("当前楼层", String.valueOf(Dungeon.depth));
        params.putString("最高楼层", String.valueOf(Statistics.deepestFloor));
        params.putString("所选难度", Difficult());
        params.putString("游戏模式", GameMode());
        params.putString("胜利情况", String.valueOf(Statistics.winGame));
        params.putString("是否返程", String.valueOf(Statistics.ascended));
        params.putString("地图种子", DungeonSeed.convertToCode(Dungeon.seed));
        params.putString("游戏版本", String.valueOf(Dungeon.version));
        mFirebaseAnalyticsRecords.logEvent("作战报告", params);
    }

    public static String Difficult(){
        String string;
        if(Dungeon.isDLC(Conducts.Conduct.EASY)){
            string = "简单";
        } else if(Dungeon.isDLC(Conducts.Conduct.NORMAL)){
            string = "普通";
        } else if(Dungeon.isDLC(Conducts.Conduct.HARD)){
            string = "困难";
        } else {
            string = "开发者";
        }
        return string;
    }

    public static String GameMode(){
        String string;
        if(Statistics.bossRushMode){
            string = "BR模式";
        } else if(Statistics.RandMode){
            string = "金蝶模式";
        }  else {
            string = "传统模式";
        }
        return string;
    }
}
