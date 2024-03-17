package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class DwarfSoliderSprite extends MobSprite {

    private MovieClip.Animation skills;

    public DwarfSoliderSprite() {
        super();

        texture(Assets.Sprites.DXL);

        TextureFilm frames = new TextureFilm(texture, 20, 16);

        idle = new MovieClip.Animation(7, true);
        idle.frames(frames, 3,3,4,4,5,6,7,8);

        run = new MovieClip.Animation(14, true);
        run.frames(frames, 9, 10, 11, 12, 13, 14, 15, 16, 17);

        attack = new MovieClip.Animation(12, false);
        attack.frames(frames, 18, 19, 20, 21);

        skills = new MovieClip.Animation(12, false);
        skills.frames(frames, 22, 23, 24, 25, 26);

        die = new MovieClip.Animation(14, false);
        die.frames(frames, 27, 28, 29, 30, 31, 32, 33, 34, 35);

        zap = attack.clone();

        play(idle);
    }
}

