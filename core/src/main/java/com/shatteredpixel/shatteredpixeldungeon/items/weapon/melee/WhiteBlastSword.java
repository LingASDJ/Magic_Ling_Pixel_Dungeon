package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.actors.Char.INFINITE_ACCURACY;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BuffsOringinForWeapon.FloatingParalysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BuffsOringinForWeapon.FloatingTargetDefenseIgnore;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Levitation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.WhiteBlastSwordStatus;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

//白浪刺剑
//5阶武器
//一位审判官曾使用的武器，仅仅是握着它你就使你充满了坚定的决心。
//3-25，+2-6，1延迟

//空灵：被动技能，装备白浪刺剑时，所有敌人在漂浮时会被持续施加麻痹（麻痹消失时立刻再次施加，永控），攻击命中时使敌人漂浮1回合，冷却7回合。

//弑羽：被动技能，对处于空中的敌人造成物理伤害时无视其50%防御力。攻击未命中或被格挡后立刻追加一次额外的攻击（不消耗回合），无法连续触发。

//判决：主动技能，使周围所有不处于空中的敌人漂浮2回合，随后对周围所有敌人分别造成3次攻击伤害。
//  这个技能需要30点充能来开启，每次攻击（无需命中）获得1点，处于空中的敌人死亡时获得4点。

//武技：风袭，2消耗，使目标浮空1回合，随后对其造成2次必中的100%伤害。
public class WhiteBlastSword extends MeleeWeapon {
    {
        image = ItemSpriteSheet.WHITE_BAST;
        tier = 5;
    }

    @Override
    public int STRReq(int lvl) {
        int req = (7 + tier * 2) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
        if (masteryPotionBonus){
            req -= 2;
        }
        return req;
    }

    @Override
    public int min(int lvl) {
        return 3 + lvl * 2;
    }
    @Override
    public int max(int lvl) {
        return 25 + lvl * 6;
    }

    @Override
    public void activate(Char ch) {
        super.activate(ch);
        Buff.affect(ch, FloatingParalysis.class);   // 装备上自动挂buff 浮空麻痹
        Buff.affect(ch, FloatingTargetDefenseIgnore.class);  // 装备上自动挂buff 浮空目标防御忽略 默认忽略0.5倍
    }

    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        boolean result = super.doUnequip(hero, collect, single);
        if (result) {
            Buff.detach(hero, FloatingParalysis.class);  // 卸掉自动删buff
            Buff.detach(hero, FloatingTargetDefenseIgnore.class);
            Buff.detach(hero, WhiteBlastSwordStatus.class);
        }
        return result;
    }

    // 每次攻击使技能充能自增，并检测当技能充能大于等于30时，玩家可以释放技能
    public int SkillCharge = 0;

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        // 空灵：命中使目标浮空，7回合冷却
        if (attacker.buff(LevitationCooldown.class) == null) {
            Buff.affect(defender, Levitation.class, 1f);
            Buff.affect(attacker, LevitationCooldown.class, 7f);
        }
        return super.proc(attacker, defender, damage);
    }

    // 空灵效果的计时器buff
    public static class LevitationCooldown extends FlavourBuff {
    }

    // 弑羽效果的不可连续触发标记
    private boolean pending    = false;   // 已排队，等待 Actor 执行
    private boolean inFollowup = false;   // 追加上击正在结算中（防嵌套连锁）

    @Override
    public void onAttackMissed(Char attacker, Char defender) {
        SkillCharge++;                  // 判决的无需命中充能
        checkReady(attacker);

        if (pending || inFollowup) return;      // 无法连续触发
        pending = true;

        Actor.addDelayed(new Actor() {
            { actPriority = HERO_PRIO + 1; }
            @Override protected boolean act() {
                Actor.remove(this);
                pending = false;                // 一出队就解锁排队标记

                if (!defender.isAlive() || !attacker.isAlive()) return true;

                Callback finish = new Callback() {
                    @Override public void call() {
                        inFollowup = true;
                        if (defender.isAlive() && attacker.isAlive()) attacker.attack(defender);
                        inFollowup = false;     // ← 结算完才解锁，防止追加上击再触发追加上击
                    }
                };

                if (attacker.sprite != null) attacker.sprite.attack(defender.pos, finish);
                else finish.call();             // 兜底：没有 sprite 就直接结算，别丢这次攻击

                return true;
            }
        }, 0);
    }

    // 判决的充能效果
    @Override public void onAttackHit(Char attacker, Char defender, int damageDealt) {
        SkillCharge++;
        if (!defender.isAlive() && defender.buff(Levitation.class) != null) SkillCharge += 4;
        checkReady(attacker);
    }
    // 判决的就绪效果
    private void checkReady(Char attacker) {
        if (SkillCharge >= 30 && attacker == Dungeon.hero) {
            Buff.affect(attacker, WhiteBlastSwordStatus.class);
        }
    }
    // 判决效果
    public void judgement(Hero hero) {
        // 1. 收集周围敌人（自身格 + 8邻格）（3*3）
        ArrayList<Char> targets = new ArrayList<>();
        Char onSelf = Actor.findChar(hero.pos);
        if (onSelf != null && onSelf != hero && onSelf.alignment != hero.alignment) targets.add(onSelf);
        for (int n : PathFinder.NEIGHBOURS8) {
            Char ch = Actor.findChar(hero.pos + n);
            if (ch != null && ch != hero && ch.alignment != hero.alignment) targets.add(ch);
        }

        // 2. 不浮空的 → 浮空2回合
        for (Char ch : targets) {
            if (ch.buff(Levitation.class) == null) Buff.affect(ch, Levitation.class, 2f);
        }

        // 3. 对每个敌人造成3次攻击伤害
        for (Char ch : targets) {
            for (int i = 0; i < 3 && ch.isAlive(); i++) {
                hero.attack(ch, 1f, 0f, INFINITE_ACCURACY);
            }
        }
        GLog.newLine();
        GLog.n("%s: \"%s\" ", Messages.titleCase(Dungeon.hero.name()), TXT_RANDOM[Random.Int(TXT_RANDOM.length)]);
    }

    private static final String SKILLCHARGE    = "skillcharge";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( SKILLCHARGE, SkillCharge );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        SkillCharge = bundle.getInt( SKILLCHARGE );
    }

    public static String[] TXT_RANDOM = {
            Messages.get(WhiteBlastSword.class,"roll1"),
            Messages.get(WhiteBlastSword.class,"roll2"),
            Messages.get(WhiteBlastSword.class,"roll3"),
    };

    //  ========== 武技：风袭 ==========

    @Override
    protected int baseChargeUse(Hero hero, Char target) {
        return 2;
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        if (target == null) return;

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
            public void call() {}
        });

        // 1. 先扣充能：beforeAbilityUsed 会按 baseChargeUse 的返回值扣掉对应充能
        beforeAbilityUsed(hero, null);
        AttackIndicator.target(enemy);

        // 2.使得目标漂浮并执行一次普通攻击
        Buff.affect(enemy, Levitation.class, 1f);
        boolean hit = hero.attack(enemy, 1f, 0f, INFINITE_ACCURACY);

        if (hit && !enemy.isAlive()) {
            onAbilityKill(hero, enemy);
        }

        // 如果还活着就进行第二次普通攻击
        if (enemy.isAlive()) {
            hit = hero.attack(enemy, 1f, 0f, INFINITE_ACCURACY);
            if (hit && !enemy.isAlive()) {
                onAbilityKill(hero, enemy);
            }
        }

        Invisibility.dispel();
        hero.spendAndNext(hero.attackDelay());

        // 4. 武技收尾：处理与武技相关的天赋联动
        afterAbilityUsed(hero);
    }

    @Override
    public String devDesc() {
        if(Dungeon.hero != null){
            return Messages.get(this, "dev_desc",Dungeon.hero.buff(LevitationCooldown.class) != null?Dungeon.hero.buff(LevitationCooldown.class).cooldown():Messages.get(this,"co"),SkillCharge);
        } else {
            return Messages.get(this, "dev_desc",Messages.get(this,"co"),SkillCharge);
        }

    }
}
