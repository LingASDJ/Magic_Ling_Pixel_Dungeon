package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class NecroWarlockSprite extends MobSprite {

    public NecroWarlockSprite() {
        super();

        texture(Assets.Sprites.NECRO_WARLOCK);
        TextureFilm frames = new TextureFilm(texture, 30, 20);

        idle = new Animation(5, true);
        idle.frames(frames, 0,1,2,3);

        run = new Animation(11, true);
        run.frames(frames, 4,5,6,7,8,9,10,11);

        attack = new Animation(11, false);
        attack.frames(frames, 12,13,14,15);

        zap = new Animation(11, false);
        zap.frames(frames, 15, 16, 17, 18);

        die = new Animation(11, false);
        die.frames(frames, 16,17,18,19);

        play(idle);
    }
}
