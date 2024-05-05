package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SmallLightSprites;
import com.watabou.utils.Random;

public class SmallLight extends Pets {
    private static final String[] TXT_RANDOM = {"Edro, edro!", "Yéni únótimë ve rámar aldaron!","A laita te, laita te! Andavë laituvalmet!","Ú-chebin Estel anim."};

    {
        spriteClass = SmallLightSprites.class;
        WANDERING = new Wandering();
        defenseSkill = 15;
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
                target = level.exit();

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
