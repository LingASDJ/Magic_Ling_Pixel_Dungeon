package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class ZeroBoatSprite extends MobSprite {
    public ZeroBoatSprite() {

        texture( Assets.Sprites.BOAT );

        TextureFilm ren = new TextureFilm(this.texture, 32, 32);

        idle = new MovieClip.Animation(8, true);
        idle.frames(ren, 0, 1, 2, 3,4);

        run = new MovieClip.Animation(10, true);
        run.frames(ren, 0, 1, 2, 3, 4);

        die = new MovieClip.Animation(10, false);
        die.frames(ren, 0);

        play(this.idle);
    }
}

