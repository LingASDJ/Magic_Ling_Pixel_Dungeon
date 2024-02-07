package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class WhiteGirlSprites extends MobSprite {
    public WhiteGirlSprites() {
        texture( Assets.Sprites.WHITE );

        TextureFilm textureFilm = new TextureFilm(this.texture, 16, 16);
        idle = new MovieClip.Animation(2, true);
        idle.frames(textureFilm, 0,1,2);

        run = new MovieClip.Animation(10, true);
        run.frames(textureFilm, 0);

        die = new MovieClip.Animation(10, false);
        die.frames(textureFilm, 0);

        play(this.idle);
    }
}

