package com.shatteredpixel.shatteredpixeldungeon.multiplayer;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.custom.CollectRankings;
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingGridPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBadge;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBadgeSelect;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndProfileLoading;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndRanking;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTextInput;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Component;
import com.watabou.input.PointerEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 联机玩家个人主页（全屏场景，样式仿原版日志/日志徽章页）。
 *
 * <p>顶部标题 + TOAST 面板 + 底部图标标签；两页：资料、全部徽章。
 * 徽章页使用 {@link ScrollingGridPane} 网格，点击徽章打开原版 {@link WndBadge}。</p>
 */
public class MpProfileScene extends PixelScene {

    private static final int TAB_PROFILE = 0;
    private static final int TAB_BADGES = 1;
    private static final long LOAD_TIMEOUT = 5000L;

    private static int pendingPlayerId = -1;
    private static boolean pendingEditable = false;
    private static Class<? extends PixelScene> returnScene = MpLobbyScene.class;

    /**
     * 打开某玩家的个人主页。
     *
     * @param playerId 玩家 id。
     * @param editable 是否可编辑（仅自己）。
     * @param back 返回场景（大厅/房间）。
     */
    public static void open(int playerId, boolean editable,
                            Class<? extends PixelScene> back) {
        pendingPlayerId = playerId;
        pendingEditable = editable;
        if (back != null) returnScene = back;
        ShatteredPixelDungeon.switchNoFade(MpProfileScene.class);
    }

    private int playerId;
    private boolean editable;
    private Mp.RoomPlayerInfo info;
    private NinePatch panel;
    private float innerX;
    private float innerY;
    private float innerW;
    private float innerH;
    private float tabY;

    private final com.watabou.noosa.Group contentLayer = new com.watabou.noosa.Group();
    private Component page;
    private int tab = TAB_PROFILE;
    private int lastUiVersion = -1;

    private StyledButton btnProfile;
    private StyledButton btnBadges;

    private WndProfileLoading loading;
    private long loadingAt;

    @Override
    public void create() {
        super.create();
        PixelScene.uiCamera.visible = false;

        playerId = pendingPlayerId;
        editable = pendingEditable;
        info = Mp.get().getRoomPlayerProfile(playerId);
        lastUiVersion = Mp.get().getRoomUiVersion();

        int w = Camera.main.width;
        int h = Camera.main.height;

        Archs archs = new Archs();
        archs.setSize(w, h);
        addToBack(archs);

        ExitButton back = new ExitButton() {
            @Override
            protected void onClick() {
                closeProfile();
            }
        };
        back.setPos(0, 0);
        add(back);

        float top = 20;
        String name = info == null || info.name == null || info.name.trim().isEmpty()
                ? Messages.get(Mp.class, "default_player_name") : info.name;
        Image icon;
        if (info != null && info.heroClass != null) {
            icon = new Image(info.heroClass.spritesheet(), 0, 0, 12, 15);
        } else {
            icon = Icons.get(Icons.BADGES);
        }
        IconTitle title = new IconTitle(icon, name);
        title.setSize(200, 0);
        title.setPos((w - title.reqWidth()) / 2f, (top - title.height()) / 2f);
        align(title);
        add(title);

        panel = Chrome.get(Chrome.Type.TOAST);
        int pw = Math.min(w - 16, 216 + panel.marginHor());
        int ph = h - 50 + panel.marginVer();
        panel.size(pw, ph);
        panel.x = (w - pw) / 2f;
        panel.y = top;
        add(panel);
        add(contentLayer);

        innerX = panel.x + panel.marginLeft();
        innerY = panel.y + panel.marginTop();
        innerW = panel.width() - panel.marginHor();
        innerH = panel.height() - panel.marginVer() - 25;

        float btnWidth = pw / 2f;
        tabY = panel.y + ph - 3;

        btnProfile = new StyledButton(Chrome.Type.GREY_BUTTON_TR, "") {
            @Override
            protected void onClick() {
                switchTab(TAB_PROFILE);
            }

            @Override
            protected String hoverText() {
                return Messages.get(MpProfileScene.class, "tab_profile");
            }
        };
        btnProfile.icon(Icons.get(Icons.TALENT));
        add(btnProfile);

        btnBadges = new StyledButton(Chrome.Type.GREY_BUTTON_TR, "") {
            @Override
            protected void onClick() {
                switchTab(TAB_BADGES);
            }

            @Override
            protected String hoverText() {
                return Messages.get(MpProfileScene.class, "tab_badges");
            }
        };
        btnBadges.icon(Icons.get(Icons.BADGES));
        add(btnBadges);

        updateTabButtons(btnWidth);
        buildPage();
        fadeIn();
    }

    @Override
    public void update() {
        super.update();
        if (Mp.get().isInRoom()) {
            Mp.get().tickRoom();
        }
        if (loading != null) {
            if (Mp.get().isProfileRecordLoaded(playerId)) {
                loading.hide();
                loading = null;
                showLoadedRecord();
            } else if (System.currentTimeMillis() - loadingAt >= LOAD_TIMEOUT) {
                loading.hide();
                loading = null;
                Mp.get().cancelProfileRequest(playerId);
                openMessage(Messages.get(MpProfileScene.class, "load_failed"));
            }
        }
        if (Mp.get().getRoomUiVersion() != lastUiVersion) {
            lastUiVersion = Mp.get().getRoomUiVersion();
            info = Mp.get().getRoomPlayerProfile(playerId);
            buildPage();
        }
    }

    @Override
    protected void onBackPressed() {
        closeProfile();
    }

    private void closeProfile() {
        Class<? extends PixelScene> target = returnScene == null ? MpLobbyScene.class : returnScene;
        ShatteredPixelDungeon.switchNoFade(target);
    }

    private void switchTab(int newTab) {
        if (tab == newTab) return;
        tab = newTab;
        updateTabButtons(panel.width() / 2f);
        buildPage();
    }

    private void updateTabButtons(float btnWidth) {
        btnProfile.setRect(panel.x, tabY, btnWidth, tab == TAB_PROFILE ? 25 : 20);
        btnBadges.setRect(panel.x + btnWidth, tabY, btnWidth, tab == TAB_BADGES ? 25 : 20);
        btnProfile.textColor(tab == TAB_PROFILE ? Window.TITLE_COLOR : Window.WHITE);
        btnBadges.textColor(tab == TAB_BADGES ? Window.TITLE_COLOR : Window.WHITE);
    }

    private void buildPage() {
        if (page != null) {
            contentLayer.remove(page);
            page.destroy();
        }
        page = new Component();
        contentLayer.add(page);
        if (tab == TAB_PROFILE) {
            buildProfilePage();
        } else {
            buildBadgesPage();
        }
    }

    private void buildProfilePage() {
        float x = innerX;
        float y = innerY;
        float w = innerW;

        String uuid = info == null ? "" : info.uuid;
        if ((uuid == null || uuid.isEmpty()) && Mp.get().isSelfProfileId(playerId)) {
            uuid = Mp.get().getMyProfile().getUuid();
        }
        if (uuid == null || uuid.isEmpty()) uuid = "—";
        RenderedTextBlock uuidText = PixelScene.renderTextBlock(
                Messages.get(MpProfileScene.class, "uuid", uuid), 6);
        uuidText.hardlight(0x9D9D9D);
        uuidText.maxWidth((int) w);
        uuidText.setPos(x, y);
        page.add(uuidText);
        y = uuidText.bottom() + 8;

        ColorBlock line = new ColorBlock(w, 1, 0x555555);
        line.x = x;
        line.y = y;
        page.add(line);
        y += 9;

        RenderedTextBlock bestLabel = PixelScene.renderTextBlock(
                Messages.get(MpProfileScene.class, "highest_score"), 7);
        bestLabel.hardlight(0xCCCCCC);
        bestLabel.setPos(x, y);
        page.add(bestLabel);

        int score = info == null ? 0 : Math.max(0, info.highestScore);
        RenderedTextBlock bestValue = PixelScene.renderTextBlock(String.valueOf(score), 12);
        bestValue.hardlight(0xFFFF66);
        bestValue.setPos(x, bestLabel.bottom() + 3);
        page.add(bestValue);

        RedButton btnDetails = new RedButton(
                Messages.get(MpProfileScene.class, "details"), 7) {
            @Override
            protected void onClick() {
                openDetails();
            }
        };
        btnDetails.setRect(x + w - 42, y, 42, 16);
        page.add(btnDetails);
        y = Math.max(bestValue.bottom(), btnDetails.bottom()) + 14;

        RenderedTextBlock badgeLabel = PixelScene.renderTextBlock(
                Messages.get(MpProfileScene.class, "badges"), 7);
        badgeLabel.hardlight(0xCCCCCC);
        badgeLabel.setPos(x, y);
        page.add(badgeLabel);
        y = badgeLabel.bottom() + 6;

        if (info == null || info.badges == null || info.badges.isEmpty()) {
            RenderedTextBlock empty = PixelScene.renderTextBlock(
                    Messages.get(MpProfileScene.class, "no_badges"), 6);
            empty.hardlight(0x888888);
            empty.setPos(x, y);
            page.add(empty);
            y = empty.bottom() + 10;
        } else {
            float ix = x;
            for (Badges.Badge badge : info.badges) {
                if (badge == null) continue;
                final Badges.Badge shown = badge;
                Image icon = BadgeBanner.image(badge.image);
                icon.x = ix;
                icon.y = y;
                PixelScene.align(icon);
                page.add(icon);
                PointerArea click = new PointerArea(ix, y, icon.width(), icon.height()) {
                    @Override
                    protected void onClick(PointerEvent event) {
                        Game.scene().addToFront(new WndBadge(shown, true));
                    }
                };
                page.add(click);
                ix += icon.width() + 6;
            }
            y += 16 + 10;
        }

        if (editable) {
            float bw = (w - 6) / 2f;
            RedButton btnName = new RedButton(
                    Messages.get(MpProfileScene.class, "modify_name"), 7) {
                @Override
                protected void onClick() {
                    openRename();
                }
            };
            btnName.setRect(x, y, bw, 20);
            page.add(btnName);

            RedButton btnBadges = new RedButton(
                    Messages.get(MpProfileScene.class, "modify_badges"), 7) {
                @Override
                protected void onClick() {
                    openBadgeSelect();
                }
            };
            btnBadges.setRect(x + bw + 6, y, bw, 20);
            page.add(btnBadges);
        }
    }

    private void buildBadgesPage() {
        List<Badges.Badge> badges = info == null || info.allBadges == null
                ? new ArrayList<>() : info.allBadges;
        if (badges.isEmpty()) {
            RenderedTextBlock empty = PixelScene.renderTextBlock(
                    Messages.get(MpProfileScene.class, "all_badges_empty"), 7);
            empty.hardlight(0x888888);
            empty.maxWidth((int) innerW);
            empty.setPos(innerX, innerY + 4);
            page.add(empty);
            return;
        }

        ScrollingGridPane grid = new ScrollingGridPane();
        grid.setCellSize(20, 20);
        for (Badges.Badge badge : badges) {
            if (badge == null || badge.type == Badges.BadgeType.HIDDEN) continue;
            grid.addItem(new BadgeGridItem(badge));
        }
        // 必须先加入场景树再 setRect：ScrollPane.layout 需要 camera()，无父节点时为 null
        page.add(grid);
        grid.setRect(innerX, innerY, innerW, innerH);
    }

    private void openDetails() {
        if (Mp.get().isSelfProfileId(playerId)) {
            openRanking(MpProfile.bestRecord());
            return;
        }
        if (loading != null) return;

        Mp.get().requestProfileRecord(playerId);
        if (Mp.get().isProfileRecordLoaded(playerId)) {
            showLoadedRecord();
            return;
        }
        loading = new WndProfileLoading();
        loadingAt = System.currentTimeMillis();
        addToFront(loading);
    }

    private void showLoadedRecord() {
        openRanking(Mp.get().getProfileRecord(playerId));
    }

    private void openRanking(Rankings.Record record) {
        if (record == null || record.gameData == null) {
            openMessage(Messages.get(MpProfileScene.class, "no_record"));
            return;
        }
        CollectRankings.INSTANCE.load();
        // 用副本展示，避免 WndRanking 重算并覆盖排行榜原始分数
        Rankings.Record display = record;
        try {
            display = MpProfile.decodeRecord(MpProfile.encodeRecord(record));
        } catch (Throwable ignored) {}
        addToFront(new WndRanking(display));
    }

    private void openRename() {
        addToFront(new WndTextInput(
                Messages.get(MpProfileScene.class, "rename_title"),
                Messages.get(Mp.class, "name_prompt"),
                Mp.get().getMyName(), 16, false,
                Messages.get(Mp.class, "ok"),
                Messages.get(Mp.class, "cancel")) {
            @Override
            public void onSelect(boolean positive, String text) {
                if (positive && text != null && !text.trim().isEmpty()) {
                    Mp.get().setName(text.trim());
                    info = Mp.get().getRoomPlayerProfile(playerId);
                    buildPage();
                }
            }
        });
    }

    private void openBadgeSelect() {
        addToFront(new WndBadgeSelect(
                MpProfile.selectableBadges(),
                Mp.get().getMyProfile().getBadges(),
                () -> {
                    info = Mp.get().getRoomPlayerProfile(playerId);
                    buildPage();
                }));
    }

    private void openMessage(String text) {
        addToFront(new WndMessage(text));
    }

    /** 徽章网格单元格：点击查看原版徽章说明。 */
    private static class BadgeGridItem extends ScrollingGridPane.GridItem {
        private final Badges.Badge badge;

        BadgeGridItem(Badges.Badge badge) {
            super(BadgeBanner.image(badge.image));
            this.badge = badge;
            if (badge.type == Badges.BadgeType.HIDDEN) {
                icon.brightness(0.4f);
            }
        }

        @Override
        public boolean onClick(float x, float y) {
            if (x < left() || x > right() || y < top() || y > bottom()) {
                return false;
            }
            Game.scene().addToFront(new WndBadge(badge, true));
            return true;
        }
    }
}
