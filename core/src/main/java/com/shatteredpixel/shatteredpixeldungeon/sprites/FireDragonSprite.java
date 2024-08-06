package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.FireDragon;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class FireDragonSprite extends MobSprite {
    private Animation leap;
    private Animation prep;
    public FireDragonSprite() {
        super();

        texture( Assets.Sprites.FRDG );

        TextureFilm frames = new TextureFilm( texture, 24, 24 );

        idle = new Animation( 8, true );
        idle.frames( frames, 0 );

        run = new Animation( 12, true );
        run.frames( frames, 1,2,3,4,5,6 );

        attack = new Animation( 15, false );
        attack.frames( frames, 7,8,9 );

        die = new Animation( 9, false );
        die.frames( frames, 10,11,12);

        zap = new Animation( 12, false );
        zap.frames( frames, 13,14,15);

        leap = new Animation( 1, true );
        leap.frames( frames, 12 );

        prep = new Animation( 1, true );
        prep.frames( frames, 9 );

        play( idle );
    }

    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {

            MagicMissile.boltFromChar( parent,
                    MagicMissile.FIRE_CONE,
                    this,
                    cell,
                    new Callback() {
                        @Override
                        public void call() {

                        }
                    } );
            Sample.INSTANCE.play( Assets.Sounds.HIT );
            play( attack );
        } else {

            super.attack( cell );

        }
    }
    public void zap( int cell ) {

        super.zap( cell );

        MagicMissile.boltFromChar( parent,
                MagicMissile.FIRE,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((FireDragon)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap) {
            idle();
        }
        super.onComplete( anim );
    }

    public void leapPrep( int cell ){
        turnTo( ch.pos, cell );
        play( prep );
    }

}
