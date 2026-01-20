package com.shatteredpixel.shatteredpixeldungeon.levels.minilevels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invulnerability;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.FoundChest;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DimandMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.EbonyMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GoldenMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.TPDoor;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.HalomethaneFlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.PrisonPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.boss.Diamand_Boss_EntranceRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.boss.Dimand_Boss_ExitRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret.ChestRoomMazeRoom_A;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret.ChestRoomMazeRoom_B;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret.SecretSmallLaboratoryRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.TrapsRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.RingRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.WaterBridgeRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FrostTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Halo;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class MiniChestMazeLevel extends RegularLevel {

    @Override
    public void playLevelMusic() {
        Game.runOnRenderThread(() -> Music.playModeBGM(Assets.Music.DIAMAND_KING_INTRO, true));
    }

    @Override
    public void playBossMusic() {
        Game.runOnRenderThread(() -> Music.playModeBGM(Assets.Music.DIAMAND_KING_INTRO, true));
    }

    @Override
    protected ArrayList<Room> initRooms() {
        ArrayList<Room> initRooms =    new ArrayList<>();
        initRooms.add ( roomEntrance = new Diamand_Boss_EntranceRoom());
        initRooms.add ( roomExit =     new Dimand_Boss_ExitRoom());

        //spawns 1 giant, 3 large, 6-8 small, and 1-2 secret cave rooms
        StandardRoom s;
        s = new RingRoom();
        s.setSizeCat();
        initRooms.add(s);

        int rooms = Random.Int(4);
        for (int i = 0; i < rooms; i++){
            TrapsRoom sd = new TrapsRoom();
            initRooms.add(sd);
        }

        SpecialRoom sx;
        sx = new ChestRoomMazeRoom_A();
        initRooms.add(sx);

        SpecialRoom x;
        x = new SecretSmallLaboratoryRoom();
        initRooms.add(x);

        SpecialRoom xs;
        xs = new ChestRoomMazeRoom_B();
        initRooms.add(xs);

        int rooms2 = 2;
        for (int i = 1; i < rooms2; i++){
            s = new WaterBridgeRoom();
            initRooms.add(s);
        }

        return initRooms;
    }

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndMessage(Messages.get(hero, "leave_chest")));
            }
        });
        return false;
    }

    @Override
    public void occupyCell(Char ch) {
        super.occupyCell(ch);
        if(Statistics.KillMazeMimic>=5 && !Statistics.TPDoorDieds){
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                if (mob instanceof Mimic) {
                    mob.maxLvl = -20;
                    Buff.affect(mob, Dread.class);
                    Buff.affect(mob, Invulnerability.class, 100f);
                }
            }
            FoundChest foundChest = Dungeon.hero.buff(FoundChest.class);
            if(foundChest != null){
                foundChest.NoLoot = 10;
            }
            Statistics.TPDoorDieds = true;
            GLog.n(Messages.get(TPDoor.class,"display"));
            TPDoor ds0 = new TPDoor();
            ds0.pos = randomDestination(ds0);
            GameScene.add(ds0);
            Buff.affect(hero, MindVision.class, 4f);
            Statistics.fuckGeneratorAlone++;
        }
        seal();
    }

    @Override
    protected Class<?>[] trapClasses() {
        return new Class<?>[]{ FrostTrap.class };
    }

    @Override
    protected float[] trapChances() {
        return new float[]{1};
    }

    @Override
    protected Painter painter() {
        return new PrisonPainter()
                .setWater(0.6f, 9)
                .setGrass(0.5f, 4)
                .setTraps(nTraps(), trapClasses(), trapChances());
    }

    public String tilesTex() {
        return Assets.Environment.TILES_COLDCHEST;
    }

    public String waterTex() {
        return Assets.Environment.WATER_PRISON;
    }
    @Override
    public Actor addRespawner() {
        return null;
    }
    @Override
    public Group addVisuals() {
        super.addVisuals();
        addPrisonVisuals(this, visuals);
        return visuals;
    }

    public static void addPrisonVisuals(Level level, Group group){
        for (int i=0; i < level.length(); i++) {
            if (level.map[i] == Terrain.WALL_DECO) {
                group.add( new PrisonLevel.Torch( i ) );
            }
        }
    }

    public static class Torch extends Emitter {

        private final int pos;

        public Torch( int pos ) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld( pos );
            pos( p.x - 1, p.y + 2, 2, 0 );

            pour( HalomethaneFlameParticle.FACTORY, 0.15f );

            add( new Halo( 12, 0xFFFFCC, 0.4f ).point( p.x, p.y + 1 ) );
        }

        @Override
        public void update() {
            if (visible == (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
                super.update();
            }
        }
    }

    private final ArrayList<Generator.Category> prizeClasses = new ArrayList<>(
            Arrays.asList(
                    Generator.Category.ARTIFACT,
                    Generator.Category.RING
            ));

    private Item Prize() {
        Generator.Category cat = prizeClasses.remove(0);
        prizeClasses.add(cat);
        Item prize;
        do {
            prize = Generator.random(cat);
            prize.level(Random.NormalIntRange(0,1));
            prize.cursed = false;
        } while (Challenges.isItemBlocked(prize));
        return prize;
    }

    @Override
    protected void createItems() {

    }

    protected void createMobs() {

        Mimic Mimic = new Mimic();
        mobs.add(com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic.spawnAt(randomRespawnCell(Mimic), Mimic.class));

        Mimic Mimic2 = new Mimic();
        mobs.add(com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic.spawnAt(randomRespawnCell(Mimic2), Mimic.class));

        GoldenMimic Mimic3 = new GoldenMimic();
        mobs.add(GoldenMimic.spawnAt(randomRespawnCell(Mimic3), GoldenMimic.class,( Generator.randomUsingDefaults( Generator.Category.POTION ) )));

        GoldenMimic Mimic4 = new GoldenMimic();
        mobs.add(GoldenMimic.spawnAt(randomRespawnCell(Mimic4), GoldenMimic.class,( Generator.randomUsingDefaults( Generator.Category.ARMOR ) )));

        Mimic MimicX = new Mimic();
        mobs.add(com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic.spawnAt(randomRespawnCell(MimicX), Mimic.class));

        Mimic MimicY = new Mimic();
        mobs.add(com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic.spawnAt(randomRespawnCell(MimicY), Mimic.class));

        if(Dungeon.isChallenged(Challenges.STRONGER_BOSSES)){
            EbonyMimic ebonyMimic = new EbonyMimic();
            mobs.add(EbonyMimic.spawnAt(randomRespawnCell(ebonyMimic), EbonyMimic.class,( Prize())));

            DimandMimic King = new DimandMimic();
            King.pos = randomRespawnCell(King);
            King.state = King.HUNTING;
            mobs.add(King);
        }

    }
}
