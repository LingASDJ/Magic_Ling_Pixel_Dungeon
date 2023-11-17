package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndKeyBindings;

public class ReloadButton extends IconButton{
    public ReloadButton() {
        super(Icons.CHANGES.get());

        width = 20;
        height = 20;
    }

    @Override
    protected void onClick() {
        super.onClick();
        //ShatteredPixelDungeon.switchNoFade(GameNewsScene.class);
    }


    @Override
    protected String hoverText() {
        return Messages.titleCase(Messages.get(WndKeyBindings.class, "update"));
    }
}
