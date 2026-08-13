package com.shatteredpixel.shatteredpixeldungeon.levels.minilevels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.BOOKSHELF;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CRYSTAL_DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EXIT;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.SIGN;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra.Kusumi;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.ATRI;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.WhiteLing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.normal.FireMagicGirl;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.normal.IceMagicGirl;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Random;

import java.util.List;

public class HotelNoramlLevel extends Level {

    {
        color1 = 0x801500;
        color2 = 0xa68521;
        viewDistance = 100;
        PaswordBadges.loadGlobal();
    }

    public List<PaswordBadges.Badge> passwordbadges = PaswordBadges.filtered(true);

    private static final int WIDTH = 14;
    private static final int HEIGHT = 14;

    private static final int W = WALL;
    private static final int D = SIGN;
    private static final int T = WATER;
    private static final int X = EXIT;
    private static final int S = CHASM;
    private static final int E = EMPTY_SP;
    private static final int K = DOOR;
    private static final int B = BOOKSHELF;
    private static final int M = CRYSTAL_DOOR;


    private static final int[] code_map = {
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,W,W,W,W,W,W,W,W,W,W,W,W,S,
            S,W,D,D,D,E,W,E,D,X,E,E,W,S,
            S,W,E,E,E,E,K,E,E,E,E,E,W,S,
            S,W,E,E,E,E,W,E,E,E,B,B,W,S,
            S,W,W,W,W,W,W,E,E,E,E,D,W,S,
            S,W,D,D,D,E,W,E,E,E,E,D,W,D,
            S,W,T,T,E,E,K,E,E,E,E,D,W,D,
            S,W,T,T,E,E,W,E,E,W,W,W,W,D,
            S,W,W,W,W,W,W,E,E,W,E,E,E,D,
            S,W,D,D,D,E,W,E,E,W,E,E,E,D,
            S,W,E,E,E,E,K,E,E,M,E,E,E,D,
            S,W,E,E,E,E,W,E,E,W,E,E,E,D,
            S,W,W,W,W,W,W,W,W,W,W,W,W,D,
    };

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if (transition.type == LevelTransition.Type.BRANCH_EXIT) {
            TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
            if (timeFreeze != null) timeFreeze.disarmPresses();
            Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
            if (timeBubble != null) timeBubble.disarmPresses();
            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
            InterlevelScene.curTransition = new LevelTransition();
            InterlevelScene.curTransition.destDepth = depth;
            InterlevelScene.curTransition.destType = LevelTransition.Type.BRANCH_ENTRANCE;
            InterlevelScene.curTransition.destBranch = 0;
            InterlevelScene.curTransition.type = LevelTransition.Type.BRANCH_ENTRANCE;
            InterlevelScene.curTransition.centerCell = -1;
            Game.switchScene(InterlevelScene.class);
        }
        return false;
    }

    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();

        int entrance = 37;

        LevelTransition enter = new LevelTransition(this, entrance, LevelTransition.Type.BRANCH_EXIT);
        transitions.add(enter);

        CustomTilemap vis = new townBehind();
        vis.pos(0, 0);
        customTiles.add(vis);

        return true;
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

    public static class townBehind extends CustomTilemap {

        {
            texture = Assets.Environment.HOTEL_ZERO;

            tileW = 14;
            tileH = 14;
        }

        final int TEX_WIDTH = 14*16;

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

        if(!Statistics.onlyLing){
            if (passwordbadges.contains(PaswordBadges.Badge.ALLCHSX) || RegularLevel.birthday == RegularLevel.DevBirthday.DEV_BIRTHDAY) {
                if (Random.Float() < 0.65f) {
                    WhiteLing n = new WhiteLing();
                    n.pos= 80;
                    mobs.add(n);
                }
            } else if (Badges.isUnlocked(Badges.Badge.VICTORY)) {
                if (Random.Float() < 0.45f) {
                    WhiteLing n = new WhiteLing();
                    n.pos = 80;
                    mobs.add(n);
                }
            } else {
                if (Random.Float() < 0.1f) {
                    WhiteLing n = new WhiteLing();
                    n.pos = 80;
                    mobs.add(n);
                }
            }
        }

        /** 旅馆组 **/
        ATRI atri = new ATRI();
        atri.pos = 102;
        mobs.add(atri);

        if(Badges.isUnlocked(Badges.Badge.KILL_MG) && Random.Float() >= 0.7f|| DeviceCompat.isMDP()){
            IceMagicGirl iceMagicGirl = new IceMagicGirl();
            iceMagicGirl.pos = 158;
            mobs.add(iceMagicGirl);
        }

        if(passwordbadges.contains(PaswordBadges.Badge.FIREGIRL) && Random.Float() >= 0.7f || DeviceCompat.isMDP()){
            FireMagicGirl fireMagicGirl = new FireMagicGirl();
            fireMagicGirl.pos = 46;
            mobs.add(fireMagicGirl);
        }

        if( Random.Float() <= 0.2f){
            Kusumi kusumi = new Kusumi();
            kusumi.pos = 137;
            mobs.add(kusumi);
        }

    }

    @Override
    protected void createItems() {

    }

    public String tilesTex() {
        return Assets.Environment.TILES_ZERO;
    }

    public String waterTex() {
        return Assets.Environment.WATER_ZERO;
    }


}


