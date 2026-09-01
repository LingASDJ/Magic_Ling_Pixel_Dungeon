package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

import java.util.ArrayList;
import java.util.HashSet;

//德诺尔镰刀
//四阶，力量需求17
//初始4-25，成长1-5
//如果在你的攻击范围内有三名或以上敌人，则这把武器可以同时攻击到它们，并且攻速上升66%。
//武技：舞刃
//增加1格攻击范围，持续10+等级*2回合。消耗1点充能。这个武技不消耗回合。
//产于善于舞蹈的德诺尔，镰刀柄上还贴心蚀刻上了挥舞说明。
public class DenorScythe extends MeleeWeapon{
    {
        image = ItemSpriteSheet.SKIN_5;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        tier = 4;
    }

    public static int extraReach = 1; // 在武器原有射程上额外加几格


    @Override
    public int max(int lvl) { return 25 + lvl * 5; }

    @Override
    public int min(int lvl) { return 4 + lvl; }

    // 基础力量需求加一
    @Override
    public int STRReq(int lvl){
        int req = STRReq(tier, lvl)+1;
        if (masteryPotionBonus){
            req -= 2;
        }
        return req;
    }

    // 每次使用武技消耗的充能点数（由决斗者的 Charger buff 提供）
    @Override
    protected int baseChargeUse(Hero hero, Char target){
        return 1;
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        // 武技每次增加的Buff回合数
        float duration = 10f + buffedLvl()*2;

        // 1. 先扣充能：beforeAbilityUsed 会按 baseChargeUse 的返回值扣掉对应充能
        beforeAbilityUsed(hero, null);

        // 2. 为自身施加buff
        Buff.affect(hero, ExtendedReach.class, duration);

        // 3. 播放使用动作。注意：这里不调用 hero.next()，
        //    因此本武技不消耗回合，放完还能继续移动/攻击
        hero.sprite.operate(hero.pos);

        // 4. 武技收尾：处理与武技相关的天赋联动
        afterAbilityUsed(hero);
    }

    // ==================== 被动：群体挥砍 ====================
    // 当自身周围 2 格内有三名或以上敌人时：
    //   1) 本次攻击会同时命中范围内所有敌人（副目标直接结算伤害）
    //   2) 攻击速度上升 66%（见 speedMultiplier）

    // 本次攻击已处理过的目标id：防止AOE递归/连锁（不写入存档）
    private HashSet<Integer> aoedTargets = new HashSet<>();

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        // 若该目标已在本次AOE中处理过（来自副目标的递归调用），直接跳过，
        // 否则副目标的 attack() 会再次进入本 proc 形成无限递归
        if (aoedTargets.contains(defender.id())) {
            return super.proc(attacker, defender, damage);
        }

        ArrayList<Char> enemies = enemiesInReach(attacker);
        if (enemies.size() >= 3) {
            // 主目标标记为已处理
            aoedTargets.add(defender.id());
            try {
                for (Char enemy : enemies) {
                    // 副目标走完整攻击流程（必定命中），并同样标记，避免连锁扩散
                    if (!aoedTargets.contains(enemy.id()) && enemy.isAlive()) {
                        aoedTargets.add(enemy.id());
                        attacker.attack(enemy, 1f, 0f, Char.INFINITE_ACCURACY);
                    }
                }
            } finally {
                // 无论本次AOE是否中途异常，都要清空标记，避免污染下一次攻击
                aoedTargets.clear();
            }
        }
        return super.proc(attacker, defender, damage);
    }

    // 攻速加成：speedMultiplier 越大，Weapon.delayFactor 越小，攻击越快。
    // 攻击范围内 >=3 名敌人时攻速 +66%（攻击间隔约为原来的 1/1.66 ≈ 0.6 倍）
    @Override
    protected float speedMultiplier(Char owner) {
        float multi = super.speedMultiplier(owner);
        if (enemiesInReach(owner).size() >= 3) {
            multi += 0.66f;
        }
        return multi;
    }

    // 收集被动AOE范围内的敌对角色
    // 横扫半径 = 武器当前攻击距离（reachFactor）
    // canAttack 内部还做寻路，会把被其他单位挡住/绕路超程的敌人漏掉，选目标不完整。
    private ArrayList<Char> enemiesInReach(Char attacker) {
        int radius = reachFactor(attacker);

        ArrayList<Char> list = new ArrayList<>();
        for (Char ch : Actor.chars()) {
            if (ch.alignment == Char.Alignment.ENEMY
                    && ch != attacker
                    && !attacker.isCharmedBy(ch)
                    && Dungeon.level.heroFOV[ch.pos]
                    && Dungeon.level.distance(attacker.pos, ch.pos) <= radius) {
                list.add(ch);
            }
        }
        return list;
    }

    public static class ExtendedReach extends FlavourBuff {
        {
            type = Buff.buffType.POSITIVE;
        }

        @Override
        public int icon() {
            return BuffIndicator.COMBO;
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", dispTurns(), extraReach);
        }

    }

}
