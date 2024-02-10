package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ColdGurad;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Null;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.ShopKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.ShopKing_Two;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.BackGoKey;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;

public class LinkLevel extends Level {

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        return false;
    }

    private static final int W = WALL;
    private static final int B = EMPTY;
    private static final int G = WATER;
    private static final int L = Terrain.CHASM;
    private static final int T = Terrain.EMPTY_SP;
    private static final int S = Terrain.EMPTY_SP;
    private static final int R = Terrain.PEDESTAL;
    private static final int K = Terrain.LOCKED_DOOR;
    private static final int D = Terrain.DOOR;

    //portals. (from, to).

    private static final int WIDTH = 45;
    private static final int HEIGHT = 34;

    private static final int[] code_map = {
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,
            W,R,B,B,B,B,B,B,B,S,S,S,S,B,B,B,B,B,B,B,W,S,R,S,W,B,B,B,B,B,B,B,S,S,S,S,S,B,B,B,B,B,B,R,W,
            W,B,B,B,B,B,B,B,B,S,B,B,S,B,B,B,B,B,B,B,W,W,B,W,W,B,B,B,B,B,B,B,B,B,B,B,S,B,B,B,B,B,B,B,W,
            W,B,B,B,B,B,B,B,B,S,B,B,S,B,B,B,B,B,B,B,W,S,B,S,W,B,B,B,B,B,B,B,B,B,B,S,B,B,B,B,B,B,B,B,W,
            W,B,B,B,B,B,B,B,B,S,B,B,S,B,B,B,B,B,B,B,W,S,B,S,W,B,B,B,B,B,B,B,B,B,S,B,B,B,B,B,B,B,B,B,W,
            W,W,W,W,W,W,W,W,W,S,B,B,S,B,W,W,W,W,W,W,W,S,B,S,W,W,W,W,W,W,W,W,B,B,S,B,B,W,W,W,W,W,W,W,W,
            W,B,B,B,B,B,B,B,W,S,S,S,S,B,W,B,B,B,B,B,W,S,B,S,W,B,B,B,B,B,B,W,B,B,S,B,B,W,B,B,B,B,B,B,W,
            W,B,B,B,B,B,B,B,W,W,W,K,W,W,W,B,B,B,B,B,W,S,B,S,W,B,B,B,B,B,B,W,W,W,K,W,W,W,B,B,B,B,B,B,W,
            W,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,W,S,B,S,W,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,W,
            W,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,W,S,B,S,W,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,W,
            W,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,W,S,B,S,W,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,B,W,
            W,B,B,B,B,B,B,B,B,B,S,S,S,S,S,S,S,S,S,B,W,S,B,S,W,B,S,S,S,S,S,S,S,S,S,B,B,B,B,B,B,B,B,B,W,
            W,B,B,B,B,B,B,B,B,S,B,B,B,B,B,B,B,B,B,S,W,S,B,S,W,S,B,B,B,B,B,B,B,B,B,S,B,B,B,B,B,B,B,B,W,
            W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,W,
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,K,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,
            W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,W,S,W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,W,
            W,B,B,B,B,B,B,B,B,S,B,B,B,B,B,B,B,B,B,B,B,W,B,W,B,B,B,B,B,B,B,B,B,B,B,S,B,B,B,B,B,B,B,B,W,
            W,B,B,B,B,B,B,B,B,S,B,B,B,B,B,B,B,T,B,B,B,W,S,W,B,B,B,T,B,B,B,B,B,B,B,S,B,B,B,B,B,B,B,B,W,
            W,B,B,B,B,B,B,B,B,S,B,B,B,B,B,B,T,G,T,B,B,W,B,W,B,B,T,G,T,B,B,B,B,B,B,S,B,B,B,B,B,B,B,B,W,
            W,S,S,S,S,S,S,S,S,S,B,B,B,B,B,T,G,T,G,T,B,W,S,W,B,T,G,T,G,T,B,B,B,B,B,S,S,S,S,S,S,S,S,S,W,
            W,W,B,B,B,B,B,B,B,S,B,B,B,B,T,G,T,G,T,G,T,W,B,W,T,G,T,G,T,G,T,B,B,B,B,S,B,B,B,B,B,B,B,W,W,
            L,W,W,B,B,B,B,B,B,B,S,B,B,B,B,T,G,T,G,T,B,W,S,W,B,T,G,T,G,T,B,B,B,B,S,B,B,B,B,B,B,B,W,W,L,
            L,L,W,W,B,B,B,B,B,B,B,S,B,B,B,B,T,G,T,B,B,W,B,W,B,B,T,G,T,B,B,B,B,S,B,B,B,B,B,B,B,W,W,L,L,
            L,L,L,W,W,B,B,B,B,B,B,B,S,B,B,B,B,T,B,B,B,W,S,W,B,B,B,T,B,B,B,B,S,B,B,B,B,B,B,B,W,W,L,L,L,
            L,L,L,L,W,W,B,B,B,B,B,B,B,S,B,B,B,B,B,B,B,D,B,D,B,B,B,B,B,B,B,S,B,B,B,B,B,B,B,W,W,L,L,L,L,
            L,L,L,L,L,W,W,B,B,B,B,B,B,B,S,S,B,B,B,B,B,W,S,W,B,B,B,B,B,S,S,B,B,B,B,B,B,B,W,W,L,L,L,L,L,
            L,L,L,L,L,L,W,W,B,B,B,B,B,B,B,S,S,B,B,B,B,W,B,W,B,B,B,B,S,S,B,B,B,B,B,B,B,W,W,L,L,L,L,L,L,
            L,L,L,L,L,L,L,W,W,B,B,B,B,B,B,S,B,S,B,B,B,W,S,W,B,B,B,S,B,S,B,B,B,B,B,B,W,W,L,L,L,L,L,L,L,
            L,L,L,L,L,L,L,L,W,W,B,B,B,B,B,S,B,B,S,B,B,W,B,W,B,B,S,B,B,S,B,B,B,B,B,W,W,L,L,L,L,L,L,L,L,
            L,L,L,L,L,L,L,L,L,W,W,B,B,B,B,S,B,B,B,S,B,W,S,W,B,S,B,B,B,S,B,B,B,B,W,W,L,L,L,L,L,L,L,L,L,
            L,L,L,L,L,L,L,L,L,L,W,W,B,B,B,S,B,B,B,B,S,W,B,W,S,B,B,B,B,S,B,B,B,W,W,L,L,L,L,L,L,L,L,L,L,
            L,L,L,L,L,L,L,L,L,L,L,W,W,B,B,S,B,B,B,B,B,W,S,W,B,B,B,B,B,S,B,B,W,W,L,L,L,L,L,L,L,L,L,L,L,
            L,L,L,L,L,L,L,L,L,L,L,L,W,W,B,S,B,B,B,B,B,W,B,W,B,B,B,B,B,S,B,W,W,L,L,L,L,L,L,L,L,L,L,L,L,
            L,L,L,L,L,L,L,L,L,L,L,L,L,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,L,L,L,L,L,L,L,L,L,L,L,L,L,
    };

    {
        color1 = 5459774;
        color2 = 12179041;
    }

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();

        int entrance = WIDTH*28 + 22;

        LevelTransition enter = new LevelTransition(this, entrance, LevelTransition.Type.BRANCH_EXIT);
        transitions.add(enter);

        return true;
    }

    @Override
    protected void createItems() {
        drop( new BackGoKey(), this.width  + 1  );
        drop( new IronKey(depth), this.width  + 22  );
    }

    @Override
    public Mob createMob() {
        return null;
    }

    @Override
    protected void createMobs() {
        ShopKing n = new ShopKing();
        n.pos = (this.width * 2 + 22);
        mobs.add(n);

        Null v = new Null();
        v.pos = (this.width * 15 + 22);
        mobs.add(v);

        ShopKing_Two vis = new ShopKing_Two();
        vis.pos = (this.width + 43);
        mobs.add(vis);

        //第一个i是初始值，第二个i是条件范围值，第三个是循环方式，这里是每次+2
        for (int i = 20; i < 25; i+=2){
            ColdGurad x = new ColdGurad();
            x.pos = (this.width * i + 10);
            mobs.add(x);
        }

        for (int i = 20; i < 25; i+=2){
            ColdGurad a = new ColdGurad();
            a.pos = (this.width * i + 34);
            mobs.add(a);
        }


    }


    public int randomRespawnCell() {
        return this.entrance - width();
    }


    public Actor respawner() {
        return null;
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_COLD;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_CAVES;
    }

}


