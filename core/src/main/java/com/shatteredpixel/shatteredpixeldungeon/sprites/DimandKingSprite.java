package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.watabou.noosa.TextureFilm;

public class DimandKingSprite extends MobSprite {
    
    public DimandKingSprite() {
        super();

        texture(Assets.Sprites.DIMK);

        TextureFilm frames = new TextureFilm( texture, 24, 24 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0, 0, 0, 1 );

        run = new Animation( 10, true );
        run.frames( frames, 2, 3, 4);

        attack = new Animation( 15, false );
        attack.frames( frames, 5,6,7,8);

        die = new Animation( 3, false );
        die.frames( frames, 9,10,11 );

        play( idle );
	}
    
}
