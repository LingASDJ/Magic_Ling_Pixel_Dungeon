package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;

public class ShopBossLevel extends Level {

    private static final int W = WALL;
    private static final int X = WATER;
    private static final int P = Terrain.EMPTY;
    private static final int G = Terrain.EMPTY;
    private static final int S = Terrain.CHASM;
    private static final int R = Terrain.EMPTY_SP;
    private static final int J = Terrain.WELL;
    private static final int K = Terrain.ALCHEMY;
    private static final int D = Terrain.STATUE;
    private static final int H = Terrain.PEDESTAL;

    private static final int[] code_map = {
            W,W,W,W,W,W,W,W,W,W,W,W,S,S,S,S,S,S,S,S,S,S,S,W,W,W,W,W,W,W,W,W,W,W,W,
            W,J,P,P,R,P,R,P,P,P,W,W,W,S,S,S,S,S,S,S,S,S,W,W,W,P,P,P,R,P,R,P,P,J,W,
            W,P,P,R,R,R,R,R,P,P,W,P,W,W,S,S,S,S,S,S,S,W,W,P,W,P,P,R,R,R,R,R,P,P,W,
            W,P,R,P,R,P,R,P,R,P,W,P,P,W,W,S,S,S,S,S,W,W,P,P,W,P,R,P,R,P,R,P,R,P,W,
            W,P,P,P,R,P,R,P,R,P,W,P,P,P,W,W,S,S,S,W,W,P,P,P,W,P,R,P,R,P,R,P,P,P,W,
            W,P,R,R,R,K,R,R,R,P,W,P,P,P,P,W,W,S,W,W,P,P,P,P,W,P,R,R,R,K,R,R,R,P,W,
            W,P,R,P,R,P,R,P,P,P,W,P,P,P,P,P,W,W,W,P,P,P,P,P,W,P,P,P,R,P,R,P,R,P,W,
            W,P,R,P,R,P,R,P,R,P,W,P,P,P,P,R,P,W,P,R,P,P,P,P,W,P,R,P,R,P,R,P,R,P,W,
            W,P,P,R,R,R,R,R,P,P,W,P,P,P,R,P,P,R,P,P,R,P,P,P,W,P,P,R,R,R,R,R,P,P,W,
            W,P,P,P,R,P,R,P,P,W,W,P,P,R,P,P,P,R,P,P,P,R,P,P,W,W,P,P,R,P,R,P,P,P,W,
            W,W,W,W,W,W,W,W,W,W,J,P,R,P,P,P,P,R,P,P,P,P,R,P,J,W,W,W,W,W,W,W,W,W,W,
            W,W,P,P,P,P,P,P,P,P,P,R,R,R,R,R,R,R,R,R,R,R,R,R,P,P,P,P,P,P,P,P,P,W,W,
            S,W,W,P,P,P,P,P,P,P,R,R,R,D,G,G,G,H,X,X,X,X,R,R,R,P,P,P,P,P,P,P,W,W,S,
            S,S,W,W,P,P,P,P,P,R,P,R,X,R,G,G,G,R,X,X,X,R,D,R,P,R,P,P,P,P,P,W,W,S,S,
            S,S,S,W,W,P,P,P,R,P,P,R,X,X,R,G,G,R,X,X,R,G,G,R,P,P,R,P,P,P,W,W,S,S,S,
            S,S,S,S,W,W,P,R,P,P,P,R,X,X,X,R,D,R,X,R,G,G,G,R,P,P,P,R,P,W,W,S,S,S,S,
            S,S,S,S,S,W,W,P,P,P,P,R,X,X,X,X,R,R,R,D,G,G,G,R,P,P,P,P,W,W,S,S,S,S,S,
            S,S,S,S,S,S,W,W,R,R,R,R,H,R,R,R,R,R,R,R,R,R,H,R,R,R,R,W,W,S,S,S,S,S,S,
            S,S,S,S,S,W,W,P,P,P,P,R,G,G,G,D,R,R,R,X,X,X,X,R,P,P,P,P,W,W,S,S,S,S,S,
            S,S,S,S,W,W,P,R,P,P,P,R,G,G,G,R,X,R,D,R,X,X,X,R,P,P,P,R,P,W,W,S,S,S,S,
            S,S,S,W,W,P,P,P,R,P,P,R,G,G,R,X,X,R,G,G,R,X,X,R,P,P,R,P,P,P,W,W,S,S,S,
            S,S,W,W,P,P,P,P,P,R,P,R,D,R,X,X,X,R,G,G,G,R,X,R,P,R,P,P,P,P,P,W,W,S,S,
            S,W,W,P,P,P,P,P,P,P,R,R,R,X,X,X,X,H,G,G,G,D,R,R,R,P,P,P,P,P,P,P,W,W,S,
            W,W,P,P,P,P,P,P,P,P,P,R,R,R,R,R,R,R,R,R,R,R,R,R,P,P,P,P,P,P,P,P,P,W,W,
            W,W,W,W,W,W,W,W,W,W,J,P,R,P,P,P,P,R,P,P,P,P,R,P,J,W,W,W,W,W,W,W,W,W,W,
            W,P,R,P,P,P,P,P,R,W,W,P,P,R,P,P,P,R,P,P,P,R,P,P,W,W,R,P,P,P,P,P,R,P,W,
            W,P,P,R,P,P,P,R,P,P,W,P,P,P,R,P,P,R,P,P,R,P,P,P,W,P,P,R,P,P,P,R,P,P,W,
            W,P,P,P,R,P,R,P,P,P,W,P,P,P,P,R,P,W,P,R,P,P,P,P,W,P,P,P,R,P,R,P,P,P,W,
            W,P,P,P,P,R,P,P,P,P,W,P,P,P,P,P,W,W,W,P,P,P,P,P,W,P,P,P,P,R,P,P,P,P,W,
            W,P,R,R,R,K,R,R,R,P,W,P,P,P,P,W,W,S,W,W,P,P,P,P,W,P,R,R,R,K,R,R,R,P,W,
            W,P,P,P,P,R,P,P,P,P,W,P,P,P,W,W,S,S,S,W,W,P,P,P,W,P,P,P,P,R,P,P,P,P,W,
            W,P,R,R,R,R,R,R,R,P,W,P,P,W,W,S,S,S,S,S,W,W,P,P,W,P,R,R,R,R,R,R,R,P,W,
            W,P,P,P,P,R,P,P,P,P,W,P,W,W,S,S,S,S,S,S,S,W,W,P,W,P,P,P,P,R,P,P,P,P,W,
            W,J,P,P,P,R,P,P,P,P,W,W,W,S,S,S,S,S,S,S,S,S,W,W,W,P,P,P,P,R,P,P,P,J,W,
            W,W,W,W,W,W,W,W,W,W,W,W,S,S,S,S,S,S,S,S,S,S,S,W,W,W,W,W,W,W,W,W,W,W,W,
    };

    {
        color1 = 5459774;
        color2 = 12179041;
    }

    protected boolean build() {
        setSize(35, 35);
        map = code_map.clone();

        this.entrance = (this.width * 25) + 17;
        exit = 0;
        return true;
    }

    protected void createItems() {
        //
    }

    public Mob createMob() {
        return null;
    }

    protected void createMobs() {
        //
    }

    public int randomRespawnCell() {
        return this.entrance - width();
    }

    public Actor respawner() {
        return null;
    }

    public String tilesTex() {
        return Assets.Environment.TILES_COLD;
    }

    public String waterTex() {
        return Assets.Environment.WATER_COLD;
    }

}

