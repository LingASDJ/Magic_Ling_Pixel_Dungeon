package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class MorpheusSprite extends MobSprite {

    public MorpheusSprite() {
        super();

        texture( Assets.Sprites.MPHON );

        TextureFilm frames = new TextureFilm( texture, 32, 32 );

        idle = new MovieClip.Animation( 6, true );
        idle.frames( frames, 0, 1, 2, 3 );

        run = new MovieClip.Animation( 12, true );
        run.frames( frames, 14,15,16,17 );

        attack = new MovieClip.Animation( 18, false );
        attack.frames( frames, 4,5,6,7,8,9,0 );

        die = new MovieClip.Animation( 18, false );
        die.frames( frames, 10, 12, 13 );

        play( idle );
    }



}


