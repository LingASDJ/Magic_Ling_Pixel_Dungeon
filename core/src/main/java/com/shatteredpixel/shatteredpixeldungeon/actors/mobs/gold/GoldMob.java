package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public abstract class GoldMob extends Mob {
    @Override
    public String info(){
        String desc = super.info();

        desc += "\n\n" + Messages.get(GoldMob.class,"infos");

        return desc;
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );
        if(Statistics.RandomQuest == 2){
            if(Statistics.GoldMobDead>=15 && Statistics.GoldMobLimit < 4){
                Statistics.goldRefogreCount++;
                Statistics.GoldMobDead = 0;
                Statistics.GoldMobLimit++;
            } else {
                Statistics.GoldMobDead++;
            }
        }

    }
}
