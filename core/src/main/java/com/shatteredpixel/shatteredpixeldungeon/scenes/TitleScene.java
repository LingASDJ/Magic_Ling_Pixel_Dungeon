package com.shatteredpixel.shatteredpixeldungeon.scenes;

import static com.shatteredpixel.shatteredpixeldungeon.BGMPlayer.playBGM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel.Holiday.XMAS;
import static com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel.holiday;
import static com.shatteredpixel.shatteredpixeldungeon.ui.StatusPane.asset;

import com.badlogic.gdx.Gdx;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.Gregorian;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.NetIcons;
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.effects.BannerSprites;
import com.shatteredpixel.shatteredpixeldungeon.effects.Fireball;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.services.news.News;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SliceGirlSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.EndButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.update.MLChangesButton;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndHardNotification;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSettings;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndVictoryCongrats;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.ColorMath;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.GameMath;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TitleScene extends PixelScene {
	public static boolean Reusable = false;

	public static boolean NightDay = false;

	public static boolean NTP_ERROR = false;

	public static boolean NTP_ERROR_VEFY = false;

	public static boolean NTP_NOINTER = false;

	public static boolean NTP_NOINTER_VEFY = false;

	public static boolean NTP_LINK = false;

	public void noInter(){
		if(NTP_NOINTER || NTP_ERROR || NTP_NOINTER_VEFY || NTP_ERROR_VEFY){
			//启用破碎默认活动
			final Calendar calendar = Calendar.getInstance();
			switch (calendar.get(Calendar.MONTH)) {
				case Calendar.JANUARY:
					if (calendar.get(Calendar.WEEK_OF_MONTH) == 1)
						holiday = RegularLevel.Holiday.XMAS;
					break;
				case Calendar.OCTOBER:
					if (calendar.get(Calendar.WEEK_OF_MONTH) >= 2)
						holiday = RegularLevel.Holiday.HWEEN;
					break;
				case Calendar.NOVEMBER:
					if (calendar.get(Calendar.DAY_OF_MONTH) == 1)
						holiday = RegularLevel.Holiday.HWEEN;
					break;
				case Calendar.DECEMBER:
					if (calendar.get(Calendar.WEEK_OF_MONTH) >= 3)
						holiday = RegularLevel.Holiday.XMAS;
					break;
			}
		}
	}

	@Override
	public void create() {
		super.create();
		Calendar calendar = Calendar.getInstance();
		int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
		Dungeon.whiteDaymode = currentHour > 7 && currentHour < 22;

		Badges.loadGlobal();
		boolean whiteDaymode = currentHour > 7 && currentHour < 22;

		if(!NTP_LINK){
			ExecutorService executor = Executors.newSingleThreadExecutor();
			Future<?> future = executor.submit(() -> {
				try {
					// NTP服务器需要合理分配，根据语言环使用不同的地址进行监测
					String ntpServer;
					switch (SPDSettings.language()){
						default:
						case CHINESE:
							ntpServer = "www.baidu.com";
							break;
						case GREEK:case ENGLISH:case RUSSIAN:case JAPANESE:case HARDCHINESE:
							ntpServer = "www.bing.com";
							break;
					}
					URL url = new URL("https://" + ntpServer);
					URLConnection conn = url.openConnection();
					conn.connect();
					long dateL = conn.getDate();
					Date onlineDate = new Date(dateL);

					Date localDate = new Date(); // 获取本地时间
					String strDateFormat = "yyyy-MM-dd";
					SimpleDateFormat dateFormat = new SimpleDateFormat(strDateFormat, Locale.getDefault());
					String onlineTimeStr = dateFormat.format(onlineDate); // 在线时间的字符串表示
					String localTimeStr = dateFormat.format(localDate); // 本地时间的字符串表示

					if (onlineTimeStr.equals(localTimeStr)) {
						if (Random.Int(10) <= 4 && !NightDay && !whiteDaymode) {
							NightDay = true;
						} else if (Badges.isUnlocked(Badges.Badge.VICTORY) && Random.Int(10) == 1 && !Reusable) {
							Reusable = true;
						}
						/**农历计算*/
						Gregorian.LunarCheckDate();
					} else if (!NTP_ERROR_VEFY) {
						noInter();

						NTP_ERROR = true;
						NTP_ERROR_VEFY = true;
					}
				} catch (IOException e) {
					if (!NTP_NOINTER_VEFY || SPDSettings.WiFi() && !Game.platform.connectedToUnmeteredNetwork()) {
						NTP_NOINTER = true;

						NTP_NOINTER_VEFY = true;
					}
				}
			});

			try {
				//超时1s 实际3s
				future.get(4, TimeUnit.SECONDS);
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				if (!NTP_NOINTER_VEFY || SPDSettings.WiFi() && !Game.platform.connectedToUnmeteredNetwork()) {
					NTP_NOINTER = true;
					NTP_NOINTER_VEFY = true;
				}
				noInter();
			}
			NTP_LINK = true;
			executor.shutdown();
		}


		if(holiday == XMAS){
			playBGM(Assets.Music.CHRAMSS, true);
		} else {
			Music.INSTANCE.playTracks(
					new String[]{Assets.Music.PRACH, Assets.BOSSDOG},
					new float[]{1, 1},
					true);
		}

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

		Image title = BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON );

		float topRegion = Math.max(title.height - 6, h * 0.45f);

		title.x = (w - title.width()) / 2f;
		title.y = 2 + (topRegion - title.height()) / 2f;

		align(title);

		placeTorch(title.x + 22, title.y + 46);
		placeTorch(title.x + title.width - 22, title.y + 46);

		Image swordLeft = new Image( BannerSprites.get( BannerSprites.Type.SWORD ) ) {
			private float preCurTime = 0;
			private float curTime = 0;

			@Override
			public void update() {
				super.update();
				this.origin.set( this.width / 2, this.height / 2 );
				float time = 0.8f;
				this.x = - this.width + curTime/ time * (Camera.main.width / 2f + this.width / 2f);
				this.angle = 90 - curTime/ time *225;
				am = curTime*curTime*curTime/(time * time * time);
				if (SPDSettings.ClassUI()) {
					asset = Assets.Interfaces.STATUS;
				} else {
					asset =  Assets.Interfaces.STATUS_DARK;
				}
				float preTime = 0.9f;
				if (preCurTime < preTime) {
					preCurTime += Game.elapsed;
					return;
				}
				if (curTime < time) {
					curTime += Game.elapsed;
					if (curTime >= time) Camera.main.shake( GameMath.gate( 1, 2, 5), 0.3f );
				}
			}
		};
		swordLeft.center(title.center());
		add( swordLeft );

		Image swordRight = new Image( BannerSprites.get( BannerSprites.Type.SWORD ) ) {
			private float preCurTime = 0;
			private float curTime = 0;

			@Override
			public void update() {
				super.update();
				this.origin.set( this.width / 2, this.height / 2 );
				float time = 0.8f;
				this.x = Camera.main.width - curTime/ time * (Camera.main.width / 2f + this.width / 2f);
				this.angle = 90 + curTime/ time * 225;
				am = curTime*curTime*curTime/(time * time * time);

				float preTime = 0.9f;
				if (preCurTime < preTime) {
					preCurTime += Game.elapsed;
					return;
				}
				if (curTime < time) curTime += Game.elapsed;
			}
		};
		swordRight.center(title.center());
		add( swordRight );

		add( title );

		Flare flare = new Flare( 7, 128 ) {
			private float time1 = 0;
			private float time2 = 0;
			private float time3 = 0;
			@Override
			public void update() {
				super.update();
				am = Math.max(0f, (float)Math.sin(time1 += Game.elapsed));
				if (time1 >= 1.5f * Math.PI) time1 = 0;
				rm = Math.max(0f, (float)Math.sin(time2 += Game.elapsed));
				if (time2 >= 1.0f * Math.E) time2 = 5;
				ra = Math.max(0f, (float)Math.sin(time3 += Game.elapsed));
				if (time3 >= 1.0f * Math.PI) time3 = 1;
			}
		};
		flare.color( Window.BLUE_COLOR, true ).show( title, 0 ).angularSpeed = +27;

		Archs archs = new Archs();
		archs.setSize( w, h );
		addToBack( archs );

		Image signs = new Image( BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON_SIGNS ) ) {
			private float time = 0;
			@Override
			public void update() {
				super.update();
				am = Math.max(0f, (float)Math.sin(time += Game.elapsed));
				if (time >= 1.5f * Math.PI) time = 0;
			}
			@Override
			public void draw() {
				Blending.setLightMode();
				super.draw();
				Blending.setNormalMode();
			}
		};
		signs.x = title.x + (title.width() - signs.width()) / 2f;
		signs.y = title.y;
		add(signs);

		final Chrome.Type GREY_TR = Chrome.Type.GREY_BUTTON_TR;

		StyledButton btnPlay = new StyledButton(GREY_TR, Messages.get(this, "enter")){
			@Override
			protected void onClick() {
				if(NTP_ERROR) {

					ExecutorService executor = Executors.newSingleThreadExecutor();
					executor.submit(() -> {
						try {
							// NTP服务器有严重延迟，直接使用百度的地址进行监测
							String ntpServer;
							switch (SPDSettings.language()){
								default:
								case CHINESE:
									ntpServer = "www.baidu.com";
									break;
								case GREEK:case ENGLISH:case RUSSIAN:case JAPANESE:case HARDCHINESE:
									ntpServer = "www.bing.com";
									break;
							}
							URL url = new URL("https://" + ntpServer);
							URLConnection conn = url.openConnection();
							conn.connect();
							long dateL = conn.getDate();
							Date onlineDate = new Date(dateL);

							Date localDate = new Date(); // 获取本地时间
							String strDateFormat = "yyyy-MM-dd";
							SimpleDateFormat dateFormat = new SimpleDateFormat(strDateFormat, Locale.getDefault());
							String onlineTimeStr = dateFormat.format(onlineDate); // 在线时间的字符串表示
							String localTimeStr = dateFormat.format(localDate); // 本地时间的字符串表示

							// 在主线程中更新UI
							Gdx.app.postRunnable(() -> {
								ShatteredPixelDungeon.scene().add(new WndHardNotification(NetIcons.get(NetIcons.ALERT),
										Messages.get(this, "ntp_error"),
										Messages.get(this, "ntp_desc", onlineTimeStr, localTimeStr),
										Messages.get(this, "ok"),
										0) {
									@Override
									public void hide() {
										NTP_ERROR = false;
										if (GamesInProgress.checkAll().size() == 0) {
											GamesInProgress.selectedClass = null;
											GamesInProgress.curSlot = 1;
										}
										ShatteredPixelDungeon.switchNoFade(StartScene.class);
									}

									@Override
									public void onBackPressed() {
										NTP_ERROR = false;
										ShatteredPixelDungeon.switchNoFade(TitleScene.class);
									}
								});
							});

						} catch (IOException ignored) {}
					});
					executor.shutdown();
				} else if(NTP_NOINTER){
					ShatteredPixelDungeon.scene().add(new WndHardNotification(NetIcons.get(NetIcons.ALERT),
							Messages.get(this, "lntp_error"),
							Messages.get(this, "lntp_desc"),
							Messages.get(this, "ok"),
							0){
						@Override
						public void hide() {
							NTP_NOINTER = false;
							if (GamesInProgress.checkAll().size() == 0) {
								GamesInProgress.selectedClass = null;
								GamesInProgress.curSlot = 1;
							}
							ShatteredPixelDungeon.switchNoFade( StartScene.class );
						}
						@Override
						public void onBackPressed() {
							NTP_NOINTER = false;
							ShatteredPixelDungeon.switchNoFade(TitleScene.class);
						}
					});
				} else {
					if (GamesInProgress.checkAll().size() == 0) {
						GamesInProgress.selectedClass = null;
						GamesInProgress.curSlot = 1;
					}
					ShatteredPixelDungeon.switchNoFade( StartScene.class );
				}
			}

			@Override
			public void update() {
				super.update();

				if(TitleScene.NightDay){
					textColor(ColorMath.interpolate( 0xFFFFFF, Window.CBLACK,
							0.1f + (float)Math.sin(Game.timeTotal*5)/2f));
					text(Messages.get(TitleScene.class, "dark"));
					icon(BadgeBanner.image(Badges.Badge.STORM.image));
				} else if (TitleScene.Reusable){
					textColor(ColorMath.interpolate( 0xFFFFFF, Window.CYELLOW,
							0.5f + (float)Math.sin(Game.timeTotal*5)/2f));
					text(Messages.get(TitleScene.class, "go"));
					icon(BadgeBanner.image(Badges.Badge.HAPPY_END.image));
				} else if(TitleScene.NTP_ERROR){
					textColor(ColorMath.interpolate( 0xFFFFFF, Window.GDX_COLOR,
							0.5f + (float)Math.sin(Game.timeTotal*5)/2f));
					text(Messages.get(TitleScene.class, "ntp"));
					icon(NetIcons.get(NetIcons.ALERT));
				} else if(TitleScene.NTP_NOINTER){
					textColor(ColorMath.interpolate( 0xFFFFFF, Window.WATA_COLOR,
							0.5f + (float)Math.sin(Game.timeTotal*5)/2f));
					text(Messages.get(TitleScene.class, "ntp_ns"));
					icon(NetIcons.get(NetIcons.ALERT));
				}
			}

			@Override
			protected boolean onLongClick() {
				//making it easier to start runs quickly while debugging
				if (DeviceCompat.isDebug()) {
					GamesInProgress.selectedClass = null;
					GamesInProgress.curSlot = 1;
					ShatteredPixelDungeon.switchScene(StartScene.class);
					return true;
				}
				return super.onLongClick();
			}
		};
		btnPlay.icon(holiday == XMAS ?  new Image(new SliceGirlSprite()) :
				new Image(Icons.get(Icons.ENTER)));
		add(btnPlay);

		StyledButton btnRankings = new StyledButton(GREY_TR,Messages.get(this, "rankings")) {
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.switchNoFade(RankingsScene.class);
			}
		};
		btnRankings.icon(new Image(Icons.get(Icons.RANKINGS)));
		add(btnRankings);

		StyledButton btnBadges = new StyledButton(GREY_TR, Messages.get(this, "badges")) {
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.switchNoFade(JournalScene.class);
			}
			@Override
			protected boolean onLongClick() {
				Badges.silentValidateHDEX();
				ShatteredPixelDungeon.switchNoFade(PassWordBadgesScene.class);
				return super.onLongClick();
			}
		};
		btnBadges.icon(new Image(Icons.get(Icons.BADGES)));
		add(btnBadges);

		StyledButton btnSupport = new SupportButton(GREY_TR, Messages.get(this, "support"));
		add(btnSupport);

		StyledButton btnChanges = new MLChangesButton(GREY_TR, Messages.get(this, "changes"));
		btnChanges.icon(new Image(Icons.get(Icons.CHANGES)));
		add(btnChanges);

		StyledButton btnSettings = new SettingsButton(GREY_TR, Messages.get(this, "settings"));
		add(btnSettings);

		StyledButton btnAbout = new StyledButton(GREY_TR, Messages.get(this, "about")){
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.switchNoFade( AboutSelectScene.class );
			}
		};
		btnAbout.icon(new Image(Icons.get(Icons.SHPX)));
		add(btnAbout);

		StyledButton seed = new SeedButton(landscape() ? Chrome.Type.GREY_BUTTON_TR : Chrome.Type.BLANK, Messages.get(this, "seed"));
		seed.icon(NetIcons.get(NetIcons.CHAT));
		add(seed);

		StyledButton btnNews = new NewsButton(GREY_TR, Messages.get(this, "news"));
		btnNews.icon(new Image(Icons.get(Icons.NEWS)));
		add(btnNews);

		final int BTN_HEIGHT = 20;
		int GAP = (int)(h - topRegion - (landscape() ? 3 : 4) * BTN_HEIGHT) / 3;
		GAP /= landscape() ? 3 : 5;
		GAP = Math.max(GAP, 2);

		BitmapText version = new BitmapText( "v" + Game.version, pixelFont);
		version.measure();
		version.alpha( 0.4f);
		version.x = w - version.width() - 4;
		version.y = h - version.height() - 2;

		add( version );

		if (landscape()) {
			btnPlay.setRect(title.x - 50, topRegion + GAP, title.width() + 100 - 1, BTN_HEIGHT);
			align(btnPlay);
			btnRankings.setRect(btnPlay.left(), btnPlay.bottom()+ GAP, (btnPlay.width() * 0.332f) - 1, BTN_HEIGHT);
			btnBadges.setRect(btnRankings.left(), btnRankings.bottom()+GAP, btnRankings.width(), BTN_HEIGHT);
			btnSupport.setRect(btnRankings.right() + 2, btnRankings.top(), btnRankings.width(), BTN_HEIGHT);
			btnChanges.setRect(btnSupport.left(), btnSupport.bottom() + GAP, btnRankings.width(), BTN_HEIGHT);
			btnSettings.setRect(btnSupport.right() + 2, btnSupport.top(), btnRankings.width(), BTN_HEIGHT);
			btnAbout.setRect(btnSettings.left(), btnSettings.bottom() + GAP, btnRankings.width(), BTN_HEIGHT);
			btnNews.setRect(btnPlay.left(), btnAbout.bottom() + GAP, btnAbout.width() + 157 - 1, BTN_HEIGHT);
			seed.setRect(0, 0,40,20);

			align(btnNews);
		}
		else {
			seed.setRect(0, version.y-10,40,20);
			btnPlay.setRect(title.x, topRegion + GAP, title.width(), BTN_HEIGHT);
			align(btnPlay);
			btnRankings.setRect(btnPlay.left(), btnPlay.bottom()+ GAP, (btnPlay.width() / 2) - 1, BTN_HEIGHT);
			btnBadges.setRect(btnRankings.right() + 2, btnRankings.top(), btnRankings.width(), BTN_HEIGHT);
			btnSupport.setRect(btnRankings.left(), btnRankings.bottom()+ GAP, btnRankings.width(), BTN_HEIGHT);
			btnChanges.setRect(btnSupport.right() + 2, btnSupport.top(), btnSupport.width(), BTN_HEIGHT);
			btnSettings.setRect(btnSupport.left(), btnSupport.bottom()+GAP, btnRankings.width(), BTN_HEIGHT);
			btnAbout.setRect(btnSettings.right() + 2, btnSettings.top(), btnSettings.width(), BTN_HEIGHT);
			btnNews.setRect(btnPlay.left(), btnAbout.bottom() + GAP, btnAbout.width() + 68 - 1, BTN_HEIGHT);
			align(btnNews);
		}

		Point p = SPDSettings.windowResolution();
		if ( p.x >= 640 && p.y >= 480) {
			EndButton btnExit = new EndButton();
			btnExit.setPos( w - btnExit.width(), 0 );
			add( btnExit );
		}

		Badges.loadGlobal();
		if (Badges.isUnlocked(Badges.Badge.VICTORY) && !SPDSettings.victoryNagged()) {
			SPDSettings.victoryNagged(true);
			add(new WndVictoryCongrats());
		}

		fadeIn();
	}

	private void placeTorch( float x, float y ) {
		Fireball fb2 = new Fireball();
		fb2.setPos( x, y );
		add( fb2 );
	}

	private static class NewsButton extends StyledButton {

		public NewsButton(Chrome.Type type, String label ){
			super(type, label);
			if (SPDSettings.news()) News.checkForNews();
		}

		int unreadCount = -1;

		@Override
		public void update() {
			super.update();

			if (unreadCount == -1 && News.articlesAvailable()){
				long lastRead = SPDSettings.newsLastRead();
				if (lastRead == 0){
					if (News.articles().get(0) != null) {
						SPDSettings.newsLastRead(News.articles().get(0).date.getTime());
					}
				} else {
					unreadCount = News.unreadArticles(new Date(SPDSettings.newsLastRead()));
					if (unreadCount > 0) {
						unreadCount = Math.min(unreadCount, 9);
						text(text() + "(" + unreadCount + ")");
					}
				}
			}

			if (unreadCount > 0){
				textColor(ColorMath.interpolate( 0xFFFFFF, Window.TITLE_COLOR,
						0.5f + (float)Math.sin(Game.timeTotal*5)/2f));
			}
		}

		@Override
		protected void onClick() {
			super.onClick();
			ShatteredPixelDungeon.switchNoFade( NewsScene.class );
		}
	}

	public static class ChangesButton extends StyledButton {

		public ChangesButton( Chrome.Type type, String label ){
			super(type, label);
		}

		@Override
		protected void onClick() {
			ShatteredPixelDungeon.switchNoFade( ChangesScene.class );
		}

	}

	private static class SeedButton extends StyledButton {

		public SeedButton( Chrome.Type type, String label ){
			super(type, label);
		}

		@Override
		protected void onClick() {
			ShatteredPixelDungeon.switchNoFade(SeedFinderScene.class);
		}

		@Override
		protected boolean onLongClick() {
			ShatteredPixelDungeon.switchNoFade(SeedFinderScene.class);
			return true;
		}

	}




	private static class SettingsButton extends StyledButton {

		public SettingsButton(Chrome.Type type, String label){
			super(type, label);
			icon(new Image(Icons.get(Icons.PREFS)));
		}

		@Override
		public void update() {
			super.update();
		}

		@Override
		protected void onClick() {
			ShatteredPixelDungeon.scene().add(new WndSettings());
		}
	}

	private static class SupportButton extends StyledButton {

		public SupportButton( Chrome.Type type, String label ){
			super(type, label);
			icon(new Image(Icons.get(Icons.THANKS)));
		}

		@Override
		protected void onClick() {
			ShatteredPixelDungeon.switchNoFade(ThanksScene.class);
		}
	}

}
