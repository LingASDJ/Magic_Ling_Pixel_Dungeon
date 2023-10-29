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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.lanterfireactive;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.LostBackpack;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.GameLog;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndError;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndStory;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.NoosaScriptNoLighting;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Random;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class InterlevelScene extends PixelScene {
	public static int lastRegion = -1;
	//slow fade on entering a new region
	private static final float SLOW_FADE = 1f; //.33 in, 1.33 steady, .33 out, 2 seconds total
	//norm fade when loading, falling, returning, or descending to a new floor
	private static final float NORM_FADE = 0.67f; //.33 in, .67 steady, .33 out, 1.33 seconds total
	//fast fade when ascending, or descending to a floor you've been on
	private static final float FAST_FADE = 0.50f; //.33 in, .33 steady, .33 out, 1 second total

	private static float fadeTime;

	public static int returnBranch;

	public enum Mode {
		DESCEND, ASCEND, CONTINUE, RESURRECT, RETURN, FALL, RESET, NONE,EXBOSS,GOBACK,FRGIRLBOSS,ANCITYBOSS,DR,GARDEN
	}
	public static Mode mode;

	public static int returnDepth;
	public static int returnPos;

	public static boolean noStory = false;

	public static boolean fallIntoPit;

	private static final int NUM_TIPS = 37;

	private static ArrayList<Integer> tipset;
	private RenderedTextBlock tip;

	private void newTipSet()
	{
		tipset = new ArrayList<>();
		for(int i = 1; i <= NUM_TIPS; i++)
			tipset.add(i);
	}

	private enum Phase {
		FADE_IN, STATIC, FADE_OUT
	}
	private Phase phase;
	private float timeLeft;

	private RenderedTextBlock message;

	private static Thread thread;
	private static Exception error = null;
	private float waitingTime;

	@Override
	public void create() {
		super.create();

		if(tipset == null || tipset.isEmpty())
			newTipSet();

		int tip_i = tipset.remove(Random.Int(tipset.size()));

		tip = PixelScene.renderTextBlock(Messages.get(this, "dialog_" + tip_i), 9);
		tip.maxWidth((int)Math.round(Camera.main.width * 0.8));
		tip.setPos((Camera.main.width - tip.width()) / 2, (Camera.main.height - tip.height()) / 2);
		align(tip);
		add(tip);

		String loadingAsset;
		int loadingDepth;
		final float scrollSpeed;
		fadeTime = NORM_FADE;
		switch (mode){
			default:
				loadingDepth = Dungeon.depth;
				scrollSpeed = 0;
				break;
			case CONTINUE:
				loadingDepth = GamesInProgress.check(GamesInProgress.curSlot).depth;
				scrollSpeed = 5;
				break;
			case DESCEND:
			case FRGIRLBOSS:
				if (Dungeon.hero == null){
					loadingDepth = 1;
					fadeTime = SLOW_FADE;
				} else {
					loadingDepth = Dungeon.depth+1;
					if (!(Statistics.deepestFloor < loadingDepth)) {
						fadeTime = FAST_FADE;
					} else if (loadingDepth == 6 || loadingDepth == 11
							|| loadingDepth == 16 || loadingDepth == 21) {
						fadeTime = SLOW_FADE;
					}
				}
				scrollSpeed = 5;
				break;
			case FALL:
				loadingDepth = Dungeon.depth+1;
				scrollSpeed = 50;
				break;
			case ASCEND:
				fadeTime = FAST_FADE;
				loadingDepth = Dungeon.depth-1;
				scrollSpeed = -5;
				break;
			case RETURN:
				loadingDepth = returnDepth;
				scrollSpeed = returnDepth > Dungeon.depth ? 15 : -15;
				break;
			case EXBOSS:
				scrollSpeed = -25;
				loadingDepth = returnDepth;
				break;
		}
		if (Dungeon.depth == 0)         	loadingAsset = Assets.Interfaces.LOADING_GOLD;
		else if (loadingDepth <= 5)     loadingAsset = Assets.Interfaces.LOADING_SEWERS;
		else if (loadingDepth <= 10)    loadingAsset = Assets.Interfaces.LOADING_PRISON;
		else if (loadingDepth <= 15)    loadingAsset = Assets.Interfaces.LOADING_COLD;
		else if (loadingDepth <= 20)    loadingAsset = Assets.Interfaces.LOADING_CITY;
		else if (loadingDepth <= 25)    loadingAsset = Assets.Interfaces.LOADING_HALLS;
		else                            loadingAsset = Assets.Interfaces.SHADOW;

		//场景过渡速度
		//本地调试+桌面
		if (DeviceCompat.isDebug() && DeviceCompat.isDesktop()){
			fadeTime = 0.1f;
		} else {
			//打包后的环境
			fadeTime = 0.75f;
		}

		SkinnedBlock bg = new SkinnedBlock(Camera.main.width, Camera.main.height, loadingAsset ){
			@Override
			protected NoosaScript script() {
				return NoosaScriptNoLighting.get();
			}

			@Override
			public void draw() {
				Blending.disable();
				super.draw();
				Blending.enable();
			}

			@Override
			public void update() {
				super.update();
				offset(0, Game.elapsed * scrollSpeed);
			}
		};
		bg.scale(4, 4);
		bg.autoAdjust = true;
		add(bg);

		Image im = new Image(TextureCache.createGradient(0xAA000000, 0xBB000000, 0xCC000000, 0xDD000000, 0xFF000000)){
			@Override
			public void update() {
				super.update();
				if (phase == Phase.FADE_IN)         aa = Math.max( 0, (timeLeft - (fadeTime - 0.333f)));
				else if (phase == Phase.FADE_OUT)   aa = Math.max( 0, (0.333f - timeLeft));
				else                                aa = 0;
			}
		};
		im.angle = 90;
		im.x = Camera.main.width;
		im.scale.x = Camera.main.height/5f;
		im.scale.y = Camera.main.width;
		add(im);
		String text = Messages.get(Mode.class, mode.name());
		message = PixelScene.renderTextBlock(text, 9);
		message.x = (Camera.main.width - message.width()) / 2;
		message.y = (Camera.main.height - message.height()) / 4;
		align(message);
		add(message);

		if(tipset == null || tipset.isEmpty())
			newTipSet();

		tip = PixelScene.renderTextBlock(Messages.get(this, "dialog_" + tip_i), 7);
		tip.maxWidth((int)Math.round(Camera.main.width * 0.8));
		tip.setPos((Camera.main.width - tip.width()) / 2, (Camera.main.height - tip.height()) / 2);
		align(tip);
		add(tip);

		phase = Phase.FADE_IN;
		timeLeft = fadeTime;

		if (thread == null) {
			thread = new Thread() {
				@Override
				public void run() {

					try {

						if (Dungeon.hero != null){
							Dungeon.hero.spendToWhole();
						}
						Actor.fixTime();

						switch (mode) {
							case DESCEND:
								descend();
								break;
							case ASCEND:
								ascend();
								break;
							case CONTINUE:
								restore();
								break;
							case RESURRECT:
								resurrect();
								break;
							case RETURN:
								returnTo();
								break;
							case GOBACK:
								returnPO();
								break;
							case FALL:
								fall();
								break;
							case RESET:
								reset();
								break;
							case EXBOSS:
								exboss(1);
								break;
							case FRGIRLBOSS:
								exboss(2);
								break;
							case ANCITYBOSS:
								exboss(4);
								break;
							case GARDEN:
								exboss(5);
								break;

						}

					} catch (Exception e) {

						error = e;

					}

					if (phase == Phase.STATIC && error == null) {
						phase = Phase.FADE_OUT;
						timeLeft = fadeTime;
					}
				}
			};
			thread.start();
		}
		waitingTime = 0f;
	}

	@Override
	public void update() {
		super.update();

		waitingTime += Game.elapsed;

		float p = timeLeft / fadeTime;

		switch (phase) {

			case FADE_IN:
				message.alpha( 1 - p );
				if ((timeLeft -= Game.elapsed) <= 0) {
					if (!thread.isAlive() && error == null) {
						phase = Phase.FADE_OUT;
						timeLeft = fadeTime;
					} else {
						phase = Phase.STATIC;
					}
				}
				break;

			case FADE_OUT:
				message.alpha( p );

				if ((timeLeft -= Game.elapsed) <= 0) {
					Game.switchScene( GameScene.class );
					thread = null;
					error = null;
				}
				break;

			case STATIC:
				if (error != null) {
					String errorMsg;
					if (error instanceof FileNotFoundException)     errorMsg = Messages.get(this, "file_not_found");
					else if (error instanceof IOException)          errorMsg = Messages.get(this, "io_error");
					else if (error.getMessage() != null &&
							error.getMessage().equals("old save")) errorMsg = Messages.get(this, "io_error");

					else throw new RuntimeException("fatal error occured while moving between floors. " +
								"Seed:" + Dungeon.seed + " depth:" + Dungeon.depth, error);

					add( new WndError( errorMsg ) {
						public void onBackPressed() {
							super.onBackPressed();
							Game.switchScene( StartScene.class );
						}
					} );
					thread = null;
					error = null;
				} else if (thread != null && (int)waitingTime == 10){
					waitingTime = 11f;
					String s = "";
					for (StackTraceElement t : thread.getStackTrace()){
						s += "\n";
						s += t.toString();
					}
					throw new RuntimeException(Messages.get(TitleScene.class,"spawnerror") +
									"Seed:" + Dungeon.seed + " depth:" + Dungeon.depth + " trace:" +
									s);
				}
				break;
		}
	}

	private void descend() throws IOException {

		if (Dungeon.hero == null) {
			Mob.clearHeldAllies();
			Dungeon.init();
			if (noStory) {
				Dungeon.chapters.add( WndStory.ID_SEWERS );
				noStory = false;
			}
			GameLog.wipe();
		} else {
			Mob.holdAllies( Dungeon.level );
			Dungeon.saveAll();
		}

		Level level;
		if (Dungeon.depth >= Statistics.deepestFloor) {
			level = Dungeon.newLevel();
		} else {
			Dungeon.depth++;
			level = Dungeon.loadLevel( GamesInProgress.curSlot );
		}
		Dungeon.switchLevel( level, level.entrance );
	}

	private void fall() throws IOException {

		Mob.holdAllies( Dungeon.level );

		Buff.affect( Dungeon.hero, Chasm.Falling.class );
		Dungeon.saveAll();

		Level level;
		if (Dungeon.depth >= Statistics.deepestFloor) {
			level = Dungeon.newLevel();
		} else {
			Dungeon.depth++;
			level = Dungeon.loadLevel( GamesInProgress.curSlot );
		}
		Dungeon.switchLevel( level, level.fallCell( fallIntoPit ));
	}

	private void ascend() throws IOException {

		Mob.holdAllies( Dungeon.level );

		Dungeon.saveAll();
		Dungeon.depth--;
		Level level = Dungeon.loadLevel( GamesInProgress.curSlot );
		Dungeon.switchLevel( level, level.exit );
	}

	private void returnTo() throws IOException {

		Mob.holdAllies( Dungeon.level );
		Dungeon.branch = returnBranch;
		Dungeon.saveAll();
		Dungeon.depth = returnDepth;
		Level level = Dungeon.loadLevel( GamesInProgress.curSlot );
		Dungeon.switchLevel( level, returnPos );
	}

	private void returnPO() throws IOException {
		Mob.holdAllies( Dungeon.level );
		hero.STR = 10;

		hero.CakeUsed = 0;

		hero.lvl = 1;
		InterlevelScene.returnDepth = Math.max(1, (Dungeon.depth - 1 - (Dungeon.depth-2)%5));
		InterlevelScene.returnPos = -1;
		Level level = Dungeon.loadLevel( GamesInProgress.curSlot );
		Dungeon.switchLevel( level, returnPos );
	}

	private void returnTx() throws IOException {

		Mob.holdAllies( Dungeon.level );

		Dungeon.saveAll();
		Dungeon.depth = 0;
		Level level = Dungeon.loadLevel( GamesInProgress.curSlot );
		Dungeon.switchLevel( level, returnPos );
	}

	private void restore() throws IOException {

		Mob.clearHeldAllies();
		GameLog.wipe();

		Dungeon.loadGame( GamesInProgress.curSlot );
		if (Dungeon.depth == -1) {
			Dungeon.depth = Statistics.deepestFloor;
			Dungeon.switchLevel( Dungeon.loadLevel( GamesInProgress.curSlot ), -1 );
		} else {
			Level level = Dungeon.loadLevel( GamesInProgress.curSlot );
			Dungeon.switchLevel( level, Dungeon.hero.pos );
		}



	}

	private void resurrect() {

		Mob.holdAllies( Dungeon.level );

		Level level;
		if (Dungeon.level.locked) {
			ArrayList<Item> preservedItems = Dungeon.level.getItemsToPreserveFromSealedResurrect();

			Dungeon.hero.resurrect();
			Dungeon.depth--;
			level = Dungeon.newLevel();
			Dungeon.hero.pos = Terrain.ENTRANCE;

			if(!Dungeon.bossLevel()) {
				for (Item i : preservedItems) {
					int pos = level.randomRespawnCell(null);
					if (pos == -1) pos = Terrain.ENTRANCE;
					level.drop(i, pos);
				}
			}
			int pos = level.randomRespawnCell(null);
			if (pos == -1) pos = Terrain.ENTRANCE;
			level.drop(new LostBackpack(), pos);

		} else {
			level = Dungeon.level;
			BArray.setFalse(level.heroFOV);
			BArray.setFalse(level.visited);
			BArray.setFalse(level.mapped);
			int invPos = Dungeon.hero.pos;
			int tries = 0;
			do {
				Dungeon.hero.pos = level.entrance;
				tries++;

				//prevents spawning on traps or plants, prefers farther locations first
			} while (level.traps.get(Dungeon.hero.pos) != null
					|| (level.plants.get(Dungeon.hero.pos) != null && tries < 500)
					|| level.trueDistance(invPos, Dungeon.hero.pos) <= 30 - (tries/10));

			//directly trample grass
			if (level.map[Dungeon.hero.pos] == Terrain.HIGH_GRASS || level.map[Dungeon.hero.pos] == Terrain.FURROWED_GRASS){
				level.map[Dungeon.hero.pos] = Terrain.GRASS;
			}
			Dungeon.hero.resurrect();

			if(lanterfireactive && hero.lanterfire <= 30){
				GLog.n("你的遗物被上一世暗影吞噬了，它正在本层游荡，快去寻找它吧！");
			} else {
				level.drop(new LostBackpack(), invPos);
			}
		}

		Dungeon.switchLevel( level, Dungeon.hero.pos );
	}


	private void reset() throws IOException {

		Mob.holdAllies( Dungeon.level );
		SpecialRoom.resetPitRoom(Dungeon.depth+1);

		//Boss全局布尔控制
		Statistics.crivusfruitslevel2 = false;

		Dungeon.depth--;
		Level level = Dungeon.newLevel();
		Dungeon.switchLevel( level, level.entrance );
	}

	@Override
	protected void onBackPressed() {
		//Do nothing
	}

	private void exboss(int branch) throws IOException {

		Actor.fixTime();
		Dungeon.saveLevel(GamesInProgress.curSlot);

		Level level;
		switch(branch){
			case 1:
				level=Dungeon.ColdFlowerCanyon();
				break;
			case 2:
				level=Dungeon.ColdFlowerCanyonDie();
				break;
			case 3:
				level=Dungeon.AncityWaterLevel();
				break;
			case 4:
				level=Dungeon.AncityBossWaterLevel();
				break;
			case 5:
				level=Dungeon.GardenLevel();
				break;
			default:
				level = Dungeon.newLevel();
		}
		Dungeon.switchLevel(level, level.entrance);
	}
}
