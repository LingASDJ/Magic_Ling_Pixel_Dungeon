package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.BOOKSHELF;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_WELL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL_DECO;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Group;
import com.watabou.noosa.Halo;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.PointF;

public class PacmanHollowActorLevel extends Level {

    private static final int WIDTH = 64;
    private static final int HEIGHT = 24;

    private static final int W = WALL;
    private static final int K =Terrain.WATER;
    private static final int G= Terrain.HIGH_GRASS;

    private static final int I =Terrain.STATUE;

    private static final int M = EMPTY_SP;
    private static final int L = WALL_DECO;
    private static final int P = Terrain.CHASM;
    private static final int D = DOOR;

    private static final int B = BOOKSHELF;
    private static final int R = EMPTY_WELL;



    private static final int[] code_map = {
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,
            W,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,W,
            W,K,W,W,W,K,W,K,K,W,W,W,K,K,K,W,K,K,K,W,K,W,K,K,K,W,W,K,K,W,W,W,K,K,K,K,W,K,K,K,K,K,K,K,W,K,K,K,W,W,W,W,K,K,K,W,W,W,W,K,K,K,K,W,
            W,K,W,W,W,K,K,K,W,L,W,L,W,K,K,W,W,K,W,W,K,W,K,K,K,W,L,W,K,W,P,P,W,K,K,W,W,W,K,K,W,K,K,W,W,W,K,K,W,L,L,W,K,W,K,W,L,L,W,K,K,K,K,W,
            W,K,K,K,K,K,K,K,W,W,L,W,W,K,K,W,K,L,K,W,K,W,K,K,K,W,W,K,K,W,P,P,W,K,K,K,W,K,K,W,W,W,K,K,W,K,K,K,W,W,W,W,K,W,K,W,W,W,W,K,K,K,K,W,
            W,W,W,W,K,K,W,K,K,W,W,W,K,K,K,W,K,K,K,W,K,W,W,W,K,W,K,K,K,W,W,W,K,K,K,K,K,K,K,K,W,K,K,K,K,K,K,K,K,K,K,K,K,W,K,K,K,K,K,K,W,W,W,W,
            P,P,P,W,K,W,W,W,K,K,W,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,W,K,K,K,K,K,K,K,K,K,K,K,K,W,K,W,W,W,W,W,K,W,K,K,W,P,P,P,
            P,P,P,W,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,W,K,W,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,W,P,P,P,
            W,W,W,L,K,W,W,W,K,K,W,W,W,K,K,W,W,W,W,W,W,K,K,W,W,W,K,K,K,K,K,W,W,W,K,K,W,K,K,K,L,D,D,D,D,L,K,K,W,W,W,K,K,W,K,K,W,W,W,K,W,W,W,W,
            R,G,G,G,K,W,P,W,K,K,W,P,W,K,W,W,P,P,P,P,W,W,K,W,P,W,K,K,K,K,K,W,P,W,K,W,K,W,K,W,I,M,M,M,M,I,W,K,W,P,W,K,K,K,K,K,W,P,W,K,G,G,G,R,
            R,G,G,G,K,W,P,W,K,K,W,P,W,K,W,P,P,W,W,P,P,W,K,W,P,W,K,K,K,K,K,W,P,W,K,K,W,K,K,W,M,M,W,W,M,M,W,K,W,P,P,W,K,K,K,W,P,P,W,K,G,G,G,R,
            R,G,G,G,K,W,P,W,W,W,W,P,W,K,W,P,W,P,P,W,P,W,K,W,P,W,K,K,K,K,K,W,P,W,K,W,K,W,K,W,M,W,P,P,W,M,W,K,K,W,P,W,K,W,K,W,P,W,K,K,G,G,G,R,
            R,G,G,G,K,W,P,P,P,P,P,P,W,K,W,P,W,P,P,W,P,W,K,W,P,W,K,K,K,K,K,W,P,W,K,K,W,K,K,W,M,W,P,P,W,M,W,K,K,W,P,P,W,W,W,P,P,W,K,K,G,G,G,R,
            W,W,L,W,K,W,P,W,W,W,W,P,W,K,W,P,W,P,P,W,P,W,K,W,P,W,K,K,K,K,K,W,P,W,K,K,K,K,K,W,M,W,P,P,W,M,W,K,K,W,P,W,P,W,P,W,P,W,K,K,W,W,W,W,
            P,P,P,W,K,W,P,W,K,K,W,P,W,K,W,P,P,W,W,P,P,W,K,W,P,W,W,W,W,W,K,W,P,W,W,W,W,W,K,W,M,M,W,W,M,M,W,K,K,K,W,P,P,W,P,P,W,K,K,K,W,P,P,P,
            P,P,P,W,K,W,P,W,K,K,W,P,W,K,W,W,P,P,P,P,W,W,K,W,P,P,P,P,P,W,K,W,P,P,P,P,P,W,K,W,I,M,M,M,M,I,W,K,K,K,W,P,W,K,W,P,W,K,K,K,W,P,P,P,
            W,W,W,L,K,W,W,W,K,K,W,W,W,K,K,W,W,W,W,W,W,K,K,W,W,W,W,W,W,W,K,W,W,W,W,W,W,W,K,K,W,W,W,W,W,W,K,K,K,K,K,W,K,K,K,W,K,K,K,K,W,W,W,W,
            W,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,W,
            W,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,W,L,W,W,W,W,W,W,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,W,
            L,K,W,B,L,B,W,K,W,B,W,W,L,W,B,W,W,W,K,K,K,K,W,W,W,L,W,W,W,W,W,W,W,W,W,W,L,W,K,W,W,W,W,W,W,W,W,K,W,L,W,B,B,B,W,W,K,K,K,W,L,W,K,W,
            W,K,W,B,W,B,W,K,W,W,B,W,W,B,W,L,W,W,K,W,L,K,W,W,W,W,W,W,W,W,W,L,W,W,W,W,W,W,K,K,K,K,W,W,K,K,K,K,W,W,W,B,B,B,W,W,K,K,K,W,W,W,K,W,
            W,K,W,B,B,B,W,K,W,W,W,B,B,W,W,W,W,W,K,W,W,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,B,B,K,L,W,K,B,B,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,W,
            W,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,L,W,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,W,W,K,K,K,K,W,W,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,K,W,
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,L,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,L,W,W,W,W,W,W,
    };

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();
        int enter = 0;

        LevelTransition ent = new LevelTransition(this, enter, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(ent);

        return true;
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_HOLLOW;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_HOLLOW;
    }

    @Override
    protected void createMobs() {

    }

    @Override
    protected void createItems() {

    }

    @Override
    public Group addVisuals() {
        super.addVisuals();
        addPrisonVisuals(this, visuals);
        return visuals;
    }

    public static void addPrisonVisuals(Level level, Group group){
        for (int i=0; i < level.length(); i++) {
            if (level.map[i] == Terrain.WALL_DECO) {
                group.add( new HollowLevel.PumpLanter( i ) );
            }
        }
    }

    public static class PumpLanter extends Emitter {

        private int pos;

        public PumpLanter( int pos ) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld( pos );
//            for (int i=0; i){
//
//            }
//            pos( p.x - 4, p.y + 2, 2, 0 );
//
//            pour( FlameParticle.FACTORY, 0.15f );

            add( new Halo( 12, 0xFFa500, 0.3f ).point( p.x, p.y + 1 ) );
        }

        @Override
        public void update() {
            if (visible == (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
                super.update();
            }
        }
    }

}
