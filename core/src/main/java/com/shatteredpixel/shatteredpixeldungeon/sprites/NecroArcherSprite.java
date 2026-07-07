package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
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

        run = new Animation( 11, true );
        run.frames( frames, 13,14,15,16 );

        attack = new Animation( 11, false );
        attack.frames( frames,  5,6,7,8,9,10,11,12);

        die = new Animation( 11, false );
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
                            CellEmitter.center(cell).burst(BlastParticle.FACTORY, 10);
                        }
                    } );
            play( cast );
            turnTo( ch.pos , cell );

        } else {

            super.attack( cell );

        }
    }

}
