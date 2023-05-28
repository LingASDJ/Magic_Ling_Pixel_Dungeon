/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.EXSG;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class PotionOfMindVision extends Potion {

    {
        icon = ItemSpriteSheet.Icons.POTION_MINDVIS;
    }

    @Override
    public void apply(Hero hero) {
        identify();

        if (Dungeon.isChallenged(EXSG)&& Random.Float()>0.4f) {
            if (Dungeon.level.mobs.size() > 0) {
                GLog.i(Messages.get(this, "can't_see_mobs"));

            } else {
                GLog.i(Messages.get(this, "cant'see_none"));
            }
            Buff.affect(hero, Blindness.class, 5f);
        } else {
            Buff.affect(hero, MindVision.class, MindVision.DURATION);
            Dungeon.observe();
            if (Dungeon.level.mobs.size() > 0) {
                GLog.i(Messages.get(this, "see_mobs"));
            } else {
                GLog.i(Messages.get(this, "see_none"));
            }
        }
    }


    @Override
    public int value() {
        return isKnown() ? 30 * quantity : super.value();
    }

    @Override
    public String desc() {
        //三元一次逻辑运算
        return Dungeon.isChallenged(Challenges.EXSG) ? Messages.get(this, "descx") : Messages.get(this, "desc");
    }

}
