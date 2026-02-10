package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.tipsgodungeon;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ALCHEMY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ENTRANCE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EXIT;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.SIGN;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;
import static com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.ready;

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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.JIT;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.WaloKe;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.ZeroDreamShop;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.ATRINewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.ArchettoNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.AutoShopBotNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.BzmdrNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.ChocoNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.DeepSeaNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.DreamLeziNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.FireMagicGirlNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.GudaziNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.HKNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.IceMagicGirlNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.KongFuNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.LuoWhiteNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.MengDongXYNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.MintCat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.MoRuoSNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.MoonCatNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.NxhyFiveYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.NyzNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.ObSirNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.PinkFoxNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.PinkLingNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.QianYueDeepNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.QinLiNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.SDragonBlue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.SheepNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.SlylNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.SmallLeafNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.WhiteYanNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.YogSTSNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.YuYeNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.ZakoFlowerNewYears;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.dlcitem.BossRushBloodGold;
import com.shatteredpixel.shatteredpixeldungeon.items.dlcitem.DLCItem;
import com.shatteredpixel.shatteredpixeldungeon.items.dlcitem.RushMobScrollOfRandom;
import com.shatteredpixel.shatteredpixeldungeon.items.journal.Guidebook;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.SakaFishSketon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
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
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Callback;
import com.watabou.utils.DeviceCompat;

import java.util.List;

public class NewZeroFiveLevel extends Level {

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

    public static class townAbove extends CustomTilemap {

        {
            texture = Assets.Environment.ZERO_BACK;

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
    }

    protected void createItems() {
        if ((!Document.ADVENTURERS_GUIDE.isPageRead(Document.GUIDE_INTRO) || SPDSettings.intro() )){
            drop( new Guidebook(),  888);
        }
    }

    public Mob createMob() {
        return null;
    }



    protected void createMobs() {

        List<PaswordBadges.Badge> passwordbadges = PaswordBadges.filtered(true);

        LanFire lanFire = new LanFire();
        lanFire.pos = 807;
        mobs.add(lanFire);

        if (passwordbadges.contains(PaswordBadges.Badge.FIREGIRL)
                || DeviceCompat.isDebug() && DeviceCompat.isDesktop() || DeviceCompat.isMDP()) {
            WaloKe shopking = new WaloKe();
            shopking.pos = 545;
            mobs.add(shopking);
        }

        ArchettoNewYears archettoNewYears = new ArchettoNewYears();
        archettoNewYears.pos = 593;
        mobs.add(archettoNewYears);

        MintCat mint = new MintCat();
        mint.pos = 537;
        mobs.add(mint);

        //DESKTOP GROUP
        SmallLeafNewYears  leafNewYears = new SmallLeafNewYears();
        leafNewYears.pos = 460;
        mobs.add(leafNewYears);

        QinLiNewYears pale = new QinLiNewYears();
        pale.pos = 459;
        mobs.add(pale);

        SDragonBlue sDragonBlue = new SDragonBlue();
        sDragonBlue.pos = 413;
        mobs.add(sDragonBlue);

        DreamLeziNewYears dlns = new DreamLeziNewYears();
        dlns.pos = 433;
        mobs.add(dlns);

        HKNewYears hkNewYears = new HKNewYears();
        hkNewYears.pos = 336;
        mobs.add(hkNewYears);

        MengDongXYNewYears mdxy = new MengDongXYNewYears();
        mdxy.pos = 366;
        mobs.add(mdxy);

        ChocoNewYears choco = new ChocoNewYears();
        choco.pos = 363;
        mobs.add(choco);

        BzmdrNewYears bzmdrNewYears = new BzmdrNewYears();
        bzmdrNewYears.pos = 338;
        mobs.add(bzmdrNewYears);

        DeepSeaNewYears deepSeaNewYears = new DeepSeaNewYears();
        deepSeaNewYears.pos = 441;
        mobs.add(deepSeaNewYears);

        QianYueDeepNewYears qyny = new QianYueDeepNewYears();
        qyny.pos = 465;
        mobs.add(qyny);

        WhiteYanNewYears flowergods = new WhiteYanNewYears();
        flowergods.pos = 438;
        mobs.add(flowergods);

        YuYeNewYears yyny = new YuYeNewYears();
        yyny.pos = 411;
        mobs.add(yyny);

        MoonCatNewYears mmct = new MoonCatNewYears();
        mmct.pos = 384;
        mobs.add(mmct);

        ZakoFlowerNewYears zknk = new ZakoFlowerNewYears();
        zknk.pos = 385;
        mobs.add(zknk);

        GudaziNewYears gudaziNewYears = new GudaziNewYears();
        gudaziNewYears.pos = 390;
        mobs.add(gudaziNewYears);

        SheepNewYears sny = new SheepNewYears();
        sny.pos = 358;
        mobs.add(sny);

        PinkFoxNewYears plk = new PinkFoxNewYears();
        plk.pos = 333;
        mobs.add(plk);

        AutoShopBotNewYears astb = new AutoShopBotNewYears();
        astb.pos = 361;
        mobs.add(astb);

        ATRINewYears atri = new ATRINewYears();
        atri.pos = 464;
        mobs.add(atri);

        JIT jit = new JIT();
        jit.pos = 408;
        mobs.add(jit);

        KongFuNewYears kf = new KongFuNewYears();
        kf.pos = 436;
        mobs.add(kf);

        //FOUR HUGE DOOR GODS
        SlylNewYears slylNewYears = new SlylNewYears();
        slylNewYears.pos = 63;
        mobs.add(slylNewYears);

        ObSirNewYears osny = new ObSirNewYears();
        osny.pos = 85;
        mobs.add(osny);

        PinkLingNewYears plnk = new PinkLingNewYears();
        plnk.pos = 61;
        mobs.add(plnk);

        NxhyFiveYears nxhyFiveYears = new NxhyFiveYears();
        nxhyFiveYears.pos = 89;
        mobs.add(nxhyFiveYears);

        //SPCT
        LuoWhiteNewYears luoWhiteNewYears = new LuoWhiteNewYears();
        luoWhiteNewYears.pos = 453;
        mobs.add(luoWhiteNewYears);

        NyzNewYears nyzNewYears = new NyzNewYears();
        nyzNewYears.pos = 303;
        mobs.add(nyzNewYears);

        YogSTSNewYears yogSTSNewYears = new YogSTSNewYears();
        yogSTSNewYears.pos = 255;
        mobs.add(yogSTSNewYears);

        ZeroDreamShop shtick = new ZeroDreamShop();
        shtick.pos = 295;
        mobs.add(shtick);

        //SISTER GROUP

        boolean isGet = Badges.isUnlocked(Badges.Badge.KILL_MG) && passwordbadges.contains(PaswordBadges.Badge.FIREGIRL);

        if(isGet || DeviceCompat.isMDP() || DeviceCompat.isDebug()){
            FireMagicGirlNewYears fmny = new FireMagicGirlNewYears();
            fmny.pos = 693;
            mobs.add(fmny);
        }

        IceMagicGirlNewYears icny = new IceMagicGirlNewYears();
        icny.pos = 717;
        mobs.add(icny);

        //HEAD HOTEL
        MoRuoSNewYears moRuoSNewYears = new MoRuoSNewYears();
        moRuoSNewYears.pos = 258;
        mobs.add(moRuoSNewYears);
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
                        GameScene.show( new WndMessage( Messages.get(hero, "leave") ) );
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
        return Assets.Environment.TILES_ZERO_SPRING;
    }

    public String waterTex() {
        return Assets.Environment.WATER_ZERO;
    }

    private void tell(String text) {
        Game.runOnRenderThread(() -> GameScene.show(new WndQuest(new Nyz(), text))
        );
    }

    private void talkToHero(){
        if(!tipsgodungeon) {
            Game.runOnRenderThread(() -> tell(Messages.get(Hero.class, "acsx")));
            ready();
            tipsgodungeon = true;
        }
    }

    @Override
    public void playLevelMusic() {
        Music.playModeBGM(Assets.Music.TOWN_YEARS, true);
    }
}
