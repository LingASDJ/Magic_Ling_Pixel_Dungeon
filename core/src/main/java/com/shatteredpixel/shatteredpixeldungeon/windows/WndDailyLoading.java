package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.PinkLingSprite;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.HeroSelectScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.Callback;

/**
 * 每日狩猎请求服务器时的缓冲窗口。
 * 请求进行中不可关闭；超过 20 秒未响应会触发超时回调。
 */
public class WndDailyLoading extends Window {

    private static final int WIDTH = 130;
    private static final long TIMEOUT = 20_000L;

    private final RenderedTextBlock text;
    private final String base;
    private final float textY;
    private final Callback onTimeout;
    private final long startTime;

    private float dotTimer = 0f;
    private int dots = 0;
    private boolean timedOut = false;

    public WndDailyLoading(Callback onTimeout) {
        super();

        this.onTimeout = onTimeout;
        this.startTime = Game.realTime;

        Image icon = new PinkLingSprite();
        icon.x = (WIDTH - icon.width()) / 2f;
        icon.y = 7;
        PixelScene.align(icon);
        add(icon);

        RenderedTextBlock title = PixelScene.renderTextBlock(
                Messages.get(HeroSelectScene.class, "daily"), 9);
        title.hardlight(TITLE_COLOR);
        title.maxWidth(WIDTH - 8);
        title.setPos((WIDTH - title.width()) / 2f, icon.y + icon.height() + 5);
        PixelScene.align(title);
        add(title);

        base = Messages.get(this, "loading");
        text = PixelScene.renderTextBlock(base, 7);
        text.hardlight(0xBBBBBB);
        text.maxWidth(WIDTH - 8);
        textY = title.bottom() + 7;
        text.setPos((WIDTH - text.width()) / 2f, textY);
        PixelScene.align(text);
        add(text);

        RenderedTextBlock hint = PixelScene.renderTextBlock(Messages.get(this, "hint"), 6);
        hint.hardlight(0x777777);
        hint.maxWidth(WIDTH - 12);
        hint.setPos(Math.max(4f, (WIDTH - hint.width()) / 2f), textY + text.height() + 5);
        PixelScene.align(hint);
        add(hint);

        resize(WIDTH, (int) (hint.bottom() + 8));
    }

    @Override
    public void update() {
        super.update();

        if (timedOut) return;

        //超时：关闭自己并通知调用方
        if (Game.realTime - startTime > TIMEOUT) {
            timedOut = true;
            hide();
            if (onTimeout != null) {
                onTimeout.call();
            }
            return;
        }

        //动态省略号，明确表示仍在读取
        dotTimer += Game.elapsed;
        if (dotTimer >= 0.45f) {
            dotTimer = 0f;
            dots = (dots + 1) % 4;

            StringBuilder sb = new StringBuilder(base);
            for (int i = 0; i < dots; i++) {
                sb.append('.');
            }
            text.text(sb.toString());
            text.setPos((WIDTH - text.width()) / 2f, textY);
        }
    }

    @Override
    public void onBackPressed() {
        //服务器请求进行中，不允许关闭
    }
}
