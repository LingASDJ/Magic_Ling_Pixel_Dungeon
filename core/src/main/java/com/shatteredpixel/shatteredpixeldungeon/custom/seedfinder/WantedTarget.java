package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;

/**
 * 结构化查询目标：物品类 + 最低等级 + 可选附魔/铭文体。
 * 匹配要求物品类精确一致、强化等级不低于要求；
 * 指定 aug 时，武器走附魔分支、护甲走铭文分支（由 augIsGlyph 在构造时定型，
 * 调用方保证 aug 为附魔则 cls 必为 Weapon、aug 为铭文则 cls 必为 Armor）。
 */
public class WantedTarget {

    public final Class<? extends Item> cls;
    public final int minLevel;
    public final Class<?> aug;
    private final boolean augIsGlyph;

    public WantedTarget(Item item) {
        this.cls = item.getClass();
        this.minLevel = item.trueLevel();
        Object aug = null;
        if (item instanceof Armor) {
            aug = ((Armor) item).glyph;
        } else if (item instanceof Weapon) {
            aug = ((Weapon) item).enchantment;
        }
        if (aug != null) {
            this.aug = aug.getClass();
            this.augIsGlyph = aug instanceof Armor.Glyph;
        } else {
            this.aug = null;
            this.augIsGlyph = false;
        }
    }

    public boolean matches(Item item) {
        if (item.getClass() != cls) return false;
        if (item.level() < minLevel) return false;
        if (aug == null) return true;
        if (augIsGlyph) {
            Armor.Glyph g = ((Armor) item).glyph;
            return g != null && g.getClass() == aug;
        }
        Weapon.Enchantment e = ((Weapon) item).enchantment;
        return e != null && e.getClass() == aug;
    }
}
