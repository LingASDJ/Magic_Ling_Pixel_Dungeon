package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class WhiteYanSprite extends MobSprite {

    public WhiteYanSprite() {
        texture( Assets.Sprites.WHITEYAN );

        TextureFilm textureFilm = new TextureFilm(this.texture, 24, 24);

        idle = new MovieClip.Animation(9, true);
        idle.frames(textureFilm, 0,1,2,3,4,5);

        run = new MovieClip.Animation(10, true);
        run.frames(textureFilm, 0);

        die = new MovieClip.Animation(10, false);
        die.frames(textureFilm, 0);

        play(this.idle);
    }

}
