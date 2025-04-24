package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.watabou.noosa.TextureFilm;

public class RoyalGuardSprite extends MobSprite {
    public RoyalGuardSprite() {
        super();

        texture(Assets.Sprites.ROYAL_GUARD);

        TextureFilm frames = new TextureFilm( texture, 16, 16 );
        idle = new Animation( 1, true );
        idle.frames( frames, 0,1);

        run = new Animation( 9, true );
        run.frames( frames,  2,3,4, 5);

        attack = new Animation( 9, false );
        attack.frames( frames, 6,7,8);

        die = new Animation( 9, false );
        die.frames( frames, 9,10,11);

        play(idle);
    }
}
