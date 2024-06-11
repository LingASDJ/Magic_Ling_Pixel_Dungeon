package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CRYSTAL_DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CUSTOM_DECO;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ENTRANCE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.HIGH_GRASS;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.LOCKED_DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.LOCKED_EXIT;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.SIGN_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.STATUE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DwarfGeneral;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DwarfGeneralNPC;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.BlizzardBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.CausticBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.InfernalBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.ShockingBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.WaterSoul;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfAntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfChallenge;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSirensSong;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class DwarfGeneralBossLevel extends Level {

    {
        color1 = 0x801500;
        color2 = 0xa68521;
    }
    CustomTilemap visx = new townBehindX();
    private static final int WIDTH = 21;
    private static final int HEIGHT = 31;

    private static final int W = WALL;

    private static final int D = DOOR;

    private static final int E = EMPTY_SP;

    private static final int V = SIGN_SP;
    private static final int S = CUSTOM_DECO;
    private static final int R = HIGH_GRASS;

    private static final int C = CHASM;

    private static final int H = LOCKED_EXIT;
    private static final int M = CRYSTAL_DOOR;

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
            C,C,C,S,R,E,E,E,S,S,S,S,S,R,E,E,E,S,C,C,C,
            C,C,S,S,E,E,E,E,S,S,S,S,S,E,R,E,E,S,S,C,C,
            C,C,S,R,E,E,E,E,S,S,S,S,S,V,E,E,V,V,S,C,C,
            C,C,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,C,C,
            C,C,S,E,E,V,E,E,E,E,E,E,E,E,E,E,E,E,S,C,C,
            C,C,S,S,E,E,E,E,E,E,E,E,E,E,E,V,E,S,S,C,C,
            C,C,C,S,E,E,E,E,E,E,E,E,E,E,E,V,E,S,C,C,C,
            C,C,S,S,E,V,R,E,E,E,E,E,E,E,E,V,E,S,S,C,C,
            C,C,S,E,E,V,E,E,E,E,E,E,E,E,E,E,E,E,W,C,C,
            C,C,S,R,E,E,E,E,E,E,E,E,E,E,E,R,E,E,W,C,C,
            C,C,S,W,E,E,E,E,E,E,E,E,V,V,E,E,E,W,W,C,C,
            C,C,C,W,W,W,S,S,S,E,E,E,V,V,S,S,S,W,C,C,C,
            C,C,C,C,C,W,W,E,R,E,E,E,E,R,W,W,C,C,C,C,C,
            C,C,C,C,C,C,W,W,W,W,D,W,W,W,W,C,C,C,C,C,C,
            C,C,C,C,C,C,W,E,B,E,E,E,B,E,W,C,C,C,C,C,C,
            C,C,C,C,C,C,W,E,E,E,E,E,E,E,W,C,C,C,C,C,C,
            C,C,C,C,C,C,W,W,E,E,E,E,E,W,W,C,C,C,C,C,C,
            C,C,C,C,C,C,C,W,E,E,X,E,E,W,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,W,W,E,E,E,W,W,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
    };

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
    public void unseal() {
        super.unseal();
        CustomTilemap vis = new townBehind();
        vis.pos(0, 0);
        customTiles.remove(visx);
        customTiles.add(vis);

        Level.set(514, DOOR);
        GameScene.updateMap(514);
        Dungeon.observe();
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_CITY_CS;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_CITY;
    }


    //修复跳楼错误
    @Override
    public int randomRespawnCell( Char ch ) {
        int pos = 598; //random cell adjacent to the entrance.
        int cell;
        do {
            cell = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
        } while (!passable[cell]
                || (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
                || Actor.findChar(cell) != null);
        return cell;
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

        CustomTilemap via = new townAbove();
        via.pos(0, 0);
        customTiles.add(via);


        visx.pos(0, 0);
        customTiles.add(visx);

        map[exit] = Terrain.LOCKED_EXIT;

        return true;
    }

    public static class townBehind extends CustomTilemap {

        {
            texture = Assets.Environment.CITY_PSD;

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

    public static class townBehindX extends CustomTilemap {

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
        Item w = new Item();
        Ring r1;
        do {
            r1 = (Ring) Generator.random(Generator.Category.RING);
        }while(Challenges.isItemBlocked(w));
        r1.level(Random.Int(4));
        r1.cursed = false;
        r1.identify();
        drop(r1,  68).type = Heap.Type.BLACK;

        Wand r2;
        do {
            r2 = (Wand) Generator.random(Generator.Category.WAND);
        }while(Challenges.isItemBlocked(w));
        r2.level(Random.Int(2));
        r2.cursed = false;
        r2.identify();
        drop(r2,  70).type = Heap.Type.BLACK;

        MissileWeapon r3;
        do {
            r3 = (MissileWeapon) Generator.random(Generator.Category.MISSILE);
        }while(Challenges.isItemBlocked(w));
        r3.level(Random.Int(3));
        r3.cursed = false;
        r3.identify();
        drop(r3,  72).type = Heap.Type.BLACK;

        Weapon x3;
        do {
            x3 = (Weapon) Generator.random(Generator.Category.WEAPON);
        }while(Challenges.isItemBlocked(w));
        x3.level(Random.Int(2));
        x3.cursed = false;
        x3.identify();
        drop(x3,  74).type = Heap.Type.CRYSTAL_CHEST;

        Scroll x2;
        do {
            x2 = (Scroll) Generator.random(Generator.Category.SCROLL);
        }while(Challenges.isItemBlocked(w));
        x2.identify();
        drop(x2,  76).type = Heap.Type.CRYSTAL_CHEST;

        ScrollOfUpgrade x1;
        do {
            x1 = new ScrollOfUpgrade();
        }while(Challenges.isItemBlocked(w));
        x1.identify();
        drop(x1,  78).type = Heap.Type.CRYSTAL_CHEST;

        Potion w45;
        do {
            w45 = (Potion) Generator.random(Generator.Category.POTION);
        }while(Challenges.isItemBlocked(w));
        w45.quantity(Random.NormalIntRange(1,6));
        w45.cursed = false;
        w45.identify();
        drop(w45,  132).type = Heap.Type.CRYSTAL_CHEST;

        Weapon mw4 = (Weapon) Generator.random(Generator.Category.WEP_T4);
        mw4.level(Random.NormalIntRange(-1,3));
        mw4.cursed = true;
        mw4.identify();
        drop(mw4, 134).type = Heap.Type.LOCKED_CHEST;

        Item w1;
        switch (Random.Int(5)){
            default:
            case 0: w1 = new ScrollOfSirensSong(); break;
            case 1: w1 = new ScrollOfChallenge(); break;
            case 2: w1 = new ScrollOfMetamorphosis(); break;
            case 3: w1 = new ScrollOfAntiMagic();    break;
            case 4: w1 = new ScrollOfPsionicBlast();   break;
        }
        drop(w1, 136).type = Heap.Type.CHEST;

        Item w2;
        switch (Random.Int(5)){
            default:
            case 1: w2 = new WaterSoul();   break;
            case 2: w2 = new BlizzardBrew(); break;
            case 3: w2 = new CausticBrew();    break;
            case 4: w2 = new InfernalBrew();   break;
            case 5: w2 = new ShockingBrew();   break;
        }
        drop(w2, 138).type = Heap.Type.LOCKED_CHEST;

        Ring w5;
        do {
            w5 = (Ring) Generator.random(Generator.Category.RING);
        }while(Challenges.isItemBlocked(w));
        w5.level(Random.Int(2));
        w5.cursed = false;
        w5.identify();
        drop(w5,  140).type = Heap.Type.CRYSTAL_CHEST;
    }

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if(Dungeon.branch == 1 && Statistics.dwarfKill && SPDSettings.KillDwarf()) {
            TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
            if (timeFreeze != null) timeFreeze.disarmPresses();
            Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
            if (timeBubble != null) timeBubble.disarmPresses();
            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
            InterlevelScene.curTransition = new LevelTransition();
            InterlevelScene.curTransition.destDepth = 20;
            InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_ENTRANCE;
            InterlevelScene.curTransition.destBranch = 0;
            InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_ENTRANCE;
            InterlevelScene.curTransition.centerCell = -1;
            Game.switchScene(InterlevelScene.class);
            return false;
        } else if(Statistics.dwarfKill && !SPDSettings.KillDwarf()) {
            GLog.n(Messages.get(DwarfGeneral.class,"story"));
            return false;
        } else {
            return false;
        }
    }

}