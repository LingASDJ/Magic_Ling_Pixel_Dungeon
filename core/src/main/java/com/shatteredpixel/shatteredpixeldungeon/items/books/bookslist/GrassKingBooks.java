package com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.books.Books;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class GrassKingBooks extends Books {
    private static final String Read	= "Read";
    {
        image = ItemSpriteSheet.GREENBOOKS;
        unique= true;
    }

    @Override
    public int value() {
        return quantity * 8;
    }

    @Override
    public void execute(final Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals( Read ) ) {
            Sample.INSTANCE.play( Assets.Sounds.READ );
            switch (Random.Int(5)){
                case 0: case 1: case 2:
                    Buff.affect(hero, Barrier.class).setShield( 10 + (hero.HP - hero.HT)/10);
                    detach( hero.belongings.backpack );
                    GLog.b( Messages.get(this, "blees") );
                    break;
                case 3: case 4: case 5:
                    Buff.affect(hero, ChampionHero.Giant.class, ChampionHero.DURATION);
                    detach( hero.belongings.backpack );
                    GLog.b( Messages.get(this, "anmazing") );
                    break;
            }
        }
    }
}
