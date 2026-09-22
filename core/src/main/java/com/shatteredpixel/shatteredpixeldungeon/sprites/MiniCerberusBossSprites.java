package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class MiniCerberusBossSprites extends MobSprite {

    private Animation dogWoof;

    public MiniCerberusBossSprites() {
        super();

        texture( Assets.Sprites.SMACERS );

        TextureFilm frames = new TextureFilm( texture, 21, 18 );

        idle = new Animation( 12, true );
        idle.frames( frames, 0,1,2,3,4,5,6,7,8);

        run = new Animation( 12, true );
        run.frames( frames, 9,10,11,12,13,14 );

        die = new Animation( 12, false );
        die.frames( frames, 31,32,33,34,35,36 );

        dogWoof = new Animation( 12, false );
        dogWoof.frames( frames, 20,21,22,23,24,25,26,27,28,29,30);

        play( idle );
    }

    public void dogWoof( int cell ){
        if (curAnim == dogWoof) return;

        turnTo( ch.pos, cell );
        play( dogWoof );
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == dogWoof) {
            idle();
        }
        super.onComplete( anim );
    }
}
