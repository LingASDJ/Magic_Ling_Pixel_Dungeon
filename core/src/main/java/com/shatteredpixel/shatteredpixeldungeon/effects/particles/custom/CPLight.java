package com.shatteredpixel.shatteredpixeldungeon.effects.particles.custom;

import com.shatteredpixel.shatteredpixeldungeon.effects.particles.CustomParticle;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Random;

public class CPLight extends CustomParticle {

    {
        color1 = 0xffffaa;
        color2 = 0xffffff;
    }
    public static final Emitter.Factory UP = new Emitter.Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((CPLight)emitter.recycle( CPLight.class )).resetUp( x, y );
        }
    };
    public static final Emitter.Factory DOWN = new Emitter.Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((CPLight)emitter.recycle( CPLight.class )).resetDown( x, y );
        }
    };
    public void resetUp( float x, float y ) {
        revive();

        speed.set( 0, Random.Float( -32, -48 ) );
        this.x = x;
        this.y = y;

        size = 6;
        left = lifespan = 1f;
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
        scale.y = 16 + (1 - p) * 16;
    }
}