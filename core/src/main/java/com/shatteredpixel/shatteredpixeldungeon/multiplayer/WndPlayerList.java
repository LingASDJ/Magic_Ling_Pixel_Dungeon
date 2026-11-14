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
    /** 实际窗口宽度：竖屏铺满屏幕，横屏保持设计宽度。 */
    private final float width;
    private long lastRefresh = 0L;

    /**
     * 玩家列表窗口（/list）。
     */
    public WndPlayerList() {
        width = MpUi.windowWidth(WIDTH, chrome.marginHor());
        RenderedTextBlock title = PixelScene.renderTextBlock(
                Messages.get(WndPlayerList.class, "title"), MpUi.font(9));
        title.hardlight(TITLE_COLOR);
        title.setPos(0f, 0f);
        title.maxWidth((int) width);
        add(title);

        top = title.bottom() + MpUi.px(4f);
        rebuild();
        lastRefresh = System.currentTimeMillis();
    }

    private void rebuild() { // skipcq: JAVA-R1000 每行玩家信息元素多：复杂度高但已实测稳定
        for (Gizmo g : rowGizmos) {
            remove(g);
            g.destroy();   // PointerArea 必须 destroy 才会移除全局监听，避免关闭后误点
        }
        rowGizmos.clear();

        List<Mp.PlayerInfo> list = Mp.get().getPlayerList();
        float y = top;
        for (Mp.PlayerInfo info : list) {
            // 整行可点击：查看该玩家的英雄详情（与看自己一致）
            final int pid = info.id;
            PointerArea rowClick = new PointerArea(0, y, width, MpUi.i(ROW_HEIGHT)) {
                @Override
                protected void onClick(PointerEvent event) {
                    // 先关闭列表：否则详情窗口关闭后列表仍留在底层，会再次拦截点击/重复打开旧详情
                    hide();
                    Mp.get().showRemoteHero(pid);
                }
            };
            add(rowClick);
            rowGizmos.add(rowClick);

            if (info.heroClass != null) {
                Image icon = new Image(info.heroClass.spritesheet(), 0, 0, 12, 15);
                icon.scale.set(MpUi.scale());
                icon.setPos(0f, y + MpUi.px(1f));
                add(icon);
                rowGizmos.add(icon);
            } else {
                RenderedTextBlock unknown = PixelScene.renderTextBlock("?", MpUi.font(8));
                unknown.setPos(MpUi.px(3f), y + MpUi.px(3f));
                add(unknown);
                rowGizmos.add(unknown);
            }

            String baseName = (info.name == null || info.name.isEmpty())
                    ? Messages.get(WndPlayerList.class, "default_name") : info.name;
            if (info.isSelf) baseName += Messages.get(WndPlayerList.class, "self_suffix");
            RenderedTextBlock name = PixelScene.renderTextBlock(baseName, MpUi.font(7));
            name.maxWidth((int) Math.max(MpUi.px(40f), width - MpUi.px(120f)));
            name.hardlight(0xFFFFFFFF);
            name.setPos(MpUi.px(16f), y + MpUi.px(1f));
            add(name);
            rowGizmos.add(name);

            // 身份标签单独着色：房主红、管理员黄、访客白
            String roleKey = info.isHost ? "host_suffix"
                    : (info.isAdmin ? "admin_suffix" : "guest_suffix");
            int roleColor = info.isHost ? 0xFFFF4444
                    : (info.isAdmin ? 0xFFFFD700 : 0xFFFFFFFF);
            RenderedTextBlock role = PixelScene.renderTextBlock(
                    Messages.get(WndPlayerList.class, roleKey), MpUi.font(7));
            role.hardlight(roleColor);
            role.setPos(MpUi.px(16f) + name.width() + MpUi.px(3f), y + MpUi.px(1f));
            add(role);
            rowGizmos.add(role);

            RenderedTextBlock state = PixelScene.renderTextBlock(statusText(info), MpUi.font(6));
            state.setPos(width - state.width() - MpUi.px(2f), y + MpUi.px(2f));
            add(state);
            rowGizmos.add(state);

            // 血量条（第二行）：背景 + 按比例填充 + 数值
            float barX = MpUi.px(16f);
            float barY = y + MpUi.px(16f);
            float hpBarWidth = MpUi.px(HP_BAR_WIDTH);
            float hpBarHeight = MpUi.px(HP_BAR_HEIGHT);
            ColorBlock hpBg = new ColorBlock(hpBarWidth, hpBarHeight, 0xCC330000);
            hpBg.setPos(barX, barY);
            add(hpBg);
            rowGizmos.add(hpBg);
            boolean hasHp = info.hp >= 0 && info.ht > 0;
            if (hasHp) {
                float ratio = Math.max(0f, Math.min(1f, info.hp / (float) info.ht));
                ColorBlock hpFill = new ColorBlock(
                        hpBarWidth * ratio, hpBarHeight, 0xFF44CC44);
                hpFill.setPos(barX, barY);
                add(hpFill);
                rowGizmos.add(hpFill);
            }
            RenderedTextBlock hpText = PixelScene.renderTextBlock(
                    hasHp ? Messages.get(WndPlayerList.class, "hp_value", info.hp, info.ht)
                            : Messages.get(WndPlayerList.class, "floor_unknown"), MpUi.font(6));
            hpText.setPos(barX + hpBarWidth + MpUi.px(4f), y + MpUi.px(14f));
            add(hpText);
            rowGizmos.add(hpText);

            // 楼层（第二行右侧）
            String floorText = info.depth > 0
                    ? Messages.get(WndPlayerList.class, "floor", info.depth)
                    : Messages.get(WndPlayerList.class, "floor_unknown");
            RenderedTextBlock floor = PixelScene.renderTextBlock(floorText, MpUi.font(6));
            floor.setPos(width - floor.width() - MpUi.px(2f), y + MpUi.px(14f));
            add(floor);
            rowGizmos.add(floor);

            y += MpUi.i(ROW_HEIGHT) + MpUi.px(GAP);
        }

        // 底部提示：点击玩家行查看英雄详情
        RenderedTextBlock hint = PixelScene.renderTextBlock(
                Messages.get(WndPlayerList.class, "click_hint"), MpUi.font(6));
        hint.hardlight(0x808080);
        hint.setPos(0f, y + MpUi.px(2f));
        add(hint);
        rowGizmos.add(hint);

        int newHeight = (int) Math.max(top + MpUi.i(ROW_HEIGHT),
                y + hint.height() + MpUi.px(2f));
        if (newHeight != height) {
            resize((int) Math.ceil(width), newHeight);
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
