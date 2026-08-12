package com.shatteredpixel.shatteredpixeldungeon.items.thanks;

import static com.shatteredpixel.shatteredpixeldungeon.actors.Char.DamageType.PHYSICAL;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.HalomethaneFlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Visual;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SniperSupport extends Buff {
    private int triggers;              // 总触发次数
    private int triggersLeft;          // 剩余触发次数
    private final int interval = 8;   // 固定间隔（15回合）
    private int delay = 0;      // cd剩余
    private int duration = 1;   // 燃烧箭燃烧buff持续时间

    // 由附加buff者设置燃烧箭燃烧buff持续时间
    public void setBurnDuration(int durate) {
        if (durate < 0) {
            duration = 1;
            return;
        }
        duration = durate;
    }

    public void setTriggers(int count) {
        triggers = count;
        triggersLeft = count;
        delay = 0;
    }

    public static class ShockSnipeArrow extends MissileWeapon {
        {
            image = ItemSpriteSheet.SHOCK_ARROW;
        }
        @Override
        public Emitter emitter() {
            Emitter e = new Emitter();
            e.pos(5, 5);
            e.fillTarget = false;
            e.pour(SparkParticle.FACTORY, 0.02f);
            return e;
        }
    }


    public static class FrostSnipeArrow extends MissileWeapon {
        {
            image = ItemSpriteSheet.FROST_ARROW;
        }
        @Override
        public Emitter emitter() {
            Emitter e = new Emitter();
            e.pos(5, 5);
            e.fillTarget = false;
            e.pour(MagicMissile.MagicParticle.FACTORY, 0.02f);
            return e;
        }
    }

    public static class BurnSnipeArrow extends MissileWeapon {
        {
            image = ItemSpriteSheet.BURN_ARROW;
        }
        @Override
        public Emitter emitter() {
            Emitter e = new Emitter();
            e.pos(5, 5);
            e.fillTarget = false;
            e.pour(HalomethaneFlameParticle.FACTORY, 0.02f);
            return e;
        }

    }

    @Override
    public boolean act() {

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
        if (target.fieldOfView == null) return null;
        ArrayList<Char> enemies = new ArrayList<>();
        for (Char ch : Actor.chars()) {
            if (ch != target
                    && ch.pos >= 0
                    && ch.pos < target.fieldOfView.length
                    && target.fieldOfView[ch.pos]
                    && ch.alignment != target.alignment
                    && ch instanceof Mob
                    && !(ch instanceof NPC)
                    && ch.isAlive()) {
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
        int type = Random.Int(3);
        Item missileItem;
        Runnable damageLogic;

        PointF screenLeftTop = Game.scene().camera().scroll;  // 狙击点为屏幕左上角
        PointF startPos = new PointF(screenLeftTop.x - 50, screenLeftTop.y - 50);

        switch (type) {
            default:
            case 0: // 霜冻箭
                missileItem = new FrostSnipeArrow();
                damageLogic = () -> {
                    int damage = frostDamage(depth);
                    damage = applyArmorReduction(enemy, damage);
                    if (enemy.isAlive()) enemy.damage(damage, this, PHYSICAL);
                    if (enemy.isAlive()) Buff.affect(enemy, Chill.class, 10);
                    int idx = Random.Int(3);
                    GLog.p(Messages.get(this, "frost_hit_" + idx));
                };
                break;
            case 1: // 电磁箭
                missileItem = new ShockSnipeArrow();
                damageLogic = () -> {
                    int enemypos = enemy.pos;
                    int damage = shockDamage(depth);
                    damage = applyArmorReduction(enemy, damage);
                    if (enemy.isAlive()) enemy.damage(damage, this, PHYSICAL);
                    if (enemy.isAlive()) Buff.affect(enemy, Paralysis.class, 2);
                    int idx = Random.Int(3);
                    GLog.yellow(Messages.get(this, "shock_hit_" + idx));

                    for (int offset : PathFinder.NEIGHBOURS9) {
                        int pos = enemypos + offset;
                        if (Dungeon.level.insideMap(pos) && Dungeon.level.passable[pos]) {
                            TrackableElectricity field = Blob.seed(pos, 5, TrackableElectricity.class);
                            field.setExternalDamage(Math.round(damage/2.0f));
                            field.setAllowParalysis(false);
                            GameScene.add(field);
                        }
                    }
                };
                break;
            case 2: // 燃烧穿甲箭
                missileItem = new BurnSnipeArrow();
                damageLogic = () -> {
                    int damage = burnDamage(depth);
                    if (enemy.isAlive()) enemy.damage(damage, this, PHYSICAL);
                    if (enemy.isAlive()) Buff.affect(enemy, HalomethaneBurning.class).reignite(enemy, 4 *(duration));
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
        int min = 5;
        int max = 10 + depth / 2;
        return Random.NormalIntRange(min, max);
    }

    private int shockDamage(int depth) {
        return frostDamage(depth); // 同霜冻公式
    }

    private int burnDamage(int depth) {
        int min = 15;
        int max = 25 + depth;
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
    public int icon() {return BuffIndicator.ARROW_NORMAL;}

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