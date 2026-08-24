package com.shatteredpixel.shatteredpixeldungeon.items.thanks;

import static com.shatteredpixel.shatteredpixeldungeon.actors.Char.DamageType.PHYSICAL;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra.KusumiMagicGirl;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.utils.*;

import java.util.ArrayList;

public class HuntingCarnival extends Buff {
    private int duration;          // 总持续回合
    private int left;              // 剩余回合数
    private final int[] cooldowns = {4, 7, 10}; // 三位狙击手的射击间隔
    private int[] timers = {0, 0, 0};     // 当前剩余冷却（0表示可射击）
    private int artifactLevel = 0;   // 套组等级，用于计算箭矢附加效果

    // 由附加buff者设置套组等级
    public void setLevel(int level) {
        artifactLevel = Math.max(0, level);
    }

    public void setDuration(int d) {
        duration = d;
        left = d;
        // 初始全部可用
        for (int i = 0; i < 3; i++) timers[i] = 0;
    }

    @Override
    public boolean act() {
        if (!target.isAlive() || left <= 0) {
            detach();
            return true;
        }
        // 进入古堡区域后，狙击手无法支援现世以外的地方
        if (DistressSignalNesting.inCastleArea()) {
            detach();
            return true;
        }

        left--;

        // 本层随机敌人，每个敌人每回合只会被 1 种箭选为目标
        ArrayList<Char> candidates = allEnemiesOnFloor();

        // 优先级：霜冻(0) > 穿甲燃烧(2) > 电磁震荡(1)，三者冷却同时进行并独立计算
        int[] priority = {0, 2, 1};
        for (int i : priority) {
            if (timers[i] > 0) {
                timers[i]--;
                continue;
            }
            if (candidates.isEmpty()) {
                continue;   // 没有可用的未选中目标，保持就绪等待下一回合
            }
            Char enemy = Random.element(candidates);
            performShot(i, enemy);           // 发射箭矢
            timers[i] = cooldowns[i];        // 重置冷却（各自间隔不同）
            candidates.remove(enemy);        // 该敌人本回合不再被其他箭选中
        }

        spend(TICK);
        return true;
    }

    //本层随机索敌（无视视野），过滤NPC、无敌目标
    private ArrayList<Char> allEnemiesOnFloor() {
        ArrayList<Char> enemies = new ArrayList<>();
        for (Char ch : Actor.chars()) {
            if (ch != target
                    && ch.pos >= 0
                    && ch.alignment != target.alignment
                    && ch instanceof Mob
                    && !(ch instanceof NPC || ch instanceof KusumiMagicGirl)
                    && ch.isAlive()
                    && !ch.isInvulnerable(getClass())) {
                enemies.add(ch);
            }
        }
        return enemies;
    }

    //计算护甲对此伤害的减免
    public static int applyArmorReduction(Char target, int rawDamage) {
        int dr = target.drRoll();
        dr = Math.round(dr * AscensionChallenge.statModifier(target));
        return Math.max(rawDamage - dr, 0);
    }

    // 改造：生成追踪箭矢动画，参考SniperSupport
    private void performShot(int typeIdx, Char enemy) {
        int depth = Dungeon.depth;
        Item missileItem;
        Runnable damageLogic;

        PointF screenLeftTop = Game.scene().camera().scroll;  // 狙击点为屏幕左上角
        PointF startPos = new PointF(screenLeftTop.x - 50, screenLeftTop.y - 50);

        switch (typeIdx) {
            default:
            case 0: // 0 霜冻阻滞箭
                missileItem = new SniperSupport.FrostSnipeArrow();
                damageLogic = () -> {
                    int damage = frostDamage(depth);
                    damage = applyArmorReduction(enemy, damage);
                    if (enemy.isAlive()) enemy.damage(damage, this, PHYSICAL);
                    if (enemy.isAlive()) Buff.affect(enemy, Chill.class, 5);
                    int idx = Random.Int(3);
                    GLog.p(Messages.get(this, "frost_hit_" + idx));
                };
                break;
            case 1: // 1 电磁震荡箭
                missileItem = new SniperSupport.ShockSnipeArrow();
                damageLogic = () -> {
                    int enemypos = enemy.pos;
                    int damage = shockDamage(depth);
                    damage = applyArmorReduction(enemy, damage);
                    if (enemy.isAlive()) enemy.damage(damage, this, PHYSICAL);
                    if (enemy.isAlive()) Buff.affect(enemy, Paralysis.class, 2);
                    int idx = Random.Int(3);
                    GLog.yellow(Messages.get(this, "shock_hit_" + idx));

                    // 电场持续时间 3+lv 回合，电场持续伤害系数 20/30/40/50%
                    int fieldDuration = 3 + artifactLevel;
                    int fieldDamage = Math.round(damage * (0.2f + 0.1f * artifactLevel));
                    for (int offset : PathFinder.NEIGHBOURS9) {
                        int pos = enemypos + offset;
                        if (Dungeon.level.insideMap(pos) && Dungeon.level.passable[pos]) {
                            TrackableElectricity field = Blob.seed(pos, fieldDuration, TrackableElectricity.class);
                            field.setExternalDamage(fieldDamage);
                            field.setAllowParalysis(false);
                            GameScene.add(field);
                        }
                    }
                };
                break;
            case 2: // 2 穿甲燃烧箭
                missileItem = new SniperSupport.BurnSnipeArrow();
                damageLogic = () -> {
                    int damage = burnDamage(depth);
                    if (enemy.isAlive()) enemy.damage(damage, this, PHYSICAL);
                    if (enemy.isAlive()) Buff.affect(enemy, HalomethaneBurning.class).reignite(enemy, 4 + artifactLevel * 2);
                    int idx = Random.Int(3);
                    GLog.b(Messages.get(this, "burn_hit_" + idx));
                };
                break;
        }

        ThanksMissileSprite missile = new ThanksMissileSprite();
        GameScene.scene.add(missile);
        missile.reset(startPos, enemy, missileItem, () -> Actor.add(new Actor() {
            {
                actPriority = VFX_PRIO;
            }

            @Override
            protected boolean act() {
                //延迟到 Actor 线程执行伤害逻辑，避免与行动队列并发竞争
                if (enemy != null && Actor.chars().contains(enemy)) {
                    damageLogic.run();
                }
                Actor.remove(this);
                return true;
            }
        }));

    }

    private int frostDamage(int depth) {
        int min = 5 + depth;
        int max = 10 + (int)(depth * 1.5);
        return Random.NormalIntRange(min, max);
    }

    private int shockDamage(int depth) {
        int min = 15 + depth;
        int max = 25 + (int)(depth * 1.3);
        return Random.NormalIntRange(min, max);
    }

    private int burnDamage(int depth) {
        int min = 15 + depth;
        int max = 25 + (int)(depth * 1.8);
        return Random.NormalIntRange(min, max);
    }

    @Override
    public int icon() {
        return BuffIndicator.ARROW_PARTY;
    }

    @Override
    public float iconFadePercent() {
        if (duration <= 0) return 0f;
        return Math.max(0, ((float) (duration - left)) / duration);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc",
                left,
                timers[0], timers[1], timers[2]);
    }

    @Override
    public String iconTextDisplay() {
        // 显示总剩余回合数（简要）
        return Integer.toString(left);
    }

    // 序列化
    private static final String DURATION = "duration";
    private static final String TIMERS = "timers";
    private static final String LEFT = "left";
    private static final String LEVEL = "artifactLevel";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DURATION, duration);
        bundle.put(TIMERS, timers);
        bundle.put(LEFT, left);
        bundle.put(LEVEL, artifactLevel);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        duration = bundle.getInt(DURATION);
        timers = bundle.getIntArray(TIMERS);
        if (timers == null || timers.length != 3) timers = new int[]{0, 0, 0};
        left = bundle.getInt(LEFT);
        artifactLevel = bundle.getInt(LEVEL);
    }
}