package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Switch extends Food {

  {
    image = ItemSpriteSheet.SWTR;
    energy = Hunger.HUNGRY - 50f;
  }

  @Override
  protected void satisfy(Hero hero) {
    Buff.affect(hero, Barrier.class).setShield(((20 + 20 * Dungeon.depth / 5)));
    super.satisfy(hero);
  }

  @Override
  public int value() {
    return 50 * quantity;
  }
}
