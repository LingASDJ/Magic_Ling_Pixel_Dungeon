package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Bullet;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class NecroArcherSprite extends MobSprite {
    private Animation cast;
    public NecroArcherSprite() {
        super();

        texture( Assets.Sprites.NECRO_ARCHER);

        TextureFilm frames = new TextureFilm( texture, 23, 17 );

        idle = new Animation( 9, true );
        idle.frames( frames, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,3,4);

        attack = new Animation( 12, false );
        attack.frames( frames, 5,6,7,8,0 );

        run = new Animation( 12, true );
        run.frames( frames,  9,10,11,12,13,14,15,16);

        die = new Animation( 12, false );
        die.frames( frames, 17,18,19,20,21);

        cast = attack.clone();

        play( idle );
    }

    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {

            ((MissileSprite)parent.recycle( MissileSprite.class )).
                    reset( this, cell, new Bullet(), new Callback() {
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
