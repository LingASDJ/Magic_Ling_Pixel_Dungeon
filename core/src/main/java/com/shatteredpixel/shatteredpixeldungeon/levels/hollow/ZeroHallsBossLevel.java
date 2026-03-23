package com.shatteredpixel.shatteredpixeldungeon.levels.hollow;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.android.AndroidGameRecords;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.HallsLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Patch;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.RankingsScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.tweeners.Delayer;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ZeroHallsBossLevel extends Level {

    {
        color1 = 0x801500;
        color2 = 0xa68521;

        viewDistance = Math.min(4, viewDistance);
    }

    @Override
    public void playBossMusic(){
        if (BossHealthBar.isBleeding()){
            Music.playModeBGM(Assets.Music.HALLS_BOSS_FINALE, true);
        } else {
            Music.playModeBGM(Assets.Music.HALLS_TENSE, true);
        }
    }

    @Override
    public void playLevelMusic() {
        Music.playModeBGM(Assets.Music.BGM_5, true);
    }

    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;

    private static final int ROOM_LEFT		= WIDTH / 2 - 4;
    private static final int ROOM_RIGHT		= WIDTH / 2 + 4;
    private static final int ROOM_TOP		= 8;
    private static final int ROOM_BOTTOM	= ROOM_TOP + 8;

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_HALLS;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_HALLS;
    }

    @Override
    protected boolean build() {

        setSize(WIDTH, HEIGHT);

        for (int i = 0; i < 5; i++) {

            int top;
            int bottom;

            if (i == 0 || i == 4){
                top = Random.IntRange(ROOM_TOP-1, ROOM_TOP+3);
                bottom = Random.IntRange(ROOM_BOTTOM+2, ROOM_BOTTOM+6);
            } else if (i == 1 || i == 3){
                top = Random.IntRange(ROOM_TOP-5, ROOM_TOP-1);
                bottom = Random.IntRange(ROOM_BOTTOM+6, ROOM_BOTTOM+10);
            } else {
                top = Random.IntRange(ROOM_TOP-6, ROOM_TOP-3);
                bottom = Random.IntRange(ROOM_BOTTOM+8, ROOM_BOTTOM+12);
            }

            Painter.fill(this, 4 + i * 5, top, 5, bottom - top + 1, Terrain.EMPTY);

            if (i == 2) {
                int entrance = (6 + i * 5) + (bottom - 1) * width();
                transitions.add(new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE));
            }

        }

        boolean[] patch = Patch.generate(width, height, 0.20f, 0, true);
        for (int i = 0; i < length(); i++) {
            if (map[i] == Terrain.EMPTY && patch[i]) {
                map[i] = Terrain.STATUE;
            }
        }

        map[entrance()] = Terrain.ENTRANCE;

        Painter.fill(this, ROOM_LEFT-1, ROOM_TOP-1, 11, 11, Terrain.EMPTY );

        patch = Patch.generate(width, height, 0.30f, 3, true);
        for (int i = 0; i < length(); i++) {
            if ((map[i] == Terrain.EMPTY || map[i] == Terrain.STATUE) && patch[i]) {
                map[i] = Terrain.WATER;
            }
        }

        for (int i = 0; i < length(); i++) {
            if (map[i] == Terrain.EMPTY && Random.Int(4) == 0) {
                map[i] = Terrain.EMPTY_DECO;
            }
        }

        Painter.fill(this, ROOM_LEFT, ROOM_TOP, 9, 9, Terrain.EMPTY_SP );

        Painter.fill(this, ROOM_LEFT, ROOM_TOP, 9, 2, Terrain.WALL_DECO );
        Painter.fill(this, ROOM_LEFT, ROOM_BOTTOM-1, 2, 2, Terrain.WALL_DECO );
        Painter.fill(this, ROOM_RIGHT-1, ROOM_BOTTOM-1, 2, 2, Terrain.WALL_DECO );

        Painter.fill(this, ROOM_LEFT+3, ROOM_TOP+2, 3, 4, Terrain.EMPTY );

        int exitCell = width/2 + ((ROOM_TOP+1) * width);
        LevelTransition exit = new LevelTransition(this, exitCell, LevelTransition.Type.REGULAR_EXIT);
        exit.top--;
        exit.left--;
        exit.right++;
        transitions.add(exit);

        CustomTilemap vis = new CenterPieceVisuals();
        vis.pos(ROOM_LEFT, ROOM_TOP+1);
        customTiles.add(vis);

        vis = new CenterPieceWalls();
        vis.pos(ROOM_LEFT, ROOM_TOP);
        customWalls.add(vis);

        //basic version of building flag maps for the pathfinder test
        for (int i = 0; i < length; i++){
            passable[i]	= ( Terrain.flags[map[i]] & Terrain.PASSABLE) != 0;
        }

        //ensures a path to the exit exists
        return (PathFinder.getStep(entrance(), exit(), passable) != -1);
    }

    @Override
    protected void createMobs() {
    }

    public Actor addRespawner() {
        return null;
    }

    @Override
    protected void createItems() {
        Random.pushGenerator(Random.Long());
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
    }

    @Override
    public int randomRespawnCell( Char ch ) {
        ArrayList<Integer> candidates = new ArrayList<>();
        for (int i : PathFinder.NEIGHBOURS8){
            int cell = entrance() + i;
            if (passable[cell]
                    && Actor.findChar(cell) == null
                    && (!Char.hasProp(ch, Char.Property.LARGE) || openSpace[cell])){
                candidates.add(cell);
            }
        }

        if (candidates.isEmpty()){
            return -1;
        } else {
            return Random.element(candidates);
        }
    }

    public static class BadDream extends Item{
        {
            image = ItemSpriteSheet.CITY_HOOD;
        }

        @Override
        public boolean isUpgradable() {
            return false;
        }

        @Override
        public boolean isIdentified() {
            return true;
        }
    }

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if(!(Dungeon.isDLC(Conducts.Conduct.DEV))){
            if(transition.type == LevelTransition.Type.REGULAR_ENTRANCE){
                GLog.w(Messages.get(BadDream.class,"dream",hero.name()));
                Badges.CITY_END();
                Badges.KILL_MORES();
                SPDSettings.unlockItem("avatars_mage_3");
                GameScene.scene.add(new Delayer(3f){
                    @Override
                    protected void onComplete() {
                        Badges.validateVictory();
                        PaswordBadges.ALLCS(Challenges.activeChallenges());
                        Badges.validateChampion(Challenges.activeChallenges());
                        PaswordBadges.HERO_CLRE(Challenges.activeChallenges());
                        Dungeon.win( BadDream.class );
                        Game.switchScene( RankingsScene.class );
                        Dungeon.deleteGame( GamesInProgress.curSlot, true );
                        AndroidGameRecords.AbyssRecord();
                    }
                });
            }
        }
        return false;
    }

    @Override
    public String tileName( int tile ) {
        switch (tile) {
            case Terrain.WATER:
                return Messages.get(HallsLevel.class, "water_name");
            case Terrain.GRASS:
                return Messages.get(HallsLevel.class, "grass_name");
            case Terrain.HIGH_GRASS:
                return Messages.get(HallsLevel.class, "high_grass_name");
            case Terrain.STATUE:
            case Terrain.STATUE_SP:
                return Messages.get(HallsLevel.class, "statue_name");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.WATER:
                return Messages.get(HallsLevel.class, "water_desc");
            case Terrain.STATUE:
            case Terrain.STATUE_SP:
                return Messages.get(HallsLevel.class, "statue_desc");
            case Terrain.BOOKSHELF:
                return Messages.get(HallsLevel.class, "bookshelf_desc");
            default:
                return super.tileDesc( tile );
        }
    }

    @Override
    public Group addVisuals () {
        super.addVisuals();
        HallsLevel.addHallsVisuals( this, visuals );
        return visuals;
    }

    public static class CenterPieceVisuals extends CustomTilemap {

        {
            texture = Assets.Environment.HALLS_SP;

            tileW = 9;
            tileH = 8;
        }

        private static final int[] map = new int[]{
                8,  9, 10, 11, 11, 11, 12, 13, 14,
                16, 17, 18, 27, 19, 27, 20, 21, 22,
                24, 25, 26, 19, 19, 19, 28, 29, 30,
                24, 25, 26, 19, 19, 19, 28, 29, 30,
                24, 25, 26, 19, 19, 19, 28, 29, 30,
                24, 25, 34, 35, 35, 35, 34, 29, 30,
                40, 41, 36, 36, 36, 36, 36, 40, 41,
                48, 49, 36, 36, 36, 36, 36, 48, 49
        };

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            updateState();
            return v;
        }

        private void updateState(){
            if (vis != null){
                int[] data = map.clone();
                if (Dungeon.level.map[Dungeon.level.exit()] == Terrain.EXIT) {
                    data[4] = 19;
                    data[12] = data[14] = 31;
                }
                vis.map(data, tileW);
            }
        }
    }

    public static class CenterPieceWalls extends CustomTilemap {

        {
            texture = Assets.Environment.HALLS_SP;

            tileW = 9;
            tileH = 9;
        }

        private static final int[] map = new int[]{
                -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1,
                32, 33, -1, -1, -1, -1, -1, 32, 33,
                40, 41, -1, -1, -1, -1, -1, 40, 41,
        };

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            updateState();
            return v;
        }

        private void updateState(){
            if (vis != null){
                int[] data = map.clone();
                if (Dungeon.level.map[Dungeon.level.exit()] == Terrain.EXIT) {
                    data[3] = 1;
                    data[4] = 0;
                    data[5] = 2;
                    data[13] = 23;
                }
                vis.map(data, tileW);
            }
        }

    }
}

