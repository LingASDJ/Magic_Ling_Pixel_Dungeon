package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class SothothNPCSprite extends MobSprite {

    private Animation changesBody;

    public SothothNPCSprite() {
        super();

        texture( Assets.Sprites.SOTS );

        TextureFilm frames = new TextureFilm( texture, 32, 32 );

        idle = new Animation( 4, false );
        idle.frames( frames, 0,1);

        changesBody = new Animation( 7, false );
        changesBody.frames( frames, 0, 1, 2, 3 ,4 ,5 ,6, 7 ,8,9, 10,11,12,13,14 );

        die = new Animation( 10, false );
        die.frames( frames, 0 );

        play( idle );
    }

    public void setChangesBody( ){
        play( changesBody );
    }



}

