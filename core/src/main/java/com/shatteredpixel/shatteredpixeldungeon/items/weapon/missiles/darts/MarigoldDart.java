package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MarigoldBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

/**
 * <div style="color:orange;font-weight:bold" >
 *     <h3>金菊飞镖</h3>
 *     <ul>
 *       <li>命中我方单位给予10回合菊寿凝身。</li>
 *       <li>命中敌方单位给予5回合眩晕。</li>
 *     </ul>
 * </div>
 *
 * @see com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MarigoldBuff  菊寿凝身
 * @see com.shatteredpixel.shatteredpixeldungeon.plants.Marigold            黄金菊
 */
public class MarigoldDart extends TippedDart {
    { image = ItemSpriteSheet.HOLY_DART; }   // TODO 占位

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        if (!processingChargedShot) {
            if (defender != attacker && attacker.alignment == defender.alignment) {
                Buff.affect(defender, MarigoldBuff.class, 10f);
                return 0;                        // 我方：不吃伤害
            } else {
                Buff.prolong(defender, Vertigo.class, 5f);
            }
        }
        return super.proc(attacker, defender, damage);
    }
}