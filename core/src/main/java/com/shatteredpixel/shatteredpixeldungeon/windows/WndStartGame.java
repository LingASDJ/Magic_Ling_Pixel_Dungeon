/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import static com.shatteredpixel.shatteredpixeldungeon.ui.Icons.RENAME_OFF;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.journal.Journal;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.IntroScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.DeviceCompat;

import java.util.ArrayList;

public class WndStartGame extends Window {

	private static final int WIDTH    = 120;
	private static final int HEIGHT   = 160;

	private static class GameOptions extends Component {

		private NinePatch bg;

		private ArrayList<StyledButton> buttons;
		private ArrayList<ColorBlock> spacers;

		@Override
		protected void createChildren() {

			bg = Chrome.get(Chrome.Type.GREY_BUTTON_TR);
			add(bg);

			buttons = new ArrayList<>();
			spacers = new ArrayList<>();
			if (DeviceCompat.isDebug() || Badges.isUnlocked(Badges.Badge.VICTORY)){

//				StyledButton chkSaver = new StyledButton(Chrome.Type.RED_BUTTON, Messages.get(WndStartGame.class,
//						"mode"), 5){
//					@Override
//					protected void onClick() {
//						super.onClick();
//						ShatteredPixelDungeon.scene().addToFront(new WndDLC(SPDSettings.dlc(), true) {
//							public void onBackPressed() {
//								super.onBackPressed();
//								icon(new ItemSprite(SPDSettings.dlc() > 0 ? ItemSpriteSheet.DG26 :
//										ItemSpriteSheet.DG24));
//							}
//						} );
//					}
//
//				};
//				chkSaver.active = false;
//				chkSaver.alpha(0.5f);
//				chkSaver.icon(new ItemSprite(ItemSpriteSheet.DG26, null));
//				add( chkSaver );
//				buttons.add(chkSaver);

				StyledButton hardButton = new StyledButton(Chrome.Type.RED_BUTTON, Messages.get(WndStartGame.class,
						"hard"), 5){
					@Override
					protected void onClick() {
						ShatteredPixelDungeon.scene().addToFront(new WndChallenges(SPDSettings.challenges(), false, null) {
							public void onBackPressed() {
								super.onBackPressed();
								icon(Icons.get(SPDSettings.challenges() > 0 ? Icons.CHALLENGE_ON : Icons.CHALLENGE_OFF));
							}
						} );
					}
				};
				hardButton.leftJustify = true;
				hardButton.active = false;
				hardButton.icon(new ItemSprite(ItemSpriteSheet.ARTIFACT_HOURGLASS, null));
				hardButton.alpha(0f);
				add(hardButton);
				buttons.add(hardButton);

				StyledButton nameButton = new StyledButton(Chrome.Type.RED_BUTTON, Messages.get(WndStartGame.class,
						"seedtitle"), 5){
					@Override
					public void onClick() {
						if(Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_1)){
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
						}

					}

				};
				if(Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_1)){
					nameButton.active = true;
					nameButton.alpha(1f);
				} else {
					nameButton.active = false;
					nameButton.alpha(0.5f);
				}
				nameButton.leftJustify = true;
				nameButton.icon(Icons.get(RENAME_OFF));
				buttons.add(nameButton);
				add(nameButton);

				StyledButton csbutton = new StyledButton(Chrome.Type.RED_BUTTON, Messages.get(WndStartGame.class,
						"news"), 5){
					@Override
					protected void onClick() {
						ShatteredPixelDungeon.scene().addToFront(new WndChallenges(SPDSettings.challenges(), false, null) {
							public void onBackPressed() {
								super.onBackPressed();
								icon(Icons.get(SPDSettings.challenges() > 0 ? Icons.CHALLENGE_ON : Icons.CHALLENGE_OFF));
							}
						} );
					}
				};
				csbutton.leftJustify = true;
				csbutton.active = false;
				csbutton.icon(new ItemSprite(ItemSpriteSheet.ARTIFACT_SPELLBOOK, null));
				csbutton.alpha(0f);
				add(csbutton);
				buttons.add(csbutton);
			}
		}




		@Override
		protected void layout() {
			super.layout();

			bg.x = x;
			bg.y = y;

			int width = 0;
			for (StyledButton btn : buttons){
				if (width < btn.reqWidth()) width = (int)btn.reqWidth();
			}
			width += bg.marginHor();

			int top = (int)y + bg.marginTop() - 1;
			int i = 0;
			for (StyledButton btn : buttons){
				btn.setRect(x+bg.marginLeft(), top, width - bg.marginHor(), 16);
				top = (int)btn.bottom();
				if (i < spacers.size()) {
					spacers.get(i).size(btn.width(), 1);
					spacers.get(i).x = btn.left();
					spacers.get(i).y = PixelScene.align(btn.bottom()-0.5f);
					i++;
				}
			}

			this.width = width;
			this.height = top+bg.marginBottom()-y-1;
//			this.height = top+bg.marginBottom()-y-1;
			bg.size(this.width, this.height);

		}
	}
	private GameOptions optionsPane;
	public WndStartGame(final int slot){

		Badges.loadGlobal();
		Journal.loadGlobal();

		RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 12 );
		title.hardlight(Window.TITLE_COLOR);
		title.setPos( (WIDTH - title.width())/2f, 3);
		PixelScene.align(title);
		add(title);

		float heroBtnSpacing = (WIDTH - 5*HeroBtn.WIDTH)/5f;

		float curX = heroBtnSpacing;
		for (HeroClass cl : HeroClass.values()){
			HeroBtn button = new HeroBtn(cl);
			button.setRect(curX, title.height() + 7, HeroBtn.WIDTH, HeroBtn.HEIGHT);
			curX += HeroBtn.WIDTH + heroBtnSpacing;
			add(button);
		}

		ColorBlock separator = new ColorBlock(1, 1, 0xFF222222);
		separator.size(WIDTH, 1);
		separator.x = 0;
		separator.y = title.bottom() + 6 + HeroBtn.HEIGHT;
		add(separator);

		HeroPane ava = new HeroPane();
		ava.setRect(20, separator.y + 2, WIDTH-30, 80);
		add(ava);

		RedButton start = new RedButton(Messages.get(this, "start")){
			@Override
			protected void onClick() {
				if (GamesInProgress.selectedClass == null) return;

				super.onClick();

				GamesInProgress.curSlot = slot;
				Dungeon.hero = null;
				ActionIndicator.action = null;
				InterlevelScene.mode = InterlevelScene.Mode.DESCEND;

				//匹配两个值以及为安卓才显示
//				if(SPDSettings.customSeed().equals(KEY_CUSTOM_LING) && SPDSettings.customSeed().equals(KEY_CUSTOM_SEED) && isAndroid()){
//					WndStartGame.showKeyInput();
//				} else
				if (SPDSettings.intro()) {
					SPDSettings.intro( false );
					Game.switchScene( IntroScene.class );
				} else {
					Game.switchScene( InterlevelScene.class );
				}
			}

			@Override
			public void update() {
				if( !visible && GamesInProgress.selectedClass != null){
					visible = true;
				}
				super.update();
			}
		};
		start.visible = false;
		start.setRect(0, HEIGHT - 20, WIDTH, 20);
		add(start);

		if (DeviceCompat.isDebug() || Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_1)){
			IconButton challengeButton = new IconButton(
					Icons.get( SPDSettings.challenges() > 0 ? Icons.CHALLENGE_ON :Icons.CHALLENGE_OFF)){
				@Override
				protected void onClick() {
					ShatteredPixelDungeon.scene().addToFront(new WndChallenges(SPDSettings.challenges(), false, null) {
						public void onBackPressed() {
							super.onBackPressed();
							if (parent != null) {
								icon(Icons.get(SPDSettings.challenges() > 0 ?
										Icons.CHALLENGE_ON : Icons.CHALLENGE_OFF));
							}
						}
					} );
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
			};
			challengeButton.setRect(WIDTH - 20, HEIGHT - 20, 20, 20);
			challengeButton.visible = false;
			add(challengeButton);

		} else {
			Dungeon.challenges = 0;
			SPDSettings.challenges(0);
		}

		optionsPane = new GameOptions(){
			public void update() {
				if( !visible && GamesInProgress.selectedClass != null){
					visible = true;
					optionsPane.setRect(WIDTH-50, HEIGHT-120, 20, 20);
				}
				super.update();
			}
		};
		optionsPane.layout();
		optionsPane.visible = false;
		add(optionsPane);
		optionsPane.setRect(WIDTH-500, HEIGHT-1200, 20, 20);
		resize(WIDTH, HEIGHT);

	}



	private static class HeroBtn extends IconButton {

		private HeroClass cl;

		private Image hero;

		private static final int WIDTH = 24;
		private static final int HEIGHT = 16;

		HeroBtn ( HeroClass cl ){
			super();

			this.cl = cl;

			add(hero = new Image(cl.spritesheet(), 0, 90, 12, 15));

		}

		@Override
		protected void layout() {
			super.layout();
			if (hero != null){
				hero.x = x + (width - hero.width()) / 2f;
				hero.y = y + (height - hero.height()) / 2f;
				PixelScene.align(hero);
			}
		}

		@Override
		public void update() {
			super.update();
			if (cl != GamesInProgress.selectedClass){
				if (!cl.isUnlocked()){
					hero.brightness(0.3f);
				} else {
					hero.brightness(0.6f);
				}
			} else {
				hero.brightness(1f);
			}
		}

		@Override
		protected void onClick() {
			super.onClick();

			if( !cl.isUnlocked() ){
				ShatteredPixelDungeon.scene().addToFront( new WndMessage(cl.unlockMsg()));
			} else {
				GamesInProgress.selectedClass = cl;
			}
		}
	}

	private class HeroPane extends Component {

		private HeroClass cl;

		private Image avatar;

		private IconButton heroItem;
		private IconButton heroLoadout;
		private IconButton heroMisc;
		private IconButton heroSubclass;
		private IconButton Telnetsc;
		private IconButton infoButton;
		private RenderedTextBlock name;

		private static final int BTN_SIZE = 20;

		@Override
		protected void createChildren() {
			super.createChildren();

			avatar = new Image(Assets.Sprites.AVATARS);
			avatar.scale.set(2f);
			add(avatar);

			heroItem = new IconButton(){
				@Override
				protected void onClick() {
					if (cl == null) return;
					ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(cl, cl.name() + "_desc_item")));
				}
			};
			heroItem.setSize(BTN_SIZE, BTN_SIZE);
			add(heroItem);

			heroLoadout = new IconButton(){
				@Override
				protected void onClick() {
					if (cl == null) return;
					ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(cl, cl.name() + "_desc_loadout")));
				}
			};
			heroLoadout.setSize(BTN_SIZE, BTN_SIZE);
			add(heroLoadout);

			heroMisc = new IconButton(){
				@Override
				protected void onClick() {
					if (cl == null) return;
					ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(cl, cl.name() + "_desc_misc")));
				}
			};
			heroMisc.setSize(BTN_SIZE, BTN_SIZE);
			add(heroMisc);

			heroSubclass = new IconButton(new ItemSprite(ItemSpriteSheet.MASTERY, null)){
				@Override
				protected void onClick() {
					if (cl == null) return;
					String msg = Messages.get(cl, cl.name() + "_desc_subclasses");
					for (HeroSubClass sub : cl.subClasses()){
						msg += "\n\n" + sub.desc();
					}
					ShatteredPixelDungeon.scene().addToFront(new WndMessage(msg));
				}
			};
			heroSubclass.setSize(BTN_SIZE, BTN_SIZE);
			add(heroSubclass);

			Telnetsc = new IconButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_SPELLBOOK, null)){
				@Override
				protected void onClick() {
					if (cl == null) return;
					ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(cl, cl.name() + "_story")));
				}
			};
			Telnetsc.setSize(BTN_SIZE, BTN_SIZE);
			add(Telnetsc);

		 	/*Rename = new IconButton(
					Icons.get( Icons.INFO)){
				@Override
				protected void onClick() {
				}

				@Override
				public void update() {
					if( !visible && GamesInProgress.selectedClass != null){
						visible = true;
					}
					super.update();
				}
			};
			Rename.setSize(BTN_SIZE, BTN_SIZE);
			Rename.visible = false;
			add(Rename);*/

			name = PixelScene.renderTextBlock(12);
			add(name);

			infoButton = new IconButton(Icons.get(Icons.INFO)){
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.scene().addToFront(new WndHeroInfo(GamesInProgress.selectedClass));
				}

				@Override
				protected String hoverText() {
					return Messages.titleCase(Messages.get(WndKeyBindings.class, "hero_info"));
				}
			};
			infoButton.setSize(21, 21);
			add(infoButton);

			visible = false;
		}

		@Override
		protected void layout() {
			super.layout();

			avatar.x = x-12;
			avatar.y = y + (height - avatar.height() - name.height() - 4)/2f;
			PixelScene.align(avatar);

			name.setPos(
					x-2 + (avatar.width() - name.width())/2f,
					avatar.y + avatar.height() + 3
			);
			PixelScene.align(name);

			infoButton.setPos(-4,
					name.y-15 + name.height());

			heroItem.setPos(0 + (name.width()/60f),
					name.y + name.height() + 3);

			heroLoadout.setPos(25 + (name.width()/60f),
					name.y + name.height() + 3);

			heroMisc.setPos(50 + (name.width()/60f),
					name.y + name.height() + 3);

			heroSubclass.setPos(75 + (name.width()/60f),
					name.y + name.height() + 3);

			Telnetsc.setPos(100 + (name.width()/60f),
					name.y + name.height() + 3);;
		}

		@Override
		public synchronized void update() {
			super.update();
			if (GamesInProgress.selectedClass != cl){
				cl = GamesInProgress.selectedClass;
				if (cl != null) {
					avatar.frame(cl.ordinal() * 24, 0, 24, 32);

					name.text(Messages.capitalize(cl.title()));

					switch(cl){
						case WARRIOR:
							heroItem.icon(new ItemSprite(ItemSpriteSheet.SEAL, null));
							heroLoadout.icon(new ItemSprite(ItemSpriteSheet.WORN_SHORTSWORD, null));
							heroMisc.icon(new ItemSprite(ItemSpriteSheet.RATION, null));
							break;
						case MAGE:
							heroItem.icon(new ItemSprite(ItemSpriteSheet.MAGES_STAFF, null));
							heroLoadout.icon(new ItemSprite(ItemSpriteSheet.HOLDER, null));
							heroMisc.icon(new ItemSprite(ItemSpriteSheet.WAND_MAGIC_MISSILE, null));
							break;
						case ROGUE:
							heroItem.icon(new ItemSprite(ItemSpriteSheet.ARTIFACT_CLOAK, null));
							heroLoadout.icon(new ItemSprite(ItemSpriteSheet.DAGGER, null));
							heroMisc.icon(Icons.get(Icons.DEPTH));
							break;
						case HUNTRESS:
							heroItem.icon(new ItemSprite(ItemSpriteSheet.SPIRIT_BOW, null));
							heroLoadout.icon(new ItemSprite(ItemSpriteSheet.GLOVES, null));
							heroMisc.icon(new Image(Assets.Environment.TILES_SEWERS, 112, 96, 16, 16 ));
							break;
					}

					layout();

					visible = true;
				} else {
					visible = false;
				}
			}
		}
	}

}
