package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class KusumiSprites extends MobSprite {
    public KusumiSprites() {

        texture( Assets.Sprites.KUSUMI);

        TextureFilm ren = new TextureFilm(this.texture, 16, 16);

        idle = new Animation(12, true);
        idle.frames(ren, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1);

        run = new Animation(10, true);
        run.frames(ren, 0);

        die = new Animation(10, false);
        die.frames(ren, 0);

        play(this.idle);
    }
}
