package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

import java.util.Arrays;

public class BzmdrSprite extends MobSprite {

    public BzmdrSprite() {
        super();

        texture( Assets.Sprites.BZ );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new MovieClip.Animation(4, true);
        // 定义一个Integer数组来存储帧序列
        Integer[] frameSequence;

        if (Statistics.amuletObtained) {
            frameSequence = new Integer[]{2,2,3,3};
        } else {
            frameSequence = new Integer[]{0,0,1,1};
        }
        idle.frames(frames, Arrays.asList(frameSequence).toArray());

        play( idle );
    }
}
