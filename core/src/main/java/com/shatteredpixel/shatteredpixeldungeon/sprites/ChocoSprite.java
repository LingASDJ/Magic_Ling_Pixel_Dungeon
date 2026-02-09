package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class ChocoSprite  extends MobSprite {
    public ChocoSprite() {
        texture(Assets.Sprites.CHOCO);
        TextureFilm textureFilm = new TextureFilm(this.texture, 16, 16);
        idle = new Animation(3, true);
        idle.frames(textureFilm, 0, 0, 0, 1, 0, 0, 0, 1, 0);
        idle();
    }
}
