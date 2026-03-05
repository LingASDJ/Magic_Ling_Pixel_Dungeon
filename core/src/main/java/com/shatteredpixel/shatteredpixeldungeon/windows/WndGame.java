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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.CS;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation.appear;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.custom.Gift;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.RankingsScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Callback;

import java.io.IOException;

public class WndGame extends Window {

	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final int GAP		= 2;
	private int pos;
	
	public WndGame() {
		
		super();

		//settings
		RedButton curBtn;
		addButton( curBtn = new RedButton( Messages.get(this, "settings") ) {
			@Override
			protected void onClick() {
				hide();
				GameScene.show(new WndSettings());
			}
		});
		curBtn.icon(Icons.get(Icons.PREFS));

		addButton(curBtn = new RedButton(Messages.get(this, "code")) {
			@Override
			protected void onClick() {
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {


						if((Dungeon.isDLC(Conducts.Conduct.DEV))) {
							ShatteredPixelDungeon.scene().addToFront(new WndError(Messages.get(WndLuoWhite.class, "key_not_rmode")) {
								public void onBackPressed() {
									super.onBackPressed();
								}
							});
						} else if (Statistics.RandMode){
							ShatteredPixelDungeon.scene().addToFront(new WndError(Messages.get(WndLuoWhite.class, "key_not_rmode")) {
								public void onBackPressed() {
									super.onBackPressed();
								}
							});
						} else if (Statistics.bossRushMode){
							ShatteredPixelDungeon.scene().addToFront(new WndError(Messages.get(WndLuoWhite.class, "key_not_rmode")) {
								public void onBackPressed() {
									super.onBackPressed();
								}
							});
						} else {
							GameScene.show( new WndTextInput( Messages.get( WndLuoWhite.class, "key_title" ),
									Messages.get( WndLuoWhite.class, "key_desc" ),
									"",
									99,
									false,
									Messages.get( WndLuoWhite.class, "key_confirm" ),
									Messages.get( WndLuoWhite.class, "key_cancel" ) ){
								@Override
								public void onSelect(boolean positive, String text) {
									if ( positive){
										int result = Gift.ActivateGift( text );
										switch ( result ){
											case 0:
												ShatteredPixelDungeon.scene().addToFront( new WndError( Messages.get( WndLuoWhite.class, "no_internet" ) ) {
													public void onBackPressed() {
														super.onBackPressed();
													}
												} );
												break;
											case 1:
												ShatteredPixelDungeon.scene().addToFront( new WndTitledMessage( Icons.INFO.get(), Messages.get( WndLuoWhite.class,"success" ), Messages.get( WndLuoWhite.class, "key_activation" ) ) {
													public void onBackPressed() {
														super.onBackPressed();
													}
												} );
												break;
											case 2:
												ShatteredPixelDungeon.scene().addToFront( new WndError( Messages.get( WndLuoWhite.class, "key_expired" ) ) {
													public void onBackPressed() {
														super.onBackPressed();
													}
												} );
												break;
											case 3:
												ShatteredPixelDungeon.scene().addToFront( new WndError( Messages.get( WndLuoWhite.class, "key_used" ) ) {
													public void onBackPressed() {
														super.onBackPressed();
													}
												} );
												break;
											case 4:
												ShatteredPixelDungeon.scene().addToFront( new WndError( Messages.get( WndLuoWhite.class, "key_not_null" ) ) {
													public void onBackPressed() {
														super.onBackPressed();
													}
												} );
												break;
											case 5:
												ShatteredPixelDungeon.scene().addToFront( new WndError( Messages.get( WndLuoWhite.class, "key_not_found" ) ) {
													public void onBackPressed() {
														super.onBackPressed();
													}
												} );
												break;
										}
									}
								}
							});
						}

					}
				});
			}
		});
		curBtn.icon(new ItemSprite(ItemSpriteSheet.CHEST));

		// Challenges window
		if (Dungeon.challenges > 0) {
			addButton( curBtn = new RedButton( Messages.get(this, "challenges") ) {
				@Override
				protected void onClick() {
					hide();
					GameScene.show( new WndChallenges( Dungeon.challenges, false,null ) );
				}
			} );
			curBtn.icon(Icons.get(Icons.CHALLENGE_ON));
		}

		boolean shouldRestart = Dungeon.hero == null || !Dungeon.hero.isAlive();

		addButton(curBtn = new RedButton(Messages.get(this, shouldRestart ? "start" : "restart" )) {
			@Override
			protected void onClick() {
				super.onClick();
				GameScene.show(new WndRestart());
			}
		});
		curBtn.icon(Icons.get( shouldRestart ? Icons.ENTER : Icons.CHANGES ));

		// Restart
		if (shouldRestart) {
			curBtn.textColor(Window.TITLE_COLOR);
			addButton( curBtn = new RedButton( Messages.get(this, "rankings") ) {
				@Override
				protected void onClick() {
					InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
					Music.INSTANCE.playTracks(
							new String[]{Assets.Music.THEME_1, Assets.Music.THEME_2},
							new float[]{1, 1},
							false);
					Game.switchScene( RankingsScene.class );
				}
			} );
			curBtn.icon(Icons.get(Icons.RANKINGS));
		}

		// Main menu
		addButton(curBtn = new RedButton(Messages.get(this, "menu")) {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
					ShatteredPixelDungeon.reportException(e);
				}
				Game.switchScene(TitleScene.class);
				BossHealthBar.clearAllBossData();
			}
		});
		curBtn.icon(Icons.get(Icons.DISPLAY));
		if (SPDSettings.intro()) curBtn.enable(false);

		if(hero != null){
			if(!Dungeon.hero.ready) {
				// Debug
				addButton(curBtn = new RedButton(Messages.get(this, "debug")) {
					@Override
					protected void onClick() {
						GameScene.logActorThread = true;
					}
				});
				curBtn.icon(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16));
			}
		}

		if(Dungeon.depth == 0 && Dungeon.branch == 0 && !Dungeon.isChallenged(CS) ) {
			// Debug
			addButton(curBtn = new RedButton(Messages.get(this, "restar")) {
				@Override
				protected void onClick() {
					appear( hero, 37 );
					Dungeon.level.occupyCell(hero );
					Dungeon.observe();
					GameScene.updateFog();
				}
			});
			curBtn.icon(Icons.get(Icons.TALENT));
		}



		resize( WIDTH, pos );
	}
	
	private void addButton( RedButton btn ) {
		add( btn );
		btn.setRect( 0, pos > 0 ? pos += GAP : 0, WIDTH, BTN_HEIGHT );
		pos += BTN_HEIGHT;
	}

	private void addButtons( RedButton btn1, RedButton btn2 ) {
		add( btn1 );
		btn1.setRect( 0, pos > 0 ? pos += GAP : 0, (WIDTH - GAP) / 2, BTN_HEIGHT );
		add( btn2 );
		btn2.setRect( btn1.right() + GAP, btn1.top(), WIDTH - btn1.right() - GAP, BTN_HEIGHT );
		pos += BTN_HEIGHT;
	}
}
