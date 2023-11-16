package com.shatteredpixel.shatteredpixeldungeon.levels.painters;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class AncityPainter extends RegularPainter {
    @Override
    protected void decorate(Level level, ArrayList<Room> rooms) {
        int[] map = level.map;
        int w = level.width();
        int l = level.length();

        for (int i = 0; i < w; i++) {
            if (map[i] == Terrain.WALL && map[i + w] == Terrain.WATER && Random.Int(4) == 0) {
                map[i] = Terrain.WALL_DECO;
            }
        }

        for (int i=w + 1; i < l - w - 1; i++) {
            if (map[i] == Terrain.EMPTY) {

                int count = 0;
                for (int j = 0; j < PathFinder.NEIGHBOURS9.length; j++) {
                    if ((Terrain.flags[map[i + PathFinder.NEIGHBOURS9[j]]] & Terrain.PASSABLE) > 0) {
                        count++;
                    }
                }

                if (Random.Int( 80 ) < count) {
                    map[i] = Terrain.EMPTY_SP;
                }

            } else
            if (map[i] == Terrain.WALL &&
                    map[i-1] != Terrain.WALL_DECO && map[i-w] != Terrain.WALL_DECO &&
                    Random.Int( 20 ) == 0) {

                map[i] = Terrain.WALL_DECO;

            }
        }

        for (Room r : rooms) {
            for (Room n : r.neigbours) {
                if (!r.connected.containsKey( n )) {
                    mergeRooms(level, r, n, null, Terrain.CHASM);
                }
            }
        }

    }
}
