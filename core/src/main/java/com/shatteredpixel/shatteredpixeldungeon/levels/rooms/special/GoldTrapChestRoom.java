package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.items.Generator.randomUsingDefaults;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BruteBot;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Crab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM201;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MobSpawner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MolotovHuntsman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Necromancer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SRPDHBLR;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Senior;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Slime_Orange;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Succubus;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.ApprenticeWitch;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Butcher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.Wisp;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;

public class GoldTrapChestRoom extends SpecialRoom {

    private final int width = 11;

    @Override
    public int minWidth() {
        return width;
    }
    @Override
    public int minHeight() {
        return width;
    }
    @Override
    public int maxWidth() {
        return width;
    }
    @Override
    public int maxHeight() {
        return width;
    }

    @Override
    public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
        return false;
    }

    @Override
    public boolean canConnect(Point p) {
        if (!super.canConnect(p)){
            return false;
        }
        //only place doors in the center
        if (Math.abs(p.x - (right - (width()-1)/2f)) < 1f){
            return true;
        }
        return Math.abs(p.y - (bottom - (height() - 1) / 2f)) < 1f;
    }

    @Override
    public boolean canPlaceTrap(Point p) {
        return false;
    }

    private static final int[] pre_map = {
            20,0,50,0,0,0,50,0,20,
            0,50,54,0,0,0,54,50,0,
            50,50,72,4,4,4,72,50,50,
            48,50,4,4,24,4,4,50,48,
            0,115,4,24,19,24,4,115,0,
            48,50,4,4,24,4,4,50,48,
            50,50,72,4,4,4,72,50,50,
            0,50,54,0,0,0,54,50,0,
            20,0,50,0,0,0,50,0,20,
    };

    private int codeToTerrain(int code){
        switch (code){
            case 50:case 54:
                return Terrain.BOOKSHELF;
            case 72:
                return Terrain.STATUE;
            case 19: case 20:
                return Terrain.PEDESTAL;
            case 4:
                return Terrain.EMPTY_SP;
            case 24:
                return Terrain.WATER;
            case 48:
                return Terrain.WALL;
            case 115:
                return Terrain.DOOR;
            default:
                return Terrain.EMPTY;
        }
    }

    private ArrayList<Generator.Category> prizeClasses = new ArrayList<>(
            Arrays.asList(Generator.Category.WAND,
                    Generator.Category.RING,
                    Generator.Category.ARTIFACT));

    private Item prize() {
        Generator.Category cat = prizeClasses.remove(0);
        prizeClasses.add(cat);
        Item prize;
        do {
            prize = Generator.random(cat);
        } while (prize == null || Challenges.isItemBlocked(prize));
        return prize;
    }

    @Override
    public void paint(Level level) {
        level.addItemToSpawn( new GoldenKey( Dungeon.depth ) );
        Random.shuffle(prizeClasses);
        Painter.fill(level,this, 0,WALL);

        for (int i = left + 1; i <= right-1; i++) {
            for (int j = top + 1; j <= bottom-1; j++) {
                int dx = i - (left + 1);
                int dy = j - (top + 1);
                int index = dy * (minWidth()-2) + dx;

                if(index >= 0 && index < pre_map.length){
                    set(level, i, j, codeToTerrain(pre_map[index]));
                } else {
                    set(level, i, j, Terrain.EMPTY);
                }
            }
        }

        int centerX = left + width() / 2;
        int centerY = top + height() /2;
        Point xpos = new Point(centerX, centerY);
        int RPos = left + right - xpos.x + xpos.y * level.width();
        level.map[RPos] = Terrain.TRAP;
        level.setTrap(new AlarmTrap(), RPos);

        level.drop(prize(), RPos).type = Heap.Type.LOCKED_CHEST;

        Point lpos = new Point(centerX-4, centerY);
        int LeftTombPos = left + right - lpos.x + lpos.y * level.width();
        level.drop(randomUsingDefaults( Generator.Category.POTION ), LeftTombPos).type = Heap.Type.TOMB;

        Point rpos = new Point(centerX+4, centerY);
        int RightTombPos = left + right - rpos.x + rpos.y * level.width();
        level.drop(randomUsingDefaults( Generator.Category.STONE ), RightTombPos).type = Heap.Type.TOMB;

        Point apos = new Point(centerX+4, centerY+4);
        Point bpos = new Point(centerX+4, centerY-4);
        Point cpos = new Point(centerX-4, centerY+4);
        Point dpos = new Point(centerX-4, centerY-4);

        //四大恶人
        int[] MBTPOS = new int[]{
            left + right - apos.x + apos.y * level.width(),
            left + right - bpos.x + bpos.y * level.width(),
            left + right - cpos.x + cpos.y * level.width(),
            left + right - dpos.x + dpos.y * level.width()
        };

        for (int i : MBTPOS) {
            Mob n = Random.Float()<=0.05f ? new Slime_Orange() :new Crab();
            if(depth >= 25) {
                n = Random.Float() <= 0.05f ? new Butcher() : new ApprenticeWitch();
            } else if(depth >= 20){
                n = Random.Float()<=0.05f ? new Succubus() : new Eye();
            } else if(depth >= 15) {
                n = Random.Float()<=0.05f ? new Senior() : new BruteBot();
            } else if(depth >= 10) {
                n = Statistics.Tomb_Reach ? new Wisp() : Random.Float()<=0.05f ? new MolotovHuntsman() :new DM201();
            } else if(depth >= 6){
                n = Random.Float()<=0.05f ? new Necromancer() : new SRPDHBLR();
            }

            n.pos = i;
            level.mobs.add(n);
        }

        entrance().set(Door.Type.HIDDEN);
    }

    private void set(Level level, int x, int y, int value) {
        level.map[x + y * level.width()] = value;
    }

    public static class AlarmTrap extends Trap {
        private GoldTrapChestRoom room;
        {
            color = RED;
            shape = DOTS;
        }

        @Override
        public void activate() {

            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                mob.beckon( pos );
            }

            if(pos == hero.pos){
                for (int n : PathFinder.NEIGHBOURS4) {
                    int cell = pos + n;
                    Mob mob = Reflection.newInstance(MobSpawner.getMobRotation(Random.IntRange(Dungeon.depth,Math.min(Dungeon.depth+2,24))).get(0));
                    mob.pos = cell;
                    GameScene.add(mob);
                }
                PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid, null ), 2 );
                for (int i = 0; i < PathFinder.distance.length; i++) {
                    if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                        if (Dungeon.level.pit[i] || Dungeon.level.water[i])
                            GameScene.add(Blob.seed(i, 1, Fire.class));
                        else
                            GameScene.add(Blob.seed(i, 5, Fire.class));
                        CellEmitter.get(i).burst(FlameParticle.FACTORY, 5);
                    }
                }
                Sample.INSTANCE.play(Assets.Sounds.BURNING);
            }



            if (Dungeon.level.heroFOV[pos]) {
                GLog.w( Messages.get(this, "alarm") );
                CellEmitter.center( pos ).start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
            }

            Sample.INSTANCE.play( Assets.Sounds.ALERT );
        }
    }


}

