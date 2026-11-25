package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silenced;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.tumulus.Egret;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.WhiteFlowerMark;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.HashMap;

/**
 * 纯白之花（希声奔赴之花）。
 *
 * <p>花朵本体是 Blob；爆炸范围由 {@link WhiteFlowerWarning} 额外提供 3x3 预警。</p>
 */
public class WhiteFlower extends Blob {

    private static final String AGE = "age";

    private int[] age;

    /** 每一格的白色花朵外观。 */
    private final HashMap<Integer, WhiteFlowerMark> marks = new HashMap<>();

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);
        emitter.pour(Speck.factory(Speck.LIGHT), 0.12f);
    }

    public static void plant(int cell) {
        if (cell < 0 || cell >= Dungeon.level.length()) {
            return;
        }

        WhiteFlower flower = (WhiteFlower) Dungeon.level.blobs.get(WhiteFlower.class);
        if (flower == null) {
            flower = new WhiteFlower();
            Dungeon.level.blobs.put(WhiteFlower.class, flower);
            GameScene.add(flower);
        }
        if (flower.addFlower(cell)) {
            // 爆炸范围预警
            WhiteFlowerWarning.reveal(cell);
        }
    }

    private boolean addFlower(int cell) {
        if (cur == null) {
            cur = new int[Dungeon.level.length()];
            off = new int[cur.length];
            age = new int[cur.length];
        }
        if (age == null || age.length != cur.length) {
            age = new int[cur.length];
        }

        if (cur[cell] <= 0) {
            cur[cell] = 1;
            age[cell] = -1;
            volume += 1;
            area.union(cell % Dungeon.level.width(), cell / Dungeon.level.width());
            createMark(cell);
            return true;
        }
        return false;
    }

    @Override
    protected void evolve() {
        if (cur == null || age == null || age.length != cur.length) {
            return;
        }

        for (int cell = 0; cell < cur.length; cell++) {
            if (cur[cell] > 0) {
                createMark(cell);
                age[cell]++;
                if (age[cell] >= 1) {
                    bloom(cell);
                    cur[cell] = 0;
                    off[cell] = 0;
                    age[cell] = 0;
                } else {
                    off[cell] = cur[cell];
                }
            } else {
                off[cell] = 0;
                age[cell] = 0;
            }
        }

        volume = 0;
        for (int cell = 0; cell < cur.length; cell++) {
            if (cur[cell] > 0) {
                volume++;
            }
        }
    }

    private void bloom(int cell) {
        removeMark(cell);
        WhiteFlowerWarning.hide(cell);

        Egret egret = null;
        for (Char ch : Actor.chars()) {
            if (ch instanceof Egret && ch.isAlive()) {
                egret = (Egret) ch;
                break;
            }
        }

        // 同格敌人：完整伤害 + 禁魔 + 护盾
        Char same = Actor.findChar(cell);
        if (isEnemy(same, egret)) {
            int dmg = Random.NormalIntRange(20, 60);
            damageEnemy(same, dmg, egret, true);
        }

        // 周围 8 格敌人：50% 伤害，不禁魔，正常给护盾
        for (int d : PathFinder.NEIGHBOURS8) {
            int adj = cell + d;
            if (adj < 0 || adj >= Dungeon.level.length()) {
                continue;
            }

            Char ch = Actor.findChar(adj);
            if (!isEnemy(ch, egret)) {
                continue;
            }

            int dmg = Math.max(1, Math.round(Random.NormalIntRange(20, 60) * 0.5f));
            damageEnemy(ch, dmg, egret, false);
        }

        if (Dungeon.level.heroFOV[cell]) {
            CellEmitter.get(cell).burst(Speck.factory(Speck.LIGHT), 8);
            CellEmitter.get(cell).burst(Speck.factory(Speck.STAR), 3);
        }
    }

    private boolean isEnemy(Char ch, Egret egret) {
        return ch != null
                && egret != null
                && ch.isAlive()
                && ch.alignment != egret.alignment
                && ch.alignment != Char.Alignment.NEUTRAL;
    }

    private void damageEnemy(Char ch, int dmg, Egret egret, boolean silence) {
        int before = ch.HP;
        ch.damage(dmg, this, Char.DamageType.MAGIC);
        int dealt = Math.max(0, before - ch.HP);

        if (silence) {
            Buff.affect(ch, Silenced.class, 7f);
        }
        if (egret != null && dealt > 0) {
            egret.gainShield(dealt);
        }
    }

    private void createMark(int cell) {
        if (marks.containsKey(cell) || Dungeon.level == null
                || Dungeon.hero == null || Dungeon.hero.sprite == null) {
            return;
        }

        PointF p = DungeonTilemap.raisedTileCenterToWorld(cell);
        if (Dungeon.hero.sprite.parent == null) {
            return;
        }

        WhiteFlowerMark mark = new WhiteFlowerMark(p.x, p.y);
        Dungeon.hero.sprite.parent.add(mark);
        marks.put(cell, mark);
    }

    private void removeMark(int cell) {
        WhiteFlowerMark mark = marks.remove(cell);
        if (mark != null) {
            mark.killAndErase();
        }
    }

    private void clearMarks() {
        for (WhiteFlowerMark mark : marks.values()) {
            mark.killAndErase();
        }
        marks.clear();
    }

    @Override
    public void fullyClear() {
        super.fullyClear();
        clearMarks();
        WhiteFlowerWarning.clearAll();
    }

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        if (age != null) {
            bundle.put(AGE, age);
        }
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (cur != null) {
            if (bundle.contains(AGE)) {
                age = bundle.getIntArray(AGE);
            }
            if (age == null || age.length != cur.length) {
                age = new int[cur.length];
            }
        }
    }
}
