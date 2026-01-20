package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BruteBot;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Bolas;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Tomahawk;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class BruteBotSprite extends MobSprite {
    private Animation cast;


    public BruteBotSprite() {
        super();

        texture( Assets.Sprites.BRUTE );

        TextureFilm frames = new TextureFilm( texture, 12, 16 );

        idle = new Animation( 2, true );
        idle.frames( frames, 42, 42, 42, 43, 43, 42, 42, 42 );

        run = new Animation( 12, true );
        run.frames( frames, 45, 46, 47, 48 );

        attack = new Animation( 12, false );
        attack.frames( frames, 43, 44 );

        die = new Animation( 12, false );
        die.frames( frames, 49, 50, 51 );

        cast = attack.clone();
        play( idle );
    }



    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {
            switch (((BruteBot) ch).count){
                case 1: default:
                    ((MissileSprite)parent.recycle( MissileSprite.class )).
                            reset( this, cell, new Bolas(), new Callback() {
                                @Override
                                public void call() {
                                    ch.onAttackComplete();
                                }
                            } );
                    play( cast );
                    turnTo( ch.pos , cell );
                    break;
                case 2:
                    ((MissileSprite)parent.recycle( MissileSprite.class )).
                            reset( this, cell, new Tomahawk(), new Callback() {
                                @Override
                                public void call() {
                                    ch.onAttackComplete();
                                }
                            } );
                    play( cast );
                    turnTo( ch.pos , cell );
                    break;
            }
        } else {
            super.attack( cell );
        }
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap) {
            idle();
        }
        super.onComplete( anim );
    }


}

