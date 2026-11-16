package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.custom.CollectRankings;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.multiplayer.Mp;
import com.shatteredpixel.shatteredpixeldungeon.multiplayer.MpUi;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSkins;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.Scene;
import com.watabou.noosa.ui.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 多人缓存管理：按“房主世界”列出访客缓存，显示英雄、创建时间、房主名。
 *
 * <p>玩家可以逐条删除；当前世界/房间选中种子会标记为“当前世界”且不可删除。
 * 缓存元数据保存在 mp_hero_&lt;seed&gt;.dat 顶层；旧缓存没有元数据时
 * 回退为英雄职业和文件修改时间。</p>
 */
public class WndMpCache extends Window {

    private static final int WIDTH = 300;
    private static final int MARGIN = 7;
    private static final int GAP = 6;
    private static final int ROW_HEIGHT = 58;
    private static final int ROW_GAP = 3;
    private static final int BTN_HEIGHT = 26;
    private static final int LIST_HEIGHT_MIN = 90;
    private static final int LIST_HEIGHT_MAX = 520;

    private static float P(float designPx) {
        return MpUi.px(designPx);
    }

    private static int I(float designPx) {
        return MpUi.i(designPx);
    }

    private static int F(int designSize) {
        return MpUi.font(designSize);
    }

    private final List<Mp.GuestCacheInfo> entries = new ArrayList<>();
    private final List<Row> rows = new ArrayList<>();
    private ScrollPane pane;
    private RenderedTextBlock countText;

    public WndMpCache() {
        super();

        float width;
        if (MpUi.screenHeight() > MpUi.screenWidth()) {
            width = Math.max(P(180f),
                    MpUi.screenWidth() - chrome.marginHor() - P(6f));
        } else {
            width = MpUi.windowWidth(WIDTH, chrome.marginHor());
        }

        float pos = I(MARGIN);
        RenderedTextBlock title = PixelScene.renderTextBlock(
                Messages.get(WndMpCache.class, "title"), Math.max(8, F(10)));
        title.hardlight(TITLE_COLOR);
        title.maxWidth((int) (width - I(MARGIN) * 2));
        title.setPos(I(MARGIN), pos);
        add(title);
        pos = title.bottom() + P(2);

        RenderedTextBlock subtitle = PixelScene.renderTextBlock(
                Messages.get(WndMpCache.class, "subtitle"), Math.max(7, F(8)));
        subtitle.hardlight(0xAAAAAA);
        subtitle.maxWidth((int) (width - I(MARGIN) * 2));
        subtitle.setPos(I(MARGIN), pos);
        add(subtitle);
        pos = subtitle.bottom() + P(5);

        // 必须先填充真实条目和计数文本，再测量列表起点；空文本高度为 0，
        // 稍后 setText 会让计数文字向下长进列表，表现为列表挡住介绍文本。
        entries.addAll(Mp.get().listGuestCaches());
        countText = PixelScene.renderTextBlock(
                Messages.get(WndMpCache.class, "count", entries.size()), Math.max(7, F(8)));
        countText.hardlight(0x999999);
        countText.maxWidth((int) (width - I(MARGIN) * 2));
        countText.setPos(I(MARGIN), pos);
        add(countText);
        pos = countText.bottom() + P(8);

        final float paneTop = pos;
        pane = new ScrollPane(new Component()) {
            @Override
            public void onClick(float x, float y) {
                if (wasDragging()) return;
                for (Row row : rows) {
                    if (row.hitTest(x, y)) {
                        openDetail(row.info);
                        return;
                    }
                }
            }
        };
        add(pane);
        float listH = Math.max(I(LIST_HEIGHT_MIN),
                Math.min(I(LIST_HEIGHT_MAX),
                        MpUi.windowHeightLimit(chrome.marginVer()) - paneTop
                                - I(BTN_HEIGHT) - P(GAP) - I(MARGIN) - P(10)));
        pane.setRect(I(MARGIN), paneTop, width - I(MARGIN) * 2, listH);
        rebuildRows();
        pos += listH + P(GAP);

        RedButton close = new RedButton(
                Messages.get(WndMpCache.class, "close"), Math.max(7, F(9))) {
            @Override
            protected void onClick() {
                if (pane != null && pane.wasDragging()) return;
                hide();
            }
        };
        close.setRect(I(MARGIN), pos, width - I(MARGIN) * 2, I(BTN_HEIGHT));
        add(close);

        resize((int) Math.ceil(width),
                (int) Math.ceil(pos + I(BTN_HEIGHT) + I(MARGIN)));
        // Window.resize 后重新布局一次，避免 ScrollPane 的内容相机偏移。
        pane.setRect(I(MARGIN), paneTop, width - I(MARGIN) * 2, listH);
    }

    @Override
    public void onBackPressed() {
        if (pane != null && (pane.isDragging() || pane.wasDragging())) return;
        super.onBackPressed();
    }

    /** 重新扫描缓存并刷新列表。 */
    private void reload() {
        entries.clear();
        entries.addAll(Mp.get().listGuestCaches());
        if (countText != null) {
            countText.text(Messages.get(WndMpCache.class, "count", entries.size()));
        }
        rebuildRows();
    }

    private void rebuildRows() {
        Component content = pane.content();
        content.clear();
        rows.clear();

        float width = Math.max(P(60f), pane.width() - P(2f));
        if (entries.isEmpty()) {
            RenderedTextBlock empty = PixelScene.renderTextBlock(
                    Messages.get(WndMpCache.class, "empty"), Math.max(7, F(9)));
            empty.hardlight(0x999999);
            empty.maxWidth((int) Math.max(P(20f), width - P(12f)));
            empty.setPos(P(6), P(5));
            content.add(empty);
            content.setSize(width, Math.max(P(1f), empty.bottom() + P(6)));
            return;
        }

        float y = 0f;
        for (Mp.GuestCacheInfo info : entries) {
            Row row = new Row(info);
            row.setRect(0, y, width, I(ROW_HEIGHT));
            float required = row.requiredHeight(I(ROW_HEIGHT));
            if (required > I(ROW_HEIGHT)) row.setRect(0, y, width, required);
            content.add(row);
            rows.add(row);
            y += row.height() + P(ROW_GAP);
        }
        content.setSize(width, Math.max(P(1f), y));
    }

    private void confirmDelete(final Mp.GuestCacheInfo info) {
        if (info.current) return;
        final Scene scene = ShatteredPixelDungeon.scene();
        if (scene == null) return;
        scene.addToFront(new WndOptions(
                Icons.get(Icons.WARNING),
                Messages.get(WndMpCache.class, "delete_title"),
                Messages.get(WndMpCache.class, "delete_body",
                        heroTitle(info), hostTitle(info)),
                Messages.get(WndMpCache.class, "delete_confirm"),
                Messages.get(WndMpCache.class, "cancel")) {
            @Override
            protected void onSelect(int index) {
                if (index != 0) return;
                int removed = Mp.get().deleteGuestCache(info.seed);
                Scene s = ShatteredPixelDungeon.scene();
                if (s == null) return;
                if (removed > 0) {
                    reload();
                    s.addToFront(new WndMessage(
                            Messages.get(WndMpCache.class, "deleted", removed)));
                } else {
                    s.addToFront(new WndMessage(
                            Messages.get(WndMpCache.class, "delete_failed")));
                }
            }
        });
    }

    /** 打开与排行榜相同的英雄详情（统计/天赋/物品/徽章/挑战）。 */
    private void openDetail(Mp.GuestCacheInfo info) {
        Scene scene = ShatteredPixelDungeon.scene();
        if (scene == null) return;
        // WndRanking 会临时替换 Dungeon/Statistics 全局状态；对局中打开会
        // 清掉 Actor/Notes 且无法完整恢复，因此只允许在对局外查看。
        if (scene instanceof GameScene) {
            scene.addToFront(new WndMessage(
                    Messages.get(WndMpCache.class, "detail_in_game")));
            return;
        }
        Rankings.Record rec = Mp.get().buildGuestCacheRecord(info);
        if (rec == null) {
            scene.addToFront(new WndMessage(
                    Messages.get(WndMpCache.class, "detail_failed")));
            return;
        }
        try {
            CollectRankings.INSTANCE.load();
            scene.addToFront(new WndRanking(rec));
        } catch (Throwable t) {
            scene.addToFront(new WndMessage(
                    Messages.get(WndMpCache.class, "detail_failed")));
        }
    }

    private static String heroTitle(Mp.GuestCacheInfo info) {
        if (info.heroClass == null) {
            return Messages.get(WndMpCache.class, "unknown_hero");
        }
        return Messages.titleCase(info.heroClass.title());
    }

    private static String hostTitle(Mp.GuestCacheInfo info) {
        if (info.hostName == null || info.hostName.trim().isEmpty()) {
            return Messages.get(WndMpCache.class, "unknown_host");
        }
        return info.hostName.trim();
    }

    private static String timeTitle(long millis) {
        if (millis <= 0) return "-";
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                    .format(new Date(millis));
        } catch (Throwable t) {
            return "-";
        }
    }

    /** 一行缓存：英雄头像 + 英雄/房主/创建时间 + 删除按钮（或“当前世界”标记）。 */
    private class Row extends Component {
        private final Mp.GuestCacheInfo info;
        private NinePatch bg;
        private Image icon;
        private RenderedTextBlock heroLine;
        private RenderedTextBlock hostLine;
        private RenderedTextBlock createdLine;
        private RenderedTextBlock currentTag;
        private RedButton btnDelete;

        Row(Mp.GuestCacheInfo info) {
            this.info = info;
            heroLine.text(Messages.get(WndMpCache.class, "hero_line", heroTitle(info)));
            hostLine.text(Messages.get(WndMpCache.class, "host_line", hostTitle(info)));
            createdLine.text(Messages.get(WndMpCache.class, "created_line",
                    timeTitle(info.createdAt)));
            if (info.current) {
                currentTag.text(Messages.get(WndMpCache.class, "current"));
                currentTag.hardlight(TITLE_COLOR);
            } else {
                btnDelete = new RedButton(
                        Messages.get(WndMpCache.class, "delete"), Math.max(7, F(9))) {
                    @Override
                    protected void onClick() {
                        if (pane != null && pane.wasDragging()) return;
                        confirmDelete(Row.this.info);
                    }
                };
                add(btnDelete);
            }
            // Component 构造会先调用 createChildren()，那时 info 还未赋值；
            // 头像必须在构造体里按真实职业创建，不能放在 createChildren()。
            HeroClass cls = info.heroClass;
            if (cls != null) {
                icon = HeroSkins.listAvatar(cls, -1, -1);
                if (icon == null) icon = Icons.get(cls);
            }
            if (icon == null) icon = Icons.get(Icons.SKULL);
            float iconFit = Math.min(P(20f) / Math.max(1f, icon.width()),
                    P(22f) / Math.max(1f, icon.height()));
            icon.scale.set(iconFit);
            add(icon);
        }

        @Override
        protected void createChildren() {
            bg = Chrome.get(Chrome.Type.GEM);
            add(bg);

            heroLine = PixelScene.renderTextBlock("", Math.max(7, F(9)));
            add(heroLine);
            hostLine = PixelScene.renderTextBlock("", Math.max(7, F(8)));
            hostLine.hardlight(0xCCCCCC);
            add(hostLine);
            createdLine = PixelScene.renderTextBlock("", Math.max(7, F(7)));
            createdLine.hardlight(0x999999);
            add(createdLine);
            currentTag = PixelScene.renderTextBlock("", Math.max(7, F(8)));
            add(currentTag);
        }

        @Override
        protected void layout() {
            if (bg != null) {
                bg.x = x;
                bg.y = y;
                bg.size(width, height);
            }

            float iconW = 0f;
            if (icon != null) {
                icon.x = x + P(8);
                icon.y = y + (height - icon.height()) / 2f;
                PixelScene.align(icon);
                iconW = icon.width();
            }

            float right = x + width - P(8);
            float slotW = P(72f);
            float textX = x + P(8) + iconW + P(8);
            float textW = Math.max(P(40f), right - slotW - textX);
            heroLine.maxWidth((int) textW);
            hostLine.maxWidth((int) textW);
            createdLine.maxWidth((int) textW);

            float top = y + P(7);
            heroLine.setPos(textX, top);
            hostLine.setPos(textX, heroLine.bottom() + P(2));
            createdLine.setPos(textX, hostLine.bottom() + P(2));

            if (btnDelete != null) {
                float bw = P(52f);
                float bh = I(22f);
                btnDelete.setRect(right - bw, y + (height - bh) / 2f, bw, bh);
            } else if (currentTag != null) {
                currentTag.maxWidth((int) slotW);
                currentTag.setPos(0, 0);
                currentTag.setPos(right - currentTag.width(),
                        y + (height - currentTag.height()) / 2f);
            }
        }

        /** 文字换行后需要的行高。 */
        float requiredHeight(float base) {
            float textH = heroLine.height() + hostLine.height() + createdLine.height() + P(4);
            return Math.max(base, textH + P(14));
        }

        boolean hitTest(float px, float py) {
            return px >= x && px < x + width && py >= y && py < y + height;
        }
    }
}
