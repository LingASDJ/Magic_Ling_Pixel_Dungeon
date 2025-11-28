package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class TragicCode extends MeleeWeapon {

    {
        image = ItemSpriteSheet.SAD_MAGIC_BOOK;
        tier = 3;
    }

    @Override
    public int min(int lvl) {
        return 2 + lvl * 2;
    }

    @Override
    public int max(int lvl) {
        return 20 + lvl * 2;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        int dmg = damage;
        if(attacker instanceof Hero){
            for (ChampionEnemy buff : defender.buffs(ChampionEnemy.class)){
                dmg = (int) Math.ceil(dmg * buff.damageTakenFactor());
            }
            if (defender.HP <= dmg) {
                float identifyChance = 0.10f + (0.05f * level());

                if (Random.Float() < identifyChance) {
                    ArrayList<Item> unidentified = new ArrayList<>();
                    for (Item item : ((Hero) attacker).belongings) {
                        if (!item.isIdentified()) {
                            unidentified.add(item);
                        }
                    }

                    if (!unidentified.isEmpty()) {
                        Item toIdentify = Random.element(unidentified);
                        if (toIdentify != null) {
                            toIdentify.identify();
                            GLog.pink(Messages.get(this, "identify", toIdentify.name()));
                            attacker.sprite.emitter().burst(Speck.factory(Speck.QUESTION), 6);
                            Sample.INSTANCE.play(Assets.Sounds.READ);
                        }
                    }
                }
            }
        }
        return super.proc(attacker, defender, damage);
    }

    public String statsInfo(){
        float identifyChance = 10 + (5 * level());
        if (isIdentified()){
            return Messages.get(this, "stats_desc", identifyChance);
        } else {
            return Messages.get(this, "typical_stats_desc", 10);
        }
    }

    @Override
    protected int baseChargeUse(Hero hero, Char target){
        if (hero.buff(CleaveTracker.class) != null){
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        //+(5+lvl) damage, roughly +45% base dmg, +40% scaling
        int dmgBoost = augment.damageFactor(5 + buffedLvl());
        cleaveAbility(hero, target, 1, dmgBoost, this);
    }

    @Override
    public String abilityInfo() {
        int dmgBoost = levelKnown ? 5 + buffedLvl() : 5;
        if (levelKnown){
            return Messages.get(this, "ability_desc", augment.damageFactor(min()+dmgBoost), augment.damageFactor(max()+dmgBoost));
        } else {
            return Messages.get(this, "typical_ability_desc", min(0)+dmgBoost, max(0)+dmgBoost);
        }
    }

    public String upgradeAbilityStat(int level){
        int dmgBoost = 5 + level;
        return augment.damageFactor(min(level)+dmgBoost) + "-" + augment.damageFactor(max(level)+dmgBoost);
    }

    public static void cleaveAbility(Hero hero, Integer target, float dmgMulti, int dmgBoost, MeleeWeapon wep){
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
            GLog.w(Messages.get(wep, "ability_target_range"));
            hero.belongings.abilityWeapon = null;
            return;
        }
        hero.belongings.abilityWeapon = null;

        hero.sprite.attack(enemy.pos, new Callback() {
            @Override
            public void call() {
                wep.beforeAbilityUsed(hero, enemy);
                AttackIndicator.target(enemy);
                if (hero.attack(enemy, dmgMulti, dmgBoost, Char.INFINITE_ACCURACY, Char.DamageType.PHYSICAL)){
                    Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
                }

                Invisibility.dispel();

                if (!enemy.isAlive()){
                    hero.next();
                    wep.onAbilityKill(hero, enemy);
                    if (hero.buff(CleaveTracker.class) != null) {
                        hero.buff(CleaveTracker.class).detach();
                    } else {
                        Buff.prolong(hero, CleaveTracker.class, 4f); //1 less as attack was instant
                    }
                } else {
                    hero.spendAndNext(hero.attackDelay());
                    if (hero.buff(CleaveTracker.class) != null) {
                        hero.buff(CleaveTracker.class).detach();
                    }
                }
                wep.afterAbilityUsed(hero);
            }
        });
    }

    public static class CleaveTracker extends FlavourBuff {

        {
            type = buffType.POSITIVE;
        }

        @Override
        public int icon() {
            return BuffIndicator.DUEL_CLEAVE;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (5 - visualcooldown()) / 5);
        }
    }


}
