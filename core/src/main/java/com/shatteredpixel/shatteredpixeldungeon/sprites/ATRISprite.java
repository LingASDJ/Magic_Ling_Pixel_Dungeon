package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class ATRISprite extends MobSprite {
    public ATRISprite() {

        texture( Assets.Sprites.ATRI );

        TextureFilm ren = new TextureFilm(this.texture, 16, 16);

        idle = new MovieClip.Animation(2, true);
        idle.frames(ren, 0, 0, 1, 1,9,9,10,10,1, 1,9,9,10,10,1, 1,9,9,10,10,1, 1,9,9,10,10,1, 1,9,9,10,10,1, 1,9,9,10,10,1, 1,9,9,10,10,1, 1,9,9,10,10, 1,9,9,10,10,2,3,4,5,6,7,8);

        run = new MovieClip.Animation(10, true);
        run.frames(ren, 0);

        die = new MovieClip.Animation(10, false);
        die.frames(ren, 0);

        play(this.idle);
    }
}
