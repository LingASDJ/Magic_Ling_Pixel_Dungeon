package com.shatteredpixel.shatteredpixeldungeon.effects.particles.custom;

import com.shatteredpixel.shatteredpixeldungeon.effects.particles.CustomParticle;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Random;

public class CPShield extends CustomParticle {

    {
        color1 = 0xddddff;
        color2 = 0x4444ff;
    }
    public static final Emitter.Factory TOCENTER = new Emitter.Factory() {//FIXME looks like garbage
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((CPShield)emitter.recycle( CPShield.class )).resetToCenter( x, y );
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
}
