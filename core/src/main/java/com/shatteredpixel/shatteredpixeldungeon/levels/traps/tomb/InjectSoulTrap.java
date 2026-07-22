package com.shatteredpixel.shatteredpixeldungeon.levels.traps.tomb;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff.DeathBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.audio.Sample;

public class InjectSoulTrap extends Trap {

    {
        color = TOMB;
        shape = DOTS;

        avoidsHallways = true;
    }

    @Override
    public void activate() {
        Char target = Actor.findChar(pos);

        //find the closest char that can be aimed at
        //can't target beyond view distance, with a min of 6 (torch range)
        int range = Math.max(6, Dungeon.level.viewDistance);
        if (target == null){
            float closestDist = Float.MAX_VALUE;
            for (Char ch : Actor.chars()){
                if (!ch.isAlive()) continue;
                float curDist = Dungeon.level.trueDistance(pos, ch.pos);
                //invis targets are considered to be at max range
                if (ch.invisible > 0) curDist = Math.max(curDist, range);
                Ballistica bolt = new Ballistica(pos, ch.pos, Ballistica.PROJECTILE);
                if (bolt.collisionPos == ch.pos
                        && ( curDist < closestDist || (curDist == closestDist && target instanceof Hero))){
                    target = ch;
                    closestDist = curDist;
                }
            }
            if (closestDist > range){
                target = null;
            }
        }

        Heap heap = Dungeon.level.heaps.get(pos);
        if (heap != null) heap.explode();

        if (target != null) {
            if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[target.pos]) {
                Sample.INSTANCE.play(Assets.Sounds.RAY);
                ShatteredPixelDungeon.scene().add(new Beam.DeathRayS(DungeonTilemap.tileCenterToWorld(pos), target.sprite.center()));
            }

            if(target.properties.contains(Char.Property.UNDEAD) && target != hero){
                target.HP = target.HT;
                target.sprite.emitter().burst( Speck.factory( Speck.HEALING ), target.HT );
            } else {
                target.die(true);
            }

            if (target == hero){
                DeathBuff deathBuff = hero.buff(DeathBuff.class);
                if(deathBuff != null){
                    deathBuff.getDeath(20);
                } else {
                    Buff.affect(hero, DeathBuff.class).set((3), 5);
                }

            }
        }

    }
}
