package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Tag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.input.GameAction;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;

public class SkipIndicator extends Tag {

    public static final int COLOR	= 0x00A2E8;

    private Image icon;

    public SkipIndicator() {
        super( 0xFF4C4C );

        setSize( 24, 16 );

        visible = true;

        hotArea.blockLevel = PointerArea.ALWAYS_BLOCK;
    }

    @Override
    protected void createChildren() {
        super.createChildren();

        icon = Icons.STATS.get();
        add( icon );
    }

    @Override
    protected void layout() {
        super.layout();
        icon.x = x;
        icon.y = y;
    }

    @Override
    protected void onClick() {
        if(this.parent instanceof WndDialog)
        {
            ((WndDialog) this.parent).skipText();
        }
    }

    @Override
    public GameAction keyAction(){
        return SPDAction.BACK;
    }

}
