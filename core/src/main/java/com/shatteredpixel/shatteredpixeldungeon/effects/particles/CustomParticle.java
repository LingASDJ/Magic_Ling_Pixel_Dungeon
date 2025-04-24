package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class CustomParticle extends PixelParticle.Shrinking {

    public int color1 = 0xffdddd;
    public int color2 = 0x00aaff;

    public CustomParticle(){
    }

    public CustomParticle(int color1, int color2) {
        this.color1 = color1;
        this.color2 = color2;
    }


    public static final Emitter.Factory MISSILE = new Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((CustomParticle)emitter.recycle( CustomParticle.class )).reset( x, y );
        }
    };

    public static final Emitter.Factory TOCENTER = new Factory() {//FIXME looks like garbage
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((CustomParticle)emitter.recycle( CustomParticle.class )).resetToCenter( x, y );
        }
    };
    public static final Emitter.Factory CHAOTIC = new Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((CustomParticle)emitter.recycle( CustomParticle.class )).resetChaotic( x, y );
        }
    };

    public static final Emitter.Factory UP = new Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((CustomParticle)emitter.recycle( CustomParticle.class )).resetUp( x, y );
        }
    };
    public static final Emitter.Factory DOWN = new Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((CustomParticle)emitter.recycle( CustomParticle.class )).resetDown( x, y );
        }
    };
    public static final Emitter.Factory OUTOFCENTER = new Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((CustomParticle)emitter.recycle( CustomParticle.class )).resetOutOfCenter( x, y );
        }
    };

    public void reset( float x, float y ) {
        revive();

        this.x = x;
        this.y = y;

        speed.set( Random.Float( -5, +5 ), Random.Float( -5, +5 ) );

        size = 6;
        left = lifespan = 0.5f;
    }

    public void resetToCenter(float x, float y ) {
        revive();

        size = 8;
        left = lifespan = 0.5f;

        speed.polar( Random.Float( PointF.PI2 ), Random.Float( 16, 32 ) );
        this.x = x - speed.x * lifespan;
        this.y = y - speed.y * lifespan;
    }
    public void resetOutOfCenter(float x, float y ) {
        revive();

        size = 8;
        left = lifespan = 0.5f;

        speed.polar( Random.Float( PointF.PI2 ), Random.Float( -32,-16 ) );
        this.x = x + speed.x * lifespan;
        this.y = y - speed.y * lifespan;
    }

    public void resetChaotic(float x, float y ) {
        revive();

        size = 8;
        left = lifespan = 0.5f;

        speed.polar( Random.Float( PointF.PI2 ), Random.Float( -16, -32 ) );
        this.x = x - speed.x * lifespan;
        this.y = y - speed.y * lifespan;
    }

    public void resetUp( float x, float y ) {
        revive();

        speed.set( Random.Float( -8, +8 ), Random.Float( -32, -48 ) );
        this.x = x;
        this.y = y;

        size = 6;
        left = lifespan = 1f;
    }

    public void resetDown( float x, float y ) {
        revive();

        speed.set( Random.Float( -8, +8 ), Random.Float( 32, 48 ) );
        this.x = x;
        this.y = y - 10;

        size = 6;
        left = lifespan = 1f;
    }


    @Override
    public void update() {
        super.update();

        float p = left / lifespan;
        // alpha: 0 -> 1 -> 0; size: 6 -> 0; color: 0x660044 -> 0x000000
        color( ColorMath.interpolate( color1, color2, p ) );
        am = p < 0.5f ? p * p * 4 : (1 - p) * 2;
    }
}