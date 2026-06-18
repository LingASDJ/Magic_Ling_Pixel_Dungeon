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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LifeLink;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bandit;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ColdMagicRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Guard;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Monk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Necromancer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RedSwarm;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Skeleton;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Thief;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Warlock;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.WarlockHead;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSirensSong;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KingSword extends MeleeWeapon {

	{
		image = ItemSpriteSheet.KING_SWORD;
		hitSound = Assets.Sounds.HIT_SLASH;
		hitSoundPitch = 1f;
		DLY = 1.5f;
		tier = 4;
	}

	@Override
	public int min(int lvl) {
		return 10 + lvl;
	}

	@Override
	public int max(int lvl) {
		return 35 + lvl * 7;
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		boolean isChampionEnemy = false;

		for (ChampionEnemy buff : defender.buffs(ChampionEnemy.class)) {
			isChampionEnemy = true;
			break;
		}

		if(defender.properties.contains(Char.Property.DEMONIC) || isChampionEnemy){
			damage = Math.round(damage * 1.5f);
		}

		if(Random.Float() < 0.4f + level() * 0.05f){
			Buff.affect(defender, Vulnerable.class, 5+ (float) level() /2);
		}

		return super.proc(attacker, defender, damage);
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		if (target == null) return;

		Char enemy = Actor.findChar(target);
		if (enemy == null || enemy == hero || !Dungeon.level.heroFOV[target]) {
			GLog.w(Messages.get(this, "ability_no_target"));
			return;
		}

		hero.belongings.abilityWeapon = this;
		if (!hero.canAttack(enemy)) {
			GLog.w(Messages.get(this, "ability_target_range"));
			hero.belongings.abilityWeapon = null;
			return;
		}
		hero.belongings.abilityWeapon = null;

		hero.sprite.attack(enemy.pos, new Callback() {
			@Override
			public void call() {
				beforeAbilityUsed(hero, enemy);
				AttackIndicator.target(enemy);

				boolean hit = hero.attack(enemy, 1.5f, 0, Char.INFINITE_ACCURACY);

				if (hit) {
					Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
					if (!enemy.isAlive()) onAbilityKill(hero, enemy);

					// 2. 召唤 1 个普通矮人
					summonAlly(hero, Random.Float()>0.5f ? Monk.class : Warlock.class,true);

					// 3. 25% + 0.05% 概率额外召唤随机亡灵
					if (Random.Float() < Math.min(1f,0.25f+(level()*0.05f))) {
						summonRandomUndead(hero);
					}
				}

				Invisibility.dispel();
				hero.spendAndNext(hero.attackDelay());
				afterAbilityUsed(hero);
			}
		});
	}

	@Override
	protected int baseChargeUse(Hero hero, Char target) {
		return 2;
	}

	// ========== 召唤盟友（自动系命/生命链接） ==========
	private void summonAlly(Hero hero, Class<? extends Mob> clazz,boolean life) {
		try {
			Mob mob = clazz.newInstance();
			int heroPos = hero.pos;
			ArrayList<Integer> candidates = new ArrayList<>();
			for (int n : PathFinder.NEIGHBOURS8) {
				int checkPos = heroPos + n;
				if (Dungeon.level.passable[checkPos] && Actor.findChar(checkPos) == null) {
					candidates.add(checkPos);
				}
			}
			if (!candidates.isEmpty()) {
				int spawnPos = Random.element(candidates);
				mob.pos = spawnPos;
				Dungeon.level.mobs.add(mob);
				GameScene.add(mob);
				mob.HT = mob.HP = (int) Math.min(Dungeon.depth /5f*10f == 0 ? 1: Dungeon.depth /5f*10f,mob.HT);
				mob.sprite.jump(mob.pos, mob.pos, null);
				if(life){
					Buff.affect(mob, LifeLink.class,100f).object = hero.id();
				}
				Buff.affect(mob, ScrollOfSirensSong.Enthralled.class);
			}
		} catch (Exception ignored) {}
	}

	private static final List<Class<? extends Mob>> UNDEAD_POOL = Arrays.asList(
			Skeleton.class, Thief.class, Bandit.class, Necromancer.class,
			Guard.class, ColdMagicRat.class, RedSwarm.class,
			Monk.class, Warlock.class, WarlockHead.class,
			Wraith.class
	);

	private void summonRandomUndead(Hero hero) {
		if (UNDEAD_POOL.isEmpty()) return;
		Class<? extends Mob> randomUndead = Random.element(UNDEAD_POOL);
		summonAlly(hero, randomUndead,false);
	}

	@Override
	public String abilityInfo() {
		if (levelKnown){
			return Messages.get(this, "typical_ability_desc", 50,Math.min(100,25+level*5));
		} else {
			return Messages.get(this, "ability_desc",50,25);
		}
	}

	public static class RunicSlashTracker extends FlavourBuff {
		public float boost = 1.5f;
	}
}