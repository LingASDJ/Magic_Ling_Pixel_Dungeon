package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.ui.Component;

public class ScrollableWindow extends Window {
    private static final int WIDTH_MIN = 180, WIDTH_MAX = 220;

    public ScrollableWindow(String message) {
        int width = WIDTH_MIN;

        RenderedTextBlock text = PixelScene.renderTextBlock(6);
        text.text(message, width);

        while (PixelScene.landscape()
                && text.bottom() > (PixelScene.MIN_HEIGHT_L - 10)
                && width < WIDTH_MAX) {
            text.maxWidth(width += 20);
        }

        int height = (int)text.bottom();
        int maxHeight = (int)(PixelScene.uiCamera.height * 0.9);
        boolean needScrollPane = height > maxHeight;

        if (needScrollPane) height = maxHeight;
        resize((int)text.width(), height);

        if (needScrollPane) {
            Component wrapper = new Component();
            wrapper.setSize(text.width(), text.height());
            ScrollPane sp = new ScrollPane(wrapper);
            add(sp);
            wrapper.add(text);
            text.setPos(0, 0);
            sp.setSize(wrapper.width(), height);
        } else {
            add(text);
        }
    }
}
