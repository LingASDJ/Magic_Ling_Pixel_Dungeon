package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Levitation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSight;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra.ArchettoWeightLess;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra.Yuanxi;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.HellFlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class UnlessEndFlowerLevel extends Level {

    private static final int S = Terrain.CHASM;
    private static final int B = Terrain.WALL_DECO;
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
        Music.playModeBGM(Assets.Music.WEIGHTLESS,true);
    }

    public static class UnlessAbyss extends Buff {

        {
            type = buffType.POSITIVE;
        }

        private int level = 0;
        private int interval = 1;

        public int Time = 0;

        public boolean isCollapsing = false;
        public boolean isCollapseFinished = false; // ✅ 新增：崩坏完成/停止标记

        @Override
        public boolean act() {
            if (target.isAlive()) {
                spend( interval );
                if (level <= 0) {
                    detach();
                }
            } else {
                detach();
            }
            return true;
        }

        public int level() {
            return level;
        }

        public void set( int value, int time ) {
            if (level <= value) {
                level = value;
                interval = time;
                spend(time - cooldown() - 1);
            }
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString(level);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", Time);
        }

        private static final String LEVEL	    = "level";
        private static final String INTERVAL    = "interval";
        private static final String TIME        = "time";
        private static final String IS_COLLAPSING = "isCollapsing";
        private static final String IS_FINISHED = "isCollapseFinished"; // ✅ 存档支持

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( INTERVAL, interval );
            bundle.put( LEVEL, level );
            bundle.put( TIME, Time);
            bundle.put( IS_COLLAPSING, isCollapsing);
            bundle.put( IS_FINISHED, isCollapseFinished);
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle( bundle );
            interval = bundle.getInt( INTERVAL );
            level = bundle.getInt( LEVEL );
            Time = bundle.getInt(TIME);
            isCollapsing = bundle.getBoolean(IS_COLLAPSING);
            isCollapseFinished = bundle.getBoolean(IS_FINISHED);
        }
    }

    @Override
    public void occupyCell(Char ch) {
        super.occupyCell(ch);
        if (!(ch instanceof Hero)) return;
        Hero hero = (Hero) ch;

        UnlessAbyss unlessAbyss = hero.buff(UnlessAbyss.class);
        if (unlessAbyss == null) {
            Buff.affect(hero, UnlessAbyss.class).set(100000000, 1);
        }
    }

    public void triggerTerrainCollapse() {
        int width = width();
        int height = height();

        int size = Random.oneOf(3,5,7,9);
        int half = size / 2;

        int centerX = Random.Int(half, width - half);
        int centerY = Random.Int(half, height - half);

        ArrayList<Integer> validCells = new ArrayList<>();
        for (int dx = -half; dx <= half; dx++) {
            for (int dy = -half; dy <= half; dy++) {
                int x = centerX + dx;
                int y = centerY + dy;
                int cell = x + y * width;
                int totalCells = width * height;

                int terrain = map[cell];

                if (x < 0 || x >= width || y < 0 || y >= height) {
                    continue;
                }

                if (cell >= totalCells) {
                    continue;
                }

                if (terrain != Terrain.GALAXY
                        && terrain != Terrain.ENTRANCE
                        && terrain != Terrain.EXIT
                        && terrain != Terrain.ENTRANCE_SP
                        && terrain != Terrain.PEDESTAL
                        && terrain != Terrain.WATER) {
                    validCells.add(cell);
                }
            }
        }

        if (validCells.size() < 2) return;

        int[] temps = new int[validCells.size()];
        for (int i = 0; i < validCells.size(); i++) {
            temps[i] = map[validCells.get(i)];
        }
        Random.shuffle(validCells);
        for (int i = 0; i < validCells.size(); i++) {
            map[validCells.get(i)] = temps[i];
        }

        for (int cell : validCells) {
            if (Random.Float() < 0.5f) {
                map[cell] = Terrain.CHASM;
                CellEmitter.get(cell).burst(HellFlameParticle.FACTORY, 2);
            }
        }

        this.map = map.clone();
        buildFlagMaps();
        cleanWalls();
        BArray.setFalse(visited);
        BArray.setFalse(mapped);

        for (Blob blob: blobs.values()){
            blob.fullyClear();
        }

        GameScene.resetMap();
        GameScene.updateMap();
        Dungeon.observe();
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
            S,S,S,S,B,F,F,F,F,B,B,B,D,B,B,B,F,F,F,F,B,S,S,S,S,
            S,S,S,S,B,F,F,R,R,F,B,B,D,B,B,F,R,R,F,F,B,S,S,S,S,
            S,S,S,S,B,F,R,F,R,R,F,B,D,B,F,R,R,F,R,F,B,S,S,S,S,
            S,S,S,S,B,F,R,R,F,R,R,F,D,F,R,R,F,R,R,F,B,S,S,S,S,
            S,S,S,S,B,B,F,R,R,F,R,F,D,F,R,F,R,R,F,B,B,S,S,S,S,
            S,S,S,S,S,B,B,F,R,R,F,F,V,F,F,R,R,F,B,B,S,S,S,S,S,
            S,S,S,S,S,S,B,B,F,F,F,F,D,F,F,F,F,B,B,S,S,S,S,S,S,
            S,S,S,S,S,S,S,B,B,B,B,B,B,B,B,B,B,B,S,S,S,S,S,S,S,
    };

    {
        color1 = 0xcc5445;
        color2 = 0xdd5445;
        viewDistance = 100;
    }

    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if (transition.type == LevelTransition.Type.REGULAR_ENTRANCE) {
            Buff.detach(Dungeon.hero, MagicalSight.class);
            Buff.detach(Dungeon.hero, Levitation.class);
            Buff.detach(Dungeon.hero, UnlessEndFlowerLevel.UnlessAbyss.class);
            TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
            if (timeFreeze != null) timeFreeze.disarmPresses();
            Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
            if (timeBubble != null) timeBubble.disarmPresses();
            InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
            InterlevelScene.curTransition = new LevelTransition();
            InterlevelScene.curTransition.destDepth = 9;
            InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_EXIT;
            InterlevelScene.curTransition.destBranch = 0;
            InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_EXIT;
            InterlevelScene.curTransition.centerCell = -1;
            Game.switchScene(InterlevelScene.class);
            return false;
        } else {
            return super.activateTransition(hero, transition);
        }
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


    protected void createItems() {
        ArchettoWeightLess archettoWeightLess = new ArchettoWeightLess();
        archettoWeightLess.pos = 312;
        mobs.add(archettoWeightLess);

        Yuanxi yuanxi = new Yuanxi();
        yuanxi.pos = 313;
        mobs.add(yuanxi);
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

    @Override
    public String tileName( int tile ) {
        switch (tile) {
            case Terrain.HIGH_GRASS:
                return Messages.get(UnlessEndFlowerLevel.class, "highgrass_name");
            case Terrain.WATER:
                return Messages.get(UnlessEndFlowerLevel.class, "water_name");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.HIGH_GRASS:
                return Messages.get(UnlessEndFlowerLevel.class, "highgrass_desc");
            case Terrain.WATER:
                return Messages.get(UnlessEndFlowerLevel.class, "water_desc");
            default:
                return super.tileDesc( tile );
        }
    }

}


