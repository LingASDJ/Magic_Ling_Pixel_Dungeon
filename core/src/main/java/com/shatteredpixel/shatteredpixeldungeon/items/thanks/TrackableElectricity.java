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
                    spreadFromCell(cell, cur[cell], water);
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
                        if (ch.buff(Paralysis.class) == null) {
                            Buff.prolong(ch, Paralysis.class, cur[cell]);
                        }
                        // 奇数强度造成伤害
                        if (cur[cell] % 2 == 1) {
                            // 伤害公式
                            int damage = Damage(Dungeon.scalingDepth());
                            ch.damage(damage, this, Char.DamageType.Element);
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
    private void spreadFromCell(int cell, int power, boolean[] water) {
        if (cur[cell] == 0) {
            area.union(cell % Dungeon.level.width(), cell / Dungeon.level.width());
        }
        cur[cell] = Math.max(cur[cell], power);

        for (int c : PathFinder.NEIGHBOURS4) {
            int neighbor = cell + c;
            if (Dungeon.level.insideMap(neighbor) && water[neighbor] && cur[neighbor] < power) {
                spreadFromCell(neighbor, power, water);
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

    private int Damage(int depth) {
        int min = depth;
        int max = 10 + depth / 2;
        if (min > max) {
            int tmp = min;
            min = max;
            max = tmp;
        }
        return Random.NormalIntRange(min, max);
    }
}