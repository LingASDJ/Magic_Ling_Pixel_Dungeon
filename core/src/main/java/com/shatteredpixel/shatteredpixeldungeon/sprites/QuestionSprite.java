package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class QuestionSprite extends MobSprite {

    public QuestionSprite() {
        super();

        texture( Assets.Sprites.QUESTION );

        TextureFilm frames = new TextureFilm( texture, 12, 15 );

        idle = new Animation( 1, true );
        idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );

        play( idle );
    }
}
