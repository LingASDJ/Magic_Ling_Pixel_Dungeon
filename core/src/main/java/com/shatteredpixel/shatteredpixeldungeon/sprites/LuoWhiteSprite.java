package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class LuoWhiteSprite extends MobSprite {
    public LuoWhiteSprite() {

        texture( Assets.Sprites.LXF );

        TextureFilm ren = new TextureFilm(this.texture, 18, 18);

        idle = new MovieClip.Animation(2, true);
        idle.frames(ren, 0, 0, 1, 1);

        run = new MovieClip.Animation(10, true);
        run.frames(ren, 0);

        die = new MovieClip.Animation(10, false);
        die.frames(ren, 0);

        play(this.idle);
    }
}
