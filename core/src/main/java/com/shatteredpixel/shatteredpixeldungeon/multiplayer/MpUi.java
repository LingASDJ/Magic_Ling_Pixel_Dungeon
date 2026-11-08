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

    private MpUi() {}

    /** 当前 UI 缩放系数（0.5~1.0）。 */
    public static float scale() {
        float width = Camera.main != null ? Camera.main.width : DESIGN_WIDTH;
        float scale = width / DESIGN_WIDTH;
        if (scale > 1f) scale = 1f;
        if (scale < 0.5f) scale = 0.5f;
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
