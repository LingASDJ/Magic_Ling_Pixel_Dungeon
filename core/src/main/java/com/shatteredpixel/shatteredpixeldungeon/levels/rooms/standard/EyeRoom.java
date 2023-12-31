package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BruteBot;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Crab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CrystalMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM201;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SRPDHBLR;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.watabou.utils.Point;

public class EyeRoom extends SpecialRoom {

    @Override
    public int minWidth() {
        return 13;
    }

    @Override
    public int minHeight() {
        return 13;
    }

    @Override
    public int maxWidth() {
        return 13;
    }

    @Override
    public int maxHeight() {
        return 13;
    }

    @Override
    public boolean canMerge(Level l, Point p, int mergeTerrain) {
        return false;
    }

    @Override
    public void paint(Level level) {
        Point center = new Point((left + right) / 2, (top + bottom) / 2);
        int eyeRadius = (right - left) / 4;
        Painter.fill(level,this, EMPTY);

        // 绘制外围墙壁
        Painter.drawLine(level, new Point(left, top), new Point(right, top), Terrain.WALL);
        Painter.drawLine(level, new Point(right, top), new Point(right, bottom), Terrain.WALL);
        Painter.drawLine(level, new Point(right, bottom), new Point(left, bottom), Terrain.WALL);
        Painter.drawLine(level, new Point(left, bottom), new Point(left, top), Terrain.WALL);

        // 绘制眼睛外圈和门
        Painter.drawCircle(level, center, eyeRadius, Terrain.EMPTY_SP);
        Painter.drawCircle(level, center, eyeRadius + 1, Terrain.WALL);

        int doorX = center.x;
        int doorY = center.y + eyeRadius;

        // 绘制眼睛中心和瞳孔
        Painter.set(level, center, Terrain.EMPTY_SP);
        int pupilRadius = eyeRadius / 2;
        Point pupilPos = center.offset(0, pupilRadius / 2);
        Painter.drawCircle(level, pupilPos, pupilRadius+1, Terrain.EMPTY_SP);

        // 绘制眼睛眉毛
        Point browStart = center.offset(-eyeRadius - 1, -3);
        Point browEnd = center.offset(eyeRadius + 1, -3);

        // 绘制眼瞳
        Painter.set(level, doorX, doorY - 3, Terrain.PEDESTAL);

        Painter.set(level, doorX, doorY,Terrain.BARRICADE);

        int chestPos = (top + 6) * level.width() + left + 6;

        /** 套4个宝箱 */
        level.mobs.add(Mimic.spawnAt(chestPos, CrystalMimic.class,( Generator.randomUsingDefaults( Generator.Category.POTION ) )));
        level.mobs.add(Mimic.spawnAt(chestPos,CrystalMimic.class,( Generator.randomUsingDefaults( Generator.Category.SCROLL ) )));
        level.mobs.add(Mimic.spawnAt(chestPos,CrystalMimic.class,( Generator.randomUsingDefaults( Generator.Category.WEAPON ) )));
        level.mobs.add(Mimic.spawnAt(chestPos,CrystalMimic.class,( Generator.randomUsingDefaults( Generator.Category.ARMOR ) )));

        //四大恶人
        int[] MBTPOS = new int[]{
                (top + 4) * level.width() + left + 6,
                (top + 8) * level.width() + left + 6,
                (top + 6) * level.width() + left + 4,
                (top + 6) * level.width() + left + 8,
        };


        for (int i : MBTPOS) {
            Mob n = new Crab();
            if(depth >= 20){
               n = new Eye();
            } else if(depth >= 15) {
               n = new BruteBot();
            } else if(depth >= 10) {
               n = new DM201();
            } else if(depth >= 6){
               n = new SRPDHBLR();
            }

            n.pos = i;
            level.mobs.add(n);
        }


        int TopPos = (top + 1) * level.width() + left + 6;

        int BotPos = (top + 11) * level.width() + left + 6;

        int LeftPos = (top + 6) * level.width() + left + 1;

        int RightPos = (top + 6) * level.width() + left + 11;

        /** 小时候诋毁cocoa，长大后质疑cocoa，成年后认可cocoa，现在成为cocoa
        /** Watabou看看你的房间是多么的恶心
        /** 由于房间类的特殊性质，无法直接从外部调用 */
        level.drop( ( Generator.random( Generator.Category.ARTIFACT )),LeftPos).type = Heap.Type.WHITETOMB;
        level.drop( ( Generator.random( Generator.Category.GOLD )),RightPos).type = Heap.Type.TOMB;
        level.drop( ( Generator.random( Generator.Category.GOLD )),BotPos).type = Heap.Type.TOMB;
        level.drop( ( Generator.random( Generator.Category.GOLD )),TopPos).type = Heap.Type.TOMB;

        // 添加边界检查
        browStart.x = Math.max(browStart.x, left + 1);
        browStart.x = Math.min(browStart.x, right - 1);
        browEnd.x = Math.max(browEnd.x, left + 1);
        browEnd.x = Math.min(browEnd.x, right - 1);
        Painter.drawLine(level, browStart, browEnd, Terrain.WALL);

        for (Door door : connected.values()) {
            door.set(Door.Type.HIDDEN);
        }

    }

}
