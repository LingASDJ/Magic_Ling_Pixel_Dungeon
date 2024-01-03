package com.shatteredpixel.shatteredpixeldungeon.scenes;

import static com.shatteredpixel.shatteredpixeldungeon.BGMPlayer.playBGM;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndFeedback;
import com.watabou.input.KeyEvent;
import com.watabou.noosa.Camera;

public class FeedBackScene extends PixelScene {

    private WndFeedback wndFeedback;

    @Override
    public void create() {
        super.create();
       playBGM( Assets.Music.CITY_BOSS_FINALE, true );
        wndFeedback = new WndFeedback(false) {
            @Override
            public void onBackPressed() {
                //super.onBackPressed();
            }
        };
        KeyEvent.removeKeyListener(wndFeedback);
        add(wndFeedback);

        Archs archs = new Archs();
        archs.reversed = true;
        archs.setSize( Camera.main.width, Camera.main.height );
        addToBack( archs );

        fadeIn();
    }

    @Override
    public void onBackPressed() {
        if (wndFeedback != null) {
            wndFeedback.hide();
            wndFeedback = null;
        }
        ShatteredPixelDungeon.switchNoFade(TitleScene.class);
    }

}

