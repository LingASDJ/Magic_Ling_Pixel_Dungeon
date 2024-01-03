package com.shatteredpixel.shatteredpixeldungeon.items.keys;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LockSword;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class BlackKey extends Key {

    {
        image = ItemSpriteSheet.BLACK_KEY;
    }

    public BlackKey() {
        this( 0 );
    }

    public BlackKey( int depth ) {
        super();
        this.depth = depth;
    }

    @Override
    public boolean doPickUp(Hero hero, int pos) {
        super.doPickUp(hero, pos);
        LockSword lockSword = Dungeon.hero.belongings.getItem(LockSword.class);
        if(lockSword != null) {
            int index = 35;
            lockSword.lvl += index;
            int lvl = lockSword.lvl;
            if (lvl >= 100 && lvl <= 1000 && lvl % 100 == 0) {
                // 提醒气泡的显示逻辑
                GLog.p(Messages.get(Key.class,"locksword"));
            }
            hero.sprite.showStatus(0x123456ff, String.valueOf(index));
            return true;
        }

        return true;
    }


}
