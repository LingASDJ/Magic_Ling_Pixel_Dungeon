package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;

public class TyphonSprite extends MobSprite {

    public TyphonSprite() {
        super();

        texture( Assets.Sprites.TYPHON );

        TextureFilm frames = new TextureFilm( texture, 16, 21 );

        idle = new Animation( 5, true );
        idle.frames( frames, 0, 1, 2, 3, 4 );

        run = new Animation( 1, true );
        run.frames( frames, 0,1 );

        attack = new Animation( 1, false );
        attack.frames( frames, 0,1 );

        die = new Animation( 1, false );
        die.frames( frames, 0, 1 );

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

