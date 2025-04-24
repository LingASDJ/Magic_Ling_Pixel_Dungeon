package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;

public class BrokenFlameParticle extends PixelParticle.Shrinking {

    public static final Emitter.Factory FACTORY = new Emitter.Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((BrokenFlameParticle)emitter.recycle( BrokenFlameParticle.class )).reset( x, y );
        }
        @Override
        public boolean lightMode() {
            return true;
        }
    };

    public BrokenFlameParticle() {
        super();

        color(Window.ORAGNECOLOR);
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
