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
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.RollLevel;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.lanterfireactive;
import static com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero.badLanterFire;
import static com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero.goodLanterFire;
import static com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.scene;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessGoodSTR;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessMixShiled;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessMobDied;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessNoMoney;
import com.shatteredpixel.shatteredpixeldungeon.effects.BannerSprites;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Banner;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

public class WndStory extends Window {


	private static final int WIDTH_P = 125;
	private static final int WIDTH_L = 160;
	private static final int MARGIN = 2;

	public static final int ID_FOREST		= -1;
	public static final int ID_SEWERS		= 0;
	public static final int ID_PRISON		= 1;
	public static final int ID_CAVES		= 2;
	public static final int ID_CITY     	= 3;
	public static final int ID_HALLS		= 4;

	public static final int ID_SEWERSBOSS		= 5;

	public static final int ID_EXSEWERSBOSS		= 30;
	public static final int ID_PRISONBOSS		= 6;
	public static final int ID_CAVESBOSS		= 7;
	public static final int ID_CITYSBOSS		= 8;
	public static final int ID_HALLSBOOS		= 9;
	public static final int ID_CHAPTONEEND		= 10;
	public static final int ID_ICEBOSS		= 11;

	public static final int ID_COLDCHESTBOSS		= 29;


	//DLC BOSSRUSH
	public static final int ID_GAME = 12;
	public static final int ID_NOMOBS = 14;
	public static final int ID_CALA = 15;
	public static final int ID_SEWS = 16;
	public static final int ID_SKBS = 17;
	public static final int ID_TGKS = 18;
	public static final int ID_DKBS = 19;
	public static final int ID_DMBS = 20;
	public static final int ID_DKVS = 21;
	public static final int ID_ICES = 22;
	public static final int ID_DKKX = 23;
	public static final int ID_LXKS = 24;
	public static final int ID_FBXA = 25;
	public static final int ID_ZTBS = 26;
	public static final int ID_DMZR = 27;
	public static final int ID_ENDS = 28;

	public static final int ID_ANCITY = 28;
	//
	public static final int ID_DWADA		= 13;

	public static final int ID_ALC		= 33;

	private static final SparseArray<String> CHAPTERS = new SparseArray<>();

	static {
		CHAPTERS.put( ID_FOREST, "forest" );
		CHAPTERS.put( ID_SEWERS, "sewers" );
		CHAPTERS.put( ID_PRISON, "prison" );
		CHAPTERS.put( ID_CAVES, "caves" );
		CHAPTERS.put( ID_CITY, "city" );
		CHAPTERS.put( ID_HALLS, "halls" );
		CHAPTERS.put( ID_SEWERSBOSS, "sewersboss" );
		CHAPTERS.put( ID_EXSEWERSBOSS, "exboss1" );
		CHAPTERS.put( ID_PRISONBOSS, "prisonboss" );
		CHAPTERS.put( ID_CAVESBOSS, "cavesboss" );
		CHAPTERS.put( ID_CITYSBOSS, "cityboss" );
		CHAPTERS.put( ID_HALLSBOOS, "hallsboss" );
		CHAPTERS.put( ID_CHAPTONEEND, "new" );
		CHAPTERS.put( ID_ICEBOSS, "icedead" );
		CHAPTERS.put( ID_COLDCHESTBOSS, "coldchest" );
		CHAPTERS.put( ID_GAME, "bossrushstart" );
		CHAPTERS.put( ID_NOMOBS, "nomobs" );
		CHAPTERS.put( ID_CALA, "calaboss" );
		CHAPTERS.put( ID_SEWS, "sewsboss" );
		CHAPTERS.put( ID_SKBS, "skingboss" );
		CHAPTERS.put( ID_TGKS, "tenguboss" );
		CHAPTERS.put( ID_DKBS, "dimandkingboss" );
		CHAPTERS.put( ID_DMBS, "dm300boss" );
		CHAPTERS.put( ID_DKVS, "dm720boss" );
		CHAPTERS.put( ID_ICES, "icegirlboss" );
		CHAPTERS.put( ID_DKKX, "kawboss" );
		CHAPTERS.put( ID_LXKS, "lkxboss" );
		CHAPTERS.put( ID_FBXA, "fireboss" );
		CHAPTERS.put( ID_ZTBS, "zotboss" );
		CHAPTERS.put( ID_DMZR, "dmzero" );
		CHAPTERS.put( ID_ENDS, "end" );
		CHAPTERS.put( ID_DWADA, "drawfmaster" );

		CHAPTERS.put( ID_ANCITY, "aientncity" );

		CHAPTERS.put( ID_ALC, "aic" );
	}



	private IconTitle ttl;
	private RenderedTextBlock tf;

	private float delay;

	public WndStory( String text ) {
		this( null, null, text );
	}

	public WndStory(Image icon, String title, String text ) {
		super( 0, 0, Chrome.get( Chrome.Type.SCROLL ) );

		int width = PixelScene.landscape() ? WIDTH_L - MARGIN * 2: WIDTH_P - MARGIN *2;

		float y = MARGIN;
		if (icon != null && title != null){
			ttl = new IconTitle(icon, title);
			ttl.setRect(MARGIN, y, width-2*MARGIN, 0);
			y = ttl.bottom()+MARGIN;
			add(ttl);
			ttl.tfLabel.invert();
		}

		tf = PixelScene.renderTextBlock( text, 6 );
		tf.maxWidth(width);
		tf.invert();
		tf.setPos(MARGIN, y);
		add( tf );

		add( new PointerArea( chrome ) {
			@Override
			protected void onClick( PointerEvent event ) {
				hide();
			}
		} );

		resize( (int)(tf.width() + MARGIN * 2), (int)Math.min( tf.bottom()+MARGIN, 180 ) );
	}

	@Override
	public void update() {
		super.update();

		if (delay > 0 && (delay -= Game.elapsed) <= 0) {
			shadow.visible = chrome.visible = tf.visible = true;
			if (ttl != null) ttl.visible = true;
		}
	}


	@Override
	public void hide() {
		super.hide();
		Banner mapnameSlain = new Banner( BannerSprites.get( BannerSprites.Type.NULL ) );
		if(!Statistics.happyMode){
			switch (Dungeon.depth) {
				case 0:
					if(!Dungeon.isChallenged(CS)){
						mapnameSlain.texture( "interfaces/mapname/snow.png" );
						mapnameSlain.show( Window.TITLE_COLOR, 0.6f, 3f );
						scene.showLogo( mapnameSlain );
					}
					break;
				case 1:
					mapnameSlain.texture( "interfaces/mapname/forest.png" );
					mapnameSlain.show( Window.GREEN_COLOR, 0.6f, 3f );
					scene.showLogo( mapnameSlain );
					break;
				case 6:
					mapnameSlain.texture( "interfaces/mapname/prison.png" );
					mapnameSlain.show( Window.BLUE_COLOR, 0.6f, 3f );
					scene.showLogo( mapnameSlain );
					break;
				case 11:
					mapnameSlain.texture( "interfaces/mapname/caves.png" );
					mapnameSlain.show( Window.MLPD_COLOR, 0.6f, 3f );
					scene.showLogo( mapnameSlain );
					break;
				case 16:
					mapnameSlain.texture( "interfaces/mapname/dwarf.png" );
					mapnameSlain.show( Window.CBLACK, 0.6f, 3f );
					scene.showLogo( mapnameSlain );
					break;
				case 17:case 18:
					if (Dungeon.branch == 1) {
						mapnameSlain.texture("interfaces/mapname/ancient.png");
						mapnameSlain.show(0x067000, 0.6f, 3f);
						scene.showLogo(mapnameSlain);
					}
					break;
				case 21:
					mapnameSlain.texture( "interfaces/mapname/halls.png" );
					mapnameSlain.show( Window.GDX_COLOR, 0.6f, 3f );
					scene.showLogo( mapnameSlain );
					break;
			}
		}


	}

	public static void showChapter( int id ) {

		if (Dungeon.chapters.contains( id )) {
			return;
		}

		String text = Messages.get(WndStory.class, CHAPTERS.get( id ));
		if (text != null) {
			WndStory wnd = new WndStory(text);
			if ((wnd.delay = 0.6f) > 0) {
				wnd.shadow.visible = wnd.chrome.visible = wnd.tf.visible = false;
				if (wnd.ttl != null) wnd.ttl.visible = false;
			}

			Game.scene().add(wnd);
			lanterfireRoll();
			Dungeon.chapters.add(id);
		}
	}

	static final float DURATION_MULTIPLIER = 123456f;
	public static void lanterfireRoll() {
		if (!lanterfireactive) return;

		if (Dungeon.depth == 6) {
			applyRandomBuff();
			GLog.b(Messages.get(WndStory.class, "start"));
		}
		if (!RollLevel()) return;

		if (hero.lanterfire >= 90) {
			goodLanterFire();
		} else if (hero.lanterfire >= 80) {
			applyEffectBasedOnChance(0.85f, 0.05f);
		} else if (hero.lanterfire >= 60) {
			applyEffectBasedOnChance(0.70f, 0.25f);
		} else if (hero.lanterfire >= 35) {
			applyEffectBasedOnChance(0.20f, 0.40f);
		} else if (hero.lanterfire >= 1) {
			applyEffectBasedOnChance(0f, 0.40f);
		} else {
			badLanterFire();
		}
	}

	private static void applyRandomBuff() {
		switch (Random.Int(5)) {
			case 0:
			default:
				Buff.affect(hero, BlessNoMoney.class).set(100, 1);
				break;
			case 1:
				Buff.affect(hero, BlessGoodSTR.class).set(100, 1);
				break;
			case 2:
				Buff.affect(hero, BlessMobDied.class).set(100, 1);
				break;
			case 3:
				Buff.affect(hero, BlessMixShiled.class).set(100, 1);
				break;
			case 4:
				Buff.affect(hero, BlessImmune.class, ChampionHero.DURATION * DURATION_MULTIPLIER);
				break;
		}
	}

	private static void applyEffectBasedOnChance(float goodChance, float badChance) {
		float roll = Random.Float();
		if (roll <= badChance) {
			badLanterFire();
		} else if (roll <= badChance + goodChance) {
			goodLanterFire();
		} else {
			GLog.b(Messages.get(WndStory.class, "normoal"));
		}
	}


}
