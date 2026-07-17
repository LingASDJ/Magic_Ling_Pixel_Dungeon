package com.shatteredpixel.shatteredpixeldungeon.levels.traps.tomb;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.watabou.noosa.audio.Sample;

public class CorpseDustTrap extends Trap {

    {
        color = TOMB;
        shape = GRILL;
    }

    @Override
    public void activate() {

       new CorpseDust.MiniCorpseDust().doPickUp(Dungeon.hero);

        CellEmitter.get(Dungeon.hero.pos).burst(ShadowParticle.UP, 25);
        Sample.INSTANCE.play( Assets.Sounds.CURSED );
    }

}
