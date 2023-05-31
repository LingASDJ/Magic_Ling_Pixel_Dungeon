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

package com.shatteredpixel.shatteredpixeldungeon;

import com.watabou.utils.Bundle;
import com.watabou.utils.SparseArray;

public class Statistics {

	//统计分数
	public static int progressScore;
	public static int treasureScore;
	public static SparseArray<Boolean> floorsExplored = new SparseArray<>();
	public static int exploreScore;
	public static int totalBossScore;
	public static int heldItemValue;
	public static int[] questScores = new int[5];
	public static int totalQuestScore;
	public static float winMultiplier;
	public static float chalMultiplier;
	public static int totalScore;

	public static int[] bossScores = new int[5];
	public static int highestAscent;
	public static boolean gameWon = false;
	public static boolean ascended = false;
	public static int realdeepestFloor;
	public static int boss_enhance = 0;
	public static int goldCollected;
	public static int deepestFloor;
	public static int enemiesSlain;
	public static int foodEaten;
	public static int itemsCrafted;
	public static int piranhasKilled;
	public static int ankhsUsed;
	public static int spawnersIce;

	public static int ChaicBlood;

	public static int naiyaziCollected;
	//used for hero unlock badges
	public static int upgradesUsed;
	public static int sneakAttacks;
	public static int thrownAssists;

	public static int spawnersAlive;
	
	public static float duration;
	
	public static boolean qualifiedForNoKilling = false;
	public static boolean completedWithNoKilling = false;
	
	public static boolean amuletObtained = false;

	public static boolean fireGirlnoshopping = false;

	public static boolean deadshoppingdied = false;

	public static boolean wangzheguilai = false;

	public static boolean endingbald = false;

	//灯火前行
	public static boolean lanterfireactive = false;

	//克里弗斯之果
	public static boolean crivusfruitslevel2 = false;

	//拟态之王
	public static boolean TPDoorDieds = false;

	//修复同步
	public static boolean bugsyncfixed = false;

	//警告
	public static boolean tipsgodungeon = false;

	//宝藏迷宫
	public static int goldchestmazeCollected;

	//珍宝
	public static int dimandchestmazeCollected;

	public static int dageCollected;

	//首次对决
	public static boolean mimicking = false;

	//Directly add float time will cause accuracy lose and stop timing if time is long enough
	//so use long to record seconds, float to count sub-seconds.
	//SPD-V1.3.2-ITEM SPAWN CODE
	public static long real_seconds = 0;
	public static float second_elapsed = 0;
	public static float turnsPassed = 0f;
    public static boolean TryUsedAnmy= false;

    public static void reset() {


		boss_enhance = 0;
		ChaicBlood = 0;
		goldCollected	= 0;
		deepestFloor	= -1;
		enemiesSlain	= 0;
		foodEaten		= 0;
		goldchestmazeCollected = 0;
		dimandchestmazeCollected =0;
		itemsCrafted    = 0;
		piranhasKilled	= 0;
		ankhsUsed		= 0;
		naiyaziCollected = 0;
		upgradesUsed    = 0;
		sneakAttacks    = 0;
		thrownAssists   = 0;
		realdeepestFloor = 0;
		spawnersAlive   = 0;
		
		duration	= 0;
		
		qualifiedForNoKilling = false;

		mimicking = false;
		
		amuletObtained = false;

		tipsgodungeon = false;

		fireGirlnoshopping = false;

		deadshoppingdied = false;
		wangzheguilai = false;
		endingbald = false;

		lanterfireactive = false;
		bugsyncfixed =  false;
		crivusfruitslevel2 = false;
		TPDoorDieds = false;
		TryUsedAnmy = false;

		second_elapsed = 0f;
		real_seconds = 0;
		turnsPassed = 0f;


		//得分
		progressScore   = 0;
		heldItemValue   = 0;
		treasureScore   = 0;
		floorsExplored  = new SparseArray<>();
		exploreScore    = 0;
		bossScores      = new int[5];
		totalBossScore  = 0;
		questScores     = new int[5];
		totalQuestScore = 0;
		winMultiplier   = 1;
		chalMultiplier  = 1;
		totalScore      = 0;
	}
	
	private static final String GOLD		= "score";
	private static final String DEEPEST		= "maxDepth";
	private static final String SLAIN		= "enemiesSlain";
	private static final String FOOD		= "foodEaten";
	private static final String ALCHEMY		= "potionsCooked";
	private static final String PIRANHAS	= "priranhas";
	private static final String ANKHS		= "ankhsUsed";
	
	private static final String UPGRADES	= "upgradesUsed";
	private static final String SNEAKS		= "sneakAttacks";
	private static final String THROWN		= "thrownAssists";

	private static final String SPAWNERS	= "spawnersAlive";
	
	private static final String DURATION	= "duration";

	private static final String NO_KILLING_QUALIFIED	= "qualifiedForNoKilling";
	
	private static final String AMULET		= "amuletObtained";

	//浊焰契约
	private static final String NOSHOPPING		= "fireGirlnoshopping";

	private static final String SHOPPINGDIED		= "deadshoppingdied";

	private static final String WZGL		= "wangzheguilai";

	private static final String ENBR		= "endingbald";

	private static final String EXLEVEL = "Exlevel";

	//灯火前行
	private static final String LANTERACTIVE		= "lanterfireactive";

	//BUG修复的机制
	private static final String BUG_SYNC_FIXED		= "bugsyncfixed";

	//克里弗斯之果
	private static final String CrivusFruitsLevel2		= "crivusfruitslevel2";

	//拟态之王
	private static final String TPDoorDied		= "TPDoorDieds";

	private static final String TIPSGO		= "tipsgo";

	private static final String GOLDCHEST		= "goldchest";

	private static final String NAYAZICOLLECTED		= "naiyaziCollected";

	private static final String AnmyMobs		= "anmymobs";

	private static final String MMC		= "mmcsx";

	private static final String DDK		= "dada";

	private static final String BossSelect	= "bossselect";

	//分数
	private static final String PROG_SCORE	    = "prog_score";
	private static final String HIGHEST		= "maxAscent";
	private static final String ITEM_VAL	    = "item_val";
	private static final String TRES_SCORE      = "tres_score";
	private static final String FLR_EXPL        = "flr_expl";
	private static final String EXPL_SCORE      = "expl_score";
	private static final String BOSS_SCORES		= "boss_scores";
	private static final String TOT_BOSS		= "tot_boss";
	private static final String QUEST_SCORES	= "quest_scores";
	private static final String TOT_QUEST		= "tot_quest";
	private static final String WIN_MULT		= "win_mult";
	private static final String CHAL_MULT		= "chal_mult";
	private static final String TOTAL_SCORE		= "total_score";
	private static final String WON		        = "won";
	private static final String ASCENDED		= "ascended";

	//嗜血荆棘
	private static final String CHACEBLOOD		= "ChaicBlood";


	public static void storeInBundle( Bundle bundle ) {

		//分数
		bundle.put( PROG_SCORE,  progressScore );
		bundle.put( ITEM_VAL,    heldItemValue );
		bundle.put( TRES_SCORE,  treasureScore );
		for (int i = 1; i < 26; i++){
			if (floorsExplored.containsKey(i)){
				bundle.put( FLR_EXPL+i, floorsExplored.get(i) );
			}
		}
		bundle.put( EXPL_SCORE,  exploreScore );
		bundle.put( BOSS_SCORES, bossScores );
		bundle.put( TOT_BOSS,    totalBossScore );
		bundle.put( QUEST_SCORES,questScores );
		bundle.put( TOT_QUEST,   totalQuestScore );
		bundle.put( WIN_MULT,    winMultiplier );
		bundle.put( CHAL_MULT,   chalMultiplier );
		bundle.put( TOTAL_SCORE, totalScore );
		bundle.put( WON,        gameWon );
		bundle.put( ASCENDED,   ascended );

		bundle.put(CHACEBLOOD,ChaicBlood);

		bundle.put(BossSelect,boss_enhance);

		bundle.put( LANTERACTIVE, lanterfireactive );
		bundle.put(GOLDCHEST,	  goldchestmazeCollected);

		bundle.put(DDK,dimandchestmazeCollected);

		bundle.put( CrivusFruitsLevel2, crivusfruitslevel2 );

		bundle.put( TPDoorDied, TPDoorDieds );

		bundle.put(MMC,mimicking);

		bundle.put( AnmyMobs, TryUsedAnmy );

		bundle.put( TIPSGO, tipsgodungeon );

		bundle.put( BUG_SYNC_FIXED, bugsyncfixed );

		bundle.put( NAYAZICOLLECTED, naiyaziCollected );

		bundle.put( GOLD,		goldCollected );
		bundle.put( DEEPEST,	deepestFloor );
		bundle.put( SLAIN,		enemiesSlain );
		bundle.put( FOOD,		foodEaten );
		bundle.put( ALCHEMY,    itemsCrafted );
		bundle.put( PIRANHAS,	piranhasKilled );
		bundle.put( ANKHS,		ankhsUsed );
		bundle.put(	EXLEVEL, realdeepestFloor);
		bundle.put( UPGRADES,   upgradesUsed );
		bundle.put( SNEAKS,		sneakAttacks );
		bundle.put( THROWN,		thrownAssists );

		bundle.put( SPAWNERS,	spawnersAlive );
		
		bundle.put( DURATION,	duration );

		bundle.put(NO_KILLING_QUALIFIED, qualifiedForNoKilling);
		
		bundle.put( AMULET,		amuletObtained );

		bundle.put( NOSHOPPING,	fireGirlnoshopping );

		bundle.put( SHOPPINGDIED, deadshoppingdied );

		bundle.put( WZGL, wangzheguilai );

		bundle.put( ENBR, endingbald );

		//SPD
		bundle.put("real_time_passed", second_elapsed);
		bundle.put("real_seconds_passed", real_seconds);
		bundle.put("turns_passed", turnsPassed);
	}
	
	public static void restoreFromBundle( Bundle bundle ) {

		//嗜血荆棘等级处理
		ChaicBlood   = bundle.getInt( CHACEBLOOD );

		//分数
		progressScore   = bundle.getInt( PROG_SCORE );
		heldItemValue   = bundle.getInt( ITEM_VAL );
		treasureScore   = bundle.getInt( TRES_SCORE );
		floorsExplored.clear();
		for (int i = 1; i < 26; i++){
			if (bundle.contains( FLR_EXPL+i )){
				floorsExplored.put(i, bundle.getBoolean( FLR_EXPL+i ));
			}
		}
		bundle.put( HIGHEST,	highestAscent );
		exploreScore    = bundle.getInt( EXPL_SCORE );
		if (bundle.contains( BOSS_SCORES )) bossScores = bundle.getIntArray( BOSS_SCORES );
		else                                bossScores = new int[5];
		totalBossScore  = bundle.getInt( TOT_BOSS );
		if (bundle.contains( QUEST_SCORES ))questScores = bundle.getIntArray( QUEST_SCORES );
		else                                questScores = new int[5];
		totalQuestScore = bundle.getInt( TOT_QUEST );
		winMultiplier   = bundle.getFloat( WIN_MULT );
		chalMultiplier  = bundle.getFloat( CHAL_MULT );
		totalScore      = bundle.getInt( TOTAL_SCORE );
		highestAscent   = bundle.getInt( HIGHEST );
		gameWon         = bundle.getBoolean( WON );
		ascended        = bundle.getBoolean( ASCENDED );


		dimandchestmazeCollected = bundle.getInt(DDK);

		goldchestmazeCollected = bundle.getInt(GOLDCHEST);

		naiyaziCollected = bundle.getInt(NAYAZICOLLECTED);

		mimicking = bundle.getBoolean(MMC);


		boss_enhance = bundle.getInt(BossSelect);

		goldCollected	= bundle.getInt( GOLD );
		deepestFloor	= bundle.getInt( DEEPEST );
		enemiesSlain	= bundle.getInt( SLAIN );
		foodEaten		= bundle.getInt( FOOD );
		itemsCrafted    = bundle.getInt( ALCHEMY );
		piranhasKilled	= bundle.getInt( PIRANHAS );
		ankhsUsed		= bundle.getInt( ANKHS );
		realdeepestFloor = bundle.getInt(EXLEVEL);
		upgradesUsed    = bundle.getInt( UPGRADES );
		sneakAttacks    = bundle.getInt( SNEAKS );
		thrownAssists   = bundle.getInt( THROWN );

		spawnersAlive   = bundle.getInt( SPAWNERS );
		
		duration		= bundle.getFloat( DURATION );

		qualifiedForNoKilling = bundle.getBoolean( NO_KILLING_QUALIFIED );

		TryUsedAnmy = bundle.getBoolean( AnmyMobs);
		
		amuletObtained	= bundle.getBoolean( AMULET );

		fireGirlnoshopping	= bundle.getBoolean( NOSHOPPING );
		deadshoppingdied = bundle.getBoolean( SHOPPINGDIED );

		wangzheguilai = bundle.getBoolean( WZGL );

		endingbald = bundle.getBoolean( ENBR );

		lanterfireactive = bundle.getBoolean( LANTERACTIVE );

		bugsyncfixed = bundle.getBoolean( BUG_SYNC_FIXED );

		crivusfruitslevel2 = bundle.getBoolean( CrivusFruitsLevel2 );

		TPDoorDieds  = bundle.getBoolean( TPDoorDied );

		tipsgodungeon = bundle.getBoolean(TIPSGO);

		//SPD
		second_elapsed = bundle.getFloat("real_time_passed");
		real_seconds =   bundle.getLong("real_seconds_passed");
		turnsPassed =    bundle.getFloat("turns_passed");
	}
	
	public static void preview( GamesInProgress.Info info, Bundle bundle ){
		info.goldCollected  = bundle.getInt( GOLD );
		info.maxDepth       = bundle.getInt( DEEPEST );
	}

}
