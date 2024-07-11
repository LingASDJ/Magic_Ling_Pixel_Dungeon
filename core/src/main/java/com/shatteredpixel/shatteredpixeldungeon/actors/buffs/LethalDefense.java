package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class LethalDefense extends Buff{
    public float duration;
    {
        type = buffType.POSITIVE;
    }

    public void resetTime(){
        int point = Dungeon.hero.pointsInTalent(Talent.LETHAL_DEFENSE);
        switch (point){
            case 1:
                duration = 2f;
                break;
            case 2:
                duration = 3f;
                break;
            case 3:
                duration = 4f;
                break;
            default:
                duration = 0;
                break;
        }
    }


    @Override
    public boolean act() {
        duration-=TICK;
        spend(TICK);
        if (duration <= 0) {
            detach();
        }
        return true;
    }

    @Override
    public int icon() {
        return BuffIndicator.COMBO;
    }

    @Override
    public float iconFadePercent() {
        float time = 0;
        int point=Dungeon.hero.pointsInTalent(Talent.KEEP_VIGILANCE);
        switch (point){
            case 1:
                time=2f;
                break;
            case 2:
                time=3f;
                break;
            case 3:
                time=4f;
                break;
        }
        return Math.max(0, (time- duration)/time);
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString((int)duration);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc" , duration);
    }

}
