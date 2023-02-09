package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.CrivusFruits;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.CrivusFruitsLasher;
import com.shatteredpixel.shatteredpixeldungeon.effects.Ripple;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

//克里弗斯之果 5层
public class ForestBossLevel extends Level {

    @Override
    protected void createMobs() {

    }

    @Override
    protected void createItems() {

    }

    public static final int WIDTH = 32;
    private static final int HEIGHT = 32;

    private static final short W = Terrain.WALL;
    private static final short L = Terrain.CHASM;
    private static final short M = Terrain.EMPTY;
    private static final short H = Terrain.EMPTY_SP;
    private static final short T = Terrain.STATUE;
    private static final short X = Terrain.DOOR;
    private static final short D = Terrain.PEDESTAL;

    private static final short B = Terrain.WALL_DECO;
    private static final short C = Terrain.WATER;
    private static final short S = Terrain.LOCKED_DOOR;
    private static final short Q = Terrain.EXIT;
    private static final short A = Terrain.CRYSTAL_DOOR;
    private static final short I = Terrain.ENTRANCE;


    private static final int getBossDoor = WIDTH*9+16;
    private static final int LDBossDoor = WIDTH*10+16;

    private static final int HOME = WIDTH + 16;

    //首先诞生的8个触手 一阶段
    public static int[] ForestBossLasherPos = new int[]{
            WIDTH*17+11,
            WIDTH*15+13,
            WIDTH*23+13,
            WIDTH*21+11,

            WIDTH*15+19,
            WIDTH*17+21,
            WIDTH*21+21,
            WIDTH*23+19,
    };

    //2阶段4个主触手
    public static int[] ForestBossLasherTWOPos = new int[]{
            WIDTH*15+13,
            WIDTH*15+19,
            WIDTH*23+13,
            WIDTH*23+19,
    };

    //铺路
    public static int[] UpdateRead = new int[]{
            WIDTH*11+12,
            WIDTH*11+10,
            WIDTH*11+11,
            WIDTH*11+9,

            WIDTH*11+20,
            WIDTH*11+21,
            WIDTH*11+22,
    };

    @Override
    public void occupyCell( Char ch ) {

        super.occupyCell( ch );

        //如果有生物来到BossDoor的下一个坐标，且生物是玩家，那么触发seal().
        if (map[getBossDoor] == Terrain.DOOR && ch.pos == LDBossDoor && ch == Dungeon.hero) {
            seal();
        }
    }

    @Override
    public void seal() {
        super.seal();

        for (int i : ForestBossLasherPos) {
            CrivusFruitsLasher csp = new CrivusFruitsLasher();
            csp.pos = i;
            GameScene.add(csp);
        }

        CrivusFruits boss = new CrivusFruits();
        boss.state = boss.WANDERING;
        boss.pos = WIDTH*19+16;
        GameScene.add( boss );
        set( getBossDoor, Terrain.WALL );
        GameScene.updateMap( getBossDoor );
        set( HOME, Terrain.EMPTY );
        GameScene.updateMap( HOME );
        Dungeon.observe();

        drop( new PotionOfPurity(),   WIDTH*11+15  );
        drop( new PotionOfPurity(),   WIDTH*15+16  );
        drop( new PotionOfPurity(),   WIDTH*11+17  );
    }

    @Override
    public void unseal() {
        super.unseal();

        set( getBossDoor, Terrain.EMPTY );
        GameScene.updateMap( getBossDoor );

        set( HOME, Terrain.ENTRANCE );
        GameScene.updateMap( HOME );

        for (int i : UpdateRead) {
            set( i, Terrain.EMPTY_SP );
            GameScene.updateMap( i );
        }

        Dungeon.observe();
    }

    //地图结构
    private static final int[] WorldRoomShort = {
            W,W,W,W,W,W,W,B,B,B,B,W,W,W,W,W,W,W,W,W,W,W,B,B,B,B,W,W,W,W,W,W,
            W,M,M,M,M,M,M,C,C,C,C,L,L,W,T,M,I,M,T,W,L,L,C,C,C,C,M,M,M,M,M,W,
            W,M,M,M,M,M,M,C,C,C,L,L,L,W,T,M,M,M,T,W,L,L,L,C,C,C,M,M,M,M,M,W,
            W,M,M,M,M,M,M,C,C,L,L,L,L,W,T,M,H,M,T,W,L,L,L,L,C,C,M,M,M,M,M,W,
            W,M,M,M,M,M,M,C,C,L,L,L,L,W,T,M,M,M,T,W,L,L,L,L,C,C,M,M,M,M,M,W,
            W,M,M,M,M,M,M,C,C,L,L,L,L,W,T,M,H,M,T,W,L,L,L,L,C,C,M,M,M,M,M,W,
            W,M,M,M,M,M,M,C,C,L,L,L,L,W,T,M,M,M,T,W,L,L,L,L,C,C,M,M,M,M,M,W,
            W,M,M,M,M,M,M,C,C,L,L,L,L,W,T,M,H,M,T,W,L,L,L,L,C,C,M,M,M,M,M,W,
            W,M,M,M,M,M,M,C,C,L,L,L,L,W,T,M,M,M,T,W,L,L,L,L,C,C,M,M,M,M,M,W,
            W,M,M,M,M,M,M,C,C,L,L,L,L,W,W,W,X,W,W,W,L,L,L,L,C,C,M,M,M,M,M,W,
            W,M,M,M,M,M,M,C,C,L,L,L,L,W,M,B,H,B,M,W,L,L,L,L,C,C,M,M,M,M,M,W,
            W,M,M,M,M,M,M,C,C,C,L,L,L,A,M,C,H,C,M,A,L,L,L,C,C,C,M,M,M,M,M,W,
            W,M,M,M,M,M,M,C,C,C,C,L,L,W,M,C,H,C,M,W,L,L,C,C,C,C,M,M,M,M,M,W,
            W,B,B,B,B,B,B,W,W,W,W,B,W,B,M,C,H,C,M,B,W,B,W,W,W,B,B,B,B,B,B,W,
            W,C,C,C,C,C,C,W,W,W,M,C,M,C,M,C,H,C,M,C,M,C,W,W,W,C,C,C,C,C,C,W,
            W,L,L,L,W,W,W,B,M,M,M,C,M,H,M,C,H,C,M,H,M,C,M,M,B,W,W,L,L,L,L,W,
            W,L,L,W,W,M,M,C,M,M,M,C,C,C,C,C,H,C,C,C,M,C,M,M,C,M,W,W,L,L,L,W,
            W,W,W,W,B,M,M,C,M,M,M,H,C,C,H,H,H,H,H,C,M,H,M,M,C,M,M,B,W,W,W,W,
            W,W,C,C,C,C,C,C,C,C,C,C,C,C,H,C,C,C,H,C,C,C,C,C,C,C,C,C,C,C,W,W,
            W,H,H,H,H,H,H,H,H,H,H,H,H,H,H,C,D,C,H,H,H,H,H,H,H,H,H,H,H,H,H,W,
            W,C,C,C,C,C,C,C,C,C,C,C,C,C,H,C,C,C,H,C,C,C,C,C,C,C,C,C,C,C,M,W,
            W,M,M,M,M,M,M,M,M,M,M,H,M,C,H,H,H,H,H,C,M,H,M,M,M,M,M,M,M,M,M,W,
            W,M,M,M,M,M,M,M,M,M,M,M,M,C,C,C,H,C,C,C,M,M,M,M,M,M,M,M,M,M,M,W,
            W,M,M,H,M,M,M,M,M,M,M,M,M,H,M,C,H,C,M,H,M,M,M,M,M,M,M,M,M,M,M,W,
            W,M,M,M,M,M,M,H,M,M,M,M,M,M,M,C,H,C,M,M,M,M,M,M,M,M,M,H,M,M,M,W,
            W,M,M,M,M,M,M,M,M,M,M,W,W,W,W,W,S,W,W,W,W,W,M,H,M,M,M,M,M,M,M,W,
            W,M,M,M,M,M,M,M,M,M,W,W,M,M,W,L,M,L,W,M,M,W,W,M,M,M,M,M,M,M,M,W,
            W,M,M,M,H,M,M,M,W,W,W,M,M,M,W,L,M,L,W,M,M,M,W,W,M,M,M,M,M,M,M,W,
            W,M,M,M,M,M,M,M,W,M,M,M,M,M,S,M,M,M,S,M,M,M,M,W,W,M,M,M,M,M,M,W,
            W,M,M,M,M,M,M,M,W,M,M,M,M,M,W,L,M,L,W,M,M,M,M,M,W,M,M,H,M,M,M,W,
            W,W,M,M,M,M,M,M,W,M,M,M,M,M,W,L,Q,L,W,M,M,M,M,M,W,M,M,M,M,M,W,W,
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,

    };

    private static class Sink extends Emitter {

        private int pos;
        private float rippleDelay = 0;

        private static final Emitter.Factory factory = new Factory() {

            @Override
            public void emit( Emitter emitter, int index, float x, float y ) {
                WaterParticle p = (WaterParticle)emitter.recycle( WaterParticle.class );
                p.reset( x, y );
            }
        };

        public Sink( int pos ) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld( pos );
            pos( p.x - 2, p.y + 3, 4, 0 );

            pour( factory, 0.1f );
        }

        @Override
        public void update() {
            if (visible == (pos < level.heroFOV.length && level.heroFOV[pos])) {

                super.update();

                if (!isFrozen() && (rippleDelay -= Game.elapsed) <= 0) {
                    Ripple ripple = GameScene.ripple( pos + level.width() );
                    if (ripple != null) {
                        ripple.y -= DungeonTilemap.SIZE / 2f;
                        rippleDelay = Random.Float(0.1f, 0.9f);
                    }
                }
            }
        }
    }

    public static final class WaterParticle extends PixelParticle {

        public WaterParticle() {
            super();

            acc.y = 50;
            am = 0.5f;

            color( ColorMath.random( 0xb6ccc2, 0x3b6653 ) );
            size( 2 );
        }

        public void reset( float x, float y ) {
            revive();

            this.x = x;
            this.y = y;

            speed.set( Random.Float( -1, +1 ), 0 );

            left = lifespan = 0.5f;
        }
    }

    public static void addSewerVisuals( Level level, Group group ) {
        for (int i=0; i < level.length(); i++) {
            if (level.map[i] == Terrain.WALL_DECO) {
                group.add( new ForestBossLevel.Sink( i ) );
            }
        }
    }

    //构建地图
    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = WorldRoomShort.clone();

        this.entrance = WIDTH + 16 ;
        this.exit = WIDTH*30 + 16;

        return true;
    }

    //加入粒子效果
    @Override
    public Group addVisuals() {
        super.addVisuals();
        addSewerVisuals(this, visuals);
        return visuals;
    }

    public String tilesTex() {
        return Assets.Environment.TILES_SEWERS;
    }

    public String waterTex() {
        return Assets.Environment.WATER_SEWERS;
    }


}

