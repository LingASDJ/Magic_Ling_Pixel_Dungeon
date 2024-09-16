package com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invulnerability;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.InvisibilityRing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class TestBooks extends Item {
    private static final String Read	= "Read";
    {
        image = ItemSpriteSheet.BLACKBOOK;
        unique= true;
        defaultAction = Read;
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions(hero);
        if (hero.HP > 0) {
            actions.add(Read);
        }
        return actions;
    }


    @Override
    public void execute(final Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals( Read )) {
            if ( Dungeon.hero.buff(Invulnerability.class) == null ) {
                GLog.p(Messages.get(this,"godmode_on"));
                Buff.prolong(hero, Invulnerability.class, Invulnerability.DURATION*1000000f);
                Buff.affect(hero, InvisibilityRing.class, InvisibilityRing.DURATION*1000000f);
                Buff.affect( hero, MindVision.class, MindVision.DURATION*1000000f );
            } else {
                GLog.n(Messages.get(this,"godmode_off"));
                Buff.detach( hero, Invulnerability.class );
                Buff.detach( hero, InvisibilityRing.class );
                Buff.detach( hero, MindVision.class );
            }
        }
    }
}
