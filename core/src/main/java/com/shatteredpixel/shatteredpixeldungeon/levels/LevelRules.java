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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.branch;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RandomBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.TestDwarfMasterLock;
import com.watabou.utils.Random;

//Level Rules
public class LevelRules {
    public static Level createBossRushLevel() {
        switch (depth) {
            case 0:
            case 17:
            case 27:
                Buff.affect(hero, RandomBuff.class).set(5, 1);
                return new AncityLevel();
            case 1: case 3: case 6: case 7: case 9:
            case 11: case 13: case 15: case 18: case 20: case 24:
                return new ItemLevel();
            case 2:
                return new ForestBossLevel();
            case 4:
                return new SewerBossLevel();
            case 5:
                return new SLMKingLevel();
            case 8:
                return new PrisonBossLevel();
            case 10:
                return new DimandKingLevel();
            case 12:
                return new CavesBossLevel();
            case 14:
                return new CaveTwoBossLevel();
            case 16:
                return new CavesGirlDeadLevel();
            case 19:
                return new ShopBossLevel();
            case 21:
                return new NewCityBossLevel();
            case 22: case 23:
                Buff.affect(hero, TestDwarfMasterLock.class).set(10, 1);
                return new CityLevel();
            case 25:
                return new DwarfMasterBossLevel();
            case 26:
                return new YogGodHardBossLevel();
//            case 28:
//                Buff.affect(hero, TestDwarfMasterLock.class).set(1, 1);
//                return new DM920BossLevel();
            default:
                Statistics.deepestFloor--;
                return new DeadEndLevel();
        }
    }

    public static Level createStandardLevel() {
            switch (depth) {
                case 0:
                    return new ZeroLevel();
                case 1:
                    //return new AncityMiniLevel();
                case 2:
                case 3:
                case 4:
                    return new SewerLevel();
                case 5:
                    return new ForestBossLevel();
                case 6:
                case 7:
                case 8:
                case 9:
                    return new PrisonLevel();
                case 10:
                    if ((Statistics.boss_enhance & 0x2) != 0 || Statistics.mimicking) {
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
                    if ((Statistics.boss_enhance & 0x4) != 0) {
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
                    if ((Statistics.boss_enhance & 0x8) != 0) {
                        Buff.affect(hero, TestDwarfMasterLock.class).set(1, 1);
                        return new DwarfMasterBossLevel();
                    } else {
                        return new NewCityBossLevel();
                    }
                case 21:
                case 22:
                case 23:
                case 24:
                    return new HallsLevel();
                case 25:
                    if ((Statistics.boss_enhance & 0x10) != 0) {
                        return new YogGodHardBossLevel();
                    } else {
                        return new HallsBossLevel();
                    }
                case 26:
                    return new LastLevel();
                case 27:
                case 28:
                case 29:
                case 30:
                    return new HollowLevel();
                case 31:
                    if ((Statistics.boss_enhance & 0x12) != 0) {
                        return new LastLevel();
                    } else {
                        return new LaveCavesBossLevel();
                    }
                case 50:
                    return new GardenLevel();
                default:
                    Statistics.deepestFloor--;
                    return new DeadEndLevel();
            }
    }

    public static Level createBranchLevel() {
        switch (branch){
            default:
            case 1:
                switch (depth) {
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                        return new MiningLevel();
                    case 17: case 18:
                        return new AncientMysteryCityLevel();
                    default:
                        return new DeadEndLevel();
                }

            case 2:
                switch (depth) {
                    case 4: case 14:
                       return new MiniBossLevel();
                   case 17: case 18:
                        return new AncientMysteryCityLevel();
                    default:
                        return new DeadEndLevel();
                }

            case 3:
                switch (depth){
                    case 17: case 18:
                        return new AncientMysteryCityBossLevel();
                    default:
                        return new DeadEndLevel();
                }

            case 5:
                switch (depth){
                    case 17: case 18:
                        return new GardenLevel();
                    default:
                        return new DeadEndLevel();
                }
        }
    }


}
