package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;

public class MorpheusSprite extends MobSprite {

    private Animation activeIdle;
    private Animation HandHat;
    private Animation SelectFate;

    public MorpheusSprite() {
        super();

        texture( Assets.Sprites.MPHON );

        TextureFilm frames = new TextureFilm( texture, 26, 32 );

        idle = new MovieClip.Animation( 9, true );
        idle.frames( frames, 0,0,0,1,1,1,2,3,4,5 );

        activeIdle = new MovieClip.Animation( 5, true );
        activeIdle.frames( frames, 6,7,8,9,10,11 );

        HandHat = new MovieClip.Animation( 7, true );
        HandHat.frames( frames, 12,13,14,15,16,17 );

        SelectFate = new MovieClip.Animation( 7, true );
        SelectFate.frames( frames,  18,19,20,21,22,23 );

        run = new MovieClip.Animation( 1, true );
        run.frames( frames, 0,1 );

        attack = new MovieClip.Animation( 1, false );
        attack.frames( frames, 0,1 );

        die = new MovieClip.Animation( 1, false );
        die.frames( frames, 0,1 );

        play( idle );
    }

    public void HatActivate(){
        idle = HandHat.clone();
        idle();
    }

    public void SelectActivate(){
        idle = SelectFate.clone();
        idle();
    }

    @Override
    public void die() {
        super.die();

        emitter().start( ElmoParticle.FACTORY, 0.03f, 60 );

        if (visible) {
            Sample.INSTANCE.play( Assets.Sounds.BURNING );
        }
    }

}


