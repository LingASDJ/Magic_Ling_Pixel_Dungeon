package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.watabou.noosa.Camera;

public class EXJournalScene extends JournalScene {

    public void create(){
        super.create();

        ExitButton btnExit = new ExitButton() {
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.switchNoFade(SeedFinderScene.class);
                System.gc();
            }
        };
        btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
        add( btnExit );
    }

    @Override
    protected void onBackPressed() {
        ShatteredPixelDungeon.switchNoFade( SeedFinderScene.class );
    }
}
