package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class StormCloudDied extends Buff {

    {
        type = buffType.POSITIVE;
        announced = true;
    }

    public static final float DURATION	= 20f;

    protected float left;

    private static final String LEFT	= "left";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( LEFT, left );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        left = bundle.getFloat( LEFT );
    }

    public void set( float duration ) {
        this.left = duration;
    }
    public static class LightningBolt{}
    @Override
    public boolean act() {

        for(Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob != null) {
                arcs.add( new Lightning.Arc(hero.sprite.center(), mob.sprite.center()));
                mob.damage( 3+Dungeon.hero.lvl/5, new LightningBolt() );
                arc(mob);
            }
        }

        ArrayList<Integer> cells = new ArrayList<>();
        for(int i = 10; i > 0; i--){
            int c = Random.Int(Dungeon.level.length());
            if(hero.fieldOfView[c] && !cells.contains(c)){
                cells.add(c);
            }
        }

        for(int p : cells){
            arcs.add( new Lightning.Arc(hero.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(p)));
            CellEmitter.center( p ).burst( SparkParticle.FACTORY, 3 );
        }

        //don't want to wait for the effect before processing damage.
        hero.sprite.parent.addToFront( new Lightning( arcs, null ) );


        spend(TICK);
        left -= TICK;
        if (left <= 0){
            detach();
        }

        return true;
    }

    @Override
    public int icon() {
        return BuffIndicator.LIGHT_DIED;
    }

    public void reignite( Char ch ) {
        reignite( ch, DURATION );
    }

    public void reignite( Char ch, float duration ) {
        left = duration;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - left) / DURATION);
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString((int)left);
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns(left));
    }

    {
        immunities.add( ToxicGas.class );
        immunities.add( Poison.class );
    }

    private ArrayList<Char> affected = new ArrayList<>();

    ArrayList<Lightning.Arc> arcs = new ArrayList<>();

    private void arc( Char ch ) {

        affected.add( ch );

        int dist;
        if (Dungeon.level.water[ch.pos] && !ch.flying)
            dist = 2;
        else
            dist = 1;

        PathFinder.buildDistanceMap( ch.pos, BArray.not( Dungeon.level.solid, null ), dist );
        for (int i = 0; i < PathFinder.distance.length; i++) {
            if (PathFinder.distance[i] < Integer.MAX_VALUE){
                Char n = Actor.findChar( i );
                if (n == Dungeon.hero && PathFinder.distance[i] > 1)
                    //the hero is only zapped if they are adjacent
                    continue;
                else if (n != null && !affected.contains( n )) {
                    //arcs.add(new Lightning.Arc(ch.sprite.center(), n.sprite.center()));
                    arc(n);
                }
            }
        }
    }


}

