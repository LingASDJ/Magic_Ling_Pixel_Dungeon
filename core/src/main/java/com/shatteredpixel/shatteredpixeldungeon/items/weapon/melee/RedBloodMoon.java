//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MolotovHuntsman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.xykl;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MolotovHuntsmanSprite;
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
        return 235 + tier * 25;
    }

    public RedBloodMoon() {
        this.image = ItemSpriteSheet.RedBloodMoon;
        this.tier = 4;
    }

    public int min(int level) {
        return 3 + level;
    }

    public int max(int level) {
        return 12 + level * 5;
    }

    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob instanceof RedBloodMoon.GuardianKnight
                    || mob instanceof RedBloodMoon.RedMagicDied) {
                mob.die(null);
            }
        }
        return super.doUnequip(hero, collect, single);
    }

  @Override
    public int proc(Char attacker, Char defender, int damage) {
        for (int i : PathFinder.NEIGHBOURS9){
            float count = 0;

            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                if (mob instanceof RedBloodMoon.GuardianKnight
                        || mob instanceof RedBloodMoon.RedMagicDied) {
                    count += mob.spawningWeight();
                }
            }

            if (attacker == hero && count < 2) {
                if(level()>=3){
                    RedMagicDied guardianKnight1 = new RedMagicDied();
                    guardianKnight1.pos = attacker.pos + i;
                    guardianKnight1.aggro(defender);
                    GameScene.add(guardianKnight1);
                    Dungeon.level.occupyCell(guardianKnight1);
                    CellEmitter.get(guardianKnight1.pos).burst(Speck.factory(Speck.EVOKE), 4);
                } else {
                    GuardianKnight guardianKnight1 = new GuardianKnight();
                    guardianKnight1.weapon = this;
                    guardianKnight1.pos = attacker.pos + i;
                    guardianKnight1.aggro(defender);
                    GameScene.add(guardianKnight1);
                    Dungeon.level.occupyCell(guardianKnight1);
                    CellEmitter.get(guardianKnight1.pos).burst(Speck.factory(Speck.EVOKE), 4);
                }
                break;
            } else if(!Dungeon.level.solid[attacker.pos + i]) {
                return super.proc( attacker, defender, damage );
            }
        }
        return super.proc(attacker, defender, damage);
    }


    public static class RedMagicDied extends Mob {
        {
            state = WANDERING;
            spriteClass = REDPDHBLRTT.class;
            alignment = Alignment.ALLY;
        }

        @Override
        public int attackProc(Char var1, int var2) {
            int var4;
            if (Random.Int(0, 10) > 7) {

                var4 = Random.IntRange(1, 10);
                this.sprite.showStatus(16711680, Messages.get(MolotovHuntsman.class,"attack_msg_"+var4));
            }

            int var5 = super.attackProc(var1, var2);
            var4 = var1.pos;
            CellEmitter.center(var4).burst(BlastParticle.FACTORY, 30);
            return var5;
        }

        public RedMagicDied() {
            if(hero.belongings.weapon instanceof RedBloodMoon){
                HP = HT = 8 + hero.belongings.weapon.level() * 2;
                defenseSkill = 4 + hero.belongings.weapon.level();
            } else {
                HP = HT = 10;
                defenseSkill = 4;
            }
        }

        private class TRUE { }
        @Override
        public boolean attack(Char enemy, float dmgMulti, float dmgBonus, float accMulti ) {
            damage(Random.NormalIntRange(1,hero.belongings.weapon.level()/3+2),new TRUE());
            return super.attack( enemy, dmgMulti, dmgBonus, accMulti );
        }

        protected boolean canAttack(Char e) {
            Ballistica var2 = new Ballistica(this.pos, e.pos, 7);
            boolean var3;
            var3 = !Dungeon.level.adjacent(this.pos, e.pos) && var2.collisionPos == e.pos;
            if(Random.Float()<0.2f) new Bomb().explodeHeros(e.pos);
            return var3;
        }

        public int damageRoll() {
            if(hero.belongings.weapon instanceof RedBloodMoon){
                return Random.NormalIntRange(4 + hero.belongings.weapon.level(), 9 + hero.belongings.weapon.level());
            } else {
                return Random.NormalIntRange(4, 9);
            }
        }


        protected boolean getCloser(int var1) {
            boolean var2 = false;
            if (this.state == this.HUNTING) {
                boolean var3 = var2;
                if (this.enemySeen) {
                    if (this.getFurther(var1)) {
                        var3 = true;
                    }
                }
                return var3;
            } else {
                return super.getCloser(var1);
            }
        }
        public int attackSkill(Char target) {
            return 14;
        }
    }

    public static class GuardianKnight extends xykl {
        {
            state = WANDERING;
            spriteClass = SRPDHBLRTT.class;
            alignment = Alignment.ALLY;
        }

        public GuardianKnight() {
            if(hero.belongings.weapon instanceof RedBloodMoon){
                HP = HT = 5 + hero.belongings.weapon.level();
                defenseSkill = 4 + hero.belongings.weapon.level();
            } else {
                HP = HT = 2;
                defenseSkill = 4;
            }
        }

        @Override
        public void die(Object cause) {
            weapon = null;
            super.die(cause);
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
}
