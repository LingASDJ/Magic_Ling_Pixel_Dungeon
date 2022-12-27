package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.EXSG;
import static com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.AutoShopRoom.Holiday.HWEENX;
import static com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.AutoShopRoom.Holiday.XMASX;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.AutoShopReBot;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Random;

import java.util.Calendar;

public class AutoShopRoom extends SpecialRoom {

    public int minWidth() {
        return 9;
    }

    public int minHeight() {
        return 9;
    }

    private static Holiday holiday;

    public enum Holiday{
        NONE,
        ZQJX, //TBD
        HWEENX,//2nd week of october though first day of november
        XMASX //3rd week of december through first week of january
    }

    static{

        holiday = Holiday.NONE;

        final Calendar calendar = Calendar.getInstance();
        switch(calendar.get(Calendar.MONTH)){
            case Calendar.JANUARY:
                if (calendar.get(Calendar.WEEK_OF_MONTH) == 1)
                    holiday = XMASX;
                break;
            //2022 9.10-10.1
            case Calendar.SEPTEMBER:
                if (calendar.get(Calendar.DAY_OF_MONTH) >= 10 ){
                    holiday = Holiday.ZQJX;
                } else {
                    holiday = Holiday.NONE;
                }
                break;
            case Calendar.OCTOBER:
                if (calendar.get(Calendar.DAY_OF_MONTH) == 1 ){
                    holiday = Holiday.ZQJX;
                } else {
                    holiday = Holiday.NONE;
                }
                break;
            case Calendar.DECEMBER:
                if (calendar.get(Calendar.WEEK_OF_MONTH) >= 3)
                    holiday = XMASX;
                break;
        }
    }

    public void paint(Level level) {
        Painter.fill(level, this, 4);
        Painter.fill(level, this, 1, 14);

        if(holiday == HWEENX || holiday == XMASX|| Dungeon.isChallenged(EXSG)) {
            placeShopkeeper(level);
        }

        for (Room.Door door : this.connected.values()) {
            door.set(Door.Type.REGULAR);
        }
        Door entrance = entrance();
        entrance.set( Door.Type.REGULAR );
        //level.addItemToSpawn( new IronKey( Dungeon.depth ) );
        int door = entrance.x + entrance.y * level.width();

        //布局 x2 箱子 左
        addChest( level, (top + 2) * level.width() + right-2, door );
        addChestB( level, (bottom - 2) * level.width() + right-2, door );

        //布局 x2 箱子 右
        addChestC( level, (top + 2) * level.width() + right-6, door );
        addChestD( level, (bottom - 2) * level.width() + right-6, door );

        //7,17层---20%概率出金箱子 20% probability of gold chest
        if(Dungeon.depth == 7||Dungeon.depth == 17) {
            if (Random.Float() < 0.2f) {
                addChestE(level, (top + 5) * level.width() + right - 2, door);
                addChestF(level, (top + 5) * level.width() + right - 6, door);
                level.addItemToSpawn(new GoldenKey(Dungeon.depth));
            }
        }

    }

    protected void placeShopkeeper(Level level) {
        int pos = level.pointToCell(center());
        Mob autoShopReBot = new AutoShopReBot();
        autoShopReBot.pos = pos;
        level.mobs.add(autoShopReBot);
    }



    private static void addChest( Level level, int pos, int door ) {

        if (pos == door - 1 ||
                pos == door + 1 ||
                pos == door - level.width() ||
                pos == door + level.width()) {
            return;
        }

        Item prize = new Gold( Random.IntRange( 10, 25 ) );

        level.drop( prize, pos ).type = Heap.Type.CHEST;
    }

    private static void addChestB( Level level, int pos, int door ) {

        if (pos == door - 1 ||
                pos == door + 1 ||
                pos == door - level.width() ||
                pos == door + level.width()) {
            return;
        }

        Item prize = Generator.randomUsingDefaults( Generator.Category.STONE );

        level.drop( prize, pos ).type = Heap.Type.CHEST;
    }

    private static void addChestC( Level level, int pos, int door ) {

        if (pos == door - 1 ||
                pos == door + 1 ||
                pos == door - level.width() ||
                pos == door + level.width()) {
            return;
        }

        Item prize = Generator.randomUsingDefaults( Generator.Category.MISSILE );

        level.drop( prize, pos ).type = Heap.Type.CHEST;
    }

    private static void addChestD( Level level, int pos, int door ) {

        if (pos == door - 1 ||
                pos == door + 1 ||
                pos == door - level.width() ||
                pos == door + level.width()) {
            return;
        }

        Item prize = Generator.randomUsingDefaults( Generator.Category.SEED );

        level.drop( prize, pos ).type = Heap.Type.CHEST;
    }

    private static void addChestE( Level level, int pos, int door ) {

        if (pos == door - 1 ||
                pos == door + 1 ||
                pos == door - level.width() ||
                pos == door + level.width()) {
            return;
        }

        Wand mw = (Wand) Generator.randomUsingDefaults( Generator.Category.WAND );
        mw.level(Random.Int(0,3));
        mw.cursed = false;
        mw.identify();
        level.drop(mw, pos ).type = Heap.Type.LOCKED_CHEST;
    }

    private static void addChestF( Level level, int pos, int door ) {

        if (pos == door - 1 ||
                pos == door + 1 ||
                pos == door - level.width() ||
                pos == door + level.width()) {
            return;
        }

        Weapon mw = Generator.randomWeapon(11);
        mw.level(Random.Int(0,3));
        mw.cursed = false;
        mw.identify();
        level.drop(mw, pos ).type = Heap.Type.LOCKED_CHEST;
    }


}

