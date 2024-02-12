package com.shatteredpixel.shatteredpixeldungeon.items.books.questbookslist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.books.Books;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class DimandBook extends Books {
    private static final String Read	= "Read";
    {
        image = ItemSpriteSheet.SOYBOOKS;
        unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.remove( AC_DROP );
        actions.remove( AC_THROW );
        return actions;
    }

    @Override
    public int value() {
        return quantity * 20;
    }

    @Override
    public String info() {
        String sb;
        if(Dungeon.isChallenged(Challenges.STRONGER_BOSSES)){
            sb = Messages.get(this, "fuck_desc");
        } else {
            sb = desc();
        }
        return sb;
    }

    @Override
    public void execute(final Hero hero, String action) {
        if (action.equals( Read ) && Statistics.deepestFloor<10) {
            Sample.INSTANCE.play( Assets.Sounds.READ );
            detach( hero.belongings.backpack );
            GLog.n(Messages.get(DimandBook.class,"action"));
            Statistics.mimicking = true;
        } else {
            detach( hero.belongings.backpack );
            GLog.n(Messages.get(DimandBook.class,"action_s"));
        }
    }

    @Override
    public ItemSprite.Glowing glowing() {
        if(Dungeon.isChallenged(Challenges.STRONGER_BOSSES)){
            return new ItemSprite.Glowing(Window.R_COLOR, 3f);
        } else {
            return null;
        }
    }

}

