package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.watabou.noosa.TextureFilm;

public class GiantWormSprite  extends MobSprite {
    public GiantWormSprite(){
        super();

        texture(Assets.Sprites.GIANT_WORM);

        TextureFilm frames = new TextureFilm( texture, 18, 11 );
        idle = new Animation( 6, true );
        idle.frames( frames, 0,1,2);

        run = new Animation( 9, true );
        run.frames( frames, 3, 4, 5, 6 );

        attack = new Animation( 11, false );
        attack.frames( frames, 7, 8, 9);

        die = new Animation( 9, false );
        die.frames( frames, 10, 11, 12 );

        play(idle);
    }
}
