package com.shatteredpixel.shatteredpixeldungeon.items.thanks;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.UnstableBrew;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class FlareBullet extends Item {

    {
        image = ItemSpriteSheet.FLARE;

        stackable = true;
        unique = true;
        defaultAction = AC_LOAD;
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{PotionOfLiquidFlame.class, UnstableBrew.class, PotionOfLevitation.class};
            inQuantity = new int[]{1, 1, 1};

            cost = 0;   // 配方不再消耗炼金能量

            output = FlareBullet.class;
            outQuantity = 5;
        }
    }

    public static final String AC_LOAD = "LOAD";

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        DistressSignalNesting signal = hero.belongings.getItem(DistressSignalNesting.class);
        if (signal != null && signal.getCharge() < signal.getChargeCap()) {
            actions.add(AC_LOAD);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_LOAD)) {
            DistressSignalNesting signal = hero.belongings.getItem(DistressSignalNesting.class);
            if (signal == null) {
                GLog.w(Messages.get(this, "no_device"));
                return;
            }
            if (signal.getCharge() >= signal.getChargeCap()) {
                GLog.w(Messages.get(this, "full_charge"));
                return;
            }
            // 消耗一个信号弹
            if (quantity() <= 1) {
                detach(hero.belongings.backpack);
            } else {
                quantity(quantity() - 1);
            }
            // 每次装填恢复至当前充能上限（最终充能不可超过上限）
            Item.updateQuickslot();
            signal.addCharge(signal.getChargeCap());
            Item.updateQuickslot();
            GLog.p(Messages.get(this, "load_success"));
            hero.spend(1f);
            hero.sprite.operate(hero.pos);
        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }
}