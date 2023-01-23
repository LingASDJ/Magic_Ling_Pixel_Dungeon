package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.FrozenCarpaccio;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.AlchemicalCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class IceFishSword extends Weapon {

  {
    image = ItemSpriteSheet.ICEFISHSWORD;
    hitSound = Assets.Sounds.HIT_CRUSH;
    hitSoundPitch = 1f;
    tier = 6;
    ACC = 2.90f; // 20% boost to accuracy
    DLY = 1.5f; // 2x speed
  }

  @Override
  public Item upgrade() {
    return upgrade(false);
  }

  public static class Recipe
      extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

    {
      inputs = new Class[] {FrozenCarpaccio.class, MagicalInfusion.class, AlchemicalCatalyst.class};
      inQuantity = new int[] {1, 1, 1};

      cost = 20 + Dungeon.depth / 2;

      output = IceFishSword.class;
      outQuantity = 1;
    }
  }

  public void bolt(Integer target, final Mob mob) {
    if (target != null) {

      final Ballistica shot = new Ballistica(Dungeon.hero.pos, target, Ballistica.PROJECTILE);

      fx(shot, () -> onHit(shot, mob));
    }
  }

  protected void onHit(Ballistica bolt, Mob mob) {

    // presses all tiles in the AOE first

    if (mob != null) {

      if (mob.isAlive() && bolt.path.size() > bolt.dist + 1) {
        Buff.prolong(mob, Chill.class, Chill.DURATION / 2f);
        Buff.affect(mob, Bleeding.class).set((float) (4));
      }
    }
  }

  protected void fx(Ballistica bolt, Callback callback) {
    MagicMissile.boltFromChar(
        Dungeon.hero.sprite.emitter(),
        MagicMissile.WARD,
        Dungeon.hero.sprite,
        bolt.collisionPos,
        callback);
  }

  public int tier;

  @Override
  public String info() {

    String info = desc();

    if (levelKnown) {
      info +=
          "\n\n"
              + Messages.get(
                  MeleeWeapon.class,
                  "stats_known",
                  tier,
                  augment.damageFactor(min()),
                  augment.damageFactor(max()),
                  STRReq());
      if (STRReq() > Dungeon.hero.STR()) {
        info += " " + Messages.get(Weapon.class, "too_heavy");
      } else if (Dungeon.hero.STR() > STRReq()) {
        info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
      }
    } else {
      info +=
          "\n\n"
              + Messages.get(MeleeWeapon.class, "stats_unknown", tier, min(0), max(0), STRReq(0));
      if (STRReq(0) > Dungeon.hero.STR()) {
        info += " " + Messages.get(MeleeWeapon.class, "probably_too_heavy");
      }
    }

    switch (augment) {
      case SPEED:
        info += " " + Messages.get(Weapon.class, "faster");
        break;
      case DAMAGE:
        info += " " + Messages.get(Weapon.class, "stronger");
        break;
      case NONE:
    }

    if (enchantment != null && (cursedKnown || !enchantment.curse())) {
      info += "\n\n" + Messages.get(Weapon.class, "enchanted", enchantment.name());
      info += " " + Messages.get(enchantment, "desc");
    }

    if (cursed && isEquipped(Dungeon.hero)) {
      info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
    } else if (cursedKnown && cursed) {
      info += "\n\n" + Messages.get(Weapon.class, "cursed");
    } else if (!isIdentified() && cursedKnown) {
      info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
    }

    return info;
  }

  @Override
  public int min(int lvl) {
    return (Dungeon.depth / 5 + tier + 1)
        + + // 10 base, down from 20
            lvl
            * Math.round(1.0f * (tier + 1)); // scaling unchanged
  }

  @Override
  public int max(int lvl) {
    return 2 * (Dungeon.depth / 5 + tier + 1)
        + // 10 base, down from 20
        lvl * Math.round(1.0f * (tier + 1)); // scaling unchanged
  }

  public int proc(Char attacker, Char defender, int damage) {
    if (attacker instanceof Hero && Random.Float() < 0.2f) {
      for (Mob mob : ((Hero) attacker).visibleEnemiesList()) {
        bolt(mob.pos, mob);
      }
    }

    return super.proc(attacker, defender, damage);
  }

  @Override
  public int STRReq(int lvl) {
    return Dungeon.depth / 10 + 20;
  }
}
