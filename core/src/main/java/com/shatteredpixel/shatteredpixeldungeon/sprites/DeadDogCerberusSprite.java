package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.DeadDogCerberus;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.LanFireGo;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class DeadDogCerberusSprite extends MobSprite {

    //磨牙
    private Animation tooteh;
    //待机2
    private Animation Altidle;
    //挥击
    private Animation SuperAttack;
    //连击
    private Animation ComboAttack;
    //飞扑
    private Animation FlyAttack;

    public DeadDogCerberusSprite() {
        super();

        texture( Assets.Sprites.NCSBR );

        TextureFilm frames = new TextureFilm( texture, 36, 28);

        idle = new Animation( 7, true );
        idle.frames( frames, 9,10,11,12,13,14,15,16 );

        run = new Animation( 9, true );
        run.frames( frames, 17,18,19,20,21 );

        attack = new Animation( 10, false );
        attack.frames( frames, 22,23,24,25 );

        die = new Animation( 10, false );
        die.frames( frames, 0 );

        Altidle = new MovieClip.Animation( 9, true );
        Altidle.frames( frames,  0,1,2,3,4,5,6,7,8);

        SuperAttack = new MovieClip.Animation( 9, false );
        SuperAttack.frames( frames, 33,34,35,36,37 );

        tooteh = new MovieClip.Animation( 9, true );
        tooteh.frames( frames, 27,28,29,30,31,32 );

        FlyAttack = new Animation(9 , true);
        FlyAttack.frames( frames, 26 );

        ComboAttack = new Animation( 9, false );
        ComboAttack.frames( frames, 38,39,40,41,42,43,44,45, 46,47,48,49,50,51,52,53);

        play( idle );
    }

    /**
     * 飞扑攻击的动画
     * @param cell 目标
     */
    public void FlyAttack( int cell ){
        turnTo( ch.pos, cell );
        play( FlyAttack );
    }

    public void Altidle( int cell ){
        turnTo( ch.pos, cell );
        play( Altidle );
    }

    /**
     * 磨牙动画
     */
    public void setTooteh(int cell){
        turnTo( ch.pos, cell );
        play( tooteh );
    }

    @Override
    public void attack( int cell ) {
        if (((DeadDogCerberus)ch).ComboAttackThis) {

            ((MissileSprite)parent.recycle( MissileSprite.class )).
                    reset( this, cell, new LanFireGo(), () -> ch.onAttackComplete());
            play( ComboAttack );
            turnTo( ch.pos , cell );

        } else {

            super.attack( cell );

        }
    }

    @Override
    public void link(Char ch) {
        super.link(ch);

        if (((DeadDogCerberus)ch).HunterReady <= 2){
            play(tooteh);
        }

        renderShadow = false;
    }

    @Override
    public void jump( int from, int to, float height, float duration,  Callback callback ) {
        super.jump( from, to, height, duration, callback );
        play( FlyAttack );
    }

}

