package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class DeathRongSprite extends MobSprite {
    public DeathRongSprite() {

        texture( Assets.Sprites.ZEROBOAT );

        TextureFilm ren = new TextureFilm(this.texture, 20, 20);

        idle = new MovieClip.Animation(8, true);
        idle.frames(ren, 0, 1, 2, 3);

        run = new MovieClip.Animation(10, true);
        run.frames(ren, 0);

        die = new MovieClip.Animation(10, false);
        die.frames(ren, 0);

        play(this.idle);
    }
}
