package com.shatteredpixel.shatteredpixeldungeon.windows;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;

public class WndGoShop extends Window {
    private static final int WIDTH		= 120;
    private static final int BTN_SIZE	= 32;
    private static final int BTN_GAP	= 5;
    private static final int GAP		= 4;

    public WndGoShop(RedButton callback) {
        IconTitle titlebar = new IconTitle();
        titlebar.setRect(0, -4, WIDTH, 0);
        titlebar.icon(new ShopkeeperSprite());
        titlebar.label(Messages.get(this, "szo"));
        add(titlebar);
        RenderedTextBlock message = PixelScene.renderTextBlock(Statistics.fireGirlnoshopping && !Statistics.deadshoppingdied ? Messages.get(this,
                "ots"):(Messages.get(this, "ary")), 6);
        message.maxWidth(WIDTH);

        message.setPos(0, titlebar.bottom() + GAP);
        add(message);

        RedButton btnBuy = new RedButton( Statistics.fireGirlnoshopping && !Statistics.deadshoppingdied ?
                Messages.get(this, "ok"):Messages.get(this
                , "yes") ) {
            @Override
            protected void onClick() {
                hide();
                if(Statistics.deadshoppingdied) {
                    GLog.n(Messages.get(WndGoShop.class, "bad", Dungeon.hero.name()));
                }else if(Statistics.fireGirlnoshopping){
                    //GLog.n(Messages.get(WndGoShop.class, "bad", Dungeon.hero.name()));
                    for (Mob mob : Dungeon.level.mobs) {
                        if (mob instanceof Shopkeeper) {
                            ((Shopkeeper) mob).flee();
                            break;
                        }
                }
                } else {
                    InterlevelScene.mode = InterlevelScene.Mode.EXBOSS;
                    Buff.affect(hero, Cost.class).set((6), 1);
                    Game.switchScene(InterlevelScene.class);
                    //商店抢劫
                    Statistics.fireGirlnoshopping = true;
                }
            }
        };
        btnBuy.setRect( (WIDTH - BTN_GAP) / 2f - BTN_SIZE, message.top() + message.height() + BTN_GAP, BTN_SIZE,
                BTN_SIZE );
        btnBuy.textColor(Window.ANSDO_COLOR);
        add( btnBuy );

        RedButton btnNo = new RedButton( Messages.get(this, "no") ) {
            @Override
            protected void onClick() {
                hide();
                GLog.n( Messages.get(WndGoShop.class, "notbad") );
            }
        };
        btnNo.setRect( btnBuy.right() + BTN_GAP, btnBuy.top(), BTN_SIZE, BTN_SIZE );
        add( btnNo );
        btnNo.textColor(Window.CYELLOW);
        resize( WIDTH, (int)btnNo.bottom() );

        }

    }
