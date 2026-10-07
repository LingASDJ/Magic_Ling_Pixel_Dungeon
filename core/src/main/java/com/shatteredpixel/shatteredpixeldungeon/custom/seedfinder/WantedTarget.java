package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;

/**
 * 结构化查询目标：物品类集合（≥1 个，任一命中即满足类条件）+ 最低等级 + 可选附魔/铭文体。
 * 匹配要求物品类命中集合中的任意一个、强化等级不低于要求；
 * 文本模糊名（如 "之戒"）会同时命中多个类（全部 X之戒 戒指），用一个目标表达"任一命中"，
 * 修复旧逻辑只取首个类导致"之戒+2"这类查询退化为单一稀有戒指、常常扫出 NONE 的问题。
 * 指定 aug 时，武器走附魔分支、护甲走铭文分支（由 augIsGlyph 在构造时定型，
 * 调用方保证 aug 为附魔则命中类必为 Weapon、aug 为铭文则命中类必为 Armor）。
 */
public class WantedTarget {

    /** 可命中的物品类集合；null 表示纯等级目标（"+N"，任意物品只看等级） */
    public final Class<? extends Item>[] classes;
    public final int minLevel;
    public final Class<?> aug;
    private final boolean augIsGlyph;

    /** 供子进程根据任务文件中的类名重建目标（aug 为 null 表示不限定附魔/铭文；cls 为 null 表示纯等级目标） */
    @SuppressWarnings("unchecked")
    public WantedTarget(Class<? extends Item> cls, int minLevel, Class<?> aug) {
        this(cls == null ? null : (Class<? extends Item>[]) new Class<?>[]{cls}, minLevel, aug);
    }

    /** 多类目标：文本模糊名命中多个类时使用；任一命中即满足类条件 */
    public WantedTarget(Class<? extends Item>[] classes, int minLevel, Class<?> aug) {
        this.classes = classes;
        this.minLevel = minLevel;
        this.aug = aug;
        this.augIsGlyph = aug != null && Armor.Glyph.class.isAssignableFrom(aug);
    }

    public WantedTarget(Item item) {
        this.classes = (Class<? extends Item>[]) new Class<?>[]{item.getClass()};
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

    /** 序列化用：命中类名列表（逗号分隔，写入任务文件）；纯等级目标（classes 为 null，如文本输入 "+2"）写空串 */
    public String clsName() {
        if (classes == null || classes.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (Class<? extends Item> c : classes) {
            if (sb.length() > 0) sb.append(',');
            sb.append(c.getName());
        }
        return sb.toString();
    }

    /** 序列化用：附魔/铭文类名，空串表示不限定 */
    public String augName() {
        return aug == null ? "" : aug.getName();
    }

    public boolean matches(Item item) {
        if (item == null) return false;
        // classes 为 null 表示纯等级目标（"+N"）：任意物品，只看等级
        if (classes != null) {
            boolean clsHit = false;
            for (Class<? extends Item> c : classes) {
                if (item.getClass() == c) {
                    clsHit = true;
                    break;
                }
            }
            if (!clsHit) return false;
        }
        if (item.level() < minLevel) return false;
        if (aug == null) return true;
        if (augIsGlyph) {
            if (!(item instanceof Armor)) return false;
            Armor.Glyph g = ((Armor) item).glyph;
            return g != null && g.getClass() == aug;
        }
        if (!(item instanceof Weapon)) return false;
        Weapon.Enchantment e = ((Weapon) item).enchantment;
        return e != null && e.getClass() == aug;
    }
}
