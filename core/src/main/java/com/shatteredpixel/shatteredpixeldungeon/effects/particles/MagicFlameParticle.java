package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;

public class MagicFlameParticle extends PixelParticle.Shrinking {

    public static final Emitter.Factory FACTORY = new Emitter.Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((MagicFlameParticle)emitter.recycle( MagicFlameParticle.class )).reset( x, y );
        }
        @Override
        public boolean lightMode() {
            return true;
        }
    };

    public MagicFlameParticle() {
        super();

        if(Dungeon.depth == 30){
            color( 0xC792EA );
        } else {
            color( 0xEE7722 );
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
}
