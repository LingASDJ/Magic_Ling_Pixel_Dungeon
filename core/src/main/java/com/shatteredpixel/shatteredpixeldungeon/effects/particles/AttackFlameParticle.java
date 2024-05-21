package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.watabou.noosa.Game;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;

public class AttackFlameParticle extends PixelParticle.Shrinking {

    public static final Emitter.Factory FACTORY = new Emitter.Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((AttackFlameParticle)emitter.recycle( AttackFlameParticle.class )).reset( x, y );
        }
        @Override
        public boolean lightMode() {
            return true;
        };
    };
    private float time;
    public AttackFlameParticle() {
        super();

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
        float interval = (Game.timeTotal % 9) / 3f;
        color(interval > 1 ? Math.max(0.9f, 1 - interval) : interval,
                interval < 1 ? Math.min(153 + interval * 72, 255) : Math.max(153 - (interval - 1) * 72, 128),
                0.5f);
    }
}

