package com.shatteredpixel.shatteredpixeldungeon.items.rings;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RingoOfReTenacity extends Ring {

    {
        icon = ItemSpriteSheet.Icons.RING_RETEACT;
    }

    @Override
    protected RingBuff buff( ) {
        return new ReTenacity();
    }

    // 复刻元素戒指的属性计算公式
    @Override
    public String upgradeStat1(int level){
        if (cursed && cursedKnown) level = Math.min(-1, level-3);
        return Messages.decimalFormat("#.##", 100f * (1f - Math.pow(0.825f, level+1)))+ "%";
    }

    // 复刻坚韧戒指的属性计算公式
    @Override
    public String upgradeStat2(int level){
        if (cursed && cursedKnown) level = Math.min(-1, level-3);
        return Messages.decimalFormat("#.##", 100f * (1f - Math.pow(0.85f, level+1)))+ "%";
    }

    public String Ele(int level){
        if (cursed && cursedKnown) level = Math.min(-1, level-3);
        return Messages.decimalFormat("#.##", 100f * (1f - Math.pow(0.825f, level+1)));
    }

    public String Fpr(int level){
        if (cursed && cursedKnown) level = Math.min(-1, level-3);
        return Messages.decimalFormat("#.##", 100f * (1f - Math.pow(0.85f, level+1)));
    }

    // 整合描述数据
    @Override
    public String statsInfo() {
        if (isIdentified()) {
            int level = soloBuffedBonus();

            String info = Messages.get(this, "stats",
                    Ele(level-1),
                    Fpr(level-1));

            if (isEquipped(hero) && level != combinedBuffedBonus(hero)) {
                int combined = combinedBuffedBonus(hero);
                info += "\n\n" + Messages.get(this, "combined_stats",
                        Ele(combined-1),
                        Fpr(combined-1));
            }
            return info;
        } else {
            return Messages.get(this, "typical_stats",
                    Messages.decimalFormat("#.##", 17.5f),
                    Messages.decimalFormat("#.##", 15f));
        }
    }

    public class ReTenacity extends RingBuff {

        // 持有原版戒指的实例作为虚拟Buff源
        private RingOfElements virtualElements;
        private RingOfTenacity virtualTenacity;

        @Override
        public boolean attachTo(Char target) {
            if (super.attachTo(target)) {
                // 实例化原版戒指
                virtualElements = new RingOfElements();
                virtualTenacity = new RingOfTenacity();

                // 同步当前复合戒指的等级和诅咒状态给虚拟戒指
                syncVirtualRings();

                virtualElements.activate(target);
                virtualTenacity.activate(target);
                return true;
            }
            return false;
        }

        @Override
        public void detach() {
            if (virtualElements != null) {
                virtualElements.doUnequip(hero,false);
                virtualElements = null;
            }
            if (virtualTenacity != null) {
                virtualTenacity.doUnequip(hero,false);
                virtualTenacity = null;
            }
            super.detach();
        }

        @Override
        public boolean act() {
            syncVirtualRings();
            return super.act();
        }

        private void syncVirtualRings() {
            if (virtualElements != null) {
                int lvl = level();
                virtualElements.level(lvl);
                virtualElements.cursed = cursed;
                virtualElements.cursedKnown = cursedKnown;
            }
            if (virtualTenacity != null) {
                int lvl = level();
                virtualTenacity.level(lvl);
                virtualTenacity.cursed = cursed;
                virtualTenacity.cursedKnown = cursedKnown;
            }
        }
    }
}
