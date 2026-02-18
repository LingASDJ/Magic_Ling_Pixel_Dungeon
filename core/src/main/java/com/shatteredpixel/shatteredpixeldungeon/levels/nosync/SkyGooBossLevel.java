package com.shatteredpixel.shatteredpixeldungeon.levels.nosync;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.BARRICADE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.BOOKSHELF;
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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.bossrush.SkyGoo;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

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
    private static final int L = BARRICADE;

    private static final int A = BOOKSHELF;


    private static final int[] pre_map = {
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,
            W,G,G,G,G,G,G,G,Q,A,E,A,Q,G,G,G,G,G,G,G,W,
            W,V,V,L,V,V,V,L,V,V,A,V,V,L,V,V,V,L,V,V,W,
            W,G,L,L,L,G,G,L,G,A,Q,A,G,L,G,G,L,L,L,G,W,
            W,V,V,L,V,V,V,L,Q,Q,Q,Q,Q,L,V,V,V,L,V,V,W,
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
        super.seal();
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
        super.unseal();
        if (this.stairs != 0) {
            this.exit = this.stairs;
            this.stairs = 0;
            set(this.exit, 7);
            GameScene.updateMap(this.exit);

            set(484, DOOR);
            set(598, DOOR);
            set(502, DOOR);
            GameScene.updateMap(484);
            GameScene.updateMap(598);
            GameScene.updateMap(502);
        }
    }

    public SkyGooBossLevel() {
        this.color1 = 5459774;
        this.color2 = 12179041;
        this.viewDistance = 6;
    }

    @Override
    public void playLevelMusic(){
        Music.playModeBGM(Assets.Music.JUNGLE_FOREST,true);
    }

    @Override
    public void playBossMusic(){
        Game.runOnRenderThread(() -> Music.INSTANCE.fadeOut(5f,
                () -> Music.playModeBGM(Assets.Music.BGM_BOSSA,true)));
    }

    protected boolean build() {
        setSize(21, 35);
        int exitCell = 661;
        int entranceCell = 31;

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

        ArrayList<Item> bonesItems = Bones.get();
        if (bonesItems != null) {
            int pos;
            do {
                pos = randomRespawnCell(null);
            } while (pos == entrance());
            for (Item i : bonesItems) {
                drop(i, pos).setHauntedIfCursed().type = Heap.Type.REMAINS;
            }
        }
        Random.popGenerator();
    }

    public Mob createMob() {
        return null;
    }

    protected void createMobs() {
        SkyGoo a = new SkyGoo();
        a.pos = 94;
        this.mobs.add(a);
    }

    @Override
    public void occupyCell( Char ch ) {
        super.occupyCell(ch);
        if (ch instanceof Hero) {
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                if (!(mob.alignment == Char.Alignment.ENEMY)){
                    if(map[484] == CRYSTAL_DOOR){
                        set(484, DOOR);
                    }
                    if(map[598] == LOCKED_DOOR){
                        set(598, DOOR);
                    }
                    if(map[502] == CRYSTAL_DOOR){
                        set(502, DOOR);
                    }
                    GameScene.updateMap(484);
                    GameScene.updateMap(598);
                    GameScene.updateMap(502);
                }
            }
        }
    }

    @Override
    public int randomRespawnCell( Char ch ) {
        int pos = 31;
        int cell;
        do {
            cell = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
        } while (!passable[cell]
                || (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
                || Actor.findChar(cell) != null);
        return cell;
    }

    public String tilesTex() {
        return "environment/tiles_sewers.png";
    }

    public String waterTex() {
        return "environment/water0.png";
    }
}

