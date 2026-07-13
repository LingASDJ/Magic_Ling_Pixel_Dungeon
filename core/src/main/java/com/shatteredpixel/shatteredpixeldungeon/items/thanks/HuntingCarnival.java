package com.shatteredpixel.shatteredpixeldungeon.items.thanks;

import static com.shatteredpixel.shatteredpixeldungeon.actors.Char.DamageType.PHYSICAL;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class HuntingCarnival extends Buff {
    private int duration;          // 总持续回合（一般为75回合）
    private int left;              // 剩余回合数
    private final int[] cooldowns = {4, 7, 10}; // 三位狙击手的射击间隔
    private int[] timers = {0, 0, 0};     // 当前剩余冷却（0表示可射击）

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

        left--;
        for (int i = 0; i < 3; i++) {
            if (timers[i] <= 0) {
                // 找敌人发射各个类型的箭
                Char enemy = chooseRandomEnemy();
                if (enemy != null)
                    Buff.affect(enemy,Paralysis.class,100f);{
                    performShot(i, enemy);
                    timers[i] = cooldowns[i];
                }
            } else {
                timers[i]--;
            }
        }
        spend(TICK);
        return true;
    }

    //视野内随机索敌，修复空指针、过滤NPC
    private Char chooseRandomEnemy() {
        if (target.fieldOfView == null) return null;
        ArrayList<Char> enemies = new ArrayList<>();
        for (Char ch : Actor.chars()) {
            if (ch != target
                    && ch.pos >= 0
                    && ch.pos < target.fieldOfView.length
                    && target.fieldOfView[ch.pos]
                    && ch.alignment != target.alignment
                    && ch instanceof Mob
                    && !(ch instanceof NPC)) {
                enemies.add(ch);
            }
        }
        if (enemies.isEmpty()) return null;
        return Random.element(enemies);
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

        switch (typeIdx) {
            default:
            case 0: // 0 霜冻阻滞箭
                missileItem = new SniperSupport.FrostSnipeArrow();
                damageLogic = () -> {
                    int damage = frostDamage(depth);
                    damage = applyArmorReduction(enemy, damage);
                    enemy.damage(damage, this, PHYSICAL);
                    Buff.affect(enemy, Frost.class, 10);
                    int idx = Random.Int(3);
                    GLog.p(Messages.get(this, "frost_hit_" + idx));
                    Buff.detach(enemy,Paralysis.class);
                };
                break;
            case 1: // 1 电磁震荡箭
                missileItem = new SniperSupport.ShockSnipeArrow();
                damageLogic = () -> {
                    int damage = shockDamage(depth);
                    damage = applyArmorReduction(enemy, damage);
                    enemy.damage(damage, this, PHYSICAL);
                    Buff.affect(enemy, Paralysis.class, 5);
                    int idx = Random.Int(3);
                    GLog.p(Messages.get(this, "shock_hit_" + idx));

                    // 在目标周围生成电场
                    for (int offset : PathFinder.NEIGHBOURS9) {
                        int pos = enemy.pos + offset;
                        if (Dungeon.level.insideMap(pos) && !Dungeon.level.solid[pos] && Dungeon.level.water[pos]) {
                            TrackableElectricity field = Blob.seed(pos, 5, TrackableElectricity.class);
                            GameScene.add(field);
                        }
                    }
                    Buff.detach(enemy,Paralysis.class);
                };
                break;
            case 2: // 2 穿甲燃烧箭
                missileItem = new SniperSupport.BurnSnipeArrow();
                damageLogic = () -> {
                    int damage = burnDamage(depth);
                    enemy.damage(damage, this, PHYSICAL);
                    Buff.affect(enemy, HalomethaneBurning.class).reignite(enemy, 10);
                    int idx = Random.Int(3);
                    GLog.p(Messages.get(this, "burn_hit_" + idx));
                    Buff.detach(enemy,Paralysis.class);
                };
                break;
        }

        MissileSprite missile = new MissileSprite();
        GameScene.scene.add(missile);
        if(enemy != null){
            missile.reset(
                    0,
                    enemy.sprite,
                    missileItem,
                    new Callback() {
                        @Override
                        public void call() {
                            damageLogic.run();
                        }
                    }
            );
        }

    }

    private int frostDamage(int depth) {
        int min = depth;
        int max = 10 + depth / 2;
        if (min > max) {
            int tmp = min;
            min = max;
            max = tmp;
        }
        return Random.NormalIntRange(min, max);
    }

    private int shockDamage(int depth) {
        return frostDamage(depth); // 同霜冻公式
    }

    private int burnDamage(int depth) {
        int min = 5 + depth;
        int max = 10 + depth;
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

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DURATION, duration);
        bundle.put(TIMERS, timers);
        bundle.put(LEFT, left);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        duration = bundle.getInt(DURATION);
        timers = bundle.getIntArray(TIMERS);
        if (timers == null || timers.length != 3) timers = new int[]{0, 0, 0};
        left = bundle.getInt(LEFT);
    }
}