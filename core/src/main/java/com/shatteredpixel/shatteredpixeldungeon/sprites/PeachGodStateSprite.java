package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class PeachGodStateSprite extends MobSprite {

    protected MovieClip.Animation idle2;
    protected MovieClip.Animation idle3;
    protected MovieClip.Animation idle4;

    public PeachGodStateSprite() {
        super();

        texture( Assets.Sprites.PEACHGODSTATUE );

        TextureFilm frames = new TextureFilm( texture, 16, 20 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0, 0, 0, 0 );

        idle2 = new Animation( 2, true );
        idle2.frames( frames, 1, 1, 1, 1 );

        idle3 = new Animation( 2, true );
        idle3.frames( frames, 2, 2, 2, 2 );

        idle4 = new Animation( 2, true );
        idle4.frames( frames, 3, 3, 3, 3 );

        run = new Animation( 10, true );
        run.frames( frames, 6, 7, 8, 9, 10 );

        attack = new Animation( 15, false );
        attack.frames( frames, 2, 3, 4, 5, 0 );

        die = new Animation( 10, false );
        die.frames( frames, 11, 12, 13, 14 );

        play( idle );
    }

    public void idle2(){
        play(idle2);
    }

    public void idle3(){
        play(idle3);
    }
    public void idle4(){
        play(idle4);
    }
}
