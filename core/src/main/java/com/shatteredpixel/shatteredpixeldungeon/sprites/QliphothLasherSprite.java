package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingSpike;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class QliphothLasherSprite extends MobSprite {

    public QliphothLasherSprite() {
        super();

        texture( Assets.Sprites.QLPH_LASHER );

        TextureFilm frames = new TextureFilm( texture, 24, 24 );

        idle = new Animation( 4, true );
        idle.frames( frames, 0);

        run = new Animation( 0, true );
        run.frames( frames, 0);

        attack = new Animation( 24, false );
        attack.frames( frames, 1, 2 );

        die = new Animation( 12, false );
        die.frames( frames, 3, 4, 5, 6 );

        zap = attack.clone();

        play( idle );
    }

    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent( cell, ch.pos )) {

            ((MissileSprite)parent.recycle( MissileSprite.class )).
                    reset( this, cell, new ThrowingSpike(), new Callback() {
                        @Override
                        public void call() {
                            ch.onAttackComplete();
                        }
                    } );
            zap( ch.pos );

        } else {

            super.attack( cell );

        }
    }

    @Override
    public int blood() {
        return 0xFF88CC44;
    }

}
