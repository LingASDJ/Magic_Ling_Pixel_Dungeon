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

package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.AquaBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.BlizzardBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.CausticBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.InfernalBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.ShockingBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfShielding;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.RandomChest;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfAntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfChallenge;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSirensSong;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.CavesPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Random;

public class BossRushItemLevel extends RegularLevel {

    {
        color1 = 0x534f3e;
        color2 = 0xb9d661;
    }

    @Override
    protected int standardRooms(boolean forceMax) {
        if (forceMax) return 1;
        //5 to 7, average 5.57
        return 1+ Dungeon.depth/5+ Random.chances(new float[]{1,1,1});
    }

    @Override
    protected int specialRooms(boolean forceMax) {
        if (forceMax) return 1;
        //1 to 3, average 2.2
        return Dungeon.depth< 25 ? 1+ Dungeon.depth/5+ Random.chances(new float[]{1,1,1}) : 0;
    }

    @Override
    public void playLevelMusic() {
        switch (depth){
            case 1:case 2:case 3:case 4:case 5:case 6:case 7:case 8:
                Music.playModeBGM(Assets.Music.JUNGLE_FOREST, true);
            break;
            case 9:case 10:case 11:case 12:case 13:case 14:case 15:case 16:
                Music.playModeBGM(Assets.Music.BGM_2, true);
            break;
            case 17:case 18:case 19:case 20:case 21:case 22:case 23:case 24:
                Music.playModeBGM(Assets.Music.BGM_3, true);
            break;
            case 25:case 26:case 27:case 28:case 29:case 30:case 31:case 32:
                Music.playModeBGM(Assets.Music.BGM_4, true);
            break;
            case 33:case 34:case 35:case 36:case 37:case 38:case 39:case 40:case 41:case 42:
                Music.playModeBGM(Assets.Music.BGM_5, true);
            break;
        }

    }

    @Override
    protected void createItems() {
       switch (depth){
           //T1 补给层
           case 3:  case 5: case 7: case 8: case 10:
                addItemToSpawn(new PotionOfStrength());
                addItemToSpawn(new PotionOfHealing());
                addItemToSpawn(new PotionOfHealing());
                addItemToSpawn(new RandomChest());
                addItemToSpawn(new ScrollOfUpgrade());
                addItemToSpawn(new PotionOfExperience());
                addItemToSpawn(Generator.random(Generator.Category.FOOD));
                addItemToSpawn(Generator.random(Generator.Category.FOOD));
          break;
           //T2 补给层
         case 12: case 14: case 15:
               addItemToSpawn(new PotionOfExperience());
               addItemToSpawn(new PotionOfHealing());
               addItemToSpawn(Generator.random(Generator.Category.FOOD));
               addItemToSpawn(Generator.randomWeapon());
               addItemToSpawn(Generator.randomArmor());
               if(Random.Float()<0.4f){
                   addItemToSpawn(new RandomChest());
               }

               //3.0 BossRushT2等级 补给层新增了3个物品
               //1个升级卷轴+1个随机2阶投掷+1个嬗变卷轴
               addItemToSpawn(new ScrollOfUpgrade());
               addItemToSpawn(Generator.random(Generator.Category.MIS_T2));
               addItemToSpawn(new ScrollOfTransmutation());
               //T2+ 补给层
               break;
           case 19: case 20:
               addItemToSpawn(new PotionOfExperience());
               addItemToSpawn(new PotionOfHealing());
               addItemToSpawn(Generator.random(Generator.Category.FOOD));
               addItemToSpawn(Generator.randomWeapon());
               addItemToSpawn(Generator.randomArmor());
               addItemToSpawn(new ScrollOfUpgrade());
               if(Random.Float()<0.4f){
                   addItemToSpawn(new RandomChest());
               }

               //3.0 BossRush T2+等级 补给层新增了3个物品
               //1个随机3阶投掷+ 1个嬗变卷轴 + 1个随机药水
               addItemToSpawn(Generator.random(Generator.Category.MIS_T3));
               addItemToSpawn(new ScrollOfTransmutation());
               addItemToSpawn(Generator.random(Generator.Category.POTION));
          break;
          //T3 补给层
           case 22: case 25:
               addItemToSpawn(new ScrollOfUpgrade());
               addItemToSpawn(new PotionOfStrength());
               addItemToSpawn(new PotionOfHealing());
               addItemToSpawn(Generator.random(Generator.Category.FOOD));
               addItemToSpawn(Generator.randomWeapon());
               break;
          //T3+ 补给层
           case 28: case 30:
               addItemToSpawn(new PotionOfStrength());
               addItemToSpawn(new PotionOfHealing());
               addItemToSpawn(Generator.random(Generator.Category.FOOD));
               addItemToSpawn(Generator.randomWeapon());
               if(Random.Float()<0.2f){
                   addItemToSpawn(new RandomChest());
               }
               addItemToSpawn(Generator.randomArmor());

               /**
                * 3.0 BossRush T3+等级
                * 补给层新增了5个物品
                * 1个随机魔药[水爆/淤泥/冰爆/炼狱/雷鸣]
                * 2个嬗变卷轴 + 1个随机药水 + 1个升级卷轴
                * */
               Item w;
               switch (Random.Int(5)){
                   case 1: w = new AquaBrew();    break;
                   case 2: w = new CausticBrew();    break;
                   case 3: w = new InfernalBrew();   break;
                   case 4: w = new ShockingBrew();   break;
                   default:
                   case 0: w = new BlizzardBrew(); break;
               }
               addItemToSpawn(w);
               addItemToSpawn(new ScrollOfUpgrade());
               addItemToSpawn(new ScrollOfTransmutation());
               addItemToSpawn(new ScrollOfTransmutation());
               addItemToSpawn(Generator.random(Generator.Category.POTION));

               //T4 补给层【从T4开始，成长性质的物品会减少，增加更多对策性的物品】
               //奥术护盾合剂，随机秘卷1-2个，随机药水，随机4阶武器，一个嬗变卷轴
               //2个随机盲盒箱子
               break;
           case 32: case 34: case 36:
           case 38: case 40: case 41:
               addItemToSpawn(new PotionOfShielding());

               switch (Random.Int(5)) {
                   case 1: w = new ScrollOfChallenge();break;
                   case 2: w = new ScrollOfMetamorphosis();break;
                   case 3: w = new ScrollOfAntiMagic();break;
                   case 4: w = new ScrollOfPsionicBlast();break;
                   default:
                          w = new ScrollOfSirensSong();break;
               }
               w.quantity = Random.NormalIntRange(1, 2);
               addItemToSpawn(w);

               addItemToSpawn(new RandomChest());
               addItemToSpawn(new RandomChest());

               addItemToSpawn(Generator.random(Generator.Category.POTION));
               addItemToSpawn(Generator.random(Generator.Category.MIS_T4));
               addItemToSpawn(new ScrollOfTransmutation());
               break;
               //TODO T5 ???
               //TODO 【BossRush4.0 死灵与幽冥之地 内容扩展（3）敬请期待  】
               //TODO 【商人领主&莲娜：我们已经收集足够多的能量，现在让我们平息BR宇宙的灾难吧！ 】
       }
        super.createItems();
    }

    @Override
    public Actor addRespawner() {
        return null;
    }

    @Override
    protected void createMobs() {
        Ghost.Quest.spawnBossRush( this,roomExit );
        if(Statistics.difficultyDLCEXLevel==4){
            super.createMobs();
        }
    }

    @Override
    protected Painter painter() {
        return new CavesPainter()
                .setWater(feeling == Feeling.WATER ? 0.85f : 0.30f, 6)
                .setGrass(feeling == Feeling.GRASS ? 0.65f : 0.15f, 3);
    }

    @Override
    public String tilesTex() {
        if(depth>=40) {
            return Assets.Environment.TILES_HALLS;
        } else if(depth>=31){
            return Assets.Environment.TILES_CITY;
        } else if(depth>=21){
            return Assets.Environment.TILES_COLD;
        } else if(depth>=11){
            return Assets.Environment.TILES_PRISON;
        } else {
            return Assets.Environment.TILES_SEWERS;
        }

    }

    @Override
    public String waterTex() {
        if(depth>=40) {
            return Assets.Environment.WATER_HALLS;
        } else if(depth>=31){
            return Assets.Environment.WATER_CITY;
        } else if(depth>=21){
            return Assets.Environment.WATER_CAVES;
        } else if(depth>=11){
            return Assets.Environment.WATER_PRISON;
        } else {
            return Assets.Environment.WATER_SEWERS;
        }
    }

}