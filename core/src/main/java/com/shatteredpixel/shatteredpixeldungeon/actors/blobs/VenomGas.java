package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Venom;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class VenomGas extends Blob {

    //FIXME should have strength per-cell
    private int strength = 0;
    private int level = 0;

    private ArrayList<Char> enemies = new ArrayList<>();
    private ArrayList<Integer> damageTotal = new ArrayList<>();


    public void setLevel(Wand wand){
        level = Math.max(wand.buffedLvl(),level);
    }

    @SuppressWarnings("SuspiciousIndentation")
    @Override
    protected void evolve() {
        super.evolve();

        int damage = 1 + Dungeon.scalingDepth()/5 + level;
        int standardDamage = 1 + Dungeon.scalingDepth()/5;

        if (volume == 0){
            strength = 0;
        } else {
            Char ch;
            int cell;

            for (int i = area.left; i < area.right; i++){
                for (int j = area.top; j < area.bottom; j++){
                    cell = i + j* Dungeon.level.width();
                    if (cur[cell] > 0 && (ch = Actor.findChar( cell )) != null) {
                        if (!ch.isImmune(this.getClass()))
                            //Buff.affect(ch, Venom.class).set(2f, strength, source);
                            //原效果
                            ch.damage(damage,this);
                            if(ch.isAlive()){
                                boolean matched = false;
                                int index = -1;
                                for(int in=0; in<enemies.size();in++ ){
                                    if(enemies.get(in) == ch){
                                        matched = true;
                                        damageTotal.set(in,damageTotal.get(in)+damage);
                                        index = in;
                                    };
                                }
                                if(!matched){
                                    enemies.add(ch);
                                    damageTotal.add(damage);
                                }

                                if(index != -1){
                                    if(damageTotal.get(index)> (8 * standardDamage)){
                                        Buff.affect(ch, Blindness.class,1f);
                                        if(damageTotal.get(index)> (20 * standardDamage)){
                                            Buff.affect(ch, Paralysis.class, 1f);
                                        }
                                    }
                                }
                            }
                    }
                }
            }
        }
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

        emitter.pour( Speck.factory(Speck.DIED), 0.4f );
    }

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }

    public VenomGas setStrength(int str){
        return setStrength(str, null);
    }
    private Class source;
    public VenomGas setStrength(int str, Class source){
        if (str > strength) {
            strength = str;
            this.source = source;
        }
        return this;
    }
}