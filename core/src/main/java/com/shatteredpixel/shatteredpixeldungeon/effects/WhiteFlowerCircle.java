package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * 白魔女开场：代码绘制的白色八芒星花。
 *
 * <p>不使用贴图，全部由 {@link ColorBlock} 沿线段绘制：外八边形、内八边形、
 * 八芒星（{8/3}）、八条剑形花瓣与中心菱形。整体从中心绽放、旋转、放大，
 * 最后淡出；配合粒子与镜头震动后由关卡生成白鹭。</p>
 */
public class WhiteFlowerCircle extends Group {

    private static final float BLOOM_HOLD = 2.2f;
    private static final float BLOOM_TIME = 0.8f;
    private static final float SHATTER_TIME = 0.9f;

    private final float cx;
    private final float cy;
    private final float radius;

    private final ArrayList<ColorBlock> blocks = new ArrayList<>();
    /** 每个色块相对花心的坐标。 */
    private final ArrayList<float[]> rel = new ArrayList<>();

    private final ArrayList<float[]> shatterVel = new ArrayList<>();
    private boolean shattering = false;
    private float shatterTime = 0f;

    private Runnable onShatter = null;

    private float life = 0f;

    public WhiteFlowerCircle(float cx, float cy, float radius) {
        this.cx = cx;
        this.cy = cy;
        this.radius = radius;
        build();
    }

    private void build() {
        final int n = 8;
        float[] vx = new float[n];
        float[] vy = new float[n];
        float[] ix = new float[n];
        float[] iy = new float[n];

        for (int i = 0; i < n; i++) {
            double a = Math.toRadians(i * 45);
            vx[i] = (float) Math.cos(a) * radius;
            vy[i] = (float) Math.sin(a) * radius;
            ix[i] = (float) Math.cos(a) * radius * 0.56f;
            iy[i] = (float) Math.sin(a) * radius * 0.56f;
        }

        final int outerColor = 0xFF72A2A8;
        final int starColor = 0xFF37E3D0;
        final int innerColor = 0xFFFFFFFF;
        final int swordColor = 0xFF37E3D0;
        final int coreColor = 0xFFE8FBFF;

        for (int i = 0; i < n; i++) {
            int next = (i + 1) % n;
            int star = (i + 3) % n;

            // 八芒星
            addLine(vx[i], vy[i], vx[star], vy[star], starColor);
            // 外八边形
            addLine(vx[i], vy[i], vx[next], vy[next], outerColor);
            // 内八边形
            addLine(ix[i], iy[i], ix[next], iy[next], innerColor);

            // 八条剑形花瓣：内圈向外延伸，并在尖端画两片短翼
            float tx = vx[i] * 1.36f;
            float ty = vy[i] * 1.36f;
            addLine(ix[i], iy[i], tx, ty, swordColor);

            float px = vx[i] / radius * 3.2f;
            float py = vy[i] / radius * 3.2f;
            float wx = -py;
            float wy = px;
            addLine(tx, ty, tx + wx, ty + wy, swordColor);
            addLine(tx, ty, tx - wx, ty - wy, swordColor);
        }

        // 中心菱形
        float c = radius * 0.15f;
        addLine(0, -c, c, 0, coreColor);
        addLine(c, 0, 0, c, coreColor);
        addLine(0, c, -c, 0, coreColor);
        addLine(-c, 0, 0, -c, coreColor);
    }

    private void addLine(float x1, float y1, float x2, float y2, int color) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        int steps = Math.max(1, (int) (len / 1.5f));

        for (int i = 0; i <= steps; i++) {
            float t = i / (float) steps;
            ColorBlock block = new ColorBlock(2, 2, color);
            float x = x1 + dx * t;
            float y = y1 + dy * t;
            block.x = cx + x;
            block.y = cy + y;
            add(block);
            blocks.add(block);
            rel.add(new float[]{x, y});
        }
    }

    @Override
    public void update() {
        super.update();

        // 破碎：每个色块带着随机速度飞散、下落并淡出
        if (shattering) {
            shatterTime += Game.elapsed;
            float p = Math.min(1f, shatterTime / SHATTER_TIME);

            for (int i = 0; i < blocks.size(); i++) {
                float[] v = shatterVel.get(i);
                v[1] += 90f * Game.elapsed;

                ColorBlock block = blocks.get(i);
                block.x += v[0] * Game.elapsed;
                block.y += v[1] * Game.elapsed;
                block.am = Math.max(0f, 1f - p);
            }

            if (p >= 1f) {
                killAndErase();
            }
            return;
        }

        life += Game.elapsed;
        float p = Math.min(1f, life / BLOOM_TIME);
        float ease = 1f - (1f - p) * (1f - p);

        float scale = 0.15f + 0.95f * ease;
        float rotation = (1f - p) * 0.65f;
        float alpha = p;

        float cs = (float) Math.cos(rotation);
        float sn = (float) Math.sin(rotation);

        for (int i = 0; i < blocks.size(); i++) {
            float[] r = rel.get(i);
            float x = r[0] * scale;
            float y = r[1] * scale;

            ColorBlock block = blocks.get(i);
            block.x = cx + x * cs - y * sn - 1f;
            block.y = cy + x * sn + y * cs - 1f;
            block.am = Math.max(0f, Math.min(1f, alpha));
        }

        // 不再按总时长自动淡出；到时间直接进入破碎，避免破碎效果被淡出吞掉
        if (life >= BLOOM_HOLD) {
            shatter();
        }
    }

    /** 破碎时立即回调，供关卡在同一帧生成白鹭，避免依赖延时。 */
    public void setOnShatter(Runnable callback) {
        onShatter = callback;
    }

    /** 让花像粒子一样破碎散开。 */
    public void shatter() {
        if (shattering) {
            return;
        }
        shattering = true;
        shatterTime = 0f;
        shatterVel.clear();

        for (int i = 0; i < blocks.size(); i++) {
            float angle = Random.Float(0f, (float) Math.PI * 2f);
            float speed = Random.Float(35f, 95f);
            shatterVel.add(new float[]{
                    (float) Math.cos(angle) * speed,
                    (float) Math.sin(angle) * speed
            });
        }

        if (onShatter != null) {
            Runnable callback = onShatter;
            onShatter = null;
            callback.run();
        }
    }
}
