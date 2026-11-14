package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.multiplayer.Mp;
import com.shatteredpixel.shatteredpixeldungeon.multiplayer.MpProfile;
import com.shatteredpixel.shatteredpixeldungeon.multiplayer.MpUi;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 佩戴徽章选择：从已解锁徽章中最多选择 3 枚，确认后保存到本机资料。
 */
public class WndBadgeSelect extends Window {

    private static final int WIDTH = 280;
    private static final int MARGIN = 7;
    private static final int ROW_HEIGHT = 32;
    private static final int GAP = 6;
    private static final int BTN_HEIGHT = 26;
    private static final int LIST_HEIGHT_MIN = 90;
    private static final int LIST_HEIGHT_MAX = 520;
    /** 行尾“#1”标号的固定槽位宽度。 */
    private static final int ORDER_SLOT = 22;
    /** 行与行之间的留白，避免徽章行挤在一起。 */
    private static final int ROW_GAP = 2;

    private static float P(float designPx) {
        return MpUi.px(designPx);
    }

    private static int I(float designPx) {
        return MpUi.i(designPx);
    }

    private static int F(int designSize) {
        return MpUi.font(designSize);
    }

    private final List<Badges.Badge> all;
    private final List<Badges.Badge> selected = new ArrayList<>();
    private final List<Row> rows = new ArrayList<>();
    private final Runnable onChanged;

    private RenderedTextBlock countText;
    private ScrollPane pane;

    /**
     * @param all 可选徽章（已解锁、已过滤替换项）。
     * @param initial 当前已佩戴的徽章。
     * @param onChanged 保存成功后的回调（刷新个人主页）。
     */
    public WndBadgeSelect(List<Badges.Badge> all, List<Badges.Badge> initial,
                          Runnable onChanged) {
        super();
        this.all = all == null ? Collections.emptyList() : all;
        this.onChanged = onChanged;
        if (initial != null) {
            for (Badges.Badge badge : initial) {
                if (badge == null || selected.contains(badge)) continue;
                if (selected.size() >= MpProfile.MAX_BADGES) break;
                selected.add(badge);
            }
        }

        // 竖屏铺满屏幕宽度（即使玩家打开了“桌面 UI 尺寸”选项，也按实际屏幕
        // 长宽判断一次），横屏保持设计宽度。只影响本窗口，不改全局按钮样式。
        float width;
        if (MpUi.screenHeight() > MpUi.screenWidth()) {
            width = Math.max(P(180f),
                    MpUi.screenWidth() - chrome.marginHor() - P(6f));
        } else {
            width = MpUi.windowWidth(WIDTH, chrome.marginHor());
        }
        float pos = I(MARGIN);
        RenderedTextBlock title = PixelScene.renderTextBlock(
                Messages.get(WndBadgeSelect.class, "title"), Math.max(8, F(10)));
        title.hardlight(TITLE_COLOR);
        title.maxWidth((int) (width - I(MARGIN) * 2));
        title.setPos(I(MARGIN), pos);
        add(title);
        pos = title.bottom() + P(3);

        // 必须在测量 paneTop 之前就填入真实文本：空文本高度为 0，
        // 稍后再 setText 会让计数文字向下长到列表区域里（被列表覆盖）。
        countText = PixelScene.renderTextBlock(
                Messages.get(WndBadgeSelect.class, "selected_count",
                        selected.size(), MpProfile.MAX_BADGES), Math.max(7, F(8)));
        countText.hardlight(0xAAAAAA);
        countText.maxWidth((int) (width - I(MARGIN) * 2));
        countText.setPos(I(MARGIN), pos);
        add(countText);
        pos = countText.bottom() + P(6);
        final float paneTop = pos;

        pane = new ScrollPane(new Component()) {
            @Override
            public void onClick(float x, float y) {
                if (wasDragging()) return;
                for (Row row : rows) {
                    if (row.hitTest(x, y)) {
                        toggle(row.badge);
                        return;
                    }
                }
            }
        };
        add(pane);
        // 列表高度按屏幕剩余空间扩展（有上限），避免固定高度导致窗口太小、
        // 一屏只能看到几行徽章；短屏时自动收缩，保证按钮不超出屏幕。
        float listH = Math.max(I(LIST_HEIGHT_MIN),
                Math.min(I(LIST_HEIGHT_MAX),
                        MpUi.screenHeight() - chrome.marginVer() - paneTop
                                - I(BTN_HEIGHT) - P(GAP) - I(MARGIN) - P(10)));
        pane.setRect(I(MARGIN), paneTop, width - I(MARGIN) * 2, listH);
        rebuildRows();
        pos += listH + P(GAP);

        float bw = (width - I(MARGIN) * 2 - P(GAP)) / 2f;
        RedButton btnCancel = new RedButton(
                Messages.get(WndBadgeSelect.class, "cancel"), Math.max(7, F(9))) {
            @Override
            protected void onClick() {
                // 列表滑动结束时手指可能正好落在按钮上，不能把这次抬手当成“取消”
                if (pane.wasDragging()) return;
                hide();
            }
        };
        btnCancel.setRect(I(MARGIN), pos, bw, I(BTN_HEIGHT));
        add(btnCancel);

        RedButton btnConfirm = new RedButton(
                Messages.get(WndBadgeSelect.class, "confirm"), Math.max(7, F(9))) {
            @Override
            protected void onClick() {
                // 同上：列表滑动后的抬手不触发确认
                if (pane.wasDragging()) return;
                Mp.get().getMyProfile().setBadges(new ArrayList<>(selected));
                if (onChanged != null) onChanged.run();
                hide();
            }
        };
        btnConfirm.setRect(I(MARGIN) + bw + P(GAP), pos, bw, I(BTN_HEIGHT));
        add(btnConfirm);

        updateCount();
        resize((int) Math.ceil(width),
                (int) Math.ceil(pos + I(BTN_HEIGHT) + I(MARGIN)));
        // Window.resize 会移动窗口内部 camera；ScrollPane 的独立 content camera
        // 必须在最终窗口位置重新布局一次，否则内容会偏移到窗口外。
        pane.setRect(I(MARGIN), paneTop, width - I(MARGIN) * 2, listH);
    }

    @Override
    public void onBackPressed() {
        // 滑动列表时若指针移出窗口，blocker 也可能判定为“点击窗口外”；
        // 拖动过程中/刚拖动结束时不要关闭窗口。
        if (pane != null && (pane.isDragging() || pane.wasDragging())) return;
        super.onBackPressed();
    }

    private void rebuildRows() {
        Component content = pane.content();
        content.clear();
        rows.clear();

        float width = Math.max(P(60f), pane.width() - P(2f));
        if (all.isEmpty()) {
            RenderedTextBlock empty = PixelScene.renderTextBlock(
                    Messages.get(WndBadgeSelect.class, "empty"), Math.max(7, F(9)));
            empty.hardlight(0x999999);
            empty.maxWidth((int) Math.max(P(20f), width - P(12f)));
            empty.setPos(P(6), P(5));
            content.add(empty);
            content.setSize(width, Math.max(P(1f), empty.bottom() + P(6)));
            return;
        }

        float y = 0f;
        float baseHeight = I(ROW_HEIGHT);
        for (Badges.Badge badge : all) {
            Row row = new Row(badge);
            row.setRect(0, y, width, baseHeight);
            float required = row.requiredHeight(baseHeight);
            if (required > baseHeight) {
                row.setRect(0, y, width, required);
            }
            content.add(row);
            rows.add(row);
            row.refresh(selected.indexOf(badge));
            y += row.height() + P(ROW_GAP);
        }
        content.setSize(width, Math.max(P(1f), y));
    }

    private void toggle(Badges.Badge badge) {
        if (selected.contains(badge)) {
            selected.remove(badge);
        } else if (selected.size() < MpProfile.MAX_BADGES) {
            selected.add(badge);
        } else {
            return;
        }
        for (Row row : rows) {
            row.refresh(selected.indexOf(row.badge));
        }
        updateCount();
    }

    private void updateCount() {
        countText.text(Messages.get(WndBadgeSelect.class, "selected_count",
                selected.size(), MpProfile.MAX_BADGES));
    }

    /** 一行徽章：选中高亮 + 图标 + 名称 + 选中序号。 */
    private static class Row extends Component {
        final Badges.Badge badge;
        private ColorBlock highlight;
        private NinePatch bg;
        private Image icon;
        private RenderedTextBlock label;
        private RenderedTextBlock order;

        Row(Badges.Badge badge) {
            this.badge = badge;

            highlight = new ColorBlock(0, 0, 0x334466);
            highlight.visible = false;
            add(highlight);

            bg = Chrome.get(Chrome.Type.GEM);
            add(bg);

            icon = BadgeBanner.image(badge.image);
            // 保持像素图原尺寸（16×16），不做非整数缩放，避免徽章图标发虚。
            icon.scale.set(1f);
            add(icon);

            label = PixelScene.renderTextBlock(badge.title(), Math.max(7, F(9)));
            add(label);

            order = PixelScene.renderTextBlock("", Math.max(7, F(9)));
            order.hardlight(0xFFFF44);
            add(order);

            refresh(-1);
        }

        void refresh(int index) {
            // 严格只显示前 MAX_BADGES 个标号；未选中的行直接隐藏标号，
            // 避免 RenderedTextBlock.text("") 不重建而残留旧数字。
            boolean show = index >= 0 && index < MpProfile.MAX_BADGES;
            if (highlight != null) highlight.visible = show;
            if (order != null) {
                order.visible = show;
                order.text(show ? "#" + (index + 1) : "");
            }
            layout();
        }

        @Override
        protected void layout() {
            if (highlight != null) {
                highlight.x = x;
                highlight.y = y;
                highlight.size(width, height);
            }
            if (bg != null) {
                bg.x = x;
                bg.y = y;
                bg.size(width, height);
            }
            if (icon != null) {
                icon.x = x + P(6);
                icon.y = y + (height - icon.height()) / 2f;
                PixelScene.align(icon);
            }
            if (label != null) {
                // 预留左侧图标、右侧固定标号槽位；名字较长时自动换行，
                // 行高由 rebuildRows() 按实际内容增高，避免文字被裁掉。
                float iconW = icon == null ? 0 : icon.width();
                float textX = x + P(6) + iconW + P(6);
                label.maxWidth((int) Math.max(P(60f),
                        width - (textX - x) - P(ORDER_SLOT) - P(6f)));
                label.setPos(textX, y + (height - label.height()) / 2f);
            }
            if (order != null && order.visible) {
                order.setPos(x + width - P(6f) - order.width(),
                        y + (height - order.height()) / 2f);
            }
        }

        /** 徽章标题可能换行，返回该行实际需要的高度。 */
        float requiredHeight(float base) {
            return Math.max(base,
                    Math.max(icon.height(), label.height()) + P(10));
        }

        boolean hitTest(float px, float py) {
            return px >= x && px < x + width && py >= y && py < y + height;
        }
    }
}
