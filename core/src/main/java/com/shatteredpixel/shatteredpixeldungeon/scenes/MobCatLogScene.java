package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
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
    }

}
