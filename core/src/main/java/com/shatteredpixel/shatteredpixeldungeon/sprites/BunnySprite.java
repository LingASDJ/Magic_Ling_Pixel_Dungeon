package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.watabou.noosa.TextureFilm;

public class BunnySprite extends HeroSprite {

    private static final int FRAME_W = 20;
    private static final int FRAME_H = 22;

    @Override
    public void updateArmor() {

        texture( Assets.Sprites.BUNNY );
        TextureFilm film = new TextureFilm( Assets.Sprites.BUNNY, FRAME_W, FRAME_H );

        idle = new Animation( 1, true );
        idle.frames( film, 0, 1 );

        run = new Animation( 20, true );
        run.frames( film, 2, 3, 4, 5, 6, 7 );

        die = new Animation( 8, false );
        die.frames( film, 8, 9, 10, 11, 12, 13, 14 );

        attack = new Animation( 15, false );
        attack.frames( film, 15, 16, 17, 18 );

        zap = attack.clone();

        operate = new Animation( 8, false );
        operate.frames( film, 19, 20 );

        fly = new Animation( 1, true );
        fly.frames( film, 21 );

        read = new Animation( 20, false );
        read.frames( film, 22, 23 );

        if (Dungeon.hero.isAlive())
            idle();
        else
            die();
    }

}
