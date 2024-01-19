package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.badlogic.gdx.Gdx;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.NetIcons;
import com.shatteredpixel.shatteredpixeldungeon.messages.Languages;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.services.news.GameUpdateNewsArticles;
import com.shatteredpixel.shatteredpixeldungeon.services.news.UpdateNews;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndHardNotification;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.DeviceCompat;

import java.util.ArrayList;


public class GameNewsScene extends PixelScene {  //定义GameNewsScene类，继承PixelScene类

    boolean displayingNoArticles = false;  //声明一个布尔类型的变量，用于标记是否需要显示文章

    private static final int BTN_HEIGHT = 22;  //声明一个常量，表示Button的高度
    private static final int BTN_WIDTH = 100;  //声明一个常量，表示Button的宽度

    @Override
    public void create() {  //重写create方法

        super.create();  //调用父类的create方法

        uiCamera.visible = false;  //设置UI相机不可见

        int w = Camera.main.width;  //获取主相机的宽度
        int h = Camera.main.height;  //获取主相机的高度

        int fullWidth = PixelScene.landscape() ? 202 : 100;  //如果是横屏模式，设置整个场景的宽度为202，否则为100
        int left = (w - fullWidth) / 2;  //左边列Button的左边沿位置

        Archs archs = new Archs();  //创建一个Archs实例，用作场景背景
        archs.setSize(w, h);  //设置Archs实例的大小
        add(archs);  //添加Archs实例到场景中

        float top = 18;  //左边列Button的上边缘位置

        displayingNoArticles = !UpdateNews.articlesAvailable();  //检查是否有文章可用，如果没有就设置displayingNoArticles为True
        if (displayingNoArticles || Messages.lang() != Languages.CHINESE) {  //如果没有文章或者语言不是中文

            Component newsInfo = new GameNewsScene.NewsInfo();  //创建一个新闻信息组件
            newsInfo.setRect(left, 20, fullWidth, 0); //设置组件的位置和大小
            add(newsInfo);  //将组件添加到场景中

            top = newsInfo.bottom();  //将左边列Button的上边沿位置设置为新闻信息组件的下边沿位置

        }

        add(new WndHardNotification(NetIcons.get(NetIcons.NEWS),
                Messages.get(this, "title"),
                Messages.get(this, "update"),
                Messages.get(this, "continue"),
                3){
            @Override
            public void hide() {
                ShatteredPixelDungeon.switchNoFade(TitleScene.class);
            }
        });

        if (!displayingNoArticles) {  //有文章可用于加载时

            ArrayList<GameUpdateNewsArticles> articles = UpdateNews.articles();  //获取文章列表

            float articleSpace = h - top - 2;  //计算剩余空间
            int rows = articles.size();  //计算行数
            if (PixelScene.landscape()) {
                rows /= 2;  //如果是横屏模式，每行只放置两个Button
            }
            rows++;

            while ((articleSpace) / (BTN_HEIGHT + 0.5f) < rows) {  //如果当前所有文章无法显示完整，则删除最后的文章
                articles.remove(articles.size() - 1);
                if (PixelScene.landscape()) {
                    articles.remove(articles.size() - 1);
                }
                rows--;
            }

            float gap = ((articleSpace) - (BTN_HEIGHT * rows)) / (float) rows;  //计算空隙大小

            boolean rightCol = false;  //是否为右边的列
            for (GameUpdateNewsArticles article : articles) {  //遍历文章列表中的所有文章

                StyledButton b = new GameNewsScene.ArticleButton(article);  //创建一个新的文章Button
                b.multiline = true;  //设置Button可换行

                if (!rightCol) {  //如果不是右边的列
                    top += gap;  //调整top位置
                    b.setRect(left, top, BTN_WIDTH, BTN_HEIGHT);  //设置Button位置和大小
                } else {
                    b.setRect(left + fullWidth - BTN_WIDTH, top, BTN_WIDTH, BTN_HEIGHT);  //设置Button位置和大小
                }

                align(b);  //将Button与其他元素对齐
                add(b);  //将Button添加到场景中

                if (!PixelScene.landscape()) {
                    top += BTN_HEIGHT;
                } else {
                    if (rightCol) {
                        top += BTN_HEIGHT;
                    }
                    rightCol = !rightCol;
                }

//                btnSite.visible = false;  //将Button的可见性设置为False
//                btnSite.active = false;  //将Button的活跃状态设置为False

                RenderedTextBlock title;

                if (article.ling > Game.versionCode) {  //如果文章需要更新的版本号大于当前版本号
                    title = PixelScene.renderTextBlock(Messages.get(this,"new_version"), 9);  //创建一个渲染的文本块，并设置为"你的版本需要更新！"
                    title.hardlight(Window.RED_COLOR);  //将文本块突出显示为红色
                } else if (article.ling < Game.versionCode) {  //如果文章需要更新的版本号小于当前版本号
                    title = PixelScene.renderTextBlock(Messages.get(this,"error_version"), 9);  //创建一个渲染的文本块，并设置为"警告：你的版本可能是盗版！"
                    title.hardlight(Window.CBLACK);  //将文本块突出显示为黑色
                } else {
                    title = PixelScene.renderTextBlock(Messages.get(this,"lastest_version"), 9);  //如果文章版本号与当前版本号相同，创建一个渲染的文本块，并设置为"已是最新版本！"
                    title.hardlight(Window.TITLE_COLOR);  //将文本块突出显示为标题颜色
                }

                title.setPos((w - title.width()) / 2f, (20 - title.height()) / 2f);  //设置文本块位置
                align(title);  //将文本块与其他元素对齐
                add(title);  //将文本块添加到场景中
            }
        } else if (SPDSettings.WiFi() && !Game.platform.connectedToUnmeteredNetwork()){
            add(new WndHardNotification(NetIcons.get(NetIcons.ALERT),
                    Messages.get(this, "no_web"),
                    Messages.get(this, "no_inter"),
                    Messages.get(this, "continue"),
                    0){
                @Override
                public void hide() {
                    ShatteredPixelDungeon.switchNoFade(TitleScene.class);
                }
            });
        }

    }

    @Override
    public void update() {  //重写update方法

        if (displayingNoArticles && UpdateNews.articlesAvailable()) {  //如果没有可用的文章并且有文章可用于加载
            ShatteredPixelDungeon.seamlessResetScene();  //重置场景
        } else if (SPDSettings.WiFi() && !Game.platform.connectedToUnmeteredNetwork()){
            ShatteredPixelDungeon.seamlessResetScene();  //重置场景
        }

        super.update();  //调用父类的update方法
    }

    private static class NewsInfo extends Component {  //创建一个新闻信息组件

        NinePatch bg;  //声明一个NinePatch实例，用作背景
        RenderedTextBlock text;  //声明一个RenderedTextBlock实例，用于显示文字信息

        RedButton button;  //声明一个RedButton实例

        @Override
        protected void createChildren() {  //创建组件中的子元素

            // 添加背景
            bg = Chrome.get(Chrome.Type.SCROLL);
            add(bg);

            // 设置消息文本信息，包含游戏版本号和版本代码
            String message = Game.version + "-VC" + Game.versionCode;

            // 检查是否有可用更新
            UpdateNews.checkForNews();

            // 如果没有可用新闻显示
            if (!UpdateNews.articlesAvailable()) {
                // 判断用户是否允许显示新闻，并且在未连接到无限制网络下提示
                if (SPDSettings.news()) {
                    if (SPDSettings.WiFi() && !Game.platform.connectedToUnmeteredNetwork()) {
                        // 显示警告信息
                        message += "\n\n" + Messages.get(this, "metered_network");
                        // 添加红色按钮，用于启用数据
                    } else {
                        // 显示没有网络的信息
                        message += "\n\n" + Messages.get(this, "no_internet");
                    }
                } else {
                    // 新闻被禁用时，显示新闻禁用的信息，并添加红色按钮，用于启用新闻
                    message += "\n\n" + Messages.get(this, "no_internet");
                }
            }

            // 去掉多余的换行符
            if (message.startsWith("\n\n")) message = message.replaceFirst("\n\n", "");

            // 渲染文本块，并设置位置
            text = PixelScene.renderTextBlock(message, 6);
            text.hardlight(CharSprite.DEFAULT);
            add(text);
        }

        // 布局
        @Override
        protected void layout() {
            // 背景定位
            bg.x = x;
            bg.y = y;

            // 设置文本最大宽度
            text.maxWidth((int) width - bg.marginHor());
            // 设置文本位置
            text.setPos(x + bg.marginLeft(), y + bg.marginTop() + 1);

            // 计算高度
            height = (text.bottom()) - y;
            // 如果有按钮，增加高度
            if (button != null) {
                height += 4;
                // 设置按钮为多行显示
                button.multiline = true;
                button.setSize(width - bg.marginHor(), 16);
                button.setSize(width - bg.marginHor(), Math.max(button.reqHeight(), 16));
                // 重新定位按钮
                button.setPos(x + (width - button.width()) / 2, y + height);
                height = button.bottom() - y;
            }

            // 添加底部边距
            height += bg.marginBottom() + 1;

            // 设置背景大小
            bg.size(width, height);
        }
    }

    // ArticleButton类，表现为一个StyledButton样式的按钮，并根据游戏版本号与服务器版本号的大小关系展示不同的图标
    private static class ArticleButton extends StyledButton {

        // 消息提示
        public static void message(String message) {
            Game.runOnRenderThread(() -> ShatteredPixelDungeon.scene().add(new WndMessage(message)));
        }

        GameUpdateNewsArticles article;
        BitmapText date;

        /**
         * 创建一个新的按钮
         *
         * @param article 按钮对应的文章
         * 如果游戏的版本号大于服务器的最大版本号，按钮会显示一个警告图标
         * 如果游戏的版本号小于服务器的最小版本号，按钮会显示一个更新图标
         * 如果游戏的版本号等于服务器的最大版本号，按钮会显示一个天赋图标
         */
        public ArticleButton(GameUpdateNewsArticles article) {
            super(Chrome.Type.GREY_BUTTON_TR, article.title, 6);
            this.article = article;

            if (article.ling > Game.versionCode || article.ling < Game.versionCode) {
                icon(UpdateNews.parseArticleIcon(article));

                if (article.ling > Game.versionCode) {
                    // 向用户展示新文章可用的选项：下载或退出游戏
                    ShatteredPixelDungeon.scene().add(new WndHardNotification(NetIcons.get(NetIcons.GLOBE),
                            article.title,
                            article.summary,
                            Messages.get(this, "download"),
                            0){
                        @Override
                        public void hide() {
                            // 如果是桌面版就打开桌面版的下载链接，否则打开安卓版的下载链接
                            if (DeviceCompat.isDesktop()) {
                                ShatteredPixelDungeon.platform.openURI(article.DesktopURL);
                            } else {
                                ShatteredPixelDungeon.platform.openURI(article.URL);
                            }
                            Gdx.app.exit();
                        }

                        @Override
                        public void onBackPressed() {
                            ShatteredPixelDungeon.switchNoFade(TitleScene.class);
                        }
                    });
                } else {
                    // 向用户展示新版本可用的选项：强制下载或退出游戏
                    ShatteredPixelDungeon.scene().add(new WndHardNotification(NetIcons.get(NetIcons.ALERT),
                            article.title,
                            article.summary,
                            Messages.get(this, "force_download"),
                            0){
                        @Override
                        public void hide() {
                                // 如果是桌面版就打开桌面版的下载链接，否则打开安卓版的下载链接
                                if (DeviceCompat.isDesktop()) {
                                    ShatteredPixelDungeon.platform.openURI(article.DesktopURL);
                                } else {
                                    ShatteredPixelDungeon.platform.openURI(article.URL);
                                }
                                Gdx.app.exit();
                        }

                        @Override
                        public void onBackPressed() {
                            //
                        }
                    });
                }
            } else {
                // 显示天赋图标并提示用户已经更新完成
                icon(UpdateNews.parseArticleIcon(article));
                ShatteredPixelDungeon.scene().add(new WndHardNotification(NetIcons.get(NetIcons.NEWS),
                        article.title,
                        article.summary,
                        Messages.get(this, "okay"),
                        0){
                    @Override
                    public void hide() {
                            ShatteredPixelDungeon.switchNoFade(TitleScene.class);
                    }

                    public void onBackPressed() {
                        ShatteredPixelDungeon.switchNoFade(TitleScene.class);
                    }
                });

            }
        }

        // 布局
        @Override
        protected void layout() {
            super.layout();
            bg.active = false;
            bg.visible = false;
            icon.x = x + bg.marginLeft() + (16 - icon.width()) / 2f;
            icon.visible = false;
            PixelScene.align(icon);
            text.setPos(x + bg.marginLeft() + 18, text.top());
            text.visible = false;
            if (date != null) {
                date.x = x + width - bg.marginRight() - date.width() + 1;
                date.y = y + height - bg.marginBottom() - date.height() + 2.5f;
                align(date);
            }
        }

        // 点击事件
        @Override
        protected void onClick() {
            super.onClick();
            //
        }
    }

}

