package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.watabou.utils.Random.getRandomElement;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.Qliphoth;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.QliphothLasher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RatKing;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForestPoisonBossLevel extends Level {

    private static final int WIDTH = 33;
    private static final int HEIGHT = 33;

    private static final short W = Terrain.WALL;
    private static final short S = Terrain.CHASM;
    private static final short Q = Terrain.EMPTY;
    private static final short E = Terrain.EMPTY_SP;
    private static final short J = Terrain.WATER;
    private static final short R = Terrain.STATUE_SP;
    private static final short D = Terrain.PEDESTAL;
    private static final short M = Terrain.ENTRANCE;
    private static final short V = Terrain.EXIT;
    private static final short X = Terrain.DOOR;
    private static final short I = Terrain.EMBERS;
    private static final short P = Terrain.DOOR;

    private int status = 0;

    private static final int[] Hard_map = {
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,W,W,W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,W,W,R,W,W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,W,W,R,M,R,W,W,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,W,W,Q,Q,E,Q,Q,W,W,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,W,W,Q,Q,Q,E,Q,Q,Q,W,W,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,W,W,Q,Q,Q,Q,E,Q,Q,Q,Q,W,W,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,W,W,Q,Q,Q,Q,Q,E,Q,Q,Q,Q,Q,W,W,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,W,W,Q,Q,Q,Q,Q,Q,E,Q,Q,W,Q,Q,Q,W,W,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,W,W,Q,Q,Q,Q,Q,Q,Q,D,Q,W,J,W,Q,Q,Q,W,W,S,S,S,S,S,S,S,
            S,S,S,S,S,S,W,W,Q,J,Q,Q,Q,Q,Q,Q,E,Q,Q,W,Q,Q,Q,J,Q,W,W,S,S,S,S,S,S,
            S,S,S,S,S,W,W,Q,Q,Q,J,Q,Q,Q,Q,Q,E,Q,Q,Q,Q,Q,J,Q,Q,Q,W,W,S,S,S,S,S,
            S,S,S,S,W,W,Q,Q,Q,Q,Q,J,Q,Q,Q,Q,E,Q,Q,Q,Q,J,Q,Q,Q,Q,Q,W,W,S,S,S,S,
            S,S,S,W,W,Q,Q,Q,Q,W,Q,Q,J,Q,Q,Q,E,Q,Q,Q,J,Q,Q,Q,Q,Q,Q,Q,W,W,S,S,S,
            S,S,W,W,Q,Q,Q,Q,W,J,W,Q,Q,J,J,E,J,E,J,J,Q,Q,Q,Q,Q,Q,Q,Q,Q,W,W,S,S,
            S,W,W,Q,Q,Q,Q,Q,Q,W,Q,Q,Q,J,E,E,E,E,E,J,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,W,W,S,
            W,W,R,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,E,E,Q,Q,Q,E,E,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,R,W,W,
            W,R,E,E,E,E,E,E,D,E,E,E,E,J,E,Q,J,Q,E,J,E,E,E,E,D,E,E,E,E,E,E,R,W,
            W,W,R,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,E,E,Q,Q,Q,E,E,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,R,W,W,
            S,W,W,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,J,E,E,E,E,E,J,Q,Q,Q,W,Q,Q,Q,Q,Q,Q,W,W,S,
            S,S,W,W,Q,Q,Q,Q,Q,Q,Q,Q,Q,J,J,E,J,E,J,J,Q,Q,W,J,W,Q,Q,Q,Q,W,W,S,S,
            S,S,S,W,W,Q,Q,Q,Q,Q,Q,Q,J,Q,Q,Q,E,Q,Q,Q,J,Q,Q,W,Q,Q,Q,Q,W,W,S,S,S,
            S,S,S,S,W,W,Q,Q,Q,Q,Q,J,Q,Q,Q,Q,E,Q,Q,Q,Q,J,Q,Q,Q,Q,Q,W,W,S,S,S,S,
            S,S,S,S,S,W,W,Q,Q,Q,J,Q,Q,W,Q,Q,E,Q,Q,Q,Q,Q,J,Q,Q,Q,W,W,S,S,S,S,S,
            S,S,S,S,S,S,W,W,Q,J,Q,Q,W,J,W,Q,D,Q,Q,Q,Q,Q,Q,J,Q,W,W,S,S,S,S,S,S,
            S,S,S,S,S,S,S,W,W,Q,Q,Q,Q,W,Q,Q,E,Q,Q,Q,Q,Q,Q,Q,W,W,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,W,W,Q,Q,Q,Q,Q,Q,E,Q,Q,Q,Q,Q,Q,W,W,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,W,W,Q,Q,Q,Q,Q,E,Q,Q,Q,Q,Q,W,W,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,W,W,Q,Q,Q,Q,E,Q,Q,Q,Q,W,W,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,W,W,Q,Q,Q,E,Q,Q,Q,W,W,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,W,W,Q,Q,E,Q,Q,W,W,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,W,W,R,E,R,W,W,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,W,W,R,W,W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,W,W,W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
    };

    private static final int[] end = {
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,
            W,Q,Q,Q,Q,E,Q,Q,Q,Q,Q,W,S,S,S,S,E,S,S,S,S,W,Q,Q,Q,Q,Q,Q,E,Q,Q,Q,W,
            W,Q,Q,Q,E,Q,E,Q,Q,Q,Q,W,S,S,S,S,E,S,S,S,S,W,Q,Q,Q,Q,Q,E,Q,E,Q,Q,W,
            W,Q,Q,E,Q,Q,Q,E,Q,Q,Q,W,S,S,S,E,M,E,S,S,S,W,Q,Q,Q,Q,E,Q,Q,Q,E,Q,W,
            W,Q,E,Q,Q,D,Q,Q,E,Q,Q,X,E,S,E,S,E,S,E,S,E,X,Q,Q,Q,E,Q,Q,D,Q,Q,E,W,
            W,Q,Q,E,Q,Q,Q,E,Q,Q,Q,W,E,E,S,S,E,S,S,E,E,W,Q,Q,Q,Q,E,Q,Q,Q,E,Q,W,
            W,Q,Q,Q,E,Q,E,Q,Q,Q,Q,W,E,S,S,S,E,S,S,S,E,W,Q,Q,Q,Q,Q,E,Q,E,Q,Q,W,
            W,Q,Q,Q,Q,E,Q,Q,Q,Q,Q,W,S,E,S,S,E,S,S,E,S,W,Q,Q,Q,Q,Q,Q,E,Q,Q,Q,W,
            W,W,W,W,W,W,W,W,W,W,W,W,S,S,E,S,E,S,E,S,S,W,W,W,W,W,W,W,W,W,W,W,W,
            W,S,J,S,J,S,J,S,S,E,J,W,S,S,S,E,E,E,S,S,S,W,J,E,S,S,S,S,J,S,S,S,W,
            W,S,J,S,J,S,J,S,S,J,E,W,W,W,W,W,P,W,W,W,W,W,E,J,S,S,J,S,J,S,J,S,W,
            W,S,J,S,S,S,J,S,S,J,S,E,S,S,S,S,E,S,S,S,S,E,S,J,S,S,S,S,S,S,S,S,W,
            W,S,S,S,J,S,S,S,S,J,S,J,E,S,S,S,E,S,S,S,E,J,S,J,S,S,J,S,J,S,J,S,W,
            W,S,S,S,J,S,S,S,S,S,S,J,S,E,E,E,J,E,E,E,S,J,S,S,S,S,S,S,S,S,S,S,W,
            W,S,J,S,J,S,J,S,S,S,S,J,S,E,J,E,E,E,J,E,S,J,S,S,S,S,J,S,J,S,J,S,W,
            W,S,J,S,S,S,J,S,S,S,S,S,S,E,E,J,J,J,E,E,S,S,S,S,S,S,S,S,J,S,S,S,W,
            W,E,E,E,E,E,E,E,E,E,E,E,E,J,E,J,V,J,E,J,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,S,S,S,S,S,S,S,S,S,S,S,S,E,E,J,J,J,E,E,S,S,S,S,S,S,S,S,S,S,S,S,W,
            W,S,S,S,S,E,S,S,S,S,S,S,S,E,J,E,E,E,J,E,S,S,S,S,S,S,S,E,S,S,S,S,W,
            W,W,S,S,E,J,E,S,S,E,S,S,S,E,E,E,J,E,E,E,S,S,S,E,S,S,E,J,E,S,S,W,W,
            W,W,W,S,S,E,S,S,J,J,E,S,E,R,J,J,E,J,J,R,E,S,E,J,J,S,S,E,S,S,W,W,W,
            S,S,W,W,S,S,S,S,J,E,S,E,J,J,D,D,D,D,D,J,J,E,S,E,J,S,S,S,S,W,W,S,S,
            S,S,S,W,W,S,S,S,J,S,E,J,J,J,D,J,E,J,D,J,J,J,E,S,J,S,S,S,W,W,S,S,S,
            S,S,S,S,W,W,W,S,J,E,E,E,R,E,D,E,I,E,D,E,R,E,E,E,J,S,W,W,W,S,S,S,S,
            S,S,S,S,S,S,W,W,S,S,E,J,J,J,D,J,E,J,D,J,J,J,E,S,S,W,W,S,S,S,S,S,S,
            S,S,S,S,S,S,S,W,W,S,S,E,J,J,D,D,D,D,D,J,J,E,S,S,W,W,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,W,W,S,S,E,R,J,J,E,J,J,R,E,S,S,W,W,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,W,W,S,S,E,J,J,E,J,J,E,S,S,W,W,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,W,W,W,S,E,J,E,J,E,S,W,W,W,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,W,W,S,E,E,E,S,W,W,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,W,W,S,E,S,W,W,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,W,W,E,W,W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,W,W,W,W,W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
    };

    private static final int[] easy_map ={
            E,E,E,E,E,E,E,E,S,S,S,S,S,S,S,W,W,W,S,S,S,S,S,S,S,E,E,E,E,E,E,E,E,
            E,E,E,E,E,J,J,S,S,S,S,S,S,J,W,W,R,W,W,J,S,S,S,S,S,S,J,J,E,E,E,E,E,
            E,E,E,E,J,J,S,S,S,S,S,S,J,W,W,R,M,R,W,W,J,S,S,S,S,S,S,J,J,E,E,E,E,
            E,E,E,J,J,S,S,S,S,S,S,J,W,W,Q,Q,E,Q,Q,W,W,J,S,S,S,S,S,S,J,J,E,E,E,
            E,E,J,J,S,S,S,S,S,S,J,W,W,Q,Q,Q,E,Q,Q,Q,W,W,J,S,S,S,S,S,S,J,J,E,E,
            E,J,J,S,S,S,S,S,S,J,W,W,Q,Q,Q,Q,E,Q,Q,Q,Q,W,W,J,S,S,S,S,S,S,J,J,E,
            E,J,S,S,S,S,S,S,J,W,W,Q,Q,Q,Q,Q,E,Q,Q,Q,Q,Q,W,W,J,S,S,S,S,S,S,J,E,
            E,S,S,S,S,S,S,J,W,W,Q,Q,Q,Q,Q,Q,E,Q,W,Q,W,Q,Q,W,W,J,S,S,S,S,S,S,E,
            S,S,S,S,S,S,J,W,W,Q,Q,Q,Q,Q,Q,Q,D,Q,Q,J,Q,Q,Q,Q,W,W,J,S,S,S,S,S,S,
            S,S,S,S,S,J,W,W,Q,J,Q,Q,Q,Q,Q,Q,E,Q,W,Q,W,Q,Q,J,Q,W,W,J,S,S,S,S,S,
            S,S,S,S,J,W,W,Q,Q,Q,J,Q,Q,Q,Q,Q,E,Q,Q,Q,Q,Q,J,Q,Q,Q,W,W,J,S,S,S,S,
            S,S,S,J,W,W,Q,Q,Q,Q,Q,J,Q,Q,Q,Q,E,Q,Q,Q,Q,J,Q,Q,Q,Q,Q,W,W,J,S,S,S,
            S,S,J,W,W,Q,Q,Q,W,Q,W,Q,J,Q,Q,Q,E,Q,Q,Q,J,Q,Q,Q,Q,Q,Q,Q,W,W,J,S,S,
            S,J,W,W,Q,Q,Q,Q,Q,J,Q,Q,Q,J,J,E,J,E,J,J,Q,Q,Q,Q,Q,Q,Q,Q,Q,W,W,J,S,
            S,W,W,Q,Q,Q,Q,Q,W,Q,W,Q,Q,J,E,E,E,E,E,J,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,W,W,S,
            W,W,R,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,E,E,Q,Q,Q,E,E,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,R,W,W,
            W,R,E,E,E,E,E,E,D,E,E,E,E,J,E,Q,J,Q,E,J,E,E,E,E,D,E,E,E,E,E,E,R,W,
            W,W,R,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,E,E,Q,Q,Q,E,E,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,R,W,W,
            S,W,W,Q,Q,Q,Q,Q,Q,Q,Q,Q,Q,J,E,E,E,E,E,J,Q,Q,W,Q,W,Q,Q,Q,Q,Q,W,W,S,
            S,J,W,W,Q,Q,Q,Q,Q,Q,Q,Q,Q,J,J,E,J,E,J,J,Q,Q,Q,J,Q,Q,Q,Q,Q,W,W,J,S,
            S,S,J,W,W,Q,Q,Q,Q,Q,Q,Q,J,Q,Q,Q,E,Q,Q,Q,J,Q,W,Q,W,Q,Q,Q,W,W,J,S,S,
            S,S,S,J,W,W,Q,Q,Q,Q,Q,J,Q,Q,Q,Q,E,Q,Q,Q,Q,J,Q,Q,Q,Q,Q,W,W,J,S,S,S,
            S,S,S,S,J,W,W,Q,Q,Q,J,Q,W,Q,W,Q,E,Q,Q,Q,Q,Q,J,Q,Q,Q,W,W,J,S,S,S,S,
            S,S,S,S,S,J,W,W,Q,J,Q,Q,Q,J,Q,Q,D,Q,Q,Q,Q,Q,Q,J,Q,W,W,J,S,S,S,S,S,
            S,S,S,S,S,S,J,W,W,Q,Q,Q,W,Q,W,Q,E,Q,Q,Q,Q,Q,Q,Q,W,W,J,S,S,S,S,S,S,
            E,S,S,S,S,S,S,J,W,W,Q,Q,Q,Q,Q,Q,E,Q,Q,Q,Q,Q,Q,W,W,J,S,S,S,S,S,S,E,
            E,J,S,S,S,S,S,S,J,W,W,Q,Q,Q,Q,Q,E,Q,Q,Q,Q,Q,W,W,J,S,S,S,S,S,S,J,E,
            E,J,J,S,S,S,S,S,S,J,W,W,Q,Q,Q,Q,E,Q,Q,Q,Q,W,W,J,S,S,S,S,S,S,J,J,E,
            E,E,J,J,S,S,S,S,S,S,J,W,W,Q,Q,Q,E,Q,Q,Q,W,W,J,S,S,S,S,S,S,J,J,E,E,
            E,E,E,J,J,S,S,S,S,S,S,J,W,W,Q,Q,E,Q,Q,W,W,J,S,S,S,S,S,S,J,J,E,E,E,
            E,E,E,E,J,J,S,S,S,S,S,S,J,W,W,R,E,R,W,W,J,S,S,S,S,S,S,J,J,E,E,E,E,
            E,E,E,E,E,J,J,S,S,S,S,S,S,J,W,W,R,W,W,J,S,S,S,S,S,S,J,J,E,E,E,E,E,
            E,E,E,E,E,E,E,E,S,S,S,S,S,S,S,W,W,W,S,S,S,S,S,S,S,E,E,E,E,E,E,E,E,
    };

    @Override
    public void playLevelMusic(){
        Music.playModeBGM(Assets.Music.JUNGLE_FOREST,true);
    }

    @Override
    public void playBossMusic(){
        Game.runOnRenderThread(() -> Music.INSTANCE.fadeOut(5f,
        () -> Music.playModeBGM(Assets.Music.BGM_BOSSA,true)));
    }

    private int[] rk_chest= new int[]{
        707,708,709,710,711,
        839,840,841,842,843,
        740,773,806,744,777,810
    };

    private List<Integer> wood_a_pos = new ArrayList<>(Arrays.asList(471,437,405,439));
    private List<Integer> wood_b_pos = new ArrayList<>(Arrays.asList(739,771,805,773));
    private List<Integer> wood_c_pos = new ArrayList<>(Arrays.asList(649,617,651,683));
    private List<Integer> wood_d_pos = new ArrayList<>(Arrays.asList(250,282,316,284));
    @Override
    public void occupyCell( Char ch ) {
        super.occupyCell(ch);

        int randomElement_A = getRandomElement(wood_a_pos);
        int randomElement_B = getRandomElement(wood_b_pos);
        int randomElement_C = getRandomElement(wood_c_pos);
        int randomElement_D = getRandomElement(wood_d_pos);

        int entrance = 82;

        if (status == 0 && ch == Dungeon.hero) {
            seal();
            status++;
            CellEmitter.get(entrance).start(FlameParticle.FACTORY, 0.1f, 10);
            if(Dungeon.isChallenged(Challenges.STRONGER_BOSSES)){

                set( randomElement_A, Terrain.BARRICADE );
                GameScene.updateMap( randomElement_A );

                set( randomElement_B, Terrain.BARRICADE );
                GameScene.updateMap( randomElement_B );

                set( randomElement_C, Terrain.BARRICADE );
                GameScene.updateMap( randomElement_C );

                set( randomElement_D, Terrain.BARRICADE );
                GameScene.updateMap( randomElement_D );
                Dungeon.level.drop(new Bomb(),hero.pos);
                Dungeon.level.drop(new Bomb(),hero.pos);
            }
        }

        for (Mob boss : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (boss instanceof Qliphoth) {
                final int random =  Random.InvNormalIntRange(7,10);
                if(((Qliphoth) boss).boss_teleport >= random && ((Qliphoth) boss).state_boss >= 2){
                    int pos;
                    switch (Random.Int(4)) {
                        case 0:
                        default:
                            pos = 416;
                            break;
                        case 1:
                            pos = 408;
                            break;
                        case 2:
                            pos = 672;
                            break;
                        case 3:
                            pos = 680;
                            break;
                    }
                    ScrollOfTeleportation.teleportToLocation(boss, pos);
                    CellEmitter.get(boss.pos).burst(Speck.factory(Speck.LIGHT), 10);
                    ((Qliphoth) boss).boss_teleport = 0;
                } else {
                    ((Qliphoth) boss).boss_teleport++;
                }
            }
        }

    }

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = Dungeon.isChallenged(Challenges.STRONGER_BOSSES) ? Hard_map.clone() : easy_map.clone();

        int enter = 82;
        LevelTransition ent = new LevelTransition(this, enter, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(ent);

        LevelTransition exit = new LevelTransition(this,0, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exit);
        return true;
    }

    //首先诞生的4个触手 一阶段
    public static int[] ForestBoss2_LasherPos = new int[]{
            280,536,775,552
    };

    @Override
    public void seal() {
        super.seal();

        set( 82, Terrain.EMPTY_SP );
        GameScene.updateMap( 82 );

        for (int i : ForestBoss2_LasherPos) {
            QliphothLasher qliphothLasher = new QliphothLasher();

            Buff.affect( hero, MindVision.class, 5f );
            Buff.affect(qliphothLasher,Qliphoth.Lasher_Damage.class);
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                if(mob instanceof Qliphoth){
                    qliphothLasher.pos = i;
                    GameScene.add(qliphothLasher);
                    mob.HP = 150;
                    mob.notice();
                    break;
                }

            }


        }
    }

    @Override
    public void unseal() {
        super.unseal();
        setMapEnd();

        for (Heap heap : Dungeon.level.heaps.valueList()) {
            List<Item> toRemove = new ArrayList<>();
            for (Item item : heap.items) {
                if (!(item instanceof PotionOfPurity.PotionOfPurityLing)) {
                    item.doPickUp(hero, 962);
                    toRemove.add(item);  // 收集待删除的元素
                } else {
                    toRemove.add(item);  // 同样收集待删除的元素
                }
            }
            // 删除所有收集到的元素
            heap.items.removeAll(toRemove);
            heap.destroy();  // 销毁 heap
        }

        changeMap(end);

        GameScene.flash(0x80cc0000);

        RatKing king = new RatKing();
        king.pos = 775;
        GameScene.add(king);
        for (int i : rk_chest) {
            Heap droppedGold = Dungeon.level.drop( new Gold( Random.IntRange( 50, 65 )),i);
            droppedGold.type = Heap.Type.CHEST;
            droppedGold.sprite.view( droppedGold );
        }

        Heap droppedB = Dungeon.level.drop( Generator.randomUsingDefaults( Generator.Category.RING), 137 );
        droppedB.type = Heap.Type.CRYSTAL_CHEST;
        droppedB.sprite.view( droppedB );

        Heap droppedC= Dungeon.level.drop( Generator.randomUsingDefaults( Generator.Category.WAND), 160 );
        droppedC.type = Heap.Type.CRYSTAL_CHEST;
        droppedC.sprite.view( droppedC );
    }

    public void setMapEnd(){
        int entrance = 115;
        int exit = 544;
        LevelTransition enter = new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(enter);

        LevelTransition exit2 = new LevelTransition(this, exit, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exit2);

        int doorPos = 647;
        Mob.holdAllies(this, doorPos);
        Mob.restoreAllies(this, Dungeon.hero.pos, doorPos);
    }

    @Override
    protected void createMobs() {
        Qliphoth boss = new Qliphoth();
        boss.pos = 544;
        mobs.add( boss );
    }

    @Override
        public int randomRespawnCell( Char ch ) {
        int pos = 148; //random cell adjacent to the entrance.
        int cell;
        do {
            cell = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
        } while (!passable[cell]
                || (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
                || Actor.findChar(cell) != null);
        return cell;
    
    }

    @Override
    protected void createItems() {

    }

    public String tilesTex() {
        return Assets.Environment.TILES_SEWERS;
    }

    public String waterTex() {
        return Assets.Environment.WATER_SEWERS;
    }

    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put("level_status", status);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        status = bundle.getInt("level_status");
    }

}
