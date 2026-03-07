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
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.LanFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.BzmdrLand;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.Gudazi;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.JIT;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.KongFu;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.MoRuoS;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.MoonLow;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.XiaYuan;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.ZeroDreamShop;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.ArchettoNewYears;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.dlcitem.BossRushBloodGold;
import com.shatteredpixel.shatteredpixeldungeon.items.dlcitem.DLCItem;
import com.shatteredpixel.shatteredpixeldungeon.items.dlcitem.RushMobScrollOfRandom;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.SakaFishSketon;
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
import com.watabou.utils.Random;

import java.util.List;

public class NormalZeroFiveLevel extends Level {

    {
        color1 = 5459774;
        color2 = 12179041;
        viewDistance = 100;
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

    /** 美食坐标*/
    public static int[] Food_Tiled = new int[]{
            334,335,/*|*/339,340,
            359,360,/*|*/364,365,
            /*-----------------*/
            409,410,/*|*/414,415,
            434,435,/*|*/439,440,
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
            for (int i : Food_Tiled) {
                if(i == checkTiledID){
                    return Messages.get(this, "food_name");
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
            for (int i : Food_Tiled) {
                if(i == checkTiledID){
                    return Messages.get(this, "food_desc");
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

    public static int[] SALEPOS_ONE = new int[]{
            502,504
    };

    public static int[] SALEPOS_TWO = new int[]{
            429,430
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


    protected void createMobs() {
        PaswordBadges.loadGlobal();
        List<PaswordBadges.Badge> passwordbadges = PaswordBadges.filtered(true);


        MoonLow ml = new MoonLow();
        ml.pos = Random.Float()>0.5f ? 722 : 720;
        mobs.add(ml);

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

        if(Random.Float()<=0.45f){
            BzmdrLand bzmdrLand = new BzmdrLand();
            bzmdrLand.pos = 831;
            mobs.add(bzmdrLand);
            Statistics.onlyBzmdr = true;
        }

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

        /** 魔法阵组 **/
        ArchettoNewYears archettoNewYears = new ArchettoNewYears();
        archettoNewYears.pos = 593;
        mobs.add(archettoNewYears);
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
        Music.playModeBGM(Assets.Music.TOWN, true);
    }
}

