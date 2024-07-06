/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import static com.shatteredpixel.shatteredpixeldungeon.ui.Icons.RENAME_OFF;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.NetIcons;
import com.shatteredpixel.shatteredpixeldungeon.effects.Fireball;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndChallenges;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDLC;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndHeroInfo;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndKeyBindings;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndStartGame;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTextInput;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Matrix;
import com.watabou.glwrap.Quad;
import com.watabou.input.GameAction;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Calendar;

public class HeroSelectScene extends PixelScene {

	private static HeroClass[] heroClasses = new HeroClass[] {
			HeroClass.WARRIOR,
			HeroClass.MAGE,
			HeroClass.ROGUE,
			HeroClass.HUNTRESS,

			HeroClass.DUELIST
	};
	private static int heroClassIndex = 0;
	private static void addHeroClassIndex(int add) {
		heroClassIndex = heroClassIndex + add;
		if (heroClassIndex >= heroClasses.length) heroClassIndex -= heroClasses.length;
		else if (heroClassIndex < 0) heroClassIndex += heroClasses.length;
	}



	private static HeroClass heroClass() {
		return heroClasses[heroClassIndex];
	}

	private static final int FRAME_WIDTH    = 88;
	private static final int FRAME_HEIGHT    = 125;

	private static final int FRAME_MARGIN_TOP    = 9;
	private static final int FRAME_MARGIN_X        = 4;

	private static final int BUTTON_HEIGHT    = 20;

	private static final int SKY_WIDTH    = 80;
	private static final int SKY_HEIGHT    = 112;

	private static final int NSTARS		= 100;
	private static final int NCLOUDS	= 5;

	private Camera viewport;

	private Avatar a;
	private RedButton startBtn;
	private IconButton skin;
	private Image frame;

	private IconButton infoButton;
	private IconButton challengeButton;

	private StyledButton holidayButton;
	private IconButton btnExit;
	private ArrayList<StyledButton> buttons;
	@Override
	public void create() {

		super.create();

		Dungeon.hero = null;
		buttons = new ArrayList<>();
		Badges.loadGlobal();

		Music.INSTANCE.playTracks(
				new String[]{Assets.Music.THEME_2},
				new float[]{1},
				false);

		PixelScene.uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );

		float vx = align((w - SKY_WIDTH) / 2f);
		float vy = align((h - SKY_HEIGHT - BUTTON_HEIGHT) / 2f);

		Point s = Camera.main.cameraToScreen( vx, vy );
		viewport = new Camera( s.x, s.y, SKY_WIDTH, SKY_HEIGHT, defaultZoom );
		Camera.add( viewport );

		Group window = new Group();
		window.camera = viewport;
		add( window );

		boolean dayTime =
				Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 18 && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 7;

		Sky sky = new Sky( Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) ;
		sky.scale.set( SKY_WIDTH, SKY_HEIGHT );
		window.add( sky );

		if (!dayTime) {
			for (int i=0; i < NSTARS; i++) {
				float size = Random.Float();
				ColorBlock star = new ColorBlock( size, size, 0xFFFFFFFF );
				star.x = Random.Float( SKY_WIDTH ) - size / 2;
				star.y = Random.Float( SKY_HEIGHT ) - size / 2;
				star.am = size * (1 - star.y / SKY_HEIGHT);
				window.add( star );
			}
		}

		float range = SKY_HEIGHT * 2 / 3;
		for (int i=0; i < NCLOUDS; i++) {
			Cloud cloud = new Cloud( (NCLOUDS - 1 - i) * (range / NCLOUDS) + Random.Float( range / NCLOUDS ), dayTime );
			window.add( cloud );
		}

		int nPatches = (int)(sky.width() / GrassPatch.WIDTH + 1);

		for (int i=0; i < nPatches * 4; i++) {
			GrassPatch patch = new GrassPatch( (i - 0.75f) * GrassPatch.WIDTH / 4, SKY_HEIGHT + 1, dayTime );
			patch.brightness( dayTime ? 0.7f : 0.4f );
			window.add( patch );
		}

		a = new Avatar( heroClass() );
		// Removing semitransparent contour
		a.am = 2; a.aa = -1;
		a.x = (SKY_WIDTH - a.width) / 2;
		a.y = SKY_HEIGHT - a.height;
		align(a);

		window.add( a );

		window.add( new PointerArea( a ) {
			protected void onClick( PointerEvent event ) {
				if (GamesInProgress.selectedClass == null) return;
				HeroClass cl = GamesInProgress.selectedClass;
				if( cl.isUnlocked() ) ShatteredPixelDungeon.scene().addToFront(new WndHeroInfo(cl));
				else ShatteredPixelDungeon.scene().addToFront( new WndMessage(cl.unlockMsg()));
			}
		} );

		for (int i=0; i < nPatches; i++) {
			GrassPatch patch = new GrassPatch( (i - 0.5f) * GrassPatch.WIDTH, SKY_HEIGHT, dayTime );
			patch.brightness( dayTime ? 1.0f : 0.8f );
			window.add( patch );
		}

		frame = new Image( SPDSettings.ClassUI() ? Assets.Interfaces.NEW_MENU : Assets.Interfaces.NEW_MENU_DARK );

		frame.frame( FRAME_WIDTH + GrassPatch.WIDTH*4, 0, FRAME_WIDTH, FRAME_HEIGHT );
		frame.x = vx - FRAME_MARGIN_X;
		frame.y = vy - FRAME_MARGIN_TOP;
		add( frame );

		if (dayTime) {
			a.brightness( 1.2f );
		} else {
			frame.hardlight( 0xDDEEFF );
		}

		startBtn = new RedButton( "" ) {
			@Override
			protected void onClick() {
				super.onClick();

				if (GamesInProgress.selectedClass == null) return;

				Dungeon.hero = null;
				ActionIndicator.action = null;
				InterlevelScene.mode = InterlevelScene.Mode.DESCEND;

				Game.switchScene( InterlevelScene.class );
			}
			public void enable( boolean value ) {
				active = value;
				text.alpha( value ? 1.0f : 0.3f );
				icon.alpha( value ? 1.0f : 0.3f );
			}
		};
		startBtn.icon(Icons.get(Icons.ENTER));
		add( startBtn );

		skin = new IconButton( NetIcons.get(NetIcons.GLOBE) ){
			@Override
			protected void onClick() {
				super.onClick();
				heroClass().SetSkin(heroClass().GetSkin()+1);
				a.heroClass(heroClass());
			}

			@Override
			protected String hoverText() {
				return Messages.titleCase(Messages.get(WndKeyBindings.class, "switch_skin"));
			}
		};
		skin.setSize( BUTTON_HEIGHT, BUTTON_HEIGHT );
		skin.setPos(frame.x+frame.width-12,frame.y);
		add(skin);

		infoButton = new IconButton(Icons.get(Icons.INFO)){
			@Override
			protected void onClick() {
				super.onClick();
				if (GamesInProgress.selectedClass == null) return;
				HeroClass cl = GamesInProgress.selectedClass;
				if( cl.isUnlocked() ) ShatteredPixelDungeon.scene().addToFront(new WndHeroInfo(cl));
				else ShatteredPixelDungeon.scene().addToFront( new WndMessage(cl.unlockMsg()));
			}

			@Override
			public GameAction keyAction() {
				return SPDAction.HERO_INFO;
			}

			@Override
			protected String hoverText() {
				return Messages.titleCase(Messages.get(WndKeyBindings.class, "hero_info"));
			}
		};
		infoButton.setSize(21, 21);
		add(infoButton);

		challengeButton = new IconButton(
				Icons.get( SPDSettings.challenges() > 0 ? Icons.CHALLENGE_ON :Icons.CHALLENGE_OFF)){
			@Override
			protected void onClick() {
				if (DeviceCompat.isDebug() || Badges.isUnlocked(Badges.Badge.VICTORY) ||Badges.isUnlocked(Badges.Badge.HOLLOWCITY) ||  Badges.isUnlocked(Badges.Badge.HAPPY_END) || Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_4)) {
					ShatteredPixelDungeon.scene().addToFront(new WndChallenges(SPDSettings.challenges(), true,null) {
						public void onBackPressed() {
							super.onBackPressed();
							icon(Icons.get(SPDSettings.challenges() > 0 ? Icons.CHALLENGE_ON : Icons.CHALLENGE_OFF));
						}
					} );
				} else ShatteredPixelDungeon.scene().addToFront( new WndMessage( Messages.get(HeroSelectScene.class, "challenges_unlock") ));
			}

			@Override
			public void update() {
				if( !visible && GamesInProgress.selectedClass != null){
					visible = true;
				}
				if (SPDSettings.challenges() > 0) {
					icon(Icons.get( Icons.CHALLENGE_ON));
				} else {
					icon(Icons.get( Icons.CHALLENGE_OFF));
				}
				super.update();
			}

			@Override
			protected String hoverText() {
				return Messages.titleCase(Messages.get(WndChallenges.class, "title"));
			}
		};
		challengeButton.setRect(startBtn.left() + 16, Camera.main.height- BUTTON_HEIGHT-16, 21, 21);
		add(challengeButton);



//		holidayButton = new StyledButton(Chrome.Type.BLANK,"TesT", 6){
//			@Override
//			protected void onClick() {
//
//			}
//			private long timeToUpdate = 0;
//			private static final long SECOND = 1000;
//			private static final long MINUTE = 60 * SECOND;
//			private static final long HOUR = 60 * MINUTE;
//			private static final long DAY = 24 * HOUR;
//			private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.ROOT);
//			{
//				dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//			}
//			@Override
//			public void update() {
//				super.update();
//
//				if (Game.realTime > timeToUpdate && visible){
//					long diff = (SPDSettings.lastDaily() + DAY) - Game.realTime;
//
//					if (diff > 0){
//						if (diff > 30*HOUR){
//							holidayButton.text("30:00:00+");
//						} else {
//							holidayButton.text(dateFormat.format(new Date(diff)));
//						}
//						timeToUpdate = Game.realTime + SECOND;
//					} else {
//						//holidayButton.text(Messages.get(HeroSelectScene.class, "daily"));
//						timeToUpdate = Long.MAX_VALUE;
//					}
//				}
//
//			}
//
//			@Override
//			protected String hoverText() {
//				return Messages.titleCase(Messages.get(WndChallenges.class, "title"));
//			}
//		};
//		holidayButton.icon(NetIcons.get(NetIcons.NEWS));
//		holidayButton.setRect(challengeButton.left(), challengeButton.y, 21, 21);
//		add(holidayButton);

		setSelectedHero();

		IconButton prevBtn = new IconButton( Icons.get(Icons.LEFTBUTTON) ) {
			{
				width = 20;
				height = 20;
			}
			@Override
			protected void onClick() {
				super.onClick();

				addHeroClassIndex(-1);
				setSelectedHero();
			}

			@Override
			protected String hoverText() {
				return Messages.titleCase(Messages.get(WndKeyBindings.class, "prev"));
			}

			@Override
			public GameAction keyAction() {
				return GameAction.NONE;
			}
		};
		prevBtn.setSize( BUTTON_HEIGHT, BUTTON_HEIGHT );
		prevBtn.setPos( frame.x - BUTTON_HEIGHT - FRAME_MARGIN_X, frame.y + frame.height / 2 - BUTTON_HEIGHT / 2f);
		PixelScene.align(prevBtn);
		add( prevBtn );

		IconButton nextBtn = new IconButton( Icons.get(Icons.RIGHTBUTTON) ) {
			{
				width = 20;
				height = 20;
			}
			@Override
			protected void onClick() {
				super.onClick();

				addHeroClassIndex(1);
				setSelectedHero();
			}

			@Override
			protected String hoverText() {
				return Messages.titleCase(Messages.get(WndKeyBindings.class, "next"));
			}

			@Override
			public GameAction keyAction() {
				return GameAction.NONE;
			}
		};
		nextBtn.setSize( BUTTON_HEIGHT, BUTTON_HEIGHT );
		nextBtn.setPos( frame.x + frame.width + FRAME_MARGIN_X, frame.y + frame.height / 2 - BUTTON_HEIGHT / 2f);
		PixelScene.align(nextBtn);
		add( nextBtn );

		IconButton Telnetsc = new IconButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_SPELLBOOK, null)){

			@Override
			protected void onClick() {
				HeroClass cl = heroClass();
				GamesInProgress.selectedClass = cl;
				a.heroClass(cl);
				ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(cl, cl.name() + "_story")));
			}
		};
		Telnetsc.setSize( BUTTON_HEIGHT, BUTTON_HEIGHT );
		Telnetsc.setPos( frame.x + frame.width + FRAME_MARGIN_X, frame.y-10 + frame.height-10 - BUTTON_HEIGHT);
		add(Telnetsc);

		StyledButton seedButton = new StyledButton(Chrome.Type.BLANK, "", 6){
			@Override
			protected void onClick() {
				String existingSeedtext = SPDSettings.customSeed();
				ShatteredPixelDungeon.scene().addToFront( new WndTextInput(Messages.get(HeroSelectScene.class, "custom_seed_title"),
						Messages.get(HeroSelectScene.class, "custom_seed_desc"),
						existingSeedtext,
						20,
						false,
						Messages.get(HeroSelectScene.class, "custom_seed_set"),
						Messages.get(HeroSelectScene.class, "custom_seed_clear")){
					@Override
					public void onSelect(boolean positive, String text) {
						text = DungeonSeed.formatText(text);
						long seed = DungeonSeed.convertFromText(text);
						if (positive && seed != -1){
							SPDSettings.customSeed(text);
							icon.hardlight(1f, 1.5f, 0.67f);
						} else {
							SPDSettings.customSeed("");
							icon.resetColor();
						}
					}
				});
			}
		};
		seedButton.leftJustify = true;
		seedButton.setSize( BUTTON_HEIGHT, BUTTON_HEIGHT );
		seedButton.setPos( frame.x-58+frame.width-58 + FRAME_MARGIN_X, frame.y-10 + frame.height-10 - BUTTON_HEIGHT);
		seedButton.icon(Icons.get(Icons.ENTER));
		if (!SPDSettings.customSeed().isEmpty()) seedButton.icon().hardlight(1f, 1.5f, 0.67f);;
		buttons.add(seedButton);
		add(seedButton);

		IconButton Rename = new IconButton(Icons.get(Icons.RENAME_OFF)){
			@Override
			protected void onClick() {
				if (Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_1)){
						Game.runOnRenderThread(() -> ShatteredPixelDungeon.scene().add(new WndTextInput(Messages.get(WndStartGame.class,"custom_name"),
								Messages.get(WndStartGame.class, "custom_name_desc")+SPDSettings.heroName(),
								SPDSettings.heroName(), 20,
								false, Messages.get(WndStartGame.class,"custom_name_set"),
								Messages.get(WndStartGame.class,"custom_name_clear")){
							@Override
							public void onSelect(boolean name, String str) {
								if (name) {
									SPDSettings.heroName(str);
								} else {
									SPDSettings.heroName("");
								}
								icon(Icons.get(SPDSettings.heroName().equals("") ? RENAME_OFF : Icons.RENAME_ON));
							}
						}));
					} else {
						ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(HeroSelectScene.class,"unlock_rename")));
					}

				}
		};
		Rename.setSize( BUTTON_HEIGHT, BUTTON_HEIGHT );
		Rename.setPos( frame.x + frame.width + FRAME_MARGIN_X, frame.y-41+ frame.height-41- BUTTON_HEIGHT);
		add(Rename);

		IconButton DiffcultButton = new IconButton(new ItemSprite(ItemSpriteSheet.DIFFCULTBOOT)) {
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.scene().addToFront(new WndDLC(SPDSettings.dlc(), true));
			}
		};
		DiffcultButton.setSize( BUTTON_HEIGHT, BUTTON_HEIGHT );
		DiffcultButton.setPos( frame.x-58+frame.width-58+FRAME_MARGIN_X, frame.y-41+ frame.height-41- BUTTON_HEIGHT);
		add(DiffcultButton);

		btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );
		btnExit.visible = !SPDSettings.intro() || Rankings.INSTANCE.totalNumber > 0;
		if (landscape()) {
			Image title = new Image(Assets.Interfaces.MENUTITLE, 0, 0, 126, 37);

			//float topRegion = Math.max(title.height/2, 20f);


			title.setPos(frame.x - frame.width / 5f + FRAME_MARGIN_X / 5f, frame.y + frame.height / 4 - BUTTON_HEIGHT - 40);
			placeTorch(title.x - 8, title.y + 42);
			placeTorch(title.x + 132, title.y + 42);
			add(title);

			Image twotitle = new Image(Assets.Interfaces.Three_YEARS, 0, 0, 126, 34);

			//float topRegion = Math.max(title.height/2, 20f);

			twotitle.setPos(frame.x - frame.width / 5f + FRAME_MARGIN_X / 5f, frame.y + frame.height / 2 - BUTTON_HEIGHT + 100);
			add(twotitle);
		} else {
			Image title = new Image(Assets.Interfaces.MENUTITLE, 0, 0, 126, 37);

			//float topRegion = Math.max(title.height/2, 20f);

			title.setPos(frame.x - frame.width / 5f + FRAME_MARGIN_X / 5f, frame.y + frame.height / 8 - BUTTON_HEIGHT - 45);
			placeTorch(title.x - 8, title.y + 42);
			placeTorch(title.x + 132, title.y + 42);
			add(title);

			Image twotitle = new Image(Assets.Interfaces.Three_YEARS, 0, 0, 126, 34);

			//float topRegion = Math.max(title.height/2, 20f);

			twotitle.setPos(frame.x - frame.width / 5f + FRAME_MARGIN_X / 5f, frame.y + frame.height / 4 - BUTTON_HEIGHT - 40);
			add(twotitle);
		}

		fadeIn();
	}

	private void placeTorch( float x, float y ) {
		Fireball fb2 = new Fireball();
		fb2.setPos( x, y );
		add( fb2 );
	}

	@Override
	public void destroy() {
		Camera.remove( viewport );
		super.destroy();
	}

	private void setSelectedHero() {
		HeroClass cl = heroClass();
		GamesInProgress.selectedClass = cl;
		a.heroClass(cl);

		startBtn.text(Messages.titleCase(cl.title()));
		startBtn.textColor(Window.WHITE);
		startBtn.setSize(Math.max(startBtn.reqWidth() + 8, SKY_WIDTH - FRAME_MARGIN_X * 2), BUTTON_HEIGHT);
		startBtn.setPos( (Camera.main.width - startBtn.width())/2f, frame.y + frame.height + FRAME_MARGIN_X );
		PixelScene.align(startBtn);
		startBtn.enable( cl.isUnlocked() );

		infoButton.setPos(startBtn.right(), startBtn.top());

		challengeButton.setPos(startBtn.left()-challengeButton.width(), startBtn.top());
	}

	@Override
	public void onBackPressed() {
		ShatteredPixelDungeon.switchScene(TitleScene.class);
	}

	private static class Sky extends Visual {
		private static final int[][] gradients = new int[][] {
				{ 0xff012459, 0xff001322 },
				{ 0xff003972, 0xff001322 },
				{ 0xff003972, 0xff001322 },
				{ 0xff004372, 0xff00182b },
				{ 0xff004372, 0xff011d34 },
				{ 0xff016792, 0xff00182b },
				{ 0xff07729f, 0xff042c47 },
				{ 0xff12a1c0, 0xff07506e },
				{ 0xff74d4cc, 0xff1386a6 },
				{ 0xffefeebc, 0xff61d0cf },
				{ 0xfffee154, 0xffa3dec6 },
				{ 0xfffdc352, 0xffe8ed92 },
				{ 0xffffac6f, 0xffffe467 },
				{ 0xfffda65a, 0xffffe467 },
				{ 0xfffd9e58, 0xffffe467 },
				{ 0xfff18448, 0xffffd364 },
				{ 0xfff06b7e, 0xfff9a856 },
				{ 0xffca5a92, 0xfff4896b },
				{ 0xff5b2c83, 0xffd1628b },
				{ 0xff371a79, 0xff713684 },
				{ 0xff28166b, 0xff45217c },
				{ 0xff192861, 0xff372074 },
				{ 0xff040b3c, 0xff233072 },
				{ 0xff040b3c, 0xff012459 },
		};

		private SmartTexture texture;
		private FloatBuffer verticesBuffer;

		public Sky( int hour ) {
			super( 0, 0, 1, 1 );

			texture = TextureCache.createGradient( gradients[hour] );

			float[] vertices = new float[16];
			verticesBuffer = Quad.create();

			vertices[2]		= 0.25f;
			vertices[6]		= 0.25f;
			vertices[10]	= 0.75f;
			vertices[14]	= 0.75f;

			vertices[3]		= 0;
			vertices[7]		= 1;
			vertices[11]	= 1;
			vertices[15]	= 0;


			vertices[0] 	= 0;
			vertices[1] 	= 0;

			vertices[4] 	= 1;
			vertices[5] 	= 0;

			vertices[8] 	= 1;
			vertices[9] 	= 1;

			vertices[12]	= 0;
			vertices[13]	= 1;

			((Buffer)verticesBuffer).position( 0 );
			verticesBuffer.put( vertices );
		}

		@Override
		public void draw() {

			super.draw();

			NoosaScript script = NoosaScript.get();

			texture.bind();

			script.camera( camera() );

			script.uModel.valueM4( matrix );
			script.lighting(
					rm, gm, bm, am,
					ra, ga, ba, aa );

			script.drawQuad( verticesBuffer );
		}
	}

	private static class Cloud extends Image {

		private static int lastIndex = -1;

		public Cloud( float y, boolean dayTime ) {
			super( Assets.Interfaces.SURFACE );

			int index;
			do {
				index = Random.Int( 3 );
			} while (index == lastIndex);

			switch (index) {
				case 0:
					frame( 88, 0, 49, 20 );
					break;
				case 1:
					frame( 88, 20, 49, 22 );
					break;
				case 2:
					frame( 88, 42, 50, 18 );
					break;
			}

			lastIndex = index;

			this.y = y;

			scale.set( 1 - y / SKY_HEIGHT );
			x = Random.Float( SKY_WIDTH + width() ) - width();
			speed.x = scale.x * (dayTime ? +8 : -8);

			if (dayTime) {
				tint( 0xCCEEFF, 1 - scale.y );
			} else {
				rm = gm = bm = +3.0f;
				ra = ga = ba = -2.1f;
			}
		}

		@Override
		public void update() {
			super.update();
			if (speed.x > 0 && x > SKY_WIDTH) {
				x = -width();
			} else if (speed.x < 0 && x < -width()) {
				x = SKY_WIDTH;
			}
		}
	}

	private static class Avatar extends Image {

		private static final int WIDTH	= 64;
		private static final int HEIGHT	= 64;

		public Avatar( HeroClass cl ) {
			super( cl.GetSkinAssest() );
			frame( new TextureFilm( texture, WIDTH, HEIGHT ).get( cl.GetSkin() ) );
		}

		public void  heroClass( HeroClass cl ) {
			texture(cl.GetSkinAssest());
			frame( new TextureFilm( texture, WIDTH, HEIGHT ).get( cl.GetSkin() ) );
		}
	}

	private static class GrassPatch extends Image {

		public static final int WIDTH	= 16;
		public static final int HEIGHT	= 14;

		private float tx;
		private float ty;

		private double a = Random.Float( 5 );
		private double angle;

		private boolean forward;

		public GrassPatch( float tx, float ty, boolean forward ) {

			super( Assets.Interfaces.SURFACE );

			frame( 88 + Random.Int( 4 ) * WIDTH, 60, WIDTH, HEIGHT );

			this.tx = tx;
			this.ty = ty;

			this.forward = forward;
		}

		@Override
		public void update() {
			super.update();
			a += Random.Float( Game.elapsed * 5 );
			angle = (2 + Math.cos( a )) * (forward ? +0.2 : -0.2);



			scale.y = (float)Math.cos( angle );

			x = tx + (float)Math.tan( angle ) * width;
			y = ty - scale.y * height;
		}

		@Override
		protected void updateMatrix() {
			super.updateMatrix();
			Matrix.skewX( matrix, (float)(angle / Matrix.G2RAD) );
		}
	}

//TODO 待添加

//	private static Image foundChallangeIcon(int i) {
//
//		if (Challenges.NAME_IDS[i].equals("no_food")) {
//			return new ItemSprite(ItemSpriteSheet.OVERPRICED,new ItemSprite.Glowing( 0xFF0000 ));
//		} else if (Challenges.NAME_IDS[i].equals("no_armor")) {
//			return new ItemSprite(ItemSpriteSheet.ARMOR_MAGE,new ItemSprite.Glowing( 0xFF0000 ));
//		} else if (Challenges.NAME_IDS[i].equals("no_healing")) {
//			return new RatSprite();
//		} else if (Challenges.NAME_IDS[i].equals("no_herbalism")) {
//			return new GnollSprite();
//		} else if (Challenges.NAME_IDS[i].equals("darkness")) {
//			return Icons.get(Icons.NEWS);
//		} else if (Challenges.NAME_IDS[i].equals("no_scrolls")) {
//			return Icons.get(Icons.LANGS);
//		} else if (Challenges.NAME_IDS[i].equals("aquaphobia")) {
//			return Icons.get(Icons.SHPX);
//		} else if (Challenges.NAME_IDS[i].equals("rlpt")) {
//			return Icons.get(Icons.LIBGDX);
//		} else if (Challenges.NAME_IDS[i].equals("sbsg")) {
//			return Icons.get(Icons.WATA);
//		} else if (Challenges.NAME_IDS[i].equals("exsg")) {
//			return Icons.get(Icons.SHPX);
//		} else if (Challenges.NAME_IDS[i].equals("stronger_bosses")) {
//			return Icons.get(Icons.HUNTRESS);
//		} else if (Challenges.NAME_IDS[i].equals("dhxd")) {
//			return Icons.get(Icons.SKULL);
//		} else {
//			return Icons.get(Icons.STATS);
//		}
//
//	}

}