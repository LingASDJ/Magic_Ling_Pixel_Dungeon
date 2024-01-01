package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.CHASM;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMBERS;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.IceGolem;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.ApprenticeWitch;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Butcher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Frankenstein;
import com.shatteredpixel.shatteredpixeldungeon.items.EnergyCrystal;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.BlackKey;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;

public class PumpkinRoom extends SpecialRoom {

    @Override
    public int minWidth() {
        return 21;
    }

    @Override
    public int minHeight() {
        return 21;
    }

    @Override
    public int maxWidth() {
        return 21;
    }

    @Override
    public int maxHeight() {
        return 21;
    }

    private ArrayList<Generator.Category> prizeClasses = new ArrayList<>(
            Arrays.asList(Generator.Category.RING));

    public static final ArrayList<Class<?extends Mob>> RARE = new ArrayList<>(Arrays.asList(
            Frankenstein.class, Butcher.class, ApprenticeWitch.class, IceGolem.class));

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
        Painter.fill( level, this, Terrain.WALL );

        Painter.fillEllipse( level, this, 1 , Terrain.EMPTY_SP );

        for (Door door : connected.values()) {
            door.set( Door.Type.REGULAR );
            if (door.x == left || door.x == right){
                Painter.drawInside(level, this, door, width()/2, Terrain.EMPTY_SP);
            } else {
                Painter.drawInside(level, this, door, height()/2, Terrain.EMPTY_SP);
            }
        }

        Painter.fillEllipse( level, this, 3, Terrain.CHASM);

        // 绘制恶魔的鼻子
        int centerX = left + width() / 2;
        int centerY = top + height() / 2;

        Painter.set(level, centerX, centerY, Terrain.ALCHEMY);

        Painter.set(level, centerX, centerY+1, EMPTY_SP);
        Point pos = new Point(centerX, centerY + 1);
        int energyCrystal = left + right - pos.x + pos.y * level.width();
        level.drop( new EnergyCrystal().random(), energyCrystal );

        Painter.set(level, centerX-1, centerY+5, EMPTY_SP);
        Painter.set(level, centerX+1, centerY+5, EMPTY_SP);

        //先画出3个⚪
        Painter.drawCircle(level, new Point(centerX - 3, centerY - 3), 2, EMPTY_SP);
        Painter.drawCircle(level, new Point(centerX - 3, centerY - 3), 1, CHASM);
        Painter.drawCircle(level, new Point(centerX - 3, centerY - 3), 0, EMBERS);

        Painter.drawCircle(level, new Point(centerX + 3, centerY - 3), 2, EMPTY_SP);
        Painter.drawCircle(level, new Point(centerX + 3, centerY - 3), 1, CHASM);

        Painter.drawCircle(level, new Point(centerX + 3, centerY - 3), 0, EMBERS);

        //以中心点的3x3矩形 为裂缝
        Painter.drawRectangle(level, new Point(centerX - 3, centerY - 3), 3, 3, CHASM,false,0);
        Painter.drawRectangle(level, new Point(centerX + 3, centerY - 3), 3, 3, CHASM,false,0);

        //绘制恶魔的眼睛
        Painter.drawRectangle(level, new Point(centerX - 3, centerY - 3), 5, 5, EMPTY_SP,true,CHASM);
        Painter.drawRectangle(level, new Point(centerX + 3, centerY - 3), 5, 5, EMPTY_SP,true,CHASM);

        //绘制恶魔的眼泪
        Painter.drawVerticalLine(level, new Point(centerX-5,centerY-1),6,WATER);
        Painter.drawVerticalLine(level, new Point(centerX-2,centerY-1),2,WATER);
        Painter.drawVerticalLine(level, new Point(centerX+5,centerY-1),6,WATER);

        Painter.drawVerticalLine(level, new Point(centerX+2,centerY-1),8,WATER);
        Painter.set(level, new Point(centerX+2,centerY),CHASM);

        Painter.drawVerticalLine(level, new Point(centerX+3,centerY),2,WATER);
        Painter.set(level, new Point(centerX+3,centerY+1),CHASM);

        //绘制恶魔的牙齿
        Painter.drawHorizontalLine(level, new Point(centerX-3,centerY+3),6,EMPTY_SP);
        Painter.drawHorizontalLineWithGaps(level, new Point(centerX-4,centerY+2),9,EMPTY_SP);
        Painter.drawHorizontalLineWithGaps(level, new Point(centerX-3,centerY+4),8,EMPTY_SP);

        Painter.set(level, new Point(centerX - 3, centerY - 2), WATER);
        Painter.set(level, new Point(centerX + 2, centerY - 2), WATER);

        Point a = new Point(centerX + 3, centerY - 3);
        int chestPos = (left + right) - a.x + a.y * level.width();

        Point b = new Point(centerX - 3, centerY - 3);
        int chestPos2 = left + right - b.x + b.y * level.width();

        level.drop(prize(), chestPos).type = Heap.Type.BLACK;
        level.drop(prize(), chestPos2).type = Heap.Type.BLACK;
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Food());
        items.add(new Gold(Random.Int(40,100)));
        items.add(new Gold(1));
        if (Random.Float() <= 0.3f) items.add(new Gold());
        if (Random.Float() <= 0.3f) items.add(new Gold());
        if (Random.Float() <= 0.6f) items.add(Generator.random());
        if (Random.Float() <= 0.3f) items.add(Generator.randomArmor());
        for (Item item : items){
            int Lpos;
            do {
                Lpos = level.pointToCell(random());
            } while (level.map[Lpos] != EMPTY_SP || level.heaps.get(Lpos) != null);
            Heap h = level.drop(item, Lpos);
            h.setHauntedIfCursed();
            h.type = Heap.Type.SKELETON;
        }

        ArrayList<Item> itemss = new ArrayList<>();
        itemss.add(new BlackKey( Dungeon.depth ));
        int count = 0; // 计数器
        for (Item item : itemss){

            if (count >= 2) {
                break; // 达到两次后跳出循环
            }

            int Lpos;
            do {
                Lpos = level.pointToCell(random());
            } while (level.map[Lpos] != EMPTY_SP || level.heaps.get(Lpos) != null);
            Heap h = level.drop(item, Lpos);
            h.setHauntedIfCursed();
            h.type = Heap.Type.TOMB;
            count++;
        }

        Mob n;

        Point xpos = new Point(centerX, centerY + 2);
        int mob = left + right - xpos.x + xpos.y * level.width();
        n = Reflection.newInstance(Random.element(RARE));
        n.flying = true;

        Class<?extends ChampionEnemy> buffCls0;
        switch (Random.Int(2)){
            case 0: default:    buffCls0 = ChampionEnemy.Small.class;      break;
            case 1:             buffCls0 = ChampionEnemy.Middle.class;   break;
        }

        Buff.affect( n, buffCls0);

        Class<?extends ChampionEnemy> buffCls1;
        switch (Random.Int(2)){
            case 0: default:             buffCls1 = ChampionEnemy.Big.class;    break;
            case 1:             buffCls1 = ChampionEnemy.LongSider.class;        break;
        }

        Buff.affect( n, buffCls1);


        Class<?extends ChampionEnemy> buffCls2;
        switch (Random.Int(8)){
            case 0: default:    buffCls2 = ChampionEnemy.Blazing.class;      break;
            case 1:             buffCls2 = ChampionEnemy.Projecting.class;   break;
            case 2:             buffCls2 = ChampionEnemy.AntiMagic.class;    break;
            case 3:             buffCls2 = ChampionEnemy.Giant.class;        break;
            case 4:             buffCls2 = ChampionEnemy.Blessed.class;      break;
            case 5:             buffCls2 = ChampionEnemy.Growing.class;      break;
            case 6:             buffCls2 = ChampionEnemy.Halo.class;      	  break;
            case 7:             buffCls2 = ChampionEnemy.DelayMob.class;     break;
        }
        Buff.affect( n, buffCls2);
        n.pos = mob;
        level.mobs.add(n);
    }
}

