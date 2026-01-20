package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class CJDragonGirlBlueSprite extends MobSprite {

    public CJDragonGirlBlueSprite() {
        texture( Assets.Sprites.CJBL);

        TextureFilm blue = new TextureFilm(this.texture, 12, 15);
        idle = new MovieClip.Animation(2, true);
        idle.frames(blue, 0, 1, 2, 3, 4,5);

        run = new MovieClip.Animation(10, true);
        run.frames(blue, 0);

        die = new MovieClip.Animation(10, false);
        die.frames(blue, 0);

        play(this.idle);
    }
}
