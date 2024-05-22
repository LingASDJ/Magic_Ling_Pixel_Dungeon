package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WorstBlizzard;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SnowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfGodIce;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ShadowCaster;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;

import java.util.ArrayList;

public class WorstBlizzardFx extends Blob{
    public static int wandLevel = 0;
    public static int damageTarget = 0;
    public static int zapPos=0;
    private static boolean firstHit = false;
    @Override
    protected void evolve() {
        super.evolve();

        int cell;
        Fire fire = (Fire) Dungeon.level.blobs.get( Fire.class );
        Inferno inf = (Inferno)Dungeon.level.blobs.get( Inferno.class );

        Point c = Dungeon.level.cellToPoint(zapPos);
        int dist = Math.min(2+wandLevel,6);
        int[] rounding = ShadowCaster.rounding[dist];

        int left, right;
        int curr;
        for (int y = Math.max(0, c.y - dist); y <= Math.min(Dungeon.level.height()-1, c.y + dist); y++) {
            if (rounding[Math.abs(c.y - y)] < Math.abs(c.y - y)) {
                left = c.x - rounding[Math.abs(c.y - y)];
            } else {
                left = dist;
                while (rounding[left] < rounding[Math.abs(c.y - y)]) {
                    left--;
                }
                left = c.x - left;
            }
            right = Math.min(Dungeon.level.width() - 1, c.x + c.x - left);
            left = Math.max(0, left);
            for (curr = left + y * Dungeon.level.width(); curr <= right + y * Dungeon.level.width(); curr++) {
                for (int i = area.left-1; i <= area.right; i++) {
                    for (int j = area.top-1; j <= area.bottom; j++) {
                        cell = i + j*Dungeon.level.width();
                        if (cur[cell] > 0) {

                            if (fire != null)
                                fire.clear(cell);

                            if (inf != null && inf.volume > 0 && inf.cur[cell] > 0){
                                inf.clear(cell);
                                off[cell] = cur[cell] = 0;
                                continue;
                            }

                            freeze(cell);

                            if(volume==0)
                                firstHit = false;
                        }
                    }
                }
                if (!Dungeon.level.solid[curr]) {
                    Char charAt= Actor.findChar(curr);
                    if(charAt!=null ){
                        Buff.affect(charAt, WorstBlizzard.class,1f);
                    }
                }
            }
        }
    }

    private void freeze( int cell ){
        Char ch = Actor.findChar( cell );

        if( ch != null && !firstHit &&  ch.properties().contains(Char.Property.MINIBOSS)) {
            firstHit = true;
            affected.clear();
            arcs.clear();

            affected.add(ch);
            arc(ch);
            for (Char target : affected) {
                if (target == hero) {
                    target.damage( Math.round(damageTarget * 0.8f), WandOfGodIce.class);
                }else {
                    target.damage(target.properties().contains(Char.Property.BOSS) ? damageTarget * 2 : damageTarget, WandOfGodIce.class);
                }
            }

            ch.sprite.parent.addToFront( new Lightning( arcs, null ) );
            Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );
        }

        if (ch != null && !ch.isImmune(WorstBlizzardFx.class)) {
            if(ch.buff(WorstBlizzard.class)==null){
                Buff.affect(ch, WorstBlizzard.class,1f);
            }

            if (ch.buff(Frost.class) != null){
                Buff.affect(ch, Frost.class, 2f);
            } else {
                if (Dungeon.level.water[cell]&&!ch.isImmune(Frost.class)){
                    Buff.affect(ch, Frost.class, Math.min(2+wandLevel,14));
                }
            }
        }

        Heap heap = Dungeon.level.heaps.get( cell );
        if (heap != null) heap.freeze();
    }

    private ArrayList<Char> affected = new ArrayList<>();

    private ArrayList<Lightning.Arc> arcs = new ArrayList<>();

    private void arc( Char ch) {
        int dist = Dungeon.level.water[ch.pos] ? 2 : 1;
        ArrayList<Char> hitThisArc = new ArrayList<>();
        PathFinder.buildDistanceMap( ch.pos, BArray.not( Dungeon.level.solid, null ), dist );
        for (int i = 0; i < PathFinder.distance.length; i++) {
            if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                Char n = Actor.findChar(i);
                if (n != null && !affected.contains(n)) {
                    hitThisArc.add(n);
                }
            }
        }

        affected.addAll(hitThisArc);
        for(Char hit : hitThisArc){
            arcs.add(new Lightning.Arc(ch.sprite.center(), hit.sprite.center()));
            arc(hit);
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("wandlevel",wandLevel);
        bundle.put("damagetarget",damageTarget);
        bundle.put("zappos", zapPos);
        bundle.put("firsthit", firstHit);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        wandLevel = bundle.getInt("wandlevel");
        firstHit = bundle.getBoolean("firsthit");
        damageTarget = bundle.getInt("damagetarget");
        zapPos = bundle.getInt("zappos");
    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );
        emitter.start( SnowParticle.FACTORY, 0.05f, 0 );
    }

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }
}
