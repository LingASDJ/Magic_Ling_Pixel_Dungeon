package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.LOCKED_DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.PEDESTAL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.STATUE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.STATUE_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WELL;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;

public class NewHeadLevel extends Level {

    private static final int A = WALL;
    private static final int B = EMPTY_SP;
    private static final int C = CHASM;
    private static final int D = STATUE_SP;
    private static final int E = STATUE;
    private static final int F = EMPTY;
    private static final int G = DOOR;
    private static final int J = Terrain.ENTRANCE;
    private static final int X = PEDESTAL;
    private static final int R = LOCKED_DOOR;
    private static final int Z = WELL;

    private static final int[] codedMap = new int[]{
            A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,
            A,F,J,F,A,C,C,C,C,C,A,B,B,B,B,A,
            A,F,B,F,A,A,A,A,A,C,A,B,F,F,B,A,
            A,A,G,A,A,D,X,D,A,C,A,G,A,F,B,A,
            A,E,B,E,A,X,B,X,A,C,A,B,A,F,B,A,
            A,F,B,F,A,D,X,D,A,C,A,B,A,F,B,A,
            A,E,B,E,A,A,A,A,A,A,A,G,A,F,B,A,
            A,F,B,F,A,D,B,B,D,A,F,B,A,F,B,A,
            A,E,B,E,A,B,B,B,B,A,E,B,A,F,B,A,
            A,F,B,F,A,D,B,B,D,A,F,B,A,F,B,A,
            A,E,B,E,A,F,F,F,F,A,E,B,A,F,B,A,
            A,F,B,B,G,F,F,F,F,G,B,B,A,B,B,A,
            A,A,A,A,A,D,B,B,D,A,A,A,A,R,A,A,
            A,Z,A,A,A,B,B,B,B,A,F,B,F,B,F,A,
            A,B,A,A,A,D,B,B,D,A,F,B,F,B,F,A,
            A,B,A,A,A,A,A,A,A,A,F,B,F,B,F,A,
            A,B,A,B,B,B,B,B,B,A,F,B,F,B,F,A,
            A,B,A,B,B,B,B,B,B,R,F,B,F,B,F,A,
            A,R,A,A,A,A,A,A,A,A,A,G,A,G,A,A,
            A,B,F,F,F,A,F,F,F,F,A,B,F,B,F,A,
            A,B,F,F,F,A,F,F,F,F,A,B,F,B,F,A,
            A,B,B,B,B,R,B,B,B,B,R,B,B,B,F,A,
            A,F,F,F,F,A,F,F,F,F,A,F,F,F,F,A,
            A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,

    };

    {
        color1 = 5459774;
        color2 = 12179041;
    }

    private int mapToTerrain(int var1) {
        if (var1 == 1 || var1 == 2 || var1 == 3) {
            return 29;
        }
        if (var1 != 4) {
            if (var1 == 16) {
                return 7;
            }
            if (var1 == 17) {
                return 8;
            }
            switch (var1) {
                case -2147483644:
                    break;
                case -2147483584:
                case 64:
                case 190:
                    return 4;
                case -2147483550:
                case 98:
                    return 25;
                case -2147483524:
                case 124:
                case 140:
                    return 27;
                case 4:
                    return 14;
                case 69:
                    return 12;
                case 80:
                    return 5;
                case 85:
                    return 11;
                case 96:
                    return 23;
                case 120:
                    return 20;
                case 123:
                    return 29;
                case 161:
                    return 12;
                default:
                    return 1;
            }
        }
        return 14;
    }

    protected boolean build() {
        setSize(16, 24);
        this.exit = (this.width * 18 + 18);
        this.entrance = (this.width * 2) + 3;
        for (int var1 = 0; var1 < this.map.length; var1++) {
            map = codedMap.clone();
        }
        return true;
    }

    protected void createItems() {
    }

    public Mob createMob() {
        return null;
    }

    protected void createMobs() {
    }

    public int randomRespawnCell() {
        return this.entrance - width();
    }

    public Actor respawner() {
        return null;
    }

    public String tilesTex() {
        return Assets.Environment.TILES_COLD;
    }

    public String waterTex() {
        return Assets.Environment.WATER_COLD;
    }

}

