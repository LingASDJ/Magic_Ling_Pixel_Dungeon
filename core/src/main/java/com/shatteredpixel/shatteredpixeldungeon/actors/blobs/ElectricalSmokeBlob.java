package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ElectricalSmoke;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlameX;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.BlizzardBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.InfernalBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.ShockingBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;


public class ElectricalSmokeBlob extends Blob{

    public ElectricalSmoke artifact;

    @Override
    protected void evolve() {
        super.evolve();
        blobEffect();

        if(hero.buff(ElectricalSmoke.SmokingAlloy.class)!=null){
            artifact = ( hero.buff(ElectricalSmoke.SmokingAlloy.class)).smoke;
        }

    }

    public void blobEffect(){

        Char ch;
        int cell;
        for (int i = area.left; i < area.right; i++){
            for (int j = area.top; j < area.bottom; j++){
                cell = i + j* Dungeon.level.width();
                if (cur[cell] > 0 && (ch = Actor.findChar( cell )) != null && ch.alignment == Char.Alignment.ENEMY) {

                    if (has(PotionOfFrost.class)){
                        if (!ch.isImmune(Freezing.class) ){
                            Freezing.freeze(cell);
                        }
                    }

                    if(has(PotionOfLiquidFlame.class)){
                        if (!ch.isImmune(Burning.class) ){
                            ch.damage(Random.NormalIntRange( 1, 3 + Dungeon.scalingDepth()/4 ),new Burning());
                        }
                    }

                    if(has(PotionOfToxicGas.class)){
                        if (!ch.isImmune(ToxicGas.class) ){
                            ch.damage(1 + Dungeon.scalingDepth()/5,new ToxicGas());
                        }
                    }

                    if(has(PotionOfLevitation.class)){
                        if (!ch.isImmune(ConfusionGas.class) ){
                            Buff.prolong(ch, Vertigo.class, 2);
                        }
                    }

                    if(has(PotionOfParalyticGas.class)){
                        if (!ch.isImmune(ParalyticGas.class) ){
                            Buff.prolong(ch, Paralysis.class, Paralysis.DURATION);
                        }
                    }

                    if(has(PotionOfLiquidFlameX.class)){
                        if (!ch.isImmune(HalomethaneBurning.class) ){
                            ch.damage(Random.NormalIntRange( 2,4 + Dungeon.depth/5 + hero.lvl/5 ),new HalomethaneBurning());
                        }
                    }

                    if(has(PotionOfCorrosiveGas.class)){
                        if (!ch.isImmune(CorrosiveGas.class) ){
                            Buff.affect(ch, Corrosion.class).set(2f, 0, null);
                        }
                    }

                    if(has(BlizzardBrew.class)){
                        if (!ch.isImmune(Freezing.class) ){
                            Freezing.freeze(cell);
                        }
                    }

                    if(has(InfernalBrew.class)){
                        if (!ch.isImmune(Burning.class) ){
                            ch.damage(Random.NormalIntRange( 1, 3 + Dungeon.scalingDepth()/4 ),new Burning());
                        }
                    }

                    if(has(ShockingBrew.class)){
                        if (!ch.isImmune(ParalyticGas.class) ){
                            Buff.prolong(ch, Paralysis.class, Paralysis.DURATION);
                        }
                    }

                }
            }
        }

        decrease(PotionOfFrost.class);
        decrease(PotionOfLiquidFlame.class);
        decrease(PotionOfToxicGas.class);
        decrease(PotionOfLevitation.class);
        decrease(PotionOfParalyticGas.class);
        decrease(PotionOfLiquidFlameX.class);
        decrease(PotionOfCorrosiveGas.class);
        decrease(BlizzardBrew.class);
        decrease(InfernalBrew.class);
        decrease(ShockingBrew.class);

    }

    public void decrease(Class c){
        if(artifact.potionCate.get(c)>0){
            int old = artifact.potionCate.get(c);
            artifact.potionCate.remove(c);
            artifact.potionCate.put(c,old-artifact.decrease);
            GLog.i(""+artifact.potionCate.get(c));
        }
    }

    public boolean has(Class c){
        return artifact.potionCate.get(c) > 0;
    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );

        emitter.pour( Speck.factory( Speck.STEAM ), 0.4f );
    }

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }

    private static final String ARTIFACT = "artifact";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(ARTIFACT,artifact);
    }

    @Override
    public void restoreFromBundle( Bundle bundle){
        super.restoreFromBundle(bundle);
        artifact = (ElectricalSmoke) bundle.get(ARTIFACT);
    }

}
