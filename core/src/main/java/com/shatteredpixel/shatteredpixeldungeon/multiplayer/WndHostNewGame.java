package com.shatteredpixel.shatteredpixeldungeon.multiplayer;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

/**
 * 房主进入新档时给访客的确认弹窗。
 *
 * <p>只能点击“确认”关闭：点击窗口外或返回键都不会关闭。</p>
 */
public class WndHostNewGame extends Window {

    private static final int WIDTH = 160;
    private static final int MARGIN = 6;
    private static final int BTN_HEIGHT = 20;

    /**
     * @param onConfirm 点击“确认”后的回调（在渲染线程执行）
     */
    public WndHostNewGame(Runnable onConfirm) {
        super();

        RenderedTextBlock text = PixelScene.renderTextBlock(
                Messages.get(Mp.class, "host_new_game_prompt"), 6);
        text.maxWidth(WIDTH - MARGIN * 2);
        text.setPos(MARGIN, MARGIN);
        add(text);

        RedButton confirm = new RedButton(Messages.get(Mp.class, "confirm")) {
            @Override
            protected void onClick() {
                hide();
                if (onConfirm != null) {
                    onConfirm.run();
                }
            }
        };
        confirm.setRect(MARGIN, text.bottom() + MARGIN,
                WIDTH - MARGIN * 2, BTN_HEIGHT);
        add(confirm);

        resize(WIDTH, (int) (confirm.bottom() + MARGIN));
    }

    @Override
    public void onBackPressed() {
        // 只能点击“确认”关闭：点击外部/返回键不生效
    }
}
