package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class ArchettoSprite extends MobSprite {
    public ArchettoSprite() {
        super();

        texture( Assets.Sprites.ARCHETTO);
        TextureFilm film = new TextureFilm( texture, 17, 16 );

        idle = new Animation( 3, true );
        idle.frames( film, 0,1,1,2,2,3,3,4,5 );

        run = idle.clone();

        attack = idle.clone();

        idle();
    }
}
