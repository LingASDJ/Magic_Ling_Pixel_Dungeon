//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MolotovHuntsman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.xykl;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MolotovHuntsmanSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class RedBloodMoon extends MeleeWeapon {

    public static class RedBloodMoonEX extends RedBloodMoon {
        {
            image = ItemSpriteSheet.RedBloodMoonEX;
        }
    }
    @Override
    public int iceCoinValue() {
        if (Badges.isUnlocked(Badges.Badge.NYZ_SHOP)){
            return (int) ((235 + tier*25) * 0.9f);
        }
        return 235 + tier*25;
    }
    public static int deadking=0;

    public RedBloodMoon() {
        this.image = ItemSpriteSheet.RedBloodMoon;
        this.tier = 5;
    }

    public int damageRoll(Char ch) {
        if (ch instanceof Hero) {
            Hero object = (Hero)ch;
            Char enemied = object.enemy();
            if (enemied instanceof Mob && ((Mob)enemied).surprisedBy(object)) {
                int max = this.max();
                int min = this.min();
                max = this.augment.damageFactor(Random.NormalIntRange(this.min() + Math.round((float)(max - min) * 0.9F), this.max()));
                int STR = object.STR() - this.STRReq();
                min = max;
                if (STR > 0) {
                    min = max + Random.IntRange(0, STR);
                }

                if (Random.Int(2) == 0) {
                    Buff.affect(enemied, Bleeding.class).set((float)(min * 4));
                }

                return min;
            }
        }

        return super.damageRoll(ch);
    }

    public int min(int level) {
        return 3 + level;
    }

    public int max(int level) {
        return 12 + level * 5;
    }

  @Override
    public int proc(Char attacker, Char defender, int damage) {
        for (int i : PathFinder.NEIGHBOURS9){

            if (!Dungeon.level.solid[attacker.pos + i]
                    && !Dungeon.level.pit[attacker.pos + i]
                    && Actor.findChar(attacker.pos + i) == null
                    && attacker == Dungeon.hero && deadking<3) {

                if(level()>=3){
                    RedMagicDied guardianKnight1 = new RedMagicDied();
                    guardianKnight1.pos = attacker.pos + i;
                    guardianKnight1.aggro(defender);
                    GameScene.add(guardianKnight1);
                    Dungeon.level.occupyCell(guardianKnight1);
                    deadking++;
                    CellEmitter.get(guardianKnight1.pos).burst(Speck.factory(Speck.EVOKE), 4);
                } else {
                    GuardianKnight guardianKnight1 = new GuardianKnight();
                    guardianKnight1.weapon = this;
                    guardianKnight1.pos = attacker.pos + i;
                    guardianKnight1.aggro(defender);
                    GameScene.add(guardianKnight1);
                    Dungeon.level.occupyCell(guardianKnight1);
                    deadking++;
                    CellEmitter.get(guardianKnight1.pos).burst(Speck.factory(Speck.EVOKE), 4);
                }


                break;
            } else if(!Dungeon.level.solid[attacker.pos + i]) {
                return super.proc( attacker, defender, damage );
            }
        }

      for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
          if (!(mob instanceof GuardianKnight)) {
              deadking = 0;
              break;
          }
      }

        return super.proc(attacker, defender, damage);
    }


    public class RedMagicDied extends MolotovHuntsman {
        {
            state = WANDERING;
            spriteClass = REDPDHBLRTT.class;
            alignment = Alignment.ALLY;
        }

        public RedMagicDied() {
            HP = HT = 5 + RedBloodMoon.this.level() * 2;
            defenseSkill = 4 + RedBloodMoon.this.level();
        }
        private class TRUE { }
        @Override
        public boolean attack(Char enemy, float dmgMulti, float dmgBonus, float accMulti ) {
            boolean result = super.attack( enemy, dmgMulti, dmgBonus, accMulti );
            damage(1,new TRUE());
            return result;
        }

        @Override
        public int drRoll() {
            return Random.Int(Dungeon.escalatingDepth(), Dungeon.escalatingDepth());
        }
        public void die(Object cause) {
            super.die(cause);
            RedBloodMoon.deadking--;
            if (cause != Chasm.class) {
                this.sprite.showStatus(16711680, Messages.get(this,"death_msg_"+Random.IntRange(1, 8)));

            }
        }


    }

    public class GuardianKnight extends xykl {
        {
            state = WANDERING;
            spriteClass = SRPDHBLRTT.class;
            alignment = Alignment.ALLY;
        }

        public GuardianKnight() {
            HP = HT = 5 + RedBloodMoon.this.level() * 2;
            defenseSkill = 4 + RedBloodMoon.this.level();
        }

        @Override
        public void die(Object cause) {
            weapon = null;
            super.die(cause);
        }

        @Override
        public int drRoll() {
            return Random.Int(Dungeon.escalatingDepth(), Dungeon.escalatingDepth());
        }
    }

    public static class SRPDHBLRTT extends com.shatteredpixel.shatteredpixeldungeon.sprites.SRPDHBLRTT {

        public SRPDHBLRTT(){
            super();
            tint(1, 0, 0, 0.4f);
        }

        @Override
        public void resetColor() {
            super.resetColor();
            tint(1, 0, 0, 0.4f);
        }
    }

    public static class REDPDHBLRTT extends MolotovHuntsmanSprite {

        public REDPDHBLRTT(){
            super();
            tint(1, 0, 0, 0.4f);
        }

        @Override
        public void resetColor() {
            super.resetColor();
            tint(1, 0, 0, 0.4f);
        }
    }

    private static final String partcold   = "partcold";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(partcold, deadking);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        deadking = bundle.getInt(partcold);
    }


}
