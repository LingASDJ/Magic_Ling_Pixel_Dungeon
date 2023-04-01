package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class DM275Sprite extends MobSprite {

    public DM275Sprite() {
        super();
        texture( Assets.Sprites.DM275 );
        TextureFilm texturefilm = new TextureFilm(texture, 22, 20);
        idle = new Animation(10, true);
        idle.frames(texturefilm,0,1);
        run = new Animation(10, true);
        run.frames(texturefilm, 2,3);
        attack = new Animation(15, false);
        attack.frames(texturefilm,4,5,6);
        die = new Animation(20, false);
        die.frames(texturefilm, 0,7,0,7,0,7,0,7,0,7,0,7,8);
        play(idle);
    }
}
