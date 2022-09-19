package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.custom.ch.GameTracker;

public class LevelSwitchListener {
    public static void onLevelSwitch(){
        GameTracker gmt = Dungeon.hero.buff(GameTracker.class);
        if(gmt != null){
            gmt.onNewLevel();
        }
    }
}
