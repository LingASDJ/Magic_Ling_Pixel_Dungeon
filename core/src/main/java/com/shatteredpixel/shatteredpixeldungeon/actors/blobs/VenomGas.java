package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class VenomGas extends Blob {

    public int wandlvl = 0;
    private LinkedHashMap<Char,Integer> targets = new LinkedHashMap<>();

    @Override
    protected void evolve() {
        super.evolve();

        int damage = 1 + Dungeon.scalingDepth()/5 + wandlvl;

        Char ch;
        int cell;

        for (int i = area.left; i < area.right; i++){
            for (int j = area.top; j < area.bottom; j++){
                cell = i + j*Dungeon.level.width();
                if (cur[cell] > 0 && (ch = Actor.findChar( cell )) != null) {
                    if (!ch.isImmune(this.getClass())) {
                        if( !targets.containsKey( ch ) )
                            targets.put( ch, 1 );
                        else
                            targets.put( ch, targets.get( ch ) + 1 );

                        ch.damage( damage + targets.get( ch ), this, Char.DamageType.Element);

                        if(ch.venodamage >= (damage) *8) {
                            if(ch.buff(Blindness.class) == null) {
                                Buff.affect(ch, Blindness.class, 1.5f);
                            } else{
                                ch.buff(Blindness.class).set(1.5f);
                            }
                        }
                        if(ch.venodamage >= (damage) *20) {
                            if(ch.buff(Paralysis.class) == null) {
                                Buff.affect(ch, Paralysis.class, 1.5f);
                            }else{
                                ch.buff(Paralysis.class).set(1.5f);
                            }
                        }
                    }
                }
            }
        }
    }

    private static final String WANDLVL = "wandlvl";
    private static final String SOURCE	= "source";
    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        wandlvl = bundle.getInt(WANDLVL);
        source = bundle.getClass( SOURCE );
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(WANDLVL, wandlvl );
        bundle.put( SOURCE, source );
    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );

        emitter.pour( Speck.factory(Speck.DIED), 0.4f );
    }

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }

    private Class source;
    public VenomGas setWandlvl(int lvl, Class source){
        if (lvl > wandlvl) {
            wandlvl = lvl;
            this.source = source;
        }
        return this;
    }
}