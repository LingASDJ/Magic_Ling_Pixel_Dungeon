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

    private static final int WIDTH = 200;
    private static final int MARGIN = 5;
    private static final int ROW_HEIGHT = 22;
    private static final int GAP = 5;
    private static final int BTN_HEIGHT = 20;
    private static final int LIST_HEIGHT = 168;
    /** 行尾“#1”标号的固定槽位宽度。 */
    private static final int ORDER_SLOT = 18;

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

        float pos = I(MARGIN);
        RenderedTextBlock title = PixelScene.renderTextBlock(
                Messages.get(WndBadgeSelect.class, "title"), F(9));
        title.hardlight(TITLE_COLOR);
        title.maxWidth(I(WIDTH) - I(MARGIN) * 2);
        title.setPos(I(MARGIN), pos);
        add(title);
        pos = title.bottom() + P(2);

        // 必须在测量 paneTop 之前就填入真实文本：空文本高度为 0，
        // 稍后再 setText 会让计数文字向下长到列表区域里（被列表覆盖）。
        countText = PixelScene.renderTextBlock(
                Messages.get(WndBadgeSelect.class, "selected_count",
                        selected.size(), MpProfile.MAX_BADGES), F(7));
        countText.hardlight(0xAAAAAA);
        countText.maxWidth(I(WIDTH) - I(MARGIN) * 2);
        countText.setPos(I(MARGIN), pos);
        add(countText);
        pos = countText.bottom() + P(4);
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
        pane.setRect(I(MARGIN), paneTop, I(WIDTH) - I(MARGIN) * 2, I(LIST_HEIGHT));
        rebuildRows();
        pos += I(LIST_HEIGHT) + P(GAP);

        float bw = (I(WIDTH) - I(MARGIN) * 2 - P(GAP)) / 2f;
        RedButton btnCancel = new RedButton(
                Messages.get(WndBadgeSelect.class, "cancel"), F(8)) {
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
                Messages.get(WndBadgeSelect.class, "confirm"), F(8)) {
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
        resize(I(WIDTH), (int) Math.ceil(pos + I(BTN_HEIGHT) + I(MARGIN)));
        // Window.resize 会移动窗口内部 camera；ScrollPane 的独立 content camera
        // 必须在最终窗口位置重新布局一次，否则内容会偏移到窗口外。
        pane.setRect(I(MARGIN), paneTop, I(WIDTH) - I(MARGIN) * 2, I(LIST_HEIGHT));
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

        float width = Math.max(P(40f), pane.width() - P(2f));
        if (all.isEmpty()) {
            RenderedTextBlock empty = PixelScene.renderTextBlock(
                    Messages.get(WndBadgeSelect.class, "empty"), F(8));
            empty.hardlight(0x999999);
            empty.maxWidth((int) Math.max(P(20f), width - P(12f)));
            empty.setPos(P(6), P(4));
            content.add(empty);
            content.setSize(width, Math.max(P(1f), empty.bottom() + P(6)));
            return;
        }

        float y = 0f;
        for (Badges.Badge badge : all) {
            Row row = new Row(badge);
            row.setRect(0, y, width, I(ROW_HEIGHT));
            content.add(row);
            rows.add(row);
            row.refresh(selected.indexOf(badge));
            y += I(ROW_HEIGHT);
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
            icon.scale.set(MpUi.scale());
            add(icon);

            label = PixelScene.renderTextBlock(badge.title(), F(8));
            label.maxWidth(I(150));
            add(label);

            order = PixelScene.renderTextBlock("", F(8));
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
                icon.x = x + P(3);
                icon.y = y + (height - icon.height()) / 2f;
                PixelScene.align(icon);
            }
            if (label != null) {
                // 预留左侧图标、右侧固定标号槽位，避免 #1/#2 顶到内容边界外
                label.maxWidth((int) Math.max(P(40f),
                        width - P(22f) - P(ORDER_SLOT) - P(4f)));
                label.setPos(x + P(22), y + (height - label.height()) / 2f);
            }
            if (order != null && order.visible) {
                order.setPos(x + width - P(4f) - order.width(),
                        y + (height - order.height()) / 2f);
            }
        }

        boolean hitTest(float px, float py) {
            return px >= x && px < x + width && py >= y && py < y + height;
        }
    }
}
