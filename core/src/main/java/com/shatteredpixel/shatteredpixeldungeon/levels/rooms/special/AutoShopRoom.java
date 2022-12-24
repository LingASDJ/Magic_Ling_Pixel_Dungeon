package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.AutoShopReBot;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Maze;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;

public class AutoShopRoom extends SpecialRoom {

  public AutoShopRoom() {}

  public int minWidth() {
    return Math.max(8, (int) (3.0d));
  }

  public int minHeight() {
    return Math.max(8, (int) (3.0d));
  }

  public void paint(Level level) {
    Painter.fill(level, this, 4);
    Painter.fill(level, this, 1, 14);
    placeShopkeeper(level);
    boolean[] passable = new boolean[width() * height()];
    boolean[][] maze = Maze.generate(this);
    for (int x = 0; x < maze.length; x++) {
      for (int y = 0; y < maze[0].length; y++) {
        if (maze[x][y] == Maze.FILLED) {
          Painter.fill(level, x + left, y + top, 1, 1, Terrain.CHASM);
        }
        passable[x + width() * y] = maze[x][y] == Maze.EMPTY;
      }
    }
    PathFinder.setMapSize(width(), height());
    Point entrance = entrance();
    int entrancePos = (entrance.x - left) + width() * (entrance.y - top);
    PathFinder.buildDistanceMap(entrancePos, passable);
    for (Room.Door door : this.connected.values()) {
      door.set(Room.Door.Type.REGULAR);
    }
  }

  protected void placeShopkeeper(Level level) {
    int pos = level.pointToCell(center());
    Mob autoShopReBot = new AutoShopReBot();
    autoShopReBot.pos = pos;
    level.mobs.add(autoShopReBot);
  }
}
