package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfHealth;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WellWater;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.FireDragon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync.ClearElemtGuard;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class LaveCavesBossLevel extends Level{
    private static final short W = Terrain.WALL;

    private static final short R = Terrain.LAVA;

    private static final short Y = Terrain.EMPTY;

    private static final short V = Terrain.WATER;

    private static final short X = Terrain.ENTRANCE;

    private static final short G = Terrain.HIGH_GRASS;

    private static final short D = Terrain.SECRET_DOOR;
    private static final short M = Terrain.WELL;
    private static final short L= Terrain.PEDESTAL;

    private static final short B= Terrain.BARRICADE;

    private static final short S= Terrain.CRYSTAL_DOOR;

    private static final int[] codedMap = {
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,Y,W,W,W,W,W,W,W,
            W,W,W,W,W,W,W,W,V,V,R,R,R,R,R,R,R,R,R,R,R,R,R,W,W,W,W,W,W,W,W,W,
            W,W,W,W,W,W,R,R,V,V,R,R,R,R,R,R,R,R,R,R,Y,R,R,R,R,W,W,W,W,W,W,W,
            W,W,W,W,R,R,R,R,V,V,R,R,R,R,Y,Y,R,R,Y,R,R,R,R,R,Y,W,W,W,W,W,W,W,
            W,W,W,R,R,R,R,R,V,V,R,R,R,Y,G,Y,Y,Y,Y,R,R,R,R,Y,R,W,W,W,W,W,W,W,
            W,W,W,R,R,R,R,R,V,V,R,R,Y,Y,Y,Y,Y,Y,Y,Y,R,R,R,R,R,W,W,W,W,W,W,W,
            W,W,R,R,R,R,R,R,V,V,R,R,Y,Y,Y,Y,Y,R,V,Y,Y,R,R,R,Y,R,W,W,W,W,W,W,
            W,W,R,R,R,R,R,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,R,V,R,Y,Y,Y,Y,Y,Y,R,R,W,W,W,W,
            W,W,R,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,R,V,R,Y,Y,Y,Y,Y,R,R,R,W,W,W,W,
            W,R,Y,Y,Y,Y,R,R,Y,Y,Y,Y,Y,Y,L,Y,Y,Y,V,R,Y,Y,Y,Y,Y,R,R,R,W,W,W,W,
            W,R,Y,R,R,R,R,R,R,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,R,R,R,W,W,
            W,Y,Y,R,R,R,R,R,R,R,Y,Y,Y,Y,Y,V,Y,Y,Y,Y,Y,R,R,Y,V,V,Y,Y,R,R,W,W,
            W,Y,Y,R,R,R,R,R,R,R,Y,Y,Y,Y,V,V,Y,Y,Y,Y,R,R,R,V,V,V,Y,Y,R,R,W,W,
            W,Y,R,R,R,R,Y,Y,R,R,V,V,Y,Y,Y,Y,Y,Y,Y,Y,R,R,R,V,V,V,R,Y,Y,R,W,W,
            W,Y,R,R,R,R,R,Y,R,R,V,V,Y,Y,Y,Y,Y,Y,R,R,Y,Y,Y,V,V,V,R,Y,Y,R,W,W,
            W,Y,R,R,R,R,R,Y,Y,R,V,V,Y,Y,Y,Y,Y,R,R,R,R,Y,R,V,V,V,R,Y,Y,R,W,W,
            W,Y,R,R,R,R,R,R,Y,Y,V,V,Y,Y,Y,Y,R,R,R,R,R,R,R,V,V,V,Y,Y,Y,R,W,W,
            W,Y,Y,R,R,R,R,R,R,R,R,Y,Y,Y,Y,Y,R,R,R,R,R,R,R,V,V,V,Y,Y,Y,Y,W,W,
            W,R,Y,R,R,R,R,Y,R,R,R,R,R,Y,Y,Y,R,R,Y,R,R,R,R,V,V,Y,Y,Y,R,W,W,W,
            W,R,Y,Y,R,R,Y,Y,Y,R,R,R,R,Y,Y,Y,Y,Y,Y,R,R,R,R,V,Y,Y,Y,R,R,W,W,W,
            W,R,R,Y,Y,Y,Y,Y,Y,R,R,V,R,Y,Y,R,R,R,R,Y,R,R,R,Y,Y,Y,R,R,R,W,W,W,
            W,R,R,Y,Y,Y,Y,Y,Y,Y,Y,V,V,Y,Y,R,R,R,R,R,R,Y,Y,Y,R,R,R,W,W,W,W,W,
            W,R,R,Y,Y,Y,Y,Y,Y,Y,Y,V,V,V,Y,Y,R,R,R,R,R,Y,Y,R,R,R,R,W,W,W,W,W,
            W,W,R,R,Y,Y,Y,Y,Y,Y,Y,V,V,V,Y,Y,Y,R,R,R,R,Y,Y,Y,Y,Y,Y,W,W,W,W,W,
            W,W,W,R,R,Y,Y,Y,Y,Y,Y,V,V,V,Y,Y,Y,Y,Y,Y,Y,Y,Y,W,W,W,W,W,W,W,W,W,
            W,W,W,R,R,R,Y,Y,R,R,R,R,R,X,Y,Y,Y,Y,W,W,W,W,D,W,W,W,W,W,W,W,W,W,
            W,W,W,W,R,R,Y,Y,R,R,R,R,R,R,Y,Y,Y,Y,W,W,W,W,Y,Y,G,G,Y,R,R,G,W,W,
            W,W,W,W,W,R,R,Y,R,R,R,R,R,R,Y,Y,Y,W,W,W,W,W,Y,G,Y,G,Y,Y,Y,Y,W,W,
            W,W,W,W,W,W,R,R,Y,R,R,R,R,R,Y,Y,Y,W,W,W,W,W,Y,Y,V,V,V,Y,M,Y,W,W,
            W,W,W,W,W,W,W,W,W,R,Y,R,R,R,Y,Y,Y,W,W,W,W,W,Y,Y,Y,Y,Y,Y,Y,Y,W,W,
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,

    };

    private static final int[] Rush_codedMap = {
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,Y,W,W,W,W,W,W,W,
            W,W,W,W,W,W,W,W,V,V,R,R,R,R,R,R,R,R,R,R,R,R,R,W,W,W,W,W,W,W,W,W,
            W,W,W,W,W,W,R,R,V,V,R,R,R,R,R,R,R,R,R,R,Y,R,R,R,R,W,W,W,W,W,W,W,
            W,W,W,W,R,R,R,R,V,V,R,R,R,R,Y,Y,R,R,Y,R,R,R,R,R,Y,W,W,W,W,W,W,W,
            W,W,W,R,R,R,R,R,V,V,R,R,R,Y,G,Y,Y,Y,Y,R,R,R,R,Y,R,W,W,W,W,W,W,W,
            W,W,W,R,R,R,R,R,V,V,R,R,Y,Y,Y,Y,Y,Y,Y,Y,R,R,R,R,R,W,W,W,W,W,W,W,
            W,W,R,R,R,R,R,R,V,V,R,R,Y,Y,Y,Y,Y,R,V,Y,Y,R,R,R,Y,R,W,W,W,W,W,W,
            W,W,R,R,R,R,R,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,R,V,R,Y,Y,Y,Y,Y,Y,R,R,W,W,W,W,
            W,W,R,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,R,V,R,Y,Y,Y,Y,Y,R,R,R,W,W,W,W,
            W,R,Y,Y,Y,Y,R,R,Y,Y,Y,Y,Y,Y,L,Y,Y,Y,V,R,Y,Y,Y,Y,Y,R,R,R,W,W,W,W,
            W,R,Y,R,R,R,R,R,R,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,R,R,R,W,W,
            W,Y,Y,R,R,R,R,R,R,R,Y,Y,Y,Y,Y,V,Y,Y,Y,Y,Y,R,R,Y,V,V,Y,Y,R,R,W,W,
            W,Y,Y,R,R,R,R,R,R,R,Y,Y,Y,Y,V,V,Y,Y,Y,Y,R,R,R,V,V,V,Y,Y,R,R,W,W,
            W,Y,R,R,R,R,Y,Y,R,R,V,V,Y,Y,Y,Y,Y,Y,Y,Y,R,R,R,V,V,V,R,Y,Y,R,W,W,
            W,Y,R,R,R,R,R,Y,R,R,V,V,Y,Y,Y,Y,Y,Y,R,R,Y,Y,Y,V,V,V,R,Y,Y,R,W,W,
            W,Y,R,R,R,R,R,Y,Y,R,V,V,Y,Y,Y,Y,Y,R,R,R,R,Y,R,V,V,V,R,Y,Y,R,W,W,
            W,Y,R,R,R,R,R,R,Y,Y,V,V,Y,Y,Y,Y,R,R,R,R,R,R,R,V,V,V,Y,Y,Y,R,W,W,
            W,Y,Y,R,R,R,R,R,R,R,R,Y,Y,Y,Y,Y,R,R,R,R,R,R,R,V,V,V,Y,Y,Y,Y,W,W,
            W,R,Y,R,R,R,R,Y,R,R,R,R,R,Y,Y,Y,R,R,Y,R,R,R,R,V,V,Y,Y,Y,R,W,W,W,
            W,R,Y,Y,R,R,Y,Y,Y,R,R,R,R,Y,Y,Y,Y,Y,Y,R,R,R,R,V,Y,Y,Y,R,R,W,W,W,
            W,R,R,Y,Y,Y,Y,Y,Y,R,R,V,R,Y,Y,R,R,R,R,Y,R,R,R,Y,Y,Y,R,R,R,W,W,W,
            W,R,R,Y,Y,Y,Y,Y,Y,Y,Y,V,V,Y,Y,R,R,R,R,R,R,Y,Y,Y,R,R,R,W,W,W,W,W,
            W,R,R,Y,Y,Y,Y,Y,Y,Y,Y,V,V,V,Y,Y,R,R,R,R,R,Y,Y,R,R,R,R,W,W,W,W,W,
            W,W,R,R,Y,Y,Y,Y,Y,Y,Y,V,V,V,Y,Y,Y,R,R,R,R,Y,Y,Y,Y,Y,Y,W,W,W,W,W,
            W,W,W,R,R,Y,Y,Y,Y,Y,Y,V,V,V,Y,Y,Y,Y,Y,Y,Y,Y,Y,W,W,W,W,W,W,W,W,W,
            W,W,W,R,R,R,Y,Y,R,R,R,R,R,X,Y,Y,Y,Y,W,W,W,W,S,W,W,W,W,W,W,W,W,W,
            W,W,W,W,R,R,Y,Y,R,R,R,R,R,R,Y,Y,Y,Y,W,W,W,W,Y,Y,G,G,Y,R,R,G,W,W,
            W,W,W,W,W,R,R,Y,R,R,R,R,R,R,Y,Y,Y,W,W,W,W,W,Y,G,Y,G,Y,Y,Y,Y,W,W,
            W,W,W,W,W,W,R,R,Y,R,R,R,R,R,Y,Y,Y,W,W,W,W,W,Y,Y,V,V,V,Y,M,Y,W,W,
            W,W,W,W,W,W,W,W,W,R,Y,R,R,R,Y,Y,Y,W,W,W,W,W,Y,Y,Y,Y,Y,Y,Y,Y,W,W,
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,

    };

    @Override
    public void playLevelMusic(){
        Music.playModeBGM(Assets.Music.BGM_1A,true);
    }

    @Override
    public void playBossMusic(){
        Music.playModeBGM(Assets.Music.DRAGON_LING,true);
    }

    @Override
    public void seal() {
        super.seal();
        set( 845, Terrain.EMPTY );
        GameScene.updateMap( 845 );

        set(334, Terrain.PEDESTAL );
        GameScene.updateMap( 334);

        if(Dungeon.isChallenged(Challenges.STRONGER_BOSSES)){
            set( 854, Terrain.CRYSTAL_DOOR );
            GameScene.updateMap( 854 );
        }

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                Music.INSTANCE.play(Assets.Music.DRAGON_LING, true);
            }
        });
    }

    @Override
    public void unseal() {
        super.unseal();
        set( 845, Terrain.ENTRANCE );
        GameScene.updateMap( 845 );

        set(334, Terrain.EXIT );
        GameScene.updateMap( 334);

        Dungeon.observe();
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                Music.INSTANCE.fadeOut(5f, new Callback() {
                    @Override
                    public void call() {
                        Music.INSTANCE.end();
                    }
                });
            }
        });
    }

    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if (transition.type == LevelTransition.Type.BRANCH_ENTRANCE) {
            if (!Statistics.GameKillFireDargon){
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GLog.w(Messages.get(ClearElemtGuard.class, "cant_kill_fire"));
                    }
                });
                return false;
            }

            TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
            if (timeFreeze != null) timeFreeze.disarmPresses();
            Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
            if (timeBubble != null) timeBubble.disarmPresses();
            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
            InterlevelScene.curTransition = new LevelTransition();
            InterlevelScene.curTransition.destDepth = 9;
            InterlevelScene.curTransition.destType = LevelTransition.Type.BRANCH_ENTRANCE;
            InterlevelScene.curTransition.destBranch = 0;
            InterlevelScene.curTransition.type = LevelTransition.Type.BRANCH_ENTRANCE;
            InterlevelScene.curTransition.centerCell = -1;
            Game.switchScene(InterlevelScene.class);
        } else {
            return super.activateTransition(hero,transition);
        }
        return false;
    }

    /**
     * @return
     */
    public Class<?extends WellWater> overrideWater = null;
    private static final Class<?>[] WATERS =
            {WaterOfHealth.class};
    @Override
    protected boolean build() {

        setSize(32, 32);

        map = Statistics.bossRushMode ? Rush_codedMap.clone() : codedMap.clone();

        CustomTilemap vis = new townBehind();
        vis.pos(0, 0);
        customTiles.add(vis);

        @SuppressWarnings("unchecked")
        Class<? extends WellWater> waterClass =
                overrideWater != null ?
                        overrideWater :
                        (Class<? extends WellWater>) Random.element( WATERS );
        WellWater.seed(956, 1, waterClass, this);

        int enter = 845;
        LevelTransition ent;
        if(Statistics.bossRushMode){
            ent = new LevelTransition(this, enter, LevelTransition.Type.REGULAR_ENTRANCE);
        } else {
            ent = new LevelTransition(this, enter, LevelTransition.Type.BRANCH_ENTRANCE);
        }
        transitions.add(ent);

        LevelTransition exit;
        if(Statistics.bossRushMode){
            exit = new LevelTransition(this, 334, LevelTransition.Type.REGULAR_EXIT);
        } else {
            exit = new LevelTransition(this, 0, LevelTransition.Type.BRANCH_EXIT);
        }
        transitions.add(exit);


        return true;
    }

    @Override
    public int randomRespawnCell( Char ch ) {
        int pos = 845;
        int cell;
        do {
            cell = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
        } while (!passable[cell]
                || (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
                || Actor.findChar(cell) != null);
        return cell;
    }


    @Override
    public String tileName( int tile ) {
        switch (tile) {
            case Terrain.LAVA:
                return Messages.get(LaveCavesBossLevel.class, "lava_name");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.LAVA:
                return Messages.get(LaveCavesBossLevel.class, "lava_desc");
            default:
                return super.tileDesc( tile );
        }
    }

    public static class townBehind extends CustomTilemap {

        {
            texture = Dungeon.isChallenged(Challenges.STRONGER_BOSSES) && !Statistics.bossRushMode ?  Assets.Environment.LAVACAVE_OP_HRAD : Assets.Environment.LAVACAVE_OP;

            tileW = 32;
            tileH = 32;
        }

        final int TEX_WIDTH = 32*16;

        @Override
        public Tilemap create() {

            Tilemap v = super.create();

            int[] data = mapSimpleImage(0, 0, TEX_WIDTH);

            v.map(data, tileW);
            return v;
        }

    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_FIRE;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_HALLS;
    }

    /**
     *
     */
    @Override
    protected void createMobs() {

        FireDragon boss = new FireDragon();
        boss.pos = 334;
        mobs.add(boss);
    }

    /**
     *
     */
    @Override
    protected void createItems() {

    }

    @Override
    public Group addVisuals () {
        super.addVisuals();
        addLavesVisuals( this, visuals );
        return visuals;
    }

    public static void addLavesVisuals( Level level, Group group ) {
        for (int i=0; i < level.length(); i++) {
            if (level.map[i] == Terrain.LAVA) {
                group.add( new Stream( i ) );
            }
        }
    }

    static class Stream extends Group {

        private int pos;

        private float delay;

        public Stream( int pos ) {
            super();

            this.pos = pos;

            delay = Random.Float( 2 );
        }

        @Override
        public void update() {

            if (!Dungeon.level.water[pos]){
                killAndErase();
                return;
            }

            if (visible == (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {

                super.update();

                if ((delay -= Game.elapsed) <= 0) {

                    delay = Random.Float( 2 );

                    PointF p = DungeonTilemap.tileToWorld( pos );
                    ((FireParticle)recycle( FireParticle.class )).reset(
                            p.x + Random.Float( DungeonTilemap.SIZE ),
                            p.y + Random.Float( DungeonTilemap.SIZE ) );
                }
            }
        }

        @Override
        public void draw() {
            Blending.setLightMode();
            super.draw();
            Blending.setNormalMode();
        }
    }

    public static final class SmokeParticle extends PixelParticle {

        public SmokeParticle() {
            super();

            color( 0x000000 );
            speed.set( Random.Float( -2, 4 ), -Random.Float( 3, 6 ) );
        }

        public void reset( float x, float y ) {
            revive();

            this.x = x;
            this.y = y;

            left = lifespan = 2f;
        }

        @Override
        public void update() {
            super.update();
            float p = left / lifespan;
            am = p > 0.8f ? 1 - p : p * 0.25f;
            size( 6 - p * 3 );
        }
    }

    public static class FireParticle extends PixelParticle.Shrinking {

        public FireParticle() {
            super();

            color(Window.DeepPK_COLOR);
            lifespan = 1f;

            acc.set( 0, +80 );
        }

        public void reset( float x, float y ) {
            revive();

            this.x = x;
            this.y = y;

            left = lifespan;

            speed.set( 0, -40 );
            size = 4;
        }

        @Override
        public void update() {
            super.update();
            float p = left / lifespan;
            am = p > 0.8f ? (1 - p) * 5 : 1;
        }
    }

}
