//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessUnlock;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.BackGoKey;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blazing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.HaloBlazing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Kinetic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Unstable;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class LockSword extends MeleeWeapon {

    public int lvl = 0;

    public LockSword() {
        super.image = ItemSpriteSheet.DG3;
        super.tier = 5;
    }

    @Override
    public int value() {

        int gold;

        switch (Dungeon.depth){
            default:
            case 6:
                gold = 50+((lvl/100)*lvl<100 ? 0 : 100);
                return gold;
            case 11:
                gold = 110+((lvl/100)*lvl<100 ? 0 : 100);
                return gold;
            case 16:
                gold = 142+((lvl/100)*lvl<100 ? 0 : 100);
                return gold;
            case 20:
                gold = 234+((lvl/100)*lvl<100 ? 0 : 100);
                return gold;
        }

    }


    private static final String AC_INTER_TP = "interlevel_tp";

    @Override
    public void execute( Hero hero, String action ) {
        super.execute( hero, action );
        if(action.equals(AC_INTER_TP)){
            if(Dungeon.hero.buff(LockedFloor.class) != null) {
                GLog.w(Messages.get(BackGoKey.class,"cannot_send"));
            } else  {
                ShatteredPixelDungeon.scene().add(new WndOptions(Icons.get(Icons.WARNING),
                        Messages.get(this, "go_interlevel"),
                        Messages.get(this, "go_desc"),
                        Messages.get(this, "okay"),
                        Messages.get(this, "cancel")) {
                    @Override
                    protected void onSelect(int index) {
                        if (index == 0) {
                            InterlevelScene.mode = InterlevelScene.Mode.ANCITYBOSS;
                            Game.switchScene(InterlevelScene.class);
                            lvl -= 300;
                        }
                    }
                });

            }
        }
    }



    @Override
    public int level() {
        if(Dungeon.isDLC(Conducts.Conduct.DEV)){
            return super.level();
        } else {
            if(lvl <=1000){
                return lvl/100;
            } else {
                return 10;
            }
        }
    }

    @Override
    public int min(int lvl) {
        return 10 + lvl;
    }

    @Override
    public int max(int lvl) {
        return  14 + lvl*6;
    }

    public String desc() {
        return Messages.get(this, "desc")+"_"+lvl+"_";
    }

    public int image() {
        if (lvl >= 1000) {
            super.image = ItemSpriteSheet.DG5;
        } else if (lvl >= 750) {
            super.image = ItemSpriteSheet.PBlade;
        } else if (lvl >= 550) {
            super.image = ItemSpriteSheet.DG4;
        }
        return image;
    }

    @Override
    public boolean isUpgradable() {
        return Dungeon.isDLC(Conducts.Conduct.DEV);
    }

    public int proc(Char attacker, Char defender, int damage ) {

        int dmg;
//        if(lock == null) {
//            if (lvl >= 1000) {
//                lvl += 1;
//            } else if (defender.properties().contains(Char.Property.BOSS) && defender.HP <= damage && lvl <= 1000) {
//                //目标Boss血量小于实际伤害判定为死亡,+9
//                lvl += 9;
//            } else if (defender.properties().contains(Char.Property.MINIBOSS) && defender.HP <= damage && lvl <= 1000) {
//                //目标迷你Boss血量小于实际伤害判定为死亡,+7
//                lvl += 7;
//            } else if (defender.HP <= damage && lvl <= 1000) {
//                //目标血量小于实际伤害判定为死亡,+5
//                lvl += 5;
//            }
//        }

        if (lvl>= 900) {
            switch (Random.NormalIntRange(1,4)){
                case 1:
                    dmg = (new Grim()).proc(this, attacker, defender, damage) + 4;
                    damage = dmg;
                    break;
                case 2:
                    dmg = (new Shocking()).proc(this, attacker, defender, damage) + 5;
                    damage = dmg;
                    break;
                case 3:
                    dmg = (new Blazing()).proc(this, attacker, defender, damage) + 6;
                    damage = dmg;
                    break;
                case 4:
                    dmg = (new Kinetic()).proc(this, attacker, defender, damage) + 7;
                    damage = dmg;
                    break;
                default:
                    dmg = (new HaloBlazing()).proc(this, attacker, defender, damage) + 8;
                    damage = dmg;
                    break;
            }
        } else if (lvl>= 800) {
            dmg = (new Unstable()).proc(this, attacker, defender, damage) + 4;
            damage = dmg;
        } else if (lvl>= 600){
            dmg = (new Shocking()).proc(this, attacker, defender, damage) + 3;
            damage = dmg;
        } else if (lvl>= 400){
            dmg = (new Blazing()).proc(this, attacker, defender, damage) + 2;
            damage = dmg;
        } else if (lvl>= 200){
            dmg = (new Kinetic()).proc(this, attacker, defender, damage) + 1;
            damage = dmg;
        }

        return super.proc(attacker, defender, damage);


    }

    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        super.image = ItemSpriteSheet.DG3;

        if (lvl >= 1000) {
            super.image = ItemSpriteSheet.DG5;
        } else if (lvl >= 750) {
            super.image = ItemSpriteSheet.PBlade;
        } else if (lvl >= 550) {
            super.image = ItemSpriteSheet.DG4;
        }

        lvl = bundle.getInt("lvl");
    }

    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        super.image = ItemSpriteSheet.DG3;

        if (lvl >= 1000) {
            super.image = ItemSpriteSheet.DG5;
        } else if (lvl >= 750) {
            super.image = ItemSpriteSheet.PBlade;
        } else if (lvl >= 550) {
            super.image = ItemSpriteSheet.DG4;
        }

        bundle.put("lvl", lvl);
    }

    @Override
    protected int baseChargeUse(Hero hero, Char target){
        return 5;
    }
    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }
    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        if (target == null) {
            return;
        }

        Char enemy = Actor.findChar(target);
        if (enemy == null || enemy == hero || hero.isCharmedBy(enemy) || !Dungeon.level.heroFOV[target]) {
            GLog.w(Messages.get(this, "ability_no_target"));
            return;
        }

        hero.belongings.abilityWeapon = this;
        if (!hero.canAttack(enemy)){
            GLog.w(Messages.get(this, "ability_bad_position"));
            hero.belongings.abilityWeapon = null;
            return;
        }
        hero.belongings.abilityWeapon = null;

        hero.sprite.attack(enemy.pos, new Callback() {
            @Override
            public void call() {
                beforeAbilityUsed(hero, enemy);
                AttackIndicator.target(enemy);
                if (hero.attack(enemy, 1.40f, 0, Char.INFINITE_ACCURACY)){
                    Sample.INSTANCE.play(Assets.Sounds.HIT_SLASH);
                    if (!enemy.isAlive()){
                        onAbilityKill(hero, enemy);
                    }
                }
                new Unstable().proc(LockSword.this,hero, enemy, buffedLvl());

                Buff.affect(hero, BlessUnlock.class).set((41), 1);

                Invisibility.dispel();
                hero.spendAndNext(hero.attackDelay());
                afterAbilityUsed(hero);
            }
        });
    }

}
