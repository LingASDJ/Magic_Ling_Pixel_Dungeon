package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class ZeroDreamSprite extends MobSprite {

    public ZeroDreamSprite() {

        texture( Assets.Sprites.SKSH );

        TextureFilm ren = new TextureFilm(this.texture, 12, 15);

        idle = new MovieClip.Animation(2, true);
        idle.frames(ren, 0, 0, 1, 1);

        run = new MovieClip.Animation(10, true);
        run.frames(ren, 0);

        die = new MovieClip.Animation(10, false);
        die.frames(ren, 0);

        play(this.idle);
    }

}

