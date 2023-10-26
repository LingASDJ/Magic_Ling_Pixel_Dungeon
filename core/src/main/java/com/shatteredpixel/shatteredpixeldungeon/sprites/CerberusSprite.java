package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class CerberusSprite extends MobSprite {

    private Animation skills;

    public  CerberusSprite() {
        super();

        texture( Assets.Sprites.CSBR );

        TextureFilm frames = new TextureFilm( texture, 32, 32);

        idle = new MovieClip.Animation( 6, true );
        idle.frames( frames, 0, 1, 2, 3, 4 ,6, 7,8 );

        run = new MovieClip.Animation( 6, true );
        run.frames( frames, 9,10,11,12,13,14,15,16,17 );

        attack = new MovieClip.Animation( 6, false );
        attack.frames( frames, 18,19,20,21 );

        skills = new MovieClip.Animation( 18, true );
        skills.frames( frames, 22, 23, 24, 25, 26 );

        die = new MovieClip.Animation( 18, false );
        die.frames( frames, 27, 28, 29, 30, 31, 32, 33, 34, 35 );

        play( idle );
    }

    //Skills Animation
    public void Skills( int cell ){
        turnTo( ch.pos, cell );
        play( skills );
    }

}

