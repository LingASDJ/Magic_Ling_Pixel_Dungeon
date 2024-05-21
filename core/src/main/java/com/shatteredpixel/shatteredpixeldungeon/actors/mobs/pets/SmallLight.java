package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SmallLightSprites;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class SmallLight extends Pets {
    private static final String[] TXT_RANDOM = {"Edro, edro!", "Yéni únótimë ve rámar aldaron!","A laita te, laita te! Andavë laituvalmet!","Ú-chebin Estel anim."};

    {
        spriteClass = SmallLightSprites.class;
        WANDERING = new Wandering();
        defenseSkill = 15;
        viewDistance = 5;
    }

    public void teleportEnemy(int Epos){
        spend(TICK);

        int bestPos = Epos;
        if(bestPos == 0){
            return;
        }
        Char c = Actor.findChar(Epos);
        if(c==null)
            return;

        for (int i : PathFinder.NEIGHBOURS8){
            if (Dungeon.level.passable[pos + i]
                    && Actor.findChar(pos+i) == null
                    && Dungeon.level.trueDistance(pos+i, Epos) > Dungeon.level.trueDistance(bestPos, Epos)){
                bestPos = pos+i;
            }
        }

        if (c.buff(MagicImmune.class) != null){
            bestPos = Epos;
        }

        if (bestPos != Epos){
            ScrollOfTeleportation.appear(c, bestPos);
        }

        //enemyTeleCooldown = 20;
    }

    public boolean canTele(int target){
        //if (enemyTeleCooldown > 0) return false;
        PathFinder.buildDistanceMap(target, BArray.not(Dungeon.level.solid, null), Dungeon.level.distance(pos, target)+1);
        //zaps can go around blocking terrain, but not through it
        if (PathFinder.distance[pos] == Integer.MAX_VALUE){
            return false;
        }
        return true;
    }

    private boolean cellIsPathable( int cell ){
        if (!Dungeon.level.passable[cell]){
            if (flying || buff(Amok.class) != null){
                if (!Dungeon.level.avoid[cell]){
                    return false;
                }
            } else {
                return false;
            }
        }
        if (Char.hasProp(this, Char.Property.LARGE) && !Dungeon.level.openSpace[cell]){
            return false;
        }
        if (Actor.findChar(cell) != null){
            return false;
        }

        return true;
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
            }
            else {
                enemySeen = false;
                target = level.exit();
            }



                int oldPos = pos;
                //always move towards the hero when wandering
                if (getCloser( target )) {
                    spend( 1 / speed() );
                    return moveSprite( oldPos, pos );
                } else {
                    spend( TICK );
                }

            if(Random.Int(10)==1){
                sprite.showStatus(0xFCE9CC, TXT_RANDOM[Random.Int(TXT_RANDOM.length)]);
            }

            return true;
        }

    }

}
