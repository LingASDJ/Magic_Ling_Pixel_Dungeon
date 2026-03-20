package com.shatteredpixel.shatteredpixeldungeon.levels.nosync;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Piranha;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync.CrabKing;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.watabou.noosa.audio.Music;

public class CrabKingBossLevel extends Level {

    private static final int R = Terrain.WATER;
    private static final int W = Terrain.WALL;
    private static final int C = Terrain.CHASM;
    private static final int E = Terrain.EMPTY;
    private static final int M = Terrain.STATUE;
    private static final int P = Terrain.EMPTY_SP;
    private static final int O = Terrain.DOOR;
    private static final int B = Terrain.PEDESTAL;
    private static final int V = Terrain.ENTRANCE;

    private static final int WIDTH = 35;
    private static final int HEIGHT = 35;

    @Override
    protected void createMobs() {

    }

    @Override
    public void playLevelMusic(){
        Music.playModeBGM(Assets.Music.BGM_1A,true);
    }

    @Override
    public void occupyCell(Char ch) {
        super.occupyCell(ch);
    }

    private static final int[] code_map = {
            W,W,W,W,W,W,W,W,W,C,C,C,W,W,W,M,E,E,E,M,W,W,W,C,C,C,W,W,W,W,W,W,W,W,W,
            W,R,R,R,R,R,R,R,W,W,W,W,W,R,W,M,E,E,E,M,W,R,W,W,W,W,W,R,R,R,R,R,R,R,W,
            W,R,R,R,R,R,R,R,W,E,E,E,W,R,W,M,E,E,E,M,W,R,W,E,E,E,W,R,R,R,R,R,R,R,W,
            W,R,R,R,R,R,R,W,W,E,W,E,W,W,W,M,E,V,E,M,W,W,W,E,W,E,W,W,R,R,R,R,R,R,W,
            W,W,R,R,R,R,R,R,W,E,E,E,W,R,W,M,E,E,E,M,W,R,W,E,E,E,W,R,R,R,R,R,R,W,W,
            W,W,W,R,R,R,R,R,W,W,W,W,W,R,W,M,E,E,E,M,W,R,W,W,W,W,W,R,R,R,R,R,W,W,W,
            W,R,W,W,R,R,R,R,R,R,W,R,R,R,W,M,E,E,E,M,W,R,R,R,W,R,R,R,R,R,R,W,W,R,W,
            W,R,R,W,W,R,W,W,W,W,W,W,W,W,W,W,W,O,W,W,W,W,W,W,W,W,W,W,W,R,W,W,R,R,W,
            W,W,R,R,W,W,W,E,E,E,E,E,E,R,R,R,R,R,R,R,R,R,E,E,E,E,E,E,W,W,W,R,R,W,W,
            W,W,W,R,R,W,W,E,E,E,E,E,E,W,W,W,W,W,W,W,W,W,E,E,E,E,E,E,W,W,R,R,W,W,W,
            W,R,W,W,R,R,W,E,E,E,E,E,E,R,R,R,R,R,R,R,R,R,E,E,E,E,E,E,W,R,R,W,W,R,W,
            W,R,R,W,W,R,W,E,E,E,W,W,E,E,E,E,E,E,E,E,E,E,E,W,W,E,E,E,W,R,W,W,R,R,W,
            W,W,R,R,W,W,W,E,E,W,W,P,E,E,E,P,W,W,W,P,E,E,E,P,W,W,E,E,W,W,W,R,R,W,W,
            W,W,W,R,R,W,W,E,E,W,P,R,P,E,P,P,P,W,P,P,P,E,P,R,P,W,E,E,W,W,R,R,W,W,W,
            C,C,W,W,R,R,W,E,R,E,R,P,R,P,E,P,R,R,R,P,E,P,R,P,R,E,R,E,W,R,R,W,W,C,C,
            C,C,C,W,W,R,W,E,R,E,R,E,P,R,P,P,P,B,P,P,P,R,P,E,R,E,R,E,W,R,W,W,C,C,C,
            C,C,C,C,W,W,W,E,R,W,R,E,W,P,P,R,R,W,R,R,P,P,W,E,R,W,R,E,W,W,W,C,C,C,C,
            C,C,C,C,C,W,W,E,W,W,W,E,W,W,B,R,W,W,W,R,B,W,W,E,W,W,W,E,W,W,C,C,C,C,C,
            C,C,C,C,C,C,W,E,R,W,R,E,W,P,P,R,R,W,R,R,P,P,W,E,R,W,R,E,W,C,C,C,C,C,C,
            C,C,C,C,C,C,W,E,R,E,R,E,P,R,P,P,P,B,P,P,P,R,P,E,R,E,R,E,W,C,C,C,C,C,C,
            C,C,C,C,C,W,W,E,R,E,R,P,R,P,E,E,P,R,P,E,E,P,R,P,R,E,R,E,W,W,C,C,C,C,C,
            C,C,C,C,W,W,W,E,E,W,P,R,P,E,E,P,E,W,E,P,E,E,P,R,P,W,E,E,W,W,W,C,C,C,C,
            C,C,C,W,W,R,W,E,E,W,W,P,E,E,P,E,W,W,W,E,P,E,E,P,W,W,E,E,W,R,W,W,C,C,C,
            C,C,W,W,R,R,W,E,E,E,W,W,E,E,E,E,E,E,E,E,E,E,E,W,W,E,E,E,W,R,R,W,W,C,C,
            W,W,W,R,R,W,W,E,E,E,E,E,E,R,R,R,R,R,R,R,R,R,E,E,E,E,E,E,W,W,R,R,W,W,W,
            W,W,R,R,W,W,W,E,E,E,E,E,E,W,W,W,W,W,W,W,W,W,E,E,E,E,E,E,W,W,W,R,R,W,W,
            W,R,R,W,W,R,W,E,E,E,E,E,E,R,R,R,R,R,R,R,R,R,E,E,E,E,E,E,W,R,W,W,R,R,W,
            W,R,W,W,R,R,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,R,R,W,W,R,W,
            W,W,W,R,R,W,W,R,R,R,W,W,R,R,W,W,C,C,C,W,W,R,R,W,W,R,R,R,W,W,R,R,W,W,W,
            W,W,R,R,W,W,R,R,R,W,W,R,R,W,W,C,C,C,C,C,W,W,R,R,W,W,R,R,R,W,W,R,R,W,W,
            W,R,R,W,W,R,R,R,W,W,R,R,W,W,C,C,C,C,C,C,C,W,W,R,R,W,W,R,R,R,W,W,R,R,W,
            W,R,W,W,R,R,R,W,W,R,R,W,W,C,C,C,C,C,C,C,C,C,W,W,R,R,W,W,R,R,R,W,W,R,W,
            W,W,W,R,R,R,W,W,R,R,W,W,C,C,C,C,C,C,C,C,C,C,C,W,W,R,R,W,W,R,R,R,W,W,W,
            W,W,R,R,R,W,W,R,R,W,W,C,C,C,C,C,C,C,C,C,C,C,C,C,W,W,R,R,W,W,R,R,R,W,W,
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,
    };

    {
        color1 = 0xcc5445;
        color2 = 0xdd5445;
        viewDistance = 100;
    }

    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if (transition.type == LevelTransition.Type.REGULAR_ENTRANCE) {

            return false;
        } else {
            return super.activateTransition(hero, transition);
        }
    }


    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();

        int entrance = 122;
        exit = 0;

        LevelTransition ecne = new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(ecne);

        return true;
    }

    public static int[] checkSafe_Point = new int[]{
            1157,1187,68,36
    };

    protected void createItems() {
        Level level = this;
        for (int i : checkSafe_Point) {
            Piranha piranha = Piranha.random();
            piranha.pos = i;
            level.mobs.add( piranha );
        }

        CrabKing crabKing = new CrabKing();
        crabKing.pos = 609;
        Buff.affect(crabKing, CrabKing.ReloopLife.class, 123456789f);
        level.mobs.add( crabKing );
    }


    public Mob createMob() {
        return null;
    }

    public int randomRespawnCell() {
        return this.entrance - width();
    }

    public Actor respawner() {
        return null;
    }

    public String tilesTex() {
        return Assets.Environment.TILES_GARDEN;
    }

    public String waterTex() {
        return Assets.Environment.WATER_PRISON;
    }

    @Override
    public String tileName( int tile ) {
        switch (tile) {
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            default:
                return super.tileDesc( tile );
        }
    }

}



