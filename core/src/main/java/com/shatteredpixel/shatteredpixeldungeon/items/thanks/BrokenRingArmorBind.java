package com.shatteredpixel.shatteredpixeldungeon.items.thanks;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.props.BrokenRing;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

/*
* 首先存储被选中的装备,再读取其描述文本与图标以及玩家装备其产生的外观
*  */

public class BrokenRingArmorBind extends Armor {

    public Armor bound;   // 被束缚的真护甲（本体存在这里）

    public BrokenRingArmorBind() {
        super(1);
    }

    // 束缚真护甲时调用：先存本体引用，再抄外观字段
    public void mimic(Armor original) {
        bound = original;
        image = original.image;
        tier = original.tier;   // 让角色模型保持原护甲外观
    }

    @Override
    public String name() {
        return bound != null ? bound.name() : super.name();
    }

    @Override
    public String desc() {
        return BrokenRing.bindDesc(bound);
    }

    @Override
    public String defaultAction() {
        return null;   // 防止被拖进快捷栏
    }

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = new ArrayList<>();
		if (isEquipped(hero)) {
			actions.add(BrokenRing.AC_RELEASE);
		} else {
			actions.add(AC_EQUIP);
		}
		return actions;
	}

    @Override
    public String actionName(String action, Hero hero) {
        String name = BrokenRing.bindActionName(action);
        return name != null ? name : super.actionName(action, hero);
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(BrokenRing.AC_RELEASE)) {
            BrokenRing.releaseBind(hero, this);
        }
    }

    // 不可被普通卸下（防止被新装备挤进背包，连带本体）
    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        return false;
    }

    // 伴生物不产生任何效果：读档时基类会调用 activate，置空避免 passiveBuff/buff 为 null 导致 NPE
    @Override
    public void activate(Char ch) {
    }

    // 数值全部压成 0：不提供减伤/闪避/力量惩罚
    @Override
    public int DRMax(int lvl) { return 0; }
    @Override
    public int DRMin(int lvl) { return 0; }
    @Override
    public float evasionFactor(Char owner, float evasion) { return evasion; }
    @Override
    public int STRReq() { return 0; }

    // 不与卷轴交互
    @Override
    public boolean isUpgradable() { return false; }
    @Override
    public boolean isIdentified() { return true; }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("bound", bound);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        bound = (Armor) bundle.get("bound");
        if (bound != null) {
            image = bound.image;
            tier = bound.tier;
        }
    }
}
