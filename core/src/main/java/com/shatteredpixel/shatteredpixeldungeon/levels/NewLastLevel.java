package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CUSTOM_DECO;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ENTRANCE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.HIGH_GRASS;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.SIGN;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.SIGN_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL_DECO;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FrostFlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.books.questbookslist.HollowCityBook;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Halo;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

public class NewLastLevel extends Level {

    {
        color1 = 0x801500;
        color2 = 0xa68521;

        viewDistance = Math.min(5, viewDistance);
    }


    private static final int WIDTH = 13;
    private static final int HEIGHT = 23;

    private static final int W = WALL;

    private static final int E = WALL_DECO;

    private static final int O = EMPTY_SP;

    private static final int S = SIGN_SP;
    private static final int K = SIGN;
    private static final int X = CUSTOM_DECO;

    private static final int G = HIGH_GRASS;

    private static final int M = ENTRANCE;

    private static final int[] code_map = {
            K,K,K,K,K,K,K,K,K,K,K,K,K,
            W,O,K,K,K,K,K,K,K,K,K,O,W,
            W,O,O,O,S,O,O,O,S,O,O,O,W,
            E,E,O,O,O,O,O,O,O,O,O,E,E,
            E,O,X,O,O,O,O,O,O,O,X,O,E,
            E,O,O,O,O,O,O,O,O,O,O,O,E,
            K,K,K,K,K,O,O,O,K,K,K,K,K,
            K,K,K,K,K,O,O,O,K,K,K,K,K,
            E,O,O,K,K,O,O,O,K,K,O,O,E,
            E,O,O,O,K,O,O,O,K,O,O,O,E,
            E,O,G,O,O,O,O,O,O,O,G,O,E,
            E,E,O,O,X,O,O,O,X,O,O,E,E,
            W,E,E,O,O,O,O,O,O,O,E,E,W,
            W,W,E,E,E,O,O,O,E,E,E,W,W,
            W,W,W,W,E,O,O,O,E,W,W,W,W,
            W,W,W,W,E,E,O,E,E,W,W,W,W,
            W,W,W,W,E,O,O,O,E,W,W,W,W,
            W,W,W,W,E,O,O,O,E,W,W,W,W,
            W,W,W,W,E,O,O,O,E,W,W,W,W,
            W,W,W,W,E,E,O,E,E,W,W,W,W,
            W,W,W,W,E,O,O,O,E,W,W,W,W,
            W,W,W,W,E,O,M,O,E,W,W,W,W,
            W,W,W,W,E,E,E,E,E,W,W,W,W,
    };

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if (transition.type == LevelTransition.Type.REGULAR_ENTRANCE) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
                    if (timeFreeze != null) timeFreeze.disarmPresses();
                    Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
                    if (timeBubble != null) timeBubble.disarmPresses();
                    InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
                    InterlevelScene.curTransition = new LevelTransition();
                    InterlevelScene.curTransition.destDepth = depth;
                    InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_EXIT;
                    InterlevelScene.curTransition.destBranch = 0;
                    InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_EXIT;
                    InterlevelScene.curTransition.centerCell  = -1;
                    Game.switchScene( InterlevelScene.class );
                }
            });
            return false;
        } else {
            return super.activateTransition(hero, transition);
        }
    }

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();

        int enter = 279;
        LevelTransition entrance = new LevelTransition(this, enter, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(entrance);

        CustomTilemap vis = new townBehind();
        vis.pos(0, 0);
        customTiles.add(vis);

        CustomTilemap via = new townAbove();
        via.pos(0, 0);
        customTiles.add(via);

        return true;
    }

    @Override
    public Group addVisuals() {
        super.addVisuals();
        addVisuals(this, visuals);
        return visuals;
    }

    public static void addVisuals(Level level, Group group){
        for (int i=0; i < level.length(); i++) {
            if (level.map[i] == CUSTOM_DECO) {
                group.add( new TorchA( i ) );
                group.add( new TorchB( i ) );
                group.add( new TorchC( i ) );
            }
            if (level.map[i] == SIGN_SP) {
                group.add( new TorchD( i ) );
                group.add( new TorchE( i ) );
                group.add( new TorchF( i ) );
                group.add( new TorchG( i ) );
                group.add( new TorchH( i ) );
                group.add( new TorchI( i ) );
            }
        }
    }

    @Override
    protected void createMobs() {

    }
    public static int AMULET_POS = 136;

    public static int BOOK_POS = 58;
    @Override
    protected void createItems() {
        drop( new HollowCityBook(), BOOK_POS );

    }

    @Override
    public void playLevelMusic() {
        if (Statistics.amuletObtained) {
            Music.INSTANCE.end();
        } else {
            Music.INSTANCE.play(Assets.Music.THEME_FINALE, true);
        }
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_HOLLOW_CS;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_HALLS;
    }

    public static class townBehind extends CustomTilemap {

        {
            texture = Assets.Environment.HALL_OP;

            tileW = 13;
            tileH = 23;
        }

        final int TEX_WIDTH = 13*16;

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
            texture = Assets.Environment.HALL_PO;

            tileW = 13;
            tileH = 23;
        }

        final int TEX_WIDTH = 13*16;

        @Override
        public Tilemap create() {

            Tilemap v = super.create();

            int[] data = mapSimpleImage(0, 0, TEX_WIDTH);

            v.map(data, tileW);
            return v;
        }

    }



    public static class TorchA extends Emitter {

        private int pos;

        public TorchA( int pos ) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld( pos );

            pos( p.x - 1, p.y - 20, 1, 0 );

            pour(FrostFlameParticle.FACTORY, 0.08f );

            add( new Halo( 10, Window.GDX_COLOR, 0.2f ).point( p.x, p.y - 20 ) );
        }

        @Override
        public void update() {
            if (visible == (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
                super.update();
            }
        }
    }

    public static class TorchB extends Emitter {

        private int pos;

        public TorchB( int pos ) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld( pos );

            pos( p.x - 8, p.y - 16, 1, 0 );

            pour(FrostFlameParticle.FACTORY, 0.08f );

            add( new Halo( 10, Window.GDX_COLOR, 0.2f ).point( p.x-8, p.y - 16 ) );
        }

        @Override
        public void update() {
            if (visible == (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
                super.update();
            }
        }
    }

    public static class TorchC extends Emitter {

        private int pos;

        public TorchC( int pos ) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld( pos );

            pos( p.x + 6, p.y - 16, 1, 0 );

            pour(FrostFlameParticle.FACTORY, 0.08f );

            add( new Halo( 10, Window.GDX_COLOR, 0.2f ).point( p.x+6, p.y - 16 ) );
        }

        @Override
        public void update() {
            if (visible == (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
                super.update();
            }
        }
    }

    public static class TorchD extends Emitter {

        private int pos;

        public TorchD( int pos ) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld( pos );

            pos( p.x + 5.2f, p.y - 10, 0.3f, 0 );

            pour(FlameParticle.FACTORY, 0.1f );

            add( new Halo( 3, 0xFFa500, 0.4f ).point( p.x + 5.2f, p.y - 10 ) );

        }

        @Override
        public void update() {
            if (visible == (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
                super.update();
            }
        }
    }

    public static class TorchE extends Emitter {

        private int pos;

        public TorchE( int pos ) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld( pos );

            pos( p.x - 6.8f, p.y - 10, 0.3f, 0 );

            pour(FlameParticle.FACTORY, 0.1f );
            add( new Halo( 3, 0xFFa500, 0.4f ).point( p.x - 6.8f, p.y - 10 ) );
        }

        @Override
        public void update() {
            if (visible == (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
                super.update();
            }
        }
    }

    public static class TorchF extends Emitter {

        private int pos;

        public TorchF( int pos ) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld( pos );

            pos( p.x + 11.5f, p.y - 7, 0.3f, 0 );

            pour(FlameParticle.FACTORY, 0.1f );
            add( new Halo( 3, 0xFFa500, 0.4f ).point( p.x + 11.5f, p.y - 7 ) );
        }

        @Override
        public void update() {
            if (visible == (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
                super.update();
            }
        }
    }

    public static class TorchG extends Emitter {

        private int pos;

        public TorchG( int pos ) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld( pos );

            pos( p.x - 12.8f, p.y - 7, 0.3f, 0 );

            pour(FlameParticle.FACTORY, 0.1f );
            add( new Halo( 3, 0xFFa500, 0.4f ).point( p.x - 12.8f, p.y - 7 ) );
        }

        @Override
        public void update() {
            if (visible == (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
                super.update();
            }
        }
    }

    public static class TorchH extends Emitter {

        private int pos;

        public TorchH( int pos ) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld( pos );

            pos( p.x + 6, p.y, 0.3f, 0 );

            pour(FlameParticle.FACTORY, 0.1f );
            add( new Halo( 3, 0xFFa500, 0.4f ).point( p.x + 6, p.y ) );
        }

        @Override
        public void update() {
            if (visible == (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
                super.update();
            }
        }
    }

    public static class TorchI extends Emitter {

        private int pos;

        public TorchI( int pos ) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld( pos );

            pos( p.x - 7.5f, p.y, 0.3f, 0 );

            pour(FlameParticle.FACTORY, 0.1f );
            add( new Halo( 3, 0xFFa500, 0.4f ).point( p.x - 7.5f, p.y ) );
        }

        @Override
        public void update() {
            if (visible == (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
                super.update();
            }
        }
    }

    @Override
    public String tileName( int tile ) {
        switch (tile) {
            case CUSTOM_DECO:
                return Messages.get(NewLastLevel.class, "candle_name");
            case SIGN:
                return Messages.get(NewLastLevel.class, "candle_top_name");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case CUSTOM_DECO:
                return Messages.get(NewLastLevel.class, "candle_desc");
            case SIGN:
                return Messages.get(NewLastLevel.class, "candle_top_desc");
            default:
                return super.tileDesc( tile );
        }
    }

}

