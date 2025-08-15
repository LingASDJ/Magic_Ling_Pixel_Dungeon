package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class DeadDogSleepCerberusSprite extends MobSprite {

    public DeadDogSleepCerberusSprite() {
        super();

        texture(Assets.Sprites.SCSR);

        TextureFilm frames = new TextureFilm(texture, 36, 27);

        idle = new Animation(5, true);
        idle.frames(frames,  0,1,2,3,4,5,6);

        run = new Animation(5, true);
        run.frames(frames, 0,1,2,3,4,5,6);

        die = new Animation(5, false);
        die.frames(frames, 0,1,2,3,4,5,6);

        play(idle);
    }
}