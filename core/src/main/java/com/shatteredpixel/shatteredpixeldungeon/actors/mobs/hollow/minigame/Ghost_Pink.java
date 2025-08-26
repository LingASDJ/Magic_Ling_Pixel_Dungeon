package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.minigame;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSight;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MiniGhostSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Ghost_Pink extends Mob {

    {
        spriteClass = MiniGhostSprite.PinkSadlyGhost.class;
        HT = HP = 10;
    }

    public int AI_state;
    private static final String RecordOtherGhost_STATE   = "RECORDOTHERGHOST_STATE";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(RecordOtherGhost_STATE, AI_state);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        AI_state = bundle.getInt(RecordOtherGhost_STATE);
    }

    public static class RecordOtherGhost extends FlavourBuff { }

    @Override
    protected boolean act() {
        AiState lastState = state;
        if(lastState == SLEEPING){
            ScrollOfTeleportation.appear(this, Dungeon.level.randomDestination(this));
            Buff.affect(hero, MagicalSight.class, MagicalSight.DURATION*200);
            state = HUNTING;
        }

        if(buff(RecordOtherGhost.class) == null){
            Buff.prolong(this, RecordOtherGhost.class,40f);
            AI_state = Random.Int(3);
        }

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            switch (AI_state){
                case 1:
                    if(mob instanceof Ghost_Junko){
                        beckon(mob.pos);
                    }
                    break;
                case 2:
                    if(mob instanceof Ghost_Pink){
                        beckon(mob.pos);
                    }
                    break;
                default:
                    if(mob instanceof Ghost_Anger){
                        beckon(mob.pos);
                    }
                    break;
            }
        }
        return super.act();
    }

    @Override
    public int attackSkill( Char target ) {
        return Integer.MAX_VALUE;
    }

    @Override
    public int drRoll() {
        return 0;
    }

}

