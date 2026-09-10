package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class MerchantSword extends MeleeWeapon{
    {
        image = ItemSpriteSheet.SHOP_SWORD;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        tier = 2;
    }
    @Override
    public int max(int lvl) {
        return  15 + lvl*3;
    }
    @Override
    public int min(int lvl) {
        return  2 + lvl;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc" , 10 + (buffedLvl() * 5));
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
                beforeAbilityUsed(hero, enemy);
                AttackIndicator.target(enemy);

                // 1. 金币带来百分比加成
                float goldPercent = 100f + 0.1f * Dungeon.gold;
                float totalPercent = Math.min(200f, goldPercent);

                // 2. 武器本身augment加成也要合并进倍率，实现整体上限200%
                float baseWeaponDmg = 15 + 3f * buffedLvl();
                float augmentedBase = augment.damageFactor((int) baseWeaponDmg);
                float augmentMulti = augmentedBase / baseWeaponDmg;

                // 合并所有倍率，封顶200%
                float finalMultiRaw = totalPercent * augmentMulti / 100f;
                float finalMulti = Math.min(2.0f, finalMultiRaw);

                // attack第二个参数是倍数，第三个extraDamage=0，不再单独追加固定伤害
                if (hero.attack(enemy, finalMulti, 0, Char.INFINITE_ACCURACY)) {
                    if (!(enemy instanceof Wraith)) {
                        onAbilityKill(hero, enemy);
                        int explorer = Dungeon.depth / 5;
                        int value = Random.IntRange(explorer < 1 ? 20 : explorer * 20, explorer < 1 ? 35 : explorer  * 35);
                        if(!enemy.isAlive()){
                            Dungeon.level.drop(new Gold(value), enemy.pos).sprite.drop();
                        }

                    }
                    Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
                }
                Invisibility.dispel();
                hero.spendAndNext(hero.attackDelay());
                afterAbilityUsed(hero);
            }
        });
    }

    // 武技描述相关
    @Override
    public String abilityInfo() {
        if (levelKnown){
            return Messages.get(this, "ability_desc", (int)Math.min(200, 100 + 0.1 * Dungeon.gold));
        } else {
            return Messages.get(this, "typical_ability_desc");
        }
    }
    // 武技随升级变动预览
    @Override
    public String upgradeAbilityStat(int level) {
        return Integer.toString(3+level);
    }

}
