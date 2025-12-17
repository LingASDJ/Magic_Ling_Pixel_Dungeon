package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Languages;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.services.news.News;
import com.shatteredpixel.shatteredpixeldungeon.services.news.NewsArticle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class NewsScene extends PixelScene {

	boolean displayingNoArticles = false;

	private static final int BTN_HEIGHT = 22;
	private static final int GAP = 2;
	private static final int SCROLL_MARGIN = 20; // 滚动条边距

	@Override
	public void create() {
		super.create();

		PixelScene.uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;
		boolean landscape = PixelScene.landscape();

		// 背景装饰
		Archs archs = new Archs();
		archs.setSize(w, h);
		addToBack(archs);

		// 标题
		IconTitle title = new IconTitle(Icons.NEWS.get(), Messages.get(this, "title"));
		title.setSize(200, 0);
		title.setPos(
				(w - title.reqWidth()) / 2f,
				(20 - title.height()) / 2f
		);
		align(title);
		add(title);

		// 退出按钮
		ExitButton btnExit = new ExitButton();
		btnExit.setPos(w - btnExit.width(), 0);
		add(btnExit);

		// 主面板
		NinePatch panel = Chrome.get(Chrome.Type.BLANK);
		int pw = w - SCROLL_MARGIN * 2; // 增加边距
		int ph = h - 36 - BTN_HEIGHT - GAP; // 为底部的"阅读更多"按钮留出空间
		panel.size(pw, ph);
		panel.x = (w - pw) / 2f;
		panel.y = title.bottom() + 5;
		align(panel);
		add(panel);

		displayingNoArticles = !News.articlesAvailable();

		// 创建滚动列表
		ScrollPane list = new ScrollPane(new Component());
		add(list);

		Component content = list.content();
		content.clear();

		float posY = 0;
		float nextPosY = 0;
		boolean second = false;
		int columns = landscape ? 2 : 1; // 根据屏幕方向决定列数

		// 显示信息提示
		if (displayingNoArticles || Messages.lang() != Languages.CHINESE) {
			Component newsInfo = new NewsInfo();
			newsInfo.setRect(0, posY, panel.innerWidth(), 0);
			content.add(newsInfo);
			posY = nextPosY = newsInfo.bottom() + GAP;
			second = false;
		}

		// 显示文章列表
		if (!displayingNoArticles) {
			List<NewsArticle> articles = new ArrayList<>(News.articles());

			// 按置顶状态排序
			Collections.sort(articles, new Comparator<NewsArticle>() {
				@Override
				public int compare(NewsArticle a1, NewsArticle a2) {
					boolean isTop1 = "true".equals(a1.top);
					boolean isTop2 = "true".equals(a2.top);
					if (isTop1 && !isTop2) return -1;
					if (!isTop1 && isTop2) return 1;
					return a2.date.compareTo(a1.date);
				}
			});

			// 添加文章按钮
			for (NewsArticle article : articles) {
				StyledButton b = new ArticleButton(article);
				b.multiline = true;

				if (columns == 1) {
					// 竖屏单列布局
					b.setRect(0, posY, panel.innerWidth(), BTN_HEIGHT);
					posY = nextPosY = b.bottom() + GAP;
				} else {
					// 横屏两列布局
					if (!second) {
						b.setRect(0, posY, panel.innerWidth()/2f - GAP/2, BTN_HEIGHT);
						second = true;
					} else {
						b.setRect(panel.innerWidth()/2f + GAP/2, posY, panel.innerWidth()/2f - GAP/2, BTN_HEIGHT);
						second = false;
						posY = nextPosY;
					}
					nextPosY = Math.max(b.bottom(), nextPosY);
				}

				content.add(b);
			}
		}

		// 设置内容大小和滚动区域
		content.setSize(panel.innerWidth(), (int)Math.ceil(posY));
		list.setRect(
				panel.x,
				panel.y,
				panel.width(),
				panel.height()
		);

		// 添加"阅读更多"按钮（独立在底部）
		StyledButton btnSite = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "read_more")){
			@Override
			protected void onClick() {
				super.onClick();
				String link = "https://mlpd.spldream.com";
				ShatteredPixelDungeon.platform.openURI(link);
			}
		};
		btnSite.icon(Icons.get(Icons.NEWS));
		btnSite.textColor(Window.TITLE_COLOR);
		btnSite.setRect(panel.x, Camera.main.height-35,panel.width(), BTN_HEIGHT);
		add(btnSite);

		fadeIn();
	}

	@Override
	protected void onBackPressed() {
		ShatteredPixelDungeon.switchNoFade(TitleScene.class);
	}

	@Override
	public void update() {
		if (displayingNoArticles && News.articlesAvailable()){
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

			if (!News.articlesAvailable()){
				if (SPDSettings.news()) {
					if (SPDSettings.WiFi() && !Game.platform.connectedToUnmeteredNetwork()) {
						message += "\n\n" + Messages.get(this, "metered_network");

						button = new RedButton(Messages.get(this, "enable_data")) {
							@Override
							protected void onClick() {
								super.onClick();
								SPDSettings.WiFi(false);
								News.checkForNews();
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
							News.checkForNews();
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

		NewsArticle article;

		BitmapText date;
		BitmapText topTag;

		public ArticleButton(NewsArticle article) {
			super(Chrome.Type.GREY_BUTTON_TR, article.title, 6);
			this.article = article;

			icon(News.parseArticleIcon(article,true));
			long lastRead = SPDSettings.newsLastRead();
			if (lastRead > 0 && article.date.getTime() > lastRead) {
				textColor(Window.Pink_COLOR);
			}

			date = new BitmapText(News.parseArticleDate(article), pixelFont);
			date.scale.set(PixelScene.align(0.5f));
			date.hardlight(0x888888);
			date.measure();

			// 添加置顶标签
			if ("true".equals(article.top)) {
				topTag = new BitmapText(Messages.get(NewsScene.class, "top_tag"), pixelFont);
				topTag.hardlight(Window.Pink_COLOR);
				topTag.scale.set(PixelScene.align(0.75f));
				topTag.measure();
				add(topTag);
			}
		}

		@Override
		protected void layout() {
			super.layout();

			icon.x = x + bg.marginLeft() + (16-icon.width())/2f;
			PixelScene.align(icon);
			text.setPos(x + bg.marginLeft() + 18, text.top());

			if (date != null) {
				date.x = x + width - bg.marginRight() - date.width() + 1;
				date.y = y + height - bg.marginBottom() - date.height() + 2.5f;
				align(date);
			}

			if (topTag != null) {
				topTag.x = x + width - bg.marginRight() - topTag.width() - date.width() - 5;
				topTag.y = y + height - bg.marginBottom() - topTag.height() + 2.5f;
				align(topTag);
			}
		}

		@Override
		protected void onClick() {
			super.onClick();
			textColor(Window.WHITE);
			if (article.date.getTime() > SPDSettings.newsLastRead()){
				SPDSettings.newsLastRead(article.date.getTime());
			}
			ShatteredPixelDungeon.scene().addToFront(new WndArticle(article));
		}
	}

	private static class WndArticle extends WndTitledMessage {

		public WndArticle(NewsArticle article) {
			super(News.parseArticleIcon(article,false), article.title, article.summary);

			RedButton link = new RedButton(Messages.get(NewsScene.class, "read_more")){
				@Override
				protected void onClick() {
					super.onClick();
					String link = article.URL;
					ShatteredPixelDungeon.platform.openURI(link);
				}
			};
			link.setHeight(BTN_HEIGHT);
			if(!Objects.equals(article.URL, "#")){
				addToBottom(link);
			}
		}
	}
}
