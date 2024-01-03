package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class RenSprite extends MobSprite {
    public RenSprite() {

        texture( Assets.Sprites.REN );

        TextureFilm ren = new TextureFilm(this.texture, 12, 14);

        idle = new Animation(2, true);
        idle.frames(ren, 0, 1, 2, 3);

        run = new Animation(10, true);
        run.frames(ren, 0);

        die = new Animation(10, false);
        die.frames(ren, 0);

        play(this.idle);
    }
}
