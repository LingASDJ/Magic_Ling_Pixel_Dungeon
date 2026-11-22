package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silenced;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.tumulus.Egret;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.WhiteFlowerMark;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.HashMap;

/**
 * 纯白之花（希声奔赴之花）。
 *
 * <p>出现 1 回合后绽放：对同格玩家造成 20~60 点魔法伤害、施加 7 回合禁魔，
 * 并使白鹭获得等同于伤害值的护盾。不可拾取，也不参与选敌/掉落/死亡逻辑。</p>
 */
public class WhiteFlower extends Blob {

    private static final String AGE = "age";

    private int[] age;

    /** 每一格的白色魔法阵外观。 */
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
        flower.addFlower(cell);
    }

    private void addFlower(int cell) {
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
        }
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
                    off[cell] = 0;
                    age[cell] = 0;
                } else {
                    off[cell] = cur[cell];
                    volume += off[cell];
                }
            } else {
                off[cell] = 0;
                age[cell] = 0;
            }
        }
    }

    private void bloom(int cell) {
        removeMark(cell);

        Hero hero = Dungeon.hero;
        if (hero != null && hero.isAlive() && hero.pos == cell) {
            int dmg = Random.NormalIntRange(20, 60);
            hero.damage(dmg, this, Char.DamageType.MAGIC);
            Buff.affect(hero, Silenced.class, 7f);

            for (Char ch : Actor.chars()) {
                if (ch instanceof Egret && ch.isAlive()) {
                    ((Egret) ch).gainShield(dmg);
                }
            }
        }

        if (Dungeon.level.heroFOV[cell]) {
            CellEmitter.get(cell).burst(Speck.factory(Speck.LIGHT), 8);
            CellEmitter.get(cell).burst(Speck.factory(Speck.STAR), 3);
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
