package com.shatteredpixel.shatteredpixeldungeon.levels.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_DECO;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.PEDESTAL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.SIGN;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow.MorphsNPC;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;

public class MorpheusBossLevel extends Level {

    {
        color1 = 0x801500;
        color2 = 0xa68521;
        viewDistance = 16;
    }

    private static final int WIDTH = 25;
    private static final int HEIGHT = 25;

    private static final int S = SIGN;
    private static final int W = WALL;
    private static final int X = WATER;
    private static final int Q = EMPTY_SP;
    private static final int U = PEDESTAL;
    private static final int T = EMPTY_SP;

    private static final int M = WATER;
    private static final int J = EMPTY_DECO;
    private static final int B = PEDESTAL;
    private static final int H = WATER;
    private static final int Y = PEDESTAL;

    private static final int[] code_map = {
            S,S,S,S,S,S,S,S,W,W,W,W,W,W,W,W,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,W,W,W,W,X,T,X,X,X,X,W,W,W,W,S,S,S,S,S,S,
            S,S,S,S,W,W,X,X,J,X,X,X,X,X,J,X,X,X,X,W,S,S,S,S,S,
            S,S,S,S,W,X,X,X,T,X,X,X,X,J,T,X,X,X,J,W,W,S,S,S,S,
            S,S,W,W,W,X,B,X,X,J,T,X,X,X,J,X,T,M,M,X,W,W,W,S,S,
            S,S,W,X,J,X,X,J,X,X,X,X,X,X,X,X,J,X,Y,X,X,M,W,W,S,
            S,S,W,X,X,B,J,T,X,M,X,X,T,X,T,X,X,X,X,U,M,Y,X,W,S,
            S,W,W,X,X,M,X,X,X,X,X,X,J,X,X,X,M,J,X,X,X,X,X,W,S,
            S,W,X,X,X,X,X,X,B,X,X,X,X,J,T,X,Y,X,X,J,M,X,X,W,W,
            W,W,H,X,J,H,X,J,X,X,X,X,X,X,M,M,X,U,X,X,M,X,X,X,W,
            W,X,M,X,X,M,X,T,J,X,X,X,X,X,X,Y,X,X,Y,X,X,X,M,X,W,
            W,X,X,X,X,X,X,X,X,X,X,X,X,X,X,X,X,X,M,X,X,X,Y,X,W,
            W,M,X,X,X,X,X,H,M,X,X,X,X,X,X,X,X,X,X,X,X,X,X,X,W,
            W,H,X,X,X,X,M,X,X,X,X,X,X,X,X,Y,X,Y,X,X,X,X,X,X,W,
            W,X,M,H,M,X,H,M,M,X,X,X,X,X,Q,M,X,X,M,X,X,Y,X,X,W,
            W,W,X,X,X,X,X,X,M,X,X,Q,X,X,X,X,X,X,Q,X,X,M,X,X,W,
            S,W,X,M,X,X,X,X,Q,X,Q,U,X,X,X,M,X,X,M,X,X,X,X,W,W,
            S,W,X,X,M,M,H,X,X,X,X,X,X,X,X,X,Q,X,X,X,Y,M,X,W,S,
            S,W,X,X,M,X,X,X,X,X,X,X,X,X,U,X,X,X,Q,X,M,Y,X,W,S,
            S,W,W,X,X,X,U,Q,X,Q,U,X,X,X,Q,X,X,Q,X,M,X,X,W,W,S,
            S,S,W,W,X,X,Q,X,X,X,Q,X,X,X,X,X,X,U,X,X,X,W,W,S,S,
            S,S,S,W,W,W,X,X,X,Q,X,X,X,Q,U,X,Q,X,X,X,X,W,S,S,S,
            S,S,S,S,S,W,W,W,X,X,X,X,U,X,Q,X,X,X,W,W,W,W,S,S,S,
            S,S,S,S,S,S,S,W,W,W,X,X,Q,X,X,W,W,W,W,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,W,W,W,W,W,W,W,S,S,S,S,S,S,S,S,S,
    };

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();

        int entrance = 563;
        int exit = 0;

        LevelTransition enter = new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(enter);

        LevelTransition exits = new LevelTransition(this, exit, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exits);

        return true;
    }

    @Override
    protected void createMobs() {
        MorphsNPC morphs = new MorphsNPC();
        morphs.pos = 312;
        mobs.add(morphs);
    }

    @Override
    protected void createItems() {

    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_MORGALAXY;
    }

    @Override
    public String waterTex() {
        return Assets.Interfaces.BLACK_RECT;
    }
}
