package com.shatteredpixel.shatteredpixeldungeon.scenes;


import static com.watabou.noosa.Game.switchScene;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Random;

public class GoScene extends PixelScene {

    @Override
    public void create() {

        if (SPDSettings.splashScreen() < 1) {
            ShatteredPixelDungeon.switchScene(WelcomeScene.class);
            return;
        }

        super.create();
        boolean isChance = Random.Int(10) == 1;
        Music.playModeBGM( isChance ? Assets.Music.GO : Assets.Sounds.ANSDOSHIP,false);
        uiCamera.visible = false;

        int w = Camera.main.width;
        int h = Camera.main.height;

        Image title = new Image(Assets.Splashes.GDX ) {
            private float time = 0;
            @Override
            public void update() {
                super.update();
                am = Math.max(0f, (float)Math.sin( time += Game.elapsed ));
                if (time >= 1.5f*Math.PI) {
                    time = 0;
                    switchScene(GoScene2.class);
                }
            }
        };
        title.scale.set(Math.min(w,h) / title.width/1.5f);
        add( title );
        title.x = (w - title.width()) / 2f;
        title.y = (h - title.height()) / 2f;

        align(title);


        fadeIn();
    }
}