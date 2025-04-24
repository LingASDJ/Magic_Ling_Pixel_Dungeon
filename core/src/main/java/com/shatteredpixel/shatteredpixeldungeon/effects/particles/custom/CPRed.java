package com.shatteredpixel.shatteredpixeldungeon.effects.particles.custom;

import com.shatteredpixel.shatteredpixeldungeon.effects.particles.CustomParticle;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Random;

public class CPRed extends CustomParticle {

    {
        color1 = 0xff3300;
        color2 = 0xff0000;
    }
    public static final Emitter.Factory UP = new Emitter.Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((CPRed)emitter.recycle( CPRed.class )).resetUp( x, y );
        }
    };
    public static final Emitter.Factory DOWN = new Emitter.Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((CPRed)emitter.recycle( CPRed.class )).resetDown( x, y );
        }
    };
    public void resetUp( float x, float y ) {
        revive();

        speed.set( 0, Random.Float( -32, -48 ) );
        this.x = x;
        this.y = y;

        size = 6;
        left = lifespan = 0.5f;
    }
    public void resetDown( float x, float y ) {
        revive();

        speed.set( 0, Random.Float( 32, 48 ) );
        this.x = x;
        this.y = y - 30;

        size = 6;
        left = lifespan = 1f;
    }

    @Override
    public void update() {
        super.update();

        float p = left / lifespan;
        am = p < 0.5f ? p : 1 - p;
        scale.x = (1 - p) * 4;
        scale.y = (1 - p) * 4;
    }
}
