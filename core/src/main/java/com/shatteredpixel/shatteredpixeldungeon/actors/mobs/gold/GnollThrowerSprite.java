package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class GnollThrowerSprite extends MobSprite {
    private Animation cast;
    public GnollThrowerSprite() {
        super();

        texture( Assets.Sprites.GOLD_GNOLL );

        TextureFilm frames = new TextureFilm( texture, 12, 15 );

        idle = new Animation( 2, true );
        idle.frames( frames, 84, 84, 84, 85, 84, 84, 85, 85 );

        run = new Animation( 12, true );
        run.frames( frames, 88, 89, 90, 91 );

        attack = new Animation( 12, false );
        attack.frames( frames, 86, 87, 84 );

        cast = attack.clone();

        die = new Animation( 12, false );
        die.frames( frames, 92, 93, 94 );

        play( idle );
    }
    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {

            ((MissileSprite)parent.recycle( MissileSprite.class )).
                    reset( this, cell, new ThrowingStone(), new Callback() {
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
