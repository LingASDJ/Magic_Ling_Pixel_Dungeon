package com.shatteredpixel.shatteredpixeldungeon.multiplayer;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTextInput;

/** 联机窗口：开房/加入/断开/名字设置/状态显示。 */
public class WndMultiplayer extends Window {

    private static final int WIDTH = 150;
    private static final int BTN_HEIGHT = 18;
    private static final int GAP = 2;

    private RenderedTextBlock status;
    private RenderedTextBlock nameText;

    /**
     * 联机窗口：开房/加入/断开/名字设置/状态显示。
     */
    public WndMultiplayer() {
        float pos = 0f;

        RenderedTextBlock title = PixelScene.renderTextBlock(
                Messages.get(WndMultiplayer.class, "title"), 9);
        title.hardlight(TITLE_COLOR);
        title.setPos(0f, pos);
        title.maxWidth(WIDTH);
        add(title);
        pos = title.bottom() + 4f;

        status = PixelScene.renderTextBlock(6);
        status.text(Mp.get().status, WIDTH);
        status.setPos(0f, pos);
        add(status);
        pos = status.bottom() + 4f;

        nameText = PixelScene.renderTextBlock(6);
        nameText.text(Messages.get(WndMultiplayer.class, "name_label", Mp.get().getMyName()), WIDTH);
        nameText.setPos(0f, pos);
        add(nameText);
        pos = nameText.bottom() + 4f;

        RedButton host = new RedButton(Messages.get(WndMultiplayer.class, "host")) {
            @Override
            protected void onClick() {
                Mp.get().host(Mp.DEFAULT_PORT);
                refresh();
            }
        };
        add(host);
        host.setRect(0f, pos, WIDTH, BTN_HEIGHT);
        pos += BTN_HEIGHT + GAP;

        RedButton join = new RedButton(Messages.get(WndMultiplayer.class, "join")) {
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.scene().addToFront(new WndTextInput(
                        Messages.get(WndMultiplayer.class, "join_title"),
                        Messages.get(WndMultiplayer.class, "join_prompt"),
                        "", 40, false,
                        Messages.get(WndMultiplayer.class, "connect"),
                        Messages.get(WndMultiplayer.class, "cancel")) {
                    @Override
                    public void onSelect(boolean positive, String text) {
                        if (positive) {
                            Mp.get().join(text, Mp.DEFAULT_PORT);
                            refresh();
                        }
                    }
                });
            }
        };
        add(join);
        join.setRect(0f, pos, WIDTH, BTN_HEIGHT);
        pos += BTN_HEIGHT + GAP;

        RedButton stop = new RedButton(Messages.get(WndMultiplayer.class, "disconnect")) {
            @Override
            protected void onClick() {
                Mp.get().disconnect();
                refresh();
            }
        };
        add(stop);
        stop.setRect(0f, pos, WIDTH, BTN_HEIGHT);
        pos += BTN_HEIGHT + GAP;

        RedButton setName = new RedButton(Messages.get(WndMultiplayer.class, "set_name")) {
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
                            refresh();
                        }
                    }
                });
            }
        };
        add(setName);
        setName.setRect(0f, pos, WIDTH, BTN_HEIGHT);
        pos += BTN_HEIGHT + GAP;

        RedButton close = new RedButton(Messages.get(WndMultiplayer.class, "close")) {
            @Override
            protected void onClick() {
                hide();
            }
        };
        add(close);
        close.setRect(0f, pos, WIDTH, BTN_HEIGHT);
        pos += BTN_HEIGHT + 4f;

        RenderedTextBlock credit = PixelScene.renderTextBlock(5);
        credit.text(Messages.get(WndMultiplayer.class, "credit"), WIDTH);
        credit.alpha(0.6f);
        credit.setPos(0f, pos);
        add(credit);
        pos = credit.bottom() + 2f;

        resize(WIDTH, (int) pos);
    }

    private void refresh() {
        if (status != null) {
            status.text(Mp.get().status, WIDTH);
        }
        if (nameText != null) {
            nameText.text(Messages.get(WndMultiplayer.class, "name_label", Mp.get().getMyName()), WIDTH);
        }
    }

    @Override
    /**
     * 每帧刷新界面状态。
     */
    public void update() {
        super.update();
        refresh();
    }
}
