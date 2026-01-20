package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bee;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Crab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Scorpio;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Spinner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Swarm;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Lucky;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class GoldLongGun extends MeleeWeapon {

    {
        image = ItemSpriteSheet.GOLDLANGGUN;
        tier = 4;
        RCH = 2;
    }

    @Override
    public int damageRoll(Char owner) {
        if (owner instanceof Hero) {
            Hero hero = (Hero)owner;
            Char enemy = hero.enemy();
            if (enemy instanceof Mob) {
                int diff = max() - min();
                int damage = augment.damageFactor(Hero.heroDamageIntRange(
                        min() + Math.round(diff*0.50f),
                        max()));
                int exStr = hero.STR() - STRReq();
                if (exStr > 0) {
                    if((enemy.properties.contains(Char.Property.DEMONIC) || (enemy.properties.contains(Char.Property.UNDEAD)))){
                        damage = (int) (damage * 1.33f);
                    }
                }
                return damage;
            }
        }
        return super.damageRoll(owner);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {

        if (Random.Int(100) < 20+5*level()) {
            damage = (new Lucky()).proc(this, attacker, defender, damage);
        }

        return super.proc(attacker, defender, damage);
    }

    @Override
    public int min(int lvl) {
        return 4 + lvl;
    }
    @Override
    public int max(int lvl) {
        return 25 + lvl * 5;
    }

    @Override
    public int iceCoinValue() {
        if (Badges.isUnlocked(Badges.Badge.NYZ_SHOP)){
            return (int) ((225 + tier*25) * 0.9f);
        }
        return 225 + tier*25;
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
            GLog.w(Messages.get(this, "ability_target_range"));
            hero.belongings.abilityWeapon = null;
            return;
        }
        hero.belongings.abilityWeapon = null;

        hero.sprite.attack(enemy.pos, new Callback() {
            @Override
            public void call() {
                int damageBoost = 0;
                if (Char.hasProp(enemy, Char.Property.INORGANIC)
                        || enemy instanceof Swarm
                        || enemy instanceof Bee
                        || enemy instanceof Crab
                        || enemy instanceof Spinner
                        || enemy instanceof Scorpio) {
                    //+(8+2*lvl) damage, equivalent to +100% damage
                    damageBoost = augment.damageFactor(8 + 2*buffedLvl());
                }
                beforeAbilityUsed(hero, enemy);
                AttackIndicator.target(enemy);

                if (hero.attack(enemy, 1, damageBoost, Char.INFINITE_ACCURACY)) {
                    if (enemy.isAlive()) {
                        Buff.affect(enemy, Vulnerable.class, 2f);
                    } else {
                        onAbilityKill(hero, enemy);
                    }
                    if(enemy.HP <= damageBoost){
                        int value = 5+Dungeon.depth/5*10;
                        enemy.sprite.showStatus(CharSprite.POSITIVE, Messages.get(GoldLongGun.class, "gold", value));
                        Dungeon.level.drop(new Gold(value), enemy.pos);
                    }
                    Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
                }
                Invisibility.dispel();
                hero.spendAndNext(hero.attackDelay());
                afterAbilityUsed(hero);
            }
        });
    }

    @Override
    public String abilityInfo() {
        int dmgBoost = 8 + 2*buffedLvl();
        return Messages.get(this, "ability_desc", augment.damageFactor(min()+dmgBoost), augment.damageFactor(max()+dmgBoost));
    }


    public String upgradeAbilityStat(int level){
        int dmgBoost = 8 + 2*level;
        return augment.damageFactor(min(level)+dmgBoost) + "-" + augment.damageFactor(max(level)+dmgBoost);
    }

}
