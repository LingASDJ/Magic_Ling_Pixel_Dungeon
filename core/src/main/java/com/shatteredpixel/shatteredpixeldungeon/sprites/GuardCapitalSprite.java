package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.ParalyticDart;
import com.watabou.noosa.TextureFilm;

public class GuardCapitalSprite extends MobSprite {
    private Animation cast;
    public GuardCapitalSprite() {
        super();

        texture( Assets.Sprites.GUARD_CAPITAL );

        TextureFilm frames = new TextureFilm( texture, 18, 16 );

        idle = new Animation( 1, true );
        idle.frames( frames, 0, 0, 0, 1 );

        run = new Animation( 11, true );
        run.frames( frames, 2, 3, 4, 5, 6, 7 );

        attack = new Animation( 11, false );
        attack.frames( frames,  8,9,10,11, 0 );

        die = new Animation( 11, false );
        die.frames( frames, 12, 13, 14, 15 );

        cast = attack.clone();

        play( idle );
    }

    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {
            ((MissileSprite)parent.recycle( MissileSprite.class )).
                    reset( this, cell, new ParalyticDart(),
                            () -> ch.onAttackComplete());
            play( cast );
            turnTo( ch.pos , cell );
        }  else {
            super.attack( cell );
        }
    }
}

