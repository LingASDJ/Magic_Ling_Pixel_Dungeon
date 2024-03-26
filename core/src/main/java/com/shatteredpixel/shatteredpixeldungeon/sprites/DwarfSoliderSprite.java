package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class DwarfSoliderSprite extends MobSprite {

    public DwarfSoliderSprite() {
        super();

        texture(Assets.Sprites.DXL);

        TextureFilm frames = new TextureFilm(texture, 20, 16);

        idle = new MovieClip.Animation(4, true);
        idle.frames(frames, 0,0,0,1,1,1);

        run = new MovieClip.Animation(12, true);
        run.frames(frames, 2,3,4,5);

        attack = new MovieClip.Animation(12, false);
        attack.frames(frames, 6,7,8);

        die = new MovieClip.Animation(9, false);
        die.frames(frames, 9,9,9,10,10,10);

        play(idle);
    }
}

