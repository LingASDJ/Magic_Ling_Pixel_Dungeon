package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Drowsy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class Cake extends Food {

  {
    image = ItemSpriteSheet.CAKE;
    energy = Hunger.HUNGRY - 50f;
  }

  public static void cure(Char ch) {
    Buff.detach(ch, Poison.class);
    Buff.detach(ch, Cripple.class);
    Buff.detach(ch, Weakness.class);
    Buff.detach(ch, Vulnerable.class);
    Buff.detach(ch, Bleeding.class);
    Buff.detach(ch, Blindness.class);
    Buff.detach(ch, Drowsy.class);
    Buff.detach(ch, Slow.class);
    Buff.detach(ch, Vertigo.class);
  }

  @Override
  protected void satisfy(Hero hero) {
    Buff.prolong(hero, Haste.class, 10f);
    if (Random.Float() < 0.45f) {
      hero.STR++;
      hero.sprite.showStatus(CharSprite.POSITIVE, "+1");
      GLog.p(Messages.get(this, "eat_good"));
    }
    Buff.affect(hero, Healing.class).setHeal((int) (0.4f * hero.HT / 5), 0.25f, 0);
    cure(hero);
    super.satisfy(hero);
  }

  @Override
  public int value() {
    return 50 * quantity;
  }
}
