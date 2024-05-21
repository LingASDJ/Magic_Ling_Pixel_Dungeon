package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WorstBlizzard;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SnowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfGodIce;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

public class WorstBlizzardFx extends Blob{
    public static int keepTime = 0;
    public static int damageTarget = 0;
    private static boolean firstHit = true;
    @Override
    protected void evolve() {

        int cell;
        Fire fire = (Fire) Dungeon.level.blobs.get( Fire.class );
        Inferno inf = (Inferno)Dungeon.level.blobs.get( Inferno.class );

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

                    WorstBlizzardFx.freeze(cell);

                    off[cell] = cur[cell] - 1;
                    volume += off[cell];
                } else {
                    off[cell] = 0;
                }
            }
        }
    }

    public static void freeze( int cell ){
        Char ch = Actor.findChar( cell );
        if(ch != null){
            if( firstHit && ch.properties().contains(Char.Property.MINIBOSS)) {
                firstHit = false;
                ch.damage( ch.properties().contains(Char.Property.BOSS) ? damageTarget : damageTarget * 2, WandOfGodIce.class);
            }
        }

        if (ch != null && !ch.isImmune(WorstBlizzardFx.class)) {
            if(ch.buff(WorstBlizzard.class)==null){
                Buff.affect(ch, WorstBlizzard.class,1f);
            }
            if (ch.buff(Frost.class) != null){
                Buff.affect(ch, Frost.class, 2f);
            } else {
                if (Dungeon.level.water[cell]&&!ch.isImmune(Frost.class)){
                    Buff.affect(ch, Frost.class, keepTime);
                }
            }
        }

        Heap heap = Dungeon.level.heaps.get( cell );
        if (heap != null) heap.freeze();
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("worstblizzardfx",keepTime);
        bundle.put("damagetarget",damageTarget);
        bundle.put("firsthit",firstHit);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        keepTime = bundle.getInt("worstblizzardfx");
        firstHit = bundle.getBoolean("firsthit");
        damageTarget = bundle.getInt("damagetarget");
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
