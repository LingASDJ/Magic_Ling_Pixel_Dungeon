package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ALCHEMY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ENTRANCE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EXIT;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.SIGN;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GameRules;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.LanFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Nyz;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.PinkLing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.BzmdrLand;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.DeepSea;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.DreamLezi;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.Gudazi;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.HollowKnight;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.JIT;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.KongFu;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.LuoWhite;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.Mint;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.MoRuoS;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.MoonCat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.MoonLow;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.PianoLe;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.SmallLeaf;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.WaloKe;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.WhiteLingLand;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.XiaYuan;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.YetYog;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.Zako;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.ZeroDreamShop;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.ArchettoNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.normal.AG;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.normal.Choco;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.normal.DogDogMusic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.normal.MagicSheep;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.normal.PinkFox;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.normal.RainNight;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.normal.SliceDream;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.normal.SmallBlue;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.dlcitem.BossRushBloodGold;
import com.shatteredpixel.shatteredpixeldungeon.items.dlcitem.DLCItem;
import com.shatteredpixel.shatteredpixeldungeon.items.dlcitem.RushMobScrollOfRandom;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.BlizzardBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.CausticBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.InfernalBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.ShockingBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.WaterSoul;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.SakaFishSketon;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RedBloodMoon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.ClearSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.DiedCrossBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.ForestBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.GoldLongGun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.MoonDao;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.RiceSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.SaiPlus;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SurfaceScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndHardNotification;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.noosa.Game;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Callback;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class NormalZeroFiveLevel extends Level {

    {
        PaswordBadges.loadGlobal();
        color1 = 5459774;
        color2 = 12179041;
        viewDistance = 100;
    }

    public List<PaswordBadges.Badge> passwordbadges = PaswordBadges.filtered(true);

    /**
     * 随机生成怪物组
     * @param posArray 位置数组
     * @param mobArray 怪物Class数组
     * @param mobs 输出的怪物列表
     */
    public void generateRandomMobGroup(int[] posArray, Class<? extends Mob>[] mobArray, HashSet<Mob> mobs) {

        List<Integer> posList = new ArrayList<>();
        for (int num : posArray) posList.add(num);
        Random.shuffle(posList);


        List<Class<? extends Mob>> mobList = new ArrayList<>(Arrays.asList(mobArray));
        Random.shuffle(mobList);

        try {
            Mob m1 = mobList.get(0).getDeclaredConstructor().newInstance();
            m1.pos = posList.get(0);
            mobs.add(m1);

            Mob m2 = mobList.get(1).getDeclaredConstructor().newInstance();
            m2.pos = posList.get(1);
            mobs.add(m2);
        } catch (Exception ignored) {}
    }

    private static final int S = SIGN;
    private static final int W = WALL;
    private static final int E = EMPTY_SP;
    private static final int D = DOOR;
    private static final int X = EXIT;
    private static final int Y = ENTRANCE;
    private static final int A = ALCHEMY;
    private static final int T = WATER;
    private static final int J = CHASM;

    private static final int[] codedMap = {
            W,W,W,W,W,W,W,W,W,S,S,S,S,S,S,S,W,W,W,W,W,W,W,W,W,
            W,W,W,W,W,W,W,W,S,S,S,S,Y,S,S,S,S,W,W,W,W,W,W,W,W,
            W,W,W,W,W,W,W,W,S,S,S,E,E,E,S,S,S,W,W,W,W,W,W,W,W,
            W,W,W,W,W,W,W,W,S,S,E,E,E,E,E,S,S,W,W,W,W,W,W,W,W,
            W,W,W,W,W,W,W,W,S,S,E,E,E,E,E,S,S,W,W,W,W,W,W,W,W,
            W,W,W,W,W,W,W,W,S,S,E,E,E,E,E,S,S,W,W,W,W,W,W,W,W,
            W,W,W,W,W,W,W,W,S,S,E,E,E,E,E,S,S,W,W,W,W,W,W,W,W,
            W,W,W,W,W,W,W,W,S,S,E,E,E,E,E,S,S,W,W,W,W,W,W,W,W,
            W,W,W,W,W,W,W,W,W,W,W,W,E,W,W,W,W,W,W,W,W,W,W,W,W,
            W,W,W,W,W,W,W,W,W,W,W,W,D,W,W,W,W,W,W,W,W,W,W,W,W,
            W,E,E,E,E,E,W,E,E,E,E,E,E,E,E,S,S,S,W,W,W,W,W,W,W,
            W,E,E,E,E,E,W,S,S,S,S,E,E,E,E,E,E,E,W,S,E,S,S,W,W,
            W,E,E,E,E,E,D,E,E,E,E,E,E,E,E,E,E,E,D,E,E,E,E,W,W,
            W,E,E,E,E,E,W,E,E,S,S,E,E,E,S,S,E,E,W,S,S,S,S,W,W,
            W,E,E,E,E,E,W,E,E,S,S,E,E,E,S,S,E,E,W,W,W,W,W,W,W,
            W,W,W,E,W,W,W,E,E,E,E,E,E,E,E,E,E,E,S,S,W,W,W,W,W,
            W,W,W,D,W,W,W,E,E,S,S,E,E,E,S,S,E,E,S,Y,S,W,W,W,W,
            W,E,E,E,E,E,W,E,E,S,S,E,E,E,S,S,E,E,E,S,S,W,W,W,W,
            W,E,E,E,E,E,D,E,E,E,E,E,E,E,E,E,E,E,W,W,S,S,W,W,W,
            W,E,E,E,E,E,W,E,E,E,E,E,E,E,E,E,E,E,W,S,S,S,S,S,W,
            W,W,E,E,E,W,W,W,W,W,W,E,E,E,W,W,W,W,W,S,S,S,S,S,W,
            W,W,W,W,W,W,S,S,S,S,W,E,E,E,W,S,S,S,S,E,E,E,S,S,S,
            W,W,W,W,W,S,S,S,S,S,W,E,E,E,W,E,E,E,E,E,E,E,E,S,S,
            W,W,W,W,W,S,S,S,S,S,W,W,E,W,W,E,E,E,E,E,E,E,E,S,S,
            W,W,W,W,S,S,E,E,E,S,S,W,D,W,S,E,E,E,E,E,E,E,E,E,S,
            W,W,W,S,S,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,
            W,W,W,S,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,
            W,W,W,S,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,W,W,W,W,
            W,W,W,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,W,W,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,W,W,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,A,W,
            W,W,W,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,E,E,E,W,
            W,W,W,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,W,W,W,W,
            W,W,W,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,S,S,S,
            W,W,W,S,S,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,S,T,T,S,S,
            W,W,W,S,S,S,S,S,S,E,E,S,S,E,E,E,S,T,T,T,T,T,T,S,S,
            W,W,W,S,S,S,S,S,S,S,S,S,S,E,E,E,S,T,T,T,T,T,T,S,S,
            W,W,W,S,S,S,S,S,S,S,S,E,S,E,E,E,S,T,T,T,T,T,S,S,S,
            W,W,W,S,S,S,S,S,S,S,S,X,E,E,E,E,S,S,S,S,S,S,S,S,S,
            W,W,W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            W,W,W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            W,W,W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            W,W,W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
    };

    private static final int WIDTH = 25;
    private static final int HEIGHT = 43;


    protected boolean build() {
        setSize(WIDTH, HEIGHT);

        int exitCell = 37;
        LevelTransition exit = new LevelTransition(this, exitCell, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exit);

        int exitCellX = 595;
        LevelTransition exitX = new LevelTransition(this, exitCellX, LevelTransition.Type.DOUBLE_ENTRANCE);
        transitions.add(exitX);

        LevelTransition bexits = new LevelTransition(this, 419, LevelTransition.Type.BRANCH_ENTRANCE);
        transitions.add(bexits);

        int enterCell = 961;
        LevelTransition enter = new LevelTransition(this, enterCell, LevelTransition.Type.SURFACE);
        transitions.add(enter);

        CustomTilemap via = new townAbove();
        via.pos(0, 0);
        customTiles.add(via);

        map = codedMap.clone();
        return true;
    }

    /** 渡口坐标 */
    public static int[] Ferry_Tiled = new int[]{
            863,864,865,
            888,889,890,891,
            913,914,915,916,
            938,939,940,941,
            962,963,964,965,966,
    };

    /** 小船坐标*/
    public static int[] Boat_Tiled = new int[]{
            961,936,937,935
    };

    /** 金币坐标*/
    public static int[] Golden_Tiled = new int[]{
            606,607,608,
            631,632,633,634,
            656,657,658,659
    };

    /** 桌子*/
    public static int[] Desktop1_Tiled = new int[]{
            334,335,
            359,360,
    };

    public static int[] Desktop2_Tiled = new int[]{
            339,340,
            364,365,
    };

    public static int[] Desktop3_Tiled = new int[]{
            409,410,
            434,435,
    };

    public static int[] Desktop4_Tiled = new int[]{
            414,415,
            439,440,
    };

    /** 魔法阵坐标*/
    public static int[] Magic_Tiled = new int[]{
            569,570,571,
            594,595,596,
            619,620,621,
    };

    public static class townAbove extends CustomTilemap {

        {
            texture = Assets.Environment.JORCT_ZERO;

            tileW = WIDTH;
            tileH = HEIGHT;
        }

        final int TEX_WIDTH = WIDTH*16;

        @Override
        public Tilemap create() {

            Tilemap v = super.create();

            int[] data = mapSimpleImage(0, 0, TEX_WIDTH);

            v.map(data, tileW);
            return v;
        }

        @Override
        public String name(int tileX, int tileY) {
            int checkTiledID = tileY*WIDTH + tileX;
            for (int i : Ferry_Tiled) {
                if(i == checkTiledID){
                    return Messages.get(this, "ferry_name");
                }
            }
            for (int i : Boat_Tiled) {
                if(i == checkTiledID){
                    return Messages.get(this, "boat_name");
                }
            }
            for (int i : Golden_Tiled) {
                if(i == checkTiledID){
                    return Messages.get(this, "golden_name");
                }
            }
            for (int i : Desktop1_Tiled) {
                if(i == checkTiledID){
                    return Messages.get(this, "desktop1_name");
                }
            }
            for (int i : Desktop2_Tiled) {
                if(i == checkTiledID){
                    return Messages.get(this, "desktop2_name");
                }
            }
            for (int i : Desktop3_Tiled) {
                if(i == checkTiledID){
                    return Messages.get(this, "desktop3_name");
                }
            }
            for (int i : Desktop4_Tiled) {
                if(i == checkTiledID){
                    return Messages.get(this, "desktop4_name");
                }
            }
            for (int i : Magic_Tiled) {
                if(i == checkTiledID){
                    return Messages.get(this, "magic_name");
                }
            }
            if(checkTiledID == 37){
                return Messages.get(this, "enter_name");
            }
            return super.desc(tileX,tileY);
        }

        @Override
        public String desc(int tileX, int tileY) {
            int checkTiledID = tileY*WIDTH + tileX;
            for (int i : Ferry_Tiled) {
                if(i == checkTiledID){
                    return Messages.get(this, "ferry_desc");
                }
            }
            for (int i : Boat_Tiled) {
                if(i == checkTiledID){
                    return Messages.get(this, "boat_desc");
                }
            }
            for (int i : Golden_Tiled) {
                if(i == checkTiledID){
                    return Messages.get(this, "golden_desc");
                }
            }
            for (int i : Desktop1_Tiled) {
                if(i == checkTiledID){
                    return Messages.get(this, "desktop1_desc");
                }
            }
            for (int i : Desktop2_Tiled) {
                if(i == checkTiledID){
                    return Messages.get(this, "desktop2_desc");
                }
            }
            for (int i : Desktop3_Tiled) {
                if(i == checkTiledID){
                    return Messages.get(this, "desktop3_desc");
                }
            }
            for (int i : Desktop4_Tiled) {
                if(i == checkTiledID){
                    return Messages.get(this, "desktop4_desc");
                }
            }
            for (int i : Magic_Tiled) {
                if(i == checkTiledID){
                    return Messages.get(this, "magic_desc");
                }
            }
            if(checkTiledID == 37){
                return Messages.get(this, "enter_desc");
            }
            return super.desc(tileX,tileY);
        }
    }

    public static int[] SALEPOS_TWO = new int[]{
            252,352
    };

    public static int[] SALEPOS_THREE = new int[]{
            277,279,327,329
    };

    public static int[] SALEPOS_FOUR = new int[]{
            326,276
    };

    protected void createItems() {
        PaswordBadges.loadGlobal();
        List<PaswordBadges.Badge> passwordbadges = PaswordBadges.filtered(true);
    }

    public Mob createMob() {
        return null;
    }

    /* 第1桌组 */
    public int[] DESKTOP_ONE = {333,336,383,386};

    public Class<? extends Mob>[] DESKTOP_1_MOBS = new Class[]{
            RainNight.class,
            HollowKnight.class,
            DreamLezi.class
    };

    /* 第2桌组 */
    public int[] DESKTOP_TWO = {338,389,366,315};

    public Class<? extends Mob>[] DESKTOP_2_MOBS = new Class[]{
            MoonCat.class,
            Zako.class,
            PinkFox.class,
            MagicSheep.class
    };

    /* 第3桌组 */
    public int[] DESKTOP_THREE = {460,459,433,436};

    public Class<? extends Mob>[] DESKTOP_3_MOBS = new Class[]{
            Statistics.moonlowgetAloneRoom ? MoonLow.class : null,
            DeepSea.class,
            PinkLing.class,
            passwordbadges.contains(PaswordBadges.Badge.GOOD_BLUE) ? SmallBlue.class : null
    };

    public int[] CAT_POS = {261,263,561,563,487,412,362};

    public Class<? extends Mob>[] CAT_MOBS = new Class[]{
            Random.Float() >= 0.7f ? Mint.class : null
    };

    protected void createMobs() {

        if(!Statistics.moonlowgetAloneRoom){
            MoonLow ml = new MoonLow();
            ml.pos = Random.Float()>0.5f ? 722 : 720;
            mobs.add(ml);
        }

        /** 篝火组 **/
        Gudazi gdz = new Gudazi();
        gdz.pos = 781;
        mobs.add(gdz);

        LanFire lf = new LanFire();
        lf.pos = 807;
        mobs.add(lf);

        XiaYuan xy = new XiaYuan();
        xy.pos = 783;
        mobs.add(xy);

        BzmdrLand bzmdrLand = new BzmdrLand();
        bzmdrLand.pos = 831;
        mobs.add(bzmdrLand);

        JIT jt = new JIT();
        jt.pos = 833;
        mobs.add(jt);

        /** 岸边组 **/
        KongFu kf = new KongFu();
        kf.pos = 845;
        mobs.add(kf);

        /** 商人组 **/
        ZeroDreamShop zdr = new ZeroDreamShop();
        zdr.pos = 295;
        mobs.add(zdr);

        MoRuoS moRuoS = new MoRuoS();
        moRuoS.pos = 258;
        mobs.add(moRuoS);

        if (Badges.isUnlocked(Badges.Badge.NYZ_SHOP) || DeviceCompat.isDesktop_Dev() || DeviceCompat.isMDP()){
            Nyz ny= new Nyz();
            ny.pos = 303;
            mobs.add(ny);

            YetYog yty = new YetYog();
            yty.pos = 255;
            mobs.add(yty);

            for (int i : SALEPOS_TWO) {
                drop((Generator.random(Generator.Category.POTION)), i).type =
                        Heap.Type.FOR_SALE;
            }

            for (int i : SALEPOS_FOUR) {
                drop((Generator.random(Generator.Category.SEED)), i).type =
                        Heap.Type.FOR_SALE;
            }
        }

        LuoWhite lw = new LuoWhite();
        lw.pos = 453;
        mobs.add(lw);

        /** 魔法阵组 **/

        AG ag = new AG();
        ag.pos = 597;
        mobs.add(ag);

        ArchettoNewYears archettoNewYears = new ArchettoNewYears();
        archettoNewYears.pos = 593;
        mobs.add(archettoNewYears);

        if (passwordbadges.contains(PaswordBadges.Badge.FIREGIRL)
                || DeviceCompat.isDesktop_Dev() || DeviceCompat.isMDP()) {
            WaloKe shopking = new WaloKe();
            shopking.pos = 545;
            mobs.add(shopking);
        }

        if(Badges.isUnlocked(Badges.Badge.KILL_MORES) && Random.Float() >=0.7f || DeviceCompat.isDebug()){
            SliceDream sliceDream = new SliceDream();
            sliceDream.pos = 645;
            mobs.add(sliceDream);
        }

        /* 4桌组 */
        generateRandomMobGroup(DESKTOP_ONE, DESKTOP_1_MOBS, mobs);
        generateRandomMobGroup(DESKTOP_TWO, DESKTOP_2_MOBS, mobs);
        generateRandomMobGroup(DESKTOP_THREE, DESKTOP_3_MOBS, mobs);

        /* 薄绿猫猫 */
        generateRandomMobGroup(CAT_POS, CAT_MOBS, mobs);

        /* 斗地主 */
        SmallLeaf smallLeaf = new SmallLeaf();
        smallLeaf.pos = 438;
        mobs.add(smallLeaf);

        Choco choco = new Choco();
        choco.pos = 416;
        mobs.add(choco);

        PianoLe pianoLe = new PianoLe();
        pianoLe.pos = 466;
        mobs.add(pianoLe);

        DogDogMusic dogDogMusic = new DogDogMusic();
        dogDogMusic.pos = 264;
        mobs.add(dogDogMusic);

        if (passwordbadges.contains(PaswordBadges.Badge.ALLCHSX) || passwordbadges.contains(PaswordBadges.Badge.GODCHSX) || DeviceCompat.isDebug()) {
            if(Random.Int(4) == 0) {
                WhiteLingLand god = new WhiteLingLand();
                god.pos = 657;
                mobs.add(god);
                Statistics.onlyLing = true;

                MeleeWeapon gods1;
                gods1 = (MeleeWeapon) Generator.random(Generator.Category.WEAPON);
                gods1.cursed = false;
                gods1.upgrade();
                Item i  = gods1;
                if(i instanceof DiedCrossBow || i instanceof MoonDao
                        || i instanceof SaiPlus || i instanceof RiceSword
                        || i instanceof RedBloodMoon || i instanceof GoldLongGun ||
                        i instanceof ClearSword || i instanceof ForestBow){
                    drop(gods1,631).type = Heap.Type.FOR_SALE;
                } else {
                    drop(gods1,631).type = Heap.Type.FOR_ICE;
                }


                Wand gods2;
                gods2 = (Wand) Generator.random(Generator.Category.WAND);
                gods2.cursed = false;
                gods2.level += Random.Int(1);
                drop(gods2,633).type = Heap.Type.FOR_ICE;

                Item gods3;
                switch (Random.Int(6)){
                    case 2: gods3 = new BlizzardBrew(); break;
                    case 3: gods3 = new CausticBrew();    break;
                    case 4: gods3 = new InfernalBrew();   break;
                    case 5: gods3 = new ShockingBrew();   break;
                    default:
                        gods3 = new WaterSoul();   break;
                }
                drop(gods3,681).type = Heap.Type.FOR_ICE;

                Item gods4;
                gods4 = Generator.random(Generator.Category.ARTIFACT);
                gods4.cursed = false;
                drop(gods4,683).type = Heap.Type.FOR_ICE;
            }
        }
    }

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if (transition.type == LevelTransition.Type.BRANCH_ENTRANCE) {
            TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
            if (timeFreeze != null) timeFreeze.disarmPresses();
            Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
            if (timeBubble != null) timeBubble.disarmPresses();
            InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
            InterlevelScene.curTransition = new LevelTransition();
            InterlevelScene.curTransition.destDepth = depth;
            InterlevelScene.curTransition.destType = LevelTransition.Type.BRANCH_EXIT;
            InterlevelScene.curTransition.destBranch = 1;
            InterlevelScene.curTransition.type = LevelTransition.Type.BRANCH_EXIT;
            InterlevelScene.curTransition.centerCell = -1;
            Game.switchScene(InterlevelScene.class);
            return false;
        } else if (transition.type == LevelTransition.Type.SURFACE){

            if (hero.belongings.getItem( Amulet.class ) == null) {
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show( new WndMessage( Messages.get(hero, "leave_boat") ) );
                    }
                });
                return false;
            } else {
                Statistics.ascended = true;
                Badges.silentValidateHappyEnd();
                Dungeon.win( Amulet.class );
                Dungeon.deleteGame( GamesInProgress.curSlot, true );
                Game.switchScene( SurfaceScene.class );
                if (hero.belongings.getItem(SakaFishSketon.class) != null) {
                    PaswordBadges.REHOMESKY();
                }
                return true;
            }
        } else if (transition.type == LevelTransition.Type.DOUBLE_ENTRANCE) {

            if (hero.belongings.getItem( DLCItem.class ) == null) {

                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show( new WndHardNotification(new ItemSprite(ItemSpriteSheet.DLCBOOKS),
                                Messages.get(hero, "dlc_name"),
                                Messages.get(hero, "leave_more_dead"),
                                "OK",
                                0));
                    }
                });
                return false;
            } else if(hero.belongings.getItem( BossRushBloodGold.class ) != null && Statistics.deepestFloor == 0) {
                GameRules.BossRush();
                return false;
            } else if(hero.belongings.getItem( RushMobScrollOfRandom.class ) != null && Statistics.deepestFloor == 0) {
                GameRules.RandMode();
                return false;
            } else {
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndMessage(Messages.get(hero, "leave_more_dead")));
                    }
                });
                return false;
            }

        } else {
            return super.activateTransition(hero, transition);
        }
    }

    public int randomRespawnCell() {
        return this.entrance - width();
    }

    public String tilesTex() {
        return Assets.Environment.TILES_ZERO;
    }

    public String waterTex() {
        return Assets.Environment.WATER_ZERO;
    }

    @Override
    public void playLevelMusic() {

        switch (SPDSettings.currentBGM()){
            case 0:
                Music.playModeBGM(Assets.Music.MORP_BOSS, true);
            break;
            case 1:
                Music.playModeBGM(Assets.Music.SAND, true);
                break;
            case 2:
                Music.playModeBGM(Assets.Music.PRACH, true);
                break;
            default:
                Music.playModeBGM(Assets.Music.TOWN, true);
                break;
        }

    }
}

