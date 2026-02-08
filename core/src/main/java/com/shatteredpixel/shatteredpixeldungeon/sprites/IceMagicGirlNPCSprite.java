package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class IceMagicGirlNPCSprite extends MobSprite {
    public IceMagicGirlNPCSprite() {

        texture( Assets.Sprites.ICEGIRL_NPC );

        TextureFilm ren = new TextureFilm(this.texture, 16, 16);

        idle = new MovieClip.Animation(12, true);
        idle.frames(ren, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,2,2,2,2,2,2,1);

        run = new MovieClip.Animation(1, true);
        run.frames(ren, 0);


        die = new MovieClip.Animation(1, false);
        die.frames(ren, 0);

        play(this.idle);
    }
}

