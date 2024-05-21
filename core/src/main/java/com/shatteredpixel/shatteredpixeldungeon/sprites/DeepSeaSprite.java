package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class DeepSeaSprite extends MobSprite {
    public DeepSeaSprite() {

        texture( Assets.Sprites.DEEP );

        TextureFilm ren = new TextureFilm(this.texture, 16, 16);

        idle = new MovieClip.Animation(3, true);
        idle.frames(ren, 0, 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,37,38,39,40,41,42,43,44,45);

        run = new MovieClip.Animation(10, true);
        run.frames(ren, 0);

        die = new MovieClip.Animation(10, false);
        die.frames(ren, 0);

        play(this.idle);
    }
}
