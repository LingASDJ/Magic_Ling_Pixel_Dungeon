package com.shatteredpixel.shatteredpixeldungeon.levels.nosync;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CRYSTAL_DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ENTRANCE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EXIT;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.LOCKED_DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.PEDESTAL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class ForestHardBossLevel extends Level {

    public String tilesTex() {
        return Assets.Environment.TILES_SEWERS;
    }

    public String waterTex() {
        return Assets.Environment.WATER_SEWERS;
    }

    {
        color1 = 0x801500;
        color2 = 0xa68521;
    }

    @Override
    public void occupyCell(Char ch) {
        super.occupyCell(ch);
        GLog.p(String.valueOf(hero.pos));
    }

    private static final int WIDTH = 33;
    private static final int HEIGHT = 32;

    private static final int S = CHASM;
    private static final int L = WALL;
    private static final int D = EMPTY_SP;
    private static final int W = WATER;
    private static final int X = LOCKED_DOOR;
    private static final int Z = EXIT;
    private static final int Q = ENTRANCE;
    private static final int C = CRYSTAL_DOOR;
    private static final int N = PEDESTAL;
    private static final int M = WATER;

    private static final int J = EMPTY;

    private static final int[] bossMap = {
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,L,L,L,L,L,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,L,L,L,L,L,L,L,L,L,L,S,S,L,J,J,J,L,S,S,L,L,L,L,L,L,L,L,L,L,S,S,
            S,L,L,M,J,J,J,J,J,J,J,L,L,L,J,J,Q,J,J,L,L,L,J,J,J,J,J,J,J,M,L,L,S,
            S,L,S,M,M,J,J,J,J,J,J,J,L,J,J,J,J,J,J,J,L,J,J,J,J,J,J,J,M,M,S,L,S,
            S,L,S,S,M,J,J,J,J,J,J,J,C,J,J,J,J,J,J,J,C,J,J,J,J,J,J,J,M,S,S,L,S,
            S,L,S,S,M,J,J,J,J,J,J,J,L,J,J,L,M,L,J,J,L,J,J,J,J,J,J,J,M,S,S,L,S,
            S,L,S,S,M,M,J,J,J,J,J,J,L,L,J,L,M,L,J,L,L,J,J,J,J,J,J,M,M,S,S,L,S,
            S,L,S,S,M,J,J,J,L,L,L,L,L,L,L,L,L,L,L,L,L,L,L,L,L,J,J,J,M,S,S,L,S,
            L,L,S,L,L,L,L,L,L,W,W,W,W,W,W,W,D,W,W,W,W,W,W,W,L,L,L,L,L,L,S,L,S,
            L,L,L,L,W,W,W,W,S,S,S,S,S,S,S,D,D,D,S,S,S,S,S,S,S,W,W,W,W,L,L,L,L,
            L,L,D,W,W,S,S,S,S,D,D,D,D,D,D,D,W,D,D,D,D,D,D,D,S,S,S,S,W,W,D,L,L,
            L,D,W,S,S,S,D,D,D,D,S,S,D,S,S,D,D,D,S,S,D,S,S,D,D,D,D,S,S,W,W,D,L,
            L,W,W,S,D,D,D,S,D,S,S,S,S,S,S,S,D,S,S,S,S,S,S,S,D,S,D,D,D,S,W,W,L,
            L,W,S,D,D,D,W,S,D,D,S,S,S,S,D,D,D,D,D,S,S,S,S,D,D,S,W,D,D,D,S,W,L,
            L,W,W,S,D,S,W,W,W,D,S,S,D,D,D,S,D,S,D,D,D,S,S,D,W,W,W,S,D,S,W,W,L,
            L,W,S,S,D,W,W,W,D,D,D,D,D,S,S,S,D,S,S,S,D,D,D,D,D,W,W,W,D,S,S,W,L,
            L,S,D,D,D,D,W,S,W,D,S,S,D,D,S,D,D,D,S,D,D,S,S,D,W,S,W,D,D,D,D,S,L,
            L,D,D,W,W,D,D,D,D,D,S,S,S,D,D,D,N,D,D,D,S,S,S,D,D,D,D,D,W,W,D,D,L,
            L,S,D,W,D,D,W,W,W,D,S,S,D,D,S,D,D,D,S,D,D,S,S,D,W,W,W,D,D,W,D,S,L,
            L,S,W,W,D,W,W,W,D,D,D,D,D,S,S,S,D,S,S,S,D,D,D,D,D,W,W,W,D,W,W,S,L,
            L,S,W,W,D,W,W,W,W,D,S,S,D,S,S,S,D,S,S,S,D,S,S,D,W,W,W,W,D,W,W,S,L,
            L,S,W,D,D,D,D,W,D,D,S,S,D,S,D,D,D,D,D,S,D,S,S,D,D,W,D,D,D,D,W,S,L,
            L,S,W,W,D,W,D,W,D,W,S,D,D,D,D,S,D,S,D,D,D,D,S,W,D,W,D,W,D,W,W,S,L,
            L,S,W,W,S,W,D,D,D,W,S,W,D,W,W,S,D,S,W,W,D,W,S,W,D,D,D,W,S,W,W,S,L,
            L,S,W,W,S,W,W,W,W,W,S,W,W,W,W,S,D,S,W,W,W,W,S,W,W,W,W,W,S,W,W,S,L,
            L,L,W,W,S,W,W,W,W,W,S,W,W,W,W,S,D,S,W,W,W,W,S,W,W,W,W,W,S,W,W,L,L,
            S,L,L,W,S,W,W,W,L,L,L,L,L,L,L,L,X,L,L,L,L,L,L,L,L,W,W,W,S,W,L,L,S,
            S,S,L,L,L,L,L,L,L,J,J,J,J,J,L,J,J,J,L,J,J,J,J,J,L,L,L,L,L,L,S,S,S,
            S,S,S,S,S,S,S,S,L,J,J,J,J,J,X,J,J,J,X,J,J,J,J,J,L,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,L,J,J,J,J,J,L,J,Z,J,L,J,J,J,J,J,L,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,L,J,J,L,L,L,L,J,J,J,L,L,L,L,J,J,L,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,L,L,L,L,S,S,L,L,L,L,L,S,S,L,L,L,L,S,S,S,S,S,S,S,S,
    };

    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = bossMap.clone();

        int entranceCell = 82;
        int exitCell = 973;

        LevelTransition enter = new LevelTransition(this, entranceCell, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(enter);

        LevelTransition exit = new LevelTransition(this, exitCell, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exit);

        return true;
    }

    @Override
    protected void createMobs() {

    }

    @Override
    protected void createItems() {

    }
}
