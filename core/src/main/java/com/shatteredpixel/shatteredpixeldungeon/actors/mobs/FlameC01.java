/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
 *
 * Overgrown Pixel Dungeon
 * Copyright (C) 2018-2019 Anon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package  com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.HalomethaneFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FlameC01Sprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

public class FlameC01 extends Mob {

    private static final float TIME_TO_BURN	= 3f;

    public int gasTankPressure;

    //the actual affected cells
    private HashSet<Integer> affectedCells;
    //the cells to trace fire shots to, for visual effects.
    private HashSet<Integer> visualCells;

    public void spawnAround( int pos ) {
        for (int n : PathFinder.NEIGHBOURS4) {
            int cell = pos + n;

            if (Dungeon.level.passable[pos+1] && Actor.findChar( cell ) == null) {
                spawnAt( cell );
            }
        }
    }

    private static final float SPAWN_DELAY	= 2f;
    public static FlameC01 spawnAt(int pos ) {
        if (!Dungeon.level.solid[pos] && Actor.findChar( pos ) == null) {

            FlameC01 w = new FlameC01();
            w.adjustStats();
            w.pos = pos;
            w.state = w.HUNTING;
            GameScene.add( w, SPAWN_DELAY );

            w.sprite.alpha( 0 );
            w.sprite.parent.add( new AlphaTweener( w.sprite, 1, 0.5f ) );

            w.sprite.emitter().burst( ShadowParticle.CURSE, 5 );

            return w;
        } else {
            return null;
        }
    }

    public void adjustStats() {
        defenseSkill = attackSkill( null ) * 5;
        enemySeen = true;
    }



    {
        spriteClass = FlameC01Sprite.class;
        properties.add(Property.INORGANIC);
        properties.add(Property.FIERY);
        HP = HT = 40;
        defenseSkill = 23;

        EXP = 10;
        maxLvl = 27;

        gasTankPressure = Random.Int(100, 250);
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;

    }

    @Override
    protected boolean doAttack( Char enemy ) {

        if (Dungeon.level.distance( pos, enemy.pos ) <= 1 || gasTankPressure <= 0) {

            return super.doAttack( enemy );

        } else {

            boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];
            if (visible) {
                sprite.attack( enemy.pos );
                spend( TIME_TO_BURN );
                shoot(this, enemy.pos);
                gasTankPressure -= Random.Int(1, 10);
            }

            return !visible;
        }
    }

    @Override
    protected Char chooseEnemy() {

        Terror terror = buff( Terror.class );
        if (terror != null) {
            Char source = (Char) Actor.findById( terror.object );
            if (source != null) {
                return source;
            }
        }

        StoneOfAggression.Aggression aggro = buff( StoneOfAggression.Aggression.class );
        if (aggro != null){
            Char source = aggro.target;
            if (source != null){
                return source;
            }
        }

        //find a new enemy if..
        boolean newEnemy = false;
        //we have no enemy, or the current one is dead
        if ( enemy == null || !enemy.isAlive() || state == WANDERING)
            newEnemy = true;
            //We are an ally, and current enemy is another ally.
        else if (alignment == Alignment.ALLY && enemy.alignment == Alignment.ALLY)
            newEnemy = true;
            //We are amoked and current enemy is the hero
        else if (buff( Amok.class ) != null && enemy == Dungeon.hero)
            newEnemy = true;
            //We are charmed and current enemy is what charmed us
        else if (buff(Charm.class) != null && buff(Charm.class).object == enemy.id())
            newEnemy = true;

        if ( newEnemy ) {

            HashSet<Char> enemies = new HashSet<>();

            //if the mob is amoked...
            if ( buff(Amok.class) != null) {
                //try to find an enemy mob to attack first.
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
                    if (mob.alignment == Alignment.ENEMY && mob != this && fieldOfView[mob.pos])
                        enemies.add(mob);

                if (enemies.isEmpty()) {
                    //try to find ally mobs to attack second.
                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
                        if (mob.alignment == Alignment.ALLY && mob != this && fieldOfView[mob.pos])
                            enemies.add(mob);

                    if (enemies.isEmpty()) {
                        //try to find the hero third
                        if (fieldOfView[Dungeon.hero.pos]) {
                            enemies.add(Dungeon.hero);
                        }
                    }
                }

                //if the mob is an ally...
            } else if ( alignment == Alignment.ALLY ) {
                //look for hostile mobs that are not passive to attack
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
                    if (mob.alignment == Alignment.ENEMY
                            && fieldOfView[mob.pos]
                            && mob.state != mob.PASSIVE)
                        enemies.add(mob);

                //if the mob is an enemy...
            } else if (alignment == Alignment.ENEMY) {
                //look for ally mobs to attack
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
                    if (mob.alignment == Alignment.ALLY && fieldOfView[mob.pos])
                        enemies.add(mob);

                //look for bats to kill
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
                    if (mob instanceof Bat)
                        enemies.add(mob);

                //and look for the hero
                if (fieldOfView[Dungeon.hero.pos]) {
                    enemies.add(Dungeon.hero);
                }

            }

            Charm charm = buff( Charm.class );
            if (charm != null){
                Char source = (Char) Actor.findById( charm.object );
                if (source != null && enemies.contains(source) && enemies.size() > 1){
                    enemies.remove(source);
                }
            }

            //neutral characters in particular do not choose enemies.
            if (enemies.isEmpty()){
                return null;
            } else {
                //go after the closest potential enemy, preferring the hero if two are equidistant
                Char closest = null;
                for (Char curr : enemies){
                    if (closest == null
                            || Dungeon.level.distance(pos, curr.pos) < Dungeon.level.distance(pos, closest.pos)
                            || Dungeon.level.distance(pos, curr.pos) == Dungeon.level.distance(pos, closest.pos) && curr == Dungeon.hero){
                        closest = curr;
                    }
                }
                return closest;
            }

        } else
            return enemy;
    }

    public void shoot(Char ch, int pos){
        final Ballistica shot = new Ballistica( ch.pos, pos, Ballistica.PROJECTILE);
        fx(shot, ch);
    }
    ConeAOE cone;

    protected void fx(Ballistica bolt, Char ch ) {
        //need to perform flame spread logic here so we can determine what cells to put flames in.
        affectedCells = new HashSet<>();
        visualCells = new HashSet<>();

        int maxDist = 2 + 4*4;
        int dist = Math.min(bolt.dist, maxDist);

        for (int i = 0; i < PathFinder.CIRCLE8.length; i++){
            if (bolt.sourcePos+PathFinder.CIRCLE8[i] == bolt.path.get(1)){
                break;
            }
        }

        cone = new ConeAOE( bolt,
                maxDist,
                80 + 40,Ballistica.MAGIC_BOLT);

        visualCells.remove(bolt.path.get(dist));

        for (Ballistica ray : cone.rays){
            ((MagicMissile)ch.sprite.parent.recycle( MagicMissile.class )).reset(
                    MagicMissile.HALOFIRE,
                    ch.sprite,
                    ray.path.get(ray.dist),
                    null
            );
        }
        Buff.affect( enemy, HalomethaneBurning.class ).reignite( enemy, 7f );

        //GameScene.add( Blob.seed( target.pos, 7, HalomethaneFire.class ) );

        if(Dungeon.level.heroFOV[bolt.sourcePos] || Dungeon.level.heroFOV[bolt.collisionPos]){
            Sample.INSTANCE.play( Assets.Sounds.ZAP );
        }
    }

    protected void onZap( Ballistica bolt ) {

        for( int cell : affectedCells){

            //ignore caster cell
            if (cell == bolt.sourcePos){
                continue;
            }

            //only ignite cells directly near caster if they are flammable
            GameScene.add( Blob.seed( cell, 1+2, HalomethaneFire.class ) );
        }
    }

    @Override
    public int attackSkill( Char target ) {
        return 15;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 8);
    }

    @Override
    public String description() {
        return Messages.get(this, "desc", gasTankPressure);
    }

    private static final String PRESSURE     = "pressure";

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(PRESSURE, gasTankPressure);
        super.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        gasTankPressure = bundle.getInt(PRESSURE);
        super.restoreFromBundle(bundle);
    }
}
