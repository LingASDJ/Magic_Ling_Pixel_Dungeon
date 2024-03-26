package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ENTRANCE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.HIGH_GRASS;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.LOCKED_DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.LOCKED_EXIT;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.SIGN;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.SIGN_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.STATUE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DwarfGeneral;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DwarfGeneralNPC;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class DwarfGeneralBossLevel extends Level {

    {
        color1 = 0x801500;
        color2 = 0xa68521;
    }

    private static final int WIDTH = 21;
    private static final int HEIGHT = 31;

    private static final int W = WALL;

    private static final int D = DOOR;

    private static final int E = EMPTY_SP;

    private static final int V = SIGN_SP;
    private static final int S = SIGN;
    private static final int R = HIGH_GRASS;

    private static final int C = CHASM;

    private static final int H = LOCKED_EXIT;
    private static final int M = LOCKED_DOOR;

    private static final int X = ENTRANCE;

    private static final int B = STATUE;

    private static final int[] code_map = {
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,W,W,W,W,W,W,W,W,W,W,W,W,W,C,C,C,C,
            C,C,C,C,W,E,W,E,W,E,W,E,W,E,W,E,W,C,C,C,C,
            C,C,C,C,W,E,E,E,E,E,E,E,E,E,E,E,W,C,C,C,C,
            C,C,C,C,W,E,E,E,E,E,E,E,E,E,E,E,W,C,C,C,C,
            C,C,C,C,W,E,E,W,E,W,E,W,E,W,E,W,W,C,C,C,C,
            C,C,C,C,W,E,W,W,W,W,W,W,W,W,W,W,C,C,C,C,C,
            C,C,C,C,W,E,W,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,W,E,W,S,S,S,S,S,S,S,C,C,C,C,C,C,C,
            C,C,C,S,W,M,W,S,S,S,S,S,S,S,S,H,W,S,C,C,C,
            C,C,C,S,R,E,E,E,V,V,V,E,V,R,E,E,E,S,C,C,C,
            C,C,S,S,E,E,E,E,V,V,V,V,E,E,R,E,E,S,S,C,C,
            C,C,S,R,E,E,E,E,E,E,E,E,E,V,E,E,V,V,S,C,C,
            C,C,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,C,C,
            C,C,S,E,E,V,E,E,E,E,E,E,E,E,E,E,E,E,S,C,C,
            C,C,S,S,E,E,E,E,E,E,E,E,E,E,E,V,E,S,S,C,C,
            C,C,C,S,E,E,E,E,E,E,E,E,E,E,E,V,E,S,C,C,C,
            C,C,S,S,E,V,R,E,E,E,E,E,E,E,E,V,E,S,S,C,C,
            C,C,S,E,E,V,E,E,E,E,E,E,E,E,E,E,E,E,W,C,C,
            C,C,S,R,E,E,E,E,E,E,E,E,E,E,E,R,E,E,W,C,C,
            C,C,S,W,E,E,E,V,V,E,E,E,V,V,V,E,E,W,W,C,C,
            C,C,C,W,W,W,R,E,E,E,E,E,V,V,V,W,W,W,C,C,C,
            C,C,C,C,C,W,W,E,R,E,E,E,E,R,W,W,C,C,C,C,C,
            C,C,C,C,C,C,W,W,W,W,D,W,W,W,W,C,C,C,C,C,C,
            C,C,C,C,C,C,W,E,B,E,E,E,B,E,W,C,C,C,C,C,C,
            C,C,C,C,C,C,W,E,E,E,E,E,E,E,W,C,C,C,C,C,C,
            C,C,C,C,C,C,W,W,E,E,E,E,E,W,W,C,C,C,C,C,C,
            C,C,C,C,C,C,C,W,E,E,X,E,E,W,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,W,W,E,E,E,W,W,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,

    };

    private int status = 0;
    private static final int START = 0;
    private static final int FIGHTING = 1;
    private static final int WON = 2;
    private static final int ERROR = 9999999;

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( "level_status", status );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        status = bundle.getInt("level_status");
    }

    private void progress(){
        if(status == START){
            status = FIGHTING;
        }else if(status == FIGHTING){
            status = WON;
        }else{
            status = ERROR;
        }
    }

    @Override
    public void occupyCell( Char ch ) {
        super.occupyCell(ch);
        //GLog.p(String.valueOf(hero.pos));
        //GLog.b("BOSS");
    }

    @Override
    public void seal() {
        super.seal();

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob instanceof DwarfGeneralNPC) {
                mob.die(null);
                GameScene.flash(Window.GDX_COLOR);
                AlarmTrap goes = new AlarmTrap();
                goes.pos = mob.pos;
                goes.activate();
            }
        }


        DwarfGeneral boss = new DwarfGeneral();
        boss.pos = 367;
        boss.notice();
        boss.state = boss.HUNTING;
        GameScene.add(boss);
        ScrollOfTeleportation.appear(hero,493);

        Level.set(514, LOCKED_DOOR);
        GameScene.updateMap(514);
        Dungeon.observe();

        Sample.INSTANCE.play(Assets.Sounds.DEATH);
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_CITY_CS;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_CITY;
    }


    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();

        int entrance = 598;
        int exit = 225;

        LevelTransition enter = new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(enter);

        LevelTransition exits = new LevelTransition(this, exit, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exits);

        CustomTilemap vis = new townBehind();
        vis.pos(0, 0);
        customTiles.add(vis);
        CustomTilemap via = new townAbove();
        via.pos(0, 0);
        customTiles.add(via);

        map[exit] = Terrain.LOCKED_EXIT;

        return true;
    }

    public static class townBehind extends CustomTilemap {

        {
            texture = Assets.Environment.CITY_POX;

            tileW = 21;
            tileH = 30;
        }

        final int TEX_WIDTH = 21*16;

        @Override
        public Tilemap create() {

            Tilemap v = super.create();

            int[] data = mapSimpleImage(0, 0, TEX_WIDTH);

            v.map(data, tileW);
            return v;
        }

    }

    public static class townAbove extends CustomTilemap {

        {
            texture = Assets.Environment.CITY_PO;

            tileW = 21;
            tileH = 30;
        }

        final int TEX_WIDTH = 21*16;

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
        DwarfGeneralNPC boss = new DwarfGeneralNPC();
        boss.pos = 367;
        mobs.add(boss);
    }

    @Override
    protected void createItems() {

    }
}
