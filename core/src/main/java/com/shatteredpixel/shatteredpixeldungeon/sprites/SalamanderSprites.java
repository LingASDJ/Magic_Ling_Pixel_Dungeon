package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Salamander;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class SalamanderSprites extends MobSprite {

    private Animation cast;

    public SalamanderSprites() {
        super();

        texture( Assets.Sprites.SAESD );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new MovieClip.Animation( 2, true );
        idle.frames( frames, 0,1 );

        run = new MovieClip.Animation( 12, true );
        run.frames( frames, 2,3,4 );

        attack = new MovieClip.Animation( 12, false );
        attack.frames( frames, 5,6 );

        cast = attack.clone();

        die = new MovieClip.Animation( 12, false );
        die.frames( frames, 7,8,9 );

        play( idle );
    }

    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {

            MagicMissile.boltFromChar( parent,
                    MagicMissile.SHAMAN_BLUE,
                    this,
                    cell,
                    new Callback() {
                        @Override
                        public void call() {
                            ((Salamander)ch).onZapComplete();
                        }
                    } );
            Sample.INSTANCE.play( Assets.Sounds.ZAP );
            turnTo( ch.pos , cell );
            play( cast );
        } else {

            super.attack( cell );

        }
    }
}
