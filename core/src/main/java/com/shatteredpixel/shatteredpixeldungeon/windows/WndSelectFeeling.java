package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.custom.utils.GameAPI;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class WndSelectFeeling extends Window {

    private static final int WIDTH = 120;
    private static final int HEIGHT = 160;
    private static final int GAP = 2;
    private static final int BTN_HEIGHT = 18;

    private GameAPI api;

    public WndSelectFeeling(GameAPI api) {
        super();
        this.api = api;

        resize(WIDTH, HEIGHT);

        float pos = 0;

        // 标题
        RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
        title.hardlight(TITLE_COLOR);
        title.maxWidth(WIDTH);
        title.setPos(0, pos);
        add(title);
        pos += title.height() + GAP;

        // 无特殊氛围
        RedButton noneBtn = new RedButton(Messages.get(this, "none")) {
            @Override
            protected void onClick() {
                api.setCustomLevelFeeling(null);
                hide();
            }
        };
        noneBtn.setRect(0, pos, WIDTH, BTN_HEIGHT);
        add(noneBtn);
        pos += BTN_HEIGHT + GAP;

        // 大型
        RedButton largeBtn = new RedButton(Messages.get(this, "large")) {
            @Override
            protected void onClick() {
                api.setCustomLevelFeeling(Level.Feeling.LARGE);
                hide();
            }
        };
        largeBtn.setRect(0, pos, WIDTH, BTN_HEIGHT);
        add(largeBtn);
        pos += BTN_HEIGHT + GAP;

        // 黑暗
        RedButton darkBtn = new RedButton(Messages.get(this, "dark")) {
            @Override
            protected void onClick() {
                api.setCustomLevelFeeling(Level.Feeling.DARK);
                hide();
            }
        };
        darkBtn.setRect(0, pos, WIDTH, BTN_HEIGHT);
        add(darkBtn);
        pos += BTN_HEIGHT + GAP;

        // 水域
        RedButton waterBtn = new RedButton(Messages.get(this, "water")) {
            @Override
            protected void onClick() {
                api.setCustomLevelFeeling(Level.Feeling.WATER);
                hide();
            }
        };
        waterBtn.setRect(0, pos, WIDTH, BTN_HEIGHT);
        add(waterBtn);
        pos += BTN_HEIGHT + GAP;

        // 草地
        RedButton grassBtn = new RedButton(Messages.get(this, "grass")) {
            @Override
            protected void onClick() {
                api.setCustomLevelFeeling(Level.Feeling.GRASS);
                hide();
            }
        };
        grassBtn.setRect(0, pos, WIDTH, BTN_HEIGHT);
        add(grassBtn);
        pos += BTN_HEIGHT + GAP;

        // 大地形
        RedButton mazeBtn = new RedButton(Messages.get(this, "maze")) {
            @Override
            protected void onClick() {
                api.setCustomLevelFeeling(Level.Feeling.LARGE);
                hide();
            }
        };
        mazeBtn.setRect(0, pos, WIDTH, BTN_HEIGHT);
        add(mazeBtn);
        pos += BTN_HEIGHT + GAP;

        // 超大地形
        RedButton cavesBtn = new RedButton(Messages.get(this, "caves")) {
            @Override
            protected void onClick() {
                api.setCustomLevelFeeling(Level.Feeling.BIGROOMS);
                hide();
            }
        };
        cavesBtn.setRect(0, pos, WIDTH, BTN_HEIGHT);
        add(cavesBtn);
        pos += BTN_HEIGHT + GAP;

        // 密室
        RedButton secretsBtn = new RedButton(Messages.get(this, "secrets")) {
            @Override
            protected void onClick() {
                api.setCustomLevelFeeling(Level.Feeling.SECRETS);
                hide();
            }
        };
        secretsBtn.setRect(0, pos, WIDTH, BTN_HEIGHT);
        add(secretsBtn);
        pos += BTN_HEIGHT + GAP;

        // 陷阱
        RedButton trapsBtn = new RedButton(Messages.get(this, "traps")) {
            @Override
            protected void onClick() {
                api.setCustomLevelFeeling(Level.Feeling.TRAPS);
                hide();
            }
        };
        trapsBtn.setRect(0, pos, WIDTH, BTN_HEIGHT);
        add(trapsBtn);
    }
}
