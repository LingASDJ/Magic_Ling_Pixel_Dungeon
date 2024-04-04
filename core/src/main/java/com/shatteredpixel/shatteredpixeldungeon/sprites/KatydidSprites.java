package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Katydid;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class KatydidSprites extends MobSprite {

    private Animation cast;

    public KatydidSprites() {
        super();

        texture( Assets.Sprites.KATID);

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new MovieClip.Animation( 3, true );
        idle.frames( frames, 0,1,2,3 );

        run = new MovieClip.Animation( 9, true );
        run.frames( frames, 4,5,6,7 );

        attack = new MovieClip.Animation( 9, false );
        attack.frames( frames, 8,9 );

        cast = attack.clone();

        die = new MovieClip.Animation( 9, false );
        die.frames( frames, 10,11 );

        play( idle );
    }

    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {

            MagicMissile.boltFromChar( parent,
                    MagicMissile.POISON,
                    this,
                    cell,
                    new Callback() {
                        @Override
                        public void call() {
                            ((Katydid)ch).onZapComplete();
                        }
                    } );
            Sample.INSTANCE.play( Assets.Sounds.GAS );
            turnTo( ch.pos , cell );
            play( cast );
        } else {

            super.attack( cell );

        }
    }
}

