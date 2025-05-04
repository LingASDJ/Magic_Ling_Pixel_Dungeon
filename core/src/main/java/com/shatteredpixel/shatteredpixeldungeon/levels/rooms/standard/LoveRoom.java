package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.PinkGhostNPC;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.watabou.utils.Point;

public class LoveRoom extends SpecialRoom {

    @Override
    public int minWidth() {
        return 17;
    }

    @Override
    public int minHeight() {
        return 17;
    }

    @Override
    public int maxWidth() {
        return 17;
    }

    @Override
    public int maxHeight() {
        return 17;
    }

    @Override
    public boolean canPlaceTrap(Point p) {
        return false;
    }

    private static final int[] pre_map = {
            144,144,144,4,144,144,144,144,144,144,144,4,144,144,144,
            144,144,4,0,4,144,144,144,144,144,4,0,4,144,144,
            144,4,0,0,0,4,144,144,144,4,0,0,0,4,144,
            4,0,0,0,0,0,4,144,4,0,0,0,0,0,4,
            4,0,0,0,0,0,0,4,0,0,0,0,0,0,4,
            4,0,0,0,0,0,0,0,0,0,0,0,0,0,4,
            4,0,0,0,0,0,0,0,0,0,0,0,0,0,4,
            4,0,0,0,0,0,0,0,0,0,0,0,0,0,4,
            144,4,0,0,0,0,0,0,0,0,0,0,0,4,144,
            144,144,4,0,0,0,0,0,0,0,0,0,4,144,144,
            144,144,144,4,0,0,0,0,0,0,0,4,144,144,144,
            144,144,144,144,4,0,0,0,0,0,4,144,144,144,144,
            144,144,144,144,144,4,0,0,0,4,144,144,144,144,144,
            144,144,144,144,144,144,4,0,4,144,144,144,144,144,144,
            144,144,144,144,144,144,144,4,144,144,144,144,144,144,144,
    };

    private int codeToTerrain(int code){
        switch (code){
            case 0:
                return Terrain.WATER;
            case 4:
                return Terrain.EMPTY_SP;
            default:
                return Terrain.EMPTY_DECO;
        }
    }

    @Override
    public void paint(Level level) {

        Painter.fill(level, this, 0, WALL);

        for (int i = left + 1; i <= right - 1; i++) {
            for (int j = top + 1; j <= bottom - 1; j++) {
                int dx = i - (left + 1);
                int dy = j - (top + 1);
                int index = dy * (minWidth() - 2) + dx;

                if (index >= 0 && index < pre_map.length) {
                    set(level, i, j, codeToTerrain(pre_map[index]));
                } else {
                    set(level, i, j, Terrain.EMPTY);
                }
            }
        }
        int MiddlePos = (top + 8) * level.width() + left + 8;

        Mob n = new PinkGhostNPC();
        n.pos = MiddlePos;
        level.mobs.add(n);
    }

    private void set(Level level, int x, int y, int value) {
        level.map[x + y * level.width()] = value;
    }
}
