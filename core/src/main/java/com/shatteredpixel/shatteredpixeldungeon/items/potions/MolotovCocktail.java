package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.BlazeZone;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ignite;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.Brew;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

/**
 * <div style="color:orange;font-weight:bold" >
 *     <h3>莫洛托夫鸡尾酒</h3>
 *     <ul>
 *       <li>合成：酒精合剂+液火药剂+2能量＝莫洛托夫鸡尾酒*4。</li>
 *       <li>投掷落地后炸出一片5x5的爆燃区域，持续10回合。</li>
 *       <li>落地当回合以及之后每回合，区域内的单位都会被施加5层引燃，并受到"楼层/5"点火焰伤害。</li>
 *       <li>引燃：每回合自然减1层；期间燃烧/磷火缠身/霜火/地狱火停止倒计时；
 *           身上没有任何燃烧类buff时，立刻获得等同于引燃层数的燃烧。</li>
 *       <li>英雄同时带着引燃和任意燃烧类buff时，每回合会被点燃一件背包物品。</li>
 *       <li>莫洛托夫鸡尾酒无法自然生成，也不在商店售卖，只能通过炼金合成，60底价。</li>
 *     </ul>
 * </div>
 *
 * <p>分散实现的效果如下
 * <ul>
 *   <li>合成— 见{@link com.shatteredpixel.shatteredpixeldungeon.items.Recipe#twoIngredientRecipes 槽位2配方}
 *       {@link com.shatteredpixel.shatteredpixeldungeon.ui.QuickRecipe#getRecipes(int) 配方挂载}</li>
 *   <li>投掷与碎裂— {@link Brew#defaultAction() 只能投掷}，落地走{@link #shatter(int)}</li>
 *   <li>爆燃区域— {@link BlazeZone#spawn(int)}，范围/回合数/引燃层数都是那边的常量</li>
 *   <li>生成与出售— 不参与自然生成，{@link com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.ShopRoom#generateItems() 商店}
 *       里也没有它；将来要上架就加在那里</li>
 * </ul>
 *
 * @see AlcoholMixture       酒精合剂（材料）
 * @see PotionOfLiquidFlame  液火药剂（材料）
 * @see Ignite               引燃
 * @see BlazeZone            爆燃区域
 * @see Burning              燃烧
 */
public class MolotovCocktail extends Brew {

    // —— 数值常量，方便后续调平衡 ——
    public static final int BREW_COST    = 2;   // 炼金能量
    public static final int OUT_QUANTITY = 4;   // 一次合成产出

    {
        image = ItemSpriteSheet.MOTO_FIRE;
    }

    // 扔：落点铺一片爆燃区域，引燃/伤害都由blob每回合结算
    @Override
    public void shatter(int cell) {
        splash(cell);
        if (Dungeon.level.heroFOV[cell]) {
            Sample.INSTANCE.play(Assets.Sounds.SHATTER);
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }
        BlazeZone.spawn(cell);
    }

    // ---------- 配方：酒精合剂 ×1 + 液火药剂 ×1 + 2能量 = 4瓶 ----------

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {
        {
            inputs      = new Class[]{ AlcoholMixture.class, PotionOfLiquidFlame.class };
            inQuantity  = new int[]{ 1, 1 };
            cost        = BREW_COST;
            output      = MolotovCocktail.class;
            outQuantity = OUT_QUANTITY;
        }
    }
}
