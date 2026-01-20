package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.OptionSlider;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

import java.util.ArrayList;

public class BossSettingWindows extends Window {
    private static final int WIDTH = 120;
    private static final int HEIGHT = 100;
    private static final int BOX_HEIGHT = 16;
    private static final int TTL_HEIGHT = 12;
    private static final int GAP= 2;

    private final ArrayList<CheckBox> cbs;

    OptionSlider level1;

    public BossSettingWindows(){
        resize(WIDTH, HEIGHT);

        RenderedTextBlock rtb = PixelScene.renderTextBlock(Messages.get(this, "title"), TTL_HEIGHT - GAP);
        rtb.setPos(WIDTH/2f - rtb.width()/2, GAP);
        PixelScene.align(rtb);
        rtb.hardlight(0xFFFF00);
        add(rtb);
        float pos = TTL_HEIGHT + GAP;
        cbs = new ArrayList<>();

        for(int i = 0; i<5; ++i) {
            int finalI = i;
            CheckBox cb = new CheckBox(Messages.get(this, "boss_" + (finalI + 1))) {
                public void checked(boolean value) {
                    if (checked != value) {
                        checked = value;
                        icon.copy(Icons.get(checked ? Icons.CHECKED : Icons.UNCHECKED));
                    }
                }
            };
            cb.setRect(GAP, pos, WIDTH - GAP * 2, BOX_HEIGHT);


            add(cb);
            cbs.add(cb);
            pos += BOX_HEIGHT + GAP;

            if (i == 0 && Badges.isUnlocked(Badges.Badge.KILL_APPLE) ){
                cb.alpha(0f);
                cb.active=false;
                cb.checked(false);
                cb.visible = false;
            } else if(i==3){
                cb.alpha(0f);
                cb.active=false;
                cb.visible = false;
                cb.checked(false);
            } else if(i == 0 && !Badges.isUnlocked(Badges.Badge.KILL_APPLE) || i == 1 && !Badges.isUnlocked(Badges.Badge.KILL_SM) || i==4 ){
                cb.alpha(0.4f);
                cb.active=false;
                cb.checked(false);
                cb.text(Messages.get(this, "boss_unselect_"+i));
            } else {
                cb.checked((Statistics.boss_enhance & (1<<i)) >0);
                cb.enable(Statistics.deepestFloor < (5+i*5));
            }

        }

        StyledButton button2;
        button2 = new StyledButton(Chrome.Type.RED_BUTTON, SPDSettings.KillDwarf() ? Messages.get(this, "kill_dwn") : Messages.get(this, "un_kill_dwn")) {
            @Override
            protected void onClick() {
                add(new WndMessage(SPDSettings.KillDwarf() ? Messages.get(this, "bos_killinfo") : Messages.get(this, "bos_needkillinfo")));
            }
        };
        button2.setRect(GAP, 68, WIDTH - GAP * 2, BOX_HEIGHT);
        add(button2);

        level1 = new OptionSlider(Messages.get(this, "random"), Messages.get(this, "norf"),
                Messages.get(this, "sync"), 1, 3) {
            @Override
            protected void onChange() {
                SPDSettings.level1boss(getSelectedValue());
             }
            @Override
            public int getTitleTextSize(){
                return 6;
            }

        };
        level1.setRect(GAP, 14, WIDTH - GAP * 2, BOX_HEIGHT);
        level1.setSelectedValue(SPDSettings.level1boss());
        if(Badges.isUnlocked(Badges.Badge.KILL_APPLE)){
            add(level1);
        }

        resize(WIDTH, HEIGHT);
    }



    @Override
    public void onBackPressed() {
        Statistics.boss_enhance = 0;
        for(int i=0, len = cbs.size(); i<len; ++i){
            if(cbs.get(i).checked()) {
                Statistics.boss_enhance += 1<<i;
            }
        }
        super.onBackPressed();
    }

}
