package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

import java.util.Arrays;

public class BzmdrSprite extends MobSprite {

    public BzmdrSprite() {
        super();

        texture( Assets.Sprites.BZ );

        TextureFilm frames = new TextureFilm( texture, 16, 18 );

        idle = new MovieClip.Animation(3, true);

        Integer[] frameSequence;
        frameSequence = new Integer[]{0,0,1,1};
        idle.frames(frames, Arrays.asList(frameSequence).toArray());

        play( idle );
    }
}
