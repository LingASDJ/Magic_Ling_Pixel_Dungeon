package com.shatteredpixel.shatteredpixeldungeon.levels.nosync;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RoseShiled;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.bossrush.Rival;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.HeavyBoomerang;
import com.shatteredpixel.shatteredpixeldungeon.levels.HallsLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.arenas.LastLevelArenas;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EmptyRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BurningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ChillingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ConfusionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ExplosiveTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.OozeTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PoisonDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ShockingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ToxicTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.Delayer;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
public class DeepShadowLevel extends Level {

    {
        color1 = 0x801500;
        color2 = 0xa68521;
    }

    public enum State {
        BRIDGE,
        PHASE_1,
        PHASE_2,
        PHASE_3,
        PHASE_4,
        PHASE_5,
        WON
    }

    private static final int JUMP_TIME = 1;

    private State state;
    private Rival rival;

    public State state(){
        return state;
    }

    //keep track of items to dump back after fight
    private ArrayList<Item> storedItems = new ArrayList<>();

    private int heroJumpPoint;
    private int rivalJumpPoint;

    private int pedestal;

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_COLD;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_CAVES;
    }

    @Override
    protected boolean build() {

        setSize(32, 64);
        Arrays.fill( map, Terrain.WALL );

        int mid = (width-1)/4;

        Painter.fill( this, 0, height-1, width, 1, Terrain.WALL );
        Painter.fill( this, mid - 1, 13, 3, (height-14), Terrain.EMPTY);
        Painter.fill( this, mid - 2, height - 3, 5, 1, Terrain.EMPTY);
        Painter.fill( this, mid - 3, height - 2, 7, 1, Terrain.EMPTY);

        Painter.fill( this, mid - 2, 12, 5, 7, Terrain.EMPTY);
        Painter.fill( this, mid - 3, 12, 7, 6, Terrain.EMPTY);

        pedestal = 15*(width()) + mid;
        map[pedestal] = Terrain.PEDESTAL;
        map[pedestal-1-width()] = map[pedestal+1-width()] = map[pedestal-1+width()] = map[pedestal+1+width()] = Terrain.STATUE_SP;

        int entranceC = (height-2) * width() + mid;

        LevelTransition enter = new LevelTransition(this, entranceC, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(enter);

        LevelTransition exit = new LevelTransition(this, 495, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exit);

        int pos;

        //place grass and bookshelves
        pos = pedestal;
        map[pos-3] = map[pos+3] = Terrain.HIGH_GRASS;
        pos -= width();
        map[pos-2] = map[pos+2] = map[pos-3] = map[pos+3] = Terrain.HIGH_GRASS;
        pos -= width();
        map[pos] = map[pos-1] = map[pos+1] = map[pos-2] = map[pos+2] = Terrain.HIGH_GRASS;
        map[pos-3] = map[pos+3] = Terrain.BOOKSHELF;
        pos -= width();
        map[pos] = map[pos-1] = map[pos+1] = Terrain.HIGH_GRASS;
        map[pos-2] = map[pos+2] = map[pos-3] = map[pos+3] = Terrain.BOOKSHELF;

        //place water
        pos = pedestal;
        map[pos-width()] = map[pos-1] = map[pos+1] = map[pos-2] = map[pos+2] = Terrain.WATER;
        pos += width();
        map[pos-2] = map[pos+2] = map[pos-3] = map[pos+3] = Terrain.WATER;
        pos += width();
        map[pos-2] = map[pos+2] = map[pos-3] = map[pos+3] = Terrain.WATER;
        pos += width();
        map[pos-2] = map[pos+2] = Terrain.WATER;

        for (int i=0; i < length(); i++) {
            if (map[i] == Terrain.EMPTY && Random.Int( 10 ) == 0) {
                map[i] = Terrain.EMPTY_DECO;
            }
        }

        state = State.BRIDGE;

        return true;
    }

    @Override
    public Mob createMob() {
        return null;
    }

    @Override
    protected void createMobs() {
        rival = new Rival();
        rival.pos = pedestal;
        mobs.add( rival );
    }

    public Actor respawner() {
        return null;
    }

    @Override
    protected void createItems() {}

    @Override
    public String tileName( int tile ) {
        switch (tile) {
            case Terrain.WATER:
                return Messages.get(HallsLevel.class, "water_name");
            case Terrain.GRASS:
                return Messages.get(HallsLevel.class, "grass_name");
            case Terrain.HIGH_GRASS:
                return Messages.get(HallsLevel.class, "high_grass_name");
            case Terrain.STATUE:
            case Terrain.STATUE_SP:
                return Messages.get(HallsLevel.class, "statue_name");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.WATER:
                return Messages.get(HallsLevel.class, "water_desc");
            case Terrain.STATUE:
            case Terrain.STATUE_SP:
                return Messages.get(HallsLevel.class, "statue_desc");
            case Terrain.BOOKSHELF:
                return Messages.get(HallsLevel.class, "bookshelf_desc");
            default:
                return super.tileDesc( tile );
        }
    }

    private static final String STATE	        = "state";
    private static final String RIVAL	        = "rival";
    private static final String STORED_ITEMS    = "storeditems";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( STATE, state );
        bundle.put( STORED_ITEMS, storedItems);
    }

    //    public void occupyCell(Char ch) {
//        super.occupyCell(ch);
//        GLog.p(String.valueOf(hero.pos));
//        GLog.b(String.valueOf(Statistics.zeroItemLevel));
//    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        state = bundle.getEnum( STATE, State.class );
        for (Bundlable item : bundle.getCollection(STORED_ITEMS)){
            storedItems.add( (Item)item );
        }
        //Statistics.doNotLookLing = false;
    }
    public void occupyCell(Char ch) {
        super.occupyCell(ch);
        //GLog.p(String.valueOf(hero.pos));

        if(rival == null && !Statistics.doNotLookLing){
            GLog.n(Messages.get(Rival.class,"mustone",hero.name()+"?"));
            Buff.prolong( hero, Paralysis.class, Paralysis.DURATION*50 );
            Buff.affect(hero, RoseShiled.class, 200000f);
            GameScene.scene.add(new Delayer(3f){
                @Override
                protected void onComplete() {

                    InterlevelScene.mode = InterlevelScene.Mode.RESET;
                    Game.switchScene(InterlevelScene.class);
                    Buff.detach(hero, Paralysis.class);
                    Buff.detach(hero, RoseShiled.class);
                }
            });

        }

        if (ch == hero && rival != null){
            //hero reaches amulet
            if (state == State.BRIDGE && (new EmptyRoom().set(7, 7, 23, 33)).inside(cellToPoint(ch.pos))){
                progress();
            }
            if (state == State.BRIDGE && (new EmptyRoom().set(6, 6, 24, 34)).inside(cellToPoint(ch.pos))){
                rival.notice();
            }
        }
    }

    public void progress() {
        switch(state) {
            case BRIDGE:
                changeMap(LastLevelArenas.randomPhase1Map());
                heroJumpPoint = 16 + 15 * width();
                rivalJumpPoint = 28 + 15 * width();

                doProgress(true);

                state = State.PHASE_1;
                break;
            case PHASE_1:
                changeMap(LastLevelArenas.randomPhase2Map());
                heroJumpPoint = 14 + 16 * width();
                rivalJumpPoint = 2 + 28 * width();

                doProgress(true);

                state = State.PHASE_2;
                break;
            case PHASE_2:
                changeMap(LastLevelArenas.randomPhase3Map());
                heroJumpPoint = 15 + 14 * width();
                rivalJumpPoint = 15 + 2 * width();

                doProgress(true);

                state = State.PHASE_3;
                break;
            case PHASE_3:
                changeMap(LastLevelArenas.randomPhase4Map());
                heroJumpPoint = 16 + 16 * width();
                rivalJumpPoint = 28 + 28 * width();

                doProgress(true);

                traps.clear();

                state = State.PHASE_4;
                break;
            case PHASE_4:
                changeMap(LastLevelArenas.randomPhase5Map());
                heroJumpPoint = 14 + 15 * width();
                rivalJumpPoint = 2 + 15 * width();

                doProgress(true);

                state = State.PHASE_5;
                break;
            case PHASE_5:
                changeMap(LastLevelArenas.randomWonMap());
                heroJumpPoint = 15 + 26 * width();
                rivalJumpPoint = 15 + 16 * width();

                doProgress(false);

                int entrance = 847;
                set( entrance, Terrain.ENTRANCE);
                GameScene.updateMap( entrance );
                transitions.add(new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE));

                int exitCell = 495;
                set( exitCell, Terrain.EXIT);
                GameScene.updateMap( exitCell );
                LevelTransition exit2 = new LevelTransition(this, exitCell, LevelTransition.Type.REGULAR_EXIT);
                transitions.add(exit2);

                GameScene.resetMap();

                rival.misc1.level = rival.misc1.level()/2;
                drop(rival.misc1, rivalJumpPoint );

                rival.misc2.level = rival.misc2.level()/2;
                drop(rival.misc2, rivalJumpPoint );

                rival.wand.level = rival.wand.level()*2;
                rival.wand.updateLevel();
                drop(rival.wand, rivalJumpPoint );

                for (Item item : storedItems)
                    drop( item, randomWonCell() );

                state = State.WON;
                break;
            case WON:
            default:
        }
    }

    private void doProgress(boolean alive) {
        GameScene.resetMap();

        boolean[] visited = Dungeon.level.visited;
        boolean[] discoverable = Dungeon.level.discoverable;
        for (int i = 0; i < Dungeon.level.length(); i++) {
            int terr = Dungeon.level.map[i];
            if (discoverable[i]) {
                visited[i] = true;
                if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {
                    Dungeon.level.discover( i );
                    if (Dungeon.level.heroFOV[i]) {
                        GameScene.discoverTile( i, terr );
                        discover( i );
                    }
                }
            }
        }

        Sample.INSTANCE.play(Assets.Sounds.ROCKS);
        GameScene.flash(0x66665D);
        Camera.main.shake( 4, 1f );
        for (int i = 0; i < Dungeon.level.length(); i++) {
            int r = Random.Int(6);
            if (Dungeon.level.heroFOV[i] && r == 0) {
                CellEmitter.get(i).start(Speck.factory(Speck.ROCK), 0.07f, 10);
            } else if (Dungeon.level.heroFOV[i] && r == 1) {
                CellEmitter.get(i).burst(Speck.factory(Speck.WOOL), 6 );
            }
        }

        hero.spendAndNext(JUMP_TIME);

        hero.sprite.jump( hero.pos, heroJumpPoint, new Callback() {
            @Override
            public void call() {
                hero.move( heroJumpPoint );

                Dungeon.observe();
                GameScene.updateFog();

                CellEmitter.center( heroJumpPoint ).burst(Speck.factory(Speck.DUST), 10);
                Camera.main.shake(2, 0.5f);
            }
        });

        if (alive) {
            rival.sprite.jump( rival.pos, rivalJumpPoint, new Callback() {
                @Override
                public void call() {
                    rival.move( rivalJumpPoint );
                    Dungeon.level.occupyCell(  rival );
                    if (Dungeon.level.heroFOV[rivalJumpPoint]) {
                        CellEmitter.center( rivalJumpPoint ).burst(Speck.factory(Speck.DUST), 10);
                    }
                }
            });
        }
    }

    public void changeMap(int[] map) {
        this.map = map.clone();
        buildFlagMaps();
        cleanWalls();

        //decoration
        for (int i=0; i < length(); i++) {
            if (map[i] == Terrain.EMPTY || map[i] == Terrain.WALL) {
                if (Random.Int( 6 ) == 0) {
                    if (map[i] == Terrain.EMPTY) {
                        map[i] = Terrain.EMPTY_DECO;
                    } else if (map[i] == Terrain.WALL) {
                        map[i] = Terrain.WALL_DECO;
                    }
                }
            }
        }

        BArray.setFalse(visited);
        BArray.setFalse(mapped);

        for (Blob blob: blobs.values()){
            blob.fullyClear();
        }
        addVisuals(); //this also resets existing visuals

        //place traps for phase 3
        traps.clear();
        for (int i = 0; i < length(); i++) {
            if (map[i] == Terrain.EMPTY) {
                Trap trap;
                int randomTrap = Random.chances(new float[]{2, 2, 2, 2, 1, 1, 1, 1});
                switch (randomTrap) {
                    default:
                    case 0:
                        trap = new BurningTrap();
                        break;
                    case 1:
                        trap = new ChillingTrap();
                        break;
                    case 2:
                        trap = new ShockingTrap();
                        break;
                    case 3:
                        trap = new OozeTrap();
                        break;
                    case 4:
                        trap = new ToxicTrap();
                        break;
                    case 5:
                        trap = new ConfusionTrap();
                        break;
                    case 6:
                        trap = new PoisonDartTrap();
                        break;
                    case 7:
                        trap = new ExplosiveTrap();
                        break;
                }

                trap.active = true;
                map[i] = Terrain.TRAP;
                setTrap(trap, trap.pos);
            }
        }

        //store excess items

        for (Heap heap : heaps.valueList()) {
            for (Item item : heap.items) {
                item.doPickUp(hero, hero.pos);
                heap.destroy();
            }
        }

        for (HeavyBoomerang.CircleBack b : hero.buffs(HeavyBoomerang.CircleBack.class)){
            storedItems.add(b.cancel());
        }

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob != rival){
                mob.destroy();
                if (mob.sprite != null)
                    mob.sprite.killAndErase();
            }
        }

        for (Plant plant : plants.values()){
            plants.remove(plant.pos);
        }

        GameScene.resetMap();
        Dungeon.observe();
    }

    private int randomWonCell() {
        int pos;
        do {
            pos = 495;

            //randomly assign a cell
            pos += Random.Int(6); //random width
            pos += Random.Int(13)*32; //random height

        } while (solid[pos]
                || map[pos] == Terrain.CHASM
                || map[pos] == Terrain.ENTRANCE);
        return pos;
    }

}
