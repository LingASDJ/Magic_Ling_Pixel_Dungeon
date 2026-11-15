/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.DamageContext;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

public class CorrosiveGas extends Blob {

	//FIXME should have strength per-cell
	private int strength = 0;

	//used in specific cases where the source of the corrosion is important for death logic
	private Class source;
	private int sourceHeroId = -1;

	@Override
	protected void evolve() {
		super.evolve();

		if (volume == 0){
			strength = 0;
			source = null;
			sourceHeroId = -1;
		} else {
			Char ch;
			int cell;

			for (int i = area.left; i < area.right; i++){
				for (int j = area.top; j < area.bottom; j++){
					cell = i + j*Dungeon.level.width();
					if (cur[cell] > 0 && (ch = Actor.findChar( cell )) != null) {
						if (!ch.isImmune(this.getClass()))
							Buff.affect(ch, Corrosion.class).setWithHero(
									2f,
									strength,
									source,
									DamageContext.heroById(sourceHeroId)
							);
					}
				}
			}
		}
	}

	public CorrosiveGas setStrength(int str){
		return setStrength(str, null);
	}

	public CorrosiveGas setStrength(int str, Class source){
		return setStrengthWithHero(str, source, null);
	}

	public CorrosiveGas setStrengthWithHero(int str, Class source, Hero sourceHero){
		if (str > strength) {
			strength = str;
			this.source = source;
			sourceHeroId = sourceHero != null ? sourceHero.id() : -1;
		}
		return this;
	}

	private static final String STRENGTH = "strength";
	private static final String SOURCE	= "source";
	private static final String SOURCE_HERO_ID = "source_hero_id";

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		strength = bundle.getInt( STRENGTH );
		source = bundle.getClass( SOURCE );
		sourceHeroId = bundle.contains(SOURCE_HERO_ID)
				? bundle.getInt(SOURCE_HERO_ID)
				: -1;
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( STRENGTH, strength );
		bundle.put( SOURCE, source );
		bundle.put( SOURCE_HERO_ID, sourceHeroId );
	}

	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );

		emitter.pour( Speck.factory(Speck.CORROSION), 0.4f );
	}

	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}
}
