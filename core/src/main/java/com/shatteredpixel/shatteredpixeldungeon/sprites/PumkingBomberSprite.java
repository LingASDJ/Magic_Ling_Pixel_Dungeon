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

        idle = new MovieClip.Animation( 1, true );
        idle.frames( frames, 0,1 );

        run = new MovieClip.Animation( 14, true );
        run.frames( frames, 2,3,4 );

        attack = new MovieClip.Animation( 14, false );
        attack.frames( frames, 5,6,7 );

        die = new MovieClip.Animation( 14, false );
        die.frames( frames, 8,9,10,11,12,13 );

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
