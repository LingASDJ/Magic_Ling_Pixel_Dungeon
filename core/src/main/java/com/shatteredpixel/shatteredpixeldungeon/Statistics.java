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
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

public class Statistics {

	//统计分数
	public static int progressScore;
	//种子处罚
	public static boolean seedCustom = false;
	public static boolean ExFruit = false;
	//神器限制
	public static boolean ARLing = false;
	public static boolean CatFirst = false;
	public static boolean snow = false;

	//小叶12挑首次对话
	public static boolean SmallLeafGet = false;
	public static boolean noClearKill = false;

	public static int zeroItemLevel;

	public static int hcDialogLevel;

	public static boolean unLockedFireDargon = false;

	//游戏局内杀死火龙
	public static boolean GameKillFireDargon = false;

	public static int wcDialogLevel;


	public static boolean youNoItem = false;

	//中继器电话号码
	public static int commonrelaycall;

	public static int GetFoodLing;

	//中秋节幽灵特别行动
	public static boolean findMoon = false;

	//妻管严
	public static boolean deadGo = false;

	public static boolean doNotLookLing = false;

	public static boolean NoTime = false;

	//萨卡班甲鱼二阶段
	public static int sakaBackStage;


	//难度等级
	public static int difficultyDLCEXLevel;

	public static int readBooks;
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

	public static int[] bossScores = new int[6];
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

	public static int HealingIsDied;

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

	//异果
	public static int CrivusbossTeleporter;
	public static boolean crivusfruitslevel3 = false;

	//仙人跳背包问题
	public static boolean ankhToExit = false;

	//拟态之王
	public static boolean TPDoorDieds = false;

	//前路优化
	public static boolean noGoReadHungry = false;

	//警告
	public static boolean tipsgodungeon = false;

	//宝藏迷宫
	public static int goldchestmazeCollected;

	//珍宝
	public static int dimandchestmazeCollected;

	//宝物生成限制，避免有byd的十字架重复刷取
	public static int fuckGeneratorAlone;

	public static int dageCollected;
	public static boolean Alone;

	//首次对决
	public static boolean mimicking = false;

	public static boolean mustTengu = false;

	public static boolean dm720Fight = false;

	public static boolean dm300Fight = false;

	public static boolean gooFight = false;

	public static boolean onlyBzmdr = false;

	//昼夜更替
	public static int gameTime;
	public static int gameDay;
	public static boolean gameNight;

	public static int dwarfchange;

	//随机嬗变
	public static int RandModeCount;

	//特殊模式
	public static boolean bossRushMode = false;
	public static boolean RandMode = false;

	//噩梦循环模式
	public static boolean NightDreamLoop = false;

	public static boolean TryUsedAnmy = false;
	public static boolean winGame = false;
	public static boolean DwarfMasterKing = false;
	//220---SPD
	public static boolean qualifiedForBossChallengeBadge = false;
	public static boolean dwarfKill = false;
	public static boolean TrueYogNoDied = false;

	public static int killYogMobsAnargy;

	private static final String ICECLAN ="iceCyanBlueSquareCoin";

	private static final String GAMETIME = "gameTime";

	private static final String GAMENIGHT = "gamenight";

    private static final String LOVX = "lovx";

	private static final String MSTG = "musttengu";

	private static final String LOCD = "locd";

    private static final String FUCKALONE = "fuckplayer";
    private static final String WINGAME = "wingame";
    private static final String HIDEEN = "hideen";
	private static final String DWSX = "dwsxxx";

	private static final String STS = "sts";

    private static final String BOSS_CHALLENGE_QUALIFIED = "qualifiedForBossChallengeBadge";
	//Directly add float time will cause accuracy lose and stop timing if time is long enough
	//so use long to record seconds, float to count sub-seconds.
    //SPD-V1.3.2-ITEM SPAWN CODE
    public static long real_seconds = 0;
    public static float second_elapsed = 0;
    public static float turnsPassed = 0f;
	
	private static final String GOLD		= "score";
	private static final String HAPPY		= "happy";
	private static final String RAND		= "randmode";
	private static final String NIGHTDR		= "nightdr";

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

	private static final String CrivusFruitsLevel3		= "crivusfruitslevel3";

	//拟态之王
	private static final String TPDoorDied		= "TPDoorDieds";

	private static final String TIPSGO		= "tipsgo";

	private static final String GOLDCHEST		= "goldchest";

	private static final String NAYAZICOLLECTED		= "naiyaziCollected";

	private static final String AnmyMobs		= "anmymobs";

	private static final String MMC		= "mmcsx";

	private static final String DDK		= "dada";

	private static final String BDK		= "bossLingCR";

	private static final String BossSelect	= "bossselect";

	private static final String XRTANKH	= "xrtankh";

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


	private static final String HEALDIED		= "HealingisDied";

	private static final String READBOOKS		= "readbooks";

	private static final String SAKATWO		= "sakatwo";

	private static final String BDTX		= "bdtx";

	private static final String LOVE		= "love";

	private static final String SEEDCUSTOM		= "seedCustom";

	private static final String SIDERLING      = "siderLing";

	private static final String DM720FIGHT      = "dm720FIGHT";

	private static final String DM300FIGHT      = "dm300FIGHT";

	private static final String GOOFIGHT      = "gooFIGHT";

	private static final String ZEROLEVEL      = "zerolevel";

	private static final String HKLEVEL      = "hklevel";
	private static final String WKLEVEL      = "wklevel";


	private static final String EXBOSS1		= "EXBOSS1";

	private static final String XOL		= "xol";



    public static void reset() {
        boss_enhance = 0;
        ChaicBlood = 0;
        readBooks = 0;

		ExFruit = false;

		DwarfMasterKing = false;

		ARLing = false;

		dwarfKill = false;

		CatFirst = false;

		SmallLeafGet = false;
		noClearKill = false;

        HealingIsDied = 0;

		dwarfchange = Random.Int(1,101);

		NoTime = false;
		dm720Fight = false;
		dm300Fight = false;
		onlyBzmdr = false;

		gameDay = 1;

		ascended = false;

		zeroItemLevel = 0;
		hcDialogLevel = 0;
		youNoItem = false;
		unLockedFireDargon = false;

		TrueYogNoDied = false;

		GameKillFireDargon = false;

		gameTime = 0;
		gameNight = false;

		gooFight = false;
		mustTengu = false;


		difficultyDLCEXLevel = 0;
		wcDialogLevel = 0;
        bossRushMode = false;
		RandMode = false;

		NightDreamLoop = false;

		RandModeCount = 0;

		killYogMobsAnargy = 0;

        findMoon = false;
		deadGo = false;
		doNotLookLing = false;
		//萨卡班甲鱼二阶段
		sakaBackStage = 0;

		goldCollected	= 0;
		deepestFloor	= -1;
		enemiesSlain	= 0;
		foodEaten		= 0;
		goldchestmazeCollected = 0;
		dimandchestmazeCollected =0;
		fuckGeneratorAlone = 0;
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
        qualifiedForBossChallengeBadge = false;
        mimicking = false;

		//生成商店限制
		Alone = false;

		commonrelaycall = Random.IntRange(10000000,99999999);

		amuletObtained = false;

		tipsgodungeon = false;

		fireGirlnoshopping = false;

		deadshoppingdied = false;
		wangzheguilai = false;
		endingbald = false;

		lanterfireactive = false;
		noGoReadHungry =  false;

		crivusfruitslevel2 = false;
		crivusfruitslevel3 = false;
		ankhToExit = false;

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
		bossScores      = new int[6];
		totalBossScore  = 0;
		questScores     = new int[5];
		totalQuestScore = 0;
		winMultiplier   = 1;
		chalMultiplier  = 1;
		totalScore      = 0;
		seedCustom = false;
		GetFoodLing = 0;
		CrivusbossTeleporter = 0;
	}

    public static void storeInBundle(Bundle bundle) {

		bundle.put(ICECLAN, RandModeCount);


        bundle.put(HIDEEN, DwarfMasterKing);

		bundle.put("KILL_YOG",killYogMobsAnargy);

		bundle.put(DWSX, dwarfKill);

		bundle.put(STS, TrueYogNoDied);

        bundle.put(WINGAME, winGame);

		bundle.put(LOCD,doNotLookLing);

        bundle.put(LOVE,findMoon);

		bundle.put(ZEROLEVEL,zeroItemLevel);

		bundle.put(HKLEVEL,hcDialogLevel);

		bundle.put(WKLEVEL,wcDialogLevel);

		bundle.put(LOVX,deadGo);

		bundle.put(SIDERLING, GetFoodLing);

		bundle.put(FUCKALONE,fuckGeneratorAlone);

		bundle.put(SEEDCUSTOM,seedCustom);

		bundle.put(EXBOSS1,ExFruit);

		bundle.put("ARLing",ARLing);

		bundle.put("CAT",CatFirst);

		bundle.put("SMALLEAFTGET",SmallLeafGet);

		bundle.put("NOCLEAR",noClearKill);

		bundle.put("SNOW",snow);

		bundle.put(DM720FIGHT,dm720Fight);

		bundle.put(DM300FIGHT,dm300Fight);

		bundle.put(GOOFIGHT,gooFight);

		bundle.put("onlyB",onlyBzmdr);

		bundle.put("NOTIME",NoTime);

		//分数
		bundle.put( PROG_SCORE,  progressScore );
		bundle.put( ITEM_VAL,    heldItemValue );
		bundle.put( TRES_SCORE,  treasureScore );
		for (int i = 1; i < 26; i++){
			if (floorsExplored.containsKey(i)){
				bundle.put( FLR_EXPL+i, floorsExplored.get(i) );
			}
		}
		bundle.put( HEALDIED, HealingIsDied);

		bundle.put( READBOOKS, readBooks);

		bundle.put( EXPL_SCORE,  exploreScore );

		bundle.put( HAPPY, bossRushMode);
		bundle.put( RAND, RandMode);
		bundle.put( NIGHTDR, NightDreamLoop);

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

		bundle.put(XRTANKH,ankhToExit);

		bundle.put( LANTERACTIVE, lanterfireactive );
		bundle.put(GOLDCHEST,	  goldchestmazeCollected);

		bundle.put(DDK,dimandchestmazeCollected);

		bundle.put(BDK,CrivusbossTeleporter);

		bundle.put( CrivusFruitsLevel2, crivusfruitslevel2 );

		bundle.put( CrivusFruitsLevel3, crivusfruitslevel3 );

		bundle.put( TPDoorDied, TPDoorDieds );

		bundle.put(MMC,mimicking);

		bundle.put( AnmyMobs, TryUsedAnmy );

		bundle.put( TIPSGO, tipsgodungeon );

		bundle.put( BUG_SYNC_FIXED, noGoReadHungry);

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

		bundle.put( SAKATWO, sakaBackStage );

		bundle.put( XOL, difficultyDLCEXLevel);

		bundle.put(MSTG,mustTengu);

		bundle.put("DWCHSAG",dwarfchange);

		//SPD
		bundle.put("real_time_passed", second_elapsed);
		bundle.put("real_seconds_passed", real_seconds);
        bundle.put("turns_passed", turnsPassed);
        bundle.put(BOSS_CHALLENGE_QUALIFIED, qualifiedForBossChallengeBadge);

		bundle.put(GAMETIME,gameTime);
		bundle.put(GAMENIGHT,gameNight);
		bundle.put("gameday",gameDay);

		bundle.put("YOU",youNoItem);
		bundle.put("DCXFG",unLockedFireDargon);
		bundle.put("DRAM",GameKillFireDargon);

		bundle.put("ALXONE",Alone);
	}
	
	public static void restoreFromBundle( Bundle bundle ) {

		GameKillFireDargon = bundle.getBoolean("DRAM");

		TrueYogNoDied = bundle.getBoolean(STS);

		unLockedFireDargon = bundle.getBoolean("DCXFG");
		RandModeCount = bundle.getInt(ICECLAN);

		NightDreamLoop = bundle.getBoolean(NIGHTDR);

		killYogMobsAnargy = bundle.getInt("KILL_YOG");

		hcDialogLevel = bundle.getInt(HKLEVEL);

		snow = bundle.getBoolean("SNOW");

		CrivusbossTeleporter = bundle.getInt(BDK);

		GetFoodLing = bundle.getInt( SIDERLING);

		dwarfchange = bundle.getInt("DWCHSAG");

		gameDay = bundle.getInt("gameday");

		winGame = bundle.getBoolean(WINGAME);

		DwarfMasterKing = bundle.getBoolean(HIDEEN);

		difficultyDLCEXLevel = bundle.getInt(XOL);

		wcDialogLevel = bundle.getInt(WKLEVEL);

		CatFirst = bundle.getBoolean("CAT");

		SmallLeafGet = bundle.getBoolean("SMALLEAFTGET");

		noClearKill = bundle.getBoolean("NOCLEAR");

		doNotLookLing = bundle.getBoolean(LOCD);

		seedCustom = bundle.getBoolean(SEEDCUSTOM);

		ankhToExit = bundle.getBoolean(XRTANKH);

		ExFruit = bundle.getBoolean(EXBOSS1);

		ARLing = bundle.getBoolean("ARLing");

		dwarfKill = bundle.getBoolean(DWSX);

		onlyBzmdr = bundle.getBoolean("onlyB");

		youNoItem = bundle.getBoolean("YOU");

		zeroItemLevel = bundle.getInt(ZEROLEVEL);

		NoTime = bundle.getBoolean("NOTIME");

		//嗜血荆棘等级处理
		ChaicBlood   = bundle.getInt( CHACEBLOOD );

		dm720Fight = bundle.getBoolean(DM720FIGHT);

		dm300Fight = bundle.getBoolean(DM300FIGHT);

		gooFight = bundle.getBoolean(GOOFIGHT);

		fuckGeneratorAlone = bundle.getInt(FUCKALONE);

		Alone = bundle.getBoolean("ALXONE");

		HealingIsDied   = bundle.getInt( HEALDIED );

		readBooks = bundle.getInt( READBOOKS );

		sakaBackStage = bundle.getInt( SAKATWO );

		findMoon = bundle.getBoolean(LOVE);
		deadGo = bundle.getBoolean(LOVX);

		mustTengu = bundle.getBoolean(MSTG);

		gameTime = bundle.getInt(GAMETIME);

		gameNight = bundle.getBoolean(GAMENIGHT);

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
		else                                bossScores = new int[6];
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
		bossRushMode = bundle.getBoolean(HAPPY);
		RandMode = bundle.getBoolean(RAND);

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

		noGoReadHungry = bundle.getBoolean( BUG_SYNC_FIXED );

		crivusfruitslevel2 = bundle.getBoolean( CrivusFruitsLevel2 );

		crivusfruitslevel3 = bundle.getBoolean( CrivusFruitsLevel3 );

		TPDoorDieds  = bundle.getBoolean( TPDoorDied );

		tipsgodungeon = bundle.getBoolean(TIPSGO);

		//SPD
		second_elapsed = bundle.getFloat("real_time_passed");
		real_seconds =   bundle.getLong("real_seconds_passed");
        turnsPassed = bundle.getFloat("turns_passed");
        qualifiedForBossChallengeBadge = bundle.getBoolean(BOSS_CHALLENGE_QUALIFIED);
	}
	
	public static void preview( GamesInProgress.Info info, Bundle bundle ){
		info.goldCollected  = bundle.getInt( GOLD );
		info.maxDepth       = bundle.getInt( DEEPEST );
	}

}
