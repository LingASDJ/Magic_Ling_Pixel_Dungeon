package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts;

import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.particles.Emitter;

public class Bullet extends TippedDart {

    {
        image = ItemSpriteSheet.BULLET;
    }

    @Override
    public Emitter emitter() {
        Emitter e = new Emitter();
        e.pos(5, 5);
        e.fillTarget = false;
        e.pour(ElmoParticle.FACTORY, 0.02f);
        return e;
    }

}
