package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.IceGolem;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.DM275;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.GnollHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.GreenSlting;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.SuccubusQueen;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class LinkRoom extends StandardRoom {

    @Override
    public int minWidth() { return 7; }
    @Override
    public int minHeight() {
        return 7;
    }
    @Override
    public int maxWidth() { return 7; }
    @Override
    public int maxHeight() { return 7; }

    @Override
    public void paint(Level level) {
        Painter.fill( level, this, Terrain.WALL );

        Painter.fillEllipse( level, this, 1 , Terrain.EMPTY );
        Painter.fillEllipse( level, this, 2 , Terrain.EMPTY_SP );

        for (Door door : connected.values()) {
            door.set( Door.Type.REGULAR );
            if (door.x == left || door.x == right){
                Painter.drawInside(level, this, door, width()/2, Terrain.EMPTY_SP);
            } else {
                Painter.drawInside(level, this, door, height()/2, Terrain.EMPTY_SP);
            }
        }

        Point center = center();
        Painter.set( level, center, Terrain.PEDESTAL );


        Mob n = new GreenSlting();
        if(depth >= 20){
            n = new SuccubusQueen();
        } else if(depth >= 15) {
            n = new IceGolem();
        } else if(depth >= 10) {
            n = new DM275();
        } else if(depth >= 6){
            n = new GnollHero();
        }
        n.pos = level.pointToCell(center);
        Class<?extends ChampionEnemy> buffCls;
        switch (Random.NormalIntRange(0,4)){
            case 0: default:
                buffCls = ChampionEnemy.Small.class;
                break;
            case 1:
                buffCls = ChampionEnemy.Bomber.class;
                break;
            case 2:
                buffCls = ChampionEnemy.Middle.class;
                break;
            case 3:
                buffCls = ChampionEnemy.Big.class;
                break;
            case 4:
                buffCls = ChampionEnemy.LongSider.class;
                break;
        }
        Buff.affect( n, buffCls);
        n.state = n.WANDERING;
        level.mobs.add(n);

        Item prize = level.findPrizeItem();
        if (prize != null){
            int pos;
            do {
                pos = level.pointToCell(random());
            } while (level.map[pos] != Terrain.EMPTY_SP);
            level.drop( prize, pos );
        }
        int pos;
        do {
            pos = level.pointToCell(random());
        } while (!level.adjacent(pos, n.pos));
    }
}
