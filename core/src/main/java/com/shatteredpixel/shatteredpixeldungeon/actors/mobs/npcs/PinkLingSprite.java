package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.noosa.TextureFilm;

public class PinkLingSprite extends CharSprite {
    public PinkLingSprite() {
        texture( Assets.Sprites.DKGirl );
        TextureFilm pinkling = new TextureFilm(this.texture, 16, 16);
        this.idle = new Animation(1, true);
        this.idle.frames(pinkling, 0, 1, 2, 3);
        this.run = new Animation(10, true);
        this.run.frames(pinkling, 0);
        this.die = new Animation(10, false);
        this.die.frames(pinkling, 0);
        this.play(this.idle);
    }
}
