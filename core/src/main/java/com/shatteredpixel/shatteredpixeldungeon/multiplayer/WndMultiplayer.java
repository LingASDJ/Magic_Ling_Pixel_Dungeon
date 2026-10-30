package com.shatteredpixel.shatteredpixeldungeon.multiplayer;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTextInput;

public class WndMultiplayer extends Window {

    private static final int WIDTH = 150;
    private static final int BTN_HEIGHT = 18;
    private static final int GAP = 2;

    private RenderedTextBlock status;
    private RenderedTextBlock nameText;

    public WndMultiplayer() {
        float pos = 0f;

        RenderedTextBlock title = PixelScene.renderTextBlock("联机", 9);
        title.hardlight(TITLE_COLOR);
        title.setPos(0f, pos);
        title.maxWidth(WIDTH);
        add(title);
        pos = title.bottom() + 4f;

        status = PixelScene.renderTextBlock(6);
        status.text(Mp.I.status, WIDTH);
        status.setPos(0f, pos);
        add(status);
        pos = status.bottom() + 4f;

        nameText = PixelScene.renderTextBlock(6);
        nameText.text("名字: " + Mp.I.getMyName(), WIDTH);
        nameText.setPos(0f, pos);
        add(nameText);
        pos = nameText.bottom() + 4f;

        RedButton host = new RedButton("开房（我是房主）") {
            @Override
            protected void onClick() {
                Mp.I.host(Mp.DEFAULT_PORT);
                refresh();
            }
        };
        add(host);
        host.setRect(0f, pos, WIDTH, BTN_HEIGHT);
        pos += BTN_HEIGHT + GAP;

        RedButton join = new RedButton("加入房间（输入房主IP）") {
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.scene().addToFront(new WndTextInput(
                        "加入房间", "输入房主的局域网IP，可写成 192.168.1.5 或 192.168.1.5:28771",
                        "", 40, false, "连接", "取消") {
                    @Override
                    public void onSelect(boolean positive, String text) {
                        if (positive) {
                            Mp.I.join(text, Mp.DEFAULT_PORT);
                            refresh();
                        }
                    }
                });
            }
        };
        add(join);
        join.setRect(0f, pos, WIDTH, BTN_HEIGHT);
        pos += BTN_HEIGHT + GAP;

        RedButton stop = new RedButton("断开连接") {
            @Override
            protected void onClick() {
                Mp.I.disconnect();
                refresh();
            }
        };
        add(stop);
        stop.setRect(0f, pos, WIDTH, BTN_HEIGHT);
        pos += BTN_HEIGHT + GAP;

        RedButton setName = new RedButton("设置名字") {
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.scene().addToFront(new WndTextInput(
                        "设置名字", "输入你的联机名字（对方能看到）",
                        Mp.I.getMyName(), 16, false, "确定", "取消") {
                    @Override
                    public void onSelect(boolean positive, String text) {
                        if (positive && text != null && !text.trim().isEmpty()) {
                            Mp.I.setName(text.trim());
                            refresh();
                        }
                    }
                });
            }
        };
        add(setName);
        setName.setRect(0f, pos, WIDTH, BTN_HEIGHT);
        pos += BTN_HEIGHT + GAP;

        RedButton close = new RedButton("关闭") {
            @Override
            protected void onClick() {
                hide();
            }
        };
        add(close);
        close.setRect(0f, pos, WIDTH, BTN_HEIGHT);
        pos += BTN_HEIGHT + 4f;

        RenderedTextBlock credit = PixelScene.renderTextBlock(5);
        credit.text("联机功能开发者: knox", WIDTH);
        credit.alpha(0.6f);
        credit.setPos(0f, pos);
        add(credit);
        pos = credit.bottom() + 2f;

        resize(WIDTH, (int) pos);
    }

    private void refresh() {
        if (status != null) {
            status.text(Mp.I.status, WIDTH);
        }
        if (nameText != null) {
            nameText.text("名字: " + Mp.I.getMyName(), WIDTH);
        }
    }

    @Override
    public void update() {
        super.update();
        refresh();
    }
}
