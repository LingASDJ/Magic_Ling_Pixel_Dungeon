package com.shatteredpixel.shatteredpixeldungeon.multiplayer;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTextInput;
import com.watabou.noosa.Gizmo;
import com.watabou.noosa.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * 联机控制窗口：图形化显示连接状态、房间号与玩家列表，并提供开房/加入/断开/改名操作。
 */
public class WndMultiplayer extends Window {

    private static final int WIDTH = 150;
    private static final int BTN_H = 16;
    private static final int ROW_H = 16;
    private static final int GAP = 2;

    private final RenderedTextBlock title;
    private final RenderedTextBlock status;
    private final RenderedTextBlock room;
    private final RenderedTextBlock playersLabel;
    private final List<Gizmo> rows = new ArrayList<>();

    private final RedButton hostBtn;
    private final RedButton joinBtn;
    private final StyledButton disconnectBtn;
    private final StyledButton nameBtn;
    private final RedButton closeBtn;
    private final RenderedTextBlock credit;
    private final RenderedTextBlock version;

    private long lastRefresh = 0L;

    public WndMultiplayer() {
        super();

        title = PixelScene.renderTextBlock(Messages.get(WndMultiplayer.class, "title"), 9);
        title.hardlight(TITLE_COLOR);
        title.maxWidth(WIDTH);
        title.setPos(0f, 0f);
        add(title);

        status = PixelScene.renderTextBlock(7);
        status.maxWidth(WIDTH);
        add(status);

        room = PixelScene.renderTextBlock(7);
        room.maxWidth(WIDTH);
        add(room);

        playersLabel = PixelScene.renderTextBlock(6);
        playersLabel.hardlight(0x9D9D9D);
        playersLabel.maxWidth(WIDTH);
        add(playersLabel);

        hostBtn = new RedButton(Messages.get(WndMultiplayer.class, "host_server"), 7) {
            @Override
            protected void onClick() {
                Mp.get().hostViaServer(Mp.defaultServerAddress());
                rebuild();
            }
        };
        add(hostBtn);

        joinBtn = new RedButton(Messages.get(WndMultiplayer.class, "join_server"), 7) {
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.scene().addToFront(new WndTextInput(
                        Messages.get(WndMultiplayer.class, "room_code_title"),
                        Messages.get(WndMultiplayer.class, "room_code_prompt"),
                        "", 6, false,
                        Messages.get(WndMultiplayer.class, "connect"),
                        Messages.get(WndMultiplayer.class, "cancel")) {
                    @Override
                    public void onSelect(boolean positive, String code) {
                        if (positive) {
                            Mp.get().joinViaServer(Mp.defaultServerAddress(), code);
                            rebuild();
                        }
                    }
                });
            }
        };
        add(joinBtn);

        disconnectBtn = new StyledButton(Chrome.Type.RED_BUTTON,
                Messages.get(WndMultiplayer.class, "disconnect"), 6) {
            @Override
            protected void onClick() {
                Mp.get().disconnect();
                rebuild();
            }
        };
        disconnectBtn.textColor(0xDDDDDD);
        add(disconnectBtn);

        nameBtn = new StyledButton(Chrome.Type.RED_BUTTON,
                Messages.get(WndMultiplayer.class, "set_name"), 6) {
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.scene().addToFront(new WndTextInput(
                        Messages.get(WndMultiplayer.class, "set_name"),
                        Messages.get(WndMultiplayer.class, "name_prompt"),
                        Mp.get().getMyName(), 16, false,
                        Messages.get(WndMultiplayer.class, "ok"),
                        Messages.get(WndMultiplayer.class, "cancel")) {
                    @Override
                    public void onSelect(boolean positive, String text) {
                        if (positive && text != null && !text.trim().isEmpty()) {
                            Mp.get().setName(text.trim());
                            rebuild();
                        }
                    }
                });
            }
        };
        nameBtn.textColor(0xDDDDDD);
        add(nameBtn);

        closeBtn = new RedButton(Messages.get(WndMultiplayer.class, "close"), 7) {
            @Override
            protected void onClick() {
                hide();
            }
        };
        add(closeBtn);

        credit = PixelScene.renderTextBlock(Messages.get(WndMultiplayer.class, "credit"), 5);
        credit.alpha(0.6f);
        add(credit);

        version = PixelScene.renderTextBlock(Mp.MP_VERSION, 5);
        version.alpha(0.6f);
        add(version);

        rebuild();
    }

    private void rebuild() {
        for (Gizmo g : rows) {
            remove(g);
        }
        rows.clear();

        boolean connected = Mp.get().connected;
        status.text(Mp.get().status, WIDTH);
        status.hardlight(connected ? 0x7FD07F : 0xBBBBBB);
        status.setPos(0f, title.bottom() + 4f);

        float y = status.bottom() + 4f;

        String code = Mp.get().getRoomCode();
        boolean hasRoom = code != null && !code.isEmpty();
        room.text(hasRoom
                ? Messages.get(WndMultiplayer.class, "room_label", code)
                : Messages.get(WndMultiplayer.class, "room_none"), WIDTH);
        room.hardlight(hasRoom ? 0xFFD700 : 0x808080);
        room.setPos(0f, y);
        y = room.bottom() + 4f;

        List<Mp.PlayerInfo> players = Mp.get().getPlayerList();
        playersLabel.text(Messages.get(WndMultiplayer.class, "players_label",
                players.size(), Mp.MAX_PLAYERS), WIDTH);
        playersLabel.setPos(0f, y);
        y = playersLabel.bottom() + 2f;

        for (Mp.PlayerInfo info : players) {
            if (info.heroClass != null) {
                Image icon = new Image(info.heroClass.spritesheet(), 0, 0, 12, 15);
                icon.setPos(0f, y + 1f);
                add(icon);
                rows.add(icon);
            } else {
                RenderedTextBlock unknown = PixelScene.renderTextBlock("?", 8);
                unknown.setPos(2f, y + 2f);
                add(unknown);
                rows.add(unknown);
            }

            String nameStr = (info.name == null || info.name.isEmpty())
                    ? Messages.get(WndPlayerList.class, "default_name") : info.name;
            if (info.isSelf) {
                nameStr += Messages.get(WndPlayerList.class, "self_suffix");
            }
            RenderedTextBlock name = PixelScene.renderTextBlock(nameStr, 7);
            name.maxWidth(WIDTH - 62);
            name.hardlight(info.isSelf ? 0xFFD700 : 0xFFFFFF);
            name.setPos(16f, y + 1f);
            add(name);
            rows.add(name);

            RenderedTextBlock rtt = PixelScene.renderTextBlock(
                    info.rttMs >= 0 ? info.rttMs + " ms"
                            : Messages.get(WndPlayerList.class, "local"), 6);
            rtt.hardlight(0x9D9D9D);
            rtt.setPos(WIDTH - rtt.width() - 2f, y + 2f);
            add(rtt);
            rows.add(rtt);

            if (info.isHost) {
                RenderedTextBlock hostTag = PixelScene.renderTextBlock(
                        Messages.get(WndPlayerList.class, "host_suffix"), 6);
                hostTag.hardlight(0xFF8844);
                hostTag.setPos(WIDTH - rtt.width() - hostTag.width() - 7f, y + 2f);
                add(hostTag);
                rows.add(hostTag);
            }

            y += ROW_H + 1f;
        }

        if (players.isEmpty()) {
            RenderedTextBlock none = PixelScene.renderTextBlock(
                    Messages.get(WndMultiplayer.class, "no_players"), 6);
            none.hardlight(0x808080);
            none.setPos(0f, y + 2f);
            add(none);
            rows.add(none);
            y += 14f;
        }

        y += 3f;

        float half = (WIDTH - GAP) / 2f;
        hostBtn.setRect(0f, y, half, BTN_H);
        joinBtn.setRect(half + GAP, y, half, BTN_H);
        y += BTN_H + GAP;

        disconnectBtn.setRect(0f, y, half, BTN_H);
        nameBtn.setRect(half + GAP, y, half, BTN_H);
        y += BTN_H + GAP;

        closeBtn.setRect(0f, y, WIDTH, BTN_H);
        y += BTN_H + 4f;

        credit.setPos(0f, y);
        version.setPos(WIDTH - version.width() - 2f, y);
        y += credit.height() + 2f;

        int newHeight = (int) y;
        if (newHeight != height) {
            resize(WIDTH, newHeight);
        }
    }

    @Override
    public void update() {
        super.update();

        long now = System.currentTimeMillis();
        if (now - lastRefresh >= 1000L) {
            lastRefresh = now;
            rebuild();
        }
    }
}
