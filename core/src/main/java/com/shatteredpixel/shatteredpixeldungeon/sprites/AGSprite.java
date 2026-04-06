package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class AGSprite extends MobSprite {

    public AGSprite() {
        super();

        texture( Assets.Sprites.AG );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new MovieClip.Animation( 1, true );
        idle.frames( frames, 0, 1, 0, 1 );

        play( idle );
    }

}
