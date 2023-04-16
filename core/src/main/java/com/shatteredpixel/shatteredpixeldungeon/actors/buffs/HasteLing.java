package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Difficulty;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class HasteLing extends Haste{

    //难度系统 敌人移速
    public static class MobLing extends Haste{


        @Override
        public int icon() {
            return BuffIndicator.NONE;
        }

        public float speedFactor(){
            if (Dungeon.isDIFFICULTY(Difficulty.DifficultyConduct.EASY)) {
                return 0.7f;
            } else if (Dungeon.isDIFFICULTY(Difficulty.DifficultyConduct.NORMAL)) {
                return 1f;
            } else if (Dungeon.isDIFFICULTY(Difficulty.DifficultyConduct.HARD)) {
                return 1.15f;
            } else {
                return 1f;
            }
        }
    }
}
