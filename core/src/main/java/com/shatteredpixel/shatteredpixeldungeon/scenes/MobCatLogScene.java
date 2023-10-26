package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Camera;

//怪物图鉴 尚未完成
public class MobCatLogScene extends PixelScene {

    @Override
    public void create() {
        super.create();
        uiCamera.visible = false;

        int w = Camera.main.width;
        int h = Camera.main.height;

        Archs archs = new Archs();
        archs.setSize(w,h);
        addToBack(archs);

        ExitButton btnExit = new ExitButton();
        btnExit.setPos( w - btnExit.width(), 0 );
        add( btnExit );

        RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title"), 9);
        title.hardlight(Window.TITLE_COLOR);
        title.setPos(
                (w - title.width()) / 2f,
                (20 - title.height()) / 2f
        );
        align(title);
        add(title);
    }

}
