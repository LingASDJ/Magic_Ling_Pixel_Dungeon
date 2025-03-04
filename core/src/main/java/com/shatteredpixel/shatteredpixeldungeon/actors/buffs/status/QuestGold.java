package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RedDragon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class QuestGold extends Buff {

    {
        type = buffType.POSITIVE;
    }

    private int level = 0;
    private int interval = 1;

    @Override
    public boolean act() {
        if (target.isAlive()) {

            spend( interval );
            if (level <= 0) {
                detach();
            }

        } else {

            detach();

        }

        return true;
    }

    @Override
    public String desc() {
        String result;
        result = super.desc();

        result  += "\n" + Messages.get(this, "gold",Statistics.goldRefogreCount);

        if (!Ghost.Quest.completed()){
            result  += "\n" + Messages.get(this, "ghost");
        }  else {
            result  += "\n" + Messages.get(this, "ghost_yes");
        }

        if (!RedDragon.Quest.completed()){
            result  += "\n" + Messages.get(this, "dragon");
        }  else {
            result  += "\n" + Messages.get(this, "dragon_yes");
        }

        result  += "\n" + Messages.get(this, "wand");

        if (!Blacksmith.Quest.completed()){
            result  += "\n" + Messages.get(this, "bm");
        }  else {
            result  += "\n" + Messages.get(this, "bm_yes");
        }

        result  += "\n" + Messages.get(this, "im");

        if ( Statistics.dimandchestmazeCollected>=3){
            result  += "\n" + Messages.get(this, "dk_yes");
        }  else {
            result  += "\n" + Messages.get(this, "dk");
        }




        result  += "\n" + Messages.get(this, "boss");

        switch (Statistics.RandomQuest){
            case 1:
                result  += "\n" + Messages.get(this, "random_1");
                break;
            case 2:
                result  += "\n" + Messages.get(this, "random_2");
                break;
            default:
                result  += "\n" + Messages.get(this, "random_3");
                break;
        }

        result  += "\n" + Messages.get(this, "ws",Statistics.upgradeGold);

        return result;
    }

    public int level() {
        return level;
    }

    public void set( int value, int time ) {
        if (level <= value) {
            level = value;
            interval = time;
            spend(time - cooldown() - 1);
        }
    }

    @Override
    public int icon() {
        return BuffIndicator.QUEST;
    }

    @Override
    public float iconFadePercent() {
        if (target instanceof Hero){
            float max = ((Hero) target).lvl*((Hero) target).pointsInTalent(Talent.BARKSKIN)/2;
            max = Math.max(max, 2+((Hero) target).lvl/3);
            return Math.max(0, (max-level)/max);
        }
        return 0;
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString(level);
    }

    private static final String LEVEL	    = "level";
    private static final String INTERVAL    = "interval";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( INTERVAL, interval );
        bundle.put( LEVEL, level );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        interval = bundle.getInt( INTERVAL );
        level = bundle.getInt( LEVEL );
    }

}

