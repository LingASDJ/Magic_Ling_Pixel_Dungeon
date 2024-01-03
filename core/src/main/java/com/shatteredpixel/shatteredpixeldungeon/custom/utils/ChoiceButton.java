package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;

public class ChoiceButton extends StyledButton {

    protected NinePatch bg;
    protected RenderedTextBlock text;
    protected Image icon;

    public ChoiceButton( String label ) {
        this(label, 9);
    }

    public ChoiceButton( String label, int size ){
        super( Chrome.Type.TOAST_TR, label, size);
    }

    public void layout()
    {
        super.layout();
    }
}
