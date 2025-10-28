package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class RedTorchManSprite extends MobSprite {
    public RedTorchManSprite() {
        this.texture(Assets.Sprites.RED_TORCH);
        TextureFilm var1 = new TextureFilm(this.texture, 20, 16);
        Integer var2 = 2;
        Integer var3 = 1;
        this.idle = new Animation(2, true);
        Animation var4 = this.idle;
        Integer var5 = 0;
        var4.frames(var1, var5, var5, var5, var3, var5, var5, var3, var3);
        this.run = new Animation(12, true);
        this.run.frames(var1, var2, 3, 4, 5, 6, var2);
        this.attack = new Animation(12, false);
        this.attack.frames(var1, 10, 11, 12);
        this.die = new Animation(12, false);
        this.die.frames(var1, 7, 8, 9);
        this.play(this.idle);
    }

    @Override
    public int blood() {
        return 0xFFcdcdb7;
    }
}
