package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class ClearGuardSprite extends MobSprite {

    private Animation sans;

    public ClearGuardSprite(){
        super();

        texture( Assets.Sprites.CLGR );

        TextureFilm frames = new TextureFilm( texture, 30, 21 );

        idle = new MovieClip.Animation( 7, true );
        idle.frames( frames, 0,1,2,3);

        run = new MovieClip.Animation( 14, true );
        run.frames( frames, 4,5,6,7,8,9 );

        attack = new MovieClip.Animation( 14, false );
        attack.frames( frames, 10,11,12,13,14 );

        die = new MovieClip.Animation( 11, false );
        die.frames( frames,  15,16,17,18 );

        sans = new MovieClip.Animation( 2, false );
        sans.frames( frames,  2,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19 );

        play( idle );
    }

    public void What_UP(){
        play( die );
    }

    public void WUP(){
        play( sans );
    }

    public static class ClearNPCGuardSprite extends MobSprite {

        private Animation sans;

        public ClearNPCGuardSprite(){
            super();

            texture( Assets.Sprites.CLGR );

            TextureFilm frames = new TextureFilm( texture, 30, 21 );

            idle = new MovieClip.Animation( 7, false );
            idle.frames( frames,  17,18,19 );

            run = new MovieClip.Animation( 14, true );
            run.frames( frames, 4,5,6,7,8,9 );

            attack = new MovieClip.Animation( 14, false );
            attack.frames( frames, 10,11,12,13,14 );

            die = new MovieClip.Animation( 11, false );
            die = idle.clone();

            sans = new MovieClip.Animation( 2, false );
            sans.frames( frames,  2,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19 );

            play( idle );
        }

        public void What_UP(){
            play( die );
        }

        public void WUP(){
            play( sans );
        }

    }

}

