package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

//迅捷剑
//三阶，力量需求14
//初始4-20，成长1-3
//你每拥有3%的移速提升，这把武器的伤害就上升1%。（不会超过（武器等级+1）*50%）
//慢慢来？切，开什么玩笑，天下武功当然是唯快不破！
//武技：唯快不破，消耗2充能，进行1次不消耗回合的攻击，不能连续释放。
public class QuickSword extends MeleeWeapon {
    {
        image = ItemSpriteSheet.HASTE_SWORD;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;
        tier = 3;

    }
    //最大伤害
    @Override
    public int max(int lvl) { return 20 + lvl * 3; }

    //最小伤害
    @Override
    public int min(int lvl) { return 4 + lvl; }

    //伤害骰子
    @Override
    public int damageRoll(Char owner){
        int damage = super.damageRoll(owner);
        //每3%移速提升→1%伤害提升，最多提升(武器等级+1)*50%
        float maxMultiplier = 1f + (buffedLvl() + 1) * 0.5f;
        float speedMultiplier = 1+((Math.max(1f, owner.speed() / owner.baseSpeed)-1)/3.0f);
        float multiplier = Math.min(speedMultiplier, maxMultiplier);
        return Math.round(damage * multiplier);
    }

    @Override
    public String statsInfo() {
        float maxMultiplier = 1f + (isIdentified() ? buffedLvl() + 1 : 1) * 0.5f;
        float speedMultiplier = Dungeon.hero != null ? 1+((Math.max(1f, Dungeon.hero.speed() / Dungeon.hero.baseSpeed)-1)/3.0f) : 1f;
        float multiplier = Math.min(speedMultiplier, maxMultiplier);
        return Messages.get(this, "stats_desc", multiplier, maxMultiplier);
    }

    // ==================== 武技：唯快不破 ====================

    //基础充能消耗为2
    @Override
    protected int baseChargeUse(Hero hero, Char target){
        return 2;
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {if (target == null) return;

        // 不能连续释放：上一次释放的标记还在（英雄还没消耗回合做过其它行动）
        if (hero.buff(QuickStrikeTracker.class) != null) {
            GLog.w(Messages.get(this, "ability_chain_limit"));
            return;
        }

        Char enemy = Actor.findChar(target);
        if (enemy == null || enemy == hero || hero.isCharmedBy(enemy)
                || !Dungeon.level.heroFOV[target]) {
            GLog.w(Messages.get(this, "ability_no_target"));
            return;
        }

        // 检查这次攻击是否合法，检查失败不会扣充能
        hero.belongings.abilityWeapon = this;
        if (!hero.canAttack(enemy)) {
            GLog.w(Messages.get(this, "ability_bad_position"));
            hero.belongings.abilityWeapon = null;
            return;
        }
        hero.belongings.abilityWeapon = null;

        hero.sprite.attack(enemy.pos, new Callback() {
                    @Override
                    public void call() {

                    }
                });

        // 1. 先扣充能：beforeAbilityUsed 会按 baseChargeUse 的返回值扣掉对应充能
        beforeAbilityUsed(hero, null);
        AttackIndicator.target(enemy);

        // 2.执行一次普通攻击
        boolean hit = hero.attack(enemy);

        if (hit && !enemy.isAlive()) {
            onAbilityKill(hero, enemy);
        }

        Invisibility.dispel();

        hero.spendAndNext(0f);

        // 3. 打上"已释放"标记：本武技不消耗回合，但必须先用掉一个回合才能再次释放
        Buff.affect(hero, QuickStrikeTracker.class, 0f);

        // 4. 武技收尾：处理与武技相关的天赋联动
        afterAbilityUsed(hero);


    }

    /**
     * 「唯快不破」的连续释放限制标记。
     *
     * 本武技不消耗回合，若不加以限制，就能在同一回合内连续倾泻充能。
     * 做法与武僧的「空振」完全一致（MonkEnergy.FlurryCooldownTracker）：
     * 挂一个 0 回合的 FlavourBuff 作为标记。由于 buff 的结算优先级低于英雄
     * （BUFF_PRIO < HERO_PRIO），只要英雄的时间不前进，标记就不会被结算移除；
     * 一旦英雄进行了任何消耗回合的行动（移动/攻击/等待等），标记便会在此回合
     * 结束、英雄的下一个回合到来之前自动消失，"不能连续释放"由此成立。
     */
    public static class QuickStrikeTracker extends FlavourBuff {}
}
