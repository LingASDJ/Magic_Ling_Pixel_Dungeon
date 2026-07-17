package com.shatteredpixel.shatteredpixeldungeon.items.thanks;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.List;

public class TrackableElectricity extends Blob {
    // 使用外部伤害
    private boolean useExternalDamage = false;
    // 外部伤害值
    private int externalDamageValue = 5;
    // 允许造成麻痹
    private boolean allowParalysis = true;
    // 每跳衰减系数（0~1之间），1表示不衰减，乘算
    private float attenuationFactor = 0.75f;
    // 基础强度
    private  int intPower = 5;


    // 外部配置函数
    public void setExternalDamage(int value) {
        this.externalDamageValue = value;
        this.useExternalDamage = true;
    }

    public void setAllowParalysis(boolean allowParalysis) {
        this.allowParalysis = allowParalysis;
    }

    public void setAttenuationFactor(float value) {
        this.attenuationFactor = value;
    }

    public void setIntPower(int value) {
        this.intPower = value;
    }

    // 记录本轮受到伤害的单位
    public final List<Char> damagedThisTurn = new ArrayList<>();

    @Override
    protected void evolve() {
        damagedThisTurn.clear();

        boolean[] water = Dungeon.level.water;
        int cell;

        // 扩散（借鉴 Electricity）
        for (int i = area.left - 1; i <= area.right; i++) {
            for (int j = area.top - 1; j <= area.bottom; j++) {
                cell = i + j * Dungeon.level.width();
                if (cur[cell] > 0) {
                    spreadFromCell(cell, (float)cur[cell], water);
                }
            }
        }

        // 衰减与伤害
        for (int i = area.left - 1; i <= area.right; i++) {
            for (int j = area.top - 1; j <= area.bottom; j++) {
                cell = i + j * Dungeon.level.width();
                if (cur[cell] > 0) {
                    Char ch = Actor.findChar(cell);
                    if (ch != null && !ch.isImmune(this.getClass())) {
                        if (allowParalysis) {
                            if (ch.buff(Paralysis.class) == null) {
                                Buff.prolong(ch, Paralysis.class, cur[cell]);
                            }
                        }
                        // 奇数强度造成伤害
                        if (cur[cell] % 2 == 1) {
                            // 伤害公式
                            int damage = Damage(Dungeon.scalingDepth() , cur[cell]);
                            ch.damage(damage, this, Char.DamageType.Element);
                            if(ch == Dungeon.hero){
                                GLog.n(Messages.get(SniperSupport.class, "shock_player_0"));
                            }
                            // 记录受伤害单位
                            damagedThisTurn.add(ch);
                            if (!ch.isAlive() && ch == Dungeon.hero) {
                                Dungeon.fail(this);
                                GLog.n(Messages.get(this, "ondeath"));
                            }
                        }
                    }

                    // 充能逻辑
                    Heap h = Dungeon.level.heaps.get(cell);
                    if (h != null) {
                        Item toShock = h.peek();
                        if (toShock instanceof Wand) {
                            ((Wand) toShock).gainCharge(0.333f);
                        } else if (toShock instanceof MagesStaff) {
                            ((MagesStaff) toShock).gainCharge(0.333f);
                        }
                    }

                    off[cell] = cur[cell] - 1;
                    volume += off[cell];
                } else {
                    off[cell] = 0;
                }
            }
        }
    }

    // 扩散
    private void spreadFromCell(int cell, float power, boolean[] water) {
        int intPower = Math.round(power);
        if (intPower <= 0) return;

        if (cur[cell] == 0) {
            area.union(cell % Dungeon.level.width(), cell / Dungeon.level.width());
        }
        cur[cell] = Math.max(cur[cell], intPower);

        float nextPower = power * attenuationFactor;
        if (nextPower < 1) return;

        for (int c : PathFinder.NEIGHBOURS4) {
            int neighbor = cell + c;
            if (Dungeon.level.insideMap(neighbor) && water[neighbor] && cur[neighbor] < Math.round(nextPower)) {
                spreadFromCell(neighbor, nextPower, water);
            }
        }
    }

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);
        emitter.start(SparkParticle.FACTORY, 0.05f, 0);
    }

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }

    private int Damage(int depth , int cellStrength) {

        int base = Random.NormalIntRange(5 , 10 + depth/2);
        if (useExternalDamage) {
            base = externalDamageValue;
        }
        return (int)(base * (cellStrength / (float)intPower));
    }
}