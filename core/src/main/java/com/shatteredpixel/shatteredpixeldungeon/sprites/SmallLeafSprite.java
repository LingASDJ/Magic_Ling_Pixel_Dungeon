package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

import java.util.Arrays;

public class SmallLeafSprite extends MobSprite {
    private Animation starStorm;
    public SmallLeafSprite() {
        texture( Assets.Sprites.SMALLEAF );

        TextureFilm textureFilm = new TextureFilm(this.texture, 20, 16);
        idle = new MovieClip.Animation(8, true);
        // 定义一个Integer数组来存储帧序列
        Integer[] frameSequence;

        if (Statistics.amuletObtained) {
            frameSequence = new Integer[]{13,13,13,13,13,13,13,14, 13,13,13,13,13,13,13,14, 13,14,13,13,13,13,13,13,13,14,15,16,17,18,19,20,21,22,23,24,25};
        } else {
            frameSequence = new Integer[]{0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,2,3,4,5,6,7,8,9,10,11,12};
        }

        starStorm = new Animation( 8, false );
        starStorm.frames( textureFilm, 1,1,2,2,3,3,4,4,5,5,6,6 );

        // 使用Arrays.asList将Integer数组转换为List<Object>，
        // 因为可变长参数本质上是数组，但这里需要的是Object类型的数组。
        // 然后使用toArray方法将List转换回数组，因为frames方法接受的是Object...类型的参数。
        idle.frames(textureFilm, Arrays.asList(frameSequence).toArray());

        play(this.idle);
    }

}
