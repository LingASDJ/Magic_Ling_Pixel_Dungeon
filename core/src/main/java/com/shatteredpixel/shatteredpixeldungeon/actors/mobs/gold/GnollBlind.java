package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Gnoll;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Random;

public class GnollBlind extends Gnoll {

    @Override
    public String info(){
        String desc = super.description();

        desc += "\n\n" + Messages.get(GoldMob.class,"infos");

        return desc;
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );
        if(Statistics.RandomQuest == 2){
            if(Statistics.GoldMobDead>=15){
                Statistics.goldRefogreCount++;
                Statistics.GoldMobDead = 0;
            }

        }
    }

    {
        spriteClass = GnollBlindSprite.class;
        viewDistance = 2;//blind
        HP = HT = 14;
        defenseSkill = 5;

        EXP = 1;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1, 5 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 12;
    }

    @Override
    public int drRoll() {
        if(Dungeon.depth == 1){
            return 0;
        }
        return super.drRoll() + Random.NormalIntRange(0, 2);
    }

}
