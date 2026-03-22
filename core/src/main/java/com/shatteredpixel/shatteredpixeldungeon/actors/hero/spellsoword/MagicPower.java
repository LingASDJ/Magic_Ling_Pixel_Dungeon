package com.shatteredpixel.shatteredpixeldungeon.actors.hero.spellsoword;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

public class MagicPower extends Buff implements ActionIndicator.Action {

    {
        type = buffType.POSITIVE;
    }

    private float magicPower = 0;

    public float getMagic(){
        return magicPower;
    }

    public void usedMagic(int value) {

        if (magicPower < 0) {
            magicPower = 0;
        }
        magicPower -= value;

        if(hero.hasTalent(Talent.SHIELD_POWER) && hero.buff(MagicPowerShieldDelay.class) == null){
            float multiplier = hero.pointsInTalent(Talent.SHIELD_POWER) == 1 ? 1f : 1.5f;
            Buff.affect(target, Barrier.class).setShield((int)(value * multiplier));
            Buff.affect(hero, MagicPowerShieldDelay.class,5 * hero.pointsInTalent(Talent.SHIELD_POWER));
        }

        hero.sprite.showStatus(Window.SKYBULE_COLOR, "-"+ value);
    }

    public void getCountMagic(int value) {
        magicPower += value;
        hero.sprite.showStatus(Window.SKYBULE_COLOR, "+"+ value);
    }

    private float maxMagicPower;
    private int interval = 11;

    @Override
    public boolean act() {
        if (target.isAlive()) {
            if(target.buff(MagicPowerIceMagic.class) != null){
                BuffIndicator.refreshHero();
            } else if(magicPower > 0){
                ActionIndicator.setAction(this);
            }
            if (Dungeon.hero.pointsInTalent(Talent.MAGIC_COMPRESSION) >= 2 && interval == 11) {
                interval = 10;
            }
            if(Dungeon.hero.pointsInTalent(Talent.MAGIC_COMPRESSION) >= 1){
                maxMagicPower = 25;
            }
            if(magicPower < maxMagicPower){
                magicPower++;
            }
            spend( interval );
        } else {
            detach();
        }

        return true;
    }

    public float level() {
        return magicPower;
    }

    /**
     *
     * @param value 魔力上限
     * @param max 魔力最大初始上限
     */
    public void set( float value,float max) {
        if (magicPower <= value) {
            magicPower = value;
        }
        maxMagicPower = max;
    }

    @Override
    public int icon() {
        return BuffIndicator.BARKSKIN;
    }

    @Override
    public String iconTextDisplay() {
        return String.valueOf(magicPower);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", magicPower,maxMagicPower, interval);
    }

    private static final String MAGICPOWER	    = "magicpower";
    private static final String INTERVAL    = "interval";
    private static final String MAXPOWER    = "maxpower";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( INTERVAL,    interval );
        bundle.put( MAGICPOWER,  magicPower);
        bundle.put( MAXPOWER,    maxMagicPower);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        interval = bundle.getInt( INTERVAL );
        magicPower = bundle.getFloat( MAGICPOWER );
        maxMagicPower = bundle.getFloat( MAXPOWER);
    }

    @Override
    public String actionName() {
        return Messages.get(this,"icemagic");
    }

    @Override
    public int actionIcon() {
        return HeroIcon.ICEMAGIC;
    }

    @Override
    public int indicatorColor() {
        return 0x5BA0A4;
    }
    @Override
    public void doAction() {
        if(magicPower>=5){
            Buff.affect(hero, MagicPowerIceMagic.class,12f);
            GLog.p("已激活冰澪附魔");
            Buff.affect(hero, MagicPowerIceMagicCooldown.class,15f);
            BuffIndicator.refreshHero();
            usedMagic(5);
        }
    }

    public static class MagicPowerShieldDelay extends FlavourBuff { }
    public static class MagicPowerIceMagic extends FlavourBuff { }
    public static class MagicPowerIceMagicCooldown extends FlavourBuff { }
}
