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
package com.shatteredpixel.shatteredpixeldungeon.items.armor.curses;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Daze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.FerretTuft;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.GameMath;

public class Stone extends Armor.Glyph {

	private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );

	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {
		testing = true;

		float evasion = defender.defenseSkill(defender);
		testing = false;

		if (defender.buff(Bless.class) != null) evasion *= 1.25f;
		if (defender.buff( Hex.class) != null) evasion *= 0.8f;
		if (defender.buff( Daze.class) != null) evasion *= 0.5f;
		for (ChampionEnemy buff : defender.buffs(ChampionEnemy.class)){
			evasion *= buff.evasionAndAccuracyFactor();
		}
		evasion *= AscensionChallenge.statModifier(defender);
		evasion *= FerretTuft.evasionMultiplier();
		evasion *= genericProcChanceMultiplier(defender);

		float damageReduction = evasion * 0.75f;
		float damageMultiplier = GameMath.gate(0.25f, 1f - damageReduction / 100f, 1f);

		damage = (int)Math.ceil(damage * damageMultiplier);
		return damage;
	}

	private static boolean testing = false;
	public static boolean testingEvasion(){
		return testing;
	}

	@Override
	public String desc() {
		Char user = Dungeon.hero;
		// 装备未穿戴时提示
		if (user == null){
			return Messages.get(this, "desc", 0, 0f);
		}

		testing = true;
		float evasion = user.defenseSkill(null);
		testing = false;

		if (user.buff(Bless.class) != null) evasion *= 1.25f;
		if (user.buff( Hex.class) != null) evasion *= 0.8f;
		if (user.buff( Daze.class) != null) evasion *= 0.5f;
		for (ChampionEnemy buff : user.buffs(ChampionEnemy.class)){
			evasion *= buff.evasionAndAccuracyFactor();
		}
		evasion *= AscensionChallenge.statModifier(user);
		evasion *= FerretTuft.evasionMultiplier();
		evasion *= genericProcChanceMultiplier(user);

		float damageReduction = evasion * 0.75f;
		damageReduction = GameMath.gate(0, damageReduction, 75);

		return Messages.get(this, "desc", evasion, damageReduction);
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return BLACK;
	}

	@Override
	public boolean curse() {
		return true;
	}
}
