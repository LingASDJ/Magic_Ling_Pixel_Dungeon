package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.galaxy.Sothoth;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class SothothSprite extends MobSprite {

    private Animation laser;

    public SothothSprite() {
        super();

        texture( Assets.Sprites.SOTS );

        TextureFilm frames = new TextureFilm( texture, 32, 32 );

        idle = new Animation( 7, true );
        idle.frames( frames, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27 ,28 ,29 );

        run = new Animation( 10, true );
        run.frames( frames, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27 ,28 ,29 );

        attack = new Animation( 15, false );
        attack.frames( frames, 109, 110,111,112,113,114,115,116,117,118,119 );

        die = new Animation( 10, false );
        die.frames( frames, 11, 12, 13, 14 );

        laser = new Animation( 10, false );
        laser.frames( frames, 143,144,145,146,147,158,149,150,151,152,153,153,154 );

        zap = attack.clone();

        play( idle );
    }

    public void setLaser( ){
        play( laser );
    }


    public void zap( int cell ) {

        turnTo( ch.pos , cell );
        play( attack );

        MagicMissile.boltFromChar( parent,
                MagicMissile.FROSTFIRE,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((Sothoth)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }


    public void setChangesBody(){
        TextureFilm frames = new TextureFilm( texture, 32, 32 );
        idle = new Animation( 2, false );
        idle.frames( frames,0, 1, 2, 3 ,4 ,5 ,6, 7 ,8,9, 10,11,12,13,14 );
        idle = run.clone();
        idle();
    }

}
