package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

/**
 * 禁魔：被纯白之花绽放波及后，短时间内无法使用法杖施法。
 *
 * <p>不显示 Buff 图标（{@link FlavourBuff} 默认 icon 为 {@code BuffIndicator.NONE}）。</p>
 */
public class Silenced extends FlavourBuff {

    public static final float DURATION = 7f;

    {
        type = buffType.NEGATIVE;
        announced = true;
    }
}
