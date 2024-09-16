/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class Pasty extends Food {

	{
		reset();

		energy = Hunger.STARVING;

		bones = true;
	}

	@Override
	public void reset() {
		super.reset();
		switch(RegularLevel.holiday){
			case NONE: default:
				image = ItemSpriteSheet.PASTY;
				break;
			case HWEEN:
				image = ItemSpriteSheet.PUMPKIN_PIE;
				break;
			case XMAS:
				image = ItemSpriteSheet.CANDY_CANE;
				break;
			case ZQJ:
				image = ItemSpriteSheet.DG1;
				break;
			case CJ:
				image = ItemSpriteSheet.Fish_A;
				break;
			case QMJ:
				image = ItemSpriteSheet.QKA;
				break;
		}
	}

	@Override
	protected void satisfy(Hero hero) {
		super.satisfy(hero);

		switch(RegularLevel.holiday){
			case NONE:
				break; //do nothing extra
			case HWEEN:
				//heals for 10% max hp
				hero.HP = Math.min(hero.HP + hero.HT/10, hero.HT);
				hero.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
				break;
			case XMAS:
				Buff.affect( hero, Recharging.class, 2f ); //half of a charge
				ScrollOfRecharging.charge( hero );
				break;
			case ZQJ:
				Buff.affect(hero, Healing.class).setHeal((int) (0.2f * hero.HT + 14), 0.25f, 0);
				Buff.affect(hero, Haste.class, 10f);
				ScrollOfRecharging.charge( hero );
				GLog.p(Messages.get(this, "moonling"));
				break;
			case CJ:
				//...but it also awards an extra item that restores 150 hunger
				FishLeftover left = new FishLeftover();
				Dungeon.level.drop(left, hero.pos).sprite.drop();
				hero.belongings.charge(0.5f); //2 turns worth
				ScrollOfRecharging.charge( hero );
				Buff.affect(hero, Haste.class, 4f);
				Buff.affect(Dungeon.hero, ArtifactRecharge.class).prolong(hero.HT/10f);
				break;
			case QMJ:
				//...but it also awards an extra item that restores 150 hunger
				QingKong lings = new QingKong();
				Dungeon.level.drop(lings, hero.pos).sprite.drop();
				hero.belongings.charge(0.5f); //2 turns worth
				ScrollOfRecharging.charge( hero );
				Buff.affect(hero, Barrier.class).setShield(8);
				Buff.affect( hero, MindVision.class, 8f );
				break;
		}
	}

	@Override
	public String name() {
		switch(RegularLevel.holiday){
			case NONE: default:
				return Messages.get(this, "pasty");
			case HWEEN:
				return Messages.get(this, "pie");
			case XMAS:
				return Messages.get(this, "cane");
			case ZQJ:
				return Messages.get(this, "moon");
			case CJ:
				return Messages.get(this, "fish_name");
			case QMJ:
				return Messages.get(this, "qk_name");
		}
	}

	@Override
	public String info() {
		switch(RegularLevel.holiday){
			case NONE: default:
				return Messages.get(this, "pasty_desc");
			case HWEEN:
				return Messages.get(this, "pie_desc");
			case XMAS:
				return Messages.get(this, "cane_desc");
			case ZQJ:
				return Messages.get(this, "moon_desc", Dungeon.hero.name());
			case CJ:
				return Messages.get(this, "fish_desc");
			case QMJ:
				return Messages.get(this, "qk_desc");
		}
	}

	public static class FishLeftover extends Food {

		{
			image = ItemSpriteSheet.Fish_B;
			energy = Hunger.HUNGRY/2;
		}

	}

	public static class QingKong extends Food {

		{
			image = ItemSpriteSheet.QKB;
			energy = Hunger.HUNGRY/2;
		}

		@Override
		protected void satisfy(Hero hero) {
			super.satisfy(hero);
			Buff.affect(hero, Barrier.class).setShield(8);
			Buff.affect(hero, MindVision.class, 5f);
		}

	}

	@Override
	public int value() {
		return 20 * quantity;
	}
}
