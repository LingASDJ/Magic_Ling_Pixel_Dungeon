package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.CS;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.gameDay;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.gameNight;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.gameTime;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class NightorDay extends Buff {
    {
        type = buffType.POSITIVE;
    }

    public static int level = 0;
    private int interval = 1;
    public static final int DISTANCE = 1;

    @Override
    public boolean act() {
        if (target.isAlive()) {

            spend(interval);
            if (level <= 0) {
                detach();
            }

            if(Dungeon.depth<1 || Dungeon.level.locked )return true;

            //昼夜更替

            if(Dungeon.isChallenged(CS) && gameNight){
                if(gameTime>0){
                    gameNight = false;
                } else {
                    gameTime++;
                }
            } else if(gameTime>400 && gameTime<600) {
                gameTime++;
                gameNight = true;
            } else if(gameTime>599){
                gameTime = 0;
                gameNight = false;
                gameDay++;
            } else {
                gameTime++;
            }

        }

        return true;
    }

    public int level() {
        return level;
    }

    public void set( int value, int time ) {
        //decide whether to override, preferring high value + low interval
        if (Math.sqrt(interval)*level <= Math.sqrt(time)*value) {
            level = value;
            interval = time;
            spend(time - cooldown() - 1);
        }
    }

    @Override
    public float iconFadePercent() {
        if (target instanceof Hero){
            float max = ((Hero) target).lvl;
            return Math.max(0, (max-level)/max);
        }
        return 0;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
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

    @Override
    public int icon() {
        if(gameTime>400){
            return BuffIndicator.NIGHT_DAY;
        } else if(gameTime>350){
            return BuffIndicator.EVEN_DAY;
        } else if(gameTime>200) {
            return BuffIndicator.MID_DAY;
        } else {
            return BuffIndicator.WHITE_DAY;
        }
    }

    @Override
    public String name() {
        if(gameTime>400){
            return Messages.get(this, "name4");
        } else if(gameTime>350){
            return Messages.get(this, "name");
        } else if(gameTime>200) {
            return Messages.get(this, "name2");
        } else  {
            return Messages.get(this, "name3");
        }
    }

    @Override
    public String desc() {
        String result;
        if(gameTime>400){
            result = Messages.get(this, "desc4");
        } else if(gameTime>350){
            result = Messages.get(this, "desc");
        } else if(gameTime>200) {
            result = Messages.get(this, "desc2");
        } else {
            result = Messages.get(this, "desc3");
        }
        return result;
    }

}
