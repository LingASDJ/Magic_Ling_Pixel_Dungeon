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

package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WandEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

//for wands that directly damage a target
//wands with AOE effects count here (e.g. fireblast), but wands with indrect damage do not (e.g. venom, transfusion)
public abstract class DamageWand extends Wand{

	public int min(){
		return min(buffedLvl());
	}

	public abstract int min(int lvl);

	public int max(){
		return max(buffedLvl());
	}

	public abstract int max(int lvl);

	public int damageRoll(){
		return damageRoll(buffedLvl());
	}

	public int damageRoll(int lvl){
		int dmg = Hero.heroDamageIntRange(min(lvl), max(lvl));
		WandEmpower emp = Dungeon.hero.buff(WandEmpower.class);
		if (emp != null){
			dmg += emp.dmgBoost;
			emp.left--;
			if (emp.left <= 0) {
				emp.detach();
			}
			Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG, 0.75f, 1.2f);
		}
			if(Dungeon.hero.hasTalent(Talent.FANATICISM_MAGIC)){
				if (dmg > 0){
					Berserk berserk = Buff.affect(Dungeon.hero, Berserk.class);
					berserk.damage(dmg/2);
				}
		}

		return Dungeon.hero.buff(AnkhInvulnerability.GodDied.class)!=null ? dmg*2 : dmg;
	}

	//TODO some naming issues here. Consider renaming this method and externalizing char awareness buff
	public static void processSoulMark(Char target, int wandLevel){
		if (Dungeon.hero.hasTalent(Talent.ARCANE_VISION)) {
			int dur = 5 + 5*Dungeon.hero.pointsInTalent(Talent.ARCANE_VISION);
			Buff.append(Dungeon.hero, TalismanOfForesight.CharAwareness.class, dur).charID = target.id();
		}

		if (target != Dungeon.hero &&
				Dungeon.hero.subClass == HeroSubClass.WARLOCK &&
				//standard 1 - 0.92^x chance, plus 7%. Starts at 15%
				Random.Float() > (Math.pow(0.92f, (wandLevel)+1) - 0.07f)){
			SoulMark.prolong(target, SoulMark.class, SoulMark.DURATION + wandLevel);
		}
	}

	@Override
	public String statsDesc() {
		if (levelKnown)
			return Messages.get(this, "stats_desc", Dungeon.hero.buff(AnkhInvulnerability.GodDied.class)!=null?
					min()*2:min(),	Dungeon.hero.buff(AnkhInvulnerability.GodDied.class)!=null?
					max()*2:max());
		else
			return Messages.get(this, "stats_desc", Dungeon.hero.buff(AnkhInvulnerability.GodDied.class)!=null?
					min(0)*2:min(0), Dungeon.hero.buff(AnkhInvulnerability.GodDied.class)!=null?
					max(0)*2:max(0));
	}

	@Override
	public String upgradeStat1(int level) {
		return min(level) + "-" + max(level);
	}
}
