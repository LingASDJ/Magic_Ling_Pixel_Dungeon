package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class ShopKing extends NTNPC {

    private static final String FIRST = "first";
    private boolean first=true;
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
    }

    public void flee() {
        destroy();
        sprite.killAndErase();
        CellEmitter.get( pos ).burst( ElmoParticle.FACTORY, 6 );
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, Dungeon.hero.pos );
        if(first){
            WndQuest.chating(this,chat);
            first=false;
        }else {
            GLog.n( Messages.get(this, "talk_7") );
          flee();
        }
        return true;
    }

    {
        spriteClass = ShopkKingSprite.class;

        chat = new ArrayList<String>() {
            {
                add(Messages.get(Null.class, "why"));
                add(Messages.get(ShopKing.class, "talk"));
                add(Messages.get(Null.class, "why_2"));
                add(Messages.get(ShopKing.class, "talk_2"));
                add(Messages.get(Null.class, "why_3"));
                add(Messages.get(ShopKing.class, "talk_3"));
                add(Messages.get(ShopKing.class, "talk_4"));
                add(Messages.get(ShopKing.class, "talk_5"));
                add(Messages.get(ShopKing.class, "talk_6"));
            }
        };

    }
}