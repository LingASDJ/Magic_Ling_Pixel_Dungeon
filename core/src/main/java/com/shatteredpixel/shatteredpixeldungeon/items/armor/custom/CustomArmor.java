package com.shatteredpixel.shatteredpixeldungeon.items.armor.custom;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

/**自定义护甲是除开地牢之外的独立护甲，
 * 但同时拥有自己的能力，无法使用皇冠的能力
 * 为此使用抽象类作为模板 */
public abstract class CustomArmor extends Armor {
    private Charger charger;
    public float charge = 0;

    public class Charger extends Buff {
        @Override
        public boolean act() {
            LockedFloor lock = target.buff(LockedFloor.class);
            if (lock == null || lock.regenOn()) {
                charge += 100 / 500f; //500 turns to full charge
                updateQuickslot();
                if (charge > 100) {
                    charge = 100;
                }
            }
            spend(TICK);
            return true;
        }
    }

    @Override
    public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
        if (super.doUnequip( hero, collect, single )) {
            if (charger != null){
                charger.detach();
                charger = null;
            }
            return true;

        } else {
            return false;

        }
    }

    protected static final String AC_CUSTOM       = "CUSTOM";

    {
        image = ItemSpriteSheet.ARMOR_ANCITY;
    }

    //自定义护甲从8开始计算
    public CustomArmor() {
        super( 0 );
    }

    @Override
    public String status() {
        return Messages.format( "%.0f%%", Math.floor(charge) );
    }

    @Override
    public void activate(Char ch) {
        super.activate(ch);
        charger = new Charger();
        charger.attachTo(ch);
    }

    /**默认情况下，需要武器等级大于等于2级才能被使用*/
    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if(this.level()>=2){
            actions.add(AC_CUSTOM);
        }
        return actions;
    }

    /**通过检测图像，在大于目标等级后添加快捷按钮*/
    @Override
    public int image() {
        super.image();
        if (level() >= 2) {
            defaultAction = AC_CUSTOM;
        }
        return image;
    }
}
