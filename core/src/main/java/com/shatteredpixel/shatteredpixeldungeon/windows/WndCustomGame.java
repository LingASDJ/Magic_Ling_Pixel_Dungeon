package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class WndCustomGame extends Window {

    private static final int WIDTH = 120;
    private static final int HEIGHT = 120;
    private static final int BOX_HEIGHT = 16;
    private static final int TTL_HEIGHT = 12;
    private static final int GAP= 2;

    public WndCustomGame(){
        resize(WIDTH, HEIGHT);

        RenderedTextBlock rtb = PixelScene.renderTextBlock(Messages.get(this, "title"), TTL_HEIGHT - GAP);
        rtb.setPos(WIDTH/2f - rtb.width()/2, GAP);
        PixelScene.align(rtb);
        rtb.hardlight(0xFFFF00);
        add(rtb);
        float pos = TTL_HEIGHT + GAP;

        CheckBox curseWeaponAndArmor = new CheckBox(Messages.get(this, "curse_weapon_and_armor")){
            public void checked( boolean value ) {
                if (checked != value) {
                    checked = value;
                    icon.copy( Icons.get( checked ? Icons.CHECKED : Icons.UNCHECKED ) );
                }
            }
        };
        curseWeaponAndArmor.setRect(GAP, pos, WIDTH - GAP * 2, BOX_HEIGHT);
        add(curseWeaponAndArmor);
        pos += BOX_HEIGHT + GAP;


    }
}
