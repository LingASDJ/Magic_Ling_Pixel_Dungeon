package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class FodderSprite extends MobSprite {

    private Animation stab;
    private Animation prep;
    private Animation leap;

    private boolean alt = Random.Int(2) == 0;

    public FodderSprite() {
        super();

        texture( Assets.Sprites.EVIL );

        TextureFilm frames = new TextureFilm( texture, 10, 14 );

        idle = new Animation( 7, true );
        idle.frames( frames, 0,1,2,3,4,5 );

        run = new Animation( 11, true );
        run.frames( frames,  6,7,8,9);

        attack = new Animation( 11, false );
        attack.frames( frames, 10,11,12,13);

        stab = new Animation( 11, false );
        stab.frames( frames, 14 );

        prep = new Animation( 1, true );
        prep.frames( frames, 14 );

        leap = new Animation( 1, true );
        leap.frames( frames, 14 );

        die = new Animation( 11, false );
        die.frames( frames, 10,11,12,13);

        play( idle );
    }

    public void leapPrep( int cell ){
        turnTo( ch.pos, cell );
        play( prep );
    }

    @Override
    public void jump( int from, int to, float height, float duration,  Callback callback ) {
        super.jump( from, to, height, duration, callback );
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

