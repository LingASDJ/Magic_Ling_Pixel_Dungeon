package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.ui.Component;

public class PageWindows extends WndTabbed {

    private static final int WIDTH_P = 122;
    private static final int WIDTH_L = 223;

    private static final int SLIDER_HEIGHT = 24;
    private static final int BTN_HEIGHT = 18;
    private static final float GAP = 2;

    private PageWindowsTab display;

    private static class PageWindowsTab extends Component {

        RenderedTextBlock title;
        ColorBlock sep1;
        CheckBox ClassPage;

        @Override
        protected void createChildren() {
            title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
            title.hardlight(TITLE_COLOR);
            add(title);

            sep1 = new ColorBlock(1, 1, 0xFF000000);
            add(sep1);

            ClassPage = new CheckBox(Messages.get(this, "page_ui")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    SPDSettings.ClassPage(checked());
                }
            };
            ClassPage.checked(SPDSettings.ClassPage());
            add(ClassPage);

        }

        @Override
        protected void layout() {

            float bottom = y;

            title.setPos((width - title.width()) / 2, bottom + GAP);
            sep1.size(width, 1);
            sep1.y = title.bottom() + 2 * GAP;

            bottom = sep1.y + 1;

            ClassPage.setRect(0, bottom + GAP, width, SLIDER_HEIGHT);

            height =ClassPage.bottom();
        }

    }

    public PageWindows() {
        super();

        float height;

        int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

        display = new PageWindows.PageWindowsTab();
        display.setSize(width, 0);
        height = display.height();
        add(display);

        resize(width, (int) Math.ceil(height));

        layoutTabs();
    }
}