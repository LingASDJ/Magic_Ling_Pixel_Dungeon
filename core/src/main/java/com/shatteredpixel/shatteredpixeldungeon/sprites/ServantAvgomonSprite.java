package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.HalomethaneFlameParticle;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class ServantAvgomonSprite extends MobSprite {

    public ServantAvgomonSprite() {
        super();

        texture( Assets.Sprites.SATS );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0, 0, 0, 1, 1, 1, 2,0, 0, 0, 1, 1, 1, 2 );

        run = new MovieClip.Animation( 15, true );
        run.frames( frames, 0, 0, 0, 1, 1, 1, 2,0, 0, 0, 1, 1, 1, 2 );

        attack = new MovieClip.Animation( 12, false );
        attack.frames( frames, 5, 6, 7, 8 ,9 );

        die = new MovieClip.Animation( 8, false );
        die.frames( frames, 10, 11, 12, 13, 14 );

        play( idle );
    }

    @Override
    public void play( Animation anim ) {
        if (anim == die) {
            emitter().burst(HalomethaneFlameParticle.FACTORY, 4 );
        }
        super.play( anim );
    }
}