package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.watabou.noosa.TextureFilm;

public class HealLanFireSprites extends MobSprite {

    public HealLanFireSprites() {
        super();

        texture( Assets.Sprites.HEALRIGH );

        TextureFilm frames = new TextureFilm( texture, 24, 24 );

        idle = new Animation( 24, true );
        idle.frames( frames, 0,1,1,2,2,3,3,4,4,0 );

        die = new Animation( 20, false );
        die.frames( frames, 0 );

        play( idle );
    }

    public void link(Char ch) {
        super.link(ch);
        this.add(State.SMOKER);
    }


}

