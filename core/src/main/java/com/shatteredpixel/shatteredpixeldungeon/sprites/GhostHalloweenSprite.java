package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Ghost_Halloween;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class GhostHalloweenSprite extends MobSprite {

    public void lookGhost(Char ch){
        if (ch instanceof Ghost_Halloween && ((Ghost_Halloween) ch).activeLook){
          alpha(1f);
        } else {
           alpha(0.1f);
        }
        hideSleep();
    }

    public GhostHalloweenSprite() {
        super();

        alpha(0.1f);

        texture( Assets.Sprites.GHOST_HE );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new MovieClip.Animation( 7, true );
        idle.frames( frames, 0,1,2,3,4 );

        run = new MovieClip.Animation( 9, true );
        run.frames( frames, 5,6,7,8,9 );

        attack = new MovieClip.Animation( 11, false );
        attack.frames( frames, 10,11,12,13 );

        die = new MovieClip.Animation( 9, false );
        die.frames( frames, 14,15,16,17,18,19 );

        play( idle );
    }

}

