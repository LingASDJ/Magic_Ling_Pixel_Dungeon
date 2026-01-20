package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Hiro;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.watabou.noosa.audio.Music;

public class HiroFlowerLevel extends Level {

    private static final int S = Terrain.CHASM;
    private static final int B = Terrain.BOOKSHELF;
    private static final int W = Terrain.HIGH_GRASS;
    private static final int G = Terrain.EMBERS;
    private static final int R = Terrain.WATER;
    private static final int F = Terrain.FURROWED_GRASS;
    private static final int X = Terrain.PEDESTAL;
    private static final int D = Terrain.EMPTY_SP;
    private static final int K = Terrain.EMPTY_DECO;
    private static final int M = Terrain.DOOR;
    private static final int V = Terrain.ENTRANCE;
    private static final int Y = Terrain.GRASS;

    private static final int WIDTH = 25;
    private static final int HEIGHT = 35;

    @Override
    protected void createMobs() {

    }

    @Override
    public void playLevelMusic(){
        Music.playModeBGM(Assets.Music.BGM_4,true);
    }

    private static final int[] code_map = {
            S,S,S,S,S,S,S,S,S,S,S,S,B,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,B,B,B,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,B,B,G,B,B,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,B,B,G,W,G,B,B,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,B,B,G,W,W,W,G,B,B,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,B,B,B,G,W,R,W,R,W,G,B,B,B,S,S,S,S,S,S,
            S,S,S,S,S,B,B,W,W,W,W,W,R,W,W,W,W,W,B,B,S,S,S,S,S,
            S,S,S,S,S,B,W,W,R,W,W,R,W,R,W,W,R,W,W,B,S,S,S,S,S,
            S,S,S,S,B,B,W,R,W,R,W,W,D,W,W,R,W,R,W,B,B,S,S,S,S,
            S,S,S,B,B,G,W,W,R,W,R,F,Y,F,R,W,R,W,W,G,B,B,S,S,S,
            S,S,B,B,G,W,W,W,W,R,F,K,G,K,F,R,W,W,W,W,G,B,B,S,S,
            S,B,B,G,W,R,W,R,W,F,K,K,K,K,K,F,W,R,W,R,W,G,B,B,S,
            B,B,G,W,W,W,R,W,D,Y,G,K,X,K,G,Y,D,W,R,W,W,W,G,B,B,
            S,B,B,G,W,R,W,R,W,F,K,K,K,K,K,F,W,R,W,R,W,G,B,B,S,
            S,S,B,B,G,W,W,W,W,R,F,K,G,K,F,R,W,W,W,W,G,B,B,S,S,
            S,S,S,B,B,G,W,W,R,W,R,F,Y,F,R,W,R,W,W,G,B,B,S,S,S,
            S,S,S,S,B,B,W,R,W,R,W,W,D,W,W,R,W,R,W,B,B,S,S,S,S,
            S,S,S,S,S,B,W,W,R,W,W,R,W,R,W,W,R,W,W,B,S,S,S,S,S,
            S,S,S,S,S,B,B,W,W,W,W,W,R,W,W,W,W,W,B,B,S,S,S,S,S,
            S,S,S,S,S,S,B,B,B,G,W,R,W,R,W,G,B,B,B,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,B,B,G,W,W,W,G,B,B,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,B,B,G,W,G,B,B,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,B,B,M,B,B,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,B,D,B,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,B,D,B,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,B,D,B,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,B,B,B,B,B,B,S,B,D,B,S,B,B,B,B,B,B,S,S,S,S,
            S,S,S,S,B,W,W,W,W,B,B,B,D,B,B,B,W,W,W,W,B,S,S,S,S,
            S,S,S,S,B,W,W,R,R,W,B,B,D,B,B,W,R,R,W,W,B,S,S,S,S,
            S,S,S,S,B,W,R,W,R,R,W,B,D,B,W,R,R,W,R,W,B,S,S,S,S,
            S,S,S,S,B,W,R,R,W,R,R,W,D,W,R,R,W,R,R,W,B,S,S,S,S,
            S,S,S,S,B,B,W,R,R,W,R,W,D,W,R,W,R,R,W,B,B,S,S,S,S,
            S,S,S,S,S,B,B,W,R,R,W,W,V,W,W,R,R,W,B,B,S,S,S,S,S,
            S,S,S,S,S,S,B,B,W,W,W,W,D,W,W,W,W,B,B,S,S,S,S,S,S,
            S,S,S,S,S,S,S,B,B,B,B,B,B,B,B,B,B,B,S,S,S,S,S,S,S,
    };

    {
        color1 = 5459774;
        color2 = 12179041;
        viewDistance = 100;
    }

    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();

        int entrance = 812;
        exit = 0;

        LevelTransition ecne = new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(ecne);

        return true;
    }

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        return super.activateTransition(hero, transition);
    }


    protected void createItems() {
        Hiro god1= new Hiro();
        god1.pos = 312;
        mobs.add(god1);
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
        return Assets.Environment.TILES_DIED;
    }

    public String waterTex() {
        return Assets.Environment.WATER_CITY;
    }

}


