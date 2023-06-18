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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Nxhy;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;

import java.util.ArrayList;

public class NxhyShopRoomList extends SpecialRoom {

    private ArrayList<Item> itemsToSpawn;

    @Override
    public int minWidth() {
        return Math.max(10, (int)(Math.sqrt(itemCount())+6));
    }

    @Override
    public int minHeight() {
        return Math.max(10, (int)(Math.sqrt(itemCount())+6));
    }

    public int itemCount(){
        if (itemsToSpawn == null) itemsToSpawn = generateItems();
        return itemsToSpawn.size();
    }

    public void paint( Level level ) {

        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.EMPTY_SP );

        placeShopkeeper( level );

        placeItems( level );

        for (Door door : connected.values()) {
            door.set( Door.Type.REGULAR );
        }

    }

    protected void placeShopkeeper( Level level ) {

        int pos = level.pointToCell(center());

        Mob Nxhy = new Nxhy();
        Nxhy.pos = pos;
        level.mobs.add( Nxhy );

    }

    protected void placeItems( Level level ){

        if (itemsToSpawn == null){
            itemsToSpawn = generateItems();
        }

        Point itemPlacement = new Point(entrance());
        if (itemPlacement.y == top){
            itemPlacement.y++;
        } else if (itemPlacement.y == bottom) {
            itemPlacement.y--;
        } else if (itemPlacement.x == left){
            itemPlacement.x++;
        } else {
            itemPlacement.x--;
        }

        for (Item item : itemsToSpawn) {

            if (itemPlacement.x == left+1 && itemPlacement.y != top+1){
                itemPlacement.y--;
            } else if (itemPlacement.y == top+1 && itemPlacement.x != right-1){
                itemPlacement.x++;
            } else if (itemPlacement.x == right-1 && itemPlacement.y != bottom-1){
                itemPlacement.y++;
            } else {
                itemPlacement.x--;
            }

            int cell = level.pointToCell(itemPlacement);

            if (level.heaps.get( cell ) != null) {
                do {
                    cell = level.pointToCell(random());
                } while (level.heaps.get( cell ) != null || level.findMob( cell ) != null);
            }

            level.drop( item, cell ).type = Heap.Type.FOR_SALE;
        }

    }

    protected static ArrayList<Item> generateItems() {

        ArrayList<Item> itemsToSpawn = new ArrayList<>();

        MeleeWeapon w;
        switch (Dungeon.depth) {
            default:
                w = (MeleeWeapon) Generator.random(Generator.wepTiers[1]);
                itemsToSpawn.add( Generator.randomUsingDefaults( Generator.Category.STONE ) );
                itemsToSpawn.add( Generator.randomUsingDefaults( Generator.Category.STONE ) );
                itemsToSpawn.add( Generator.randomUsingDefaults( Generator.Category.STONE ) );
                itemsToSpawn.add( Generator.randomUsingDefaults( Generator.Category.STONE ) );
                itemsToSpawn.add( Generator.randomUsingDefaults( Generator.Category.STONE ) );
                itemsToSpawn.add( Generator.randomUsingDefaults( Generator.Category.STONE ) );
                itemsToSpawn.add( Generator.randomUsingDefaults( Generator.Category.STONE ) );
                itemsToSpawn.add( Generator.randomUsingDefaults( Generator.Category.STONE ) );
                itemsToSpawn.add( Generator.randomUsingDefaults( Generator.Category.STONE ) );
                itemsToSpawn.add( Generator.randomUsingDefaults( Generator.Category.STONE ) );
                itemsToSpawn.add( Generator.randomUsingDefaults( Generator.Category.STONE ) );
                itemsToSpawn.add( Generator.randomUsingDefaults( Generator.Category.STONE ) );
                itemsToSpawn.add( Generator.randomUsingDefaults( Generator.Category.STONE ) );
                itemsToSpawn.add( Generator.randomUsingDefaults( Generator.Category.STONE ) );
                itemsToSpawn.add( Generator.randomUsingDefaults( Generator.Category.STONE ) );
                itemsToSpawn.add( Generator.randomUsingDefaults( Generator.Category.STONE ) );
                break;
        }
        w.enchant(null);
        w.cursed = false;
        w.level(0);
        w.identify();
        itemsToSpawn.add(w);
        itemsToSpawn.add( new PotionOfLevitation() );
        itemsToSpawn.add( new PotionOfLevitation() );
        itemsToSpawn.add( new PotionOfLevitation() );
        itemsToSpawn.add( new PotionOfLevitation() );
        itemsToSpawn.add( new PotionOfLevitation() );
        itemsToSpawn.add( new PotionOfLevitation() );
        itemsToSpawn.add( new PotionOfLevitation() );
        itemsToSpawn.add( new PotionOfLevitation() );
        itemsToSpawn.add( new PotionOfLevitation() );
        itemsToSpawn.add( new PotionOfLevitation() );
        itemsToSpawn.add( new PotionOfLevitation() );
        return itemsToSpawn;
    }

}
