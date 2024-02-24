package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.CS;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ALCHEMY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.BARRICADE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.BOOKSHELF;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CRYSTAL_DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CUSTOM_DECO;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.DOOR;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.ENTRANCE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EXIT;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.MINE_CRYSTAL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.PEDESTAL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.SIGN;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.STATUE;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL_DECO;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WELL;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.LanFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NxhyNpc;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Nyz;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.REN;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Slyl;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.obSir;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.BzmdrLand;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.DeepSea;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.JIT;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.KongFu;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.LuoWhite;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.Mint;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.MoRuoS;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.MoonLow;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.Question;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.SmallLeaf;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.WhiteYan;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.YetYog;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.BookBag;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.journal.Guidebook;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.RandomChest;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.SakaFishSketon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SurfaceScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.noosa.Game;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.List;

public class ZeroCityLevel extends Level {

    {
        color1 = 0x801500;
        color2 = 0xa68521;
        viewDistance = 100;
    }

    private static final int WIDTH = 64;
    private static final int HEIGHT = 64;

    private static final int M = WALL;
    private static final int R = WATER;
    private static final int D = EMPTY;
    private static final int B = DOOR;
    //告示牌会拯救我们 指可见但不可行走 空气墙 嗯 是这样的
    private static final int P = SIGN;
    private static final int O = CUSTOM_DECO;
    private static final int V = WALL_DECO;
    private static final int K = WELL;
    private static final int E = BARRICADE;
    private static final int J = STATUE;
    private static final int Y = BOOKSHELF;
    private static final int S = CHASM;
    private static final int T = ENTRANCE;

    private static final int N = EXIT;

    private static final int I = MINE_CRYSTAL;

    private static final int A = STATUE;

    private static final int G = ALCHEMY;

    private static final int Q = CRYSTAL_DOOR;

    private static final int X = PEDESTAL;

    private static final int[] codedMap = {
            P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,M,P,P,P,P,S,P,P,P,P,P,P,P,P,P,P,P,M,M,M,M,M,M,P,
            P,P,P,M,D,D,D,P,P,D,P,M,M,P,M,P,P,P,P,P,P,P,M,M,M,M,M,M,M,P,P,P,P,P,P,P,P,P,P,P,M,R,X,R,R,S,P,P,P,P,P,P,P,P,P,P,P,M,M,P,D,D,D,M,
            P,P,D,T,D,D,I,D,D,D,D,D,D,D,M,P,P,P,P,P,P,P,M,O,O,O,O,O,M,M,M,M,M,M,M,M,M,M,M,M,M,R,R,R,R,M,M,M,M,M,M,M,M,M,P,P,P,D,M,P,A,A,A,M,
            P,P,I,D,A,D,D,D,A,D,D,D,D,D,M,M,P,P,P,P,P,M,M,D,D,D,D,D,D,D,D,D,D,R,R,R,D,D,M,M,M,B,M,M,M,M,D,D,D,D,D,D,D,M,P,P,P,D,M,P,D,D,D,M,
            P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,M,P,P,P,P,M,M,D,D,D,D,D,M,M,M,M,M,M,M,M,M,M,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,M,M,P,P,D,M,P,D,N,D,M,
            P,P,D,D,D,D,K,D,D,D,D,D,D,D,D,M,M,P,P,P,M,D,D,D,D,D,D,M,D,D,D,D,D,D,D,D,M,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,M,M,P,D,M,P,D,D,D,M,
            P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,M,M,M,M,M,D,D,D,D,D,D,M,D,D,D,D,D,D,D,D,M,D,D,D,D,D,D,D,R,R,D,D,D,D,D,D,D,D,D,M,P,D,M,M,M,B,M,M,
            P,P,D,D,A,D,D,D,A,D,D,D,A,D,D,D,M,M,M,M,M,D,D,D,D,D,D,M,D,D,D,D,D,D,D,D,M,D,D,D,D,D,D,D,R,R,D,D,D,D,D,D,D,D,D,D,D,D,D,A,D,D,D,P,
            P,P,D,D,D,D,D,D,D,I,D,D,D,D,D,D,O,D,D,D,D,D,D,D,D,D,D,M,D,D,D,D,D,D,D,D,M,D,D,D,D,D,D,D,R,R,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,
            P,P,D,D,D,D,D,D,D,R,R,R,R,R,R,D,M,M,M,M,M,M,M,M,M,D,D,M,M,M,M,D,D,M,M,M,M,D,D,D,D,D,D,R,R,R,R,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,
            P,P,D,D,J,R,R,R,R,R,R,R,R,R,R,D,M,D,D,D,D,Y,Y,D,M,D,D,P,P,P,M,D,M,M,D,D,D,D,D,D,D,D,D,R,R,R,R,D,D,D,D,D,D,D,D,D,D,D,D,M,D,D,D,P,
            P,P,D,D,R,R,R,R,R,R,R,R,R,R,R,D,M,D,D,D,D,D,D,D,M,D,D,P,P,P,M,B,M,D,D,D,D,D,D,D,D,D,D,R,R,R,R,D,D,D,D,D,D,D,D,P,P,P,D,M,D,D,D,P,
            P,P,D,D,R,R,R,R,R,R,R,R,R,R,R,D,M,D,D,D,D,Y,Y,D,M,D,D,R,R,R,D,D,D,D,D,D,D,D,D,D,D,D,D,R,R,R,R,D,D,D,D,D,D,D,D,P,P,D,D,M,G,D,D,P,
            P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,M,D,D,D,D,D,D,D,M,D,D,R,R,R,A,D,A,D,D,D,D,D,D,D,D,D,D,R,R,R,R,D,D,D,D,D,D,D,P,P,P,D,D,M,D,D,D,P,
            P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,M,M,M,B,M,M,M,M,M,D,D,R,R,R,D,D,D,D,D,D,D,D,D,D,D,D,D,R,D,D,D,D,D,D,D,D,D,D,P,R,P,D,D,M,D,D,D,P,
            P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,R,P,D,D,M,D,D,D,P,
            P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,P,R,P,D,D,M,M,M,B,M,
            P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,R,R,P,D,D,M,D,D,D,P,
            P,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,M,M,M,B,M,M,M,M,M,M,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,R,R,P,D,D,M,D,D,D,P,
            P,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,M,D,D,D,D,D,D,D,D,M,O,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,P,R,R,P,D,D,M,D,D,D,P,
            P,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,M,K,D,D,D,D,D,D,D,M,O,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,P,D,D,D,D,D,D,D,P,R,R,R,P,D,D,M,M,M,M,M,
            P,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,M,D,D,D,D,D,D,D,D,M,O,D,D,P,D,D,D,D,D,D,D,D,D,D,D,D,P,P,P,D,D,D,D,D,D,D,P,R,R,R,P,P,D,D,D,D,D,P,
            P,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,M,D,D,D,D,D,M,M,M,M,P,P,P,P,D,D,D,D,D,D,D,D,D,D,D,D,P,P,P,P,D,D,D,D,D,D,P,R,R,R,R,P,D,D,D,D,D,P,
            P,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,M,M,M,M,M,M,M,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,P,P,P,P,P,D,D,D,D,D,P,R,R,R,R,P,P,D,D,D,D,P,
            P,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,P,P,P,P,P,P,D,D,D,D,D,P,R,R,R,R,R,P,P,D,D,P,P,
            P,P,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,P,P,P,P,P,P,P,D,D,D,D,D,P,R,R,R,R,R,R,P,P,P,P,P,
            P,P,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,P,P,P,P,P,D,D,D,D,D,D,D,P,P,P,P,P,P,P,P,D,D,D,D,D,P,P,R,R,R,R,R,R,R,R,R,P,
            P,P,P,P,P,P,P,P,D,D,D,D,D,P,P,P,P,P,P,P,P,P,P,P,P,P,R,R,R,R,R,R,P,D,D,D,D,D,D,P,P,P,P,P,P,D,D,D,D,D,D,D,D,P,R,R,R,R,R,R,R,R,D,P,
            P,P,P,P,R,R,R,R,D,D,D,D,D,R,R,R,R,R,R,R,R,R,R,R,R,R,R,D,D,D,D,R,P,D,D,D,D,D,D,D,P,P,P,P,D,D,D,D,D,D,D,D,D,P,P,R,R,R,R,R,R,R,D,P,
            P,P,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,D,D,D,D,D,D,D,D,P,P,D,D,D,D,D,D,D,D,D,D,D,P,R,R,R,R,R,R,R,D,P,
            P,P,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,M,M,E,M,M,M,D,D,D,D,D,D,D,D,D,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,R,R,R,R,R,R,R,D,P,
            P,P,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,M,D,D,D,D,D,D,D,D,D,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,P,R,R,R,R,R,R,D,P,
            P,P,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,M,D,D,D,D,D,D,D,D,D,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,P,R,R,R,R,R,D,P,
            P,P,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,M,D,D,D,D,D,D,D,D,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,P,P,R,R,R,D,P,
            P,P,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,M,D,D,D,D,D,D,P,P,R,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,P,R,R,R,P,
            P,P,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,P,R,R,R,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,P,R,R,P,
            P,P,P,P,D,D,D,D,D,D,D,A,D,D,D,D,D,D,D,D,D,D,D,P,P,P,P,R,R,R,D,D,D,D,D,D,D,D,D,M,M,M,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,P,R,P,
            P,P,P,P,D,D,D,D,D,D,D,A,D,D,D,D,D,D,A,D,D,D,D,D,R,R,R,R,D,D,D,D,D,D,D,D,D,D,D,D,D,M,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,P,P,
            P,P,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,M,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,P,
            P,P,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,M,D,D,D,D,D,D,D,D,D,D,D,M,M,M,V,M,M,M,D,D,P,P,
            P,P,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,M,D,D,D,D,D,D,D,D,D,D,D,M,D,D,D,D,D,M,D,D,D,P,
            P,P,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,M,D,D,D,D,M,D,D,D,D,D,D,D,D,D,D,D,M,D,D,D,D,D,M,D,D,D,P,
            P,P,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,M,D,D,G,D,M,D,D,D,D,D,D,D,D,D,D,D,M,D,D,D,D,D,M,D,D,D,P,
            P,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,M,D,D,D,D,M,D,D,D,D,D,D,D,D,D,D,D,M,D,D,D,D,D,M,D,D,D,P,
            P,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,A,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,M,M,M,M,M,M,D,D,D,D,D,I,D,D,D,D,D,M,D,D,D,D,D,M,D,D,D,P,
            P,P,P,D,D,D,D,D,D,D,D,A,D,D,D,D,D,D,A,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,I,D,M,M,M,M,M,D,D,D,D,D,M,D,D,P,P,
            P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,I,D,M,D,D,D,D,D,D,D,D,D,M,D,D,P,P,
            P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,I,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,B,D,D,D,D,D,D,D,D,D,M,D,D,P,P,
            P,P,D,D,D,D,D,D,I,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,P,P,P,P,D,D,D,D,D,D,D,D,D,D,D,I,D,M,D,D,D,D,D,D,D,D,D,M,D,D,P,P,
            P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,P,P,R,R,R,P,D,D,D,D,D,D,D,D,D,D,D,I,D,M,M,M,M,M,D,D,D,D,D,M,D,D,P,P,
            P,P,D,D,D,D,D,P,P,P,P,P,P,P,P,D,D,D,D,D,I,D,D,D,D,D,P,P,P,P,R,R,R,R,R,P,D,D,D,D,D,D,D,D,D,D,D,I,D,D,D,D,D,M,D,D,D,D,D,M,D,P,P,P,
            P,P,D,D,D,P,P,P,R,R,R,R,R,R,P,P,P,P,D,D,D,D,D,D,D,P,P,R,R,R,R,R,R,R,R,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,M,D,D,D,D,D,M,D,P,R,P,
            P,P,P,P,P,P,R,R,R,R,R,R,R,R,R,R,R,P,P,D,D,D,P,P,P,P,R,R,R,R,R,R,R,R,R,R,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,M,D,D,D,D,D,M,D,P,R,P,
            P,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,P,P,P,P,P,R,R,R,R,R,R,R,R,R,R,R,R,R,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,M,D,D,D,D,D,M,D,P,R,P,
            P,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,M,M,M,M,M,M,M,S,P,R,P,
            P,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,P,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,P,P,
            P,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,P,
            P,D,D,D,D,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,P,D,D,I,D,D,D,D,D,D,D,D,I,D,D,D,D,D,D,D,D,D,D,D,D,D,P,
            P,D,D,D,D,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,P,D,D,D,D,D,D,D,D,D,D,D,D,D,D,P,P,D,D,D,D,D,D,D,D,D,P,
            P,S,S,D,D,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,P,P,D,D,D,D,D,D,D,D,D,D,D,P,P,P,P,D,D,D,D,D,D,D,D,D,P,
            P,P,S,D,D,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,P,D,D,D,D,D,I,D,D,D,D,P,P,R,R,P,D,D,D,I,D,D,D,D,P,P,
            P,P,S,D,D,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,P,P,D,D,D,D,D,D,D,P,P,P,R,R,R,P,D,D,D,D,D,D,D,P,P,P,
            P,P,S,D,D,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,P,P,P,P,P,P,P,P,P,R,R,R,R,R,P,P,D,D,D,D,D,P,P,P,P,
            P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,P,
    };


//    public void occupyCell(Char ch) {
//        super.occupyCell(ch);
//        GLog.p(String.valueOf(hero.pos));
//        GLog.b(String.valueOf(Statistics.zeroItemLevel));
//    }

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
            } else {
            return super.activateTransition(hero, transition);
        }
    }

    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = codedMap.clone();


        int exitCell = 317;
        LevelTransition exit = new LevelTransition(this, exitCell, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exit);

        int enterCell = 131;
        LevelTransition enter = new LevelTransition(this, enterCell, LevelTransition.Type.SURFACE);
        transitions.add(enter);

        LevelTransition bexits = new LevelTransition(this, 2615, LevelTransition.Type.BRANCH_ENTRANCE);
        transitions.add(bexits);

        CustomTilemap vis = new townBehind();
        vis.pos(0, 0);
        customTiles.add(vis);
        CustomTilemap via = new townAbove();
        via.pos(0, 0);
        customTiles.add(via);

        //map[exit] = Terrain.LOCKED_EXIT;

        return true;
    }

    public static class townBehind extends CustomTilemap {

        {
            texture = Assets.Environment.ICE_POX;

            tileW = 64;
            tileH = 64;
        }

        final int TEX_WIDTH = 64*16;

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
            texture = Assets.Environment.ICE_PO;

            tileW = 64;
            tileH = 64;
        }

        final int TEX_WIDTH = 64*16;

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
        return Assets.Environment.TILES_COLD_CS;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_CAVES;
    }

    @Override
    protected void createMobs() {

            REN n = new REN();
            n.pos = 252;
            mobs.add(n);

            Slyl npc1 = new Slyl();
            npc1.pos = 254;
            mobs.add(npc1);

            obSir npc2 = new obSir();
            npc2.pos = 380;
            mobs.add(npc2);

            NxhyNpc npc3 = new NxhyNpc();
            npc3.pos = 382;
            mobs.add(npc3);

            Question npc6 = new Question();
            npc6.pos = 3757;
            mobs.add(npc6);

            LanFire npc7 = new LanFire();
            npc7.pos = 3020;
            mobs.add(npc7);

            JIT npc8 = new JIT();
            npc8.pos = 2955;
            mobs.add(npc8);

            DeepSea npc10 = new DeepSea();
            npc10.pos = 2685;
            mobs.add(npc10);

            MoonLow npc11 = new MoonLow();
            npc11.pos = 2728;
            mobs.add(npc11);

            KongFu npc12 = new KongFu();
            npc12.pos = 3100;
            mobs.add(npc12);

        WhiteYan npc16 = new WhiteYan();
        npc16.pos = 3255;
        mobs.add(npc16);

            //45%概率
            if(Random.Float()<=0.45f){
                BzmdrLand npc9 = new BzmdrLand();
                npc9.pos = 3085;
                mobs.add(npc9);
                //如果地表版本生成 旅馆禁止生成Bzmdr
                Statistics.onlyBzmdr = true;
            }


        MoRuoS npc4 = new MoRuoS();
        npc4.pos = 3066;
        mobs.add(npc4);

        if(Random.Float()<=0.4f){
            Mint m = new Mint();
            m.pos = 3060;
            mobs.add(m);
        }


        SmallLeaf npc5 = new SmallLeaf();
        npc5.pos = 365;
        mobs.add(npc5);

        LuoWhite npc13 = new LuoWhite();
        npc13.pos = 1300;
        mobs.add(npc13);

        PaswordBadges.loadGlobal();
        List<PaswordBadges.Badge> passwordbadges = PaswordBadges.filtered(true);

//        if (passwordbadges.contains(PaswordBadges.Badge.FIREGIRL)) {
//            WaloKe npc14 = new WaloKe();
//            npc14.pos = 479;
//            mobs.add(npc14);
//        }

    }

    public static int[] SALEPOS_ONE = new int[]{
            1427,1428
    };

    public static int[] SALEPOS_TWO = new int[]{
          785,786
    };

    public static int[] SALEPOS_THREE = new int[]{
           1429,1365
    };

    public static int[] SALEPOS_FOUR = new int[]{
          849,850
    };

    public static int[] POSSALE = new int[]{
           657,658,721,722
    };

    @Override
    protected void createItems() {

        if (Dungeon.depth == 0 &&
                (!Document.ADVENTURERS_GUIDE.isPageRead(Document.GUIDE_INTRO) || SPDSettings.intro() )){
            drop( new Guidebook(),  132);
        }

        if (Badges.isUnlocked(Badges.Badge.NYZ_SHOP)){
            Nyz npc4= new Nyz();
            npc4.pos = 723;
            mobs.add(npc4);

            YetYog npc1= new YetYog();
            npc1.pos = 663;
            mobs.add(npc1);


            for (int i : SALEPOS_TWO) {
                drop((Generator.random(Generator.Category.POTION)), i).type =
                        Heap.Type.FOR_SALE;
            }

            for (int i : SALEPOS_FOUR) {
                drop((Generator.random(Generator.Category.SCROLL)), i).type =
                        Heap.Type.FOR_SALE;
            }
            for (int i : POSSALE) {
                drop((Generator.random(Generator.Category.SEED)), i).type =
                        Heap.Type.FOR_SALE;
            }
        }

        if(!Dungeon.isChallenged(CS)) {
            PaswordBadges.loadGlobal();
            List<PaswordBadges.Badge> passwordbadges = PaswordBadges.filtered(true);

            drop( new RandomChest(), 1425  ).type = Heap.Type.FOR_SALE;
            drop( new RandomChest(), 1426  ).type = Heap.Type.FOR_SALE;
            for (int i : SALEPOS_ONE) {
                drop((Generator.random(Generator.Category.MISSILE)), i).type =
                        Heap.Type.FOR_SALE;
            }
            for (int i : SALEPOS_THREE) {
                drop((Generator.random(Generator.Category.WEAPON)), i).type =
                        Heap.Type.FOR_SALE;
            }
            if (passwordbadges.contains(PaswordBadges.Badge.GODD_MAKE)) {
                drop((Generator.random(Generator.Category.RING)), 3001);
            }
            if (passwordbadges.contains(PaswordBadges.Badge.BIG_X)) {
                if (Dungeon.isChallenged(Challenges.NO_ARMOR)) {
                    drop((Generator.random(Generator.Category.WAND)), 3065);
                } else {
                    drop((Generator.random(Generator.Category.ARMOR)), 3065);
                }
            }

            if (Badges.isUnlocked(Badges.Badge.RLPT)) {
                Item item = new BookBag();
                drop(item, 3129);
            }

        }

        if(RegularLevel.holiday == RegularLevel.Holiday.CJ){
            drop( new Pasty(), 725  );
            drop( new Food(), 853  );
        }




        drop((Generator.randomUsingDefaults(Generator.Category.SCROLL)), 3193);
        drop((Generator.randomUsingDefaults(Generator.Category.SCROLL)), 2937);
    }

    public Mob createMob() {
        return null;
    }
    public int randomRespawnCell() {
        return this.entrance - width();
    }


}
