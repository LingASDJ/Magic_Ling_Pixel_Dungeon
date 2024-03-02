package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DwarfMaster;
import com.shatteredpixel.shatteredpixeldungeon.custom.messages.M;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.LloydsBeacon;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.FireFishSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DwarfMasterBossLevel extends Level {

    {
        color1 = 0x48763c;
        color2 = 0x59994a;
    }

    private static final int WIDTH = 38;
    private static final int HEIGHT = 38;

    public static final int CENTER = 18 + WIDTH * 17;

    private int status = 0;
    private static final int START = 0;
    private static final int FIGHTING = 1;
    private static final int WON = 2;
    private static final int ERROR = 9999999;

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
    public String tilesTex() {
        return Assets.Environment.TILES_CITY;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_CITY;
    }


    private Mob getKing(){
        for (Mob m : mobs){
            if (m instanceof DwarfMaster) return m;
        }
        return null;
    }

    //四个基座
    private static final int[] pedestals = new int[4];

    //基座召唤基本逻辑
    public int getSummoningPos(){
        Mob king = getKing();
        HashSet<DwarfMaster.Summoning> summons = king.buffs(DwarfMaster.Summoning.class);
        ArrayList<Integer> positions = new ArrayList<>();
        for (int pedestal : pedestals) {
            boolean clear = true;
            for (DwarfMaster.Summoning s : summons) {
                if (s.getPos() == pedestal) {
                    clear = false;
                    break;
                }
            }
            if (clear) {
                positions.add(pedestal);
            }
        }
        if (positions.isEmpty()){
            return -1;
        } else {
            return Random.element(positions);
        }
    }

    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = codedMap.clone();

        int entrance = (this.width * 31) + 18;
        int exit = 94;

        LevelTransition enter = new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(enter);

        LevelTransition exits = new LevelTransition(this, exit, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exits);

        map[exit] = Terrain.LOCKED_EXIT;

        return true;
    }

    private static final Rect arena = new Rect(1, 25, 14, 38);

    private static final Rect end = new Rect(0, 0, 15, 22);

    private static final int topDoor = 7 + arena.top*15;

    public static final int throne;

    //常量基座调用
    static {
        throne = 15 + WIDTH * 20;

        pedestals[0] = 23 + WIDTH * 14;

        pedestals[1] = 14 + WIDTH * 14;

        pedestals[2] = 14 + WIDTH * 22;

        pedestals[3] = 23 + WIDTH * 22;

    }

    @Override
    protected void createMobs() {

    }


    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        //pre-1.3.0 saves, modifies exit transition with custom size
        status = bundle.getInt("level_status");
    }

    public Actor addRespawner() {
        return null;
    }

    //宝箱创建
    @Override
    protected void createItems() {
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

        MeleeWeapon mw = Generator.randomWeapon(11);
        mw.level(2);
        mw.cursed = false;
        mw.identify();
        drop(mw, 16 + WIDTH * 34).type = Heap.Type.LOCKED_CHEST;
        drop(new LloydsBeacon(), 18 + WIDTH * 34).type = Heap.Type.LOCKED_CHEST;
        Wand w;
        do {
            w = (Wand) Generator.random(Generator.Category.WAND);
        }while(Challenges.isItemBlocked(w));
        w.level(2);
        w.cursed = false;
        w.identify();
        drop(w, 20 + WIDTH * 34).type = Heap.Type.LOCKED_CHEST;

        Ring mw2;
        do {
            mw2 = (Ring) Generator.random(Generator.Category.RING);
        }while(Challenges.isItemBlocked(w));
        mw2.level(2);
        mw2.cursed = false;
        mw2.identify();
        drop(mw2, 16 + WIDTH * 36).type = Heap.Type.CRYSTAL_CHEST;

        Weapon w3;
        do {
            w3 = (Weapon) Generator.random(Generator.Category.WEP_T4);
        }while(Challenges.isItemBlocked(w));
        w3.level(2);
        w3.cursed = false;
        w3.identify();
        drop(w3, 18 + WIDTH * 36).type = Heap.Type.CRYSTAL_CHEST;


        mw.level(Random.NormalIntRange(1,4));
        mw.cursed = false;
        mw.identify();
        drop(new FireFishSword(), 20 + WIDTH * 36).type = Heap.Type.LOCKED_CHEST;

        Ring w5;
        do {
            w5 = (Ring) Generator.random(Generator.Category.RING);
        }while(Challenges.isItemBlocked(w));
        w5.level(Random.Int(4));
        w5.cursed = false;
        w5.identify();
        drop(w5,  26 + WIDTH * 24).type = Heap.Type.BLACK;

        Potion w45;
        Weapon w4;
        do {
            w45 = (Potion) Generator.random(Generator.Category.POTION);
        }while(Challenges.isItemBlocked(w));
        w45.quantity(Random.NormalIntRange(1,6));
        w45.cursed = false;
        w45.identify();
        drop(w45,  28 + WIDTH * 24).type = Heap.Type.BLACK;

        do {
            w4 = (Weapon) Generator.random(Generator.Category.WEP_T4);
        }while(Challenges.isItemBlocked(w));
        w4.level(Random.Int(5));
        w4.cursed = false;
        w4.identify();
        drop(w4,  30 + WIDTH * 24).type = Heap.Type.BLACK;

        Scroll w41;
        do {
            w41 = (Scroll) Generator.random(Generator.Category.SCROLL);
        }while(Challenges.isItemBlocked(w));
        w41.quantity(Random.NormalIntRange(1,4));
        w41.cursed = false;
        w41.identify();
        drop(w41,  32 + WIDTH * 24).type = Heap.Type.BLACK;

        do {
            w4 = (MissileWeapon) Generator.random(Generator.Category.MIS_T1);
        }while(Challenges.isItemBlocked(w));
        w4.level(Random.Int(6));
        w4.cursed = false;
        w4.identify();
        w4.quantity(Random.NormalIntRange(1,4));
        drop(w4,  34 + WIDTH * 24).type = Heap.Type.BLACK;

        //TWO
        Wand w51;
        do {
            w51 = (Wand) Generator.random(Generator.Category.WAND);
        }while(Challenges.isItemBlocked(w));
        w51.level(Random.Int(3));
        w51.cursed = false;
        w51.identify();
        drop(w5,  26 + WIDTH * 30).type = Heap.Type.LOCKED_CHEST;

        Scroll w6;
        do {
            w6 = (Scroll) Generator.random(Generator.Category.SCROLL);
        }while(Challenges.isItemBlocked(w));
        drop(w6,  28 + WIDTH * 30).type = Heap.Type.LOCKED_CHEST;
        w6.identify();

         Potion w7;
        do {
            w7 = (Potion) Generator.random(Generator.Category.POTION);
        }while(Challenges.isItemBlocked(w));
        w7.identify();
        drop(w7,  30 + WIDTH * 30).type = Heap.Type.LOCKED_CHEST;

        Ring mw3 = (Ring) Generator.random(Generator.Category.RING);
        mw3.level(Random.NormalIntRange(1,4));
        mw3.cursed = true;
        mw3.identify();
        drop(mw3, 32 + WIDTH * 30).type = Heap.Type.LOCKED_CHEST;

        Weapon mw4 = (Weapon) Generator.random(Generator.Category.WEP_T4);
        mw4.level(Random.NormalIntRange(-1,3));
        mw4.cursed = true;
        mw4.identify();
        drop(mw3, 34 + WIDTH * 30).type = Heap.Type.LOCKED_CHEST;

    }

    @Override
    public void occupyCell( Char ch ) {
        super.occupyCell(ch);
        int entrance = (this.width * 31) + 18;

        if (status == START && ch == Dungeon.hero) {
            progress();
            seal();
            CellEmitter.get( entrance ).start( FlameParticle.FACTORY, 0.1f, 10 );
        }

        if(ch == hero){
            if(MAIN_PORTAL.containsKey(ch.pos)) {
                ScrollOfTeleportation.appear(ch, IF_MAIN_PORTAL.get(ch.pos));
                hero.interrupt();
                Dungeon.observe();
                GameScene.updateFog();
            }
        }
    }

    private static final HashMap<Integer, Integer> IF_MAIN_PORTAL = new HashMap<>(4);
    {
        IF_MAIN_PORTAL.put(26 + WIDTH * 25, WIDTH*31 + 18);
        IF_MAIN_PORTAL.put(31 + WIDTH * 6, WIDTH*31 + 18);
        IF_MAIN_PORTAL.put(6 + WIDTH * 6, WIDTH*31 + 18);
        IF_MAIN_PORTAL.put(12 + WIDTH * 23, WIDTH*31 + 18);
        IF_MAIN_PORTAL.put(26 + WIDTH * 31, WIDTH*31 + 18);
    }

    private static final HashMap<Integer, Integer> MAIN_PORTAL = new HashMap<>(4);
    {
        MAIN_PORTAL.put(26 + WIDTH * 25, WIDTH*31 + 18);
        MAIN_PORTAL.put(31 + WIDTH * 6, WIDTH*31 + 18);
        MAIN_PORTAL.put(6 + WIDTH * 6, WIDTH*31 + 18);
        MAIN_PORTAL.put(12 + WIDTH * 23, WIDTH*31 + 18);
        MAIN_PORTAL.put(26 + WIDTH * 31, WIDTH*31 + 18);
    }

    @Override
    public void seal() {
        super.seal();

        DwarfMaster boss = new DwarfMaster();
        boss.pos = CENTER;
        GameScene.add(boss);

        Level.set(entrance(), Terrain.EMPTY);
        GameScene.updateMap(entrance());
        Dungeon.observe();

        GLog.p(Messages.get(this,"dead_go"));
        Sample.INSTANCE.play(Assets.Sounds.DEATH);
    }

    @Override
    public void unseal() {
        super.unseal();

        set( entrance(), Terrain.ENTRANCE );
        GameScene.updateMap( entrance() );
        Dungeon.observe();
    }

    @Override
    public String tileName(int tile) {
        switch (tile) {
            case Terrain.WATER:
                return M.L(CityLevel.class, "water_name");
            case Terrain.GRASS:
                return M.L(CityLevel.class, "grass_name");
            case Terrain.HIGH_GRASS:
                return M.L(CityLevel.class, "high_grass_name");
            case Terrain.STATUE:
                return M.L(CityLevel.class, "statue_name");
            case Terrain.WELL:
                return M.L(DwarfMasterBossLevel.class, "well_name");
            default:
                return super.tileName(tile);
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.WATER:
                return M.L(CityLevel.class, "water_desc");
            case Terrain.STATUE:
            case Terrain.STATUE_SP:
                return M.L(CityLevel.class, "statue_desc");
            case Terrain.BOOKSHELF:
                return M.L(CityLevel.class, "bookshelf_desc");
            case Terrain.WELL:
                return M.L(DwarfMasterBossLevel.class, "well_desc");
            default:
                return super.tileDesc(tile);
        }
    }

    //Fixed
    private static final int T = Terrain.WELL;

    private static final int A = WALL;
    private static final int R = Terrain.BOOKSHELF;
    private static final int L = Terrain.EMPTY;
    private static final int S = Terrain.EMPTY_SP;
    private static final int F = Terrain.WATER;
    private static final int X = Terrain.LOCKED_DOOR;
    private static final int B = Terrain.DOOR;
    private static final int P = Terrain.STATUE_SP;
    private static final int J = Terrain.PEDESTAL;
    private static final int O = Terrain.STATUE;
    private static final int N = Terrain.ENTRANCE;
    private static final int D = Terrain.LOCKED_EXIT;

    private static final int[] codedMap = new int[]{
            A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,
            A,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,L,L,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,A,
            A,R,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,D,B,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,R,A,
            A,R,A,R,R,R,R,R,R,R,R,R,R,R,R,R,R,X,L,L,X,R,R,R,R,R,R,R,R,R,R,R,R,R,R,A,R,A,
            A,R,A,R,L,L,L,L,L,L,L,R,R,R,R,A,A,A,B,B,A,A,A,R,R,R,R,L,L,L,L,L,L,L,R,A,R,A,
            A,R,A,R,L,L,L,L,L,L,R,R,A,A,A,A,L,L,L,L,L,L,A,A,A,A,R,R,L,L,L,L,L,L,R,A,R,A,
            A,R,A,R,L,L,T,L,R,R,R,A,A,L,L,L,L,L,L,L,L,L,L,L,L,A,A,R,R,R,L,T,L,L,R,A,R,A,
            A,R,A,R,L,L,L,R,R,A,A,A,L,P,L,L,L,L,L,L,L,L,L,L,P,L,A,A,A,R,R,L,L,L,R,A,R,A,
            A,R,A,R,L,L,R,R,A,A,L,L,L,L,S,S,S,S,S,S,S,S,S,S,L,L,L,L,A,A,R,L,L,L,R,A,R,A,
            A,R,A,R,L,L,R,A,A,L,L,L,L,L,S,F,F,F,F,F,F,F,F,S,L,L,L,L,L,A,R,R,L,L,R,A,R,A,
            A,R,A,R,L,R,R,A,A,L,L,L,L,L,S,F,F,A,F,F,A,F,F,S,L,L,L,L,L,A,A,R,R,L,R,A,R,A,
            A,R,A,R,L,R,A,A,L,L,S,S,L,L,S,F,F,A,F,F,A,F,F,S,L,L,S,S,L,L,A,A,R,L,R,A,R,A,
            A,R,A,R,L,R,A,L,L,S,L,L,S,L,S,F,F,A,F,F,A,F,F,S,L,S,L,L,S,L,L,A,R,L,R,A,R,A,
            A,R,A,R,R,R,A,L,L,S,L,L,S,L,S,F,F,A,F,F,A,F,F,S,L,S,L,L,S,L,L,A,R,R,R,A,R,A,
            A,R,A,R,R,A,A,L,L,L,S,S,L,L,J,S,F,F,F,F,F,F,S,J,L,L,S,S,L,L,L,A,A,R,R,A,R,A,
            A,R,A,R,R,A,L,L,L,L,L,L,L,L,L,L,S,F,F,F,F,S,L,L,L,L,L,L,L,L,L,L,A,R,R,A,R,A,
            A,R,A,R,R,A,L,L,L,L,L,L,L,L,L,L,L,S,F,F,S,L,L,L,L,L,L,L,L,L,L,L,A,R,R,A,R,A,
            A,R,A,R,A,A,L,L,L,L,L,L,L,L,L,L,L,L,S,S,L,L,L,L,L,L,L,L,L,L,L,L,A,A,R,A,R,A,
            A,R,A,L,L,A,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,A,L,L,A,R,A,
            A,R,A,A,A,A,O,L,L,L,L,L,L,L,L,L,L,L,S,S,L,L,L,L,L,L,L,L,L,L,L,O,A,A,A,A,R,A,
            A,R,A,A,A,A,L,L,L,L,A,A,A,A,A,L,L,S,L,L,S,L,L,A,A,A,A,A,L,L,L,L,A,A,A,A,R,A,
            A,R,A,A,A,A,L,L,L,L,L,L,L,L,L,L,S,L,L,L,L,S,L,L,L,L,L,L,L,L,L,L,A,A,A,A,R,A,
            A,R,A,A,A,A,A,A,A,A,A,A,A,A,J,S,L,L,L,L,L,L,S,J,A,A,A,A,A,A,A,X,A,A,A,A,A,A,
            A,R,L,L,L,L,L,L,L,L,L,L,T,A,S,A,L,A,S,S,A,L,A,S,A,L,L,L,L,L,L,L,L,L,L,L,A,A,
            A,R,L,L,L,L,L,L,L,L,L,L,L,A,S,A,S,A,S,S,A,S,A,S,A,L,L,L,L,L,L,L,L,L,L,L,A,A,
            A,R,L,L,L,L,L,L,L,L,L,L,L,A,S,A,S,A,S,S,A,S,A,S,A,L,T,L,L,L,L,L,L,L,L,L,R,A,
            A,R,L,L,L,L,L,L,L,L,L,L,L,A,S,A,S,A,S,S,A,S,A,S,A,A,A,A,A,A,A,A,A,A,A,A,R,A,
            A,R,L,L,L,L,L,L,L,L,L,L,L,A,S,A,S,A,S,S,A,S,A,S,A,A,A,A,A,A,A,A,A,A,R,R,R,A,
            A,R,L,L,L,L,L,L,L,L,L,L,L,A,S,S,S,S,S,S,S,S,S,S,A,A,A,A,A,A,A,A,A,A,R,A,A,A,
            A,R,L,L,L,L,L,L,L,L,L,L,L,A,L,L,L,L,L,L,L,L,L,L,A,L,L,L,L,L,L,L,L,L,L,L,A,A,
            A,R,L,L,L,L,L,L,L,L,L,L,L,A,L,L,L,L,L,L,L,L,L,L,A,L,L,L,L,L,L,L,L,L,L,L,A,A,
            A,R,L,L,L,L,L,L,L,L,L,L,L,A,A,A,L,L,N,L,L,L,A,A,A,L,T,L,L,L,L,L,L,L,L,L,A,A,
            A,R,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,X,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,
            A,R,A,A,A,A,A,A,A,A,A,A,A,A,A,A,S,S,S,S,S,S,A,A,A,A,A,A,A,A,A,A,A,A,A,A,R,A,
            A,R,A,A,A,A,A,A,A,A,A,A,A,A,A,A,S,S,S,S,S,S,A,A,A,A,A,A,A,A,A,A,A,A,A,A,R,A,
            A,R,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,R,A,
            A,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,L,L,L,L,L,L,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,A,
            A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,
    };


}
