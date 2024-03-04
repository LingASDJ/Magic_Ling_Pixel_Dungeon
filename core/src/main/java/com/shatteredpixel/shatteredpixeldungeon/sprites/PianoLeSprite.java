package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class PianoLeSprite extends MobSprite {

    public PianoLeSprite() {
        super();

        texture( Assets.Sprites.PIA );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 7, true );
        idle.frames( frames, 0, 0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10 );

        play( idle );
    }
}
