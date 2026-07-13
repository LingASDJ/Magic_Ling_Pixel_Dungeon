package com.shatteredpixel.shatteredpixeldungeon.items.thanks;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.*;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

import static com.shatteredpixel.shatteredpixeldungeon.actors.Char.DamageType.*;

public class SniperSupport extends Buff {
    private int triggers;              // 总触发次数
    private int triggersLeft;          // 剩余触发次数
    private final int interval = 15;   // 固定间隔（15回合）
    private int delay = interval;      // cd剩余
    private int playerTextCooldown = 0;

    public void setTriggers(int count) {
        triggers = count;
        triggersLeft = count;
        delay = 0;
    }

    @Override
    public boolean act() {

        // 检查电场是否电到玩家
        if(playerTextCooldown>0) playerTextCooldown--;
        if (playerTextCooldown==0 && Dungeon.hero.isAlive()) {
            for (Blob blob : Dungeon.level.blobs.values()) {
                if (blob instanceof TrackableElectricity) {
                    TrackableElectricity te = (TrackableElectricity) blob;
                    if (te.damagedThisTurn.contains(Dungeon.hero)) {
                        GLog.p(Messages.get(this, "shock_player_0"));
                        playerTextCooldown = 5;
                        break;
                    }
                }
            }
        }

        if (!target.isAlive() || triggersLeft <= 0) {
            detach();
            return true;
        }
        spend(1);
        // 如果尚未就绪，减少延迟并等待
        if (delay > 0) {
            delay--;
            return true;
        }
        // 延迟为0，尝试执行狙击
        Char enemy = chooseRandomEnemy();
        if (enemy != null) {
            performSnipe(enemy);
            triggersLeft--;
            delay = interval;
        }
        return true;
    }

    //视野内随机索敌，返回视野内的一个随机敌人
    private Char chooseRandomEnemy() {
        ArrayList<Char> enemies = new ArrayList<>();
        for (Char ch : Actor.chars()) {
            if (ch != target && target.fieldOfView[ch.pos] && ch.alignment != target.alignment) {
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

    private void performSnipe(Char enemy) {
        int depth = Dungeon.depth;
        int type = Random.Int(3); // 0:霜冻, 1:电磁, 2:燃烧
        int damage;
        switch (type) {
            default:
            case 0: // 霜冻阻滞箭
            {
                damage = frostDamage(depth);
                damage = applyArmorReduction(enemy, damage);
                enemy.damage(damage, this, PHYSICAL);
                Buff.affect(enemy, Frost.class, 10);
                int idx = Random.Int(3); // 0,1,2
                GLog.p(Messages.get(this, "frost_hit_" + idx));
                break;
            }
            case 1: // 电磁震荡箭
            {
                damage = shockDamage(depth);
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
                break;
            }
            case 2: // 穿甲燃烧箭
            {
                damage = burnDamage(depth);
                enemy.damage(damage, this, PHYSICAL);
                Buff.affect(enemy, HalomethaneBurning.class).reignite(enemy, 10);
                int idx = Random.Int(3); // 0,1,2
                GLog.p(Messages.get(this, "burn_hit_" + idx));
                break;
            }
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
    public String toString() {return Messages.get(this, "name");}

    @Override
    public String desc() {
        return Messages.get(this, "desc",
                triggersLeft,          // 剩余次数
                dispTurns(delay)       // 剩余cd
        );
    }
    @Override
    public int icon() {return BuffIndicator.HALOMETHANEBURNING;}

    @Override
    public float iconFadePercent() {
        if (triggers <= 0) return 0f;
        return Math.max(0, ((float)(triggers - triggersLeft) )/ triggers);
    }

    // 序列化
    private static final String TRIGGERS = "triggers";
    private static final String TRIGGERS_LEFT = "triggersLeft";
    private static final String DELAY = "delay";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(TRIGGERS, triggers);
        bundle.put(TRIGGERS_LEFT, triggersLeft);
        bundle.put(DELAY, delay);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        triggers = bundle.getInt(TRIGGERS);
        triggersLeft = bundle.getInt(TRIGGERS_LEFT);
        delay = bundle.getInt(DELAY);
    }
}