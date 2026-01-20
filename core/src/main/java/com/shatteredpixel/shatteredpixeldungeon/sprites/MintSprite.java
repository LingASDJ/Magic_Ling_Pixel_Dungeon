package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class MintSprite extends MobSprite {

    private Animation wakeup;

    private Animation idles;

    public MintSprite() {

        texture( Assets.Sprites.MINT );

        TextureFilm ren = new TextureFilm(this.texture, 16, 16);

        idles = new MovieClip.Animation(4, true);
        idles.frames(ren, 25,26,27,28,29);

        idle = new MovieClip.Animation(3, true);

        if(!Statistics.CatFirst){
            idle.frames(ren, 0,1,2,3,4,5,6,7,8,9,10,11);
        } else {
            idle.frames(ren, 25,26,27,28,29);
        }

        wakeup = new MovieClip.Animation(10, false);
        wakeup.frames(ren,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27);

        run = new MovieClip.Animation(8, true);
        run.frames(ren, 0);

        die = new MovieClip.Animation(10, false);
        die.frames(ren, 0);

        play(this.idle);
    }

    public void wakeUp( ){
        play( wakeup );
    }

    public void idleS( ){
        play( idles );
    }

}

