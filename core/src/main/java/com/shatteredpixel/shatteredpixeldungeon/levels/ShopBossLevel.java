package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.CrystalDiedTower;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.FireMagicDied;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.FireMagicDiedNPC;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NullDied;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NullDiedTO;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class ShopBossLevel extends Level {

    private static final int W = WALL;
    private static final int X = WATER;
    private static final int P = Terrain.EMPTY;
    private static final int G = Terrain.EMPTY;
    private static final int S = Terrain.CHASM;
    private static final int R = Terrain.EMPTY_SP;
    private static final int J = Terrain.PEDESTAL;
    private static final int K = Terrain.ALCHEMY;
    private static final int D = Terrain.STATUE;
    private static final int H = Terrain.WELL;

    //portals. (from, to).

    private static final int WIDTH = 35;
    private static final int HEIGHT = 35;

    @Override
    protected void createMobs() {

        NullDied csp = new NullDied();
        csp.pos = WIDTH * 17 + 17;
        mobs.add(csp);

    }

    @Override
    public void seal() {
        super.seal();

        for (int i : CryStalPosition) {
            CrystalDiedTower csp = new CrystalDiedTower();
            csp.pos = i;
            GameScene.add(csp);
        }

        FireMagicDied boss = new FireMagicDied();
        boss.pos = WIDTH*17 + 17;
        GameScene.add(boss);

        //activateAll();

        GLog.p(Messages.get(FireMagicDied.class,"go", hero.name()));
        Sample.INSTANCE.play(Assets.Sounds.DEATH);
    }

    public static int[] CryStalPosition = new int[]{
            WIDTH*22 + 17,
            WIDTH*12 + 17,
            WIDTH*17 + 12,
            WIDTH*17 + 22,
    };

    public static int TRUEPosition = WIDTH * 17 + 17;
    public static int FALSEPosition = WIDTH * 22 + 17;
    @Override
    public void unseal() {
        super.unseal();

        if(Dungeon.depth == 19){
            set(  this.entrance =  WIDTH*17 + 17, Terrain.EXIT );
            GameScene.updateMap( this.entrance =  WIDTH*17 + 17 );

            set(  this.entrance =  WIDTH*21 + 17, Terrain.ENTRANCE );
            GameScene.updateMap( this.entrance =  WIDTH*21 + 17 );
        } else {
            FireMagicDiedNPC boss = new FireMagicDiedNPC();
            boss.pos = WIDTH*17 + 17;
            GameScene.add(boss);

            NullDiedTO bossx = new NullDiedTO();
            bossx.pos = WIDTH*15 + 17;
            GameScene.add(bossx);
        }


    }

    @Override
    public void occupyCell( Char ch ) {
        super.occupyCell( ch );

        if (map[entrance] == Terrain.ENTRANCE && map[exit] != Terrain.EXIT
                && ch == hero && Dungeon.level.distance(ch.pos, entrance) >= 2) {
            seal();
        }

        if(ch == hero){
            //指定区域
            if(MAIN_PORTAL.containsKey(ch.pos)) {
                ScrollOfTeleportation.appear(ch, IF_MAIN_PORTAL.get(ch.pos));
                //传送目标区域
                hero.interrupt();
                Dungeon.observe();
                GameScene.updateFog();
            }
        }
    }

    private Mob getKing(){
        for (Mob m : mobs){
            if (m instanceof FireMagicDied) return m;
        }
        return null;
    }
    //四个基座
    private static final int[] pedestals = new int[4];
    public int getSummoningPos(){
        Mob king = getKing();
        //fixed
        HashSet<FireMagicDied.Summoning> summons = Objects.requireNonNull(king).buffs(FireMagicDied.Summoning.class);
        ArrayList<Integer> positions = new ArrayList<>();
        for (int pedestal : pedestals) {
            boolean clear = true;
            for (FireMagicDied.Summoning s : summons) {
                if (s.getPos() == pedestal) {
                    clear = false;
                    break;
                }
            }
            if (clear) {
                positions.add(pedestal);
            }
        }
        //if (positions.isEmpty()){
        //    return -1;
        //} else {
        //    return Random.element(positions);
        //}
        return -1;
    }

    public static final int thronex;

    //常量基座调用
    static {
        thronex = 1 + WIDTH* 17;
    }

    public static final int throne;

    //常量基座调用
    static {
        throne =  WIDTH*25 + 17;
    }

    public static final int throneling;

    //常量基座调用
    static {
        throneling =  WIDTH*17 + 17;
    }

    private static final HashMap<Integer, Integer> MAIN_PORTAL = new HashMap<>(4);
    {
        MAIN_PORTAL.put(10+10*WIDTH, 2+2*WIDTH);
        MAIN_PORTAL.put(1+WIDTH, throne);

        MAIN_PORTAL.put(24+10*WIDTH, 32+2*WIDTH);
        MAIN_PORTAL.put(33+WIDTH, throne);

        MAIN_PORTAL.put(10+24*WIDTH, 2+32*WIDTH);
        MAIN_PORTAL.put(1+33*WIDTH, throne);

        MAIN_PORTAL.put(24+24*WIDTH, 32+32*WIDTH);
        MAIN_PORTAL.put(33+33*WIDTH, throne);
    }

    private static final HashMap<Integer, Integer> IF_MAIN_PORTAL = new HashMap<>(4);
    {
        IF_MAIN_PORTAL.put(10+10*WIDTH, 2+2*WIDTH);
        IF_MAIN_PORTAL.put(1+WIDTH, throne);

        IF_MAIN_PORTAL.put(24+10*WIDTH, 32+2*WIDTH);
        IF_MAIN_PORTAL.put(33+WIDTH, throne);

        IF_MAIN_PORTAL.put(10+24*WIDTH, 2+32*WIDTH);
        IF_MAIN_PORTAL.put(1+33*WIDTH, throne);

        IF_MAIN_PORTAL.put(24+24*WIDTH, 32+32*WIDTH);
        IF_MAIN_PORTAL.put(33+33*WIDTH, throne);
    }


    private static final int[] code_map = {
            W,W,W,W,W,W,W,W,W,W,W,W,S,S,S,S,S,S,S,S,S,S,S,W,W,W,W,W,W,W,W,W,W,W,W,
            W,J,P,P,R,P,R,P,P,P,W,W,W,S,S,S,S,S,S,S,S,S,W,W,W,P,P,P,R,P,R,P,P,J,W,
            W,P,P,R,R,R,R,R,P,P,W,P,W,W,S,S,S,S,S,S,S,W,W,P,W,P,P,R,R,R,R,R,P,P,W,
            W,P,R,P,R,P,R,P,R,P,W,P,P,W,W,S,S,S,S,S,W,W,P,P,W,P,R,P,R,P,R,P,R,P,W,
            W,P,P,P,R,P,R,P,R,P,W,P,P,P,W,W,S,S,S,W,W,P,P,P,W,P,R,P,R,P,R,P,P,P,W,
            W,P,R,R,R,K,R,R,R,P,W,P,P,P,P,W,W,S,W,W,P,P,P,P,W,P,R,R,R,K,R,R,R,P,W,
            W,P,R,P,R,P,R,P,P,P,W,P,P,P,P,P,W,W,W,P,P,P,P,P,W,P,P,P,R,P,R,P,R,P,W,
            W,P,R,P,R,P,R,P,R,P,W,P,P,P,P,R,P,W,P,R,P,P,P,P,W,P,R,P,R,P,R,P,R,P,W,
            W,P,P,R,R,R,R,R,P,P,W,P,P,P,R,P,P,R,P,P,R,P,P,P,W,P,P,R,R,R,R,R,P,P,W,
            W,P,P,P,R,P,R,P,P,W,W,P,P,R,P,P,P,R,P,P,P,R,P,P,W,W,P,P,R,P,R,P,P,P,W,
            W,W,W,W,W,W,W,W,W,W,J,P,R,P,P,P,P,R,P,P,P,P,R,P,J,W,W,W,W,W,W,W,W,W,W,
            W,W,P,P,P,P,P,P,P,P,P,R,R,R,R,R,R,R,R,R,R,R,R,R,P,P,P,P,P,P,P,P,P,W,W,
            S,W,W,P,P,P,P,P,P,P,R,R,G,D,G,G,G,H,X,X,X,X,G,R,R,P,P,P,P,P,P,P,W,W,S,
            S,S,W,W,P,P,P,P,P,R,P,R,X,G,G,G,G,R,X,X,X,G,D,R,P,R,P,P,P,P,P,W,W,S,S,
            S,S,S,W,W,P,P,P,R,P,P,R,X,X,G,G,G,R,X,X,G,G,G,R,P,P,R,P,P,P,W,W,S,S,S,
            S,S,S,S,W,W,P,R,P,P,P,R,X,X,X,G,D,R,X,G,G,G,G,R,P,P,P,R,P,W,W,S,S,S,S,
            S,S,S,S,S,W,W,P,P,P,P,R,X,X,X,X,G,R,G,D,G,G,G,R,P,P,P,P,W,W,S,S,S,S,S,
            S,S,S,S,S,S,W,W,R,R,R,R,H,R,R,R,R,G,R,R,R,R,H,R,R,R,R,W,W,S,S,S,S,S,S,
            S,S,S,S,S,W,W,P,P,P,P,R,G,G,G,D,G,R,G,X,X,X,X,R,P,P,P,P,W,W,S,S,S,S,S,
            S,S,S,S,W,W,P,R,P,P,P,R,G,G,G,G,X,R,D,G,X,X,X,R,P,P,P,R,P,W,W,S,S,S,S,
            S,S,S,W,W,P,P,P,R,P,P,R,G,G,G,X,X,R,G,G,G,X,X,R,P,P,R,P,P,P,W,W,S,S,S,
            S,S,W,W,P,P,P,P,P,R,P,R,D,G,X,X,X,R,G,G,G,G,X,R,P,R,P,P,P,P,P,W,W,S,S,
            S,W,W,P,P,P,P,P,P,P,R,R,G,X,X,X,X,H,G,G,G,D,G,R,R,P,P,P,P,P,P,P,W,W,S,
            W,W,P,P,P,P,P,P,P,P,P,R,R,R,R,R,R,R,R,R,R,R,R,R,P,P,P,P,P,P,P,P,P,W,W,
            W,W,W,W,W,W,W,W,W,W,J,P,R,P,P,P,P,R,P,P,P,P,R,P,J,W,W,W,W,W,W,W,W,W,W,
            W,P,R,P,P,P,P,P,R,W,W,P,P,R,P,P,P,R,P,P,P,R,P,P,W,W,R,P,P,P,P,P,R,P,W,
            W,P,P,R,P,P,P,R,P,P,W,P,P,P,R,P,P,R,P,P,R,P,P,P,W,P,P,R,P,P,P,R,P,P,W,
            W,P,P,P,R,P,R,P,P,P,W,P,P,P,P,R,P,W,P,R,P,P,P,P,W,P,P,P,R,P,R,P,P,P,W,
            W,P,P,P,P,R,P,P,P,P,W,P,P,P,P,P,W,W,W,P,P,P,P,P,W,P,P,P,P,R,P,P,P,P,W,
            W,P,R,R,R,K,R,R,R,P,W,P,P,P,P,W,W,S,W,W,P,P,P,P,W,P,R,R,R,K,R,R,R,P,W,
            W,P,P,P,P,R,P,P,P,P,W,P,P,P,W,W,S,S,S,W,W,P,P,P,W,P,P,P,P,R,P,P,P,P,W,
            W,P,R,R,R,R,R,R,R,P,W,P,P,W,W,S,S,S,S,S,W,W,P,P,W,P,R,R,R,R,R,R,R,P,W,
            W,P,P,P,P,R,P,P,P,P,W,P,W,W,S,S,S,S,S,S,S,W,W,P,W,P,P,P,P,R,P,P,P,P,W,
            W,J,P,P,P,R,P,P,P,P,W,W,W,S,S,S,S,S,S,S,S,S,W,W,W,P,P,P,P,R,P,P,P,J,W,
            W,W,W,W,W,W,W,W,W,W,W,W,S,S,S,S,S,S,S,S,S,S,S,W,W,W,W,W,W,W,W,W,W,W,W,
    };

    {
        color1 = 5459774;
        color2 = 12179041;
    }

    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();

        this.entrance = WIDTH*25 + 17;
        exit = 0;
        return true;
    }

    protected void createItems() {
    }

    static {
        pedestals[0] = 10 + WIDTH * 10;

        pedestals[1] = 24  + WIDTH * 10;

        pedestals[2] = 10 + WIDTH * 24;

        pedestals[3] = 32 + WIDTH * 32;
    }

    public Mob createMob() {
        return null;
    }

    public int randomRespawnCell() {
        return this.entrance - width();
    }

    public Actor respawner() {
        return null;
    }

    public String tilesTex() {
            return Assets.Environment.TILES_DIED;
    }

    public String waterTex() {
            return Assets.Environment.WATER_CAVES;
    }

}

