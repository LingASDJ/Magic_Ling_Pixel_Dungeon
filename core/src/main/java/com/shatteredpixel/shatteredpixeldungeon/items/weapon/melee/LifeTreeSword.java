/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import java.util.ArrayList;

public class LifeTreeSword extends MeleeWeapon {

  {
    image = ItemSpriteSheet.LifeTreeSword;
    hitSoundPitch = 1f;
    ACC = 1.28f; // 28% boost to accuracy
    tier = 3;
    defaultAction = AC_SUMMON;
  }

  @Override
  public ArrayList<String> actions(Hero hero) {
    ArrayList<String> actions = super.actions(hero);
    actions.add(AC_SUMMON);
    return actions;
  }

  @Override
  public void execute(Hero hero, String action) {
    super.execute(hero, action);
    if (action.equals(AC_SUMMON)) {

      curUser = hero;
      curItem = this;
      if (getFood > 74) {
        GameScene.selectCell(summonpos);
      } else {
        GLog.w(Messages.get(LifeTreeSword.class, "not_chare"));
      }
    }
  }

  public void teleportToLocation(int pos) {
    if (Dungeon.level.avoid[pos] || !Dungeon.level.passable[pos] || Actor.findChar(pos) != null) {
      GLog.n(Messages.get(LifeTreeSword.class, "badlocation"));
      return;
    } else if (Dungeon.level.distance(pos, Dungeon.hero.pos) >= 3) {
      GLog.n(Messages.get(LifeTreeSword.class, "nolongrange"));
      return;
    }

    CrivusFruitsFriend ward = new CrivusFruitsFriend();
    ward.pos = pos;
    GameScene.add(ward, 1f);
    getFood = 0;
    Dungeon.level.occupyCell(ward);
    ward.HP = ward.HT = 3 + LifeTreeSword.curItem.buffedLvl() / 3;
    ward.defenseSkill = 4 + LifeTreeSword.curItem.buffedLvl() / 3;
    ward.sprite.emitter().burst(MagicMissile.WardParticle.UP, 6);
    Dungeon.level.pressCell(pos);
    CellEmitter.get(ward.pos).burst(Speck.factory(Speck.EVOKE), 4);
  }

  protected CellSelector.Listener summonpos =
      new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {
          if (target != null) {
            teleportToLocation(target);
          }
        }

        @Override
        public String prompt() {
          return Messages.get(Wand.class, "summon");
        }
      };

  public static final String AC_SUMMON = "summon";

  public String desc() {
    return Messages.get(this, "desc") + "_" + getFood + "_";
  }

  public int proc(Char attacker, Char defender, int damage) {

    if (defender.HP <= damage) {
      getFood += 1;
    }

    return super.proc(attacker, defender, damage);
  }

  private int getFood;

  public void restoreFromBundle(Bundle bundle) {
    super.restoreFromBundle(bundle);
    this.getFood = bundle.getInt("getFood");
  }

  public void storeInBundle(Bundle bundle) {
    super.storeInBundle(bundle);
    bundle.put("getFood", this.getFood);
  }

  @Override
  public int max(int lvl) {
    return 12 + lvl * 2;
  }

  public int min(int lvl) {
    return 9 + lvl;
  }

  public class CrivusFruitsFriend extends Rat {
    {
      spriteClass = CrivusFruitsRedSprites.class;
      alignment = Alignment.ALLY;

      state = WANDERING = new Waiting();

      properties.add(Property.IMMOVABLE);
    }

    @Override
    protected boolean getCloser(int target) {
      return true;
    }

    @Override
    public int damageRoll() {
      return Random.NormalIntRange(5, 8);
    }

    @Override
    public int attackSkill(Char target) {
      return 6;
    }

    @Override
    public int drRoll() {
      return Random.NormalIntRange(0, 4);
    }

    @Override
    protected boolean getFurther(int target) {
      return true;
    }

    private class Waiting extends Mob.Wandering {}

    {
      immunities.add(ToxicGas.class);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
      damage = super.attackProc(enemy, damage);
      Buff.affect(enemy, Cripple.class, 2f);

      return super.attackProc(enemy, damage);
    }

    @Override
    public void damage(int dmg, Object src) {
      if (dmg >= 0) {
        // 限伤1
        dmg = 1;
      }
      super.damage(dmg, src);
    }

    @Override
    public boolean interact(Char c) {
      if (c != Dungeon.hero) {
        return true;
      }
      Game.runOnRenderThread(
          new Callback() {
            @Override
            public void call() {
              GameScene.show(
                  new WndOptions(
                      sprite(),
                      Messages.get(this, "crivusfruitslasher_title"),
                      Messages.get(this, "crivusfruitslasher_body"),
                      Messages.get(this, "crivusfruitslasher_confirm"),
                      Messages.get(this, "crivusfruitslasher_cancel")) {
                    @Override
                    protected void onSelect(int index) {
                      if (index == 0) {
                        die(null);
                        getFood = 70;
                      }
                    }
                  });
            }
          });
      return true;
    }

    @Override
    public void die(Object cause) {
      super.die(cause);
    }
  }

  public static class CrivusFruitsRedSprites
      extends com.shatteredpixel.shatteredpixeldungeon.sprites.RotLasherSprite {

    public CrivusFruitsRedSprites() {
      super();
      tint(0, 1, 1, 0.4f);
    }

    @Override
    public void resetColor() {
      super.resetColor();
      tint(0, 1, 1, 0.4f);
    }
  }
}
