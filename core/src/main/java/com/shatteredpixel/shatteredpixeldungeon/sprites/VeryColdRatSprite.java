package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class VeryColdRatSprite extends MobSprite {

    public VeryColdRatSprite() {
        super();

        texture(Assets.Sprites.VERYCOLDRAT);

        TextureFilm frames = new TextureFilm(texture, 16, 16);

        idle = new Animation(1, true);
        idle.frames(frames, 0, 0, 1);

        run = new Animation(9, true);
        run.frames(frames, 6,7,8, 9, 10);

        attack = new Animation(11, false);
        attack.frames(frames, 2, 3, 4, 5);

        die = new Animation(11, false);
        die.frames(frames, 11,12, 13, 14);

        play(idle);
    }
}
