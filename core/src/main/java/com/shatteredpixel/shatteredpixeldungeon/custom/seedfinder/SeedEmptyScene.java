package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndChallenges;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.ui.Component;

public class SeedEmptyScene extends PixelScene {

    protected RedButton btnSeed;
    @Override
    public void create() {
        super.create();

        int w = Camera.main.width;
        int h = Camera.main.height;
        final float colWidth = 120;

        Archs archs = new Archs();
        archs.setSize(w, h);
        add(archs);

        add(new ColorBlock(w, h, 0));

        ScrollPane list = new ScrollPane(new Component());
        add(list);

        Component content = list.content();
        content.clear();

        ShatteredPixelDungeon.scene().add(new WndChallenges(SPDSettings.challenges(), true,null) {
            public void onBackPressed() {
                super.onBackPressed();
                ShatteredPixelDungeon.switchNoFade(SeedFinderScene.class);
            }
          }
        );

        btnSeed = new RedButton(Messages.get(SeedFinderScene.class,"bs")){
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.switchNoFade(SeedFinderScene.class);
            }
        };
        btnSeed.icon(Icons.ENTER.get());
        btnSeed.setRect((Camera.main.width - colWidth)/2f+15, h/6f-30, 90, 16);
        add(btnSeed);

    }

    @Override
    protected void onBackPressed() {
        ShatteredPixelDungeon.switchNoFade(SeedFinderScene.class);
    }
}
