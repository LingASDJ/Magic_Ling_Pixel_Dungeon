package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArcaneArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.Cerberus;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.CrivusFruits;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DeathRong;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.ZeroBoat;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SentryRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Halo;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CerDogBossLevel extends Level{

    private static final int W = WALL;
    private static final int E = EMPTY_SP;
    private static final int R = EMPTY;
    private static final int V = Terrain.ENTRANCE;
    private static final int S = Terrain.CHASM;
    private static final int T= Terrain.HIGH_GRASS;
    private static final int M =Terrain.WATER;
    private static final int D= Terrain.DOOR;

    private static final int X= Terrain.WALL_DECO;
    private static final int N =Terrain.STATUE;

    public static int[] FireWallDied = new int[]{
            229,230,231,232,233,234,235,
            267,298,329,360,391,422,453,
            477,478,479,480,481,482,483,
            445,414,383,352,321,290,259,
    };

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_HOLLOW;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_HOLLOW;
    }

    private static final int getBossDoor = 697;
    private static final int LDBossDoor = 666;

    @Override
    public void occupyCell(Char ch) {
        super.occupyCell(ch);

        boolean isTrue = ch.pos == LDBossDoor && ch == hero && level.distance(ch.pos, entrance) >= 2;

        //如果有生物来到BossDoor的下一个坐标，且生物是玩家，那么触发seal().
        if (map[getBossDoor] == Terrain.DOOR && isTrue || map[getBossDoor] == Terrain.EMBERS && isTrue) {
            seal();

        }


    }

    @Override
    public int randomRespawnCell( Char ch ) {
        int pos = 1434;
        int cell;
        do {
            cell = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
        } while (!passable[cell]
                || (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
                || Actor.findChar(cell) != null);
        return cell;
    }

    @Override
    public void unseal() {
        super.unseal();

        set(46, Terrain.EXIT);
        GameScene.updateMap(46);

        LevelTransition exit = new LevelTransition(this,46, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exit);

        set(getBossDoor, Terrain.EMPTY);
        GameScene.updateMap(getBossDoor);

        Dungeon.observe();
    }

    private static int[] FirstPos = new int[]{259,453,235,477};
    private static int[] EndPos = new int[]{386,326,324,388};

    @Override
    public void seal() {
        super.seal();

        int doorPos = 449;

        for (Mob mob : level.mobs.toArray(new Mob[0])){
            if ( mob instanceof DeathRong) {
                if(((DeathRong) mob).secnod){
                    ((DeathRong) mob).secnod = false;
                    ((DeathRong) mob).rd = false;
                }
            }
        }

        Buff.affect( hero, MindVision.class, MindVision.DURATION );

        //Dungeon.observe();
        set( getBossDoor, Terrain.WALL );
        GameScene.updateMap( getBossDoor );

        Mob.holdAllies(this, doorPos);
        Mob.restoreAllies(this, Dungeon.hero.pos, doorPos);

        hero.sprite.jump(getBossDoor, 449, new Callback() {
            @Override
            public void call() {
                ScrollOfTeleportation.appear(hero, doorPos);
            }
        });

        for (Mob mob : level.mobs.toArray(new Mob[0])){
            if (mob instanceof YellowStar){

                mob.sprite.jump(46, 356, 145, 12f,new Callback() {
                    @Override
                    public void call() {
                        // This is Project,Shuold May be Can Work...


                        mob.sprite.parent.add(new Chains(FirstPos[0], EndPos[0], Effects.Type.RED_CHAIN,null));

                        mob.sprite.parent.add(new Chains(FirstPos[1], EndPos[1], Effects.Type.RED_CHAIN,null));

                        mob.sprite.parent.add(new Chains(FirstPos[2], EndPos[2], Effects.Type.RED_CHAIN, null));

                        mob.sprite.parent.add(new Chains(FirstPos[3], EndPos[3], Effects.Type.RED_CHAIN, null));

                        mob.sprite.parent.add(new Chains(FirstPos[0], EndPos[1], Effects.Type.RED_CHAIN, null));

                        mob.sprite.parent.add(new Chains(FirstPos[1], EndPos[2], Effects.Type.RED_CHAIN, null));

                        mob.sprite.parent.add(new Chains(FirstPos[2], EndPos[0], Effects.Type.RED_CHAIN,null));

                        mob.sprite.parent.add(new Chains(FirstPos[3], EndPos[0], Effects.Type.RED_CHAIN,new Callback() {
                            @Override
                            public void call() {
                                mob.destroy();
                                mob.sprite.killAndErase();
                                Camera.main.shake(2f, 10f);
                                Cerberus ncx = new Cerberus();
                                ncx.pos = 356;
                                ncx.notice();

                                Buff.detach( hero, MindVision.class );

                                for (Mob mob : level.mobs.toArray(new Mob[0])){
                                    if ( mob instanceof DeathRong) {
                                        if(!((DeathRong) mob).rd){
                                            Buff.affect( ncx, DiedCrused.class);
                                            DeathRong.tell(Messages.get(DeathRong.class, "fuck",hero.name()));
                                        }
                                    }
                                }

                                ncx.state = ncx.WANDERING;
                                GameScene.add(ncx);
                            }
                        }));


                    }
                });


            }
        }



    }


    public static class DiedCrused extends ChampionEnemy {

        {
            color = 0x8f8f8f;
        }

        @Override
        public boolean act() {
            if (target.isAlive()) {

                Buff.affect(target, CrivusFruits.CFBarrior.class).setShield(((50)));

                Buff.affect(target, ArcaneArmor.class).set(50,1);

                spend(50f);
            }

            return true;
        }

        @Override
        public float meleeDamageFactor() {
            return 1.20f;
        }

        @Override
        public float speedFactor() {
            return super.speedFactor()*2.3f;
        }


    }


    public static class townBehind extends CustomTilemap {

        {
            texture = Assets.Environment.HOLLOW_OP;

            tileW = 31;
            tileH = 50;
        }

        final int TEX_WIDTH = 31*16;

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
            texture = Assets.Environment.HOLLOW_PO;

            tileW = 31;
            tileH = 50;
        }

        final int TEX_WIDTH = 31*16;

        @Override
        public Tilemap create() {

            Tilemap v = super.create();

            int[] data = mapSimpleImage(0, 0, TEX_WIDTH);

            v.map(data, tileW);
            return v;
        }

    }


    private static final int WIDTH = 31;
    private static final int HEIGHT = 50;
    private static final int[] code_map = {
            S,S,S,S,S,S,S,S,S,W,W,X,W,W,W,X,W,W,W,X,W,W,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,W,W,W,W,R,R,R,R,R,R,R,R,R,R,R,W,W,W,W,S,S,S,S,S,S,
            S,S,S,S,W,W,W,R,T,R,R,W,W,R,R,R,R,R,W,W,R,R,T,R,X,W,W,S,S,S,S,
            S,S,S,W,W,R,T,R,R,W,R,W,X,R,R,R,R,R,X,W,R,W,R,R,T,T,W,W,S,S,S,
            S,S,W,X,R,T,R,X,R,R,R,N,R,R,R,R,R,R,R,N,R,R,R,X,R,R,R,X,W,S,S,
            S,W,W,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,T,R,T,W,W,S,
            S,W,R,T,W,W,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,W,W,R,R,W,S,
            W,X,R,R,W,W,N,R,R,R,R,X,R,R,R,R,R,R,R,X,R,R,R,R,N,X,W,R,T,W,W,
            W,R,T,R,R,R,R,R,R,R,N,R,R,R,R,R,R,R,R,R,N,R,R,R,R,R,R,R,T,T,W,
            W,R,R,W,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,W,R,R,W,
            W,R,T,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,T,R,W,
            W,T,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,T,W,
            W,R,T,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,T,R,W,
            W,R,R,W,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,W,R,R,W,
            W,R,R,R,R,R,R,R,R,R,N,R,R,R,R,R,R,R,R,R,N,R,R,R,R,R,R,R,R,T,W,
            X,W,T,R,W,W,N,R,R,R,R,X,R,R,R,R,R,R,R,X,R,R,R,R,N,W,W,R,T,W,X,
            S,W,R,R,W,X,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,X,W,T,R,W,S,
            S,W,W,R,T,R,T,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,T,R,R,R,W,W,S,
            S,S,X,W,R,T,R,X,R,R,R,N,R,R,R,R,R,R,R,N,R,R,R,X,R,T,R,W,X,S,S,
            S,S,S,W,W,R,R,T,R,W,R,W,W,R,R,R,R,R,W,W,R,W,R,T,R,R,W,W,S,S,S,
            S,S,S,S,X,W,W,R,R,R,R,W,X,R,R,R,R,R,X,W,R,R,R,R,W,W,X,S,S,S,S,
            S,S,S,S,S,S,X,W,W,W,R,R,R,R,R,R,R,R,R,R,R,W,W,W,X,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,W,W,W,W,X,W,D,W,X,W,W,W,W,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,M,M,M,R,N,R,N,R,M,M,M,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,M,M,R,R,R,M,M,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,M,M,E,M,M,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,E,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,E,S,S,S,E,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,E,E,E,E,E,S,S,S,E,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,E,S,S,S,E,S,S,S,E,E,E,E,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,E,E,E,E,S,S,S,E,S,S,S,E,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,E,S,S,S,E,E,E,E,E,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,E,S,S,S,E,S,S,E,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,E,S,S,E,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,E,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,W,W,W,X,W,W,W,X,W,W,E,S,S,S,S,S,S,S,S,S,S,S,S,S,
            S,S,S,S,S,S,W,W,R,T,T,R,R,R,M,M,M,E,S,S,S,S,S,E,S,S,S,S,S,S,S,
            S,S,S,S,S,S,W,R,R,R,T,T,M,M,M,R,M,E,S,S,S,S,S,E,S,S,S,S,S,S,S,
            S,S,S,S,S,S,W,R,V,R,R,R,R,E,E,E,E,E,E,E,E,E,E,E,E,E,S,S,S,S,S,
            S,S,S,S,S,S,W,R,R,R,R,T,T,R,M,M,M,M,S,S,E,S,S,E,S,S,S,S,S,S,S,
            S,S,S,S,S,S,W,W,R,T,T,T,R,M,M,M,S,S,S,S,E,S,S,E,S,S,S,S,S,S,S,
            S,S,S,S,S,S,S,W,W,W,W,W,W,W,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,S,
    };

    @Override
    public Group addVisuals() {
        super.addVisuals();
        addLanterVisuals(this, visuals);
        return visuals;
    }

    {
        viewDistance = 10;
    }

    public static void addLanterVisuals(Level level, Group group){
        for (int i=0; i < level.length(); i++) {
            if (level.map[i] == Terrain.WALL_DECO) {
                group.add( new PumpLanter( i ) );
            }
        }
    }


    public static class PumpLanter extends Emitter {

        private int pos;

        public PumpLanter( int pos ) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld( pos );
//            for (int i=0; i){
//
//            }
//            pos( p.x - 4, p.y + 2, 2, 0 );
//
//            pour( FlameParticle.FACTORY, 0.15f );

            add( new Halo( 12, 0xFFa500, 0.3f ).point( p.x, p.y + 1 ) );
        }

        @Override
        public void update() {
            if (visible == (pos < level.heroFOV.length && level.heroFOV[pos])) {
                super.update();
            }
        }
    }

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = code_map.clone();
        int enter = 1434;

        LevelTransition ent = new LevelTransition(this, enter, LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(ent);



        CustomTilemap vis = new townBehind();
        vis.pos(0, 0);
        customTiles.add(vis);
        CustomTilemap via = new townAbove();
        via.pos(0, 0);
        customTiles.add(via);


        return true;
    }



    /**
     *
     */
    @Override
    public Mob createMob() {
        return null;
    }

    /**
     *
     */
    @Override
    protected void createMobs() {
        DeathRong n = new DeathRong();
        n.pos = 1355;
        mobs.add(n);

        ZeroBoat nc = new ZeroBoat();
        nc.pos = 1357;
        mobs.add(nc);

        YellowStar ncx = new YellowStar();
        ncx.pos = 46;
        mobs.add(ncx);

    }

    /**
     *
     */
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

    public static class YellowStar extends NPC {

        {
            spriteClass = YowLingSprite.class;
            properties.add(Property.IMMOVABLE);
        }

        @Override
        public int defenseSkill( Char enemy ) {
            return INFINITE_EVASION;
        }

        @Override
        public void damage( int dmg, Object src ) {
            //do nothing
        }

        @Override
        public boolean add(Buff buff ) {
            return false;
        }

        @Override
        public boolean reset() {
            return true;
        }

        @Override
        public boolean interact(Char c) {
            for (Mob mob : level.mobs.toArray(new Mob[0])){
                if (mob instanceof YellowStar){

                    mob.sprite.jump(46, 356, 145, 12f,new Callback() {
                        @Override
                        public void call() {
                            // This is Project,Shuold May be Can Work...


                            mob.sprite.parent.add(new Chains(FirstPos[0], EndPos[0], Effects.Type.RED_CHAIN,null));

                            mob.sprite.parent.add(new Chains(FirstPos[1], EndPos[1], Effects.Type.RED_CHAIN,null));

                            mob.sprite.parent.add(new Chains(FirstPos[2], EndPos[2], Effects.Type.RED_CHAIN, null));

                            mob.sprite.parent.add(new Chains(FirstPos[3], EndPos[3], Effects.Type.RED_CHAIN, null));

                            mob.sprite.parent.add(new Chains(FirstPos[0], EndPos[1], Effects.Type.RED_CHAIN, null));

                            mob.sprite.parent.add(new Chains(FirstPos[1], EndPos[2], Effects.Type.RED_CHAIN, null));

                            mob.sprite.parent.add(new Chains(FirstPos[2], EndPos[0], Effects.Type.RED_CHAIN,null));

                            mob.sprite.parent.add(new Chains(FirstPos[3], EndPos[0], Effects.Type.RED_CHAIN,new Callback() {
                                @Override
                                public void call() {
                                    mob.destroy();
                                    mob.sprite.killAndErase();
                                    Camera.main.shake(2f, 10f);
                                    Cerberus ncx = new Cerberus();
                                    ncx.pos = 356;
                                    ncx.notice();

                                    Buff.detach( hero, MindVision.class );

                                    for (Mob mob : level.mobs.toArray(new Mob[0])){
                                        if ( mob instanceof DeathRong) {
                                            if(!((DeathRong) mob).rd){
                                                Buff.affect( ncx, DiedCrused.class);
                                                DeathRong.tell(Messages.get(DeathRong.class, "fuck",hero.name()));
                                            }
                                        }
                                    }

                                    if(Statistics.difficultyDLCEXLevel == 3){
                                        Class<?extends ChampionEnemy> buffCls2;
                                        switch (Random.Int(5)){
                                            case 0: default:    buffCls2 = ChampionEnemy.Middle.class;      break;
                                            case 1:             buffCls2 = ChampionEnemy.Bomber.class;      break;
                                            case 2:             buffCls2 = ChampionEnemy.Sider.class;       break;
                                            case 3:             buffCls2 = ChampionEnemy.LongSider.class;   break;
                                            case 4:             buffCls2 = ChampionEnemy.Big.class;         break;
                                        }
                                        Buff.affect(ncx, buffCls2);
                                        Class<?extends ChampionEnemy> buffCls;
                                        switch (Random.Int(9)){
                                            case 0: default:    buffCls = ChampionEnemy.Blazing.class;      break;
                                            case 1:             buffCls = ChampionEnemy.Projecting.class;   break;
                                            case 2:             buffCls = ChampionEnemy.AntiMagic.class;    break;
                                            case 3:             buffCls = ChampionEnemy.Giant.class;        break;
                                            case 4:             buffCls = ChampionEnemy.Blessed.class;      break;
                                            case 5:             buffCls = ChampionEnemy.Growing.class;      break;
                                            case 6:             buffCls = ChampionEnemy.Halo.class;      	break;
                                            case 7:             buffCls = ChampionEnemy.DelayMob.class;     break;
                                        }
                                        Buff.affect(ncx, buffCls);
                                    } else if (Statistics.difficultyDLCEXLevel == 2){
                                        Class<?extends ChampionEnemy> buffCls;
                                        switch (Random.Int(9)){
                                            case 0: default:    buffCls = ChampionEnemy.Blazing.class;      break;
                                            case 1:             buffCls = ChampionEnemy.Projecting.class;   break;
                                            case 2:             buffCls = ChampionEnemy.AntiMagic.class;    break;
                                            case 3:             buffCls = ChampionEnemy.Giant.class;        break;
                                            case 4:             buffCls = ChampionEnemy.Blessed.class;      break;
                                            case 5:             buffCls = ChampionEnemy.Growing.class;      break;
                                            case 6:             buffCls = ChampionEnemy.Halo.class;      	break;
                                            case 7:             buffCls = ChampionEnemy.DelayMob.class;     break;
                                        }
                                        Buff.affect(ncx, buffCls);
                                    }


                                    ncx.state = ncx.WANDERING;
                                    GameScene.add(ncx);
                                }
                            }));


                        }
                    });


                }
            }
            return true;
        }

    }



    public static class YowLingSprite extends MobSprite {

        private Animation charging;
        private Emitter chargeParticles;

        public YowLingSprite(){
            texture( Assets.Sprites.YOW_SENTRY );

            idle = new Animation(1, true);
            idle.frames(texture.uvRect(0, 0, 8, 15));

            run = idle.clone();
            attack = idle.clone();
            charging = idle.clone();
            die = idle.clone();
            zap = idle.clone();

            play( idle );
        }

        @Override
        public void zap( int pos ) {
            idle();
            flash();
            emitter().burst(MagicMissile.WardParticle.UP, 2);
            if (Actor.findChar(pos) != null){
                parent.add(new Beam.DeathRay(center(), Actor.findChar(pos).sprite.center()));
            } else {
                parent.add(new Beam.DeathRay(center(), DungeonTilemap.raisedTileCenterToWorld(pos)));
            }
            ((SentryRoom.Sentry)ch).onZapComplete();
        }

        @Override
        public void link(Char ch) {
            super.link(ch);

            chargeParticles = centerEmitter();
            chargeParticles.autoKill = false;
            chargeParticles.pour(MagicMissile.WardParticle.UP, 0.04f);

            chargeParticles.on = false;

            play(charging);
        }

        @Override
        public void die() {
            super.die();
            if (chargeParticles != null){
                chargeParticles.on = false;
            }
        }

        @Override
        public void kill() {
            super.kill();
            if (chargeParticles != null){
                chargeParticles.killAndErase();
            }
        }

        public void charge(){
            play(charging);
            if (visible) Sample.INSTANCE.play( Assets.Sounds.CHARGEUP );
        }

        @Override
        public void play(Animation anim) {
            if (chargeParticles != null) chargeParticles.on = anim == charging;
            super.play(anim);
        }

        private float baseY = Float.NaN;

        @Override
        public void place(int cell) {
            super.place(cell);
            baseY = y;
        }

        @Override
        public void turnTo(int from, int to) {
            //do nothing
        }
        private float time;
        @Override
        public void update() {
            super.update();
            if (chargeParticles != null){
                chargeParticles.pos( center() );
                chargeParticles.visible = visible;
            }

            if (flashTime <= 0) {
                time += Game.elapsed / 3.5f;
                float r = 0.33f+0.57f*Math.max(0f, (float)Math.sin( time));
                float g = 0.53f+0.57f*Math.max(0f, (float)Math.sin( time + 2*Math.PI/3 ));
                float b = 0.63f+0.57f*Math.max(0f, (float)Math.sin( time + 4*Math.PI/3 ));
                tint( r,g,b, 0.3f);
            }

            if (!paused){
                if (Float.isNaN(baseY)) baseY = y;
                y = baseY + (float) Math.sin(Game.timeTotal);
                shadowOffset = 0.25f - 0.8f*(float) Math.sin(Game.timeTotal);
            }
        }

    }

}
