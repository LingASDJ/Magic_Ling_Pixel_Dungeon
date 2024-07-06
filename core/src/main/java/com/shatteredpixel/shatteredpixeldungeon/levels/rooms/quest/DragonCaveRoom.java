package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMBERS;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.FURROWED_GRASS;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL_DECO;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Point;

public class DragonCaveRoom extends SpecialRoom {

    @Override
    public int minWidth() {
        return 13;
    }

    @Override
    public int minHeight() {
        return 13;
    }

    @Override
    public int maxWidth() {
        return 13;
    }

    @Override
    public int maxHeight() {
        return 13;
    }

	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		return false;
	}

    @Override
    public void paint(Level level) {
        Point center = new Point((left + right) / 2, (top + bottom) / 2);
        int DragonPos = (top + 6) * level.width() + left + 6;

        DreamcatcherMaker vis = new DreamcatcherMaker();
        Point c = center();
        vis.pos(c.x - 2, c.y - 2);
        level.customTiles.add(vis);

        level.transitions.add(new LevelTransition(level,
                DragonPos,
                LevelTransition.Type.BRANCH_EXIT,
                5,
                Dungeon.branch+1,
                LevelTransition.Type.BRANCH_ENTRANCE));
        Painter.set(level, DragonPos, Terrain.EXIT);

        Painter.fill( level, this,0, Terrain.WALL );
        drawDreamcatcher(center,level);
    }

    public static class DreamcatcherMaker extends CustomTilemap {

        {
            texture = Assets.Environment.FireQuest;
            tileW = tileH = 5;

        }

        final int TEX_WIDTH = 80;

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            v.map(mapSimpleImage(0, 0, TEX_WIDTH), 5);
            return v;
        }

        @Override
        public String name(int tileX, int tileY) {
            return Messages.get(this, "name");
        }

        @Override
        public String desc(int tileX, int tileY) {
            return Messages.get(this, "desc");
        }
    }

    private void drawDreamcatcher(Point center,Level level) {
        drawWeb(center,level);
        drawCenter(level);
    }



    private void drawWeb(Point center,Level level) {
        Painter.drawCircle(level, center, 6, WALL_DECO);
        Painter.drawCircle(level, center, 5, EMPTY_SP);
        Painter.drawCircle(level, center, 4, WATER);
        Painter.drawCircle(level, center, 3, EMPTY_SP);
        Painter.drawCircle(level, center, 2, EMPTY_SP);
        Painter.drawCircle(level, center, 1, EMBERS);
    }

    private void drawCenter(Level level) {
        for (Door door : connected.values()) {
            door.set( Door.Type.REGULAR );
            if (door.x == left || door.x == right){
                Painter.drawInside(level, this, door, width()/2, FURROWED_GRASS);
            } else {
                Painter.drawInside(level, this, door, height()/2, FURROWED_GRASS);
            }
        }
    }
}

