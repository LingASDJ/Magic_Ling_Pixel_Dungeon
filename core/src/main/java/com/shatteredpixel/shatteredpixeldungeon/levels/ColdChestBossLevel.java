package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.levels.ColdChestBossLevel.State.START;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DiamondKnight;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

//宝藏迷宫 10层
public class ColdChestBossLevel extends Level {

    private State pro;

    private DiamondKnight diamond;

    public State pro(){
        return pro;
    }
    private static final String PRO	        = "pro";
    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        pro = bundle.getEnum(PRO, State.class);
    }

    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(PRO, pro);
    }

    //地图状态
    public enum State {
        START,
        MAZE_START,
        VSBOSS_START,
        FIND_START,
        WIN
    }

    private static final int WIDTH = 35;
    private final int HEIGHT = 35;

    @Override
    protected boolean build() {
        setSize(WIDTH,HEIGHT);
        this.entrance = 0;
        this.exit = 0;
        //首次构建地图
        pro = START;
        setMapStart();

        return true;
    }
    private static final short W = Terrain.WALL;
    private static final short E = Terrain.CHASM;

    private static final short X = Terrain.EXIT;
    private static final short J = Terrain.ENTRANCE;
    private static final short O = Terrain.WATER;
    private static final short P = Terrain.EMPTY_SP;
    private static final short B = Terrain.EMPTY_SP;
    private static final short K = Terrain.EMPTY;
    private static final short L = Terrain.EMPTY;
    private static final short D = Terrain.DOOR;
    private static final short T = Terrain.PEDESTAL;

    private static final int[] WorldRoomShort = {
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,
            W,E,E,E,W,W,W,W,W,E,E,E,E,E,E,E,W,J,W,E,E,E,E,E,E,E,W,W,W,W,W,E,E,E,W,
            W,E,E,E,O,O,O,O,O,E,E,E,E,E,E,E,W,P,W,E,E,E,E,E,E,E,O,O,O,O,O,E,E,E,W,
            W,E,E,E,P,P,P,P,P,E,E,E,E,E,E,E,W,P,W,E,E,E,E,E,E,E,P,P,P,P,P,E,E,E,W,
            W,E,E,E,P,E,E,E,P,E,E,E,E,E,E,E,W,K,W,E,E,E,E,E,E,E,P,E,E,E,P,E,E,E,W,
            W,E,P,E,P,E,P,E,P,E,P,E,P,E,P,E,W,P,W,E,P,E,P,E,P,E,P,E,P,E,P,E,P,E,W,
            W,E,E,E,P,E,E,E,P,E,E,E,E,E,E,E,W,P,W,E,E,E,E,E,E,E,P,E,E,E,P,E,E,E,W,
            W,E,E,E,P,P,P,P,P,E,E,E,E,E,E,E,W,P,W,E,E,E,E,E,E,E,P,P,P,P,P,E,E,E,W,
            W,E,E,E,E,E,P,E,E,E,E,E,E,E,E,E,W,K,W,E,E,E,E,E,E,E,E,E,P,E,E,E,E,E,W,
            W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,P,W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,E,E,E,E,E,P,E,W,W,W,W,E,E,E,E,W,P,W,E,E,E,E,W,W,W,W,E,P,E,E,E,E,E,W,
            W,E,E,E,E,E,E,E,W,K,K,W,W,W,W,W,W,D,W,W,W,W,W,W,K,K,W,E,E,E,E,E,E,E,W,
            W,E,E,E,E,E,P,E,W,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,W,E,P,E,E,E,E,E,W,
            W,E,E,E,E,E,E,E,W,K,K,K,K,K,K,K,K,O,K,K,K,K,K,K,K,K,W,E,E,E,E,E,E,E,W,
            W,W,W,W,W,W,W,W,W,K,K,K,K,K,K,K,O,O,O,K,K,K,K,K,K,K,W,W,W,W,W,W,W,W,W,
            W,B,B,B,B,B,B,B,W,K,K,K,K,K,K,O,O,O,O,O,K,K,K,K,K,K,W,B,B,B,B,B,B,B,W,
            W,B,K,K,K,K,K,B,W,K,K,K,K,K,O,O,O,K,O,O,O,K,K,K,K,K,W,B,K,K,K,K,K,B,W,
            W,B,K,B,B,B,K,B,W,K,K,K,K,K,O,O,K,K,K,O,O,K,K,K,K,K,W,B,K,B,B,B,K,B,W,
            W,B,B,B,T,B,B,B,W,K,K,K,K,O,O,K,O,O,O,K,O,O,K,K,K,K,W,B,B,B,T,B,B,B,W,
            W,B,K,B,B,B,K,B,D,P,P,K,K,O,K,K,O,K,O,K,K,O,K,K,P,P,D,B,K,B,B,B,K,B,W,
            W,B,K,K,K,K,K,B,W,K,K,K,K,O,O,K,O,O,O,K,O,O,K,K,K,K,W,B,K,K,K,K,K,B,W,
            W,B,K,K,K,K,K,B,W,K,K,K,K,K,O,O,K,K,K,O,O,K,K,K,K,K,W,B,K,K,K,K,K,B,W,
            W,B,B,B,B,B,B,B,W,K,K,K,K,K,O,O,O,K,O,O,O,K,K,K,K,K,W,B,B,B,B,B,B,B,W,
            W,W,W,W,W,W,W,W,W,K,K,K,K,K,K,O,O,O,O,O,K,K,K,K,K,K,W,W,W,W,W,W,W,W,W,
            W,E,E,W,T,W,E,E,W,K,K,K,K,K,K,K,O,O,O,K,K,K,K,K,K,K,W,E,W,T,W,E,E,E,W,
            W,E,E,W,K,W,E,E,W,K,K,W,W,W,K,K,K,O,K,K,K,W,W,W,K,K,W,E,W,K,W,E,E,E,W,
            W,E,E,W,K,W,E,E,W,K,K,W,E,W,K,K,K,K,K,K,K,W,E,W,K,K,W,E,W,K,W,E,E,E,W,
            W,E,E,W,K,W,E,E,W,W,W,W,E,W,K,K,K,P,K,K,K,W,E,W,W,W,W,E,W,K,W,E,E,E,W,
            W,E,E,W,K,W,E,E,E,E,E,E,E,W,K,K,K,K,K,K,K,W,E,E,E,E,E,E,W,K,W,E,E,E,W,
            W,E,E,W,K,W,E,E,E,E,E,E,E,W,K,K,K,P,K,K,K,W,E,E,E,E,E,E,W,K,W,E,E,E,W,
            W,E,E,W,K,W,W,W,W,W,W,W,W,W,K,K,P,P,P,K,K,W,W,W,W,W,W,W,W,K,W,E,E,E,W,
            W,E,E,W,K,K,K,K,K,K,K,K,K,D,K,P,P,X,P,P,K,D,K,K,K,K,K,K,K,K,W,E,E,E,W,
            W,E,E,W,W,W,W,W,W,W,W,W,W,W,K,K,P,P,P,K,K,W,W,W,W,W,W,W,W,W,W,E,E,E,W,
            W,E,E,E,E,E,E,E,E,E,E,E,E,W,K,K,K,P,K,K,K,W,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,

    };

    private static final int[] MazeRoom = {
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,
            W,J,L,L,L,L,W,L,W,L,W,W,W,W,W,L,L,L,W,W,W,W,W,W,L,L,L,L,W,L,W,W,W,L,W,
            W,W,W,W,W,L,W,L,W,L,W,W,W,W,W,L,W,W,W,W,W,W,W,W,L,W,L,L,W,L,W,W,W,L,W,
            W,W,W,L,L,L,W,L,L,L,W,L,L,L,W,L,L,L,W,L,L,L,L,L,L,W,L,L,W,L,W,W,W,L,W,
            W,W,W,L,W,W,W,L,W,W,W,L,W,L,L,L,L,L,W,L,L,L,L,W,W,W,L,L,L,L,L,L,W,L,W,
            W,L,L,L,W,L,L,L,L,L,L,L,W,L,W,L,W,L,W,W,W,L,L,W,L,L,L,L,L,L,W,L,W,L,W,
            W,L,W,L,W,L,W,W,W,W,W,W,W,W,W,L,W,L,W,L,L,L,L,W,L,L,W,W,W,W,W,L,W,L,W,
            W,W,W,L,L,L,L,L,L,W,L,W,L,L,W,L,W,W,W,L,L,W,W,W,L,L,L,L,L,L,W,W,W,L,W,
            W,W,W,L,W,W,W,W,W,W,L,W,L,L,W,L,W,L,L,L,L,W,L,W,L,L,W,W,W,L,W,L,L,L,W,
            W,W,W,L,L,W,L,W,L,L,L,L,L,L,W,L,W,W,W,W,W,W,L,W,L,L,L,L,W,L,W,L,L,L,W,
            W,W,W,L,L,W,L,W,L,W,W,W,W,W,W,L,W,L,W,L,L,W,L,W,L,L,W,L,W,L,L,L,W,L,W,
            W,W,W,L,L,L,L,W,L,L,W,L,L,L,W,L,L,L,W,L,L,W,L,W,L,L,W,L,W,L,W,W,W,L,W,
            W,W,W,W,W,W,L,L,L,L,W,L,L,L,W,L,W,W,W,L,L,L,L,L,L,L,W,L,L,L,L,L,W,L,W,
            W,W,W,L,L,L,L,L,L,L,W,L,L,L,L,L,W,W,W,L,L,W,L,W,L,L,W,W,W,W,L,W,W,L,W,
            W,W,W,W,W,L,L,W,L,L,L,L,L,L,L,L,W,W,W,L,L,W,L,W,L,L,L,L,L,L,L,L,W,L,W,
            W,W,W,L,L,L,W,W,L,W,W,W,L,W,L,L,L,L,W,L,W,W,W,W,W,L,L,L,L,L,L,L,W,L,W,
            W,W,W,L,L,L,L,W,L,W,W,W,L,W,L,L,L,L,W,L,L,W,L,L,L,L,L,W,W,W,W,W,W,L,W,
            W,W,W,L,L,L,L,W,L,W,W,W,L,W,L,L,W,L,L,L,L,W,L,W,L,L,L,L,L,L,L,L,L,L,W,
            W,W,W,L,W,W,W,W,L,L,L,L,L,L,L,L,W,L,L,L,L,W,L,W,W,W,L,L,W,L,L,L,L,W,W,
            W,W,W,L,L,L,L,W,L,L,L,L,L,L,L,L,W,W,W,W,L,L,L,W,L,W,W,L,W,W,W,W,W,W,W,
            W,W,W,L,L,L,L,W,L,L,L,L,W,W,W,L,W,L,L,L,L,L,L,W,L,L,L,L,L,L,L,L,L,W,W,
            W,L,L,L,L,L,L,L,L,L,L,L,W,L,L,L,W,L,W,W,W,W,L,W,L,W,W,W,W,W,W,W,L,W,W,
            W,L,W,L,L,W,W,W,W,L,W,W,W,W,W,W,W,L,L,L,L,W,L,L,L,L,L,L,L,L,L,W,L,L,W,
            W,L,W,L,L,L,L,L,W,L,L,L,L,L,W,L,W,W,W,W,L,W,L,L,L,L,L,L,W,L,L,W,L,L,W,
            W,L,W,W,W,W,L,L,W,L,L,W,W,W,W,L,L,W,L,W,L,W,L,L,W,W,W,L,W,L,L,L,L,L,W,
            W,W,W,L,L,L,L,L,W,L,L,L,L,L,W,L,L,W,L,L,L,W,L,L,L,L,W,L,W,W,W,W,W,W,W,
            W,W,W,L,W,L,W,W,W,L,L,L,W,L,W,L,L,W,W,W,L,W,W,W,W,W,W,L,W,L,L,L,L,L,W,
            W,W,W,L,W,L,L,L,W,L,L,L,W,L,W,L,L,W,L,L,L,L,L,L,L,L,W,L,L,L,W,W,W,L,W,
            W,W,W,L,W,L,L,W,W,L,W,L,W,L,L,L,L,W,L,W,W,W,L,W,L,L,W,L,W,L,W,L,W,L,W,
            W,W,W,W,W,L,L,W,W,L,W,L,W,L,W,W,W,W,L,L,W,L,L,W,L,L,L,L,W,L,W,W,W,L,W,
            W,W,W,L,L,L,W,W,W,L,W,L,W,L,L,W,L,L,L,L,L,L,L,W,L,L,L,L,W,L,L,L,L,L,W,
            W,W,W,L,W,L,L,L,W,L,W,L,W,L,L,W,L,W,W,W,W,W,L,W,W,W,W,W,W,W,W,W,W,W,W,
            W,W,W,L,W,L,W,L,W,W,W,W,W,L,W,W,L,L,L,L,W,L,L,L,W,L,L,L,W,L,L,L,W,L,W,
            W,L,L,L,W,L,W,L,L,L,L,L,L,L,L,W,W,W,W,W,W,L,W,L,L,L,W,L,L,L,W,L,L,X,W,
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,
    };


    private void setMapStart() {
        entrance = HOME;
        map = WorldRoomShort.clone();
    }

    private void setMazeStart() {
        entrance = HOME;
        map = MazeRoom.clone();
    }

    @Override
    public void occupyCell(Char ch) {
        super.occupyCell(ch);

        if (ch == Dungeon.hero){
            switch (pro){
                case START:
                    if (map[getBossDoor] == Terrain.DOOR && ch.pos == LDBossDoor) {
                        progress();
                    }
                    break;
                case MAZE_START:
                    changeMap(MazeRoom);
                    break;
            }
        }

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if(mob instanceof DiamondKnight && mob.HP >400){
                GLog.n("ColdChestBoss");
            }

        }

    }

    private void cleanMapState(){
        buildFlagMaps();
        cleanWalls();

        BArray.setFalse(visited);
        BArray.setFalse(mapped);

        for (Blob blob: blobs.values()){
            blob.fullyClear();
        }
        addVisuals(); //this also resets existing visuals
        traps.clear();

        GameScene.resetMap();
        Dungeon.observe();
    }

    public void progress(){
        switch (pro) {
            case START:
                //如果有生物来到BossDoor的下一个坐标，且生物是玩家，那么触发seal().
                    seal();
                    DiamondKnight boss = new DiamondKnight();
                    boss.state = boss.WANDERING;
                    boss.pos = WIDTH*19+17;
                    GameScene.add( boss );
                    set( getBossDoor, Terrain.LOCKED_DOOR );
                    GameScene.updateMap( getBossDoor );
                    set( HOME, Terrain.EMPTY );
                    GameScene.updateMap( HOME );
                    Dungeon.observe();
                break;
            case MAZE_START:
                break;
        }
    }




    @Override
    protected void createMobs() {

    }

    private static final int getBossDoor = WIDTH*11+17;
    private static final int LDBossDoor = WIDTH*12+17;
    private static final int HOME = WIDTH*2+17;

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
