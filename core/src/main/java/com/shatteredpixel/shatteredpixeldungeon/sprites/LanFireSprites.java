package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.watabou.noosa.TextureFilm;

public class LanFireSprites extends MobSprite {

    public LanFireSprites() {
        super();

        texture( Assets.Sprites.LanFire );

        TextureFilm frames = new TextureFilm( texture, 24, 20 );

        idle = new Animation( 16, true );
        idle.frames( frames, 0,1,1,2,2,3,3,4,4,3,3,2,2,1,1,0 );

        play( idle );
    }

    public void link(Char var1) {
        super.link(var1);
        this.add(State.SMOKER);
    }


}

