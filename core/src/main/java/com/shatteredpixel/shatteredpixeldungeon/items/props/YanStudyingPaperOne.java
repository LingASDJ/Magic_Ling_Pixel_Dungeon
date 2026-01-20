package com.shatteredpixel.shatteredpixeldungeon.items.props;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PropBuff;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class YanStudyingPaperOne extends Prop{
    {
        rareness = 2;
        kind = 1;
        image = ItemSpriteSheet.YANSTUDYINGPAPERONE;
    }

    @Override
    public boolean collect() {
        if(Dungeon.hero.buff(PropBuff.class)==null) Buff.affect(Dungeon.hero, PropBuff.class);
        return super.collect();
    }
}
