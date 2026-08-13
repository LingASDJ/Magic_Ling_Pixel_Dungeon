package com.shatteredpixel.shatteredpixeldungeon.items.props;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindofMisc;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.thanks.BrokenRingArmorBind;
import com.shatteredpixel.shatteredpixeldungeon.items.thanks.BrokenRingArtifactBind;
import com.shatteredpixel.shatteredpixeldungeon.items.thanks.BrokenRingMiscBind;
import com.shatteredpixel.shatteredpixeldungeon.items.thanks.BrokenRingRingBind;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class BrokenRing extends Prop {

    {
        rareness = 2;
        kind = 1;
        image = ItemSpriteSheet.BROKEN_RING;
    }

    // ---- 伴生物共用逻辑 ----

    public static final String AC_RELEASE = "RELEASE";

    // 伴生物描述 = 本体描述 + 束缚提示
    public static String bindDesc(Item bound) {
        return (bound != null ? bound.desc() : "") + "\n\n"
                + Messages.get(BrokenRing.class, "bound_desc");
    }

    // 伴生物唯一的动作按钮文本
    public static String bindActionName(String action) {
        if (action.equals(AC_RELEASE)) return Messages.get(BrokenRing.class, "ac_release");
        return null;
    }

    // 判断是否为伴生物：伴生物不参与嬗变等卷轴交互
    public static boolean isBind(Item item) {
        return item instanceof BrokenRingArmorBind
                || item instanceof BrokenRingArtifactBind
                || item instanceof BrokenRingRingBind
                || item instanceof BrokenRingMiscBind;
    }

    // 玩家点“卸下以解除束缚”：本体收回背包；背包满了则掉在地上
    public static void releaseBind(Hero hero, Item bind) {
        Item real = takeBound(hero, bind);
        if (real == null) return;

        if (!real.collect()) {
            Dungeon.level.drop(real, hero.pos).sprite.drop();
        }
    }

    // 血量恢复自动解除：不耗回合，直接放回栏位并恢复效果
    public static void releaseBindSilent(Hero hero, Item bind) {
        Item real = takeBound(hero, bind);
        if (real == null) return;

        if (real instanceof Armor) {
            hero.belongings.armor = (Armor) real;
        } else if (real instanceof Artifact) {
            hero.belongings.artifact = (Artifact) real;
        } else if (real instanceof Ring) {
            hero.belongings.ring = (Ring) real;
        } else if (real instanceof KindofMisc) {
            hero.belongings.misc = (KindofMisc) real;
        }

        // 恢复效果：戒指挂回 RingBuff、神器挂回被动、护甲挂回护甲 buff（通用装备是空实现）
        ((EquipableItem) real).activate(hero);
        if (real instanceof Armor) {
            ((HeroSprite) hero.sprite).updateArmor();
        }
    }

    // 从伴生物取出本体，并把伴生物从栏位移除
    private static Item takeBound(Hero hero, Item bind) {
        Item real;
        if (bind instanceof BrokenRingArmorBind) {
            real = ((BrokenRingArmorBind) bind).bound;
            if (hero.belongings.armor == bind) hero.belongings.armor = null;
        } else if (bind instanceof BrokenRingArtifactBind) {
            real = ((BrokenRingArtifactBind) bind).bound;
            if (hero.belongings.artifact == bind) hero.belongings.artifact = null;
        } else if (bind instanceof BrokenRingRingBind) {
            real = ((BrokenRingRingBind) bind).bound;
            if (hero.belongings.ring == bind) hero.belongings.ring = null;
        } else if (bind instanceof BrokenRingMiscBind) {
            real = ((BrokenRingMiscBind) bind).bound;
            if (hero.belongings.misc == bind) hero.belongings.misc = null;
        } else {
            return null;
        }
        return real;
    }

}

