package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class CrossDiedSprites extends MobSprite {

    public CrossDiedSprites() {
        super();

        texture(Assets.Sprites.DIED);

        TextureFilm frames = new TextureFilm(texture, 16, 16);

        idle = new Animation(2, true);
        idle.frames(frames, 0, 0, 0, 0);

        run = new Animation(10, true);
        run.frames(frames, 0, 0, 0, 0);

        attack = new Animation(15, false);
        attack.frames(frames, 0, 0, 0, 0);

        die = new Animation(10, false);
        die.frames(frames, 0, 0, 0, 0);

        play(idle);
    }
}