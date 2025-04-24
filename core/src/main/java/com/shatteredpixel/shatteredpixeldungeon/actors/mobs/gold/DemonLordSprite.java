package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.watabou.noosa.TextureFilm;

public class DemonLordSprite extends MobSprite {
    public DemonLordSprite() {
        super();

        texture(Assets.Sprites.DEMONLORD);

        TextureFilm frames = new TextureFilm( texture, 22, 20 );
        idle = new Animation( 3, true );
        idle.frames( frames, 0,1,3,4);

        run = new Animation( 7, true );
        run.frames( frames,  5, 6 ,7,8,9);

        attack = new Animation( 9, false );
        attack.frames( frames, 10,11,12,13);

        zap = attack.clone();

        die = new Animation( 11, false );
        die.frames( frames, 14,15,16,17,18,19,20);

        play(idle);
    }
}
