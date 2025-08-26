package com.shatteredpixel.shatteredpixeldungeon.items.food.fantong;

import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
public class ZakoSoup extends Food {

    {
        image = ItemSpriteSheet.ZAKOSOUP;
        energy = Hunger.HUNGRY;
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        effect(hero);
    }

    @Override
    public void effect(Hero hero) {
        GLog.i( Messages.get(ZakoSoup.class, "effect") );
        Buff.affect(hero, Healing.class).setHeal(5 + 3*hero.lvl, 0, 1);
    }

    @Override
    public int value() {
        return 150 * quantity;
    }
}
