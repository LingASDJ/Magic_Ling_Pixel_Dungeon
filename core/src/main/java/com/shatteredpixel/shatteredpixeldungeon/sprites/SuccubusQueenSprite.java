package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.watabou.noosa.TextureFilm;

public class SuccubusQueenSprite extends MobSprite {

    public SuccubusQueenSprite() {
        super();

        texture( Assets.Sprites.SUCCUBUS_QUEEN );

        TextureFilm frames = new TextureFilm( texture, 20, 22 );

        idle = new Animation( 5, true );
        idle.frames( frames, 0, 1, 2 );

        run = new Animation( 14, true );
        run.frames( frames, 3, 4, 5, 6 );

        attack = new Animation( 12, false );
        attack.frames( frames, 7, 8, 9, 10 );

        die = new Animation( 10, false );
        die.frames( frames, 12 );

        play( idle );
    }

    @Override
    public void die() {
        super.die();
        emitter().burst( Speck.factory( Speck.HEART ), 6 );
        emitter().burst( ShadowParticle.UP, 8 );
    }
}

