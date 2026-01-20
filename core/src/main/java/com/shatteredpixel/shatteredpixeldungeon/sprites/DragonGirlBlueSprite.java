package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class DragonGirlBlueSprite extends MobSprite {

    public DragonGirlBlueSprite() {
        texture( Assets.Sprites.DragonBlueGirl);

        TextureFilm blue = new TextureFilm(this.texture, 16, 16);
        idle = new MovieClip.Animation(6, true);
        idle.frames(blue, 0, 1, 2, 3, 4,5, 4,5, 4,5, 4,5, 4,5,6 ,7,8,9,9,9,9,9,9 ,10,10,10,10,10,10,10,10);

        run = new MovieClip.Animation(10, true);
        run.frames(blue, 0);

        die = new MovieClip.Animation(10, false);
        die.frames(blue, 0);

        play(this.idle);
    }
}
