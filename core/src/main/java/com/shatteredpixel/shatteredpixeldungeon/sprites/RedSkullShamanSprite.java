package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.utils.MobsUtilsRoom;
import com.watabou.noosa.TextureFilm;

public class RedSkullShamanSprite extends MobSprite {
    public RedSkullShamanSprite() {
        this.texture(Assets.Sprites.RED_GNOLL);
        TextureFilm var1 = new TextureFilm(this.texture, 12, 15);
        this.idle = new Animation(2, true);
        this.idle.frames(var1, 0, 0, 0, 1, 0, 0, 1, 1);
        this.run = new Animation(12, true);
        this.run.frames(var1, 4, 5, 6, 7);
        this.attack = new Animation(12, false);
        this.attack.frames(var1, 2, 3, 0);
        this.zap = this.attack.clone();
        this.die = new Animation(12, false);
        this.die.frames(var1, 8, 9, 10);
        this.play(this.idle);
    }

    public void zap(int var1) {
        this.parent.add(new Lightning(this.ch.pos, var1, (MobsUtilsRoom.RedShaman)this.ch));
        this.turnTo(this.ch.pos, var1);
        this.play(this.zap);
    }
}
