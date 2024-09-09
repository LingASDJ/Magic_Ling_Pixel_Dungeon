/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Clipboard;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder.SeedFinder;
import com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder.WndCatalog;
import com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder.WndSeedfinderLog;
import com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder.WndSeedfinderSeedinput;
import com.shatteredpixel.shatteredpixeldungeon.effects.BannerSprites;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSettings;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.DeviceCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class SeedFinderScene extends PixelScene {

	@Override
	public void create() {

		super.create();

		Music.INSTANCE.playTracks(
				new String[]{Assets.Music.SAND, Assets.Music.THEME_2},
				new float[]{1, 1},
				true);

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );

		Image title = BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON );
		add( title );

		float topRegion = Math.max(title.height - 6, h*0.45f);

		title.x = (w - title.width()) / 2f;
		title.y = 2 + (topRegion - title.height()) / 2f;

		align(title);

		Image signs = new Image( BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON_SIGNS ) ) {
			private float time = 0;
			@Override
			public void update() {
				super.update();
				am = Math.max(0f, (float)Math.sin( time += Game.elapsed ));
				if (time >= 1.5f*Math.PI) time = 0;
			}
			@Override
			public void draw() {
				Blending.setLightMode();
				super.draw();
				Blending.setNormalMode();
			}
		};
		signs.x = title.x + (title.width() - signs.width())/2f;
		signs.y = title.y;
		add( signs );

		final Chrome.Type GREY_TR = Chrome.Type.GREY_BUTTON_TR;

		StyledButton btnScout = new StyledButton(GREY_TR, Messages.get(SeedFinderScene.class, "scout_seed_button")) {
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.scene()
						.addToFront(new WndSeedfinderSeedinput(Messages.get(SeedFinderScene.class, "scout_custom_seed_title"),
								Messages.get(SeedFinderScene.class, "scout_info_text"),
								SPDSettings.seedinputText(),
								20,
								false,
								Messages.get(SeedFinderScene.class, "scout_button_yes"),
								Messages.get(SeedFinderScene.class, "scout_button_no")) {
							@Override
							public void onSelect(boolean positive, String text) {
								if (positive && text != null && !text.isEmpty()) {
									SPDSettings.seedinputText(text);

									text = DungeonSeed.formatText(text);
									long seed = DungeonSeed.convertFromText(text);

									String[] seedfinderOutputLog = new SeedFinder().logSeedItemsSeededRun(seed);

									ShatteredPixelDungeon.scene().addToFront(
											new WndSeedfinderLog(Icons.get(Icons.BACKPACK),
													"Items for seed " + DungeonSeed.convertToCode(Dungeon.seed),
													seedfinderOutputLog));
								} else {
									SPDSettings.seedinputText("");
								}
							}
						});
			}
		};
		btnScout.icon(Icons.get(Icons.ENTER));
		add(btnScout);

		StyledButton btnSeedfinder = new StyledButton(GREY_TR, Messages.get(this, "seedfinder_button")){
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.scene()
						.addToFront(new WndSeedfinderSeedinput(Messages.get(SeedFinderScene.class, "seedfinder_title"),
								Messages.get(SeedFinderScene.class, "seedfinder_info_text"),
								SPDSettings.seeditemsText(),
								100,
								true,
								Messages.get(SeedFinderScene.class, "seedfinder_button_yes"),
								Messages.get(SeedFinderScene.class, "seedfinder_button_no")) {
							@Override
							public void onSelect(boolean positive, String seeditems_userInput) {
								if (positive) {
									SPDSettings.seeditemsText(seeditems_userInput);

									//activate the seedfinder. this one takes a while
									String foundSeed = new SeedFinder().find_seed(seeditems_userInput);

									//copy seed to clipboard on success
									Clipboard clipboard = Gdx.app.getClipboard();
									clipboard.setContents(foundSeed);

									long seed = DungeonSeed.convertFromText(foundSeed);

									String[] seedfinderOutputLog = new SeedFinder().logSeedItemsSeededRun(seed);

									ShatteredPixelDungeon.scene().addToFront(
											new WndSeedfinderLog(Icons.get(Icons.BACKPACK),
													"Found seed " + DungeonSeed.convertToCode(Dungeon.seed),
													seedfinderOutputLog));

								} else {
									SPDSettings.seeditemsText("");
								}
							}
						});
			}
		};
		btnSeedfinder.icon(Icons.get(Icons.MAGNIFY));
		add(btnSeedfinder);

		StyledButton btnScoutDaily = new StyledButton(GREY_TR, Messages.get(this, "scout_daily")) {
			@Override
			protected void onClick() {
				String[] seedfinderOutputLog = new SeedFinder().logSeedItemsDailyRunRun(0);

				long DAY = 1000 * 60 * 60 * 24;
				long currentDay = (long) Math.floor((double) Game.realTime / DAY) + SeedFinder.Options.DailyOffset;
				SPDSettings.lastDaily(DAY * currentDay);
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
				format.setTimeZone(TimeZone.getTimeZone("UTC"));
				String date = format.format(new Date(SPDSettings.lastDaily()));

				ShatteredPixelDungeon.scene().addToFront(
						new WndSeedfinderLog(Icons.get(Icons.BACKPACK),
								"日常挑战于(MLPD日常尚未实装 仅供预览)： " + date,
								seedfinderOutputLog));
			}
		};
		btnScoutDaily.icon(Icons.get(Icons.ENTER));
		add(btnScoutDaily);
		Dungeon.daily = Dungeon.dailyReplay = false;

		StyledButton btnCatalog = new StyledButton(GREY_TR, Messages.get(this, "item_catalog")){
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.scene().addToFront(new WndCatalog());
			}
		};
		btnCatalog.icon(new ItemSprite(ItemSpriteSheet.POTION_AZURE));
		add(btnCatalog);

		StyledButton btnSource = new StyledButton(GREY_TR, Messages.get(this, "source")) {
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.platform.openURI("https://github.com/Elektrochecker/shpd-toolkit");
			}
		};
		btnSource.icon(Icons.get(Icons.NEWS));
		add(btnSource);

		StyledButton btnSettings = new SettingsButton(GREY_TR, Messages.get(TitleScene.class, "settings"));
		add(btnSettings);

		final int BTN_HEIGHT = 20;
		int GAP = (int)(h - topRegion - (landscape() ? 3 : 4)*BTN_HEIGHT)/3;
		GAP /= landscape() ? 3 : 5;
		GAP = Math.max(GAP, 2);

		if (landscape()) {
			btnScout.setRect(title.x-50, topRegion+GAP, ((title.width()+100)/2)-1, BTN_HEIGHT);
			align(btnScout);
			btnSeedfinder.setRect(btnScout.right()+2, btnScout.top(), btnScout.width(), BTN_HEIGHT);
			btnScoutDaily.setRect(btnScout.left(), btnScout.bottom()+ GAP, btnScout.width(), BTN_HEIGHT);
			btnCatalog.setRect(btnScoutDaily.left(), btnScoutDaily.bottom()+GAP, btnScoutDaily.width(), BTN_HEIGHT);
			btnSource.setRect(btnScoutDaily.right()+2, btnScoutDaily.top(), btnScoutDaily.width(), BTN_HEIGHT);
			btnSettings.setRect(btnCatalog.right()+2, btnScoutDaily.bottom()+ GAP, btnScoutDaily.width(), BTN_HEIGHT);
		} else {
			btnScout.setRect(title.x, topRegion+GAP, title.width(), BTN_HEIGHT);
			align(btnScout);
			btnSeedfinder.setRect(btnScout.left(), btnScout.bottom()+ GAP, btnScout.width(), BTN_HEIGHT);
			btnScoutDaily.setRect(btnScout.left(), btnSeedfinder.bottom()+ GAP, (btnScout.width()/2)-1, BTN_HEIGHT);
			btnCatalog.setRect(btnScoutDaily.right()+2, btnScoutDaily.top(), btnScoutDaily.width(), BTN_HEIGHT);
			btnSource.setRect(btnScoutDaily.left(), btnScoutDaily.bottom()+ GAP, btnScoutDaily.width(), BTN_HEIGHT);
			btnSettings.setRect(btnScoutDaily.right()+2, btnScoutDaily.bottom()+ GAP, btnScoutDaily.width(), BTN_HEIGHT);
		}

		BitmapText version = new BitmapText( "v" + Game.version, pixelFont);
		version.measure();
		version.hardlight( 0x888888 );
		version.x = w - version.width() - 4;
		version.y = h - version.height() - 2;
		add( version );

		if (DeviceCompat.isDesktop()) {
			ExitButton btnExit = new ExitButton();
			btnExit.setPos( w - btnExit.width(), 0 );
			add( btnExit );
		}

		fadeIn();
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

}
