package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.watabou.noosa.TextureFilm;

public class GudaziSprite extends MobSprite {
    private Animation what_up;
    public GudaziSprite() {
        super();
        texture( Assets.Sprites.GUDAZI );
//        if(chinaHoliday == RegularLevel.ChinaHoliday.GQJ) {
//            texture( Assets.Sprites.GQ_GUDAZI );
//        } else if(chinaHoliday == RegularLevel.ChinaHoliday.CJ) {
//            if(Statistics.gudaZiRandomSkin == 2){
//                texture( Assets.Sprites.XS_GUDAZI );
//            } else {
//                texture( Assets.Sprites.HF_GUDAZI );
//            }
//
//        } else if(holiday == RegularLevel.WestHoliday.XMAS){
//            texture( Assets.Sprites.XMS_GUDAZI );
//        } else {
//            texture( Assets.Sprites.GUDAZI );
//        }


        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 9, true );
        idle.frames( frames, 0,0, 1,1, 2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10 );

        die = new Animation( 20, false );
        die.frames( frames, 0 );

        what_up = new Animation( 9, false );
        what_up.frames( frames, 11,12,13,14,15,16 );


        play( idle );
    }

    @Override
    public void die() {
        super.die();
        emitter().start( ElmoParticle.FACTORY, 0.03f, 60 );
    }

    public void What_Up( ){
        play( what_up );
    }
}
