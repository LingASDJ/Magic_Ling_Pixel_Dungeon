package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class SliceAlterSprite extends MobSprite {

    public SliceAlterSprite() {
        super();

        texture( Assets.Sprites.SWTICH_ALTER );

        TextureFilm frames = new TextureFilm( texture, 30, 24 );

        idle = new Animation( 7, true );
        idle.frames( frames, 0,1,2,3,4,5,6,7,8);

        run = idle.clone();

        attack = idle.clone();

        die = idle.clone();

        play( idle );
    }

}
