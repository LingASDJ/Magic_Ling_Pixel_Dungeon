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
  public static int naiyaziCollected;
  public static boolean iscustomName = false;
  // used for hero unlock badges
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

  // TODO 灯火前行
  public static boolean lanterfireactive = false;

  // TODO BUG修复同步
  public static boolean bugsyncfixed = false;

  // TODO 警告
  public static boolean tipsgodungeon = false;

  public static int dageCollected;

  // Directly add float time will cause accuracy lose and stop timing if time is long enough
  // so use long to record seconds, float to count sub-seconds.
  // SPD-V1.3.2-ITEM SPAWN CODE
  public static long real_seconds = 0;
  public static float second_elapsed = 0;
  public static float turnsPassed = 0f;

  public static SparseArray<Boolean> floorsExplored = new SparseArray<>();

  public static void reset() {

    goldCollected = 0;
    deepestFloor = -1;
    enemiesSlain = 0;
    foodEaten = 0;
    itemsCrafted = 0;
    piranhasKilled = 0;
    ankhsUsed = 0;

    upgradesUsed = 0;
    sneakAttacks = 0;
    thrownAssists = 0;
    realdeepestFloor = 0;
    spawnersAlive = 0;

    duration = 0;

    qualifiedForNoKilling = false;

    amuletObtained = false;

    tipsgodungeon = false;

    fireGirlnoshopping = false;

    deadshoppingdied = false;
    wangzheguilai = false;
    endingbald = false;

    lanterfireactive = false;
    bugsyncfixed = false;

    second_elapsed = 0f;
    real_seconds = 0;
    turnsPassed = 0f;
  }

  private static final String GOLD = "score";
  private static final String DEEPEST = "maxDepth";
  private static final String SLAIN = "enemiesSlain";
  private static final String FOOD = "foodEaten";
  private static final String ALCHEMY = "potionsCooked";
  private static final String PIRANHAS = "priranhas";
  private static final String ANKHS = "ankhsUsed";

  private static final String UPGRADES = "upgradesUsed";
  private static final String SNEAKS = "sneakAttacks";
  private static final String THROWN = "thrownAssists";

  private static final String SPAWNERS = "spawnersAlive";

  private static final String DURATION = "duration";

  private static final String NO_KILLING_QUALIFIED = "qualifiedForNoKilling";

  private static final String AMULET = "amuletObtained";

  // 浊焰契约
  private static final String NOSHOPPING = "fireGirlnoshopping";

  private static final String SHOPPINGDIED = "deadshoppingdied";

  private static final String WZGL = "wangzheguilai";

  private static final String ENBR = "endingbald";

  private static final String EXLEVEL = "Exlevel";

  // TODO 灯火前行
  private static final String LANTERACTIVE = "lanterfireactive";

  // TODO BUG修复的机制
  private static final String BUG_SYNC_FIXED = "bugsyncfixed";

  private static final String TIPSGO = "tipsgo";

  public static void storeInBundle(Bundle bundle) {
    bundle.put(LANTERACTIVE, lanterfireactive);

    bundle.put(TIPSGO, tipsgodungeon);
    // TODO BUG修复的机制
    bundle.put(BUG_SYNC_FIXED, bugsyncfixed);

    bundle.put(GOLD, goldCollected);
    bundle.put(DEEPEST, deepestFloor);
    bundle.put(SLAIN, enemiesSlain);
    bundle.put(FOOD, foodEaten);
    bundle.put(ALCHEMY, itemsCrafted);
    bundle.put(PIRANHAS, piranhasKilled);
    bundle.put(ANKHS, ankhsUsed);
    bundle.put(EXLEVEL, realdeepestFloor);
    bundle.put(UPGRADES, upgradesUsed);
    bundle.put(SNEAKS, sneakAttacks);
    bundle.put(THROWN, thrownAssists);

    bundle.put(SPAWNERS, spawnersAlive);

    bundle.put(DURATION, duration);

    bundle.put(NO_KILLING_QUALIFIED, qualifiedForNoKilling);

    bundle.put(AMULET, amuletObtained);

    bundle.put(NOSHOPPING, fireGirlnoshopping);

    bundle.put(SHOPPINGDIED, deadshoppingdied);

    bundle.put(WZGL, wangzheguilai);

    bundle.put(ENBR, endingbald);

    // SPD
    bundle.put("real_time_passed", second_elapsed);
    bundle.put("real_seconds_passed", real_seconds);
    bundle.put("turns_passed", turnsPassed);
  }

  public static void restoreFromBundle(Bundle bundle) {
    goldCollected = bundle.getInt(GOLD);
    deepestFloor = bundle.getInt(DEEPEST);
    enemiesSlain = bundle.getInt(SLAIN);
    foodEaten = bundle.getInt(FOOD);
    itemsCrafted = bundle.getInt(ALCHEMY);
    piranhasKilled = bundle.getInt(PIRANHAS);
    ankhsUsed = bundle.getInt(ANKHS);
    realdeepestFloor = bundle.getInt(EXLEVEL);
    upgradesUsed = bundle.getInt(UPGRADES);
    sneakAttacks = bundle.getInt(SNEAKS);
    thrownAssists = bundle.getInt(THROWN);

    spawnersAlive = bundle.getInt(SPAWNERS);

    duration = bundle.getFloat(DURATION);

    qualifiedForNoKilling = bundle.getBoolean(NO_KILLING_QUALIFIED);

    amuletObtained = bundle.getBoolean(AMULET);

    fireGirlnoshopping = bundle.getBoolean(NOSHOPPING);
    deadshoppingdied = bundle.getBoolean(SHOPPINGDIED);

    wangzheguilai = bundle.getBoolean(WZGL);

    endingbald = bundle.getBoolean(ENBR);

    lanterfireactive = bundle.getBoolean(LANTERACTIVE);

    bugsyncfixed = bundle.getBoolean(BUG_SYNC_FIXED);

    tipsgodungeon = bundle.getBoolean(TIPSGO);

    // SPD
    second_elapsed = bundle.getFloat("real_time_passed");
    real_seconds = bundle.getLong("real_seconds_passed");
    turnsPassed = bundle.getFloat("turns_passed");
  }

  public static void preview(GamesInProgress.Info info, Bundle bundle) {
    info.goldCollected = bundle.getInt(GOLD);
    info.maxDepth = bundle.getInt(DEEPEST);
  }
}
