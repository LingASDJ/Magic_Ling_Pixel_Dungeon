package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Firebomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ShrapnelBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.IncendiaryDart;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Maze;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ExplosiveTrap;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class NukeRoom extends StandardRoom {

    @Override
    public int minWidth() {
        return 18;
    }

    @Override
    public int minHeight() {
        return 18;
    }

    @Override
    public int maxWidth() {
        return 18;
    }

    @Override
    public int maxHeight() {
        return 18;
    }

    @Override
    public boolean canMerge(Level l, Point p, int mergeTerrain) {
        return false;
    }

    @Override
    public void paint(Level level) {

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMBERS);

        //true = space, false = wall
        Maze.allowDiagonals = false;
        boolean[][] maze = Maze.generate(this);
        boolean[] passable = new boolean[width()*height()];

        Painter.fill(level, this, 1, Terrain.EMPTY);
        Painter.fill(level, this, 2, Terrain.HIGH_GRASS);
        for (int x = 0; x < maze.length; x++) {
            for (int y = 0; y < maze[0].length; y++) {
                if (maze[x][y] == Maze.FILLED) {
                    Painter.fill(level, x + left, y + top, 1, 1, Terrain.WALL_DECO);
                }
                passable[x + width()*y] = maze[x][y] == Maze.EMPTY;
            }
        }

        PathFinder.setMapSize(width(), height());
        Point entrance = connected.values().iterator().next();
        int entrancePos = (entrance.x - left) + width()*(entrance.y - top);

        PathFinder.buildDistanceMap( entrancePos, passable );

        for (int i = 0; i < 15; i++) {
            int dropPos;
            do {
                dropPos = level.pointToCell(random());
            } while (level.map[dropPos] != Terrain.HIGH_GRASS || level.heaps.get( dropPos ) != null);
            Item prize = Random.Int(8) >= 4 ? new ShrapnelBomb() : new Firebomb();
            level.drop(prize, dropPos).type = Heap.Type.HEAP;
            level.map[dropPos] = Terrain.TRAP;
            level.setTrap(new ExplosiveTrap(), dropPos);
        }

        for (int i = 0; i < 9; i++) {
            int dropPos;
            do {
                dropPos = level.pointToCell(random());
            } while (level.map[dropPos] != Terrain.HIGH_GRASS || level.heaps.get( dropPos ) != null);
            Item prize = new IncendiaryDart();
            level.drop(prize, dropPos).type = Heap.Type.HEAP;
        }

        for (int i = 0; i < 20; i++) {
            int dropPos;
            do {
                dropPos = level.pointToCell(random());
            } while (level.map[dropPos] != Terrain.HIGH_GRASS || level.heaps.get( dropPos ) != null);
            Painter.set(level,dropPos,Terrain.DOOR);
        }

        int bestDist = 0;
        Point bestDistP = new Point();
        for (int i = 0; i < PathFinder.distance.length; i++){
            if (PathFinder.distance[i] != Integer.MAX_VALUE
                    && PathFinder.distance[i] > bestDist){
                bestDist = PathFinder.distance[i];
                bestDistP.x = (i % width()) + left;
                bestDistP.y = (i / width()) + top;
            }
        }

        Item prize;
        //1 floor set higher in probability, never cursed
        do {
            if (Random.Int(2) == 0) {
                prize = Generator.randomWeapon((Dungeon.depth / 5) + 1, true);
            } else {
                prize = Generator.randomArmor((Dungeon.depth / 5) + 1);
            }
        } while (prize.cursed || Challenges.isItemBlocked(prize));
        prize.cursedKnown = true;

        //33% chance for an extra update.
        if (Random.Int(3) == 0){
            prize.upgrade();
        }

        level.drop(prize, level.pointToCell(bestDistP)).type = Heap.Type.CHEST;

        PathFinder.setMapSize(level.width(), level.height());
    }
}