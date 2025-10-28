package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class RedShieldHuntsmanSpite extends MobSprite {
    public RedShieldHuntsmanSpite() {
        texture(Assets.Sprites.RED_SAMH);
        TextureFilm frames = new TextureFilm(this.texture, 20, 16);
        this.idle = new MovieClip.Animation(2, true);
        this.idle.frames(frames, new Object[]{0, 0, 1, 0, 0, 0, 1, 1});
        this.run = new MovieClip.Animation(12, true);
        this.run.frames(frames, new Object[]{2, 3, 4, 5, 6, 7});
        this.attack = new MovieClip.Animation(12, false);
        this.attack.frames(frames, new Object[]{8, 9, 10});
        this.die = new MovieClip.Animation(12, false);
        this.die.frames(frames, new Object[]{11, 12, 13});
        play(this.idle);
    }
}

