package com.shatteredpixel.shatteredpixeldungeon.multiplayer;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Gizmo;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;

import java.util.ArrayList;
import java.util.List;

/**
 * /list 的玩家列表窗口（房主/访客都可打开）。
 * 每行：职业图标、玩家名+身份（房主/管理员/访客）、血量条+数值、楼层、网络状态；每 3 秒刷新一次。
 */
/** 玩家列表窗口（/list）。 */
public class WndPlayerList extends Window {

    private static final int WIDTH = 260;
    private static final int ROW_HEIGHT = 27;
    private static final int GAP = 2;
    private static final float HP_BAR_WIDTH = 90f;
    private static final float HP_BAR_HEIGHT = 4f;

    private final List<Gizmo> rowGizmos = new ArrayList<>();
    private final float top;
    private long lastRefresh = 0L;

    /**
     * 玩家列表窗口（/list）。
     */
    public WndPlayerList() {
        RenderedTextBlock title = PixelScene.renderTextBlock(
                Messages.get(WndPlayerList.class, "title"), 9);
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
            // 整行可点击：查看该玩家的英雄详情（与看自己一致）
            final int pid = info.id;
            PointerArea rowClick = new PointerArea(0, y, WIDTH, ROW_HEIGHT) {
                @Override
                protected void onClick(PointerEvent event) {
                    Mp.get().showRemoteHero(pid);
                }
            };
            add(rowClick);
            rowGizmos.add(rowClick);

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

            String baseName = (info.name == null || info.name.isEmpty())
                    ? Messages.get(WndPlayerList.class, "default_name") : info.name;
            if (info.isSelf) baseName += Messages.get(WndPlayerList.class, "self_suffix");
            RenderedTextBlock name = PixelScene.renderTextBlock(baseName, 7);
            name.maxWidth(WIDTH - 120);
            name.hardlight(0xFFFFFFFF);
            name.setPos(16f, y + 1f);
            add(name);
            rowGizmos.add(name);

            // 身份标签单独着色：房主红、管理员黄、访客白
            String roleKey = info.isHost ? "host_suffix"
                    : (info.isAdmin ? "admin_suffix" : "guest_suffix");
            int roleColor = info.isHost ? 0xFFFF4444
                    : (info.isAdmin ? 0xFFFFD700 : 0xFFFFFFFF);
            RenderedTextBlock role = PixelScene.renderTextBlock(
                    Messages.get(WndPlayerList.class, roleKey), 7);
            role.hardlight(roleColor);
            role.setPos(16f + name.width() + 3f, y + 1f);
            add(role);
            rowGizmos.add(role);

            RenderedTextBlock state = PixelScene.renderTextBlock(statusText(info), 6);
            state.setPos(WIDTH - state.width() - 2f, y + 2f);
            add(state);
            rowGizmos.add(state);

            // 血量条（第二行）：背景 + 按比例填充 + 数值
            float barX = 16f;
            float barY = y + 16f;
            ColorBlock hpBg = new ColorBlock(HP_BAR_WIDTH, HP_BAR_HEIGHT, 0xCC330000);
            hpBg.setPos(barX, barY);
            add(hpBg);
            rowGizmos.add(hpBg);
            boolean hasHp = info.hp >= 0 && info.ht > 0;
            if (hasHp) {
                float ratio = Math.max(0f, Math.min(1f, info.hp / (float) info.ht));
                ColorBlock hpFill = new ColorBlock(
                        HP_BAR_WIDTH * ratio, HP_BAR_HEIGHT, 0xFF44CC44);
                hpFill.setPos(barX, barY);
                add(hpFill);
                rowGizmos.add(hpFill);
            }
            RenderedTextBlock hpText = PixelScene.renderTextBlock(
                    hasHp ? Messages.get(WndPlayerList.class, "hp_value", info.hp, info.ht)
                            : Messages.get(WndPlayerList.class, "floor_unknown"), 6);
            hpText.setPos(barX + HP_BAR_WIDTH + 4f, y + 14f);
            add(hpText);
            rowGizmos.add(hpText);

            // 楼层（第二行右侧）
            String floorText = info.depth > 0
                    ? Messages.get(WndPlayerList.class, "floor", info.depth)
                    : Messages.get(WndPlayerList.class, "floor_unknown");
            RenderedTextBlock floor = PixelScene.renderTextBlock(floorText, 6);
            floor.setPos(WIDTH - floor.width() - 2f, y + 14f);
            add(floor);
            rowGizmos.add(floor);

            y += ROW_HEIGHT + GAP;
        }

        // 底部提示：点击玩家行查看英雄详情
        RenderedTextBlock hint = PixelScene.renderTextBlock(
                Messages.get(WndPlayerList.class, "click_hint"), 6);
        hint.hardlight(0x808080);
        hint.setPos(0f, y + 2f);
        add(hint);
        rowGizmos.add(hint);

        int newHeight = (int) Math.max(top + ROW_HEIGHT, y + hint.height() + 2f);
        if (newHeight != height) {
            resize(WIDTH, newHeight);
        }
    }

    private String statusText(Mp.PlayerInfo info) {
        if (info.isSelf) return Messages.get(WndPlayerList.class, "local");
        if (info.rttMs >= 0) return info.rttMs + " ms";
        if (info.timeout) return Messages.get(WndPlayerList.class, "timeout");
        return Messages.get(WndPlayerList.class, "measuring");
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
