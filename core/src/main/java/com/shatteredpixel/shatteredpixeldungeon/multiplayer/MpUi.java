package com.shatteredpixel.shatteredpixeldungeon.multiplayer;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.Camera;

/**
 * 联机自定义 UI 的等比缩放工具。
 *
 * <p>大厅/房间/个人主页等界面最初按桌面虚拟宽度约 480 设计；手机端
 * {@link Camera#main} 的虚拟宽度通常只有 270~400，直接使用固定尺寸会显得臃肿。
 * 这里按可用宽度相对 480 等比缩小，最大不超过 1（桌面保持原样）。</p>
 */
public final class MpUi {

    /** 横屏设计基准虚拟宽度（桌面实测约 541，手机横屏约 270~400）。 */
    public static final float DESIGN_WIDTH = 480f;
    /** 横屏设计基准虚拟高度（手机横屏高度通常只有 160~225）。 */
    public static final float DESIGN_HEIGHT = 360f;
    /** 竖屏设计基准虚拟宽度（9:16 手机）。竖屏屏幕更窄，使用独立基准避免 UI 被压得过小。 */
    public static final float DESIGN_WIDTH_PORTRAIT = 270f;
    /** 竖屏设计基准虚拟高度。 */
    public static final float DESIGN_HEIGHT_PORTRAIT = 480f;

    private MpUi() {}

    /**
     * 当前 UI 缩放系数（0.35~1.0）。
     *
     * <p>横屏按 480×360、竖屏按 270×480 的设计基准，分别同时参考宽度和高度，
     * 取两个方向的较小值，保证界面在低高度/窄宽度屏幕上也能完整放下。
     * 竖屏使用更小的设计基准，避免手机上所有控件被 480 宽基准压得过小。</p>
     */
    public static float scale() {
        float width = Camera.main != null ? Camera.main.width : DESIGN_WIDTH;
        float height = Camera.main != null ? Camera.main.height : DESIGN_HEIGHT;
        float designWidth = portrait() ? DESIGN_WIDTH_PORTRAIT : DESIGN_WIDTH;
        float designHeight = portrait() ? DESIGN_HEIGHT_PORTRAIT : DESIGN_HEIGHT;
        float scale = Math.min(width / designWidth, height / designHeight);
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

    /** 竖屏左右留白（设计像素）；竖屏屏幕窄，留白比横屏小。 */
    private static final float MARGIN_PORTRAIT = 2f;
    /** 横屏左右留白（设计像素）。 */
    private static final float MARGIN_LANDSCAPE = 6f;

    /** 是否竖屏（与游戏自身的横竖屏判定一致）。 */
    public static boolean portrait() {
        return !PixelScene.landscape();
    }

    /** 当前游戏画面的虚拟宽度。 */
    public static float screenWidth() {
        return Camera.main != null ? Camera.main.width : DESIGN_WIDTH;
    }

    /** 当前游戏画面的虚拟高度。 */
    public static float screenHeight() {
        return Camera.main != null ? Camera.main.height : DESIGN_HEIGHT;
    }

    /**
     * 当前方向的左右留白（虚拟像素）。
     *
     * <p>竖屏用 2 设计像素、横屏用 6 设计像素；竖屏时尽量把宽度让给列表和窗口。</p>
     */
    public static float sideMargin() {
        return px(portrait() ? MARGIN_PORTRAIT : MARGIN_LANDSCAPE);
    }

    /**
     * 场景内容宽度：竖屏铺满（只留 {@link #sideMargin()} 的小边距），
     * 横屏按设计宽度并限制在屏幕内。
     *
     * @param designWidth 横屏设计宽度（设计像素）
     * @return 实际可用的内容宽度（虚拟像素）
     */
    public static float contentWidth(float designWidth) {
        float available = Math.max(px(40f), screenWidth() - sideMargin() * 2f);
        if (portrait()) return available;
        return Math.min(px(designWidth), available);
    }

    /**
     * 窗口内容宽度：在屏幕宽度里扣掉 {@link com.shatteredpixel.shatteredpixeldungeon.ui.Window}
     * 边框（chrome）占用的横向边距，竖屏同样铺满。
     *
     * @param designWidth 横屏设计宽度（设计像素）
     * @param chromeMarginHor 窗口 chrome 的左右边距和（虚拟像素）
     * @return 实际可用的窗口内容宽度（虚拟像素）
     */
    public static float windowWidth(float designWidth, float chromeMarginHor) {
        float available = Math.max(px(40f),
                screenWidth() - chromeMarginHor - sideMargin() * 2f);
        if (portrait()) return available;
        return Math.min(px(designWidth), available);
    }
}
