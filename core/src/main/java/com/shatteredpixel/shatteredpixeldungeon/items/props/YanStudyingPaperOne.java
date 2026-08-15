package com.shatteredpixel.shatteredpixeldungeon.items.props;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PropBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfMight;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class YanStudyingPaperOne extends Prop{
    {
        rareness = 1;
        kind = 1;
        image = ItemSpriteSheet.YANSTUDYINGPAPERONE;
    }

    @Override
    public boolean collect() {
        if(Dungeon.hero.buff(PropBuff.class)==null) Buff.affect(Dungeon.hero, PropBuff.class);
        return super.collect();
    }

    //每次下楼时判定：25%概率遗失除力量/根骨外的药水
    public static void onDescend() {
        Hero hero = Dungeon.hero;
        if (hero == null) return;
        if (hero.belongings.getItem(YanStudyingPaperOne.class) == null) return;
        if (Random.Int(1, 100) > 25) return;
        if (Dungeon.depth <= 0) return;

        ArrayList<Potion> potions = new ArrayList<>();
        for (Potion p : hero.belongings.getAllItems(Potion.class)) {
            if (!(p instanceof PotionOfStrength) && !(p instanceof ElixirOfMight)) {
                potions.add(p);
            }
        }

        if (!potions.isEmpty()) {
            int amount = Math.min(Random.Int(1, 5), potions.size());
            while (amount > 0 && !potions.isEmpty()) {
                int idx = Random.Int(0, potions.size());
                Potion p = potions.get(idx);
                if (p.quantity() <= 0 || !hero.belongings.backpack.contains(p)) {
                    potions.remove(idx);
                    continue;
                }
                p.detach(hero.belongings.backpack);
                amount--;
                Item.updateQuickslot();
            }
            hero.buff(PropBuff.class).potionLost = true;
        }
    }
}
