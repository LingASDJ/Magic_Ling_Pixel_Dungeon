package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.STATUE;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync.FayiNaSayBye;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.ClearCryStal;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EntranceRoom;
import com.watabou.utils.Point;

import java.util.ArrayList;

public class RIPSwordRoom extends SpecialRoom {

    @Override
    public int minWidth() { return 9; }

    @Override
    public int minHeight() { return 9; }

    @Override
    public int maxWidth() {
        return 9;
    }

    @Override
    public int maxHeight() {
        return 9;
    }

    public void paint(Level level){

        Room.Door entrance = entrance();
        entrance.set(Door.Type.HIDDEN);

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.CUSTOM_DECO_EMPTY);
        int centerX = left + width() / 2;
        int centerY = top + height() / 2;
        Painter.drawHorizontalLineWithGaps(level, new Point(centerX-3, centerY-3),3, STATUE);
        Painter.drawHorizontalLineWithGaps(level, new Point(centerX-3, centerY+3),8, STATUE);
        Painter.set(level, new Point(centerX - 2, centerY),STATUE);
        Painter.set(level, new Point(centerX - 2, centerY+2),STATUE);
        Painter.set(level, new Point(centerX, centerY-2),STATUE);
        Painter.set(level, new Point(centerX+1, centerY-1),STATUE);
        Painter.set(level, new Point(centerX+1, centerY+1),STATUE);
        Painter.set(level, new Point(centerX + 3, centerY),STATUE);
        Painter.set(level, new Point(centerX + 3, centerY-3),STATUE);
        ArrayList<Item> items = new ArrayList<>();
        //100% corpse dust, 2x100% 1 coin, 2x30% coins, 1x60% random item, 1x30% armor
        if(SPDSettings.KillDragon()){
            items.add(Generator.random());
        } else {
            items.add(new ClearCryStal());
        }
        items.add(Generator.randomWeapon());
        items.add(Generator.randomArmor());
        items.add(Generator.randomMissile());

        for (Item item : items){
            int pos;
            do {
                pos = level.pointToCell(random());
            } while (level.map[pos] != Terrain.CUSTOM_DECO_EMPTY || level.heaps.get(pos) != null);
            Heap h = level.drop(item, pos);
            if(!SPDSettings.KillDragon()) {
                h.MustCursed();
            }
            h.type = Heap.Type.SKELETON;
        }


        if(RegularLevel.chinaHoliday == RegularLevel.ChinaHoliday.CJ) {
            if (!SPDSettings.FayiNaBerry() && Dungeon.branch == 2 && SPDSettings.KillDragon()) {
                int fpos;
                fpos = (top + 4) * level.width() + left + 4;
                Mob fayiNa = new FayiNaSayBye();
                fayiNa.pos = fpos;
                level.mobs.add(fayiNa);
            }
        }


    }

    @Override
    public boolean canConnect(Room r) {
        if (r instanceof EntranceRoom){
            return false;
        }

        //must have at least 3 rooms between it and the entrance room
        for (Room r1 : r.connected.keySet()) {
            if (r1 instanceof EntranceRoom){
                return false;
            }
            for (Room r2 : r1.connected.keySet()) {
                if (r2 instanceof EntranceRoom){
                    return false;
                }
                for (Room r3 : r2.connected.keySet()) {
                    if (r3 instanceof EntranceRoom){
                        return false;
                    }
                }
            }
        }

        return super.canConnect(r);
    }
}

