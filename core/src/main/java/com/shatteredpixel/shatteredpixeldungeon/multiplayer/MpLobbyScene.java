package com.shatteredpixel.shatteredpixeldungeon.multiplayer;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTextInput;
import com.watabou.noosa.Camera;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 联机大厅：左上返回、玩家名、加入房间/设置名字/开房、可滚动的房间按钮列表。
 */
public class MpLobbyScene extends PixelScene {

    private int margin;
    private int btnHeight;
    private int gap;
    private int rowHeight;
    /** 房间列表使用固定合适宽度并居中，不铺满整个游戏窗口。 */
    private float listWidth;

    private RenderedTextBlock nameText;
    private RenderedTextBlock statusText;
    private RenderedTextBlock emptyText;
    private ScrollPane pane;
    private final List<RoomRow> rows = new ArrayList<>();
    private int lastListVersion = -1;
    private String lastListSignature = "";
    private boolean lobbyMessageChecked = false;
    private RedButton btnHost;
    private RedButton btnJoin;
    private RedButton btnProfile;

    @Override
    public void create() {
        super.create();
        PixelScene.uiCamera.visible = false;
        Music.INSTANCE.play(Assets.Music.THEME, true);

        int w = Camera.main.width;
        int h = Camera.main.height;
        margin = MpUi.i(6);
        btnHeight = MpUi.i(24);
        gap = MpUi.i(4);
        rowHeight = MpUi.i(66);
        listWidth = MpUi.px(220f);

        Archs archs = new Archs();
        archs.setSize(w, h);
        addToBack(archs);

        ExitButton back = new ExitButton() {
            @Override
            protected void onClick() {
                leaveLobby();
            }
        };
        back.setPos(0, 0);
        add(back);

        nameText = PixelScene.renderTextBlock(MpUi.font(11));
        nameText.maxWidth(w - margin * 2);
        nameText.setPos(margin, MpUi.px(30));
        add(nameText);

        btnHost = new RedButton(Messages.get(MpLobbyScene.class, "host_room"), MpUi.font(11)) {
            @Override
            protected void onClick() {
                if (Mp.get().role != Mp.Role.NONE) return;
                Mp.get().hostViaServer(Mp.defaultServerAddress());
            }
        };
        add(btnHost);

        btnProfile = new RedButton(Messages.get(MpLobbyScene.class, "profile_settings"), MpUi.font(11)) {
            @Override
            protected void onClick() {
                openProfile();
            }
        };
        add(btnProfile);

        btnJoin = new RedButton(Messages.get(MpLobbyScene.class, "join_room"), MpUi.font(11)) {
            @Override
            protected void onClick() {
                openManualJoin();
            }
        };
        add(btnJoin);

        float y = MpUi.px(2);
        float right = w - margin;
        // 显式宽度：reqWidth() 在首次布局前可能为 0，直接使用会让文字溢出按钮边框
        // 顺序（左→右）：个人设置 / 加入房间 / 开房
        float hostW = MpUi.px(52f);
        float joinW = MpUi.px(82f);
        float profileW = MpUi.px(82f);
        btnHost.setRect(right - hostW, y, hostW, btnHeight);
        right = btnHost.left() - gap;
        btnJoin.setRect(right - joinW, y, joinW, btnHeight);
        right = btnJoin.left() - gap;
        btnProfile.setRect(right - profileW, y, profileW, btnHeight);

        float listTop = Math.max(MpUi.px(72f), nameText.bottom() + MpUi.px(10f));
        float listW = Math.min(listWidth, w - MpUi.px(24f));
        float listX = (w - listW) / 2f;
        NinePatch panel = Chrome.get(Chrome.Type.WINDOW);
        panel.size(listW, h - listTop - MpUi.px(26f));
        panel.x = listX;
        panel.y = listTop;
        add(panel);

        RenderedTextBlock listTitle = PixelScene.renderTextBlock(MpUi.font(11));
        listTitle.text(Messages.get(MpLobbyScene.class, "room_list_title"));
        listTitle.setPos(0, 0);   // 触发 layout 计算 width/height
        listTitle.setPos(listX + (listW - listTitle.width()) / 2f, panel.y + MpUi.px(7));
        align(listTitle);
        add(listTitle);

        pane = new ScrollPane(new Component()) {
            @Override
            public void onClick(float x, float y) {
                if (wasDragging()) return;
                for (RoomRow row : rows) {
                    if (row.hitTest(x, y)) {
                        if (row.info.locked) {
                            openPasswordPrompt(row.info.code);
                        } else {
                            joinRoom(row.info.code, "");
                        }
                        return;
                    }
                }
            }
        };
        add(pane);
        float paneTop = listTitle.bottom() + MpUi.px(7);
        pane.setRect(panel.x + MpUi.px(5), paneTop, panel.width - MpUi.px(10),
                panel.y + panel.height - paneTop - MpUi.px(5));

        emptyText = PixelScene.renderTextBlock(MpUi.font(10));
        emptyText.maxWidth((int) pane.width());
        emptyText.setPos(pane.x, pane.y + MpUi.px(4));
        add(emptyText);

        statusText = PixelScene.renderTextBlock(MpUi.font(9));
        statusText.maxWidth(w - margin * 2);
        statusText.setPos(margin, h - MpUi.px(22));
        statusText.alpha(0.7f);
        add(statusText);

        RenderedTextBlock version = PixelScene.renderTextBlock(MpUi.font(6));
        version.text(Mp.MP_VERSION);
        version.alpha(0.6f);
        version.setPos(0, 0);   // 触发 layout 计算 width/height
        version.setPos(w - version.width() - margin, h - version.height() - MpUi.px(2));
        add(version);

        refreshName();
        refreshStatus();
        fadeIn();
    }

    @Override
    public void update() {
        super.update();
        if (!lobbyMessageChecked) {
            lobbyMessageChecked = true;
            String pending = Mp.get().consumeLobbyMessage();
            if (pending != null) {
                addToFront(new WndMessage(pending));
            }
        }
        if (Mp.get().isInRoom()) {
            ShatteredPixelDungeon.switchNoFade(MpRoomScene.class);
            return;
        }
        refreshName();
        refreshStatus();
        if (Mp.get().role == Mp.Role.NONE) {
            Mp.get().requestRoomList();
            if (Mp.get().getRoomListVersion() != lastListVersion) {
                lastListVersion = Mp.get().getRoomListVersion();
                rebuildList();
            }
        }
    }

    @Override
    protected void onBackPressed() {
        leaveLobby();
    }

    private void leaveLobby() {
        if (Mp.get().isInRoom()) {
            // 已有房间时不要误关房间，回房间界面（房间返回键才是关闭房间）
            ShatteredPixelDungeon.switchNoFade(MpRoomScene.class);
            return;
        }
        if (Mp.get().role != Mp.Role.NONE) {
            Mp.get().disconnect();
        }
        ShatteredPixelDungeon.switchNoFade(TitleScene.class);
    }

    private void refreshName() {
        if (nameText == null) return;
        String text = Messages.get(MpLobbyScene.class, "name_label", Mp.get().getMyName());
        if (!text.equals(nameText.text())) {
            nameText.text(text);
        }
    }

    private void refreshStatus() {
        if (statusText == null) return;
        String text = Mp.get().status;
        if (text != null && !text.equals(statusText.text())) {
            statusText.text(text);
        }
    }

    private void openProfile() {
        MpProfileScene.open(Mp.get().getLocalProfileInfo().id, true, MpLobbyScene.class);
    }

    private void openManualJoin() {
        if (Mp.get().role != Mp.Role.NONE) return;
        ShatteredPixelDungeon.scene().addToFront(new WndTextInput(
                Messages.get(MpLobbyScene.class, "room_code_title"),
                Messages.get(MpLobbyScene.class, "room_code_prompt"),
                "", 6, false,
                Messages.get(MpLobbyScene.class, "connect"),
                Messages.get(Mp.class, "cancel")) {
            @Override
            public void onSelect(boolean positive, String text) {
                if (!positive || text == null) return;
                String code = text.trim();
                if (!code.matches("\\d{6}")) {
                    ShatteredPixelDungeon.scene().addToFront(new WndMessage(
                            Messages.get(MpLobbyScene.class, "room_code_invalid")));
                    return;
                }
                openPasswordPrompt(code);
            }
        });
    }

    /** 输入房间密码（可留空）；锁定房间用。 */
    private void openPasswordPrompt(String code) {
        ShatteredPixelDungeon.scene().addToFront(new WndTextInput(
                Messages.get(MpLobbyScene.class, "password_title"),
                Messages.get(MpLobbyScene.class, "password_prompt"),
                "", 32, false,
                Messages.get(MpLobbyScene.class, "connect"),
                Messages.get(Mp.class, "cancel")) {
            @Override
            public void onSelect(boolean positive, String text) {
                if (!positive) return;
                joinRoom(code, text == null ? "" : text);
            }
        });
    }

    private void joinRoom(String code, String password) {
        if (Mp.get().role != Mp.Role.NONE) return;
        Mp.get().joinViaServer(Mp.defaultServerAddress(), code, password);
        ShatteredPixelDungeon.switchNoFade(MpRoomScene.class);
    }

    private void rebuildList() {
        if (pane == null) return;
        List<Mp.RoomListing> list = Mp.get().getRoomList();
        String signature = signature(list);
        if (signature.equals(lastListSignature)) {
            refreshEmptyText(list);
            return;
        }
        lastListSignature = signature;

        Component content = pane.content();
        float scroll = content.camera.scroll.y;
        content.clear();
        rows.clear();

        float width = Math.max(MpUi.px(40f), pane.width() - MpUi.px(2f));
        float y = 0f;
        for (Mp.RoomListing info : list) {
            RoomRow row = new RoomRow(info);
            row.setRect(0, y, width, rowHeight);
            content.add(row);
            rows.add(row);
            y += rowHeight + gap;
        }
        content.setSize(width, Math.max(y, MpUi.px(1f)));
        if (scroll > 0f) {
            pane.scrollTo(0, Math.min(scroll, Math.max(0f, y - pane.height())));
        }
        refreshEmptyText(list);
    }

    private void refreshEmptyText(List<Mp.RoomListing> list) {
        if (emptyText == null) return;
        if (!list.isEmpty()) {
            emptyText.visible = false;
            return;
        }
        String error = Mp.get().getRoomListError();
        if (error != null && !error.isEmpty()) {
            emptyText.text(error);
        } else if (Mp.get().isRoomListLoading()) {
            emptyText.text(Messages.get(MpLobbyScene.class, "room_list_loading"));
        } else {
            emptyText.text(Messages.get(MpLobbyScene.class, "room_list_empty"));
        }
        emptyText.visible = true;
        emptyText.setPos(pane.x + MpUi.px(4), pane.y + MpUi.px(4));
        emptyText.maxWidth((int) Math.max(MpUi.px(20f), pane.width() - MpUi.px(8f)));
    }

    private static String signature(List<Mp.RoomListing> list) {
        StringBuilder sb = new StringBuilder();
        for (Mp.RoomListing r : list) {
            sb.append(r.code).append('|').append(r.players).append('|')
                    .append(r.maxPlayers).append('|').append(r.hostName).append('\n');
        }
        return sb.toString();
    }

    /** 一行房间按钮：房间号 + 房主名 + 人数。点击由 ScrollPane 命中检测转发。 */
    private static class RoomRow extends Component {
        final Mp.RoomListing info;
        private NinePatch bg;
        private RenderedTextBlock code;
        private RenderedTextBlock host;
        private RenderedTextBlock players;

        RoomRow(Mp.RoomListing info) {
            this.info = info;
            // Component 的父类构造会先调用 createChildren()，此时 info 尚未赋值；
            // 因此在构造体里填充文本。
            code.text(Messages.get(MpLobbyScene.class, "room_code_label", info.code)
                    + (info.locked ? "  " + Messages.get(MpLobbyScene.class, "locked_tag") : ""));
            host.text(Messages.get(MpLobbyScene.class, "room_host_label", info.hostName));
            players.text(Messages.get(MpLobbyScene.class, "room_players_label",
                    info.players, info.maxPlayers));
        }

        @Override
        protected void createChildren() {
            bg = Chrome.get(Chrome.Type.GEM);
            add(bg);

            code = PixelScene.renderTextBlock(MpUi.font(12));
            add(code);

            host = PixelScene.renderTextBlock(MpUi.font(10));
            add(host);

            players = PixelScene.renderTextBlock(MpUi.font(10));
            add(players);
        }

        @Override
        protected void layout() {
            bg.x = x;
            bg.y = y;
            bg.size(width, height);

            // 先触发各文本的 layout，按内容总高度在行框内垂直居中
            code.setPos(0, 0);
            players.setPos(0, 0);
            host.maxWidth((int) Math.max(MpUi.px(20f), width - MpUi.px(16f)));
            host.setPos(0, 0);
            float groupHeight = code.height() + MpUi.px(4f) + host.height();
            float top = y + Math.max(MpUi.px(2f), (height - groupHeight) / 2f);
            code.setPos(x + MpUi.px(8f), top);
            host.setPos(x + MpUi.px(8f), code.bottom() + MpUi.px(4f));
            players.setPos(x + width - players.width() - MpUi.px(8f),
                    top + (code.height() - players.height()) / 2f);
        }

        boolean hitTest(float px, float py) {
            return px >= x && px < x + width && py >= y && py < y + height;
        }
    }
}
