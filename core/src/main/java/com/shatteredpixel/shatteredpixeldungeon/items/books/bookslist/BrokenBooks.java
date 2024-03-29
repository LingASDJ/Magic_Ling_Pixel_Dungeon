package com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.books.Books;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class BrokenBooks extends Books {
    private static final String Read	= "Read";
    {
        image = ItemSpriteSheet.FIRELIYD;
        unique= true;
    }

    @Override
    public void execute(final Hero hero, String action) {
        super.execute(hero, action);
        for (Buff b : hero.buffs(ChampionHero.class)){
            if(b != null){
                GLog.w(Messages.get(Books.class, "your_character"));
                return;
            }
        }
        if (action.equals( Read ) ) {
            Sample.INSTANCE.play( Assets.Sounds.READ );
            Statistics.readBooks++;
            Badges.valiReadBooks();
            switch (Random.Int(5)){
                case 0: case 1: case 2:
                    Buff.affect(hero, Adrenaline.class, 30f);
                    detach( hero.belongings.backpack );
                    GLog.b( Messages.get(this, "blees") );
                    break;
                case 3: case 4: case 5:
                    Buff.affect(hero, ChampionHero.Growing.class, 275f);
                    detach( hero.belongings.backpack );
                    GLog.b( Messages.get(this, "anmazing") );
                    break;
            }
        }
    }
}
