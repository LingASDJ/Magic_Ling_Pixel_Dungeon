package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class SliceGirlSprite extends MobSprite {

    public SliceGirlSprite() {
        super();

        texture( Assets.Sprites.SWTICH );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 5, true );
        idle.frames( frames, 0, 1, 2, 3, 4 ,5 );

        run = new Animation( 9, true );
        run.frames( frames,  6,7,8,9 );

        attack = new Animation( 12, false );
        attack.frames( frames, 9,10,11,12,13,14);

        die = new Animation( 18, false );
        die.frames( frames, 10, 12, 13,14 );

        play( idle );
    }

    public void leapPrep( int cell ){
        turnTo( ch.pos, cell );
        play( run );
    }

}
