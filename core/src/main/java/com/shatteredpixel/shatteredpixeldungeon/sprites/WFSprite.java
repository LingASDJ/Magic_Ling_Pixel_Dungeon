package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class WFSprite extends MobSprite {

    public WFSprite() {
        super();

        texture( Assets.Sprites.WFS );
        TextureFilm film = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 10, true );
        idle.frames( film, 1, 1, 1, 1, 1, 0, 0, 0, 0 );

        die = new Animation( 20, false );
        die.frames( film, 0 );

        run = idle.clone();

        attack = idle.clone();

        idle();
    }
}

