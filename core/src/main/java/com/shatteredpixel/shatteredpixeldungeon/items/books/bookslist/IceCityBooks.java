package com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArcaneArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.books.Books;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class IceCityBooks extends Books {
    private static final String Read	= "Read";
    {
        image = ItemSpriteSheet.ICEBOOK;
        unique = true;
    }

    @Override
    public void execute(final Hero hero, String action) {
        super.execute(hero, action);
        for (Buff b : hero.buffs(ChampionHero.class)) {
            if (b != null) {
                GLog.w(Messages.get(Books.class, "your_character"));
                return;
            }
        }
        if (action.equals(Read)) {
            Statistics.readBooks++;
            Badges.valiReadBooks();
            Sample.INSTANCE.play(Assets.Sounds.READ);
            switch (Random.Int(5)) {
                case 0:
                case 1:
                case 2:
                    Buff.affect(hero, ArcaneArmor.class).set(Dungeon.hero.lvl + 5, 5);
                    detach(hero.belongings.backpack);
                    GLog.b(Messages.get(this, "blees"));
                    break;
                case 3:
                case 4:
                case 5:
                    Buff.affect(hero, ChampionHero.Blazing.class, ChampionHero.DURATION);
                    detach(hero.belongings.backpack);
                    GLog.b(Messages.get(this, "anmazing"));
                    break;
            }
        }
    }
}
