package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class DogDogMusicSprite extends MobSprite {

    public DogDogMusicSprite() {
        super();

        texture( Assets.Sprites.DOGMUSIC );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 12, true );
        idle.frames( frames, 0,1,2,3,4,5,6,7);

        play( idle );
    }
}