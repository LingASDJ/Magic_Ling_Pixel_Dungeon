package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class RoomStoneSprites extends MobSprite {
    private Animation leap;
    private boolean alt = Random.Int(2) == 0;
    private Animation stab;
    private Animation prep;
    public RoomStoneSprites() {
        super();

        texture( Assets.Sprites.ROOMSTONE);

        TextureFilm frames = new TextureFilm( texture, 20, 18 );

        idle = new MovieClip.Animation( 2, true );
        idle.frames( frames, 0, 0, 0, 1 );

        run = new MovieClip.Animation( 10, true );
        run.frames( frames, 2,3 );

        attack = new MovieClip.Animation( 15, false );
        attack.frames( frames, 2, 3 );

        die = new MovieClip.Animation( 10, false );
        die.frames( frames, 4,5,6);

        leap = new Animation( 1, true );
        leap.frames( frames, 7 );

        stab = new Animation( 12, false );
        stab.frames( frames, 0, 9, 8, 9 );

        prep = new Animation( 1, true );
        prep.frames( frames, 9 );

        play( idle );
    }

    public void leapPrep( int cell ){
        turnTo( ch.pos, cell );
        play( prep );
    }

    @Override
    public void jump(int from, int to, Callback callback) {
        super.jump(from, to, callback);
        play( leap );
    }

    @Override
    public void attack( int cell ) {
        super.attack( cell );
        if (alt) {
            play( stab );
        }
        alt = !alt;
    }

    @Override
    public void onComplete( Animation anim ) {
        super.onComplete( anim == stab ? attack : anim );
    }

}

