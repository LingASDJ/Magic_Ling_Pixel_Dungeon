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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.CS;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.SPDSettings.ClassPage;
import static com.shatteredpixel.shatteredpixeldungeon.SPDSettings.ClassUI;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.gameDay;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.gameNight;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.gameTime;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.lanterfireactive;
import static com.shatteredpixel.shatteredpixeldungeon.ui.MenuPane.version;
import static com.shatteredpixel.shatteredpixeldungeon.update.MLChangesButton.downloadSuccess;
import static com.shatteredpixel.shatteredpixeldungeon.update.MLChangesButton.updateProgress;

import com.nlf.calendar.Solar;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.NightorDay;
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
import com.watabou.noosa.Visual;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.ColorMath;
import com.watabou.utils.GameMath;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StatusPane extends Component {

	public String name() {
		if(gameTime>400){
			return Messages.get(NightorDay.class, "name4");
		} else if(gameTime>350){
			return Messages.get(NightorDay.class, "name");
		} else if(gameTime>200) {
			return Messages.get(NightorDay.class, "name2");
		} else {
			return Messages.get(NightorDay.class, "name3");
		}
	}

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

	private Image lanterFireEnergy;

	private BitmapText lanterText;

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

	public static String asset = Assets.Interfaces.STATUS_DARK;

	private boolean large;

	private RenderedTextBlock timeText;
	private RenderedTextBlock timeStatusText;

	public StatusPane( boolean large ){
		super();

		this.large = large;

		if (ClassUI()) {
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
				Camera.main.panTo( hero.sprite.center(), 5f );
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

		avatar = HeroSprite.avatar( hero.heroClass, lastTier );
		add( avatar );

		talentBlink = 0;

		compass = new Compass( Statistics.amuletObtained && Dungeon.depth <26 ? Dungeon.level.entrance() : Dungeon.level.exit() );
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
			lanterFireEnergy =  new Image(asset, 0, 135, 128, 6);
		else     lanterFireEnergy = new Image(asset, 0, 49, 52, 4);

		add(lanterFireEnergy);

		lanterfirevae = ClassUI() ? new Image(Assets.Interfaces.LANTERLING) : new Image(Assets.Interfaces.LANTERLING_N);
		add(lanterfirevae);

		hpText = new BitmapText(PixelScene.pixelFont);
		hpText.alpha(0.6f);
		add(hpText);

		hgText = new BitmapText(PixelScene.pixelFont);
		hgText.alpha(0.6f);
		add(hgText);

		lanterText = new BitmapText(PixelScene.pixelFont);
		lanterText.alpha(0.6f);
		add(lanterText);

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
				Camera.main.panTo( hero.sprite.center(), 5f );
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

		buffs = new BuffIndicator( hero,large);
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

			lanterFireEnergy.x = x+ 30;
			lanterFireEnergy.y = y + 2f;

			lanterText.x = lanterFireEnergy.x + (100 - lanterText.width())/2f;
			lanterText.y = lanterFireEnergy.y-1;
			PixelScene.align(lanterText);

			expText.x = exp.x + (128 - expText.width())/2f;
			expText.y = exp.y;
			PixelScene.align(expText);

			heroInfoOnBar.setRect(heroInfo.right(), y + 19, 130, 20);

			if(SPDSettings.TimeLimit()) {
				PixelScene.align(timeText);
				timeStatusText.x = version.x;
				timeStatusText.y = version.y+10;
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

			lanterFireEnergy.x = 30.0f;
			lanterFireEnergy.y = 13.0f;

			heroInfoOnBar.setRect(heroInfo.right(), y, 50, 9);

			lanterfirevae.x= 1.0f;
			lanterfirevae.y= 142.0f;
			lanterfirevae.visible = false; //

			buffs.setPos( x + 34, y + 13 );

			busy.x = x + 1;
			busy.y = y + 33;

			lanter.setPos(0, 1000);

			lanterFireEnergy.active = false;
			lanterFireEnergy.visible = false;
		}

		if(SPDSettings.TimeLimit()) {
			timeText.x = MenuPane.depthButton.x - 27 - timeText.width();;

			timeText.y = version.y + 5;

			PixelScene.align(timeText);
			timeStatusText.x = timeText.right();
			timeStatusText.y = version.y + 5;
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
	private float time;
	@Override
	public void update() {
		super.update();

		if (ClassUI()) {
			if(Dungeon.depth>25) {
				asset = Assets.Interfaces.STATUS_HOLLOW;
			} else {
				asset = Assets.Interfaces.STATUS;
			}
		} else {
			asset =  Assets.Interfaces.STATUS_DARK;
		}



		Visual visual = new Visual(0,0,0,0);
		visual.am = 1f + 0.01f*Math.max(0f, (float)Math.sin( time += Game.elapsed ));
		time += Game.elapsed / 3.5f;

		if(downloadSuccess) {
			version.text("Download,Completed");
			version.alpha(1f);
			version.x = x + width - version.width();
		} else if (!updateProgress.isEmpty()) {
			version.text("Download:" + updateProgress);
			version.alpha(1f);
			version.x = x + width - version.width();
		} else if(Statistics.bossRushMode) {
			switch (Statistics.difficultyDLCEXLevel){
				case 1:
					version.text("v" + Game.version + "-"+"BossRush-EASY");
					version.x = x + width - version.width();
				break;
				case 2:
					version.text("v" + Game.version + "-"+"BossRush-NORMAL");
					version.x = x + width - version.width();
				break;
				case 3:
					version.text("v" + Game.version + "-"+"BossRush-HARD");
					version.x = x + width - version.width();
				break;
				case 4:
					version.text("v" + Game.version + "-"+"BossRush-HELL");
					version.x = x + width - version.width();
				break;
			}
		} else if(Challenges.activeChallenges()>13 && !Dungeon.isDLC(Conducts.Conduct.DEV)){
			visual.am = 1f + 0.01f * Math.max(0f, (float) Math.sin(time += Game.elapsed));
			time += Game.elapsed / 3.5f;
			// 宝石蓝渐变到黑金色
			float r = 0.63f+0.57f*Math.max(0f, (float)Math.sin( time));
			float g = 0.63f+0.57f*Math.max(0f, (float)Math.sin( time + 2*Math.PI/3 ));
			float b = 0.53f+0.57f*Math.max(0f, (float)Math.sin( time + 4*Math.PI/3 ));
			version.text("v" + Game.version + "-"+"High-Challenges");
			version.hardlight(r, g, b);
			version.x = x + width - version.width();
		}

		//时间紊乱
		if(Statistics.NoTime){
			if(Dungeon.isChallenged(CS) && gameNight){
				if(gameTime>0){
					gameNight = false;
				} else {
					gameTime++;
				}
			} else if(gameTime>400 && gameTime<600) {
				gameTime++;
				gameNight = true;
			} else if(gameTime>599){
				gameTime = 0;
				gameNight = false;
				gameDay++;
			} else {
				gameTime++;
			}
		}

		int maxHunger = (int) Hunger.STARVING;
		float maxPureSole = hero.lanterfire;
		int mtPureSole = 100;

		//冰血聪明 x
		int maxLFSHp = hero.lanterfire;
		int mjsLFSHp = 100;

		int health = hero.HP;
		int shield = hero.shielding();
		int max = hero.HT;

		if (ClassUI()) {
			if(Dungeon.depth>25){
				bg.texture = TextureCache.get(Assets.Interfaces.STATUS_HOLLOW);
			} else {
				bg.texture = TextureCache.get(Assets.Interfaces.STATUS);
			}

		} else {
			bg.texture = TextureCache.get(Assets.Interfaces.STATUS_DARK);
		}

		if(SPDSettings.TimeLimit()) {
			if (hero.buff(LockedFloor.class) != null) {
				timeText.y = version.y + 14;
			} else {
				timeText.y = version.y + 5;
			}
		}

		if(lanterfireactive && !large){
			lanter.setPos(0, 30);
			lanter.visible = true;
			lanter.active  = true;
			lanterfirevae.visible = true;
			lanterfirevae.x= 1.0f;

			if(ClassUI()){
				lanterfirevae.y= 31.0f;
			} else {
				lanterfirevae.y= 30.0f;
			}
			float r =  0.53f+0.57f*Math.max(0f, (float)Math.sin( time - 10/Math.PI/3 ));
			float g =  0.03f+0.57f*Math.max(0f, (float)Math.sin( time + 4/Math.PI/2 ));
			float b =  0.93f+0.57f*Math.max(0f, (float)Math.sin( time));

			float lanter = hero.lanterfire;
			lanterText.text(lanter+"/"+100);
			lanterText.scale.set(PixelScene.align(0.5f));
			lanterText.x = 3;
			lanterText.y = 25;
			lanterText.y -= 0.001f; //prefer to be slightly higher
			PixelScene.align(lanterText);
			lanterText.measure();

			if(hero.lanterfire<50){
				lanterfirevae.hardlight(r,g,b);
				lanterfirevae.scale.x = 1.0f;
				lanterText.hardlight(Window.RED_COLOR);
				lanterText.alpha(1f);
			} else {
				lanterfirevae.resetColor();
				lanterText.resetColor();
			}
		} else if (lanterfireactive){
			lanterText.visible = true;
			if(hero.lanterfire<50){
				lanterText.hardlight(Window.RED_COLOR);
				lanterText.alpha(1f);
			} else {
				lanterText.resetColor();
			}

			lanter.visible = false;
			lanterfirevae.visible = false;
			lanterText.alpha(0.6f);
		} else {
			lanterText.visible = false;
		}

		if (ClassPage()) {
			page.setPos(0, 40);
			pageb.setPos(0, 1000);
			joinxxx.setPos(0, 52);
			bossselect.setPos(0, 78);
		} else {
			page.setPos(0, 1000);
			pageb.setPos(0, 40);

			joinxxx.setPos(0, 1000);
			bossselect.setPos(0, 1000);
		}

		if (hero != null && hero.isAlive()) {
			Date date = new Date();
			String strDateFormat = "yyyy-MM-dd HH:mm";
			SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat, Locale.getDefault());

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			int s = cal.get(Calendar.SECOND);
			if (s < 20) {
				timeText.hardlight(Window.CWHITE);
			} else if (s < 40) {
				timeText.hardlight(Window.CYELLOW);
			} else {
				timeText.hardlight(Window.SKYBULE_COLOR);
			}

			Calendar calendar = Calendar.getInstance();
			Solar solardate = Solar.fromDate(calendar.getTime());
			if(Dungeon.isChallenged(CS)){

				String str = String.valueOf(gameTime);

				String result;

				if (gameTime < 10) {
					result = "";
				} else {
					result = str.substring(0, 1);
				}
				int lastTwoDigits = gameTime % 100;

				timeText.text(sdf.format(date) + " " + Messages.get(this,Integer.toString(solardate.getWeek()))
						+"\n"+Messages.get(this,"time") + (gameTime < 100 ? 0 : Math.abs(Integer.parseInt(result))) +":"+Math.abs(lastTwoDigits)+"-"+name()+"\n"+Messages.get(this,"day",gameDay));
			} else {
				timeText.text(sdf.format(date) + " " + Messages.get(this,Integer.toString(solardate.getWeek())));
			}

		} else {
			timeText.text(Messages.get(this,"game_over"));
		}

		if (!hero.isAlive()) {
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
			lanterfirevae.scale.x = Math.max( 0, (maxPureSole)/(float)mtPureSole);
		} else {
			lanterfirevae.scale.x = 1.0f;
		}

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

		lanterText.text(maxLFSHp + "/" + mjsLFSHp);

		if (hero.isAlive() && lanterfireactive) {
			lanterFireEnergy.scale.x = (float) maxLFSHp / mjsLFSHp;
		} else {
			lanterFireEnergy.scale.x = 1f;
		}

		Hunger hungerBuff = hero.buff(Hunger.class);
		if (hungerBuff != null) {
			int hunger = Math.max(0, maxHunger - hungerBuff.hunger());
			hg.scale.x = (float) hunger / (float) maxHunger;
			hgText.text(hunger + "/" + maxHunger);
		}
		else if (hero.isAlive()) {
			hg.scale.x = 1.0f;
		}

		if (large) {
			exp.scale.x = (128 / exp.width) * hero.exp / hero.maxExp();

			hpText.measure();
			hpText.x = hp.x + (128 - hpText.width())/2f;

			expText.text(hero.exp + "/" + hero.maxExp());
			expText.measure();
			expText.x = hp.x + (128 - expText.width())/2f;

		} else {
			exp.scale.x = (width / exp.width) * hero.exp / hero.maxExp();
		}

		if (hero.lvl != lastLvl) {

			if (lastLvl != -1) {
				showStarParticles();
			}

			lastLvl = hero.lvl;

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

		int tier = hero.tier();
		if (tier != lastTier) {
			lastTier = tier;
			avatar.copy( HeroSprite.avatar( hero.heroClass, tier ) );
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
