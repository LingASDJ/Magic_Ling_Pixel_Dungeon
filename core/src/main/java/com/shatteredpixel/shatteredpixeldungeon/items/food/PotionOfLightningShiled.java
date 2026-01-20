/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class PotionOfLightningShiled extends Item {

    public static final float TIME_TO_EAT	= 2f;

    public static final String AC_AAT	= "OAT";

    public float energy = Hunger.HUNGRY;

    {
        stackable = true;
        image = ItemSpriteSheet.ANTILIGHT;
        defaultAction = AC_AAT;
        bones = true;
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add(AC_AAT);
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {

        super.execute( hero, action );

        if (action.equals( AC_AAT )) {
            detach( hero.belongings.backpack );

            satisfy(hero);
            GLog.i( Messages.get(this, "look_msg") );
            Buff.affect(hero, ChampionHero.Light.class, ChampionHero.DURATION/10);
            hero.sprite.operate( hero.pos );
            hero.busy();
            Sample.INSTANCE.play( Assets.Sounds.DRINK );

            hero.spend( eatingTime() );
        }
    }

    protected float eatingTime(){
        if (Dungeon.hero.hasTalent(Talent.IRON_STOMACH)
                || Dungeon.hero.hasTalent(Talent.ENERGIZING_MEAL)
                || Dungeon.hero.hasTalent(Talent.MYSTICAL_MEAL)
                || Dungeon.hero.hasTalent(Talent.INVIGORATING_MEAL)){
            return TIME_TO_EAT - 2;
        } else {
            return TIME_TO_EAT;
        }
    }

    protected void satisfy( Hero hero ){
        if (Dungeon.isChallenged(Challenges.NO_FOOD)){
            Buff.affect(hero, Hunger.class).satisfy(energy/3f);
        } else {
            Buff.affect(hero, Hunger.class).satisfy(energy);
        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int value() {
        return 10 * quantity;
    }
}
