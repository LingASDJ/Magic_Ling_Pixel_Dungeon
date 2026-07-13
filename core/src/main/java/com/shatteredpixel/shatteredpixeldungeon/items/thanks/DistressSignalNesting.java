package com.shatteredpixel.shatteredpixeldungeon.items.thanks;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DistressSignalNesting extends Artifact implements Item.ThanksItem {

    {
        image = ItemSpriteSheet.SOS_0;
        animation = false;
        levelCap = 3;           // 等级上限为3
        chargeCap = 1;          // 初始 1 充能上限
        charge = chargeCap;     // 购买时默认满充能
        defaultAction = AC_FIRE;
    }

    private class SignalBuff extends ArtifactBuff {
        {
            actPriority = HERO_PRIO;
        }
        @Override
        public boolean act() {
            spend(TICK);
            return true;
        }
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new SignalBuff();   // 现在使用静态内部类
    }

    // 提供信号弹
    private void spawnFlare(int number) {
        Item item = new FlareBullet();
        item.quantity(number);
        Hero hero = Dungeon.hero;
        if (item.collect(hero.belongings.backpack)) {
            GLog.p(Messages.get(this , "spawn_flare_bag"));
        } else {
            GLog.w(Messages.get(this , "spawn_flare_foot"));
            Dungeon.level.drop(item, hero.pos).sprite.drop();
        }
    }

    // 等级对应的显示强化数值
    @Override
    public int visiblyUpgraded() {
        if (!levelKnown) return 0;
        switch (level()) {
            default:
            case 0: return 0;
            case 1: return 3;
            case 2: return 7;
            case 3: return 10;
        }
    }

    // 图标随等级变化
    @Override
    public int image() {
        switch (level()) {
            default:
            case 0:
                return ItemSpriteSheet.SOS_0;
            case 1:
                return ItemSpriteSheet.SOS_1;
            case 2:
                return ItemSpriteSheet.SOS_2;
            case 3:
                return ItemSpriteSheet.SOS_3;
        }
    }

    @Override
    public String desc() {
        String desc = super.desc();
        switch (level()) {
            default:
            case 0:
                desc += "\n\n" + Messages.get(this, "level_0");
                break;
            case 1:
                desc += "\n\n" + Messages.get(this, "level_1");
                break;
            case 2:
                desc += "\n\n" + Messages.get(this, "level_2");
                break;
            case 3:
                desc += "\n\n" + Messages.get(this, "level_3");
                break;
        }
        return desc;
    }

    // 击发信号弹或激活狩猎狂欢
    public static final String AC_FIRE = "FIRE";
    public static final String AC_HUNT = "HUNT";

    // 在被穿戴、不被诅咒、无魔法免疫buff情况下，显示击发信号弹和激活狩猎狂欢（后者仅等级为3时显示）
    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped(hero) && !cursed && hero.buff(MagicImmune.class) == null) {
            actions.add(AC_FIRE);
            if (level() == 3)actions.add(AC_HUNT);
        }
        return actions;
    }

    // 修改 execute() 方法
    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_FIRE)) {
            if (!isEquipped(hero)) {
                GLog.w(Messages.get(Artifact.class, "need_to_equip"));
                return;
            }
            if (charge <= 0) {
                GLog.w(Messages.get(this, "no_charge"));
                return;
            }
            // 消耗 1 发
            charge--;
            partialCharge = 0;
            // 根据等级决定触发次数
            int triggers;
            switch (level()) {
                case 0: triggers = 6; break;
                case 1: triggers = 11; break;
                default: triggers = 17; break;
            }
            Buff.affect(hero, SniperSupport.class).setTriggers(triggers);
            int idx = Random.Int(3); // 0,1,2
            GLog.p(Messages.get(this, "fire_" + idx));
            hero.sprite.operate(hero.pos);
            hero.next();
        } else if (action.equals(AC_HUNT)) {
            if (!isEquipped(hero)) {
                GLog.w(Messages.get(Artifact.class, "need_to_equip"));
                return;
            }
            if (level() < 3 || charge != chargeCap || chargeCap != 3) {
                GLog.w(Messages.get(this, "hunt_unavailable"));
                return;
            }
            // 消耗全部 3 发
            charge = 0;
            partialCharge = 0;
            Buff.affect(hero, HuntingCarnival.class).setDuration(75);
            GLog.p(Messages.get(this, "hunt"));
            hero.sprite.operate(hero.pos);
            hero.next();
        }
    }

    public int getCharge() {
        return charge;
    }

    public int getChargeCap() {
        return chargeCap;
    }

    //增加充能
    public void addCharge(int amount) {
        charge = Math.min(charge + amount, chargeCap);
        partialCharge = 0;
    }

    // 每次升级赠送 1 发
    @Override
    public Item upgrade() {
        Item item = super.upgrade();
        // 每次升级获得 1 颗信号弹
        if (Dungeon.hero != null) {
            spawnFlare(1);
        }
        // +3 时充能上限变为 3
        if (item.level() == 3) {
            chargeCap = 3;
            charge = 3;
        }
        return item;
    }

    // 返回升级花费
    public int getUpgradeCost() {
        switch (level()) {
            case 0: return 500;
            case 1: return 1000;
            case 2: return 1500;
            default: return 0;
        }
    }
}

