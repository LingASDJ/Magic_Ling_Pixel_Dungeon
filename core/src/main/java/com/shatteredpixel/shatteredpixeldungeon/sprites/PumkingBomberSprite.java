package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class PumkingBomberSprite extends MobSprite {
    private Animation stab;
    private Animation prep;
    private Animation leap;

    private boolean alt = Random.Int(2) == 0;
    public PumkingBomberSprite() {
        super();

        texture( Assets.Sprites.BOMB );

        TextureFilm frames = new TextureFilm( texture, 16, 18 );

        idle = new MovieClip.Animation( 7, true );
        idle.frames( frames, 0,1,2,3,4,5 );

        run = new MovieClip.Animation( 9, true );
        run.frames( frames, 6,7,8 );

        attack = new MovieClip.Animation( 11, false );
        attack.frames( frames, 9,10,11,12 );

        die = new MovieClip.Animation( 9, false );
        die.frames( frames, 13,14,15,16,17,18 );

        stab = run.clone();

        prep = idle.clone();

        leap = attack.clone();

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
