package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class WhiteYanSprite extends MobSprite {

    public WhiteYanSprite() {
        texture( Assets.Sprites.WHITEYAN );

        TextureFilm textureFilm = new TextureFilm(this.texture, 16, 16);

        idle = new MovieClip.Animation(12, true);
        idle.frames(textureFilm, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1);

        run = new MovieClip.Animation(10, true);
        run.frames(textureFilm, 0);

        die = new MovieClip.Animation(10, false);
        die.frames(textureFilm, 0);

        play(this.idle);
    }

}
