package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;

public class WndShopKing extends Window {
    private static final int WIDTH		= 120;
    private static final int BTN_SIZE	= 32;
    private static final int BTN_GAP	= 5;
    private static final int GAP		= 4;

    public WndShopKing() {
        IconTitle titlebar = new IconTitle();
        titlebar.setRect(0, -4, WIDTH, 0);
        titlebar.icon(new ShopkKingSprite());
        titlebar.label(Messages.get(this, "szo"));
        add(titlebar);
        RenderedTextBlock message = PixelScene.renderTextBlock((Messages.get(this, "ary")), 6);
        message.maxWidth(WIDTH);

        message.setPos(0, titlebar.bottom() + GAP);
        add(message);

        RedButton btnBuy = new RedButton( Messages.get(this, "yes") ) {
            @Override
            protected void onClick() {
                hide();
                InterlevelScene.mode = InterlevelScene.Mode.FRGIRLBOSS;
                Game.switchScene(InterlevelScene.class);
            }
        };
        btnBuy.setRect( (WIDTH - BTN_GAP) / 2 - BTN_SIZE, message.top() + message.height() + BTN_GAP, BTN_SIZE, BTN_SIZE );
        btnBuy.textColor(Window.ANSDO_COLOR);
        add( btnBuy );

        RedButton btnNo = new RedButton( Messages.get(this, "no") ) {
            @Override
            protected void onClick() {
                hide();
                GLog.n( Messages.get(WndShopKing.class, "notbad") );
            }
        };
        btnNo.setRect( btnBuy.right() + BTN_GAP, btnBuy.top(), BTN_SIZE, BTN_SIZE );
        add( btnNo );
        btnNo.textColor(Window.CYELLOW);
        resize( WIDTH, (int)btnNo.bottom() );

    }

}

