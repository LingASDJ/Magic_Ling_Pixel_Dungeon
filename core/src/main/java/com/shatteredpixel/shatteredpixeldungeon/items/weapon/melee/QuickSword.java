package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

//迅捷剑
//三阶，力量需求14
//初始4-20，成长1-4
//你每拥有1%的移速提升，这把武器的伤害就上升1%。（不会超过（武器等级+1）*50%）
//慢慢来？切，开什么玩笑，天下武功当然是唯快不破！
//武技：唯快不破，消耗1充能，进行1次不消耗回合的攻击。
public class QuickSword extends MeleeWeapon {
    {
        image = ItemSpriteSheet.HASTE_SWORD;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;
        tier = 3;

    }
    //最大伤害
    @Override
    public int max(int lvl) { return 20 + lvl * 4; }

    //最小伤害
    @Override
    public int min(int lvl) { return 4 + lvl; }

    //伤害骰子
    @Override
    public int damageRoll(Char owner){
        int damage = super.damageRoll(owner);
        //每1%移速提升→1%伤害提升，最多提升(武器等级+1)*50%
        float maxMultiplier = 1f + (buffedLvl() + 1) * 0.5f;
        float speedMultiplier = Math.max(1f, owner.speed() / owner.baseSpeed);
        float multiplier = Math.min(speedMultiplier, maxMultiplier);
        return Math.round(damage * multiplier);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc");
    }

    @Override
    public String statsInfo() {
        float maxMultiplier = 1f + (level() + 1) * 0.5f;
        float speedMultiplier = Dungeon.hero != null ? Math.max(1f, Dungeon.hero.speed() / Dungeon.hero.baseSpeed) : 1f;
        float multiplier = Math.min(speedMultiplier, maxMultiplier);
        return Messages.get(this, "stats_desc", multiplier, maxMultiplier);
    }

    // ==================== 武技：唯快不破 ====================

    //基础充能消耗为1
    @Override
    protected int baseChargeUse(Hero hero, Char target){
        return 1;
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {if (target == null) return;

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

        // 3. 武技收尾：处理与武技相关的天赋联动
        afterAbilityUsed(hero);
    }
}