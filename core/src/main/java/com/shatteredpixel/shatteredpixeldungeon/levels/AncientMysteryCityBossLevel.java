package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.SakaFishBoss;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class AncientMysteryCityBossLevel extends Level{


    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_ANCIENT;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_ANCIENT;
    }

    private static int WIDTH = 27;
    private static int HEIGHT = 32;

    private static final short W = Terrain.WALL;
    private static final short D = Terrain.WALL_DECO;
    private static final short L = Terrain.EMPTY_SP;
    private static final short R = Terrain.WATER;
    private static final short E = Terrain.STATUE;
    private static final short C = Terrain.HIGH_GRASS;
    private static final short M = Terrain.CRYSTAL_DOOR;
    private static final short G = Terrain.DOOR;

    private static final short K = Terrain.WELL;

    private static final short S = Terrain.BOOKSHELF;

    private static final int[] WorldRoomShort = {
            L,L,L,L,L,L,L,L,L,L,L,L,W,W,W,L,L,L,L,L,L,L,L,L,L,L,L,
            L,R,R,R,R,R,R,L,R,R,R,D,E,R,E,W,R,R,R,L,R,R,R,R,R,R,L,
            L,R,L,L,L,R,R,L,R,R,W,W,R,R,R,W,D,R,R,L,R,R,L,L,L,R,L,
            L,R,L,L,R,R,L,L,R,W,W,R,R,C,R,R,W,W,R,L,L,R,R,L,L,R,L,
            L,R,L,R,R,L,L,R,D,W,E,R,C,L,C,R,E,W,D,R,L,L,R,R,L,R,L,
            L,R,R,R,L,L,R,W,W,R,R,C,L,R,L,C,R,R,W,W,R,L,L,R,R,R,L,
            L,R,R,L,L,R,R,W,R,R,C,L,R,R,R,L,C,R,R,W,R,R,L,L,R,R,L,
            L,L,L,L,R,R,W,E,R,R,R,C,L,R,L,C,R,R,R,E,W,R,R,L,L,L,L,
            L,R,R,R,R,D,W,R,R,R,R,R,C,L,C,R,R,R,R,R,W,W,R,R,R,R,L,
            L,R,R,R,R,W,W,W,D,W,D,W,W,M,W,D,W,D,W,W,W,W,R,R,R,R,L,
            L,R,R,R,W,D,E,C,R,R,R,R,R,R,R,R,R,R,R,R,E,D,W,R,R,R,L,
            L,R,R,W,W,W,K,R,R,R,R,R,R,R,R,R,R,R,R,R,R,W,W,W,R,R,L,
            L,R,W,W,W,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,W,W,W,R,L,
            L,D,W,D,E,R,R,W,R,R,R,R,R,R,R,R,R,R,R,W,R,R,E,W,D,W,L,
            L,W,W,E,R,R,W,W,W,R,R,R,R,R,R,R,R,R,W,W,W,R,R,E,W,W,L,
            L,W,D,C,R,R,R,W,R,R,R,R,R,R,R,R,R,R,R,W,R,R,R,C,W,W,L,
            L,W,W,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,D,W,L,
            L,W,D,C,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,C,W,W,L,
            L,W,W,E,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,D,W,W,L,
            L,R,W,D,E,R,R,W,R,R,R,R,R,R,R,R,R,R,R,W,R,R,D,D,W,R,L,
            L,R,R,W,W,R,W,W,W,R,R,R,R,R,R,R,R,R,W,W,W,R,W,W,R,R,L,
            L,R,R,R,W,R,R,W,R,R,R,R,R,R,R,R,R,R,R,W,R,R,W,R,R,R,L,
            L,L,L,L,W,W,R,R,R,R,R,R,R,R,R,R,R,R,R,R,K,D,W,L,L,L,L,
            L,R,R,L,L,W,E,C,R,R,R,R,R,R,R,R,R,R,R,C,E,W,L,L,R,R,L,
            L,R,R,R,L,W,W,D,R,R,R,R,R,R,R,R,R,R,R,D,W,W,L,R,R,R,L,
            L,R,L,R,L,W,W,W,W,W,W,W,W,G,W,W,W,W,W,W,W,W,L,R,L,R,L,
            L,R,R,R,L,R,R,W,L,R,R,R,E,L,E,R,R,R,R,W,R,R,L,R,R,R,L,
            L,L,R,L,L,R,W,W,W,L,R,S,S,L,S,S,R,L,W,W,W,R,L,L,R,L,L,
            L,L,L,L,R,R,W,L,L,W,R,W,E,L,E,W,R,W,L,L,W,R,R,L,L,L,L,
            L,R,R,R,R,W,W,D,W,W,D,W,W,L,W,W,W,W,W,D,W,W,R,R,R,R,L,
            L,R,R,R,W,D,W,W,D,W,W,W,W,W,W,W,D,W,W,W,W,D,W,R,R,R,L,
            L,L,L,L,L,L,L,L,L,L,L,L,L,L,L,L,L,L,L,L,L,L,L,L,L,L,L,

    };

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = WorldRoomShort.clone();
        entrance = WIDTH*28+13;
        return true;
    }

    @Override
    public int randomRespawnCell( Char ch ) {
        int pos = WIDTH + 16; //random cell adjacent to the entrance.
        int cell;
        do {
            cell = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
        } while (!passable[cell]
                || (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
                || Actor.findChar(cell) != null);
        return cell;
    }



    private static final int getBossDoor = 688;
    private static final int LDBossDoor = 661;
    @Override
    public void occupyCell( Char ch ) {

        super.occupyCell( ch );

        boolean isTrue = ch.pos == LDBossDoor && ch == Dungeon.hero;

        //如果有生物来到BossDoor的下一个坐标，且生物是玩家，那么触发seal().
        if (map[getBossDoor] == Terrain.DOOR && isTrue || map[getBossDoor] == Terrain.EMBERS && isTrue) {
            seal();
        }
    }

    @Override
    public void seal() {
        super.seal();

        set( getBossDoor, Terrain.WALL );
        GameScene.updateMap( getBossDoor );
        set( 688, Terrain.LOCKED_DOOR );
        GameScene.updateMap( 688 );
        Dungeon.observe();
    }

    /**
     *
     */
    @Override
    protected void createMobs() {
        SakaFishBoss boss = new SakaFishBoss();
        boss.pos = 337;
        mobs.add(boss);
    }

    /**
     *
     */
    @Override
    protected void createItems() {

    }
}
