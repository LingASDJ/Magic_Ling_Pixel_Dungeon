package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.watabou.noosa.TextureFilm;

public class GorgonSprite extends MobSprite {
    public GorgonSprite() {
        super();

        texture(Assets.Sprites.GORGON);

        TextureFilm frames = new TextureFilm( texture, 24, 24 );
        idle = new Animation( 5, true );
        idle.frames( frames, 0,1,2,3,4);

        run = new Animation( 9, true );
        run.frames( frames,  5, 6 ,7,8,9);

        attack = new Animation( 9, false );
        attack.frames( frames, 10,11,12,13);

        zap = attack.clone();

        die = new Animation( 9, false );
        die.frames( frames, 14,15,16,17,18);

        play(idle);
    }
}
