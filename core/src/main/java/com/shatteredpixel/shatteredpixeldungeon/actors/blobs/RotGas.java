package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

public class RotGas extends Blob {
    
    private int strength = 0;
    
    private Class source;

    @Override
    protected void evolve() {
        super.evolve();

        if (volume == 0){
            strength = 0;
            source = null;
        } else {
            Char ch;
            int cell;

            int damage = 1 + Dungeon.scalingDepth()/5;

            for (int i = area.left; i < area.right; i++){
                for (int j = area.top; j < area.bottom; j++){
                    cell = i + j*Dungeon.level.width();
                    if (cur[cell] > 0 && (ch = Actor.findChar( cell )) != null) {
                        if (!ch.isImmune(this.getClass())) {

                            ch.damage(damage, this , Char.DamageType.Element);
                        }
                    }
                }
            }

            for (int i = area.left; i < area.right; i++){
                for (int j = area.top; j < area.bottom; j++){
                    cell = i + j* Dungeon.level.width();
                    if (cur[cell] > 0 && (ch = Actor.findChar( cell )) != null) {
                        if (!ch.isImmune(this.getClass()))
                            Buff.affect(ch, Corrosion.class).set(2f, strength, source);
                    }
                }
            }
        }
    }

    public RotGas setStrength(int str){
        return setStrength(str, null);
    }

    public RotGas setStrength(int str, Class source){
        if (str > strength) {
            strength = str;
            this.source = source;
        }
        return this;
    }

    private static final String STRENGTH = "strength";
    private static final String SOURCE	= "source";

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        strength = bundle.getInt( STRENGTH );
        source = bundle.getClass( SOURCE );
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put( STRENGTH, strength );
        bundle.put( SOURCE, source );
    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );

        emitter.pour( Speck.factory(Speck.ROT), 0.4f );
    }

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }
}

