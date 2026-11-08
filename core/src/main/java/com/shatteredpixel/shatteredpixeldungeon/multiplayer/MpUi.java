package com.shatteredpixel.shatteredpixeldungeon.multiplayer;

import com.watabou.noosa.Camera;

/**
 * 联机自定义 UI 的等比缩放工具。
 *
 * <p>大厅/房间/个人主页等界面最初按桌面虚拟宽度约 480 设计；手机端
 * {@link Camera#main} 的虚拟宽度通常只有 270~400，直接使用固定尺寸会显得臃肿。
 * 这里按可用宽度相对 480 等比缩小，最大不超过 1（桌面保持原样）。</p>
 */
public final class MpUi {

    /** 设计基准虚拟宽度（桌面实测约 541，手机横屏约 270~400）。 */
    public static final float DESIGN_WIDTH = 480f;
    /** 设计基准虚拟高度（手机横屏高度通常只有 160~225）。 */
    public static final float DESIGN_HEIGHT = 360f;

    private MpUi() {}

    /**
     * 当前 UI 缩放系数（0.35~1.0）。
     *
     * <p>同时参考宽度和高度：手机横屏的虚拟高度往往只有 160~225，
     * 只按宽度缩放会让聊天窗口等界面接近屏幕高度。取两个方向的较小值，
     * 保证界面在低高度屏幕上也能完整放下。</p>
     */
    public static float scale() {
        float width = Camera.main != null ? Camera.main.width : DESIGN_WIDTH;
        float height = Camera.main != null ? Camera.main.height : DESIGN_HEIGHT;
        float scale = Math.min(width / DESIGN_WIDTH, height / DESIGN_HEIGHT);
        if (scale > 1f) scale = 1f;
        if (scale < 0.35f) scale = 0.35f;
        return scale;
    }

    /** 按设计像素缩放。 */
    public static float px(float designPx) {
        return designPx * scale();
    }

    /** 按设计像素缩放并取整（最小 1）。 */
    public static int i(float designPx) {
        return Math.max(1, Math.round(px(designPx)));
    }

    /** 字体大小缩放（最小 5，避免不可读）。 */
    public static int font(int designSize) {
        return Math.max(5, Math.round(designSize * scale()));
    }
}
