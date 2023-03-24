package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ClearElemental;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class ClearElementalSprites extends MobSprite {

    private Animation cast;

    public ClearElementalSprites() {
        super();

        texture( Assets.Sprites.CLEAR );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 10, true );
        idle.frames( frames, 0, 1, 2 );

        run = new Animation( 10, true );
        run.frames( frames, 0, 0, 0, 1 );

        attack = new Animation( 15, false );
        attack.frames( frames, 1, 1, 2, 2 );

        die = new Animation( 10, false );
        die.frames( frames, 3, 4, 5, 6, 7, 8 ,9 );

        play( idle );
    }

    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {

            MagicMissile.boltFromChar( parent,
                    MagicMissile.RAINBOW,
                    this,
                    cell,
                    new Callback() {
                        @Override
                        public void call() {
                            ((ClearElemental)ch).onZapComplete();
                        }
                    } );
            Sample.INSTANCE.play( Assets.Sounds.ZAP );
            turnTo( ch.pos , cell );

        } else {

            super.attack( cell );

        }
    }

}
