package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.AlcoholGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MarigoldBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

/**
 * <div style="color:orange;font-weight:bold" >
 *     <h3>传世佳酿</h3>
 *     <ul>
 *       <li>合成：瓶装菊酒+10能量。</li>
 *       <li>饮用获得90回合菊寿凝身。</li>
 *       <li>投掷后破碎并产生3500单位酒气。</li>
 *       <li>无法自然生成，只能炼金合成与商店售卖，1800底价，每层+100。</li>
 *       <li>重阳节（{@link RegularLevel.ChinaHoliday#CYJ}）限时道具。</li>
 *     </ul>
 * </div>
 *
 * <p>分散实现的效果如下
 * <ul>
 *   <li>合成— 见{@link com.shatteredpixel.shatteredpixeldungeon.items.Recipe#oneIngredientRecipes 单材料配方表}
 *       {@link com.shatteredpixel.shatteredpixeldungeon.ui.QuickRecipe#getRecipes(int) 配方挂载}</li>
 *   <li>生成与出售— {@link com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.ShopRoom#generateItems() 商店出售}，
 *       售价随深度见 {@link #value()}</li>
 * </ul>
 *
 * @see BottledMarigoldWine 瓶装菊酒（唯一材料）
 * @see MarigoldBuff        菊寿凝身
 * @see AlcoholGas          酒气
 */
public class LegacyWine extends Potion {

    // —— 数值常量，方便后续调平衡 ——
    public static final int BASE_VALUE       = 1800;   // 底价
    public static final int VALUE_PER_DEPTH  = 100;    // 每层加价
    public static final float BUFF_DURATION  = 90f;    // 菊寿凝身回合数
    public static final int GAS_VOLUME       = 3500;   // 酒气量（存续 = 3 + 3500/100 = 38 回合）
    public static final int BREW_COST        = 10;     // 炼金能量

    {
        image = ItemSpriteSheet.POTION_DEEPYELLOW;   // TODO 占位瓶身
    }

    static {
        canThrowPots.add(LegacyWine.class);       // 可喝可扔，扔时不弹确认框
    }

    @Override
    public boolean isKnown() {
        return true;   // 不在 Generator.Category.POTION 里，必须自己声明已鉴定
    }

    /**
     * 1800 + 100 × (层数-1)。value() 拿不到深度参数，只能读 Dungeon.depth。
     * 城镇/ZeroLevel 的 depth 可能是 0，用 Math.max 兜底避免负价。
     */
    @Override
    public int value() {
        return (int)(quantity * (BASE_VALUE + VALUE_PER_DEPTH * Math.max(0, Dungeon.depth - 1) * 0.1f));
    }

    @Override
    public int energyVal() {
        return 12 * quantity;    // 不要取 value()/5，否则拆解成 360+ 能量
    }

    // 喝：90 回合菊寿凝身
    @Override
    public void apply(Hero hero) {
        Buff.affect(hero, MarigoldBuff.class, BUFF_DURATION);
    }

    // 扔：3500 单位酒气（同瓶装菊酒的写法：自己放音效，不走 super.shatter 的 GLog）
    @Override
    public void shatter(int cell) {
        splash(cell);
        if (Dungeon.level.heroFOV[cell]) {
            Sample.INSTANCE.play(Assets.Sounds.SHATTER);
            Sample.INSTANCE.play(Assets.Sounds.GAS);
        }
        AlcoholGas.spawn(cell, GAS_VOLUME);
    }

    /** 是否处于重阳活动期（配方 / 商店上架的门控判断） */
    public static boolean active() {
        return RegularLevel.chinaHoliday == RegularLevel.ChinaHoliday.CYJ;
    }

    // ---------- 配方：瓶装菊酒 ×1 + 10 能量 ----------

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {
        {
            inputs      = new Class[]{ BottledMarigoldWine.class };
            inQuantity  = new int[]{ 1 };
            cost        = BREW_COST;
            output      = LegacyWine.class;
            outQuantity = 1;
        }

        // 限时门控：非重阳期炼金台里看不到这条配方
        @Override
        public boolean testIngredients(java.util.ArrayList<com.shatteredpixel.shatteredpixeldungeon.items.Item> ingredients) {
            return active() && super.testIngredients(ingredients);
        }
    }
}
