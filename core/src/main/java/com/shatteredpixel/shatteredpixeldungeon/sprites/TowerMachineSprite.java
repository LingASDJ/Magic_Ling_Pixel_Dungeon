package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class TowerMachineSprite extends MobSprite {

    public TowerMachineSprite() {
        super();

        texture( Assets.Sprites.TowerMachine );

        TextureFilm frames = new TextureFilm( texture, 32, 32 );

        idle = new MovieClip.Animation( 10, true );
        idle.frames( frames, 0 );

        run = new MovieClip.Animation( 10, true );
        run.frames( frames, 0 );

        attack = new MovieClip.Animation( 10, false );
        attack.frames( frames, 0 );

        die = new MovieClip.Animation( 9, false );
        die.frames( frames, 1,2,3,4,5,6,7 );

        play( idle );
    }

}

