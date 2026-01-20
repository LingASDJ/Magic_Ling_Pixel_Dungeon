package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;

public class HollowKnightSprite extends MobSprite {

    public HollowKnightSprite() {
        super();

        texture( Assets.Sprites.HK );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new MovieClip.Animation(8, true);
        idle.frames(frames, 0,0,1,1,2,2,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10,11,11,12,12,13,13,14,14,15,15,16,16,17,17);

        die = new Animation( 2, false );
        die.frames( frames, 0 );

        play( idle );
    }
    @Override
    public void die() {
        super.die();
        emitter().start( ElmoParticle.FACTORY, 0.03f, 60 );

        if (visible) {
            Sample.INSTANCE.play( Assets.Sounds.BURNING );
        }
    }

}

