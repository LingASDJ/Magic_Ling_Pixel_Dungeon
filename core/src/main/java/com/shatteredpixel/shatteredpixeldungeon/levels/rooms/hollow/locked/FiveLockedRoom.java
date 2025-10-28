package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.locked;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MobSpawner;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.hollow.AllSearchIQuest;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.CustomLuaRoom;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class FiveLockedRoom extends CustomLuaRoom {
    {
        width = 11;
        height = 11;
        map_lua_file = Assets.Map_Luas.LockedSevenRoom_MapLua;
    }

    private Item HighChestRules() {
        Item item;

        ArrayList<Item> highValueItems = new ArrayList<>();
        ArrayList<Item> midValueItems = new ArrayList<>();
        ArrayList<Item> lowValueItems = new ArrayList<>();

        highValueItems.add(new AllSearchIQuest.HollowLantern());
        highValueItems.add(new AllSearchIQuest.HollowCityProps());
        highValueItems.add(new AllSearchIQuest.HollowGoldCards());

        midValueItems.add(new AllSearchIQuest.CrystalHeartChoco());
        midValueItems.add(new AllSearchIQuest.CreateWorldHeartModel());
        midValueItems.add(new AllSearchIQuest.GhostBlueModel());
        midValueItems.add(new AllSearchIQuest.GreenDamModel());
        midValueItems.add(new AllSearchIQuest.GreenStingModel());

        lowValueItems.add(new AllSearchIQuest.THEATER_CARDS());
        lowValueItems.add(new AllSearchIQuest.HOLLOW_SUGARS());
        lowValueItems.add(new AllSearchIQuest.GREEN_PRISM());
        lowValueItems.add(new AllSearchIQuest.GNOLL_WOOD());
        lowValueItems.add(new AllSearchIQuest.FOUR_KIDS());

        float randomValue = Random.Float();

        if (randomValue > 0.99f) {
            Random.shuffle(highValueItems);
            item = highValueItems.get(Random.index(highValueItems));
        } else if (randomValue > 0.75f) {
            Random.shuffle(midValueItems);
            item = midValueItems.get(Random.index(midValueItems));
        } else {
            Random.shuffle(lowValueItems);
            item = lowValueItems.get(Random.index(lowValueItems));
        }

        return item;
    }

    @Override
    public void paint(Level level) {
        super.paint(level);

        int c = (top + 3) * level.width() + left + 5;
        level.drop(HighChestRules(), c).type = Heap.Type.CHEST;

        int is = (top + 5) * level.width() + left + 7;
        Mob mob = Reflection.newInstance(MobSpawner.getMobRotation(31).get(0));
        mob.pos = is;
        mob.state = mob.WANDERING;
        level.mobs.add(mob);

        level.addItemToSpawn( new IronKey( Dungeon.depth ) );
    }
}
