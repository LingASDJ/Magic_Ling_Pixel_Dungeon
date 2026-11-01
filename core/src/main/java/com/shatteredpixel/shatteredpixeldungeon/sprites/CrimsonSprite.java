package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.watabou.noosa.TextureFilm;

public class CrimsonSprite extends HeroSprite {

    private static final int FRAME_W = 28;
    private static final int FRAME_H = 19;

    @Override
    public void updateArmor() {

        texture( Assets.Sprites.CRIMSON );
        TextureFilm film = new TextureFilm( Assets.Sprites.CRIMSON, FRAME_W, FRAME_H );

        idle = new Animation( 6, true );
        idle.frames( film, 0,1,2,3,4,5,0,1,2,3,4,5,0,1,2,3,4,5,0,1,2,3,4,5,27,28,29,30,31,31,31,31,32,33,33,33,33,33,33,34 );

        run = new Animation( 20, true );
        run.frames( film, 6,7,8,9,10,11);

        die = new Animation( 8, false );
        die.frames( film, 12,13,14,15,16);

        attack = new Animation( 15, false );
        attack.frames( film, 17,18,19,20,21);

        zap = attack.clone();

        operate = new Animation( 8, false );
        operate.frames( film, 22, 23 );

        fly = new Animation( 1, true );
        fly.frames( film, 24 );

        read = new Animation( 20, false );
        read.frames( film, 25, 26 );

        if (Dungeon.hero.isAlive())
            idle();
        else
            die();
    }

}

