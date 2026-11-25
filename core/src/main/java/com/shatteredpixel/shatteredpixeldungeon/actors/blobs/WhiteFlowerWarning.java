package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.ColorTargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 纯白之花爆炸范围预警。
 *
 * <p>纯白之花本体已经是地面 Item，这个 Blob 只负责显示 3x3 爆炸预警，
 * 不造成伤害。多个花范围重叠时用计数管理。</p>
 */
public class WhiteFlowerWarning extends Blob {

    private static final String COUNTS = "counts";

    private int[] counts;

    /** 每朵花对应的 3x3 预警格。 */
    private final HashMap<Integer, ArrayList<ColorTargetedCell>> cellEffects = new HashMap<>();

    {
        alwaysVisible = true;
    }

    public static void reveal(int center) {
        if (Dungeon.level == null || center < 0 || center >= Dungeon.level.length()) {
            return;
        }

        WhiteFlowerWarning warning = (WhiteFlowerWarning) Dungeon.level.blobs.get(WhiteFlowerWarning.class);
        if (warning == null) {
            warning = new WhiteFlowerWarning();
            Dungeon.level.blobs.put(WhiteFlowerWarning.class, warning);
            GameScene.add(warning);
        }
        warning.ensureArrays();

        int width = Dungeon.level.width();
        int cx = center % width;
        int cy = center / width;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int x = cx + dx;
                int y = cy + dy;
                if (x < 0 || x >= width || y < 0 || y >= Dungeon.level.height()) {
                    continue;
                }
                int cell = x + y * width;
                if (warning.counts[cell]++ == 0) {
                    warning.cur[cell] = 1;
                    warning.area.union(x, y);
                }
            }
        }
        warning.recalcVolume();
        warning.showCellEffects(center);
    }

    private void showCellEffects(int center) {
        if (Dungeon.hero == null || Dungeon.hero.sprite == null
                || Dungeon.hero.sprite.parent == null) {
            return;
        }
        if (cellEffects.containsKey(center)) {
            return;
        }

        ArrayList<ColorTargetedCell> effects = new ArrayList<>();
        int width = Dungeon.level.width();
        int cx = center % width;
        int cy = center / width;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int x = cx + dx;
                int y = cy + dy;
                if (x < 0 || x >= width || y < 0 || y >= Dungeon.level.height()) {
                    continue;
                }
                int cell = x + y * width;
                if (counts[cell] <= 0) {
                    continue;
                }

                ColorTargetedCell effect = new ColorTargetedCell(cell, 0xFF4422);
                Dungeon.hero.sprite.parent.add(effect);
                effects.add(effect);
            }
        }

        if (!effects.isEmpty()) {
            cellEffects.put(center, effects);
        }
    }

    public static void hide(int center) {
        if (Dungeon.level == null
                || !(Dungeon.level.blobs.get(WhiteFlowerWarning.class) instanceof WhiteFlowerWarning)) {
            return;
        }

        WhiteFlowerWarning warning = (WhiteFlowerWarning) Dungeon.level.blobs.get(WhiteFlowerWarning.class);
        if (warning.counts == null || center < 0 || center >= warning.counts.length) {
            return;
        }

        int width = Dungeon.level.width();
        int cx = center % width;
        int cy = center / width;

        warning.hideCellEffects(center);

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int x = cx + dx;
                int y = cy + dy;
                if (x < 0 || x >= width || y < 0 || y >= Dungeon.level.height()) {
                    continue;
                }
                int cell = x + y * width;
                if (warning.counts[cell] > 0 && --warning.counts[cell] == 0) {
                    warning.cur[cell] = 0;
                }
            }
        }

        warning.recalcVolume();
        if (warning.volume <= 0) {
            warning.fullyClear();
            Dungeon.level.blobs.remove(WhiteFlowerWarning.class);
            Actor.remove(warning);
        }
    }

    public static void clearAll() {
        if (Dungeon.level == null) {
            return;
        }
        WhiteFlowerWarning warning = (WhiteFlowerWarning) Dungeon.level.blobs.get(WhiteFlowerWarning.class);
        if (warning != null) {
            warning.clearCellEffects();
            warning.fullyClear();
            Dungeon.level.blobs.remove(WhiteFlowerWarning.class);
            Actor.remove(warning);
        }
    }

    private void hideCellEffects(int center) {
        ArrayList<ColorTargetedCell> effects = cellEffects.remove(center);
        if (effects != null) {
            for (ColorTargetedCell effect : effects) {
                effect.killAndErase();
            }
        }
    }

    private void clearCellEffects() {
        for (ArrayList<ColorTargetedCell> effects : cellEffects.values()) {
            for (ColorTargetedCell effect : effects) {
                effect.killAndErase();
            }
        }
        cellEffects.clear();
    }

    private void ensureArrays() {
        int length = Dungeon.level.length();
        if (cur == null || cur.length != length) {
            cur = new int[length];
            off = new int[length];
        }
        if (counts == null || counts.length != length) {
            counts = new int[length];
        }
    }

    private void recalcVolume() {
        if (cur == null) {
            volume = 0;
            return;
        }
        volume = 0;
        for (int i = 0; i < cur.length; i++) {
            off[i] = cur[i] > 0 ? 1 : 0;
            volume += off[i];
        }
    }

    @Override
    protected void evolve() {
        if (cur == null) {
            return;
        }
        recalcVolume();
    }

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);
        emitter.pour(Speck.factory(Speck.STAR), 0.30f);
        emitter.pour(Speck.factory(Speck.LIGHT), 0.15f);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        if (counts != null) {
            bundle.put(COUNTS, counts);
        }
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (bundle.contains(COUNTS)) {
            counts = bundle.getIntArray(COUNTS);
        }
    }
}
