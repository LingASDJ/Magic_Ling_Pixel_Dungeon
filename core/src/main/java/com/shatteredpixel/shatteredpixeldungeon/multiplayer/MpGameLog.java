package com.shatteredpixel.shatteredpixeldungeon.multiplayer;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 左上角联机日志：只显示聊天和联机相关消息。
 *
 * <p>原版左下角事件栏（GameLog/GLog）继续只显示游戏本体内容；
 * 联机消息与聊天统一显示在这里。</p>
 */
public class MpGameLog extends Component {

    /** 左侧页面/Boss/节日指示按钮约占 x=0~24，日志从 x=28 开始避开它们。 */
    private static final float X = 28f;
    /** 状态栏高度约 32~39，日志从 y=40 开始。 */
    private static final float Y = 40f;
    private static final float LINE_GAP = 2f;

    private final List<RenderedTextBlock> blocks = new ArrayList<>();
    private final float width;
    private final float x;
    private final float anchorY;
    private final boolean bottomUp;
    private final int maxEntries;
    /** 指定文字使用的相机（对局内必须用 uiCamera；房间内为 null 继承场景相机）。 */
    private final Camera cameraOverride;
    /** 半透明底衬，保证手机端在明亮/复杂地图上也能看清。 */
    private final ColorBlock bg;
    private long lastVersion = -1L;

    /** 对局内左上角日志：从 y=40 向下排列，最多 5 条。 */
    public MpGameLog() {
        this(X, Y, false, Mp.MPLOG_MAX_LINES, PixelScene.uiCamera);
    }

    /** 房间左下角日志：从 bottomY 向上排列（最新在底部），最多 maxEntries 条。 */
    public MpGameLog(float x, float bottomY, int maxEntries) {
        this(x, bottomY, true, maxEntries, null);
    }

    private MpGameLog(float x, float anchorY, boolean bottomUp, int maxEntries,
                      Camera cameraOverride) {
        this.x = x;
        this.anchorY = anchorY;
        this.bottomUp = bottomUp;
        this.maxEntries = Math.max(1, maxEntries);
        this.cameraOverride = cameraOverride;
        if (cameraOverride != null) {
            this.camera = cameraOverride;
        }
        width = Math.min(220f, Math.max(120f, PixelScene.uiCamera.width * 0.45f));
        bg = new ColorBlock(1f, 1f, 0x000000);
        bg.am = 0.35f;
        bg.visible = false;
        if (cameraOverride != null) {
            bg.camera = cameraOverride;
        }
        add(bg);
        lastVersion = Mp.get().getMpLogVersion();
        refresh();
    }

    @Override
    public void update() {
        super.update();
        long version = Mp.get().getMpLogVersion();
        if (version != lastVersion) {
            lastVersion = version;
            refresh();
        }
    }

    /** 按联机日志快照重建（最多 5 条，重建成本很低）。 */
    private void refresh() {
        for (RenderedTextBlock block : blocks) {
            remove(block);
            block.destroy();
        }
        blocks.clear();

        List<Mp.MpLogLine> lines = Mp.get().getMpLogLines();
        if (lines.size() > maxEntries) {
            lines = new ArrayList<>(lines.subList(lines.size() - maxEntries, lines.size()));
        }
        for (Mp.MpLogLine line : lines) {
            RenderedTextBlock block = PixelScene.renderTextBlock(6);
            block.hardlight(colorFor(line.type));
            // 聊天内容里的下划线/花括号不应该触发原版文字高亮
            block.setHightlighting(false);
            block.text(line.text, (int) width);
            block.setPos(0, 0);   // 先测量高度，再统一排版
            if (cameraOverride != null) {
                block.camera = cameraOverride;
            }
            add(block);
            blocks.add(block);
        }

        if (bottomUp) {
            float total = 0f;
            for (RenderedTextBlock block : blocks) {
                total += block.height() + LINE_GAP;
            }
            float y = anchorY - Math.max(0f, total - LINE_GAP);
            for (RenderedTextBlock block : blocks) {
                block.setPos(x, y);
                y = block.bottom() + LINE_GAP;
            }
        } else {
            float y = anchorY;
            for (RenderedTextBlock block : blocks) {
                block.setPos(x, y);
                y = block.bottom() + LINE_GAP;
            }
        }

        if (blocks.isEmpty()) {
            bg.visible = false;
        } else {
            float top = Float.MAX_VALUE;
            float bottom = -Float.MAX_VALUE;
            for (RenderedTextBlock block : blocks) {
                top = Math.min(top, block.top());
                bottom = Math.max(bottom, block.bottom());
            }
            bg.x = Math.max(0f, x - 2f);
            bg.y = Math.max(0f, top - 2f);
            bg.size(width + 4f, (bottom - top) + 4f);
            bg.visible = true;
        }
    }

    private static int colorFor(int type) {
        switch (type) {
            case Mp.MPLOG_CHAT_SELF:
                return Window.Pink_COLOR;
            case Mp.MPLOG_CHAT_OTHER:
                return 0xB06BE0;
            case Mp.MPLOG_CHAT_SYSTEM:
            case Mp.MPLOG_EVENT:
                return 0xFFD700;
            case Mp.MPLOG_NEGATIVE:
                return CharSprite.NEGATIVE;
            case Mp.MPLOG_POSITIVE:
                return CharSprite.POSITIVE;
            case Mp.MPLOG_WARNING:
                return CharSprite.WARNING;
            case Mp.MPLOG_INFO:
            default:
                return CharSprite.DEFAULT;
        }
    }
}
