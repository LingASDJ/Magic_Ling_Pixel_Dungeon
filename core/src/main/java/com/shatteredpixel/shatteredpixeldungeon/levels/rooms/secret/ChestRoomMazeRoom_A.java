package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CrystalMimic;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MIME;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Maze;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;

public class ChestRoomMazeRoom_A extends SecretRoom {

    @Override
    public int minWidth() {
        return 19;
    }

    @Override
    public int minHeight() {
        return 19;
    }

    @Override
    public int maxWidth() {
        return 19;
    }

    @Override
    public int maxHeight() {
        return 19;
    }

    @Override
    public void paint(Level level) {
        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY);

        //true = space, false = wall
        Maze.allowDiagonals = false;
        boolean[][] maze = Maze.generate(this);
        boolean[] passable = new boolean[width()*height()];

        Painter.fill(level, this, 1, Terrain.EMPTY);
        for (int x = 0; x < maze.length; x++) {
            for (int y = 0; y < maze[0].length; y++) {
                if (maze[x][y] == Maze.FILLED) {
                    Painter.fill(level, x + left, y + top, 1, 1, Terrain.WALL);
                }
                passable[x + width()*y] = maze[x][y] == Maze.EMPTY;
            }
        }

        PathFinder.setMapSize(width(), height());
        Point entrance = entrance();
        int entrancePos = (entrance.x - left) + width()*(entrance.y - top);

        PathFinder.buildDistanceMap( entrancePos, passable );

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
        //1 floor set higher in probability, never cursed
        prize = Statistics.RandomMimicItem ? new MIME.GOLD_THREE() : new Food();
        prize.cursed = false;
        prize.cursedKnown = true;

        if(Statistics.fuckGeneratorAlone==1) {
            level.mobs.add(CrystalMimic.spawnAt( level.pointToCell(bestDistP), CrystalMimic.class,( prize )));
        }

        PathFinder.setMapSize(level.width(), level.height());

        entrance().set(Door.Type.REGULAR);
    }
}

