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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import static com.shatteredpixel.shatteredpixeldungeon.SPDSettings.ClassPage;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.lanterfireactive;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.effects.CircleArc;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndHero;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndKeyBindings;
import com.watabou.gltextures.TextureCache;
import com.watabou.input.GameAction;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.ColorMath;
import com.watabou.utils.GameMath;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StatusPane extends Component {

	private NinePatch bg;
	private Image avatar;
	private Button heroInfo;
	public static float talentBlink;
	private float warning;

	public static final float FLASH_RATE = (float)(Math.PI*1.5f); //1.5 blinks per second

	private int lastTier = 0;

	private Image rawShielding;
	private Image shieldedHP;
	private Image hp;
	private BitmapText hpText;
	private Button heroInfoOnBar;
	private Image hg;

	private Image icehp;

	private BitmapText icehpText;

	private Image lanterfirevae;
	private BitmapText hgText;

	private Image exp;
	private BitmapText expText;

	private int lastLvl = -1;

	private BitmapText level;

	private BuffIndicator buffs;
	private Compass compass;

	private BusyIndicator busy;
	private CircleArc counter;

	//Custom UI Left
	public PageIndicator page;
	public PageIndicatorB pageb;

	public BossSelectIndicator bossselect;
	public JoinIndicator joinxxx;
	public LanterFireCator lanter;

	private static String asset = Assets.Interfaces.STATUS_DARK;

	private boolean large;

	private RenderedTextBlock timeText;
	private RenderedTextBlock timeStatusText;

	public StatusPane( boolean large ){
		super();

		this.large = large;

		if (SPDSettings.ClassUI()) {
			asset = Assets.Interfaces.STATUS;
		} else {
			asset =  Assets.Interfaces.STATUS_DARK;
		}

		if (large)  bg = new NinePatch( asset, 0, 64, 41, 39, 33, 0, 4, 0 );
		else        bg = new NinePatch( asset, 0, 0, 128, 36, 85, 0, 45, 0 );
		add( bg );

		heroInfo = new Button(){
			@Override
			protected void onClick () {
				Camera.main.panTo( Dungeon.hero.sprite.center(), 5f );
				GameScene.show( new WndHero() );
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
		add(heroInfo);

		avatar = HeroSprite.avatar( Dungeon.hero.heroClass, lastTier );
		add( avatar );

		talentBlink = 0;

		compass = new Compass( Statistics.amuletObtained ? Dungeon.level.entrance() : Dungeon.level.exit() );
		add( compass );

		if (large)  rawShielding = new Image(asset, 0, 112, 128, 9);
		else        rawShielding = new Image(asset, 0, 40, 50, 4);
		rawShielding.alpha(0.5f);
		add(rawShielding);

		if (large)  shieldedHP = new Image(asset, 0, 112, 128, 9);
		else        shieldedHP = new Image(asset, 0, 40, 50, 4);
		add(shieldedHP);

		if (large)  hp = new Image(asset, 0, 103, 128, 9);
		else        hp = new Image(asset, 0, 36, 50, 4);
		add( hp );

		if (large)  hg = new Image(asset, 0, 128, 128, 7);
		else        hg = new Image(asset, 0, 45, 49, 4);
		add( hg );

		if (large)
			icehp =  new Image(asset, 0, 135, 128, 6);
		else     icehp = new Image(asset, 0, 49, 52, 4);

		add( icehp );

	 	lanterfirevae = new Image(Assets.Interfaces.LANTERLING);
		add(lanterfirevae);

		hpText = new BitmapText(PixelScene.pixelFont);
		hpText.alpha(0.6f);
		add(hpText);

		hgText = new BitmapText(PixelScene.pixelFont);
		hgText.alpha(0.6f);
		add(hgText);

		icehpText = new BitmapText(PixelScene.pixelFont);
		icehpText.alpha(0.6f);
		add(icehpText);

		//TIME TEXT
		timeText = PixelScene.renderTextBlock(5);
		timeText.alpha(0.6f);
		add(timeText);

		timeStatusText = PixelScene.renderTextBlock( 5 );
		timeStatusText.alpha(0.6f);
		add(timeStatusText);

		heroInfoOnBar = new Button(){
			@Override
			protected void onClick () {
				Camera.main.panTo( Dungeon.hero.sprite.center(), 5f );
				GameScene.show( new WndHero() );
			}
		};
		add(heroInfoOnBar);

		if (large)  exp = new Image(asset, 0, 121, 128, 7);
		else        exp = new Image(asset, 0, 44, 16, 1);
		add( exp );

		if (large){
			expText = new BitmapText(PixelScene.pixelFont);
			expText.hardlight( 0xFFFFAA );
			expText.alpha(0.6f);
			add(expText);
		}

		level = new BitmapText( PixelScene.pixelFont);
		level.hardlight( 0xFFFFAA );
		add( level );

		buffs = new BuffIndicator( Dungeon.hero,large);
		add( buffs );

		busy = new BusyIndicator();
		add( busy );

		counter = new CircleArc(18, 4.25f);
		counter.color( 0x808080, true );
		counter.show(this, busy.center(), 0f);

		page=new PageIndicator();
		add(page);

		pageb=new PageIndicatorB();
		add(pageb);

		bossselect=new BossSelectIndicator();
		add(bossselect);

		joinxxx=new JoinIndicator();
		add(joinxxx);

		lanter=new LanterFireCator();
		add(lanter);
	}



	@Override
	protected void layout() {

		height = large ? 39 : 32;

		bg.x = x;
		bg.y = y;
		if (large)  bg.size( 160, bg.height ); //HP bars must be 128px wide atm
		else        bg.size( width, bg.height );

		avatar.x = bg.x - avatar.width / 2f + 15;
		avatar.y = bg.y - avatar.height / 2f + (large ? 15 : 16);
		PixelScene.align(avatar);

		heroInfo.setRect( x, y+(large ? 0 : 1), 30, large ? 40 : 30 );

		compass.x = avatar.x + avatar.width / 2f - compass.origin.x;
		compass.y = avatar.y + avatar.height / 2f - compass.origin.y;
		PixelScene.align(compass);

		if (large) {
			exp.x = x + 30;
			exp.y = y + 30;

			hp.x = shieldedHP.x = rawShielding.x = x + 30;
			hp.y = shieldedHP.y = rawShielding.y = y + 19;

			hpText.x = hp.x + (125 - hpText.width())/2f;
			hpText.y = hp.y + 1;
			PixelScene.align(hpText);

			hg.x= x + 30;
			hg.y= y + 10f;

			hgText.x = x+80;
			hgText.y = hg.y;
			PixelScene.align(hgText);

			icehp.x = x+ 30;
			icehp.y = y + 2f;

			icehpText.x = x+80;
			icehpText.y = icehp.y-0.6f;
			PixelScene.align(icehpText);

			expText.x = exp.x + (128 - expText.width())/2f;
			expText.y = exp.y;
			PixelScene.align(expText);

			heroInfoOnBar.setRect(heroInfo.right(), y + 19, 130, 20);

			if(SPDSettings.TimeLimit()) {
				PixelScene.align(timeText);
				timeStatusText.x = MenuPane.version.x;
				timeStatusText.y = MenuPane.version.y+10;
				PixelScene.align(timeStatusText);
			} else {
				timeText.visible=false;
				timeText.active=false;
				timeStatusText.active=false;
				timeStatusText.visible=false;
			}
			lanter.setPos(0, 1000);
			busy.x = x + bg.width + 1;
			busy.y = y + bg.height - 9;
		} else {
			exp.x = x;
			exp.y = y;
			hp.x = shieldedHP.x = rawShielding.x = x + 30;
			hp.y = shieldedHP.y = rawShielding.y = y + 3;

			hpText.scale.set(PixelScene.align(0.5f));
			hpText.x = hp.x + 1;
			hpText.y = hp.y + (hp.height - (hpText.baseLine()+hpText.scale.y))/2f;
			hpText.y -= 0.001f; //prefer to be slightly higher
			PixelScene.align(hpText);

			hg.x = 30.0f;
			hg.y = 8.0f;

			hgText.scale.set(PixelScene.align(0.5f));
			hgText.x = hg.x + 1;
			hgText.y = hg.y + (hp.height - (hgText.baseLine()+hgText.scale.y))/2f;
			hgText.y -= 0.001f; //prefer to be slightly higher
			PixelScene.align(hgText);

			icehp.x = 30.0f;
			icehp.y = 13.0f;

			icehpText.scale.set(PixelScene.align(0.5f));
			icehpText.x = icehp.x + 1;
			icehpText.y = icehp.y + (icehp.height - (icehpText.baseLine()+icehpText.scale.y))/2f;
			icehpText.y -= 0.001f; //prefer to be slightly higher
			PixelScene.align(icehpText);

			heroInfoOnBar.setRect(heroInfo.right(), y, 50, 9);

			lanterfirevae.x= 1.0f;
			lanterfirevae.y= 142.0f;
			lanterfirevae.visible = false; //

			buffs.setPos( x + 34, y + 20 );

			busy.x = x + 1;
			busy.y = y + 33;

			lanter.setPos(0, 1000);
		}

		if(SPDSettings.TimeLimit()) {
			timeText.x = MenuPane.depthButton.x;

			timeText.y = MenuPane.version.y + 5;

			PixelScene.align(timeText);
			timeStatusText.x = MenuPane.version.x;
			timeStatusText.y = MenuPane.version.y+10;
			PixelScene.align(timeStatusText);
		} else {
			timeText.visible=false;
			timeText.active=false;
			timeStatusText.active=false;
			timeStatusText.visible=false;
		}

		counter.point(busy.center());
	}
	
	private static final int[] warningColors = new int[]{0x660000, 0xCC0000, 0x660000};

	@Override
	public void update() {
		super.update();

		if (SPDSettings.ClassUI()) {
			asset = Assets.Interfaces.STATUS;
		} else {
			asset =  Assets.Interfaces.STATUS_DARK;
		}

		int maxHunger = (int) Hunger.STARVING;
		float maxPureSole = Dungeon.hero.lanterfire;
		int mtPureSole = 100;

		//冰血聪明 x
		int maxIceHp = Dungeon.hero.icehp;
		int mtIceHp = 100;

		int health = Dungeon.hero.HP;
		int shield = Dungeon.hero.shielding();
		int max = Dungeon.hero.HT;

		if (SPDSettings.ClassUI()) {
			bg.texture = TextureCache.get(Assets.Interfaces.STATUS);
		} else {
			bg.texture = TextureCache.get(Assets.Interfaces.STATUS_DARK);
		}

		if(SPDSettings.TimeLimit()) {
			if (Dungeon.hero.buff(LockedFloor.class) != null) {
				timeText.y = MenuPane.version.y + 14;
			} else {
				timeText.y = MenuPane.version.y + 5;
			}
		}

		if (ClassPage()) {
			page.setPos(0, 40);
			pageb.setPos(0, 1000);

			joinxxx.setPos(0, 52);

			if(Dungeon.isDLC(Conducts.Conduct.BOSSRUSH)){
				bossselect.setPos(0, 1000);
			} else {
				bossselect.setPos(0, 78);
			}


			if(lanterfireactive){
				lanter.setPos(0, 1000);
				lanterfirevae.visible = false;
			}

		} else {
			page.setPos(0, 1000);
			pageb.setPos(0, 40);

			joinxxx.setPos(0, 1000);
			bossselect.setPos(0, 1000);

			if(lanterfireactive){
				lanter.setPos(0, 75);
				lanter.visible = true;
				lanter.active  = true;
				lanterfirevae.visible =true;
				lanterfirevae.x= 1.0f;
				lanterfirevae.y= 142.0f;
			}

		}

		if (Dungeon.hero != null && Dungeon.hero.isAlive()) {
			Date date = new Date();
			String strDateFormat = "yyyy-MM-dd HH:mm";
			SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat, Locale.getDefault());

			Calendar cal=Calendar.getInstance();
			int s=cal.get(Calendar.SECOND);
			if(s<20) {
				timeText.hardlight(Window.CWHITE);
			} else if(s<40) {
				timeText.hardlight(Window.CYELLOW);
			} else {
				timeText.hardlight(Window.SKYBULE_COLOR);
			}
			timeText.text(sdf.format(date));
		} else {
			timeText.text("GAME OVER");
		}


		if (!Dungeon.hero.isAlive()) {
			avatar.tint(0x000000, 0.5f);
		} else if ((health/(float)max) < 0.3f) {
			warning += Game.elapsed * 5f *(0.4f - (health/(float)max));
			warning %= 1f;
			avatar.tint(ColorMath.interpolate(warning, warningColors), 0.5f );
		} else if (talentBlink > 0.33f){ //stops early so it doesn't end in the middle of a blink
			talentBlink -= Game.elapsed;
			avatar.tint(1, 1, 0, (float)Math.abs(Math.cos(talentBlink*FLASH_RATE))/2f);
		} else {
			avatar.resetColor();
		}

		hp.scale.x = Math.max( 0, (health-shield)/(float)max);
		shieldedHP.scale.x = health/(float)max;

		if(lanterfireactive) {
			lanterfirevae.scale.y = -Math.max( 0, (maxPureSole)/(float)mtPureSole);
		} else {
			lanterfirevae.scale.y = -1.0f;
		}

		icehp.scale.x = Math.max( 0, (maxIceHp)/(float)mtIceHp);

		if (shield > health) {
			rawShielding.scale.x = shield / (float) max;
		} else {
			rawShielding.scale.x = 0;
		}

		if (shield <= 0){
			hpText.text(health + "/" + max);
		} else {
			hpText.text(health + "+" + shield +  "/" + max);
		}

		icehpText.text(maxIceHp + "/" + mtIceHp);

		Hunger hungerBuff = Dungeon.hero.buff(Hunger.class);
		if (hungerBuff != null) {
			int hunger = Math.max(0, maxHunger - hungerBuff.hunger());
			hg.scale.x = (float) hunger / (float) maxHunger;
			hgText.text(hunger + "/" + maxHunger);
		}
		else if (Dungeon.hero.isAlive()) {
			hg.scale.x = 1.0f;
		}

		if (large) {
			exp.scale.x = (128 / exp.width) * Dungeon.hero.exp / Dungeon.hero.maxExp();

			hpText.measure();
			hpText.x = hp.x + (128 - hpText.width())/2f;

			expText.text(Dungeon.hero.exp + "/" + Dungeon.hero.maxExp());
			expText.measure();
			expText.x = hp.x + (128 - expText.width())/2f;

		} else {
			exp.scale.x = (width / exp.width) * Dungeon.hero.exp / Dungeon.hero.maxExp();
		}

		if (Dungeon.hero.lvl != lastLvl) {

			if (lastLvl != -1) {
				showStarParticles();
			}

			lastLvl = Dungeon.hero.lvl;

			if (large){
				level.text( "lv. " + lastLvl );
				level.measure();
				level.x = x + (30f - level.width()) / 2f;
				level.y = y + 33f - level.baseLine() / 2f;
			} else {
				level.text( Integer.toString( lastLvl ) );
				level.measure();
				level.x = x + 27.5f - level.width() / 2f;
				level.y = y + 28.0f - level.baseLine() / 2f;
			}
			PixelScene.align(level);
		}

		int tier = Dungeon.hero.tier();
		if (tier != lastTier) {
			lastTier = tier;
			avatar.copy( HeroSprite.avatar( Dungeon.hero.heroClass, tier ) );
		}

		counter.setSweep((1f - Actor.now()%1f)%1f);
	}

	public void showStarParticles(){
		Emitter emitter = (Emitter)recycle( Emitter.class );
		emitter.revive();
		emitter.pos( avatar.center() );
		emitter.burst( Speck.factory( Speck.STAR ), 12 );
	}

	public void alpha( float value ){
		value = GameMath.gate(0, value, 1f);
		bg.alpha(value);
		avatar.alpha(value);
		rawShielding.alpha(0.5f*value);
		shieldedHP.alpha(value);
		hp.alpha(value);
		hpText.alpha(0.6f*value);
		exp.alpha(value);
		if (expText != null) expText.alpha(0.6f*value);
		level.alpha(value);
		compass.alpha(value);
		busy.alpha(value);
		counter.alpha(value);
	}
}
