package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class MoRuoSSprite extends MobSprite {

    public MoRuoSSprite() {

            texture( Assets.Sprites.MORUS );

            TextureFilm ren = new TextureFilm(this.texture, 16, 16);

            idle = new Animation(2, true);
            idle.frames(ren, 0, 0,1, 1);

            run = new Animation(10, true);
            run.frames(ren, 0);

            die = new Animation(10, false);
            die.frames(ren, 0);

            play(this.idle);
        }

}
