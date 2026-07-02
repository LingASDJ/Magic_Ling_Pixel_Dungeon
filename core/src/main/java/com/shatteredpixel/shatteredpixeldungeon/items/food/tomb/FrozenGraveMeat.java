package com.shatteredpixel.shatteredpixeldungeon.items.food.tomb;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.List;

public class FrozenGraveMeat extends Food {

    {
        image = ItemSpriteSheet.FrozenGraveMeat;
        energy = Hunger.HUNGRY/2f;
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        effect(hero);
    }

    public int value() {
        return 5 * quantity;
    }

    public void effect(Hero hero) {

        List<Integer> allEffects = new ArrayList<>();
        allEffects.add(0);
        allEffects.add(1);
        allEffects.add(2);
        allEffects.add(3);
        allEffects.add(4);
        allEffects.add(5);

        int triggerCount = Random.Int(2) + 1;
        List<Integer> selected = new ArrayList<>();
        for (int i = 0; i < triggerCount; i++) {
            int idx = Random.index(allEffects);
            selected.add(allEffects.remove(idx));
        }
        Random.shuffle(allEffects);
        for (int effId : selected) {
            applySingleEffect(hero, effId);
        }
    }

    private void applySingleEffect(Hero hero, int id) {
        switch (id) {
            case 0:
                GLog.w(Messages.get(this, "poison"));
                Buff.affect(hero, Poison.class).set(hero.HT / 5);
                break;
            case 1:
                GLog.w(Messages.get(this, "vertigo"));
                Buff.prolong(hero, Vertigo.class, 15f);
                break;
            case 2:
                Buff.affect(hero, Corrosion.class).set(5f, 2);
                GLog.w(Messages.get(this, "corrosion"));
                break;
            case 3:
                Buff.affect(hero, Healing.class).setHeal(hero.HT / 4, 0.25f, 0);
                GLog.p(Messages.get(this, "heal"));
                break;
            case 4:
                GLog.p(Messages.get(this, "invisible"));
                Buff.affect(hero, Invisibility.class, 20f);
                break;
            case 5:
                GLog.p(Messages.get(this, "barskin"));
                Buff.affect(hero, Barkskin.class).set(hero.HT / 4, 10);
                break;
        }
    }

    public static Food cook( GraveMeat ingredient ) {
        FrozenGraveMeat result = new FrozenGraveMeat();
        result.quantity = ingredient.quantity();
        return result;
    }

}

