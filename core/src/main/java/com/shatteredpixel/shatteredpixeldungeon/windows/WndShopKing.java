package com.shatteredpixel.shatteredpixeldungeon.windows;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
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
                TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
                if (timeFreeze != null) timeFreeze.disarmPresses();
                Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
                if (timeBubble != null) timeBubble.disarmPresses();
                InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                InterlevelScene.curTransition = new LevelTransition();
                InterlevelScene.curTransition.destDepth = depth;
                InterlevelScene.curTransition.destType = LevelTransition.Type.BRANCH_EXIT;
                InterlevelScene.curTransition.destBranch = 7;
                InterlevelScene.curTransition.type = LevelTransition.Type.BRANCH_EXIT;
                InterlevelScene.curTransition.centerCell = -1;
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

