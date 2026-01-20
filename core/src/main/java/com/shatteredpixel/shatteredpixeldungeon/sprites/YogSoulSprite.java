package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class YogSoulSprite extends MobSprite {

    public YogSoulSprite() {
        super();

        texture( Assets.Sprites.YOG_SOUL );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new MovieClip.Animation( 4, true );
        idle.frames( frames, 0,1,2,3,4,5,6);

        run = new MovieClip.Animation( 7, true );
        run = idle.clone();

        attack = new MovieClip.Animation( 10, false );
        attack = attack.frames( frames, 4,5,6,7,7,8,8);

        die = new MovieClip.Animation( 7, false );
        die.frames( frames, 7,7,8,8,9,9);

        play( idle );
    }

}