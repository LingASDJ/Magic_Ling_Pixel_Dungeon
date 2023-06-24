package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;

public class MagicFireParticle extends PixelParticle.Shrinking {

    public static final Emitter.Factory FACTORY = new Emitter.Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((MagicFireParticle)emitter.recycle( MagicFireParticle.class )).reset( x, y );
        }
        @Override
        public boolean lightMode() {
            return true;
        };
    };

    public MagicFireParticle() {
        super();

        color( 0xFFD700 );
        lifespan = 0.6f;

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