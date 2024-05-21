package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class KongFuSprites extends CharSprite {

    private Animation what_up;

    public KongFuSprites() {

        texture( Assets.Sprites.KONG );

        TextureFilm ren = new TextureFilm(this.texture, 16, 16);

        idle = new MovieClip.Animation(2, true);
        idle.frames(ren, 0, 0, 1, 1,2,2,3,3);

        run = new MovieClip.Animation(10, true);
        run.frames(ren, 0);

        die = new MovieClip.Animation(10, false);
        die.frames(ren, 0);

        what_up = new Animation( 9, false );
        what_up.frames(ren, 6,6,7,7,8,8,9,9,10,10,11,11 );

        play(this.idle);
    }

    public void What_Up( ){
        play( what_up );
    }

}
