package com.shatteredpixel.shatteredpixeldungeon.levels.spical;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ENTRANCE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.SIGN;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.peach.WhiteYan;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Tilemap;

public class PeachGodBossLevel extends Level {

    {
        color1 = 0x801500;
        color2 = 0xa68521;
        viewDistance = 16;
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_CITY_CS;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_CITY;
    }

    @Override
    public void occupyCell( Char ch ) {
        super.occupyCell(ch);
        //GLog.p(String.valueOf(hero.pos));
    }

    private static final int WIDTH = 26;
    private static final int HEIGHT = 29;

    private static final int E = EMPTY;
    private static final int S = SIGN;
    private static final int W = CHASM;
    private static final int R = ENTRANCE;

    private static final int[] code_map = {
            W,W,W,W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            W,W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            W,W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            W,W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            W,W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            W,S,S,S,S,S,S,S,S,S,E,E,E,E,S,S,S,S,S,S,S,S,S,S,S,S,
            W,S,S,S,E,E,E,E,E,S,E,E,E,E,E,E,S,S,S,S,S,S,S,S,S,S,
            W,S,S,E,E,E,E,E,E,S,E,E,E,E,E,E,S,E,E,E,E,E,S,S,S,S,
            W,S,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,S,S,S,
            W,S,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,S,
            W,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,S,
            W,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,S,
            W,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,S,
            S,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,S,
            S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,S,
            S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,W,
            S,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,W,
            S,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,W,
            S,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,W,
            S,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,W,
            W,S,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,S,W,
            W,W,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,S,W,
            W,W,S,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,S,W,W,
            W,W,W,S,S,S,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,S,W,W,W,
            W,W,W,W,S,S,S,S,S,S,S,S,E,E,E,S,S,S,S,S,S,S,W,W,W,W,
            W,W,W,W,W,W,W,W,W,W,W,S,R,S,S,S,S,S,S,S,W,W,W,W,W,W,

    };

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();

        int entrance = 472;
        int exit = 0;

        LevelTransition enter = new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(enter);

        LevelTransition exits = new LevelTransition(this, exit, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exits);

        CustomTilemap vis = new townBehind();
        vis.pos(0, 0);
        customTiles.add(vis);

        //map[exit] = Terrain.LOCKED_EXIT;

        return true;
    }

    public static class townBehind extends CustomTilemap {

        {
            texture = Assets.Environment.PEACH_BOSS;

            tileW = WIDTH;
            tileH = HEIGHT;
        }

        final int TEX_WIDTH = WIDTH*16;

        @Override
        public Tilemap create() {

            Tilemap v = super.create();

            int[] data = mapSimpleImage(0, 0, TEX_WIDTH);

            v.map(data, tileW);
            return v;
        }

    }

    @Override
    protected void createMobs() {

        WhiteYan boss = new WhiteYan();
        boss.pos = 326;
        mobs.add(boss);
    }

    @Override
    protected void createItems() {

    }
}
