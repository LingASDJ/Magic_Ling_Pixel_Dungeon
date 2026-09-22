package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd;

import static com.shatteredpixel.shatteredpixeldungeon.actors.Char.INFINITE_ACCURACY;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra.KusumiMagicGirl;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

//断生者
//四阶，力量需求17
//初始6-24，成长2-6
//每次命中都会让敌人一分为二：本体与分身各继承当前生命值与生命上限的一半。
//武技：腰斩，消耗2充能，对目标造成160%必中伤害，对与目标相连的所有敌人造成120%必中伤害，如果腰斩击杀了1个单位，获得1充能且本次攻击不消耗回合。每次释放至多获得1次充能。

public class LifeCutter extends MeleeWeapon {
    {
        image = ItemSpriteSheet.NO_LIVE;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        tier = 4;
    }

    @Override
    public int STRReq(int lvl){
        int req = STRReq(tier, lvl)+1;
        if (masteryPotionBonus){
            req -= 2;
        }
        return req;
    }

    @Override
    public int max(int lvl) { return 24 + lvl * 6; }

    @Override
    public int min(int lvl) { return 6 + lvl * 2; }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        // 命中即标记目标：真正的分裂在伤害结算（Char.damage）后进行
        if (defender instanceof Mob
                && defender.alignment == Char.Alignment.ENEMY
                && !Char.hasProp(defender, Char.Property.BOSS)
                && !Char.hasProp(defender, Char.Property.MINIBOSS)){
            Buff.affect(defender, SplitMark.class);
        }
        return super.proc(attacker, defender, damage);
    }

    // 伤害结算（Char.damage）后调用：目标仍存活才分裂，本体与分身各继承一半血量和血量上限
    public static void trySplit(Char defender, Object src){
        if (!defender.isAlive()
                || !(defender instanceof Mob)
                || defender.alignment != Char.Alignment.ENEMY
                || Char.hasProp(defender, Char.Property.BOSS)
                || Char.hasProp(defender, Char.Property.MINIBOSS)
                || defender.HP < 2){
            return;
        }

        int cell = freeCellNear(defender);
        if (cell == -1){
            return;
        }

        try {
            int halfHP = defender.HP / 2;
            int halfHT = defender.HT / 2;
            defender.HP = halfHP;
            defender.HT = halfHT;

            Mob copy = Reflection.newInstance(((Mob) defender).getClass());
            copy.HP = halfHP;
            copy.HT = halfHT;
            copy.isEndLess = ((Mob) defender).isEndLess;

            GameScene.add(copy);
            ScrollOfTeleportation.appear(copy, cell);
            if (src instanceof Char){
                copy.aggro((Char) src);
            }
        } catch (Exception ignored) {
            // 遇到无法复制的特殊敌人时，静默跳过分裂
        }
    }

    // 在敌人周围找一个可站立的空格，找不到返回 -1
    private static int freeCellNear(Char ch){
        ArrayList<Integer> candidates = new ArrayList<>();
        for (int n : PathFinder.NEIGHBOURS8) {
            int cell = ch.pos + n;
            if (Dungeon.level.passable[cell]
                    && Actor.findChar(cell) == null
                    && (!Char.hasProp(ch, Char.Property.LARGE) || Dungeon.level.openSpace[cell])) {
                candidates.add(cell);
            }
        }
        return candidates.size() > 0 ? Random.element(candidates) : -1;
    }

    // 一次性分裂标记：挂在目标身上，伤害结算后消费
    public static class SplitMark extends Buff {
    }

    // ========== 武技：腰斩 ==========
    // 每次使用武技消耗的充能点数（由决斗者的 Charger buff 提供）
    @Override
    protected int baseChargeUse(Hero hero, Char target){
        return 2;
    }

    // 选址文本与选址功能启用
    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    // 仿照MerchantSword的5g
    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        // 选址为空，则选取失败
        if (target == null) {
            return;
        }

        // 选址位置的目标若为空、为魅惑者、不在视野范围内、为NPC（以及NPC形态久住）、为无敌单位，则选取失败
        Char enemy = Actor.findChar(target);
        if (enemy == null
                || enemy == hero
                || hero.isCharmedBy(enemy)
                || !Dungeon.level.heroFOV[target]
                || (enemy instanceof NPC || enemy instanceof KusumiMagicGirl)
                || enemy.isInvulnerable(getClass())
        ) {
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

        // 对选择的目标进行攻击
        hero.sprite.attack(enemy.pos, new Callback() {
            @Override public void call() {
                // 扣2充能，并设置 abilityWeapon
                beforeAbilityUsed(hero, enemy);
                AttackIndicator.target(enemy);

                // 创建一个列表用于存储主目标周围一圈共八格的目标
                ArrayList<Char> linked = new ArrayList<>();

                // 遍历主目标周围八格索敌
                for (int n : PathFinder.NEIGHBOURS8) {
                    Char ch = Actor.findChar(enemy.pos + n);
                    // 向列表中添加遍历到的非空、非英雄、存活着的、非中立、非我方单位、非魅惑者
                    if (ch != null
                            && ch != hero
                            && ch.isAlive()
                            && ch.alignment != Char.Alignment.NEUTRAL
                            && ch.alignment != hero.alignment
                            && !hero.isCharmedBy(ch)) linked.add(ch);
                }

                // 击杀标记，表示本次攻击是否成功完成击杀，用于后续回复充能
                boolean killed = false;

                // 攻击与攻击倍率应用，先进行攻击，再判断单位是否已死亡，攻击成功且单位死亡时进入分支体
                if (hero.attack(enemy, 1.6f, 0f, INFINITE_ACCURACY) && !enemy.isAlive()) {
                    // 击杀置真
                    killed = true;
                    // 武技击杀的天赋联动效果
                    onAbilityKill(hero, enemy);
                }
                // 增强for，每次循环从linked中按顺序（0，1，2，……）取出元素存入ch以参与运算
                for (Char ch : linked) {
                    if (ch.isAlive() && hero.attack(ch, 1.2f, 0f, INFINITE_ACCURACY) && !ch.isAlive()) {
                        killed = true;
                        onAbilityKill(hero, ch);
                    }
                }

                // 令攻击者（使用武技者）破隐
                Invisibility.dispel();

                // 成功击杀则不做消耗回合的行动，并且回复1点充能
                if (killed) {
                    refundCharge(hero,1);
                    hero.next();
                } else {
                    // 击杀失败则按攻击间隔消耗回合
                    hero.spendAndNext(hero.attackDelay());
                }

                // 武技后处理
                afterAbilityUsed(hero);
            }
        });
    }

    // 武技描述相关
    @Override
    public String abilityInfo() {
        if (levelKnown){
            return Messages.get(this, "ability_desc");
        } else {
            return Messages.get(this, "typical_ability_desc");
        }
    }
}