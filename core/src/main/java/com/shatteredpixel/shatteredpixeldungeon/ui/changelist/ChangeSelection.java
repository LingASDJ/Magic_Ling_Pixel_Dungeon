package com.shatteredpixel.shatteredpixeldungeon.ui.changelist;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

public class ChangeSelection extends Component {

    private static final int BTN_HEIGHT	    = 18;

    protected ColorBlock line;

    private RenderedTextBlock title;

    private RedButton btnSelect;

    public ChangeSelection(String title, String text) {
        super();

        this.title = PixelScene.renderTextBlock( title, 9 );
        line = new ColorBlock( 1, 1, 0xFF222222);
        add(line);

        add(this.title);

        this.btnSelect = new RedButton(text) {
            @Override
            protected void onClick() {
                ChangeSelection.this.onClick();
            }
        };
        add(this.btnSelect);

    }

    public Image icon() {
        return btnSelect.icon();
    }

    public void icon(Image icon) {
        btnSelect.icon(icon);
    }

    public void onClick() {

    }

    public void hardlight( int color ){
        title.hardlight( color );
    }

    @Override
    protected void layout() {
        float posY = this.y + 5;

        title.setPos(
                x + (width - title.width()) / 2f,
                posY
        );
        PixelScene.align( title );
        posY += title.height() + 2;

        float posX = x;
        btnSelect.setRect(posX, posY, width(), BTN_HEIGHT);
        PixelScene.align(btnSelect);
        posY += BTN_HEIGHT;

        height = posY - this.y;

        line.size(width(), 1);
        line.x = x;
        line.y = y+2;
    }
}
