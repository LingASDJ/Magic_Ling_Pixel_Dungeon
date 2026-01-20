package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Daze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DwarfSolider;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfAnmy;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class KingAxe extends MeleeWeapon {

    private int discount;

    {
        image = ItemSpriteSheet.KINGAXE;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 0.9f;

        tier = 5;
        ACC = 1.67f; //24% boost to accuracy
    }

    @Override
    public int min(int lvl) {
        return 4 + lvl * 2;
    }

    @Override
    public int max(int lvl) {
        return 32 + lvl * 6;
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected int baseChargeUse(Hero hero, Char target){
        if (target == null || (target instanceof Mob && ((Mob) target).surprisedBy(hero))) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        KingAxe.heavyBlowAbility(hero, target, 1.65f, this);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage ) {
        int dmg = damageRoll(attacker);
        discount++;
        if(discount>5){
            defender.damage(dmg/4,new DM100.LightningBolt());
            damage = dmg;
            discount = 0;
            Buff.prolong(attacker, ArmorNoFear.class, 5f);
        }
        return super.proc(attacker, defender, damage);
    }

    public String desc() {
        return Messages.get(this, "desc")+"_"+discount+"_";
    }

    public static void heavyBlowAbility(Hero hero, Integer target, float dmgMulti, MeleeWeapon wep){
        if (target == null) {
            return;
        }

        Char enemy = Actor.findChar(target);
        if (enemy == null || enemy == hero || hero.isCharmedBy(enemy) || !Dungeon.level.heroFOV[target]) {
            GLog.w(Messages.get(wep, "ability_no_target"));
            return;
        }

        hero.belongings.abilityWeapon = wep;
        if (!hero.canAttack(enemy)){
            GLog.w(Messages.get(wep, "ability_bad_position"));
            hero.belongings.abilityWeapon = null;
            return;
        }
        hero.belongings.abilityWeapon = null;

        //need to separately check charges here, as non-surprise attacks cost 2
        if (enemy instanceof Mob && !((Mob) enemy).surprisedBy(hero)){
            Charger charger = Buff.affect(hero, Charger.class);
            if (Dungeon.hero.belongings.weapon == wep) {
                if (charger.charges + charger.partialCharge < wep.abilityChargeUse(hero, enemy)){
                    GLog.w(Messages.get(wep, "ability_no_charge"));
                    return;
                }
            } else {
                if (charger.secondCharges + charger.secondPartialCharge < wep.abilityChargeUse(hero, enemy)){
                    GLog.w(Messages.get(wep, "ability_no_charge"));
                    return;
                }
            }
        }

        hero.sprite.attack(enemy.pos, new Callback() {
            @Override
            public void call() {
                wep.beforeAbilityUsed(hero, enemy);
                AttackIndicator.target(enemy);
                if (hero.attack(enemy, dmgMulti, 0, Char.INFINITE_ACCURACY)) {
                    Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
                    if (enemy.isAlive()){
                        Buff.affect(enemy, Daze.class, Daze.DURATION);
                    } else {
                        wep.onAbilityKill(hero, enemy);
                    }
                }
                Invisibility.dispel();
                hero.spendAndNext(hero.attackDelay());
                wep.afterAbilityUsed(hero);

                DwarfSolider solider = new DwarfSolider();
                solider.pos = target;
                solider.HT = solider.HP = 20;
                Buff.affect(solider, WandOfAnmy.AllyToRestartOK.class);
                GameScene.add(solider);
            }
        });
    }

    public static class ArmorNoFear extends FlavourBuff {

        {
            type = buffType.POSITIVE;
            announced = true;
        }

        public static final float DURATION	= 5f;
        @Override
        public int icon() {
            return BuffIndicator.ARMOR;
        }
        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.SHPX_COLOR);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }

    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        discount = bundle.getInt("lvl");
    }

    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("lvl", discount);
    }

}
