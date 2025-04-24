package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class PinkFoxSprite extends MobSprite {

    private MovieClip.Animation wakeup;

    private MovieClip.Animation idles;

    public PinkFoxSprite() {

        texture( Assets.Sprites.PINKFOX );

        TextureFilm ren = new TextureFilm(this.texture, 16, 16);

        idles = new MovieClip.Animation(3, true);
        idles.frames(ren, 14,15,16,17);

        idle = new MovieClip.Animation(3, true);
        if(!Statistics.PinkFox){
            idle.frames(ren, 0,1,2,3,4,5,6,7);
        } else {
            idle.frames(ren, 14,15,16,17);
        }

        wakeup = new MovieClip.Animation(10, false);
        wakeup.frames(ren,8,9,10,11,12,13,14,15,16);

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
