package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

import java.util.Arrays;

public class JITSprite extends MobSprite {
    private Animation what_up;
    public JITSprite() {
        super();

        texture( Assets.Sprites.JIT );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new MovieClip.Animation(4, true);
        // 定义一个Integer数组来存储帧序列
        Integer[] frameSequence;

        what_up = new Animation( 8, false );

        if (Statistics.amuletObtained) {
            frameSequence = new Integer[]{8,8,9,9,10,10,11,11,12,12,13,13};
            what_up.frames( frames, 14,14,15,15 );
        } else {
            frameSequence = new Integer[]{0,0,1,1,2,2,3,3,4,4,5,5};
            what_up.frames( frames, 6,6,7,7 );
        }
        idle.frames(frames, Arrays.asList(frameSequence).toArray());

        play( idle );
    }

    public void What_Up( ){
        play( what_up );
    }

}
