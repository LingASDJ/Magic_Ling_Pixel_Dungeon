package com.shatteredpixel.shatteredpixeldungeon.levels.nosync;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CRYSTAL_DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_DECO;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ENTRANCE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EXIT;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.LOCKED_DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.PEDESTAL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.bossrush.SkyGoo;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
public class SkyGooBossLevel extends Level {
    private static final int SIZE = 5;
    private static final int Y = CHASM;
    private static final int W = WALL;
    private static final int G = EMPTY;
    private static final int V = EMPTY_SP;
    private static final int Q = WATER;
    private static final int R = WATER;
    private static final int E = ENTRANCE;
    private static final int C = EXIT;
    private static final int U = EMPTY_DECO;
    private static final int P = PEDESTAL;
    private static final int D = DOOR;
    private static final int X = CRYSTAL_DOOR;
    private static final int M = LOCKED_DOOR;

    private static final int[] pre_map = {
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,
            W,G,G,G,G,G,G,G,Q,Q,E,Q,Q,G,G,G,G,G,G,G,W,
            W,V,V,V,V,V,V,V,V,V,Q,V,V,V,V,V,V,V,V,V,W,
            W,G,G,G,G,G,G,G,G,G,Q,G,G,G,G,G,G,G,G,G,W,
            W,V,V,V,V,V,V,V,Q,Q,Q,Q,Q,V,V,V,V,V,V,V,W,
            W,G,G,G,G,G,G,G,Q,Q,G,Q,Q,G,G,G,G,G,G,G,W,
            W,W,W,D,W,W,W,W,Q,G,G,G,Q,W,W,W,W,W,D,W,W,
            W,Q,Q,Q,Q,Q,Q,W,W,W,W,W,W,W,Q,Q,Q,Q,Q,Q,W,
            W,Q,Q,Q,Q,Q,Q,W,V,V,V,V,V,W,Q,Q,Q,Q,Q,Q,W,
            W,Q,Q,Q,Q,Q,Q,W,V,Q,Q,Q,V,W,Q,Q,Q,Q,Q,Q,W,
            W,Q,Q,Q,Q,Q,Q,W,V,Q,V,Q,V,W,Q,Q,Q,Q,Q,Q,W,
            W,V,Q,V,W,W,W,W,V,Q,V,Q,V,W,W,W,W,V,Q,V,W,
            W,V,Q,V,W,V,V,W,V,V,V,Q,V,W,V,V,W,V,Q,V,W,
            W,V,Q,V,W,V,Q,Q,Q,Q,W,Q,V,Q,Q,V,W,V,Q,V,W,
            W,V,Q,V,W,V,V,Q,V,V,V,V,V,Q,V,V,W,V,Q,V,W,
            W,V,Q,V,W,Q,V,W,V,Q,Q,Q,Q,W,V,Q,W,V,Q,V,W,
            W,V,Q,V,W,Q,V,Q,V,Q,V,V,V,Q,V,Q,W,V,Q,V,W,
            W,V,Q,V,W,Q,V,Q,V,Q,V,Q,V,Q,V,V,W,V,Q,V,W,
            W,V,Q,V,W,V,V,Q,V,Q,P,Q,V,Q,V,V,W,V,Q,V,W,
            W,V,Q,V,W,V,Q,Q,V,Q,Q,Q,V,Q,Q,V,W,V,Q,V,W,
            W,V,Q,V,W,V,Q,Q,V,V,V,V,V,Q,Q,V,W,V,Q,V,W,
            W,V,Q,V,W,V,W,Q,Q,Q,Q,Q,Q,Q,W,V,W,V,Q,V,W,
            W,V,Q,V,W,V,V,V,V,V,Q,V,V,V,V,V,W,V,Q,V,W,
            W,X,W,W,W,Q,Q,Q,Q,V,Q,V,Q,Q,Q,Q,W,W,W,X,W,
            W,Q,Q,V,V,V,V,V,Q,V,W,V,Q,V,V,V,V,V,Q,Q,W,
            W,Q,Q,V,Q,Q,Q,V,Q,V,Q,V,Q,V,Q,Q,Q,V,Q,Q,W,
            W,Q,Q,V,Q,Q,Q,Q,Q,V,Q,V,Q,Q,Q,Q,Q,V,Q,Q,W,
            W,Q,Q,V,V,V,V,V,V,V,Q,V,V,V,V,V,V,V,Q,Q,W,
            W,W,W,W,W,W,W,W,W,W,M,W,W,W,W,W,W,W,W,W,W,
            W,Y,Y,R,G,G,G,G,R,G,V,G,R,G,G,G,G,R,Y,Y,W,
            W,Y,R,R,G,G,U,G,R,G,V,G,R,G,U,G,G,R,R,Y,W,
            W,R,R,R,R,R,R,R,R,R,C,R,R,R,R,R,R,R,R,R,W,
            W,Y,R,R,G,G,U,G,R,G,V,G,R,G,U,G,G,R,R,Y,W,
            W,Y,Y,R,G,G,G,G,R,G,V,G,R,G,G,G,G,R,Y,Y,W,
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,
    };
    private int stairs = 0;

    public void seal() {
        if (this.exit != 0) {
            super.seal();
            set(this.exit, 14);
            GameScene.updateMap(this.exit);
            GameScene.ripple(this.exit);
            this.stairs = this.entrance;
            this.entrance = 0;
        }
    }

    public void unseal() {
        if (this.stairs != 0) {
            super.unseal();
            this.exit = this.stairs;
            this.stairs = 0;
            set(this.exit, 7);
            GameScene.updateMap(this.exit);
        }
    }

    public SkyGooBossLevel() {
        this.color1 = 5459774;
        this.color2 = 12179041;
        this.viewDistance = 6;
    }

    protected boolean build() {
        setSize(21, 35);
        int exitCell = (this.width * 10) + 31;
        int entranceCell = (this.width * 10) + 11;

        LevelTransition enter = new LevelTransition(this, entranceCell, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(enter);

        LevelTransition exit = new LevelTransition(this, exitCell, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exit);
        map = pre_map.clone();
        return true;
    }

    protected void createItems() {
        drop(new PotionOfHealing(), (this.width) + 7);
        drop(new Food(), (this.width) + 13);
    }

    public Mob createMob() {
        return null;
    }

    protected void createMobs() {
        SkyGoo a = new SkyGoo();
        a.pos = (this.width * 17) + 10;
        this.mobs.add(a);
    }

    public int randomRespawnCell() {
        return this.entrance - width();
    }

    public Actor respawner() {
        return null;
    }

    public String tilesTex() {
        return "environment/tiles_sewers.png";
    }

    public String waterTex() {
        return "environment/water0.png";
    }
}

