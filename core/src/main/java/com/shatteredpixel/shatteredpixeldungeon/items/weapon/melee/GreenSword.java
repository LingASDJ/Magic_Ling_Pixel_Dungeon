//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.jdsdz;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class GreenSword extends MeleeWeapon {
    public GreenSword() {
        this.image = ItemSpriteSheet.DG9;
        this.tier = 3;
    }

    public int damageRoll(Char hero) {
        if (hero instanceof Hero) {
            Hero jim = (Hero)hero;
            Char var3 = jim.enemy();
            if (var3 instanceof Mob && ((Mob)var3).surprisedBy(jim)) {
                int var4 = this.max();
                int var5 = this.min();
                var4 = this.augment.damageFactor(Random.NormalIntRange(this.min() + Math.round((float)(var4 - var5) * 0.9F), this.max()));
                int var6 = jim.STR() - this.STRReq();
                var5 = var4;
                if (var6 > 0) {
                    var5 = var4 + Random.IntRange(0, var6);
                }

                if (Random.Int(2) == 0) {
                    ((Bleeding)Buff.affect(var3, Bleeding.class)).set((float)(var5 * 4));
                }

                return var5;
            }
        }

        return super.damageRoll(hero);
    }

    public int max(int var1) {
        int var2 = Math.round((float)(this.tier + 1) * 1.125F);
        int var3 = Math.round((float)(this.tier + 4) * 1.125F);
        this.DLY = 1.25F;
        return var2 * var1 + var3;
    }

    public int min(int var1) {
        return Math.round((float)(this.tier) * 1.08F) * var1 + Math.round((float)(this.tier + 1) * 0.6675F);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        for (int i : PathFinder.NEIGHBOURS9){

            if (!Dungeon.level.solid[attacker.pos + i]
                    && !Dungeon.level.pit[attacker.pos + i]
                    && Actor.findChar(attacker.pos + i) == null
                    && attacker == Dungeon.hero) {

                GuardianKnight guardianKnight = new GuardianKnight();
                guardianKnight.weapon2 = this;
                guardianKnight.pos = attacker.pos + i;
                guardianKnight.aggro(defender);
                GameScene.add(guardianKnight);
                Dungeon.level.occupyCell(guardianKnight);

                CellEmitter.get(guardianKnight.pos).burst(Speck.factory(Speck.EVOKE), 4);
                break;
            }
        }
        return super.proc(attacker, defender, damage);
    }

    public static class GuardianKnight extends jdsdz {
        {
            state = WANDERING;
            spriteClass = SRPDHBLRTT.class;
            alignment = Alignment.ALLY;
        }

        public GuardianKnight() {
            HP = HT = 5 + Dungeon.escalatingDepth() * 2;
            defenseSkill = 4 + Dungeon.escalatingDepth();
        }

        @Override
        public void die(Object cause) {
            weapon2 = null;
            super.die(cause);
        }

        @Override
        public int drRoll() {
            return Random.Int(Dungeon.escalatingDepth(), Dungeon.escalatingDepth());
        }
    }

    public static class SRPDHBLRTT extends com.shatteredpixel.shatteredpixeldungeon.sprites.GuardSprite {

        public SRPDHBLRTT(){
            super();
            tint(0, 2, 0, 0.4f);
        }

        @Override
        public void resetColor() {
            super.resetColor();
            tint(0, 2, 0, 0.4f);
        }
    }


}
