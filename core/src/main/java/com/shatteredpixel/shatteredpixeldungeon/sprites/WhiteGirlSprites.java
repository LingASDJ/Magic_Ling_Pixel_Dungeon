package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class WhiteGirlSprites extends MobSprite {

    private Animation go;
    private Animation sleep;

    public WhiteGirlSprites() {
        texture( Assets.Sprites.WHITE );

        TextureFilm textureFilm = new TextureFilm(this.texture, 18, 21);
        idle = new MovieClip.Animation(6, true);
        idle.frames(textureFilm, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1);

        run = new MovieClip.Animation(10, true);
        run.frames(textureFilm, 0);

        die = new MovieClip.Animation(10, false);
        die.frames(textureFilm, 0);

        go = new MovieClip.Animation(8, false);
        go.frames(textureFilm, 0);

        sleep = new MovieClip.Animation(6, true);
        sleep.frames(textureFilm, 2,3,4,5,6,7,8,9,10);

        play(this.idle);
    }

    public void go() {
        play( go );
    }

    public void sleep() {
        play( sleep );
    }
}

