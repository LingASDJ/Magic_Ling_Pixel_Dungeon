package com.shatteredpixel.shatteredpixeldungeon.scenes;

import static com.shatteredpixel.shatteredpixeldungeon.ui.Window.SHPX_COLOR;
import static com.shatteredpixel.shatteredpixeldungeon.ui.Window.WATA_COLOR;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedDragonSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RenSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TianscarSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Component;

public class ThanksScene extends PixelScene {

    private static final float SCROLL_SPEED	= 30f;
    public static ScrollPane list;
    private float shift = -30;

    @Override
    public void create() {
        super.create();

        final float colWidth = 120;
        final float fullWidth = colWidth * (landscape() ? 2 : 1);

        int w = Camera.main.width;
        int h = Camera.main.height;

        list = new ScrollPane( new Component() );
        add( list );

        Component content = list.content();
        content.clear();

        CreditsBlock mispd = new CreditsBlock(true, Window.TITLE_COLOR,
                "Magic Ling Pixel Dungeon",
                new Image("Ling.png", 0, 0, 16, 16),
                "_Start to 2021-2-12_\n\nDev:_JDSA Ling_"
                ,
                null,
                null);
        mispd.setRect((Camera.main.width - colWidth)/2f-10, 120, colWidth+20, 0);
        content.add(mispd);

        //*** Musics Used ***

        CreditsBlock musicUsed = new CreditsBlock(true, Window.TITLE_COLOR,
                "Thanks:\n",
                null,
                Messages.get(ThanksScene.class,"code")+ Messages.get(ThanksScene.class,"test")+Messages.get(ThanksScene.class,"music")+Messages.get(ThanksScene.class,"art")+ Messages.get(ThanksScene.class,"ad")
                ,
                null,
                null);
        musicUsed.setRect((Camera.main.width - colWidth)/2f-10, mispd.bottom()+6, colWidth+20, 0);
        content.add(musicUsed);

        CreditsBlock ptr = new CreditsBlock(true, 0x39aae5,
                "ThanksScene Base on MISPD",
                new Image("mispd.png", 0, 0, 32, 39),
                "Developed by: _Jinkeloid_",
                null,
                null);
        ptr.setRect((Camera.main.width - colWidth)/2f-10, musicUsed.bottom()+6, colWidth+20, 0);
        content.add(ptr);
        //*** Special Thanks ***

        CreditsBlock specialThanks = new CreditsBlock(true, 0xffffff,
                "Original Music By Prohonor",
                new Image("ptr.png", 0, 0, 32, 32),
                null,
                null,
                null);
        specialThanks.setRect((Camera.main.width - colWidth)/2f-10, ptr.bottom()+6, colWidth+20, 0);
        content.add(specialThanks);

        CreditsBlock specialThanksB = new CreditsBlock(true, 0xbe8e6e,
                "Part Music Used Terraria",
                new Image("tra.png", 0, 0, 17, 28),
                null,
                null,
                null);
        specialThanksB.setRect((Camera.main.width - colWidth)/2f-10, specialThanks.bottom()+6, colWidth+20, 0);
        content.add(specialThanksB);

        //*** Shattered Pixel Dungeon Credits ***

        CreditsBlock shpx = new CreditsBlock(true, SHPX_COLOR,
                "All Special Thanks\n\n           Shattered Pixel Dungeon",
                Icons.SHPX.get(),
                "Developed by: _Evan Debenham_\nBased on Pixel Dungeon's open source",
                null,
                null);
        shpx.setRect((Camera.main.width - colWidth)/2f-10, specialThanksB.bottom()+6, colWidth+20, 0);
        content.add(shpx);

        addLine(shpx.top() - 4, content);

        //*** Freesound Credits ***

        CreditsBlock freesound = new CreditsBlock(true,
                WATA_COLOR,
                "Pixel Dungeon",
                Icons.WATA.get(),
                "Developed by: _Watabou_\nInspired by Brian Walker's Brogue",
                null,
                null);
        freesound.setRect((Camera.main.width - colWidth)/2f-10, shpx.bottom() + 8, colWidth+20, 0);
        content.add(freesound);

        CreditsBlock exp = new CreditsBlock(true,
                0x00ccccc,
                "TrashBox Bodylev",
                Icons.TALENT.get(),
                "Main Dev:Experienced Pixel Dungeon",
                null,
                null);
        exp.setRect((Camera.main.width - colWidth)/2f-10,  freesound.bottom() + 8, colWidth+20, 0);
        content.add(exp);

        CreditsBlock ren = new CreditsBlock(true,
                0xff00ff,
                "莲仁-REN",
                new RenSprite(),
                "Main Dev:Night Pro 2 Pixel Dungeon\nWhisky Pixel Dungeon",
                null,
                null);
        ren.setRect((Camera.main.width - colWidth)/2f-10,  exp.bottom() + 8, colWidth+20, 0);
        content.add(ren);

        CreditsBlock reddragon = new CreditsBlock(true,
                0xFF6600,
                "Alexstrasza",
                new RedDragonSprite(),
                "Main Dev:ShatteredPD-DetailedDesc",
                null,
                null);
        reddragon.setRect((Camera.main.width - colWidth)/2f-10,  ren.bottom() + 8, colWidth+20, 0);
        content.add(reddragon);

        CreditsBlock evan = new CreditsBlock(true, 0xBBBBBB,
                "Tianscar",
                new TianscarSprite(),
                "Main Dev:Carbonized Pixel Dungeon",
                "加入碳化总群",
                "https://jq.qq.com/?_wv=1027&k=6jAlnlNL");
        if (landscape()){
            evan.setRect((Camera.main.width - colWidth)/2f-10, reddragon.bottom() + 8, colWidth+20, 0);
        } else {
            evan.setRect((Camera.main.width - colWidth)/2f-10, reddragon.bottom() + 8, colWidth+20, 0);
        }
        content.add(evan);

        //*** Pixel Dungeon Credits ***
        CreditsBlock wata = new CreditsBlock(true,
                Window.RED_COLOR,
                "SmuJB",
                new Image("smujb.png", 0, 0, 16, 16),
                "Main Dev:Cursed Pixel Dungeon\nHard Sproted Pixel Dungeon\nPoweredPD Pixel Dungeon",
                null,
                null);
        if (landscape()){
            wata.setRect((Camera.main.width - colWidth)/2f-10, evan.bottom() + 8, colWidth+20, 0);
        } else {
            wata.setRect((Camera.main.width - colWidth)/2f-10, evan.bottom() + 8, colWidth+20, 0);
        }
        content.add(wata);

        CreditsBlock wata2 = new CreditsBlock(true,
                Window.TITLE_COLOR,
                "Thanks Playing My Dungeon",
                Icons.TALENT.get(),
                "All the Players wishing you all the best every day!!!\n\n",
                "加入QQ魔绫开发总群",
                "https://jq.qq.com/?_wv=1027&k=wzMjU0f7");
        if (landscape()){
            wata2.setRect((Camera.main.width - colWidth)/2f-10, wata.bottom() + 20, colWidth+20, 0);
        } else {
            wata2.setRect((Camera.main.width - colWidth)/2f-10, wata.bottom() + 20, colWidth+20, 0);
        }
        content.add(wata2);

        addLine(wata2.top() - 4, content);

        CreditsBlock wata3 = new CreditsBlock(true,
                Window.TITLE_COLOR,
                null,
                null,
                "Join the Pixel Dungeon Server",
                "Pixel Dungeon Discord Server",
                "https://discord.gg/47bguQtcwZ");
        if (landscape()){
            wata3.setRect((Camera.main.width - colWidth)/2f-10, wata2.bottom() + 18, colWidth+20, 0);
        } else {
            wata3.setRect((Camera.main.width - colWidth)/2f-10, wata2.bottom() + 18, colWidth+20, 0);
        }
        content.add(wata3);

        //*** Some Words ***
        content.add(wata);

        content.setSize( fullWidth, wata.bottom() + 10 );

        list.setRect( 0, 0, w, wata.bottom() - h/2f );
        list.scrollTo(0, 0);

        Archs archs = new Archs();
        archs.setSize( Camera.main.width, Camera.main.height );
        addToBack( archs );

        ExitButton btnExit = new ExitButton();
        btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
        add( btnExit );

        fadeIn();
    }

    @Override
    public void update() {
        super.update();
        ScrollPane list = ThanksScene.list;
        list.disableThumb();
        shift += Game.elapsed * SCROLL_SPEED;
        list.scrollTo(0, Math.min(Math.max(shift, 0), list.height()));
//		throw new RuntimeException("custom exception");
    }

    @Override
    protected void onBackPressed() {
        MusicImplantSPD.switchScene(TitleScene.class, new Game.SceneChangeCallback() {
            @Override
            public void beforeCreate() {
                shift = -40;
            }

            @Override
            public void afterCreate() {
            }
        });
    }

    private void addLine( float y, Group content ){
        ColorBlock line = new ColorBlock(Camera.main.width, 1, 0xFF333333);
        line.y = y;
        content.add(line);
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
            if (large) this.body.align(RenderedTextBlock.CENTER_ALIGN);
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

                    if (avatar.height() > body.height()){
                        avatar.y = topY;
                        body.setPos( avatar.x + avatar.width() + 1, topY + (avatar.height() - body.height())/2f);
                        topY += avatar.height() + 1;
                    } else {
                        avatar.y = topY + (body.height() - avatar.height())/2f;
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