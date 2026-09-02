package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.TombFlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.CityPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CursingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DistortionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PitfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WarpingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WeakeningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.tomb.CorpseDustTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.tomb.DeadDoorTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.tomb.DeadSoulTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.tomb.InjectSoulTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.tomb.LegionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.tomb.MobSpawnTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Halo;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class ChurchLevel extends RegularLevel {

    {
        color1 = 0x4b6636;
        color2 = 0xf2f2f2;
        extraGlass = false;
        viewDistance = 5;
    }

    @Override
    protected int standardRooms(boolean forceMax) {
        if (forceMax) return 2;
        //6 to 8, average 7
        return 2 + Random.chances(new float[]{1, 3, 1});
    }

    //TODO 等待正式音乐到达
    @Override
    public void playLevelMusic(){
        Music.playModeBGM("music/tomb/tomb2.mp3",true);
    }

    @Override
    protected int specialRooms(boolean forceMax) {
        if (forceMax) return 6;
        //2 to 3, average 2.33
        return 3 + Random.chances(new float[]{2, 1});
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_PLACE;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_PLACE;
    }

    @Override
    protected Painter painter() {
        return new CityPainter()
                .setWater(feeling == Level.Feeling.WATER ? 0.90f : 0.30f, 4)
                .setGrass(feeling == Level.Feeling.GRASS ? 0.80f : 0.20f, 3)
                .setTraps(nTraps(), trapClasses(), trapChances());
    }

    @Override
    protected Class<?>[] trapClasses() {
        return new Class[]{
                // 高频：古墓核心机制（权重 4-5）
                InjectSoulTrap.class, InjectSoulTrap.class,    // 灵魂注入
                CorpseDustTrap.class, CorpseDustTrap.class,    // 尸尘弥漫

                // 中频：亡灵战术（权重 3-4）
                MobSpawnTrap.class,                              // 尸群苏醒
                DeadDoorTrap.class,                              // 死亡之门
                LegionTrap.class, LegionTrap.class,              // 军团集结

                // 中低频：死灵术士的恶意（权重 2）
                DeadSoulTrap.class, DeadSoulTrap.class,          // 死灵缠绕
                CursingTrap.class,                               // 诅咒（新/替换混乱）
                WeakeningTrap.class,                             // 虚弱（契合恶意秘术）

                // 低频：标准危险陷阱（权重 1）
                SummoningTrap.class,                             // 召唤
                WarpingTrap.class,                               // 传送
                PitfallTrap.class,                               // 落穴
                DistortionTrap.class                         // 解离
        };
    }

    @Override
    protected float[] trapChances() {
        return new float[]{
                // InjectSoulTrap x2
                4, 4,
                // CorpseDustTrap x2
                4, 4,
                // MobSpawnTrap
                3,
                // DeadDoorTrap
                3,
                // LegionTrap x2
                3, 3,
                // DeadSoulTrap x2
                2, 2,
                // CursingTrap
                2,
                // WeakeningTrap
                2,
                // SummoningTrap
                1,
                // WarpingTrap
                1,
                // PitfallTrap
                1,
                // DisintegrationTrap
                1
        };
    }


    @Override
    protected void createMobs() {
        //Imp.Quest.spawn( this );

        super.createMobs();
    }

    @Override
    public String tileName( int tile ) {
        switch (tile) {
            case Terrain.WATER:
                return Messages.get(CityLevel.class, "water_name");
            case Terrain.HIGH_GRASS:
                return Messages.get(CityLevel.class, "high_grass_name");
            case Terrain.STATUE:case Terrain.STATUE_SP:
                return Messages.get(CityLevel.class, "statue_name");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.ENTRANCE:
            case Terrain.ENTRANCE_SP:
                return Messages.get(CityLevel.class, "entrance_desc");
            case Terrain.EXIT:
                return Messages.get(CityLevel.class, "exit_desc");
            case Terrain.WALL_DECO:
            case Terrain.EMPTY_DECO:
                return Messages.get(CityLevel.class, "deco_desc");
            case Terrain.EMPTY_SP:
                return Messages.get(CityLevel.class, "sp_desc");
            case Terrain.STATUE:
            case Terrain.STATUE_SP:
                return Messages.get(CityLevel.class, "statue_desc");
            case Terrain.BOOKSHELF:
                return Messages.get(CityLevel.class, "bookshelf_desc");
            default:
                return super.tileDesc( tile );
        }
    }

    @Override
    public Group addVisuals() {
        super.addVisuals();
        addCityVisuals( this, visuals );
        return visuals;
    }

    public static void addCityVisuals( Level level, Group group ) {
        for (int i=0; i < level.length(); i++) {
            if (level.map[i] == Terrain.WALL_DECO) {
                group.add( new LongLiveFire( i ) );
            }
        }
    }

    public static class LongLiveFire extends Emitter {

        private int pos;

        public LongLiveFire( int pos ) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld( pos );
            pos( p.x - 1, p.y + 2, 2, 0 );

            pour(TombFlameParticle.FACTORY, 0.15f );

            add( new Halo( 12, 0xccf8ff, 0.2f ).point( p.x, p.y + 1 ) );
        }

        @Override
        public void update() {
            if (visible == (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
                super.update();
            }
        }
    }
    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if(Dungeon.depth == 16 && Statistics.Tomb_Reach && transition.type == LevelTransition.Type.REGULAR_ENTRANCE){
            TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
            if (timeFreeze != null) timeFreeze.disarmPresses();
            Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
            if (timeBubble != null) timeBubble.disarmPresses();
            InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
            InterlevelScene.curTransition = new LevelTransition();
            InterlevelScene.curTransition.destDepth = depth - 1;
            InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_EXIT;
            InterlevelScene.curTransition.destBranch = 1;
            InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_EXIT;
            InterlevelScene.curTransition.centerCell = -1;
            Game.switchScene(InterlevelScene.class);
            return false;
        } else {
            return super.activateTransition(hero,transition);
        }
    }

}
