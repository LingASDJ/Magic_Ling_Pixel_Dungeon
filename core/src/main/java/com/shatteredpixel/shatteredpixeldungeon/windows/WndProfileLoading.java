package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.multiplayer.MpUi;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

/**
 * 获取他人历史最高分对局时的加载提示；由调用方在数据到达/超时后关闭。
 */
public class WndProfileLoading extends Window {

    private static final int WIDTH = 150;
    private static final int MARGIN = 6;

    public WndProfileLoading() {
        super();

        RenderedTextBlock text = PixelScene.renderTextBlock(
                Messages.get(WndProfileLoading.class, "text"), MpUi.font(8));
        text.maxWidth(MpUi.i(WIDTH) - MpUi.i(MARGIN) * 2);
        text.align(RenderedTextBlock.CENTER_ALIGN);
        text.setPos(MpUi.px(MARGIN), MpUi.px(MARGIN));
        add(text);

        resize(MpUi.i(WIDTH), (int) Math.ceil(text.bottom() + MpUi.px(MARGIN)));
    }
}
