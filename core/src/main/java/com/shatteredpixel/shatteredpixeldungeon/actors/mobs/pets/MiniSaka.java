package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.SakaFishSketon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MiniSakaFishBossSprites;
import com.watabou.utils.Random;

public class MiniSaka extends Pets {

    private static final String[] TXT_RANDOM = {"saka……saka……", "saka!!!","saka?!"};

    {
        spriteClass = MiniSakaFishBossSprites.class;
        WANDERING = new Wandering();
        defenseSkill = 15;
    }

    @Override
    public void die( Object cause ) {
        super.die(cause);
        this.sprite.showStatus(0xFCE9CC, "saka……T-T");
        Dungeon.level.drop(Generator.random(Generator.Category.STONE),pos).sprite.drop();
        Buff.affect(hero, SakaFishSketon.CoolDownStoneRecharge.class, SakaFishSketon.CoolDownStoneRecharge.DURATION);
    }

    private class Wandering extends Mob.Wandering {

        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            if ( enemyInFOV ) {

                enemySeen = true;

                notice();
                alerted = true;
                state = HUNTING;
                target = enemy.pos;

            } else {

                enemySeen = false;

                int oldPos = pos;
                target = hero.pos;

                //always move towards the hero when wandering
                if (getCloser( target )) {
                    spend( 1 / speed() );
                    return moveSprite( oldPos, pos );
                } else {
                    spend( TICK );
                }
            }

            if(Random.Int(10)==1){
                sprite.showStatus(0xFCE9CC, TXT_RANDOM[Random.Int(TXT_RANDOM.length)]);
            }

            return true;
        }

    }

}
