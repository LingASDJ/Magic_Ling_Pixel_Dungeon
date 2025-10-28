package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.guard;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.CustomLuaRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.utils.MobsUtilsRoom;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class GuardTwoRoom extends CustomLuaRoom {
    {
        width = 13;
        height = 13;
        map_lua_file = Assets.Map_Luas.GuardTwoRoom_MapLua;
    }

    private ArrayList<Integer> perimeterCells;
    @Override
    public void paint(Level level) {
        super.paint(level);

        perimeterCells = new ArrayList<>();
        for (int x = left + 1; x < right - 1; x++) {
            perimeterCells.add(level.pointToCell(new Point(x, top + 1)));
            perimeterCells.add(level.pointToCell(new Point(x, bottom - 1)));
        }
        for (int y = top + 2; y < bottom - 1; y++) {
            perimeterCells.add(level.pointToCell(new Point(left + 1, y)));
            perimeterCells.add(level.pointToCell(new Point(right - 1, y)));
        }

        int guardCount = Random.Int(2) + 1;

        for (int i = 0; i < guardCount; i++) {
            Mob guard = createRandomGuard();
            if (guard != null) {
                int position;
                do {
                    position = perimeterCells.get(Random.index(perimeterCells));
                } while (isPositionOccupied(level, position));

                guard.pos = position;
                try {
                    Method setPatrolPath = guard.getClass().getMethod("setPatrolPath", ArrayList.class);
                    setPatrolPath.invoke(guard, new ArrayList<>(perimeterCells));
                } catch (Exception e) {
                    System.err.println("Failed to set patrol path for guard: " + e.getMessage());
                }
                level.mobs.add(guard);
            }
        }
    }

    private Mob createRandomGuard() {
        int guardType = Random.Int(6);

        switch (guardType) {
            case 0:
                return new MobsUtilsRoom.Red_A();
            case 1:
                return new MobsUtilsRoom.Red_B();
            case 2:
                return new MobsUtilsRoom.Red_C();
            case 3:
                return new MobsUtilsRoom.Red_D();
            case 4:
                return new MobsUtilsRoom.Red_E();
            case 5:
                return new MobsUtilsRoom.Red_F();
            default:
                return null;
        }
    }

    private boolean isPositionOccupied(Level level, int position) {
        for (Mob mob : level.mobs) {
            if (mob.pos == position) {
                return true;
            }
        }
        return false;
    }

}
