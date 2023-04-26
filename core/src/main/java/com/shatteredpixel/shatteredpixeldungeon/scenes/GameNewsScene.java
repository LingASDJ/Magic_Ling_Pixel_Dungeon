package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Languages;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.services.news.GameUpdateNewsArticles;
import com.shatteredpixel.shatteredpixeldungeon.services.news.UpdateNews;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.Calendar;

public class GameNewsScene extends PixelScene {

    boolean displayingNoArticles = false;

    private static final int BTN_HEIGHT = 22;
    private static final int BTN_WIDTH = 100;

    @Override
    public void create() {
        super.create();

        uiCamera.visible = false;

        int w = Camera.main.width;
        int h = Camera.main.height;

        int fullWidth = PixelScene.landscape() ? 202 : 100;
        int left = (w - fullWidth)/2;

        Archs archs = new Archs();
        archs.setSize(w, h);
        add(archs);

        RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
        title.hardlight(Window.TITLE_COLOR);
        title.setPos(
                (w - title.width()) / 2f,
                (20 - title.height()) / 2f
        );
        align(title);
        add(title);

        float top = 18;

        displayingNoArticles = !UpdateNews.articlesAvailable();
        if (displayingNoArticles || Messages.lang() != Languages.CHINESE) {

            Component newsInfo = new GameNewsScene.NewsInfo();
            newsInfo.setRect(left, 20, fullWidth, 0);
            add(newsInfo);

            top = newsInfo.bottom();

        }

        if (!displayingNoArticles) {
            ArrayList<GameUpdateNewsArticles> articles = UpdateNews.articles();

            float articleSpace = h - top - 2;
            int rows = articles.size();
            if (PixelScene.landscape()){
                rows /= 2;
            }
            rows++;

            while ((articleSpace) / (BTN_HEIGHT+0.5f) < rows) {
                articles.remove(articles.size() - 1);
                if (PixelScene.landscape()) {
                    articles.remove(articles.size() - 1);
                }
                rows--;
            }

            float gap = ((articleSpace) - (BTN_HEIGHT * rows)) / (float)rows;

            boolean rightCol = false;
            for (GameUpdateNewsArticles article : articles) {
                StyledButton b = new GameNewsScene.ArticleButton(article);
                b.multiline = true;
                if (!rightCol) {
                    top += gap;
                    b.setRect( left, top, BTN_WIDTH, BTN_HEIGHT);
                } else {
                    b.setRect( left + fullWidth - BTN_WIDTH, top, BTN_WIDTH, BTN_HEIGHT);
                }
                align(b);
                add(b);
                if (!PixelScene.landscape()) {
                    top += BTN_HEIGHT;
                } else {
                    if (rightCol){
                        top += BTN_HEIGHT;
                    }
                    rightCol = !rightCol;
                }
            }
            top += gap;
        } else {
            top += 18;
        }

        StyledButton btnSite = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "read_more")){
            @Override
            protected void onClick() {
                super.onClick();
                ShatteredPixelDungeon.switchNoFade( TitleScene.class );
            }
        };
        btnSite.icon(Icons.get(Icons.NEWS));
        btnSite.textColor(Window.TITLE_COLOR);
        btnSite.setRect(left, 190, fullWidth, BTN_HEIGHT);
        add(btnSite);

    }

    @Override
    public void update() {
        if (displayingNoArticles && UpdateNews.articlesAvailable()){
            ShatteredPixelDungeon.seamlessResetScene();
        }
        super.update();
    }

    private static class NewsInfo extends Component {

        NinePatch bg;
        RenderedTextBlock text;

        RedButton button;

        @Override
        protected void createChildren() {
            bg = Chrome.get(Chrome.Type.GREY_BUTTON_TR);
            add(bg);

            String message = "";

            if (Messages.lang() != Languages.CHINESE){
                message += Messages.get(this, "english_warn");
            }

            SPDSettings.WiFi(false);
            UpdateNews.checkForNews();

            if (!UpdateNews.articlesAvailable()){
                if (SPDSettings.news()) {
                    if (SPDSettings.WiFi() && !Game.platform.connectedToUnmeteredNetwork()) {
                        message += "\n\n" + Messages.get(this, "metered_network");

                        button = new RedButton(Messages.get(this, "enable_data")) {
                            @Override
                            protected void onClick() {
                                super.onClick();
                                SPDSettings.WiFi(false);
                                UpdateNews.checkForNews();
                                ShatteredPixelDungeon.seamlessResetScene();
                            }
                        };
                        add(button);
                    } else {
                        message += "\n\n" + Messages.get(this, "no_internet");
                    }
                } else {
                    message += "\n\n" + Messages.get(this, "news_disabled");

                    button = new RedButton(Messages.get(this, "enable_news")) {
                        @Override
                        protected void onClick() {
                            super.onClick();
                            SPDSettings.news(true);
                            UpdateNews.checkForNews();
                            ShatteredPixelDungeon.seamlessResetScene();
                        }
                    };
                    add(button);
                }
            }

            if (message.startsWith("\n\n")) message = message.replaceFirst("\n\n", "");

            text = PixelScene.renderTextBlock(message, 6);
            text.hardlight(CharSprite.WARNING);
            add(text);
        }

        @Override
        protected void layout() {
            bg.x = x;
            bg.y = y;

            text.maxWidth((int)width - bg.marginHor());
            text.setPos(x + bg.marginLeft(), y + bg.marginTop()+1);

            height = (text.bottom()) - y;

            if (button != null){
                height += 4;
                button.multiline = true;
                button.setSize(width - bg.marginHor(), 16);
                button.setSize(width - bg.marginHor(), Math.max(button.reqHeight(), 16));
                button.setPos(x + (width - button.width())/2, y + height);
                height = button.bottom() - y;
            }

            height += bg.marginBottom() + 1;

            bg.size(width, height);

        }
    }

    private static class ArticleButton extends StyledButton {
        GameUpdateNewsArticles article;
        BitmapText date;

        public ArticleButton(GameUpdateNewsArticles article) {
            super(Chrome.Type.GREY_BUTTON_TR, article.title, 6);
            this.article = article;

            icon(UpdateNews.parseArticleIcon(article));

            Calendar cal = Calendar.getInstance();
            cal.setTime(article.date);
            date = new BitmapText( UpdateNews.parseArticleDate(article), pixelFont);
            date.scale.set(PixelScene.align(0.5f));
            date.hardlight( 0x888888 );
            date.measure();
            //add(date);

            IconTitle title = new IconTitle(new ItemSprite( ItemSpriteSheet.MAGICGIRLBOOKS ),article.title);
            title.setRect( 30, 20, 100, 0 );
            title.setPos( title.centerX(), title.bottom() + 4 );
            add(title);
            int w = Camera.main.width;
            String message = article.summary;
            message += "\n\n你现在的游戏版本代码："+ Game.versionCode;
            message += "\n\n网络上的游戏版本代码："+ article.ling;
            message += "\n\n"+(
                    article.ling > Game.versionCode ? "✦你的版本是旧版本，请及时更新✦":
                    article.ling == Game.versionCode ? "Δ版本一致,你已是最新版本。Δ": "Γ版本号大于服务器数据，可能是修改版，请谨慎游玩。Γ");
            int fullWidth = PixelScene.landscape() ? 202 : 100;
            int left = (w - fullWidth)/2;
            if(article.ling > Game.versionCode || article.ling < Game.versionCode){
                StyledButton btnDownload = new StyledButton(Chrome.Type.SCROLL,
                        //这里读取的是外面的数据,Condition 'article.ling < Game.versionCode' is always 'true'
                        //AS不会判定这种外网数据，它实际可以运行，请勿通过AS优化此代码
                        article.ling > Game.versionCode ? Messages.get(this, "btnDownload_more") :
                                article.ling < Game.versionCode ? Messages.get(this, "btnFixedDownload") : ""){
                    @Override
                    protected void onClick() {
                        super.onClick();
                        ShatteredPixelDungeon.platform.openURI(article.URL);
                    }
                };
                btnDownload.icon(Icons.get(article.ling > Game.versionCode?Icons.STATS :
                        article.ling < Game.versionCode ? Icons.WARNING: Icons.STATS));
                btnDownload.textColor(Window.TITLE_COLOR);
                btnDownload.setRect(left, 160, fullWidth, BTN_HEIGHT);
                add(btnDownload);
            }

            RenderedTextBlock text = PixelScene.renderTextBlock( 6 );
            text.text( message, 100 );
            text.setPos( title.left(), title.bottom() + 4 );
            add( text );
        }

        @Override
        protected void layout() {
            super.layout();
            bg.active=false;
            bg.visible=false;
            icon.x = x + bg.marginLeft() + (16-icon.width())/2f;
            icon.visible=false;
            PixelScene.align(icon);
            text.setPos(x + bg.marginLeft() + 18, text.top());
            text.visible=false;
            if (date != null) {
                date.x = x + width - bg.marginRight() - date.width() + 1;
                date.y = y + height - bg.marginBottom() - date.height() + 2.5f;
                align(date);
            }
        }

        @Override
        protected void onClick() {
            super.onClick();
           //
        }
    }

}

