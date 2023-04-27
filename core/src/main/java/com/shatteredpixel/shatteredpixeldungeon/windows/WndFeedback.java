package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.badlogic.gdx.Gdx;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.JoinIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.utils.DeviceCompat;

public class WndFeedback extends Window {

    protected static final int WIDTH_P    = 122;
    protected static final int WIDTH_L    = 223;
    private static final int BTN_HEIGHT	    = 18;
    private static final float GAP          = 2;

    public WndFeedback(boolean ingame){

        int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

        IconTitle title = new IconTitle(new ItemSprite( ItemSpriteSheet.MAGICGIRLBOOKS ), Messages.get(this, ingame ?
                "title_ingame" : "title"));
        title.setRect( 0, 0, width, 0 );
        add(title);

        String message = Messages.get(this, ingame ? "intro_ingame" : "intro");
        message += "\n\n" + Messages.get(this, "sponsor_msg");
        message += "\n\n" + Messages.get(this, "feedback_msg");
        message += "\n" + Messages.get(this, "feedback_msg_pr");
        message += "\n\n" + Messages.get(this, "thanks");
        message += "\n\n_-_ JDSALing";

        RenderedTextBlock text = PixelScene.renderTextBlock( 6 );
        text.text( message, width );
        text.setPos( title.left(), title.bottom() + 4 );
        add( text );

        ColorBlock sep2 = new ColorBlock(1, 1, 0xFF000000);
        sep2.size(width, 1);
        sep2.y = text.bottom() + GAP;
        add(sep2);

        RedButton btnSponsor = new RedButton(Messages.get(this, "sponsor_link")){
            @Override
            protected void onClick() {
                super.onClick();
                if(DeviceCompat.isAndroid()){
                    //由于安卓架构对于下方的LibGDX退出并非完全退出，仍然可能有进程在后台运行，为此强制抛出错误以达到游戏进程完全停止的目的。
                    throw new IllegalStateException("Died");
                } else {
                    Gdx.app.exit();
                }
            }
        };
        btnSponsor.icon(Icons.get(Icons.COMPASS));
        if (PixelScene.landscape()) {
            btnSponsor.setRect(0, text.bottom() + GAP*3, (width - GAP) * 0.5f, BTN_HEIGHT);
        } else {
            btnSponsor.setRect(0, text.bottom() + GAP*3, width, BTN_HEIGHT);
        }
        add(btnSponsor);

        RedButton btnFeedback = new RedButton(Messages.get(this, "feedback_link")){
            @Override
            protected void onClick() {
                super.onClick();
                ShatteredPixelDungeon.switchNoFade( TitleScene.class );
            }
        };
        btnFeedback.icon(Icons.get(Icons.BUFFS));
        if (PixelScene.landscape()) {
            btnFeedback.setRect(btnSponsor.right() + 2, text.bottom() + GAP*3, (width - 2) * 0.5f, BTN_HEIGHT);
        } else {
            btnFeedback.setRect(0, btnSponsor.bottom() + GAP, width, BTN_HEIGHT);
        }
        add(btnFeedback);

        RedButton btnJoinback = new RedButton(Messages.get(this, "join_link")){
            @Override
            protected void onClick() {
                super.onClick();
                String link = Messages.get(JoinIndicator.class, "link");
                Game.platform.openURI(link);
            }
        };
        btnJoinback.icon(Icons.get(Icons.TALENT));
        if (PixelScene.landscape()) {
            btnJoinback.setRect(btnSponsor.left(), btnSponsor.bottom() + GAP, width, BTN_HEIGHT);
        } else {
            btnJoinback.setRect(0,btnFeedback.bottom() + GAP, width, BTN_HEIGHT);
        }
        add(btnJoinback);

        if (ingame) {
            RedButton btnClose = new RedButton(Messages.get(this, "close")){
                @Override
                protected void onClick() {
                    super.onClick();
                    SPDSettings.supportNagged(true);
                    WndFeedback.super.hide();
                }
            };
            btnClose.setRect(0, btnJoinback.bottom() + GAP, width, BTN_HEIGHT);
            add(btnClose);

            resize(width, (int)btnClose.bottom());
        }
        else resize(width, (int) btnJoinback.bottom());

    }

    @Override
    public void hide() {
        //do nothing, have to close via the close button
    }

}

