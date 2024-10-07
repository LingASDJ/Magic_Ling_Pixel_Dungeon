package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.props.RapidEarthRoot;
import com.shatteredpixel.shatteredpixeldungeon.items.props.WenStudyingPaperOne;
import com.shatteredpixel.shatteredpixeldungeon.items.props.YanStudyingPaperTwo;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class PropBuff extends Buff{

    {
        type = buffType.POSITIVE;
    }

    @Override
    public boolean act() {

        if(Dungeon.depth>0){

            Hero hero = Dungeon.hero;

            if(Dungeon.hero.belongings.getItem(RapidEarthRoot.class)!=null) {
                Buff.affect(hero, Barkskin.class).set((int) hero.getZone() * 2, 10);
                spend(250f);
            }
            if(Dungeon.hero.belongings.getItem(WenStudyingPaperOne.class)!=null) {
                Buff.affect(hero, Swiftthistle.TimeBubble.class).setLeft(5f);
                spend(125f);
            }
            if(Dungeon.hero.belongings.getItem(YanStudyingPaperTwo.class)!=null) {
                Buff.affect(hero, Haste.class,5f);
                spend(125f);
            }
        }

        return true;
    }

    @Override
    public int icon() {
        return BuffIndicator.BARKSKIN;
    }

}
