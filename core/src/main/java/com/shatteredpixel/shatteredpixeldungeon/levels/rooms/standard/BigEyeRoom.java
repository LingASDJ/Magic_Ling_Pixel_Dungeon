package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Piranha;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;

public class BigEyeRoom extends SpecialRoom {
	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		return false;
	}
    @Override
    public int minWidth() {
        return 13;
    }

    @Override
    public int minHeight() {
        return 11;
    }

    @Override
    public int maxWidth() {
        return 13;
    }

    @Override
    public int maxHeight() {
        return 11;
    }

    private ArrayList<Generator.Category> prizeClasses = new ArrayList<>(
            Arrays.asList(Generator.Category.WAND,
                    Generator.Category.RING,
                    Generator.Category.ARTIFACT,
                    Generator.Category.WEAPON));

    private Item prize() {
        Generator.Category cat = prizeClasses.remove(0);
        prizeClasses.add(cat);
        Item prize;
        do {
            prize = Generator.random(cat);
            prize.level(Random.NormalIntRange(0,2));
            prize.cursed = false;
        } while (Challenges.isItemBlocked(prize));
        return prize;
    }


    @Override
    public void paint(Level level) {
        Painter.fill(level,this,1, EMPTY);
        Painter.fill(level,this, 0,WALL);
        Painter.fill(level,this,2, WALL);
        Painter.fill(level,this,3, WATER);
        Painter.fill(level,this,4, EMPTY_SP);

        for (Room.Door door : connected.values()) {
            door.set( Room.Door.Type.REGULAR );
        }




        int centerX = left + width() / 2;
        int centerY = top + height() / 2;

        Point a = new Point(centerX+5, centerY);
        Point b = new Point(centerX-5, centerY);

        int LeftPos = (left + right) - a.x + a.y * level.width();

        int RightPos = (left + right) - b.x + b.y * level.width();

        level.drop( ( Generator.random( Generator.Category.WEAPON )),LeftPos).type = Heap.Type.WHITETOMB;

        level.drop( ( Generator.random( Generator.Category.FOOD )),RightPos).type = Heap.Type.TOMB;

        Painter.set(level, centerX, centerY, Terrain.PEDESTAL);



        Painter.drawRectangle(level, new Point(centerX, centerY), 11, 9, EMPTY_SP,false,CHASM);

        Painter.set(level, centerX, centerY+3, Terrain.WALL_DECO);
        Painter.set(level, centerX, centerY-3, Terrain.LOCKED_DOOR);

        Painter.set(level, centerX-2, centerY, EMPTY_SP);
        Painter.set(level, centerX+2, centerY, EMPTY_SP);


        Point c = new Point(centerX-3, centerY -2);
        Point d = new Point(centerX+3, centerY -2);

        Point e = new Point(centerX-3, centerY +2);
        Point f = new Point(centerX+3, centerY +2);

        Point g = new Point(centerX-3, centerY);
        Point h = new Point(centerX+3, centerY);

        //四大恶人
        int[] MBTPOS = new int[]{
             (left + right) - c.x + c.y * level.width(),
             (left + right) - d.x + d.y * level.width(),
             (left + right) - e.x + e.y * level.width(),
             (left + right) - f.x + f.y * level.width(),
        };

        //二大天鱼
        int[] FTPOS = new int[]{
                (left + right) - g.x + g.y * level.width(),
                (left + right) - h.x + h.y * level.width(),
        };

        for (int i : MBTPOS) {
            Mob n = new Eye();
            n.HP = n.HT = 20;
            n.pos = i;
            level.mobs.add(n);
        }
        for (int i : FTPOS) {
            Mob fish = new Piranha();
            fish.pos = i;
            level.mobs.add(fish);
        }

        Point i = new Point(centerX+1, centerY+1);
        Point j = new Point(centerX-1, centerY+1);
        Point k = new Point(centerX+1, centerY-1);
        Point l = new Point(centerX-1, centerY-1);

        int[] BTPOS = new int[]{
                (left + right) - i.x + i.y * level.width(),
                (left + right) - j.x + j.y * level.width(),
                (left + right) - k.x + k.y * level.width(),
                (left + right) - l.x + l.y * level.width(),
        };

        for (int is : BTPOS) {
            Mob mob = Reflection.newInstance(Bestiary.getMobRotation(Random.NormalIntRange(15, 29)).get(0));
            mob.pos = is;
            level.mobs.add(mob);
        }

        Point m = new Point(centerX+1, centerY);
        Point n = new Point(centerX-1, centerY);
        Point o = new Point(centerX, centerY-1);
        Point p = new Point(centerX, centerY+1);

        int[] XTPOS = new int[]{
                (left + right) - m.x + m.y * level.width(),
                (left + right) - n.x + n.y * level.width(),
                (left + right) - o.x + o.y * level.width(),
                (left + right) - p.x + p.y * level.width(),
        };

        for (int is : XTPOS) {
            level.drop(prize(), is).type = Heap.Type.CHEST;
        }
    }
}

