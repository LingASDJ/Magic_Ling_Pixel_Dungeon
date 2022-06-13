package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

import java.util.ArrayList;

public class BossSettingWindows extends Window {
    private static final int WIDTH = 120;
    private static final int BOX_HEIGHT = 16;
    private static final int TTL_HEIGHT = 12;
    private static final int GAP= 2;

    private ArrayList<CheckBox> cbs;

    public BossSettingWindows(){
        resize(WIDTH, 1);

        RenderedTextBlock rtb = PixelScene.renderTextBlock(Messages.get(this, "title"), TTL_HEIGHT - GAP);
        rtb.setPos(WIDTH/2f - rtb.width()/2, GAP);
        PixelScene.align(rtb);
        rtb.hardlight(0x00FF00);
        add(rtb);

        float pos = TTL_HEIGHT + GAP;
        cbs = new ArrayList<>();
        for(int i = 0; i<5; ++i){
            CheckBox cb = new CheckBox(Messages.get(this, "boss_"+String.valueOf(i+1)));
            cb.setRect(GAP, pos, WIDTH - GAP * 2, BOX_HEIGHT);
            add(cb);
            cbs.add(cb);
            pos += BOX_HEIGHT + GAP;
            cb.checked((Statistics.boss_enhance & (1<<i)) >0);
            cb.enable(Statistics.deepestFloor < (5+i*5));
        }

        resize(WIDTH, (int) Math.floor(pos));
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
