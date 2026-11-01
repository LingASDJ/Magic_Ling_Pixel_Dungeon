package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class ATRISprite extends MobSprite {
    public ATRISprite() {

        texture( Assets.Sprites.ATRI );

        TextureFilm ren = new TextureFilm(this.texture, 15, 17);

        idle = new MovieClip.Animation(4, true);
        idle.frames(ren, 0,1,2,3,4);

        run = new MovieClip.Animation(10, true);
        run.frames(ren, 0);

        die = new MovieClip.Animation(10, false);
        die.frames(ren, 0);

        play(this.idle);
    }
}
