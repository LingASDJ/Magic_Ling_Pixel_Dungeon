package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CrystalMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;
import java.util.Arrays;

public class YinYangRoom extends SpecialRoom {

    @Override
    public boolean canConnect(Point p) {
        if (!super.canConnect(p)){
            return false;
        }
        //only place doors in the center
        if (Math.abs(p.x - (right - (width()-1)/4f)) < 1f){
            return true;
        }
        if (Math.abs(p.y - (bottom - (height()-1)/4f)) < 1f){
            return true;
        }
        return false;
    }

    @Override
    public int minWidth() { return 11; }
    @Override
    public int minHeight() {
        return 11;
    }
    @Override
    public int maxWidth() { return 11; }
    @Override
    public int maxHeight() {
        return 11;
    }
    @Override
    public boolean canMerge(Level l, Point p, int mergeTerrain) {
        return false;
    }

    @Override
    public Rect resize(int w, int h) {
        super.resize(w, h);
        return this;
    }

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

    private Item Blackprize() {
        Generator.Category cat = prizeClasses.remove(0);
        prizeClasses.add(cat);
        Item prize;
        do {
            prize = Generator.random(cat);
            prize.level(Random.NormalIntRange(1,3));
            prize.cursed = true;
        } while (Challenges.isItemBlocked(prize));
        return prize;
    }

    private ArrayList<Generator.Category> prizeClasses = new ArrayList<>(
            Arrays.asList(Generator.Category.WAND,
                    Generator.Category.RING,
                    Generator.Category.ARTIFACT));

    @Override
    public void paint(Level level) {
        Point a = new Point((left + right) / 2 + (right - left + 2) / 4 - 1,top + (right - left + 2) / 4 - 1);
        Point b = new Point((left + right) / 2 - (right - left + 2) / 4 + 1,bottom - (right - left + 2) / 4 + 1);

        Painter.fill(level,this,Terrain.WALL);
        Painter.fill(level,left + 1,top + 1,width() - 2,height() - 2,Terrain.WATER);

        for (int i = 1;i < right - left;i++) {
            for (int j =1;j < bottom - top;j++) {
                if (i + j < right - left + 1) {
                    Painter.set(level,left + i,top + j,Terrain.CHASM);
                }
            }
        }

        Painter.fill(level,a.x,top + 1,right - a.x,a.y - top + 1,Terrain.WATER);
        Painter.fill(level,left + 1,b.y,b.x - left,bottom - b.y,Terrain.CHASM);

        Painter.drawLine(level,new Point((left + right) / 2,top),a,Terrain.WALL);
        Painter.drawLine(level,new Point((left + right) / 2,bottom),b,Terrain.WALL);
        Painter.drawLine(level,a.offset(0,1),b.offset(0,-1),Terrain.WALL);

        int chestPos = left + right - a.x + a.y * level.width();

        int chestPos2 = left + right - b.x + b.y * level.width();

        Painter.set(level,chestPos,Terrain.PEDESTAL);
        level.drop(prize(), chestPos).type = Heap.Type.CRYSTAL_CHEST;
        Painter.set(level,chestPos2,Terrain.EMPTY_SP);

        if (Random.Int(10) == 0){
            level.mobs.add(Mimic.spawnAt(chestPos2,CrystalMimic.class,Blackprize()));
        } else {
            level.drop(prize(), chestPos2).type = Heap.Type.CRYSTAL_CHEST;
        }

        Painter.set(level,left + 1,top + 1,Terrain.WALL);
        Painter.set(level,right - 1,top + 1,Terrain.WALL);
        Painter.set(level,left + 1,bottom - 1,Terrain.WALL);
        Painter.set(level,right - 1,bottom - 1,Terrain.WALL);

        for (Door door : connected.values()) {
            if(Dungeon.depth == 1){
                door.set(Door.Type.REGULAR);
            } else {
                level.addItemToSpawn( new IronKey( Dungeon.depth ) );
                if(Dungeon.depth == 4 && Dungeon.branch == 2){
                    entrance().set( Door.Type.CRYSTAL );
                } else {
                    entrance().set( Door.Type.LOCKED );
                }
            }

        }

        level.addItemToSpawn( new CrystalKey( Dungeon.depth ) );
        level.addItemToSpawn( new PotionOfLevitation());

    }

}
