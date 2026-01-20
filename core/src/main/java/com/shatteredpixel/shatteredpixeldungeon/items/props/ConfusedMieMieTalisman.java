package com.shatteredpixel.shatteredpixeldungeon.items.props;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PropBuff;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class ConfusedMieMieTalisman extends Prop{
    {
        rareness = 0;
        image = ItemSpriteSheet.CONfUSEDMIEMIETALISMAN;
        kind = 1;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        if(Dungeon.hero != null){
            PropBuff buff = Dungeon.hero.buff(PropBuff.class);
            if(buff != null){
                if(buff.warningTime>0){
                    return new ItemSprite.Glowing(0x880000, 6f);
                }
            }
        }
        return null;
    }


    @Override
    public boolean collect() {
        if(Dungeon.hero.buff(PropBuff.class)==null) Buff.affect(Dungeon.hero, PropBuff.class);
        return super.collect();
    }
}