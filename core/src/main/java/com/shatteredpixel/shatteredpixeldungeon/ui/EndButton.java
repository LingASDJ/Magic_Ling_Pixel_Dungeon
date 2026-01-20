package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.FeedBackScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndKeyBindings;
import com.watabou.input.GameAction;

public class EndButton extends IconButton {

    public EndButton() {
        super(Icons.EXIT.get());

        width = 20;
        height = 20;
    }

    @Override
    protected void onClick() {
        super.onClick();
        ShatteredPixelDungeon.switchNoFade(FeedBackScene.class);
    }

    @Override
    public GameAction keyAction() {
        return GameAction.BACK;
    }

    @Override
    protected String hoverText() {
        return Messages.titleCase(Messages.get(WndKeyBindings.class, "back"));
    }
}

