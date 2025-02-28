package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ConfusionGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ElectricalSmoke;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
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
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.HashMap;

public class ElectricalSmokeParticle extends PixelParticle.Shrinking {

    public HashMap<Class<? extends Potion>, Integer> map;

    public static final Emitter.Factory FACTORY = new Emitter.Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((ElectricalSmokeParticle)emitter.recycle( ElectricalSmokeParticle.class )).reset( x, y );
        }
        @Override
        public boolean lightMode() {
            return true;
        }
    };

    public void setMap(){
        if(Dungeon.hero.buff(ElectricalSmoke.SmokingAlloy.class)!=null){
            map = Dungeon.hero.buff(ElectricalSmoke.SmokingAlloy.class).getMap();
        }
    }

    public ElectricalSmokeParticle() {
        super();

        if(map == null) setMap();

        color( 0xFFFFFF );

        if(map != null){
            int potionCount = 0;
            if(has(PotionOfFrost.class) || has(PotionOfLiquidFlame.class) ||has(PotionOfToxicGas.class) || has(PotionOfLevitation.class)
                    || has(PotionOfParalyticGas.class) || has(PotionOfLiquidFlameX.class) || has(PotionOfCorrosiveGas.class)
                    || has(BlizzardBrew.class) || has(InfernalBrew.class) || has(ShockingBrew.class)){

                if (has(PotionOfFrost.class)){
                    potionCount++;
                    color(0xADD8E6);
                }

                if(has(PotionOfLiquidFlame.class)){
                    potionCount++;
                    color(0xFF69B4);
                }

                if(has(PotionOfToxicGas.class)){
                    potionCount++;
                    color(0x00CED1);
                }

                if(has(PotionOfLevitation.class)){
                    potionCount++;
                    color(0xC0C0C0);
                }

                if(has(PotionOfParalyticGas.class)){
                    potionCount++;
                    color(0xFFFFE0);
                }

                if(has(PotionOfLiquidFlameX.class)){
                    potionCount++;
                    color(0x008B8B);
                }

                if(has(PotionOfCorrosiveGas.class)){
                    potionCount++;
                    color(0x6B8E23);
                }

                if(has(BlizzardBrew.class)) {
                    potionCount++;
                    color(0x00FF7F);
                }

                if (has(InfernalBrew.class)) {
                    potionCount++;
                    color(0xFF69B4);
                }

                if (has(ShockingBrew.class)) {
                    potionCount++;
                    color(0xFFFF00);
                }
                if(potionCount ==2){
                    color(0xFFF0F5);
                } else if (potionCount == 3) {
                    color(0xF0FFF0);
                }
            }
        }

        lifespan = 0.6f;
        revive();

        left = lifespan;

        super.speed.set(speed);
        super.speed.scale( size );
        scale.set( 1.3f );
        this.x = x - super.speed.x * lifespan / 2;
        this.y = y - super.speed.y * lifespan / 2;
        angle = 0.25F;
        angularSpeed=60;
        speed = (new PointF()).polar(angle, 16.0F);
        am=0;
        acc.set( 0, -80 );
    }

    public void reset( float x, float y ) {
        revive();

        if(map == null) setMap();

        color( 0xFFFFFF );

        if(map != null){
            int potionCount = 0;
            if(has(PotionOfFrost.class) || has(PotionOfLiquidFlame.class) ||has(PotionOfToxicGas.class) || has(PotionOfLevitation.class)
                    || has(PotionOfParalyticGas.class) || has(PotionOfLiquidFlameX.class) || has(PotionOfCorrosiveGas.class)
                    || has(BlizzardBrew.class) || has(InfernalBrew.class) || has(ShockingBrew.class)){

                if (has(PotionOfFrost.class)){
                    potionCount++;
                    color(0xADD8E6);
                }

                if(has(PotionOfLiquidFlame.class)){
                    potionCount++;
                    color(0xFF69B4);
                }

                if(has(PotionOfToxicGas.class)){
                    potionCount++;
                    color(0x00CED1);
                }

                if(has(PotionOfLevitation.class)){
                    potionCount++;
                    color(0xC0C0C0);
                }

                if(has(PotionOfParalyticGas.class)){
                    potionCount++;
                    color(0xFFFFE0);
                }

                if(has(PotionOfLiquidFlameX.class)){
                    potionCount++;
                    color(0x008B8B);
                }

                if(has(PotionOfCorrosiveGas.class)){
                    potionCount++;
                    color(0x6B8E23);
                }

                if(has(BlizzardBrew.class)) {
                    potionCount++;
                    color(0x00FF7F);
                }

                if (has(InfernalBrew.class)) {
                    potionCount++;
                    color(0xFF69B4);
                }

                if (has(ShockingBrew.class)) {
                    potionCount++;
                    color(0xFFFF00);
                }
                if(potionCount ==2){
                    color(0xFFF0F5);
                } else if (potionCount == 3) {
                    color(0xF0FFF0);
                }
            }
        }

        this.x = x;
        this.y = y;

        left = lifespan;

        size = 4;
        speed.set( 0 );
    }

    @Override
    public void update() {
        super.update();
        float p = left / lifespan;
        am = p > 0.8f ? (1 - p) * 5 : 1;
    }

    public boolean has(Class c){
        return map.get(c) > 0;
    }
}
