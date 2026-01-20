package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class TrueInvisibiity extends FlavourBuff {

    public static final float DURATION	= 20f;

    {
        type = Buff.buffType.POSITIVE;
        announced = true;
    }

    @Override
    public boolean attachTo( Char target ) {
        if (super.attachTo( target )) {
            target.invisible++;
            if (target instanceof Hero && ((Hero) target).subClass == HeroSubClass.ASSASSIN){
                Buff.affect(target, Preparation.class);
            }
            if (target instanceof Hero && ((Hero) target).hasTalent(Talent.PROTECTIVE_SHADOWS)){
                Buff.affect(target, Talent.ProtectiveShadowsTracker.class);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void detach() {
        if (target.invisible > 0)
            target.invisible--;
        super.detach();
    }

    @Override
    public int icon() {
        return BuffIndicator.NONE;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add( CharSprite.State.TRUE_INVISIBLE );
        else if (target.invisible == 0) target.sprite.remove( CharSprite.State.TRUE_INVISIBLE );
    }

    public static void dispel() {
        if (Dungeon.hero == null) return;

        dispel(Dungeon.hero);
    }

    public static void dispel(Char ch){

        for ( Buff invis : ch.buffs( Invisibility.class )){
            invis.detach();
        }
        CloakOfShadows.cloakStealth cloakBuff = ch.buff( CloakOfShadows.cloakStealth.class );
        if (cloakBuff != null) {
            cloakBuff.dispel();
        }

        //these aren't forms of invisibility, but do dispel at the same time as it.
        TimekeepersHourglass.timeFreeze timeFreeze = ch.buff( TimekeepersHourglass.timeFreeze.class );
        if (timeFreeze != null) {
            timeFreeze.detach();
        }

        Preparation prep = ch.buff( Preparation.class );
        if (prep != null){
            prep.detach();
        }

        Swiftthistle.TimeBubble bubble =  ch.buff( Swiftthistle.TimeBubble.class );
        if (bubble != null){
            bubble.detach();
        }
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns());
    }

}

