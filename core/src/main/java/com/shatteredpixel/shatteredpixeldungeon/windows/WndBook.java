package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.items.books.Books;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class WndBook extends Window {

    //only one wnditem can appear at a time
    private static WndBook INSTANCE;

    private static final float GAP	= 2;

    private static final int WIDTH_MIN = 120;


    public WndBook(final Books book ) {

        super();

        if( INSTANCE != null ){
            INSTANCE.hide();
        }
        INSTANCE = this;

        int width = WIDTH_MIN;

        RenderedTextBlock info = PixelScene.renderTextBlock(
                Messages.get(book, "author")+"\n\n"+book.desc, 6 );
        info.maxWidth(width);
        info.hardlight(0xfffffff);

        width += 20;
        info.maxWidth(width);


        IconTitle titlebar = new IconTitle( book );
        titlebar.setRect( 0, 0, width, 0 );
        add( titlebar );

        info.setPos(titlebar.left(), titlebar.bottom() + GAP);
        add( info );

        float y = info.top() + info.height() + GAP;

        resize( width, (int) y);
    }

    @Override
    public void hide() {
        super.hide();
        if (INSTANCE == this){
            INSTANCE = null;
        }
    }
}
