package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfElements;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

import java.util.HashSet;

//菊寿凝身：持续期间增加0.8生命恢复，0～层数/2.5防御（向上取整，至少为0～1）和（层数）％元素抗性。
//守望者效果：菊寿凝身的所有效果*1.5。
public class MarigoldBuff extends FlavourBuff{
    // 守望者额外倍率
    public static float MardenExtendMultiplier = 0.5f;
    // 基础生命恢复数值
    public static float BaseExtendRenge = 0.8f;

    {
        type = buffType.POSITIVE;
        announced = true;
    }

    @Override
    public int icon() {
        return BuffIndicator.MARIGOLD;
    }

    public float ExtendRegen(){
        float multiplier = 1f;
        if(target instanceof Hero
                && ((Hero) target).subClass == HeroSubClass.WARDEN) multiplier += MardenExtendMultiplier;
        return multiplier*BaseExtendRenge;
    }

    public static final HashSet<Class> RESISTED = RingOfElements.RESISTS;

    public static float resist(Char target, Class effect){
        MarigoldBuff b = target.buff(MarigoldBuff.class);
        if (b == null) return 1f;
        float mult = 1f;
        if (target instanceof Hero && ((Hero) target).subClass == HeroSubClass.WARDEN)
            mult += MardenExtendMultiplier;                          // ×1.5
        for (Class c : RESISTED){
            if (c.isAssignableFrom(effect)){
                float res = 0.01f * Dungeon.depth * mult;             // “层数 %”
                return 1f - Math.min(res, 0.75f);                     // 必须封顶
            }
        }
        return 1f;
    }

    public int defenseMax(){
        float multiplier = 1f;
        if (target instanceof Hero && ((Hero) target).subClass == HeroSubClass.WARDEN)
            multiplier += MardenExtendMultiplier;
        return Math.max(0, (int)Math.ceil(Dungeon.depth/2.5f* multiplier));
    }

    // 多实例取最强，和 Barkskin.currentLevel 同构
    public static int currentDefense(Char ch){
        int best = 0;
        for (MarigoldBuff b : ch.buffs(MarigoldBuff.class)) best = Math.max(best, b.defenseMax());
        return best;
    }

}
