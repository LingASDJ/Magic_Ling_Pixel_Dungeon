package com.shatteredpixel.shatteredpixeldungeon.items.props;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PropBuff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class FaintGlimmer extends Prop {

    {
        rareness = 2;
        kind = 0;
        image = ItemSpriteSheet.FAINT_GLIMMER;
    }

    @Override
    public String desc() {
       String s = "";
        s = Messages.get(this,"rareness",rareness+1,kindRules());
       if(Dungeon.hero != null){
           PropBuff propBuff = Dungeon.hero.buff(PropBuff.class);
           if(propBuff != null){
               s += "\n\n" + Messages.get(this,"desc",propBuff.levelA);
           } else {
               s += "\n\n" + Messages.get(this,"desc",0);
           }
       } else {
           s += "\n\n" + Messages.get(this,"desc",10);
       }
       return s;
    }

    @Override
    public boolean collect() {
        if(Dungeon.hero.buff(PropBuff.class)==null) Buff.affect(Dungeon.hero, PropBuff.class);
        return super.collect();
    }

}
