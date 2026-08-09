package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class SDBSword extends MeleeWeapon {

    {
        tier = 4;
        image = ItemSpriteSheet.SDBlade;
        DLY = 1.5f;
    }

    // ===================== 武技基础配置 =====================
    @Override
    protected int baseChargeUse(Hero hero, Char target) {
        return 2;
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(ArmorAbility.class, "prompt");
    }

    // ===================== 武技核心逻辑 =====================
    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        if (target == null) return;

        Char enemy = Actor.findChar(target);
        if (enemy == null || enemy == hero || enemy.alignment == Char.Alignment.ALLY
                || !hero.fieldOfView[enemy.pos] || !hero.canAttack(enemy)) {
            GLog.w(Messages.get(this, "ability_no_target"));
            return;
        }

        throwSound();
        hero.sprite.attack(enemy.pos, new Callback() {
            @Override
            public void call() {
                beforeAbilityUsed(hero, enemy);

                float dmgMulti = 1f;
                float accMulti = 1f;
                boolean isCloseTarget = Dungeon.level.distance(hero.pos, enemy.pos) < 2;

                // 计算武器基础伤害，用于追加攻击
                int baseDmg = Math.round((min() + max()) / 2f);

                if (isCloseTarget) {
                    dmgMulti = 1.3f;       // 伤害+30%
                    accMulti = 999f;       // 必定命中
                }

                // 执行武技主攻击
                boolean hit = hero.attack(enemy, dmgMulti, 0, accMulti);

                // 命中且贴身时，执行标准击飞（参考冲击波法杖throwChar逻辑）
                if (hit && isCloseTarget) {
                    // 构造击退轨迹：沿英雄→敌人方向直线延伸
                    int dir = Ballistica.direction(hero.pos, enemy.pos);
                    int farPos = enemy.pos;
                    for (int i = 0; i < 20; i++) {
                        int next = Ballistica.step(farPos, dir);
                        if (!Dungeon.level.insideMap(next)) break;
                        farPos = next;
                    }
                    Ballistica trajectory = new Ballistica(enemy.pos, farPos, Ballistica.MAGIC_BOLT);
                    Invisibility.dispel();
                    afterAbilityUsed(hero);
                    hero.spendAndNext(hero.attackDelay());
                    // 执行击飞，击退强度=3格，撞墙触发追加攻击
                    throwChar(enemy, trajectory, 3, new Callback() {
                        @Override
                        public void call() {
                        }
                    }, hero, baseDmg * dmgMulti);

                } else {
                    // 无击飞，直接收尾
                    Invisibility.dispel();
                    afterAbilityUsed(hero);
                    hero.spendAndNext(hero.attackDelay());
                }
            }
        });
    }

    // ===================== 标准击飞实现（参考WandOfBlastWave.throwChar） =====================
    private void throwChar(final Char ch, final Ballistica trajectory, int power,
                           final Callback finishCallback, final Hero hero, final float followDmgBase) {

        int dist = Math.min(trajectory.dist, power);
        boolean collided = dist == trajectory.dist;

        // 定身、无法移动单位 免疫击退
        if (dist <= 0 || ch.rooted || ch.properties().contains(Char.Property.IMMOVABLE)) {
            finishCallback.call();
            return;
        }

        // 大型单位无法进入非开阔地形
        if (Char.hasProp(ch, Char.Property.LARGE)) {
            for (int i = 1; i <= dist; i++) {
                if (!Dungeon.level.openSpace[trajectory.path.get(i)]) {
                    dist = i - 1;
                    collided = true;
                    break;
                }
            }
        }

        // 目标格有其他单位时，后退一格并标记碰撞
        if (Actor.findChar(trajectory.path.get(dist)) != null) {
            dist--;
            collided = true;
        }

        if (dist < 0) {
            finishCallback.call();
            return;
        }

        final int newPos = trajectory.path.get(dist);
        if (newPos == ch.pos) {
            finishCallback.call();
            return;
        }

        final boolean finalCollided = collided;
        final int initialPos = ch.pos;

        // 官方标准推动动画
        Actor.add(new Pushing(ch, ch.pos, newPos, new Callback() {
            public void call() {
                // 安全校验：位置被改变/目标格有单位则取消
                if (initialPos != ch.pos || Actor.findChar(newPos) != null) {
                    ch.sprite.place(ch.pos);
                    finishCallback.call();
                    return;
                }

                ch.pos = newPos;
                Dungeon.level.occupyCell(ch);

                if (finalCollided && ch.isActive()) {
                    GLog.n(Messages.get(SDBSword.this, "knock_wall_hit",ch.name()));

                    // 追加魔法伤害 + 星光特效
                    int followDmg = Math.round(followDmgBase);
                    ch.damage(followDmg, hero, Char.DamageType.MAGIC);
                    ch.sprite.emitter().burst(Speck.factory(Speck.STAR), 6);

                    // 附加流血效果
                    int bleedTurns = followDmg / 3;
                    if (bleedTurns > 0) {
                        Buff.affect(ch, Bleeding.class).set(bleedTurns);
                    }
                    hero.spend(1f);
                }

                if (ch == Dungeon.hero) {
                    Dungeon.observe();
                }

                finishCallback.call();
            }
        }));
    }

    // ===================== 武技描述 =====================
    @Override
    public String abilityInfo() {
        if (levelKnown) {
            return Messages.get(this, "typical_ability_desc");
        } else {
            return Messages.get(this, "ability_desc");
        }
    }

    // ===================== 原有武器被动完全保留 =====================
    @Override
    public int proc(Char attacker, Char defender, int damage) {
        if (Dungeon.level.distance(defender.pos, Dungeon.hero.pos) <= 1) {
            int poisonTurns = damage / 3;
            if (poisonTurns > 0) {
                Buff.affect(defender, Poison.class).set(poisonTurns);
            }
        } else if (Dungeon.level.distance(defender.pos, Dungeon.hero.pos) >= 2) {
            defender.damage(damage, this, Char.DamageType.MAGIC);
            defender.sprite.emitter().burst(Speck.factory(Speck.STAR), 6);

            int bleedTurns = damage / 3;
            if (bleedTurns > 0) {
                Buff.affect(defender, Bleeding.class).set(bleedTurns);
            }
            return 0;
        }
        return super.proc(attacker, defender, damage);
    }

    @Override
    public int image() {
        if (level() >= 6) {
            image = ItemSpriteSheet.HHBlade;
        } else {
            image = ItemSpriteSheet.SDBlade;
        }
        return super.image();
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        image();
    }

    @Override
    public int min() {
        return 5 + buffedLvl();
    }

    @Override
    public int max() {
        return 25 + buffedLvl() * 6;
    }
}