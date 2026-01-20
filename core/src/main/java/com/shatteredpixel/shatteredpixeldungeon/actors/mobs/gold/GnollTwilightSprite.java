package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.ParalyticDart;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class GnollTwilightSprite extends MobSprite {

    private Animation cast;

    public GnollTwilightSprite() {
        super();

        texture( Assets.Sprites.GOLD_GNOLL );

        TextureFilm frames = new TextureFilm( texture, 12, 15 );

        idle = new MovieClip.Animation( 2, true );
        idle.frames( frames, 63, 63, 63, 63, 63, 64, 64, 64 );

        run = new MovieClip.Animation( 12, true );
        run.frames( frames, 67, 68, 69, 70 );

        attack = new MovieClip.Animation( 12, false );
        attack.frames( frames, 65, 66, 63 );

        cast = attack.clone();

        die = new MovieClip.Animation( 12, false );
        die.frames( frames, 71, 72, 73 );

        play( idle );
    }

    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {

            ((MissileSprite)parent.recycle( MissileSprite.class )).
                    reset( this, cell, new ParalyticDart(), new Callback() {
                        @Override
                        public void call() {
                            ch.onAttackComplete();
                        }
                    } );

            play( cast );
            turnTo( ch.pos , cell );

        } else {

            super.attack( cell );

        }
    }
}
