package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class GalaxyLinkSprite extends HeroSprite {

    private static final int FRAME_W = 26;
    private static final int FRAME_H = 17;

    @Override
    public void updateArmor() {

        texture( Assets.Sprites.GALAXYLINK );
        TextureFilm film = new TextureFilm( Assets.Sprites.GALAXYLINK, FRAME_W, FRAME_H );

        idle = new MovieClip.Animation( 4, true );
        idle.frames( film, 0,1,2,3,4,0,1,2,3,4,0,1,2,3,4,0,1,2,3,4,5,5,6,7,7,7 );

        run = new MovieClip.Animation( 20, true );
        run.frames( film, 8,9,10,11,12,13);

        die = new MovieClip.Animation( 8, false );
        die.frames( film, 14,15,16,17,18,19);

        attack = new MovieClip.Animation( 15, false );
        attack.frames( film, 20,21,22,23,24);

        zap = attack.clone();

        operate = new MovieClip.Animation( 8, false );
        operate.frames( film, 25, 26 );

        fly = new MovieClip.Animation( 1, true );
        fly.frames( film, 27 );

        read = new MovieClip.Animation( 20, false );
        read.frames( film, 28, 29 );

        if (Dungeon.hero.isAlive())
            idle();
        else
            die();
    }

}
