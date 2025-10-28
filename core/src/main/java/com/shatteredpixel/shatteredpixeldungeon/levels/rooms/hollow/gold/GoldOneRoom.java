package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.gold;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.hollow.AllSearchIQuest;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.CustomLuaRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.utils.MobsUtilsRoom;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class GoldOneRoom extends CustomLuaRoom {
    private ArrayList<Integer> perimeterCells;

    {
        width = 11;
        height = 11;
        map_lua_file = Assets.Map_Luas.GoldOneRoom_MapLua;
    }

    @Override
    public void paint(Level level) {
        super.paint(level);

        int c = (top + 3) * level.width() + left + 5;
        level.drop(HighChestRules(), c).type = Heap.Type.GREEN_CHSET;

        perimeterCells = new ArrayList<>();
        for (int x = left + 1; x < right - 1; x++) {
            perimeterCells.add(level.pointToCell(new Point(x, top + 1)));
            perimeterCells.add(level.pointToCell(new Point(x, bottom - 1)));
        }
        for (int y = top + 2; y < bottom - 1; y++) {
            perimeterCells.add(level.pointToCell(new Point(left + 1, y)));
            perimeterCells.add(level.pointToCell(new Point(right - 1, y)));
        }

        Mob guard;
        switch (Random.Int(3)) {
            case 1:
                guard = new MobsUtilsRoom.B_Guard();
                ((MobsUtilsRoom.B_Guard) guard).setPatrolPath(new ArrayList<>(perimeterCells));
                guard.pos = perimeterCells.get(Random.index(perimeterCells));
                level.mobs.add(guard);
                break;
            case 2:
                guard = new MobsUtilsRoom.C_Guard();
                ((MobsUtilsRoom.C_Guard) guard).setPatrolPath(new ArrayList<>(perimeterCells));
                guard.pos = perimeterCells.get(Random.index(perimeterCells));
                level.mobs.add(guard);
                break;
            default:
                guard = new MobsUtilsRoom.A_Guard();
                ((MobsUtilsRoom.A_Guard) guard).setPatrolPath(new ArrayList<>(perimeterCells));
                guard.pos = perimeterCells.get(Random.index(perimeterCells));
                level.mobs.add(guard);
                break;
        }

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

        if (randomValue > 0.75f) {
            Random.shuffle(highValueItems);
            item = highValueItems.get(Random.index(highValueItems));
        } else if (randomValue > 0.40f) {
            Random.shuffle(midValueItems);
            item = midValueItems.get(Random.index(midValueItems));
        } else {
            Random.shuffle(lowValueItems);
            item = lowValueItems.get(Random.index(lowValueItems));
        }

        return item;
    }

}
