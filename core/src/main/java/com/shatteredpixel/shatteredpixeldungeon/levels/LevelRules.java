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

package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.CS;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.branch;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.SliceDeadBless;
import com.shatteredpixel.shatteredpixeldungeon.levels.hollow.AllSearchHollowActorLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.hollow.CerDogBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.hollow.MorpheusBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.hollow.MoveBoxHollowActorLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.hollow.PacmanHollowActorLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.hollow.TheatreLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.hollow.ZeroHallsBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.minilevels.DragonCaveLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.minilevels.DragonFestivalMiniLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.minilevels.HotelLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.minilevels.MiniBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.minilevels.MiniChestMazeLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.minilevels.MiniSkyShadowBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.nosync.DeepShadowLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.nosync.ForestHardBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.nosync.SkyGooBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.spical.GalaxyKeyBossLevel;
import com.watabou.utils.Random;

//Level Rules Test Android
public class LevelRules {

    public static Level createStandardLevel() {

            if(Statistics.bossRushMode){
                switch (depth) {
                    //T1-Boss区
                    case 1: return new AncityLevel();

                    case 3: return new SewerBossLevel();

                    case 5: return new ForestPoisonBossLevel();
                    case 7: return new ForestHardBossLevel();

                    case 9: return new PrisonBossLevel();

                    //T2-Boss区
                    case 11: return new LaveCavesBossLevel();

                    case 13:
                        return new ColdChestBossLevel();

                    case 15:
                        if(Statistics.difficultyDLCEXLevel >=2)
                            return new SkyGooBossLevel();
                        else {
                            return new BossRushItemLevel();
                        }

                    case 17:
                        if(Statistics.difficultyDLCEXLevel >=2)
                            return new DeepShadowLevel();
                        else {
                            return new BossRushItemLevel();
                        }

                    case 19:
                        return new GreenStlingBossLevel();

                    //T3-Boss区
                    case 21:
                        return new DragonFestivalMiniLevel();

                    case 23:
                        return new CavesBossLevel();

                    case 25:
                        return new ShopBossLevel();

                    case 27:
                        return new CavesGirlDeadLevel();

                    case 29:
                        return new CaveTwoBossLevel();

                    //T4-Boss区
                    case 31:
                        return new AncientMysteryCityBossLevel();

                    case 33:
                        return new DwarfGeneralBossLevel();

                    case 35:
                        return new DwarfMasterBossLevel();



                    case 39:
                        return new HallsBossLevel();

                    case 42:
                        return new YogGodHardBossLevel();



                    case 2:  case 4: case 6:  case 8: case 10:
                    //补给层 T2
                    case 12: case 14:case 16: case 18: case 20:
                    //补给层 T3
                    case 22: case 24:case 26: case 28: case 30:
                    //补给层 T4
                    case 32: case 34: case 36: case 37:case 38:
                    //普通结局
                    case 40: case 41:
                      return new BossRushItemLevel();

                    default:
                        Statistics.deepestFloor--;
                        return new DeadEndLevel();
                }
            } else {
                switch (depth) {
                    case 0:
                        if(Dungeon.isChallenged(CS)){
                            return new ZeroLevel();
                        } else {
                            return new NewZeroFiveLevel();
                        }
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        return new SewerLevel();
                    case 5:
                        if(Statistics.RandMode){
                            if(Statistics.ExFruit){
                                return new ForestHardBossLevel();
                            } else {
                                switch (Random.Int(5)){
                                    case 1: return new SkyGooBossLevel();
                                    case 2: return new ForestPoisonBossLevel();
                                    case 3:
                                        Statistics.ExFruit = true;
                                        return new ForestHardBossLevel();
                                    default:
                                        return new SLMKingLevel();
                                }
                            }
                        } else if(SPDSettings.level1boss() == 1){
                            return new ForestPoisonBossLevel();
                        } else if(SPDSettings.level1boss() == 3){
                            Statistics.ExFruit = true;
                            return new ForestHardBossLevel();
                        } else if(Statistics.ExFruit){
                            return new ForestHardBossLevel();
                        } else if(Challenges.activeChallenges()>8 && SPDSettings.level1boss()==1){
                            if (!Badges.isUnlocked(Badges.Badge.KILL_CLSISTER)){
                                Statistics.ExFruit = true;
                                return new ForestHardBossLevel();
                            } else if (Random.Float()<=0.7f){
                                Statistics.ExFruit = true;
                                return new ForestHardBossLevel();
                            } else {
                                return new ForestPoisonBossLevel();
                            }
                        } else if(Badges.isUnlocked(Badges.Badge.KILL_CLSISTER) && Random.Float()<=0.2f) {
                            Statistics.ExFruit = true;
                            return new ForestHardBossLevel();
                        } else {
                            return new ForestPoisonBossLevel();
                        }
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                        return new PrisonLevel();
                    case 10:
                       if(Statistics.RandMode){
                           switch (Random.Int(9)){
                               case 1: return new DeepShadowLevel();
                               case 3: return new ColdChestBossLevel();
                               case 5: return new GreenStlingBossLevel();
                               case 7: return new CavesBossLevel();
                               default:
                                   return new PrisonBossLevel();

                           }
                        } else if(Statistics.mimicking && !Statistics.mustTengu){
                            return new ColdChestBossLevel();
                        } else if ((Statistics.boss_enhance & 0x2) != 0 && !Statistics.mustTengu) {
                            return new ColdChestBossLevel();
                        } else {
                            return new PrisonBossLevel();
                       }
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                        return new CavesLevel();
                    case 15:
                        if(Statistics.RandMode){
                            switch (Random.Int(5)){
                                case 2: return new CavesGirlDeadLevel();
                                case 3: return new CaveTwoBossLevel();
                                case 4: return new DwarfMasterBossLevel();
                                default:
                                    return new AncientMysteryCityBossLevel();
                            }
                        } else if ((Statistics.boss_enhance & 0x4) != 0) {
                            return new CavesGirlDeadLevel();
                        } else {
                            if(Random.Float() <= 0.4f && !Statistics.dm300Fight || Statistics.dm720Fight){
                                return new CaveTwoBossLevel();
                            } else {
                                return new CavesBossLevel();
                            }
                        }
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                        return new CityLevel();
                    case 20:
                        if(Statistics.RandMode){
                            switch (Random.Int(6)){
                                case 1: return new DwarfMasterBossLevel();
                                case 3: return new DwarfGeneralBossLevel();
                                case 5: return new NewCityBossLevel();
                                default:
                                        return new ShopBossLevel();
                            }
                        } else {
                            return new NewCityBossLevel();
                        }
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                        return new HallsLevel();
                    case 25:
                        if ((Statistics.boss_enhance & 0x10) != 0 || Dungeon.isChallenged(CS) || Statistics.RandMode ) {
                            return new YogGodHardBossLevel();
                        } else {
                            return new HallsBossLevel();
                        }
                    case 26:
                        return !Statistics.Hollow_Holiday ? new LastLevel() : new HollowExitLevel();

                    case 27: case 28: case 29:   case 30:
                        return Statistics.Hollow_Holiday ? new HollowLevel() : new DeadEndLevel();

                    case 31:
                        if(hero.buff(SliceDeadBless.class) !=null || Statistics.AbyssCityRules == 2){
                            return new TheatreLevel();
                        } else {
                            return new CerDogBossLevel();
                        }

                    case 32:
                        if(hero.buff(SliceDeadBless.class) !=null  || Statistics.AbyssCityRules == 2){
                            return new TheatreLevel();
                        } else {
                            return new DeadEndLevel();
                        }

                    case 33:
                        return new MorpheusBossLevel();

                    default:
                        Statistics.deepestFloor--;
                        return new DeadEndLevel();
                }
            }
    }

    public static Level createBranchLevel() {
        switch (branch) {
            default:
            case 1:
                switch (depth) {
                    case 0:
                        return new HotelLevel();
                    case 5:
                        return new DragonCaveLevel();
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                        return new MiningLevel();
                    case 17:
                    case 18:
                        return new AncientMysteryCityLevel();
                    case 20:
                        return new DwarfGeneralBossLevel();
                    case 31:
                        return new PacmanHollowActorLevel();
                    default:
                        return new DeadEndLevel();
                }

            case 2:
                switch (depth) {
                    case 4:
                        return new MiniBossLevel();
                    case 8:
                        return new MiniSkyShadowBossLevel();
                    case 5:
                        return new DragonCaveLevel();
                    case 17:
                    case 18:
                        return new AncientMysteryCityLevel();
                    case 31:
                        return new MoveBoxHollowActorLevel();
                    default:
                        return new DeadEndLevel();
                }

            case 3:
                switch (depth) {
                    case 5:
                        return new LaveCavesBossLevel();
                    case 17:
                    case 18:
                        return new AncientMysteryCityBossLevel();
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                        return new DragonFestivalMiniLevel();
                    case 31:
                        return new AllSearchHollowActorLevel();
                    default:
                        return new DeadEndLevel();
                }

            case 4:
                switch (depth) {
                    case 25:
                        return new HollowExitLevel();
                    case 17:
                    case 18:
                        return new GardenLevel();
                    case 10: case 11: case 13:
                        return new MiniChestMazeLevel();
                    default:
                        return new DeadEndLevel();
                }

            case 5:
                switch (depth) {
                    case 17:
                        return new GardenLevel();
                    case 0:
                        return new HiroFlowerLevel();
                    default:
                        return new DeadEndLevel();
                }

            case 6:
                return new LinkLevel();

            case 7:
                return new ShopBossLevel();

            case 8:
                switch (depth) {
                    case 1:
                        return new AncityLevel();

                    case 2:
                        return new ForestPoisonBossLevel();

                    case 4:
                        return new ForestHardBossLevel();

                    case 6:
                        return new SLMKingLevel();

                    case 7:
                        if (Statistics.difficultyDLCEXLevel >= 2) {
                            return new SkyGooBossLevel();
                        } else {
                            return new BossRushItemLevel();
                        }

                    case 9:
                        return new PrisonBossLevel();

                    case 11:
                        return new ColdChestBossLevel();

                    case 13:
                        return new GreenStlingBossLevel();

                    case 14:
                        if (Statistics.difficultyDLCEXLevel >= 2) {
                            return new DeepShadowLevel();
                        } else {
                            return new BossRushItemLevel();
                        }

                        //御三家 最难时刻
                    case 16:
                        return new CavesBossLevel();
                    case 17:
                        return new CaveTwoBossLevel();
                    case 18:
                        return new CavesGirlDeadLevel();

                    case 21:
                        return new ShopBossLevel();

                    case 23:
                        return new AncientMysteryCityBossLevel();
                    case 24:
                        return new NewCityBossLevel();

                    case 27:
                        return new DwarfMasterBossLevel();

                    case 29:
                        return new HallsBossLevel();

                    case 31:
                        return new YogGodHardBossLevel();

                    //补给层 T1
                    case 3:
                    case 5:
                    case 8:
                    case 10:
                        //补给层 T2
                    case 12:
                    case 15:
                    case 19:
                    case 20:
                        //补给层 T3
                    case 22:
                    case 25:case 26:
                    case 28:
                    case 30:
                        return new BossRushItemLevel();

                    default:
                        Statistics.deepestFloor--;
                        return new DeadEndLevel();
                }

            case 10:
                switch (depth) {
                    default:
                        Statistics.deepestFloor--;
                        return new DeadEndLevel();
                    case 26:
                        return new GalaxyKeyBossLevel();
                    case 25:
                        return new ZeroHallsBossLevel();
                }

            case 12:
                switch (depth) {
                    default:
                        Statistics.deepestFloor--;
                        return new DeadEndLevel();
                    case 0:
                        return new ForestPoisonBossLevel();
                    case 1:
                        return new SewerBossLevel();
                    case 2:
                        return new ForestHardBossLevel();
                    case 3:
                        return new LaveCavesBossLevel();
                    case 4:
                        return new PrisonBossLevel();
                    case 5:
                        return new DeepShadowLevel();
                    case 6:
                        return new ColdChestBossLevel();
                    case 7:
                        return new CavesBossLevel();
                    case 8:
                        return new CaveTwoBossLevel();
                    case 9:
                        return new CavesGirlDeadLevel();
                    case 10:
                        return new DwarfGeneralBossLevel();
                    case 11:
                        return new DwarfMasterBossLevel();
                    case 12:
                        return new NewCityBossLevel();
                    case 13:
                        return new DragonFestivalMiniLevel();
                    case 14:
                        return new AncientMysteryCityBossLevel();
                    case 15:
                        return new HallsBossLevel();
                    case 16:
                        return new YogGodHardBossLevel();
                    case 17:
                        return new ShopBossLevel();
                    case 18:
                        return new CerDogBossLevel();
                    case 19:
                        return new MorpheusBossLevel();
                    case 20:
                        return new SkyGooBossLevel();
                    case 21:
                        return new GreenStlingBossLevel();
                    case 22:
                        return new MiniSkyShadowBossLevel();
                    case 23:
                        return new SLMKingLevel();
                    case 24:
                        return new DM920BossLevel();
                }
        }
    }


}
