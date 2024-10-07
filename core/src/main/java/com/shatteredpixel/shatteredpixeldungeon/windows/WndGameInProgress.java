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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Clipboard;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.custom.messages.M;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.StartScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import java.io.IOException;
import java.util.Locale;

public class WndGameInProgress extends Window {
	
	private static final int WIDTH    = 120;
	
	private int GAP	  = 6;
	
	private float pos;
	Clipboard clipboard;
	
	public WndGameInProgress(final int slot){
		clipboard = Gdx.app.getClipboard();
		final GamesInProgress.Info info = GamesInProgress.check(slot);
		String className;
		assert info != null;
		if (info.subClass != HeroSubClass.NONE){
			className = info.subClass.title();
		} else {
			className = info.heroClass.title();
		}
		
		IconTitle title = new IconTitle();
		title.icon( HeroSprite.avatar(info.heroClass, info.armorTier) );

		if (info.name.isEmpty()) {
			title.label(Messages.get(this, "title", info.level, className.toUpperCase(Locale.ENGLISH)));
		} else {
			String csname = info.name +
					"\n" +
					Messages.get(this, "title", info.level, className);
			title.label(csname.toUpperCase(Locale.ENGLISH));
		}


		title.color(Window.TITLE_COLOR);
		title.setRect( 0, 0, WIDTH, 0 );
		add(title);
		
		//manually produces debug information about a run, mainly useful for levelgen errors
		Button debug = new Button();
		debug.setRect(0, 0, title.imIcon.width(), title.imIcon.height);
		add(debug);
		
		if (info.challenges > 0) GAP -= 2;
		
		pos = title.bottom() + GAP;

		RedButton btnGameInfo;

		if (info.challenges > 0) {
			RedButton btnChallenges = new RedButton( Messages.get(this, "challenges") ) {
				@Override
				protected void onClick() {
					Game.scene().add( new WndChallenges( info.challenges, false,null ) );
				}
			};
			btnChallenges.icon(Icons.get(Icons.CHANGES));
			float btnW = btnChallenges.reqWidth() + 2;
			btnChallenges.setRect( (WIDTH - btnW)/2, pos, btnW , 18 );
			add( btnChallenges );

			btnGameInfo = new RedButton( Messages.get(this, "gameinfo") ) {
				@Override
				protected void onClick() {
					try {
						Bundle bundle = FileUtils.bundleFromFile(GamesInProgress.gameFile(slot));
						String ing =
								Messages.get(WndGameInProgress.class,"gameversion") +(info.version == 0 ? "NULL": info.version)+"\n\n"+
										Messages.get(WndGameInProgress.class,"gameseed")+ DungeonSeed.convertToCode(bundle.getLong("seed"))+"\n\n"+
										Messages.get(WndGameInProgress.class,"gamegold") + bundle.getInt("gold") +"\n\n"+
										Messages.get(WndGameInProgress.class,"gamenayzi") + bundle.getInt("naiyaziCollected")+
										Messages.get(WndGameInProgress.class,"gamenayzis") +"\n\n"+
										Messages.get(WndGameInProgress.class,"gamemimic") + bundle.getInt("goldchest") +"\n\n"+
										Messages.get(WndGameInProgress.class,"gameinof");
						ShatteredPixelDungeon.scene().addToFront(new WndMessage(ing));
					} catch (IOException ignored) {
					}
				}
			};
			btnGameInfo.icon(new ItemSprite(ItemSpriteSheet.SEED_SKYBLUEFIRE));
			btnGameInfo.setRect( btnChallenges.right()+2, pos, btnGameInfo.reqWidth() + 1 , 18 );
			add( btnGameInfo );
		} else {

			btnGameInfo = new RedButton( Messages.get(this, "gameinfo") ) {
				@Override
				protected void onClick() {
					try {
						Bundle bundle = FileUtils.bundleFromFile(GamesInProgress.gameFile(slot));
						String ing =
								Messages.get(WndGameInProgress.class,"gameversion") + Game.version+"\n\n"+
										Messages.get(WndGameInProgress.class,"gameseed")+ DungeonSeed.convertToCode(bundle.getLong("seed"))+"\n\n"+
										Messages.get(WndGameInProgress.class,"gamegold") + bundle.getInt("gold") +"\n\n"+
										Messages.get(WndGameInProgress.class,"gamenayzi") + bundle.getInt("naiyaziCollected")+
										Messages.get(WndGameInProgress.class,"gamenayzis") +"\n\n"+
										Messages.get(WndGameInProgress.class,"gamemimic") + bundle.getInt("goldchest") +"\n\n"+
										Messages.get(WndGameInProgress.class,"gameinof");
						ShatteredPixelDungeon.scene().addToFront(new WndMessage(ing));
					} catch (IOException ignored) {
					}
				}
			};
			btnGameInfo.icon(new ItemSprite(ItemSpriteSheet.SEED_SKYBLUEFIRE));
			btnGameInfo.setRect( 20, pos, btnGameInfo.reqWidth() + 1 , 18 );
			add( btnGameInfo );
		}



		RedButton buttonSeed = new RedButton(M.L(WndGameInProgress.class, "copy_seed"), 8){
			@Override
			protected void onClick() {
				super.onClick();

				boolean seedType = (info.challenges & Challenges.MOREROOM) != 0;

				clipboard.setContents(M.L(WndGameInProgress.class, "seed_copy",info.customSeed.isEmpty() ? DungeonSeed.convertToCode(info.seed) : info.customSeed,(seedType ? "B" : "A"),info.version));

				Game.runOnRenderThread(() -> ShatteredPixelDungeon.scene().add(new WndMessage(M.L(WndGameInProgress.class, "seed_copied",info.customSeed.isEmpty() ? DungeonSeed.convertToCode(info.seed) : info.customSeed,(seedType ? "B" : "A"),info.version))));
			}
		};
		add(buttonSeed);
		boolean multiLine=btnGameInfo.right()+buttonSeed.reqWidth()>WIDTH;
		float btnX,btnY;
		btnX = multiLine?2:btnGameInfo.right()+2;
		btnY = multiLine?btnGameInfo.bottom()+2:pos;
		buttonSeed.setRect(btnX, btnY, buttonSeed.reqWidth() + 1, 18);

		RedButton btDLC = new RedButton( Messages.get(this, "dlc") ) {
			@Override
			protected void onClick() {
				//
			}
		};
		btDLC.icon(new ItemSprite(ItemSpriteSheet.DIFFCULTBOOT));
		btDLC.alpha(0.7f);
		btDLC.setRect( 80, title.y, 40 , 18 );
		//add( btDLC );

		pos = buttonSeed.bottom() + GAP;
		
		pos += GAP;

		int strBonus = info.strBonus;
		if (strBonus > 0)           statSlot( Messages.get(this, "str"), info.str + " + " + strBonus );
		else if (strBonus < 0)      statSlot( Messages.get(this, "str"), info.str + " - " + -strBonus );
		else                        statSlot( Messages.get(this, "str"), info.str );
		if (info.shld > 0)  statSlot( Messages.get(this, "health"), info.hp + "+" + info.shld + "/" + info.ht );
		else                statSlot( Messages.get(this, "health"), (info.hp) + "/" + info.ht );
		statSlot( Messages.get(this, "exp"), info.exp + "/" + Hero.maxExp(info.level) );

		//tatSlot( Messages.get(this, "icehp"), (info.icehp) + "/" + 100 );
		
		pos += GAP;
		statSlot( Messages.get(this, "gold"), info.goldCollected );
		statSlot( Messages.get(this, "depth"), info.maxDepth );

		if (!info.customSeed.isEmpty()){
			statSlot( Messages.get(this, "custom_seed"), "_" + info.customSeed + "_" );
		} else {
			statSlot( Messages.get(this, "dungeon_seed"), DungeonSeed.convertToCode(info.seed) );
		}
		
		pos += GAP;
		
		RedButton cont = new RedButton(Messages.get(this, "continue")){
			@Override
			protected void onClick() {
				super.onClick();
				
				GamesInProgress.curSlot = slot;
				
				hero = null;
				ActionIndicator.action = null;
				InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
				ShatteredPixelDungeon.switchScene(InterlevelScene.class);
			}
		};
		
		RedButton erase = new RedButton( Messages.get(this, "erase")){
			@Override
			protected void onClick() {
				super.onClick();
				
				ShatteredPixelDungeon.scene().add(new WndOptions(Icons.get(Icons.WARNING),
						Messages.get(WndGameInProgress.class, "erase_warn_title"),
						Messages.get(WndGameInProgress.class, "erase_warn_body"),
						Messages.get(WndGameInProgress.class, "erase_warn_yes"),
						Messages.get(WndGameInProgress.class, "erase_warn_no") ) {
					@Override
					protected void onSelect( int index ) {
						if (index == 0) {
							Dungeon.deleteGame(slot, true);
							ShatteredPixelDungeon.switchNoFade(StartScene.class);
						}
					}
				} );
			}
		};

		cont.icon(Icons.get(Icons.ENTER));
		cont.setRect(0, pos, WIDTH/2f -1, 20);
		add(cont);

		erase.icon(Icons.get(Icons.CLOSE));
		erase.setRect(WIDTH/2f + 1, pos, WIDTH/2f - 1, 20);
		add(erase);
		
		resize(WIDTH, (int)cont.bottom()+1);
	}
	
	private void statSlot( String label, String value ) {
		
		RenderedTextBlock txt = PixelScene.renderTextBlock( label, 8 );
		txt.setPos(0, pos);
		add( txt );
		
		txt = PixelScene.renderTextBlock( value, 8 );
		txt.setPos(WIDTH * 0.6f, pos);
		PixelScene.align(txt);
		add( txt );
		
		pos += GAP + txt.height();
	}
	
	private void statSlot( String label, int value ) {
		statSlot( label, Integer.toString( value ) );
	}
}
