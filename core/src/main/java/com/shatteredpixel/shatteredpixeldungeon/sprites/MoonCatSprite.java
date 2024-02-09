package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

import java.util.Arrays;

public class MoonCatSprite extends MobSprite {

    private Animation what_up;
    public MoonCatSprite() {
        texture( Assets.Sprites.MOONC );

        TextureFilm textureFilm = new TextureFilm(this.texture, 16, 16);
        idle = new MovieClip.Animation(4, true);
        // 定义一个Integer数组来存储帧序列
        Integer[] frameSequence;
        what_up = new Animation( 9, false );
        if (Statistics.amuletObtained) {
            frameSequence = new Integer[]{11,11,12,12};
            what_up.frames( textureFilm, 13,13,14,14,15,15,16,16,17,17,18,18 );
        } else {
            frameSequence = new Integer[]{0,0,1,1,2,2,3,3,4,4,9,10};
            what_up.frames( textureFilm, 5,5,6,6,7,7,8,8 );
        }




        // 使用Arrays.asList将Integer数组转换为List<Object>，
        // 因为可变长参数本质上是数组，但这里需要的是Object类型的数组。
        // 然后使用toArray方法将List转换回数组，因为frames方法接受的是Object...类型的参数。
        idle.frames(textureFilm, Arrays.asList(frameSequence).toArray());

        play(this.idle);
    }

    public void What_Up( ){
        play( what_up );
    }


}
