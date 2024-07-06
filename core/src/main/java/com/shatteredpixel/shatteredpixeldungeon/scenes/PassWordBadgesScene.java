package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.effects.PasswordBadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndProBadge;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.List;

public class PassWordBadgesScene extends PixelScene {

    @Override
    public void create() {

        super.create();

        Badges.HDEX();
        fadeIn();
        //Music.INSTANCE.play( Assets.Music.THEME, true );

        PixelScene.uiCamera.visible = false;

        int w = Camera.main.width;
        int h = Camera.main.height;

        Archs archs = new Archs();
        archs.setSize( w, h );
        add( archs );

        float left = 5;
        float top = 20;

        RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title"), 9 );
        title.hardlight(Window.TITLE_COLOR);
        title.setPos(
                (w - title.width()) / 2f,
                (top - title.height()) / 2f
        );
        align(title);
        add(title);

        PaswordBadges.loadGlobal();

        List<PaswordBadges.Badge> badges = PaswordBadges.filtered( true );

        int blankBadges = 1;
        blankBadges -= badges.size();
        if (badges.contains(Badges.Badge.ALL_ITEMS_IDENTIFIED))	blankBadges -= 6;
        if (badges.contains(Badges.Badge.YASD)) 				blankBadges -= 5;
        blankBadges = Math.max(0, blankBadges);

        //guarantees a max of 5 rows in landscape, and 8 in portrait, assuming a max of 40 buttons
        int nCols = landscape() ? 7 : 4;
        if (badges.size() + blankBadges > 32 && !landscape())	nCols++;

        int nRows = 1 + (blankBadges + badges.size())/nCols;

        float badgeWidth = (w - 2*left)/nCols;
        float badgeHeight = (h - 2*top)/nRows;

        for (int i = 0; i < badges.size() + blankBadges; i++){
            int row = i / nCols;
            int col = i % nCols;
            PaswordBadges.Badge b = i < badges.size() ? badges.get( i ) : null;
            BadgeButton button = new BadgeButton( b );
            button.setPos(
                    left + col * badgeWidth + (badgeWidth - button.width()) / 2,
                    top + row * badgeHeight + (badgeHeight - button.height()) / 2);
            align(button);
            add( button );
        }

        ExitButton btnExit = new ExitButton();
        btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
        add( btnExit );

        fadeIn();
    }

    @Override
    public void destroy() {

        PaswordBadges.saveGlobal();

        super.destroy();
    }

    @Override
    protected void onBackPressed() {
        ShatteredPixelDungeon.switchNoFade( TitleScene.class );
    }

    private static class BadgeButton extends Button {

        private PaswordBadges.Badge badge;

        private Image icon;

        public BadgeButton( PaswordBadges.Badge badge ) {
            super();

            this.badge = badge;
            active = (badge != null);

            icon = active ? PasswordBadgeBanner.image(badge.image) : new Image( Assets.Interfaces.LOCKED );
            add(icon);

            setSize( icon.width(), icon.height() );
        }

        @Override
        protected void layout() {
            super.layout();

            icon.x = x + (width - icon.width()) / 2;
            icon.y = y + (height - icon.height()) / 2;
        }

        @Override
        public void update() {
            super.update();

            if (Random.Float() < Game.elapsed * 0.1) {
                BadgeBanner.highlight( icon, badge.image );
            }
        }

        @Override
        protected void onClick() {
            Sample.INSTANCE.play( Assets.Sounds.CLICK, 0.7f, 0.7f, 1.2f );
            Game.scene().add( new WndProBadge( badge ) );
        }
    }
}