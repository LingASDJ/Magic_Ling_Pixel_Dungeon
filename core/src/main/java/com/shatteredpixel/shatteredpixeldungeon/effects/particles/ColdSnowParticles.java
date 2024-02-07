package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class ColdSnowParticles extends PixelParticle {

    public static final Emitter.Factory FACTORY = new Emitter.Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((ColdSnowParticles)emitter.recycle(ColdSnowParticles.class)).reset( x, y );
        }
    };

    private static float angle = Random.Float(6.283185F);

    private static PointF speed = (new PointF()).polar(angle, 10.0F);

    private float size;

    public ColdSnowParticles() {


        switch (Dungeon.depth){
            case 1:case 2:case 3:case 4:case 5:
                texture("leaf.png");
            break;
            case 0:case 10:case 6:case 7:case 8:case 9:
                texture("snowflake.png");
            break;
        }

        lifespan = Random.Float( 1,2);
        scale.set( size = Random.Float() );
        origin.set(width/4,height/4);
    }

    public void reset(float x, float y) {
        revive();


        switch (Dungeon.depth){
            case 1:case 2:case 3:case 4:case 5:
                left = lifespan;
                texture("leaf.png");
                super.speed.set(speed);
                super.speed.scale( size );

                this.x = x - super.speed.x * lifespan / 2;
                this.y = y - super.speed.y * lifespan / 2;
                origin.set(width/2,height/2);
                angle = 0.85F;
                angularSpeed=Random.IntRange(-20,50);
                speed = (new PointF()).polar(angle, 16.0F);
                am = 0; // 保持这个属性不变
                color(0x8780FF80);
                break;
            case 0:case 10:case 6:case 7:case 8:case 9:
                left = lifespan;

                super.speed.set(speed);
                super.speed.scale( size );

                this.x = x - super.speed.x * lifespan / 2;
                this.y = y - super.speed.y * lifespan / 2;
                origin.set(width/2,height/2);
                angle = 0.35F;
                angularSpeed=40;
                speed = (new PointF()).polar(angle, 36.0F);
                am=0;
                color(0xC5F2F0FF);
                //alpha(0.75f);
                break;
        }


    }

    //
    @Override
    public void update() {
        super.update();

        if(Dungeon.depth == 0 && Dungeon.branch == 0 && hero.viewDistance<20){
            return;
        }
        float p = left / lifespan;
        am = (p < 0.5f ? p : 1 - p) * size * 1f;
    }

    public static class Snow extends Group {
        private float delay;

        private int pos;

        private float x;

        private float y;

        public Snow(int pos) {


         this.pos = pos;
         PointF pointF = DungeonTilemap.tileToWorld(pos);
         x = pointF.x;
         y = pointF.y;
         delay = Random.Float(3.0F);



        }

        public void update() {
            super.update();
            if(!Statistics.snow && Dungeon.depth == 0 && Dungeon.branch == 0) {

                if (visible == (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
                    float f = delay - Game.elapsed;
                    delay = f;
                    if (f <= 0.0F) {
                        delay = Random.Float(5.0F);
                        ((ColdSnowParticles) recycle(ColdSnowParticles.class)).reset(this.x + Random.Float(16.0F), this.y + Random.Float(16.0F));
                    }
                }
                visible = true;
            } else if(Statistics.snow && Dungeon.depth == 0 && Dungeon.branch == 0) {
                visible = false;
            } else {
                if (visible == (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
                    float f = delay - Game.elapsed;
                    delay = f;
                    if (f <= 0.0F) {
                        delay = Random.Float(5.0F);
                        ((ColdSnowParticles) recycle(ColdSnowParticles.class)).reset(this.x + Random.Float(16.0F), this.y + Random.Float(16.0F));
                    }
                }
            }
        }
    }
}
