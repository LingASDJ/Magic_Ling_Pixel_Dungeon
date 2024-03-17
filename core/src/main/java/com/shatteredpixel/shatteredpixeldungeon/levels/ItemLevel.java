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
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.RandomChest;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.CavesPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Random;

public class ItemLevel extends RegularLevel {

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
    protected void createItems() {
        Ghost.Quest.spawnBossRush( this );
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
           case 14: case 12: case 15:
               addItemToSpawn(new PotionOfExperience());
               addItemToSpawn(new PotionOfHealing());
               addItemToSpawn(Generator.random(Generator.Category.FOOD));
               addItemToSpawn(Generator.randomWeapon());
               addItemToSpawn(Generator.randomArmor());
               if(Random.Float()<0.4f){
                   addItemToSpawn(new RandomChest());
               }
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
          break;
          //T3 补给层
           case 22: case 25:
               addItemToSpawn(new ScrollOfUpgrade());
               addItemToSpawn(new PotionOfStrength());
               addItemToSpawn(new PotionOfHealing());
               addItemToSpawn(Generator.random(Generator.Category.FOOD));
               addItemToSpawn(Generator.randomWeapon());
           case 28: case 30:
               addItemToSpawn(new PotionOfStrength());
               addItemToSpawn(new PotionOfHealing());
               addItemToSpawn(Generator.random(Generator.Category.FOOD));
               addItemToSpawn(Generator.randomWeapon());
               if(Random.Float()<0.2f){
                   addItemToSpawn(new RandomChest());
               }
               addItemToSpawn(Generator.randomArmor());
               break;
       }
        super.createItems();
    }

    @Override
    public Actor addRespawner() {
        return null;
    }

    @Override
    protected void createMobs() {
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
        if(depth>=16){
            return Assets.Environment.TILES_CITY;
        } else if(depth>=6){
            return Assets.Environment.TILES_PRISON;
        } else {
            return Assets.Environment.TILES_COLD;
        }

    }

    @Override
    public String waterTex() {
        if(depth>=16){
            return Assets.Environment.WATER_CITY;
        } else if(depth>=6){
            return Assets.Environment.WATER_PRISON;
        } else {
            return Assets.Environment.WATER_CAVES;
        }
    }

}