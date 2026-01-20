package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;

public class SkyDeadWellRoom extends SpecialRoom {
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
    public boolean canConnect(Point p) {
        if (!super.canConnect(p)){
            return false;
        }
        if (Math.abs(p.x - (right - (width()-1)/4f)) < 1f){
            return true;
        }
        return Math.abs(p.y - (bottom - (height() - 1) / 4f)) < 1f;
    }

    @Override
    public boolean canPlaceTrap(Point p) {
        return false;
    }

    private static final int[] pre_map = {
            0,0,0,0,0,0,0,0,0,0,0,
            0,92,92,0,92,20,92,0,92,92,0,
            0,92,0,4,24,24,24,4,0,92,0,
            0,92,4,24,24,24,24,24,4,92,0,
            0,0,4,24,4,24,4,24,4,0,0,
            0,92,4,24,24,19,24,24,4,92,0,
            0,92,0,4,24,24,24,4,0,92,0,
            0,0,0,4,24,4,24,4,0,0,0,
            0,92,0,0,4,4,4,0,0,92,0,
            0,92,92,0,92,0,92,0,92,92,0,
            0,0,0,0,0,0,0,0,0,0,0,
    };

    private int codeToTerrain(int code){
        switch (code){
            case 5:
                return Terrain.WATER;
            case 20:
                return Terrain.PEDESTAL;
            case 19:
                return Terrain.WELL;
            case 92:
                return Terrain.BOOKSHELF;
            case 4:
                return Terrain.EMPTY_SP;
            case 24:
                return Terrain.CHASM;
            default:
                return Terrain.EMPTY;
        }
    }

    @Override
    public void paint(Level level) {

        Painter.fill(level,this, 0, WALL);

        for (int i = left + 1; i <= right-1; i++) {
            for (int j = top + 1; j <= bottom-1; j++) {
                int dx = i - (left + 1);
                int dy = j - (top + 1);
                int index = dy * (minWidth()-2) + dx;

                if(index >= 0 && index < pre_map.length){
                    set(level, i, j, codeToTerrain(pre_map[index]));
                } else {
                    set(level, i, j, Terrain.EMPTY);
                }
            }
        }

        entrance().set(Door.Type.REGULAR);

        int centerX = left + width() / 2;
        int centerY = top + height() /2;
        Point xpos = new Point(centerX, centerY-4);
        int RPos = left + right - xpos.x + xpos.y * level.width();

        level.drop( new PotionOfLevitation(), RPos );

        Point Cpos = new Point(centerX, centerY);
        int entrancePos = left + right - Cpos.x + Cpos.y * level.width();
        level.transitions.add(new LevelTransition(level,
                entrancePos,
                LevelTransition.Type.DOUBLE_ENTRANCE,
                Dungeon.depth,
                Dungeon.branch + 2,
                LevelTransition.Type.BRANCH_ENTRANCE));
    }

    private void set(Level level, int x, int y, int value) {
        level.map[x + y * level.width()] = value;
    }
}

