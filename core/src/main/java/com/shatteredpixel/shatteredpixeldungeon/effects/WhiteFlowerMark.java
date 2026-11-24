package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;

/**
 * 纯白之花的贴图表现。
 *
 * <p>16×16 的 whiteflower.png 直接居中放在格子上，保留轻微明暗呼吸。</p>
 */
public class WhiteFlowerMark extends Image {

    private float time;

    public WhiteFlowerMark(float centerX, float centerY) {
        super(Assets.Sprites.WHITEFLOWER);

        x = centerX - width() / 2f;
        y = centerY - height() / 2f;
        origin.set(width() / 2f, height() / 2f);
    }

    @Override
    public void update() {
        super.update();

        time += Game.elapsed;
        alpha(0.75f + 0.25f * (float) Math.sin(time * 5f));
    }
}
