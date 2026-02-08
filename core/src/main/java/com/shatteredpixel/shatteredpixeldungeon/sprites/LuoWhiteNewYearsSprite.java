package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class LuoWhiteNewYearsSprite extends MobSprite {
    public LuoWhiteNewYearsSprite() {

        texture( Assets.Sprites.LXFCJ );

        TextureFilm ren = new TextureFilm(this.texture, 18, 18);

        idle = new MovieClip.Animation(8, true);
        idle.frames(ren, 0, 0, 1, 1, 2, 2, 3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10);

        run = new MovieClip.Animation(10, true);
        run.frames(ren, 0);

        die = new MovieClip.Animation(10, false);
        die.frames(ren, 0);

        play(this.idle);
    }
}

