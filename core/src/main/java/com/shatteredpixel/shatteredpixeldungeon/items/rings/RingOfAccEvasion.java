package com.shatteredpixel.shatteredpixeldungeon.items.rings;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RingOfAccEvasion extends Ring {

    {
        icon = ItemSpriteSheet.Icons.RING_CLASACT;
    }

    @Override
    protected RingBuff buff( ) {
        return new AccEvasion();
    }

    @Override
    public String upgradeStat1(int level){
        if (cursed && cursedKnown) level = Math.min(-1, level-3);
        return Messages.decimalFormat("#.##", 100f * (Math.pow(1.3f, level+1)-1f))+"%";
    }

    @Override
    public String upgradeStat2(int level){
        if (cursed && cursedKnown) level = Math.min(-1, level-3);
        return Messages.decimalFormat("#.##", 100f * (Math.pow(1.125f, level+1)-1f))+"%";
    }


    public String Acc(int level){
        if (cursed && cursedKnown) level = Math.min(-1, level-3);
        return Messages.decimalFormat("#.##", 100f * (Math.pow(1.3f, level)-1f));
    }

    public String Eva(int level){
        if (cursed && cursedKnown) level = Math.min(-1, level-3);
        return Messages.decimalFormat("#.##", 100f * (Math.pow(1.125f, level)-1f));
    }

    // 整合描述数据
    @Override
    public String statsInfo() {
        if (isIdentified()) {
            int level = soloBuffedBonus();

            String info = Messages.get(this, "stats",
                    Acc(level),
                    Eva(level));

            if (isEquipped(hero) && level != combinedBuffedBonus(hero)) {
                int combined = combinedBuffedBonus(hero);
                info += "\n\n" + Messages.get(this, "combined_stats",
                        Acc(combined),
                        Eva(combined));
            }
            return info;
        } else {
            return Messages.get(this, "typical_stats",
                    Messages.decimalFormat("#.##", 30f),
                    Messages.decimalFormat("#.##", 12.5f));
        }
    }

    public class AccEvasion extends RingBuff {

        // 持有原版戒指的实例作为虚拟Buff源
        private RingOfAccuracy virtualAccuracy;
        private RingOfEvasion virtualEvasion;

        @Override
        public boolean attachTo(Char target) {
            if (super.attachTo(target)) {
                // 实例化原版戒指
                virtualAccuracy = new RingOfAccuracy();
                virtualEvasion = new RingOfEvasion();

                // 同步当前复合戒指的等级和诅咒状态给虚拟戒指
                syncVirtualRings();

                virtualAccuracy.activate(target);
                virtualEvasion.activate(target);
                return true;
            }
            return false;
        }

        @Override
        public void detach() {
            // 移除时，调用原版戒指的 deactivate，它们会自动清理自己附加的 Buff
            if (virtualAccuracy != null) {
                virtualAccuracy.doUnequip(hero,false);
                virtualAccuracy = null;
            }
            if (virtualEvasion != null) {
                virtualEvasion.doUnequip(hero,false);
                virtualEvasion = null;
            }
            super.detach();
        }

        // 核心：每次 RingBuff 行动时（每回合触发），同步虚拟戒指的等级和诅咒状态
        @Override
        public boolean act() {
            syncVirtualRings();
            return super.act();
        }

        // 抽取同步逻辑为独立方法，方便复用
        private void syncVirtualRings() {
            if (virtualAccuracy != null) {
                int lvl = level();
                virtualAccuracy.level(lvl);
                virtualAccuracy.cursed = cursed;
                virtualAccuracy.cursedKnown = cursedKnown;
            }
            if (virtualEvasion != null) {
                int lvl = level();
                virtualEvasion.level(lvl);
                virtualEvasion.cursed = cursed;
                virtualEvasion.cursedKnown = cursedKnown;
            }
        }
    }
}
