package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SmallLightSprites;
import com.watabou.utils.BArray;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class SmallLight extends Mob implements Callback {
    private static final String[] TXT_RANDOM = {"Edro, edro!", "Yéni únótimë ve rámar aldaron!","A laita te, laita te! Andavë laituvalmet!","Ú-chebin Estel anim."};

    {
        alignment = Alignment.ALLY;
        spriteClass = SmallLightSprites.class;
        WANDERING = new Wandering();

        defenseSkill = 15;
        invisible = 1;
        viewDistance = 5;
    }

    public void onZapComplete() {
        teleportEnemy(enemy.pos);
        next();
    }

    @Override
    public void call() {
        next();
    }

    public void teleportEnemy(int Epos){
        spend(TICK);

        int bestPos = Epos;
        if(bestPos == 0 || Epos == pos){
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
    }

    public boolean canTele(int target){
        PathFinder.buildDistanceMap(target, BArray.not(Dungeon.level.solid, null), Dungeon.level.distance(pos, target)+1);
        //zaps can go around blocking terrain, but not through it
        return PathFinder.distance[pos] != Integer.MAX_VALUE;
    }

    private class Wandering extends Mob.Wandering {

        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {

            if(pos == level.exit()){
                die(null);
            }

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

            flying = level.feeling == Level.Feeling.BIGTRAP || level.feeling == Level.Feeling.TRAPS;

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
