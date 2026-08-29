package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndError;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Component;

public class EulaScene extends PixelScene {

    @Override
    public void create() {
        super.create();

        final float colWidth = 120;
        final float fullWidth = colWidth * (landscape() ? 2 : 1);

        int w = Camera.main.width;
        int h = Camera.main.height;

        Archs archs = new Archs();
        archs.setSize( w, h );
        add( archs );

        //darkens the arches
        add(new ColorBlock(w, h, 0x88000000));

        ScrollPane list = new ScrollPane( new Component() );
        add( list );

        Component content = list.content();
        content.clear();

        //*** Shattered Pixel Dungeon Credits ***

        CreditsBlock shpx = new CreditsBlock(false, Window.TITLE_COLOR,
                Messages.get(this,"title"),
                null,
                Messages.get(this,"desc"),
                null,
                null);
        if (landscape()){
            shpx.setRect((w - fullWidth)/2f - 6, 10, fullWidth, 0);
        } else {
            shpx.setRect((w - fullWidth)/2f, 6, fullWidth, 0);
        }
        content.add(shpx);

        RedButton acceptButton = new RedButton(Messages.get(this, "accept")) {
            @Override
            protected void onClick() {
                SPDSettings.firebase(true);
                SPDSettings.firebaseRecords(true);
                ShatteredPixelDungeon.switchScene(GoScene.class);
            }
        };

        RedButton rejectButton = new RedButton(Messages.get(this, "reject")) {
            @Override
            protected void onClick() {
                Game.instance.finish();
            }
        };

        // 设置按钮位置和大小
        float buttonWidth = 40;
        float buttonHeight = 18;
        float buttonSpacing = 10;
        float bottomMargin = 10;

        float totalWidth = buttonWidth * 2 + buttonSpacing;
        float startX = (Camera.main.width - totalWidth) / 2;
        float startY = shpx.bottom()+10;

        acceptButton.setRect(startX, startY, buttonWidth, buttonHeight);
        rejectButton.setRect(startX + buttonWidth + buttonSpacing, startY, buttonWidth, buttonHeight);

        content.add(acceptButton);
        content.add(rejectButton);


        content.setSize( fullWidth, shpx.bottom()+30 );

        list.setRect( 0, 0, w, h );
        list.scrollTo(0, 0);
    }

    @Override
    protected void onBackPressed() {
        ShatteredPixelDungeon.scene().add( new WndError( Messages.get(this, "need_read") ) );
    }

    private static class CreditsBlock extends Component {

        boolean large;
        RenderedTextBlock title;
        Image avatar;
        Flare flare;
        RenderedTextBlock body;

        RenderedTextBlock link;
        ColorBlock linkUnderline;
        PointerArea linkButton;

        //many elements can be null, but body is assumed to have content.
        private CreditsBlock(boolean large, int highlight, String title, Image avatar, String body, String linkText, String linkUrl){
            super();

            this.large = large;

            if (title != null) {
                this.title = PixelScene.renderTextBlock(title, large ? 8 : 6);
                if (highlight != -1) this.title.hardlight(highlight);
                add(this.title);
            }

            if (avatar != null){
                this.avatar = avatar;
                add(this.avatar);
            }

            if (large && highlight != -1 && this.avatar != null){
                this.flare = new Flare( 7, 24 ).color( highlight, true ).show(this.avatar, 0);
                this.flare.angularSpeed = 20;
            }

            this.body = PixelScene.renderTextBlock(body, 6);
            if (highlight != -1) this.body.setHightlighting(true, highlight);
            if (large) this.body.align(RenderedTextBlock.LEFT_ALIGN);
            add(this.body);

            if (linkText != null && linkUrl != null){

                int color = 0xFFFFFFFF;
                if (highlight != -1) color = 0xFF000000 | highlight;
                this.linkUnderline = new ColorBlock(1, 1, color);
                add(this.linkUnderline);

                this.link = PixelScene.renderTextBlock(linkText, 6);
                if (highlight != -1) this.link.hardlight(highlight);
                add(this.link);

                linkButton = new PointerArea(0, 0, 0, 0){
                    @Override
                    protected void onClick( PointerEvent event ) {
                        ShatteredPixelDungeon.platform.openURI( linkUrl );
                    }
                };
                add(linkButton);
            }

        }

        @Override
        protected void layout() {
            super.layout();

            float topY = top();

            if (title != null){
                title.maxWidth((int)width());
                title.setPos( x + (width() - title.width())/2f, topY);
                topY += title.height() + (large ? 2 : 1);
            }

            if (large){

                if (avatar != null){
                    avatar.x = x + (width()-avatar.width())/2f;
                    avatar.y = topY;
                    PixelScene.align(avatar);
                    if (flare != null){
                        flare.point(avatar.center());
                    }
                    topY = avatar.y + avatar.height() + 2;
                }

                body.maxWidth((int)width());
                body.setPos( x + (width() - body.width())/2f, topY);
                topY += body.height() + 2;

            } else {

                if (avatar != null){
                    avatar.x = x;
                    body.maxWidth((int)(width() - avatar.width - 1));

                    float fullAvHeight = Math.max(avatar.height(), 16);
                    if (fullAvHeight > body.height()){
                        avatar.y = topY + (fullAvHeight - avatar.height())/2f;
                        PixelScene.align(avatar);
                        body.setPos( avatar.x + avatar.width() + 1, topY + (fullAvHeight - body.height())/2f);
                        topY += fullAvHeight + 1;
                    } else {
                        avatar.y = topY + (body.height() - fullAvHeight)/2f;
                        PixelScene.align(avatar);
                        body.setPos( avatar.x + avatar.width() + 1, topY);
                        topY += body.height() + 2;
                    }

                } else {
                    topY += 1;
                    body.maxWidth((int)width());
                    body.setPos( x, topY);
                    topY += body.height()+2;
                }

            }

            if (link != null){
                if (large) topY += 1;
                link.maxWidth((int)width());
                link.setPos( x + (width() - link.width())/2f, topY);
                topY += link.height() + 2;

                linkButton.x = link.left()-1;
                linkButton.y = link.top()-1;
                linkButton.width = link.width()+2;
                linkButton.height = link.height()+2;

                linkUnderline.size(link.width(), PixelScene.align(0.49f));
                linkUnderline.x = link.left();
                linkUnderline.y = link.bottom()+1;

            }

            topY -= 2;

            height = Math.max(height, topY - top());
        }
    }
}

