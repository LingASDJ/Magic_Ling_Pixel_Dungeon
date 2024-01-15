package com.shatteredpixel.shatteredpixeldungeon.levels.painters;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.HollowMimic;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class HollowPainter extends RegularPainter {

    @Override
    protected void decorate(Level level, ArrayList<Room> rooms) {

        int w = level.width();
        int l = level.length();
        int[] map = level.map;

        for (int i=w + 1; i < l - w - 1; i++) {
            if (map[i] == Terrain.EMPTY) {

                float c = 0.05f;
                if (map[i + 1] == Terrain.WALL && map[i + w] == Terrain.WALL) {
                    c += 0.2f;
                }
                if (map[i - 1] == Terrain.WALL && map[i + w] == Terrain.WALL) {
                    c += 0.2f;
                }
                if (map[i + 1] == Terrain.WALL && map[i - w] == Terrain.WALL) {
                    c += 0.2f;
                }
                if (map[i - 1] == Terrain.WALL && map[i - w] == Terrain.WALL) {
                    c += 0.2f;
                }

                if (Random.Float() < c) {
                    map[i] = Terrain.EMPTY_DECO;
                }
            }
        }

        for (int i=0; i < w; i++) {
            if (map[i] == Terrain.WALL &&
                    (map[i + w] == Terrain.EMPTY || map[i + w] == Terrain.EMPTY_SP) &&
                    Random.Int( 6 ) == 0) {
                map[i] = Terrain.WALL_DECO;
            }
        }

        for (int i = w; i < l - w; i++) {
            if (map[i] == Terrain.WALL && map[i - w] == Terrain.WALL && map[i + w] == Terrain.WATER && Random.Int(2) == 0) {
                map[i] = Terrain.WALL_DECO;
            }
        }

        int mimicCount = 0; // 记录已生成的拟态怪物数量

        for (int i = w; i < l - w; i++) {
            if (map[i] == Terrain.DOOR) {

                if (mimicCount >= 1) {
                    continue; // 跳过生成拟态怪物的步骤
                }

                level.mobs.add(HollowMimic.spawnAt(i, Generator.random(Generator.Category.ARMOR), HollowMimic.class));
                map[i] = Terrain.EMPTY;
                mimicCount++; // 增加拟态怪物数量的计数器
            }
        }

    }
}

