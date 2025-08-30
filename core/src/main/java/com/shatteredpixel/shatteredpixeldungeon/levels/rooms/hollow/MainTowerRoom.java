package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_DECO;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.watabou.utils.Point;

public class MainTowerRoom extends SpecialRoom {

    @Override
    public int minWidth() {
        return 9;
    }
    @Override
    public int minHeight() {
        return 9;
    }
    @Override
    public int maxWidth() {
        return 9;
    }
    @Override
    public int maxHeight() {
        return 9;
    }

    private static final int[] pre_map = {
            1, 1, 5, 5, 5, 1, 1,
            1, 49, 49, 5, 49, 49, 1,
            5, 49, 74, 1, 74, 49, 5,
            5, 5, 1, 21, 1, 5, 5,
            5, 49, 11, 1, 11, 49, 5,
            1, 49, 74, 5, 74, 49, 1,
            1, 1, 5, 5, 5, 1, 1
    };

    private int codeToTerrain(int code){
        switch (code){
            case 50:
                return Terrain.WALL_DECO;
            case 74:
                return Terrain.STATUE_SP;
            case 5:
                return Terrain.EMPTY_SP;
            case 49:
                return Terrain.WALL;
            case 25:
                return Terrain.CHASM;
            case 21:
                return Terrain.PEDESTAL;
            default:
                return EMPTY_DECO;
        }
    }

    private void set(Level level, int x, int y, int value) {
        level.map[x + y * level.width()] = value;
    }

    @Override
    public boolean canPlaceTrap(Point p) {
        return false;
    }

    @Override
    public void paint(Level level) {
        Painter.fill(level,this, 0, WALL);

        for (int i = left + 1; i <= right-1; i++) {
            for (int j = top + 1; j <= bottom-1; j++) {
                int dx = i - (left + 1);
                int dy = j - (top + 1);
                int index = dy * (minWidth()-2) + dx;
                if (index >= 0 && index < pre_map.length) {
                    set(level, i, j, codeToTerrain(pre_map[index]));
                }
            }
        }
        entrance().set(Door.Type.REGULAR);
    }
}

