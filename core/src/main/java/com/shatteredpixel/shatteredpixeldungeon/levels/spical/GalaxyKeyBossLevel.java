package com.shatteredpixel.shatteredpixeldungeon.levels.spical;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ENTRANCE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.SIGN;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.STATUE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessNoDied;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.galaxy.FourStone;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.galaxy.Sothoth;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.galaxy.SothothEyeDied;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.galaxy.SothothLasher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.galaxy.SothothNPC;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Game;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Random;

import java.util.HashSet;
import java.util.Set;

public class GalaxyKeyBossLevel extends Level {

    {
        viewDistance = 16;
    }

    private static final int WIDTH = 26;
    private static final int HEIGHT = 58;

    private static final int S = CHASM;
    private static final int W = SIGN;
    private static final int E = EMPTY;
    private static final int R = WATER;
    private static final int T = STATUE;
    private static final int B = ENTRANCE;
    private static final int G = DOOR;

    private static final int[] eye_map = {
            S,S,S,S,S,S,S,S,S,S,S,W,W,W,W,W,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,W,W,E,E,E,W,W,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,W,E,E,E,E,E,W,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,W,E,E,B,E,E,W,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,W,E,E,E,E,E,W,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,W,W,E,E,E,W,W,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,W,W,E,W,W,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,E,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,E,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,E,S,S,S,S,S,W,W,W,W,W,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,E,S,S,S,S,S,W,E,E,E,W,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,E,S,S,S,S,S,W,E,E,E,W,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,E,S,S,S,S,S,W,E,E,E,W,S,S,
            S,S,W,W,W,W,W,S,S,S,S,S,S,E,S,S,S,S,S,W,W,E,W,W,S,S,
            S,S,W,E,E,E,W,S,S,S,S,S,S,E,S,S,S,S,S,S,S,E,S,S,S,S,
            S,S,W,E,E,E,W,S,S,S,S,S,S,E,S,S,S,S,S,S,S,E,S,S,S,S,
            S,S,W,E,E,E,W,S,S,S,S,S,S,E,S,S,S,S,S,S,E,S,S,S,S,S,
            S,S,W,W,E,W,W,S,S,S,S,S,E,S,S,S,S,S,S,S,E,S,S,S,S,S,
            S,S,S,S,E,S,S,S,S,S,S,S,E,S,S,S,S,S,S,E,S,S,S,S,S,S,
            S,S,S,S,E,S,S,S,S,S,S,S,E,S,S,S,S,E,E,S,S,S,S,S,S,S,
            S,S,S,S,E,S,S,S,S,S,S,E,S,S,S,S,E,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,E,S,S,S,S,S,E,S,S,E,E,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,E,S,S,S,S,S,S,E,E,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,E,S,S,S,S,S,S,S,E,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,E,S,S,S,S,S,S,S,S,E,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,E,S,S,S,E,E,S,E,S,E,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,E,E,E,S,S,E,S,S,E,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,E,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,E,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,E,S,S,E,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,E,E,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,E,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,E,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,E,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,W,W,W,W,W,E,W,W,W,W,W,W,S,S,S,S,S,S,S,
            S,S,S,S,S,W,W,W,E,E,E,E,E,E,E,E,E,E,W,W,W,S,S,S,S,S,
            S,S,S,W,W,W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,W,W,S,S,S,
            S,S,W,W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,W,S,S,
            S,S,W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,S,S,
            S,W,W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,W,S,
            S,W,E,E,E,E,E,E,E,R,E,E,E,E,E,E,E,T,E,E,E,E,E,E,W,S,
            W,W,E,E,E,E,R,R,E,E,E,E,E,E,E,E,E,R,R,R,E,E,E,E,W,W,
            W,E,E,E,E,E,R,R,E,E,E,E,E,E,E,E,E,R,R,R,E,E,E,E,E,W,
            W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,E,E,E,E,T,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,E,E,E,E,E,E,E,R,R,R,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,E,E,E,E,E,E,E,R,R,R,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,W,
            S,W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,S,
            S,W,W,E,E,E,E,E,E,T,E,E,E,R,E,R,R,E,E,E,E,E,E,W,W,S,
            S,S,W,E,E,E,E,E,E,E,E,E,E,E,E,R,R,E,E,E,E,E,E,W,S,S,
            S,S,W,W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,W,S,S,
            S,S,S,W,W,W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,W,W,S,S,S,
            S,S,S,S,S,W,W,W,E,E,E,E,E,E,E,E,E,E,W,W,W,S,S,S,S,S,
            S,S,S,S,S,S,S,W,W,W,W,W,W,W,W,W,W,W,W,S,S,S,S,S,S,S,
    };

    private static final int getBossDoor = 896;

    public void occupyCell(Char ch) {
        super.occupyCell(ch);
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof Sothoth || mob instanceof SothothEyeDied) {
                mob.pos = 1182;
            }
        }


    }

    public static int[] RandomPos = new int[]{
            1152,1263,1053,1336,1263,1177,1187
    };

    //四 碑 法 阵
    public static int[] FourStoneTower = new int[]{
            //Blue Green Red Yellow
            1383,1398,1008,993
    };

    @Override
    public void unseal() {
        super.unseal();
        set( getBossDoor, EMPTY);
        GameScene.updateMap( getBossDoor );
    }

    @Override
    public void seal() {
        super.seal();
        Dungeon.hero.rest(true);
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob instanceof SothothNPC) {
                mob.die(null);
                GameScene.flash(Window.DeepPK_COLOR);
                AlarmTrap goes = new AlarmTrap();
                goes.pos = mob.pos;
                goes.activate();
            }
        }

        set( getBossDoor, Terrain.CRYSTAL_DOOR);
        GameScene.updateMap( getBossDoor );

        Buff.affect(hero, BlessNoDied.class).set((100), 1);

        Set<Integer> selectedPositions = new HashSet<>();
        int maxPositions = Math.min(RandomPos.length, 4);

        while (selectedPositions.size() < maxPositions) {
            int index = Random.Int(RandomPos.length);
            selectedPositions.add(RandomPos[index]);
        }

        FourStone.MagicFourStone csp0 = new FourStone.MagicFourStone();
        csp0.pos = 993;
        GameScene.add(csp0);

        FourStone csp1 = new FourStone();
        csp1.pos = 1008;
        GameScene.add(csp1);

        FourStone.FrostFourStone csp2 = new FourStone.FrostFourStone();
        csp2.pos = 1383;
        GameScene.add(csp2);

        FourStone.PoisonFourStone csp3 = new FourStone.PoisonFourStone();
        csp3.pos = 1398;
        GameScene.add(csp3);

        for (int pos : selectedPositions) {
            SothothLasher csp = new SothothLasher();
            csp.pos = pos;
            GameScene.add(csp);
        }

        ScrollOfTeleportation.appear(hero, 1052);

        Sothoth boss = new Sothoth();
        boss.state = boss.WANDERING;
        boss.pos = 1182;
        GameScene.add(boss);
        Dungeon.observe();
    }

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {

        if(Statistics.TrueYogNoDied){
            TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
            if (timeFreeze != null) timeFreeze.disarmPresses();
            Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
            if (timeBubble != null) timeBubble.disarmPresses();
            InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
            InterlevelScene.curTransition = new LevelTransition();
            InterlevelScene.curTransition.destDepth = 25;
            InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_ENTRANCE;
            InterlevelScene.curTransition.destBranch = 0;
            InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_ENTRANCE;
            InterlevelScene.curTransition.centerCell = -1;
            Game.switchScene(InterlevelScene.class);
        }

        return false;
    }

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = eye_map.clone();

        int entrance = 91;
        int exit = 0;

        LevelTransition enter = new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(enter);

        LevelTransition exits = new LevelTransition(this, exit, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exits);
        CustomTilemap vis = new GalaxyBehind();
        vis.pos(0, 0);
        customTiles.add(vis);
        CustomTilemap via = new GalaxyAbove();
        via.pos(0, 0);
        customTiles.add(via);

        return true;
    }

    public static class GalaxyBehind extends CustomTilemap {

        {
            texture = Assets.Environment.GALAXY_BD;

            tileW = 26;
            tileH = 58;
        }

        final int TEX_WIDTH = 26*16;

        @Override
        public Tilemap create() {

            Tilemap v = super.create();

            int[] data = mapSimpleImage(0, 0, TEX_WIDTH);

            v.map(data, tileW);
            return v;
        }

    }

    public static class GalaxyAbove extends CustomTilemap {

        {
            texture = Assets.Environment.GALAXY_AE;

            tileW = 26;
            tileH = 58;
        }

        final int TEX_WIDTH = 26*16;

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
        SothothNPC boss = new SothothNPC();
        boss.pos = 1182;
        mobs.add(boss);
    }

    @Override
    protected void createItems() {}

    @Override
    public String tilesTex() {
        return Assets.Environment.GALAXY_TILED;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.GALAXY_WATER;
    }

}
