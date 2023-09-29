package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BlueWraith;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Red;
import com.watabou.utils.Random;

public class GardenLevel extends Level {

    private static final int W = WALL;
    private static final int E = EMPTY;
    private static final int L = Terrain.CRYSTAL_DOOR;
    private static final int S =Terrain.EMPTY_SP;

    private static final int F =Terrain.FURROWED_GRASS;

    private static final int D = Terrain.SECRET_DOOR;

    private static final int M = Terrain.SECRET_DOOR;
    private static final int X = Terrain.WATER;

    private static final int WIDTH = 24;
    private static final int HEIGHT = 24;

    private static final int[] code_map = {
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,
            W,S,S,S,S,S,W,E,E,E,W,E,E,E,S,E,E,E,E,E,W,E,E,W,
            W,E,E,E,E,E,D,S,S,S,W,S,S,S,S,E,E,E,E,E,W,E,E,W,
            W,E,E,E,E,E,W,X,X,X,W,E,E,E,S,E,E,E,E,E,W,S,S,W,
            W,W,M,W,D,W,W,X,X,X,W,W,W,W,W,W,D,W,W,W,W,E,E,W,
            W,E,S,E,E,E,E,X,X,X,E,E,E,E,S,E,E,E,E,E,S,E,E,W,
            W,E,S,E,E,E,E,X,X,X,E,S,S,S,S,S,S,E,E,E,S,E,E,W,
            W,E,S,S,S,S,S,S,S,E,E,S,E,E,E,E,S,E,E,E,S,E,E,W,
            W,E,E,E,E,E,E,E,S,E,E,S,E,E,E,E,W,W,W,W,D,W,W,W,
            W,W,W,D,W,W,E,E,W,W,W,W,W,W,W,E,W,E,E,E,S,E,E,W,
            W,S,E,E,E,W,E,E,W,F,F,F,F,F,W,E,W,E,E,E,S,E,E,W,
            W,S,S,S,S,W,S,S,W,F,W,L,W,F,W,E,M,S,S,S,S,E,E,W,
            W,X,S,X,X,W,E,E,W,F,W,S,W,F,W,E,W,E,S,X,X,X,X,W,
            W,X,S,X,X,W,E,E,W,F,W,W,W,F,W,E,W,E,S,X,X,X,X,W,
            W,X,S,X,X,W,E,E,W,F,F,F,F,F,W,E,W,E,S,X,X,X,X,W,
            W,X,S,X,X,M,E,E,W,W,W,L,W,W,W,S,W,S,S,X,X,X,X,W,
            W,X,S,X,X,W,S,S,S,S,S,S,S,S,S,S,W,W,W,D,W,W,W,W,
            W,X,S,X,X,W,E,E,S,X,X,X,X,X,S,S,E,E,S,E,E,E,E,W,
            W,X,S,X,X,W,E,E,S,X,X,S,S,S,S,E,E,E,S,E,E,E,E,W,
            W,E,S,E,E,W,E,E,S,X,X,S,S,E,E,E,E,E,S,S,S,S,S,W,
            W,W,W,D,W,W,S,S,S,S,S,S,S,S,W,W,W,W,D,W,W,M,W,W,
            W,E,E,E,E,W,E,E,S,E,E,S,E,E,W,E,E,E,S,E,E,E,E,W,
            W,E,E,E,E,M,E,E,S,E,E,S,E,E,W,E,E,E,S,E,E,E,E,W,
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,

    };

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();
        this.entrance = 25;
        exit = 0;
        return true;
    }

    /**
     *
     */
    @Override
    protected void createMobs() {
        //第一个i是初始值，第二个i是条件范围值，第三个是循环方式，这里是每次+2
        for (int i = 10; i < 15; i+=2){
            BlueWraith x = new BlueWraith();
            x.pos = (this.width * i + 5);
            mobs.add(x);
        }

        for (int i = 10; i < 15; i+=2){
            BlueWraith a = new BlueWraith();
            a.pos = (this.width * i + 16);
            mobs.add(a);
        }
    }

    /**
     *
     */
    @Override
    protected void createItems() {
        float randomValue = Random.Float();
        int secondCoordinate = (randomValue < 0.3f) ? 529 :
                (randomValue < 0.6f) ? 386 :
                        (randomValue < 0.9f) ? 62 :
                                (randomValue < 0.95f) ? 284 : 251;
        drop(new CrystalKey( Dungeon.depth ), 546 );
        drop(new CrystalKey( Dungeon.depth ), secondCoordinate);


        drop(new Red(), 299);
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_GARDEN;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_COLD;
    }
}
