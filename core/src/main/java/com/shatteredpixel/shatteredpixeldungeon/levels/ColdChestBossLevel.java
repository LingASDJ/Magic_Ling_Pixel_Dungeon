package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ColdChestBossLevel.State.GO_START;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ColdChestBossLevel.State.MAZE_START;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ColdChestBossLevel.State.START;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ColdChestBossLevel.State.VSBOSS_START;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ColdChestBossLevel.State.VSLINK_START;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ColdChestBossLevel.State.VSYOU_START;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ColdChestBossLevel.State.WIN;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Levitation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RoseShiled;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CrystalMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DCrystal;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DiamondKnight;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.TPDoor;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MIME;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

//宝藏迷宫 10层
public class ColdChestBossLevel extends Level {

    public State pro;

    public State pro(){
        return pro;
    }

    @Override
    public void playLevelMusic() {
        Music.playModeBGM(Assets.Music.BGM_2, true);
    }

    @Override
    public void playBossMusic() {
        Music.playModeBGM(Assets.Music.DIAMAND_KING_INTRO, true);
    }

    private static final String PRO	= "pro";

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        pro = bundle.getEnum(PRO, State.class);
    }

    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(PRO, pro);
    }

    //地图状态
    public enum State {
        GO_START,
        START,
        MAZE_START,
        VSBOSS_START,
        VSLINK_START,
        VSYOU_START,
        WIN
    }

    private static final int WIDTH = 35;
    private final int HEIGHT = 35;

    @Override
    public int randomRespawnCell( Char ch ) {
        int pos = WIDTH*19+17;
        int cell;
        do {
            cell = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
        } while (!passable[cell]
                || (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
                || Actor.findChar(cell) != null);
        return cell;
    }

    @Override
    protected boolean build() {
        setSize(WIDTH,HEIGHT);
        int entrance = 87;

        LevelTransition enter = new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(enter);

        LevelTransition exit = new LevelTransition(this, 0, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exit);

        //首次构建地图
        pro = GO_START;
        setMapStart();

        return true;
    }
    private static final short W = Terrain.WALL;

    private static final short X =Terrain.WALL;
    private static final short A = Terrain.CHASM;

    private static final short M = Terrain.WELL;
    private static final short E = Terrain.CHASM;
    private static final short J = Terrain.ENTRANCE;
    private static final short O = Terrain.WATER;
    private static final short P = Terrain.EMPTY_SP;
    private static final short B = Terrain.EMPTY_SP;
    private static final short K = Terrain.EMPTY;
    private static final short L = Terrain.EMPTY;
    private static final short D = Terrain.DOOR;
    private static final short T = Terrain.PEDESTAL;
    private static final short S = Terrain.STATUE;

    public static int[] FourCrystal = new int[]{
            651,787,643,507
    };

    private static final int[] WorldRoomShort = {
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,
            W,E,E,E,W,W,W,W,W,E,E,E,E,E,E,E,W,W,W,E,E,E,E,E,E,E,W,W,W,W,W,E,E,E,W,
            W,E,E,E,O,O,O,O,O,E,E,E,E,E,E,E,W,J,W,E,E,E,E,E,E,E,O,O,O,O,O,E,E,E,W,
            W,E,E,E,P,P,P,P,P,E,E,E,E,E,E,E,W,O,W,E,E,E,E,E,E,E,P,P,P,P,P,E,E,E,W,
            W,E,E,E,P,E,E,E,P,E,E,E,E,E,E,E,W,K,W,E,E,E,E,E,E,E,P,E,E,E,P,E,E,E,W,
            W,E,P,E,P,E,M,E,P,E,P,E,P,E,P,E,W,P,W,E,P,E,P,E,P,E,P,E,M,E,P,E,P,E,W,
            W,E,E,E,P,E,E,E,P,E,E,E,E,E,E,E,W,P,W,E,E,E,E,E,E,E,P,E,E,E,P,E,E,E,W,
            W,E,E,E,P,P,P,P,P,E,E,E,E,E,E,E,W,P,W,E,E,E,E,E,E,E,P,P,P,P,P,E,E,E,W,
            W,E,E,E,E,E,P,E,E,E,E,E,E,E,E,E,W,K,W,E,E,E,E,E,E,E,E,E,P,E,E,E,E,E,W,
            W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,P,W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,E,E,E,E,E,P,E,W,W,W,W,E,E,E,E,W,P,W,E,E,E,E,W,W,W,W,E,P,E,E,E,E,E,W,
            W,E,E,E,E,E,E,E,W,K,K,W,W,W,W,W,W,D,W,W,W,W,W,W,K,K,W,E,E,E,E,E,E,E,W,
            W,E,E,E,E,E,P,E,W,K,K,K,K,K,K,K,W,K,W,K,K,K,K,K,K,K,W,E,P,E,E,E,E,E,W,
            W,E,E,E,E,E,E,E,W,K,K,K,K,K,K,K,W,O,W,K,K,K,K,K,K,K,W,E,E,E,E,E,E,E,W,
            W,W,W,W,W,W,W,W,W,K,K,K,K,K,K,K,O,O,O,K,K,K,K,K,K,K,W,W,W,W,W,W,W,W,W,
            W,B,B,B,B,B,B,B,W,K,K,K,K,K,K,O,O,O,O,O,K,K,K,K,K,K,W,B,B,B,B,B,B,B,W,
            W,B,K,K,K,K,K,B,W,K,K,K,K,K,O,O,O,K,O,O,O,K,K,K,K,K,W,B,K,K,K,K,K,B,W,
            W,B,K,B,B,B,K,B,W,K,K,K,K,K,O,O,K,K,K,O,O,K,K,K,K,K,W,B,K,B,B,B,K,B,W,
            W,B,B,B,T,B,B,B,W,K,K,K,K,O,O,K,O,O,O,K,O,O,K,K,K,K,W,B,B,B,T,B,B,B,W,
            W,B,K,B,B,B,K,B,D,P,P,K,K,O,K,K,O,K,O,K,K,O,K,K,P,P,D,B,K,B,B,B,K,B,W,
            W,B,K,K,K,K,K,B,W,K,K,K,K,O,O,K,O,O,O,K,O,O,K,K,K,K,W,B,K,K,K,K,K,B,W,
            W,B,K,K,K,K,K,B,W,K,K,K,K,K,O,O,K,K,K,O,O,K,K,K,K,K,W,B,K,K,K,K,K,B,W,
            W,B,B,B,B,B,B,B,W,K,K,K,K,K,O,O,O,K,O,O,O,K,K,K,K,K,W,B,B,B,B,B,B,B,W,
            W,W,W,W,W,W,W,W,W,K,K,K,K,K,K,O,O,O,O,O,K,K,K,K,K,K,W,W,W,W,W,W,W,W,W,
            W,E,E,W,T,W,E,E,W,K,K,K,K,K,K,K,O,O,O,K,K,K,K,K,K,K,W,E,W,T,W,E,E,E,W,
            W,E,E,W,K,W,E,E,W,K,K,W,W,W,K,K,K,O,K,K,K,W,W,W,K,K,W,E,W,K,W,E,E,E,W,
            W,E,E,W,K,W,E,E,W,K,K,W,E,W,K,K,K,K,K,K,K,W,E,W,K,K,W,E,W,K,W,E,E,E,W,
            W,E,E,W,K,W,E,E,W,W,W,W,E,W,K,K,K,P,K,K,K,W,E,W,W,W,W,E,W,K,W,E,E,E,W,
            W,E,E,W,K,W,E,E,E,E,E,E,E,W,K,K,K,K,K,K,K,W,E,E,E,E,E,E,W,K,W,E,E,E,W,
            W,E,E,W,K,W,E,E,E,E,E,E,E,W,K,K,K,P,K,K,K,W,E,E,E,E,E,E,W,K,W,E,E,E,W,
            W,E,E,W,K,W,W,W,W,W,W,W,W,W,K,K,W,W,W,K,K,W,W,W,W,W,W,W,W,K,W,E,E,E,W,
            W,E,E,W,K,K,K,K,K,K,K,K,K,D,K,P,W,E,W,P,K,D,K,K,K,K,K,K,K,K,W,E,E,E,W,
            W,E,E,W,W,W,W,W,W,W,W,W,W,W,K,K,W,W,W,K,K,W,W,W,W,W,W,W,W,W,W,E,E,E,W,
            W,E,E,E,E,E,E,E,E,E,E,E,E,W,K,K,K,P,K,K,K,W,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,
    };

    private static final int[] EndMap = {
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,
            W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,K,O,K,W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,E,E,W,W,W,W,W,W,W,W,W,E,E,E,W,K,O,K,W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,E,E,W,B,B,B,B,B,B,B,W,E,E,E,W,K,O,K,W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,E,E,W,B,K,K,K,K,K,B,W,E,E,E,W,K,O,K,W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,E,E,W,B,K,B,B,B,K,B,W,E,E,E,W,K,O,K,W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,E,E,W,B,B,B,T,B,B,B,W,E,E,E,W,K,O,K,W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,E,E,W,B,K,B,B,B,K,B,W,E,E,E,W,K,O,K,W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,E,E,W,B,K,K,K,K,K,B,W,E,W,W,W,K,O,K,W,W,W,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,E,E,W,B,B,B,B,B,B,B,W,E,W,S,K,K,K,K,K,S,W,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,E,E,W,W,W,W,B,W,W,W,W,E,W,K,O,K,O,K,O,K,W,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,E,E,E,E,E,W,B,W,W,W,W,W,W,K,O,K,O,K,O,K,W,W,W,W,W,E,E,E,E,E,E,E,E,W,
            W,E,E,E,E,E,W,B,W,W,S,K,K,K,K,O,K,O,K,O,K,K,K,K,S,W,E,E,E,E,E,E,E,E,W,
            W,E,E,E,E,E,W,B,W,W,K,O,O,O,O,O,K,O,K,O,O,O,O,O,K,W,E,E,E,E,E,E,E,E,W,
            W,E,E,E,E,E,W,B,W,W,K,O,K,S,K,K,K,T,K,K,K,S,K,O,K,W,E,E,E,E,E,E,E,E,W,
            W,W,W,W,W,W,W,D,W,W,K,K,K,K,B,B,B,B,B,B,B,K,K,K,K,W,W,W,W,W,W,W,W,W,W,
            W,K,K,K,K,K,K,K,K,S,K,K,K,K,B,K,K,K,K,K,B,K,K,K,K,S,K,K,K,K,K,K,K,K,W,
            W,O,O,O,K,K,O,K,O,K,O,K,O,K,B,K,B,B,B,K,B,K,O,O,O,K,K,K,K,K,K,O,O,O,W,
            W,O,K,O,O,O,O,O,O,K,K,K,O,T,B,B,B,K,B,B,B,T,O,K,K,K,O,O,O,O,O,O,K,O,W,
            W,O,O,O,K,K,K,K,K,K,O,O,O,K,B,K,B,B,B,K,B,K,O,K,O,K,O,K,O,K,K,O,O,O,W,
            W,K,K,K,K,K,K,K,K,S,K,K,K,K,B,K,K,K,K,K,B,K,K,K,K,S,K,K,K,K,K,K,K,K,W,
            W,W,W,W,W,W,W,W,W,W,K,O,K,K,B,B,B,B,B,B,B,K,K,K,K,W,W,D,W,W,W,W,W,W,W,
            W,E,E,E,E,E,E,E,E,W,K,O,K,S,K,K,K,T,K,K,K,S,K,O,K,W,W,B,W,E,E,E,E,E,W,
            W,E,E,E,E,E,E,E,E,W,K,O,O,O,O,O,K,O,K,O,O,O,O,O,K,W,W,B,W,E,E,E,E,E,W,
            W,E,E,E,E,E,E,E,E,W,S,K,K,K,K,O,K,O,K,O,K,K,K,K,S,W,W,B,W,E,E,E,E,E,W,
            W,E,E,E,E,E,E,E,E,W,W,W,W,W,K,O,K,O,K,O,K,W,W,W,W,W,W,B,W,E,E,E,E,E,W,
            W,E,E,E,E,E,E,E,E,E,E,E,E,W,K,O,K,O,K,O,K,W,E,E,E,E,W,B,W,E,E,E,E,E,W,
            W,E,E,E,E,E,E,E,E,E,E,E,E,W,S,K,K,K,K,K,S,W,E,E,E,W,W,D,W,W,E,E,E,E,W,
            W,E,E,E,E,E,E,E,E,E,E,E,E,W,W,W,K,O,K,W,W,W,E,E,E,W,K,K,K,W,E,E,E,E,W,
            W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,K,O,K,W,E,E,E,E,E,W,K,T,K,W,E,E,E,E,W,
            W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,K,O,K,W,E,E,E,E,E,W,K,K,K,W,E,E,E,E,W,
            W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,K,O,K,W,E,E,E,E,E,W,W,W,W,W,E,E,E,E,W,
            W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,K,O,K,W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,K,O,K,W,E,E,E,E,E,E,E,E,E,E,E,E,E,E,W,
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,
    };


    private void setMapStart() {
        int entrance = 87;

        LevelTransition enter = new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(enter);

        LevelTransition exit = new LevelTransition(this, 0, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exit);

        map = WorldRoomShort.clone();
    }


    public void setMapEnd(){
        transitions.clear();
        int entrance = 52;
        int exit = 647;
        LevelTransition enter = new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(enter);

        LevelTransition exit2 = new LevelTransition(this, exit, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exit2);

        int doorPos = 647;
        Mob.holdAllies(this, doorPos);
        Mob.restoreAllies(this, Dungeon.hero.pos, doorPos);
    }

    @Override
    public void occupyCell(Char ch) {
        super.occupyCell(ch);
        boolean isTrue = ch.pos == LDBossDoor && ch == Dungeon.hero && level.distance(ch.pos, entrance) >= 2;
        if (map[getBossDoor] == Terrain.DOOR && isTrue || map[getBossDoor] == Terrain.EMBERS || map[getBossDoor] == Terrain.WATER && isTrue) {
           progress();
        }

        ColdChestBossLevel.State level = ((ColdChestBossLevel) Dungeon.level).pro();
        for (Mob boss : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (boss instanceof DiamondKnight) {
                if (level == ColdChestBossLevel.State.MAZE_START && Statistics.KillMazeMimic >= 5) {
                    boss.HP = 240;
                    ScrollOfTeleportation.teleportToLocation(boss,647);
                    ((ColdChestBossLevel) Dungeon.level).progress();
                    ((DiamondKnight) boss).phase++;
                    ScrollOfTeleportation.teleportToLocation(hero, 962);
                    GameScene.flash(0x808080);
                    for (Mob bo : Dungeon.level.mobs.toArray(new Mob[0])) {
                        if (bo instanceof TPDoor) {
                            bo.die(null);
                        }
                    }
                }
            }
        }

        if(pro == START) {
            if (ch == hero) {
                if (ch.pos == 844) {
                    teleportHeroIfHeapEmpty(ch, 111, 844);
                } else if (ch.pos == 869) {
                    teleportHeroIfHeapEmpty(ch, 133, 869);
                } else if (ch.pos == 181) {
                    teleportHeroIfHeapEmpty(ch, 682, 181);
                } else if (ch.pos == 203) {
                    teleportHeroIfHeapEmpty(ch, 682, 203);
                }
                if(ch.pos==682){
                    Buff.detach(hero, Levitation.class);
                } else if(ch.pos==111||ch.pos==133) {
                    Buff.affect(hero, Levitation.class, 99999999999999999999999999999999f);
                }
            }
        }
    }


    public void progress(){
        switch (pro) {
            case GO_START:
                seal();

                DiamondKnight bossx = new DiamondKnight();
                bossx.state = bossx.WANDERING;
                bossx.pos = WIDTH*19+17;
                GameScene.add( bossx );

                ArrayList<Ankh> ankh = hero.belongings.getAllItems(Ankh.class);
                for (Ankh w : ankh.toArray(new Ankh[0])){
                    GameScene.add(Mimic.spawnAt(1102, CrystalMimic.class,w));
                    w.detachAll(hero.belongings.backpack);
                }

                set( getBossDoor, Terrain.LOCKED_DOOR );
                GameScene.updateMap( getBossDoor );
                set( HOME, Terrain.EMPTY );
                GameScene.updateMap( HOME );
                set( 1102, Terrain.EMPTY );
                GameScene.updateMap( 1102 );
                Dungeon.observe();

                //进行Roll判定

                if(Statistics.fuckGeneratorAlone==0) {
                    switch (Random.NormalIntRange(0, 3)) {
                        case 0:
                            drop(new MIME.GOLD_ONE(), 634);
                            break;
                        case 1:
                            drop(new MIME.GOLD_ONE(), 660);
                            break;
                        case 2:
                            drop(new MIME.GOLD_ONE(), 308);
                            break;
                        case 3:
                            drop(new MIME.GOLD_ONE(), 286);
                            break;
                    }
                }
                Statistics.fuckGeneratorAlone++;

                int doorPos = WIDTH*17+17;
                Mob.holdAllies(this, doorPos);
                Mob.restoreAllies(this, Dungeon.hero.pos, doorPos);

                pro = START;
                break;
            case START:
                //血量低于360后且在START枚举中
                for (Mob boss : level.mobs.toArray(new Mob[0])) {
                    if(boss instanceof DiamondKnight) {
                        //如果楼层为开始且boss血量小于360 1阶段
                        if (pro == START && boss.HP <= 360 && !Statistics.TPDoorDieds) {
                            Buff.affect(boss, RoseShiled.class, 10f);
                            Buff.detach(hero, Levitation.class);
                            //宝箱王移动到看戏位
                            ScrollOfTeleportation.appear(boss, MDX);
                            //玩家移动到初始位
                            ScrollOfTeleportation.appear(hero, STARTPOS);
                            boss.HP = 360;
                            pro = MAZE_START;

                            TPDoor ds0 = new TPDoor();
                            ds0.pos = 682;
                            GameScene.add(ds0);
                        }
                    }
                }
                break;
            case MAZE_START:
                //血量低于300后且在MAZE_START枚举中
                for (Mob boss : level.mobs.toArray(new Mob[0])) {
                    if(boss instanceof DiamondKnight) {
                        //如果楼层为开始且boss血量小于300 2阶段
                        if (pro == MAZE_START && boss.HP <= 360 && Statistics.TPDoorDieds) {
                            //动态修改整个房间 宝藏迷宫
                            changeMap(EndMap);
                            //在切换房间的时候立刻切换全新坐标

                            for (Heap heap : level.heaps.valueList()) {
                                for (Item item : heap.items) {
                                    if(heap.type != Heap.Type.CRYSTAL_CHEST){
                                        if(!(item instanceof MIME)){
                                            item.doPickUp(hero, 962);
                                            heap.destroy();
                                        } else {
                                            heap.destroy();
                                        }
                                    }
                                }
                            }



                            Mob.holdAllies(this, 961);
                            Mob.restoreAllies(this, Dungeon.hero.pos, 963);

                            setMapEnd();

                            //进行Roll判定
                            if(Statistics.fuckGeneratorAlone==2) {
                                if (Random.Float() < 0.5f) {
                                    switch (Random.NormalIntRange(0, 1)) {
                                        case 0:
                                            drop(new MIME.GOLD_FOUR(), 217);
                                            break;
                                        case 1:
                                            drop(new MIME.GOLD_FOUR(), 1042);
                                            break;
                                    }
                                } else {
                                    switch (Random.NormalIntRange(0, 1)) {
                                        case 0:
                                            drop(new MIME.GOLD_FIVE(), 217);
                                            break;
                                        case 1:
                                            drop(new MIME.GOLD_FIVE(), 1042);
                                            break;
                                    }
                                }
                            }
                            Statistics.fuckGeneratorAlone++;

                            //生成四个水晶，宝箱王持续回血
                            for (int i : FourCrystal) {
                                DCrystal ds = new DCrystal();
                                ds.pos = i;
                                GameScene.add(ds);
                            }

                            boss.HP = 300;
                            pro = VSBOSS_START;
                        }
                    }
                }
                break;
            case VSBOSS_START:
                for (Mob boss : level.mobs.toArray(new Mob[0])) {
                    if(boss instanceof DiamondKnight) {
                        //如果楼层为开始且boss血量小于240 3阶段
                        if (pro == VSBOSS_START && boss.HP <= 240 && ((DiamondKnight) boss).phase == 2) {
                            boss.HP = 240;
                            pro = VSLINK_START;
                        }
                    }
                }
                break;
            case VSLINK_START:
                for (Mob boss : level.mobs.toArray(new Mob[0])) {
                    if(boss instanceof DiamondKnight) {
                        //如果楼层为开始且boss血量小于200 4阶段
                        if (pro == VSLINK_START && boss.HP <= 200 && ((DiamondKnight) boss).phase == 3) {
                            //Buff.detach(boss, ChampionEnemy.Halo.class);
                            boss.baseSpeed *= 2;
                            pro = VSYOU_START;
                        }
                    }
                }
                break;
            case VSYOU_START:
                //血量低于300后且在MAZE_START枚举中
                for (Mob boss : level.mobs.toArray(new Mob[0])) {
                    if(boss instanceof DiamondKnight) {
                        //如果楼层为开始且boss血量小于100 判定WIN
                        if (pro == VSYOU_START && boss.HP <= 100 && ((DiamondKnight) boss).phase == 4) {
                            pro = WIN;
                        }
                    }
                }
                //水晶自爆
                break;
            case WIN:
                for (Mob boss : level.mobs.toArray(new Mob[0])) {
                    if (boss instanceof DCrystal) {
                        boss.die(true);
                    }
                }
                break;
        }
    }

    @Override
    protected void createMobs() {

    }
    //keep track of removed items as the level is changed. Dump them back into the level at the end.



    private static final int getBossDoor = WIDTH*11+17;
    private static final int LDBossDoor = WIDTH*12+17;

    public static final int MDX = 1102;
    public static final int STARTPOS = 787;
    private static final int HOME = 87;

    @Override
    protected void createItems() {
        Random.pushGenerator(Random.Long());
        ArrayList<Item> bonesItems = Bones.get();
        if (bonesItems != null) {
            for (Item i : bonesItems) {
                drop(i, WIDTH*19+17).setHauntedIfCursed().type = Heap.Type.REMAINS;
            }
        }
        Random.popGenerator();
    }


    public String tilesTex() {
        return Assets.Environment.TILES_COLDCHEST;
    }

    public String waterTex() {
        return Assets.Environment.WATER_PRISON;
    }
}
