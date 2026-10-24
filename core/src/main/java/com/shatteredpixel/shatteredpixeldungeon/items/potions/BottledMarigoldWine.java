package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.AlcoholGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Inebriation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MarigoldBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.plants.Marigold;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

/**
 * <div style="color:orange;font-weight:bold" >
 *     <h3>瓶装菊酒</h3>
 *     <ul>
 *       <li>合成：黄金菊种子+黄金菊种子+口粮＝瓶装菊酒*2，黄金菊种子+黄金菊种子+小型口粮＝瓶装菊酒*1。</li>
 *       <li>饮用获得30%醉意和30回合菊寿凝身效果。</li>
 *       <li>投掷后产生400气体量酒气。</li>
 *       <li>瓶装菊酒无法自然生成，只能通过炼金合成和商人售卖，商人以200底价出售。</li>
 *     </ul>
 * </div>
 *
 * <p>分散实现的效果如下
 * <ul>
 *   <li>合成— 见 {@link Recipe#twoIngredientRecipes 槽位2控制} {@link Recipe#threeIngredientRecipes 槽位3控制} {@link com.shatteredpixel.shatteredpixeldungeon.ui.QuickRecipe#getRecipes(int) 配方挂载}</li>
 *   <li>生成与出售— {@link com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.ShopRoom#generateItems() 常规商店出售}</li>
 * </ul>
 *
 * @see MarigoldBuff  菊寿凝身
 * @see Inebriation   醉意
 * @see AlcoholGas    酒气
 */
public class BottledMarigoldWine extends Potion{

    {
        image = ItemSpriteSheet.MARIGOLD_POTION;
    }

    static {
        canThrowPots.add(BottledMarigoldWine.class);      // 可喝可扔，扔时不再弹确认框
    }

    @Override
    public boolean isKnown() {
        return true;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override public int value()     { return 20 * quantity; }
    @Override public int energyVal() { return  4 * quantity; }


    // 喝：30% 醉意 + 30 回合菊寿凝身
    @Override
    public void apply(Hero hero) {
        Inebriation.addInebriation(hero, 30f);        // MAX_VALUE = 100，30 即 30%
        Buff.affect(hero, MarigoldBuff.class, 30f);
    }


    public void blt_apply(Hero hero) {
        Buff.affect(hero, MarigoldBuff.class, 30f);
    }

    // 扔：400 气体量酒气
    @Override
    public void shatter(int cell) {
        splash(cell);
        if (Dungeon.level.heroFOV[cell]) {
            Sample.INSTANCE.play(Assets.Sounds.SHATTER);
            Sample.INSTANCE.play(Assets.Sounds.GAS);
        }
        AlcoholGas.spawn(cell, 400);
    }

    // ---------- 配方（参照信号弹 SimpleRecipe）----------

    // 黄金菊种子 ×2 + 口粮 ×1 = 瓶装菊酒 ×2
    public static class SeedToWineFood extends Recipe.SimpleRecipe {
        {
            inputs = new Class[]{ Marigold.Seed.class, Food.class };
            inQuantity = new int[]{ 2, 1 };
            cost = 0;
            output = BottledMarigoldWine.class;
            outQuantity = 2;
        }
    }

    // 黄金菊种子 ×2 + 小型口粮 ×1 = 瓶装菊酒 ×1
    // (SimpleRecipe 用 getClass() 严格相等，SmallRation 不会被上面那条收走)
    public static class SeedToWineSmallRation extends Recipe.SimpleRecipe {
        {
            inputs = new Class[]{ Marigold.Seed.class, SmallRation.class };
            inQuantity = new int[]{ 2, 1 };
            cost = 0;
            output = BottledMarigoldWine.class;
            outQuantity = 1;
        }
    }
}
