package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.AlcoholGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Inebriation;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

/**
 * <div style="color:orange;font-weight:bold" >
 *     <h3>酒精合剂</h3>
 *     <ul>
 *       <li>合成：瓶装菊酒+4能量。</li>
 *       <li>饮用获得100%醉意。</li>
 *       <li>投掷后产生1000气体量酒气。</li>
 *       <li>瓶装菊酒无法自然生成，只能通过炼金合成和商人售卖，400底价。</li>
 *     </ul>
 * </div>
 *
 * <p>分散实现的效果如下
 * <ul>
 *   <li>合成— 见{@link com.shatteredpixel.shatteredpixeldungeon.items.Recipe#oneIngredientRecipes 槽位1配方} {@link com.shatteredpixel.shatteredpixeldungeon.ui.QuickRecipe#getRecipes(int) 配方挂载}</li>
 *   <li>生成与出售— {@link com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.ShopRoom#generateItems() 常规商店出售}</li>
 * </ul>
 *
 * @see com.shatteredpixel.shatteredpixeldungeon.items.potions.BottledMarigoldWine 瓶装菊酒
 * @see com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Inebriation   醉意
 * @see com.shatteredpixel.shatteredpixeldungeon.actors.blobs.AlcoholGas    酒气
 */
public class AlcoholMixture extends Potion{

    {
        image = ItemSpriteSheet.POTION_DEEPYELLOW;   // TODO 先随便复用一个现成瓶身
    }

    static {
        canThrowPots.add(AlcoholMixture.class);      // 可喝可扔，扔时不再弹确认框
    }

    @Override
    public boolean isKnown() { return true; }


    @Override public int value() { return 40 * quantity; }
    @Override public int energyVal() { return  8 * quantity; }

    @Override
    public void apply(Hero hero) {
        Inebriation.addInebriation(hero, Inebriation.MAX_VALUE);
    }

    @Override
    public void shatter(int cell) {
        super.shatter(cell);            // splash + 碎裂音 + 日志，Potion.java:382 已经写好了
        AlcoholGas.spawn(cell, 1000);   // 1000气体量
    }

    // ========== 配方 ==========

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {
        {
            inputs     = new Class[]{ BottledMarigoldWine.class };
            inQuantity = new int[]{ 1 };
            cost       = 4;                       // 4 点炼金能量
            output     = AlcoholMixture.class;
            outQuantity = 1;
        }
    }

}