package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;

//宝藏迷宫 10层
public class ColdChestBossLevel extends Level {

    private State state;
    //地图状态
    public enum State {
        START,
        MAZE_START,
        VSBOSS_START,
        FIND_START,
        WON
    }



    @Override
    protected boolean build() {
        setSize(9,7);
        this.entrance = 0;
        this.exit = 0;
        state = State.START;
        setMapStart();

        return true;
    }
    private static final short W = Terrain.WALL;
    private static final short E = Terrain.EMPTY;

    private static final short X = Terrain.EXIT;
    private static final short R = Terrain.ENTRANCE;

    private static final int[] WorldRoomShort = {
            W, W, W, W, W, W, W, W,W,
            W, E, E, E, E, E, E, X,W,
            W, E, E, E, E, E, E, E,W,
            W, E, E, E, E, E, E, E,W,
            W, E, E, E, E, E, E, E,W,
            W, E, E, E, E, E, E, R,W,
            W, W, W, W, W, W, W, W,W,
    };

    private void setMapStart() {
        entrance = 0;
        exit = 0;
        map = WorldRoomShort.clone();
    }

    @Override
    protected void createMobs() {

    }

    @Override
    protected void createItems() {

    }


    public String tilesTex() {
        return Assets.Environment.TILES_COLDCHEST;
    }

    public String waterTex() {
        return Assets.Environment.WATER_PRISON;
    }
}
