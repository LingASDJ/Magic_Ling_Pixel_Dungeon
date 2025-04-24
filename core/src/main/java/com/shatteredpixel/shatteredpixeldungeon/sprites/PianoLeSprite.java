package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class PianoLeSprite extends MobSprite {

    public PianoLeSprite() {
        super();

        texture( Assets.Sprites.PIA );

        TextureFilm frames = new TextureFilm( texture, 18, 17 );

        idle = new Animation( 7, true );
        idle.frames( frames, 0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,2,3,4,5,6,7,8,9,10,11 );

        play( idle );
    }
}
