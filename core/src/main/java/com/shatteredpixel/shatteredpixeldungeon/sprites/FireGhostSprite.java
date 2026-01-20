//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.watabou.noosa.TextureFilm;

public class FireGhostSprite extends MobSprite {
    public FireGhostSprite() {
        super();

        this.texture("sprites/wengi.png");

        TextureFilm frames = new TextureFilm( texture, 14, 15 );

        this.idle = new Animation(5, true);
        Animation var3 = this.idle;
        var3.frames(frames, 0, 1);
        this.run = new Animation(10, true);
        this.run.frames(frames, 0, 1);
        this.attack = new Animation(10, false);
        this.attack.frames(frames, 0, 2, 3);
        this.die = new Animation(8, false);
        this.die.frames(frames, 0, 4, 5, 6, 7);
        this.play(this.idle);
    }

    @Override
    public int blood() {
        return 0xFFFF7D13;
    }
}
