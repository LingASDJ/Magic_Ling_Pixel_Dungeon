package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;

import java.util.ArrayList;

/**
 * 白鹭开场的天降光柱。
 *
 * <p>一整根连续的青白色激光，从顶部贯穿到落点；只用同色、不同宽度的柔边叠出
 * 外柔内亮的效果，没有额外光斑或异色分层。</p>
 *
 * <p>颜色使用白鹭主题：{@code #37E3D0}。</p>
 */
public class DeadLaser extends Group {

    private static final float LIFE = 1.8f;

    private final ArrayList<ColorBlock> beams = new ArrayList<>();
    private final ArrayList<Float> baseAlpha = new ArrayList<>();

    private float life = 0f;

    public DeadLaser(float centerX, float topY, float bottomY) {
        float height = Math.max(1f, bottomY - topY);
        int color = 0xFF37E3D0;

        // 同一根激光的柔边：同色、不同宽度与透明度，重叠后仍是一根连续光柱
        addBeam(centerX, topY, height, 30, color, 0.16f);
        addBeam(centerX, topY, height, 18, color, 0.42f);
        addBeam(centerX, topY, height, 8, color, 1f);
    }

    private void addBeam(float centerX, float topY, float height, int width, int color, float alpha) {
        ColorBlock block = new ColorBlock(width, (int) height, color);
        block.x = centerX - width / 2f;
        block.y = topY;
        block.am = alpha;
        add(block);
        beams.add(block);
        baseAlpha.add(alpha);
    }

    @Override
    public void update() {
        super.update();

        life += Game.elapsed;

        float pulse = 0.78f + 0.22f * (float) Math.sin(life * 28f);
        float fade = life > LIFE - 0.35f ? Math.max(0f, (LIFE - life) / 0.35f) : 1f;

        for (int i = 0; i < beams.size(); i++) {
            beams.get(i).am = Math.max(0f, Math.min(1f, baseAlpha.get(i) * pulse * fade));
        }

        if (life >= LIFE) {
            killAndErase();
        }
    }
}
