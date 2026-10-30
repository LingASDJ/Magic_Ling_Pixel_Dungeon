package com.shatteredpixel.shatteredpixeldungeon.multiplayer;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Gizmo;
import com.watabou.noosa.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * /list 的玩家列表窗口（房主/访客都可打开）。
 * 每行：职业图标（仅图标）、玩家名、网络状态；延迟每 3 秒刷新一次。
 */
/** 玩家列表窗口（/list）。 */
public class WndPlayerList extends Window {

    private static final int WIDTH = 170;
    private static final int ROW_HEIGHT = 18;
    private static final int GAP = 2;

    private final List<Gizmo> rowGizmos = new ArrayList<>();
    private final float top;
    private long lastRefresh = 0L;

    /**
     * 玩家列表窗口（/list）。
     */
    public WndPlayerList() {
        RenderedTextBlock title = PixelScene.renderTextBlock("玩家列表", 9);
        title.hardlight(TITLE_COLOR);
        title.setPos(0f, 0f);
        title.maxWidth(WIDTH);
        add(title);

        top = title.bottom() + 4f;
        rebuild();
        lastRefresh = System.currentTimeMillis();
    }

    private void rebuild() {
        for (Gizmo g : rowGizmos) {
            remove(g);
        }
        rowGizmos.clear();

        List<Mp.PlayerInfo> list = Mp.get().getPlayerList();
        float y = top;
        for (Mp.PlayerInfo info : list) {
            if (info.heroClass != null) {
                Image icon = new Image(info.heroClass.spritesheet(), 0, 0, 12, 15);
                icon.setPos(0f, y + 1f);
                add(icon);
                rowGizmos.add(icon);
            } else {
                RenderedTextBlock unknown = PixelScene.renderTextBlock("?", 8);
                unknown.setPos(3f, y + 3f);
                add(unknown);
                rowGizmos.add(unknown);
            }

            String label = (info.name == null || info.name.isEmpty()) ? "玩家" : info.name;
            if (info.isSelf) label += "（我）";
            if (info.isHost) label += " [房主]";
            RenderedTextBlock name = PixelScene.renderTextBlock(label, 7);
            name.maxWidth(WIDTH - 70);
            name.setPos(16f, y + 2f);
            add(name);
            rowGizmos.add(name);

            RenderedTextBlock state = PixelScene.renderTextBlock(statusText(info), 6);
            state.setPos(WIDTH - state.width() - 2f, y + 4f);
            add(state);
            rowGizmos.add(state);

            y += ROW_HEIGHT + GAP;
        }

        int newHeight = (int) Math.max(top + ROW_HEIGHT, y - GAP);
        if (newHeight != height) {
            resize(WIDTH, newHeight);
        }
    }

    private String statusText(Mp.PlayerInfo info) {
        if (info.isSelf) return "本机";
        if (info.rttMs >= 0) return info.rttMs + " ms";
        if (info.timeout) return "超时";
        return "测量中…";
    }

    @Override
    /**
     * 每帧刷新界面状态。
     */
    public void update() {
        super.update();
        long now = System.currentTimeMillis();
        if (now - lastRefresh >= 3000L) {
            lastRefresh = now;
            rebuild();
        }
    }
}
