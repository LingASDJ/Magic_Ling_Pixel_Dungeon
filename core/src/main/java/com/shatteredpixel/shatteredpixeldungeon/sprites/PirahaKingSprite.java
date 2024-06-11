package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class PirahaKingSprite extends MobSprite {
    private Animation stab;
    private Animation prep;
    private Animation leap;

    private boolean alt = Random.Int(2) == 0;
    public PirahaKingSprite() {
        super();

        renderShadow = false;
        perspectiveRaise = 0.2f;

        texture( Assets.Sprites.PIRANHA_KING );

        TextureFilm frames = new TextureFilm( texture, 34, 44 );

        idle = new Animation( 3, true );
        idle.frames( frames, 0, 1, 2, 3, 4 );

        run = new Animation( 10, true );
        run.frames( frames, 5,6,7,8 );

        attack = new Animation( 9, false );
        attack.frames( frames, 9, 10, 11,12,13 );

        die = new Animation( 9, false );
        die.frames( frames, 14,15,16,17,18,19 );

        stab = new Animation( 12, false );
        stab.frames( frames, 0, 9, 11, 9 );

        prep = new Animation( 7, false );
        prep.frames( frames, 9,10,11,12 );

        leap = new Animation( 8, true );
        leap.frames( frames, 5,6,7,8 );

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


