package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;

public class SmokeFlameParticle extends PixelParticle.Shrinking {

    public static final Emitter.Factory FACTORY = new Emitter.Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((SmokeFlameParticle)emitter.recycle( SmokeFlameParticle.class )).reset( x, y );
        }
    };

    public SmokeFlameParticle() {
        super();

        color(Window.GDX_COLOR);
        lifespan = 0.6f;
        acc.set( 0, -80 );
    }

    public void reset( float x, float y ) {
        revive();

        this.x = x;
        this.y = y;

        left = lifespan;

        size = 2;
        speed.set( 0 );
    }

    @Override
    public void update() {
        super.update();
        float p = left / lifespan;
        am = p > 0.8f ? (1 - p) * 5 : 1;
    }
}
