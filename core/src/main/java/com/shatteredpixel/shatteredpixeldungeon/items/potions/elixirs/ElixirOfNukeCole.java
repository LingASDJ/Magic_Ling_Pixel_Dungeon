package com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HaloFireImBlue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WellFed;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.UnlessFlower;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;


public class ElixirOfNukeCole extends Elixir {

    {
        image = ItemSpriteSheet.REDDRAGON;
    }

    @Override
    public void apply(Hero hero) {
        if(Dungeon.hero.buff(UnlessFlower.UnlessFlowerTime.class) == null){
            Buff.affect(hero, WellFed.class).resetload();
            Buff.affect(hero, Barrier.class).setShield((int) (0.3f * hero.HT + 10));
            Buff.affect(hero, Healing.class).setHeal((int) (0.4f * hero.HT + 5), 0.2f, 0);
        } else {
            Buff.affect(hero, HaloFireImBlue.class).set(HaloFireImBlue.DURATION);
            Buff.affect(hero, Haste.class, 10f);
        }
        Sample.INSTANCE.play( Assets.Sounds.BURNING );
        hero.sprite.emitter().burst(FlameParticle.FACTORY, 10);
    }
}
