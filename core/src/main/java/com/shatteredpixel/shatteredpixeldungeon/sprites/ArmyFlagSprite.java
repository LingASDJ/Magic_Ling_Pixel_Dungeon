package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class ArmyFlagSprite extends MobSprite {

    public ArmyFlagSprite() {
        super();

        texture(Assets.Sprites.DFL);

        TextureFilm frames = new TextureFilm(texture, 16, 24);

        idle = new MovieClip.Animation(7, true);
        idle.frames(frames, 0, 1, 2, 3,4);

        die = new MovieClip.Animation(14, false);
        die.frames(frames, 5,6,7);

        play(idle);
    }
}

