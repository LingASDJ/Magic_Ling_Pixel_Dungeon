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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RevealedArea;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.NaturesPower;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Blindweed;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Icecap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sorrowmoss;
import com.shatteredpixel.shatteredpixeldungeon.plants.Stormvine;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class SpiritBow extends Weapon {
	
	public static final String AC_SHOOT		= "SHOOT";
	
	{
		image = ItemSpriteSheet.SPIRIT_BOW;
		
		defaultAction = AC_SHOOT;
		usesTargeting = true;
		
		unique = true;
		bones = false;
	}
	
	public boolean sniperSpecial = false;
	public float sniperSpecialBonusDamage = 0f;
	
	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.remove(AC_EQUIP);
		actions.add(AC_SHOOT);
		return actions;
	}
	
	@Override
	public void execute(Hero hero, String action) {
		
		super.execute(hero, action);
		
		if (action.equals(AC_SHOOT)) {
			
			curUser = hero;
			curItem = this;
			GameScene.selectCell( shooter );
			
		}
	}

	private static Class[] harmfulPlants = new Class[]{
			Blindweed.class, Firebloom.class, Icecap.class, Sorrowmoss.class,  Stormvine.class
	};

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		if (attacker.buff(NaturesPower.naturesPowerTracker.class) != null && !sniperSpecial){

			Actor.add(new Actor() {
				{
					actPriority = VFX_PRIO;
				}

				@Override
				protected boolean act() {

					if (Random.Int(12) < ((Hero)attacker).pointsInTalent(Talent.NATURES_WRATH)){
						Plant plant = (Plant) Reflection.newInstance(Random.element(harmfulPlants));
						plant.pos = defender.pos;
						plant.activate( defender.isAlive() ? defender : null );
					}

					if (!defender.isAlive()){
						NaturesPower.naturesPowerTracker tracker = attacker.buff(NaturesPower.naturesPowerTracker.class);
						if (tracker != null){
							tracker.extend(((Hero) attacker).pointsInTalent(Talent.WILD_MOMENTUM));
						}
					}

					Actor.remove(this);
					return true;
				}
			});

		}

		return super.proc(attacker, defender, damage);
	}

	@Override
	public String info() {
		String info = super.info();
		
		info += "\n\n" + Messages.get( SpiritBow.class, "stats",
				Math.round(augment.damageFactor(min())),
				Math.round(augment.damageFactor(max())),
				STRReq());
		
		if (STRReq() > Dungeon.hero.STR()) {
			info += " " + Messages.get(Weapon.class, "too_heavy");
		} else if (Dungeon.hero.STR() > STRReq()){
			info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
		}
		
		switch (augment) {
			case SPEED:
				info += "\n\n" + Messages.get(Weapon.class, "faster");
				break;
			case DAMAGE:
				info += "\n\n" + Messages.get(Weapon.class, "stronger");
				break;
			case NONE:
		}

		if (enchantment != null && (cursedKnown || !enchantment.curse())){
			info += "\n\n" + Messages.capitalize(Messages.get(Weapon.class, "enchanted", enchantment.name()));
			if (enchantHardened) info += " " + Messages.get(Weapon.class, "enchant_hardened");
			info += " " + enchantment.desc();
		} else if (enchantHardened){
			info += "\n\n" + Messages.get(Weapon.class, "hardened_no_enchant");
		}
		
		if (cursed && isEquipped( Dungeon.hero )) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
		} else if (cursedKnown && cursed) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed");
		} else if (!isIdentified() && cursedKnown){
			info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
		}
		
		info += "\n\n" + Messages.get(MissileWeapon.class, "distance");
		
		return info;
	}
	
	@Override
	public int STRReq(int lvl) {
		return STRReq(1, lvl); //tier 1
	}
	
	@Override
	public int min(int lvl) {
		int dmg = 2 + Dungeon.hero.lvl/5
				+ RingOfSharpshooting.levelDamageBonus(Dungeon.hero)
				+ (curseInfusionBonus ? 1 + Dungeon.hero.lvl/30 : 0);
		return Math.max(0, dmg);
	}
	
	@Override
	public int max(int lvl) {
		int dmg = 6 + (int)(Dungeon.hero.lvl/2.5f)
				+ 2*RingOfSharpshooting.levelDamageBonus(Dungeon.hero)
				+ (curseInfusionBonus ? 2 + Dungeon.hero.lvl/15 : 0);
		return Math.max(0, dmg);
	}

	@Override
	public int targetingPos(Hero user, int dst) {
		return knockArrow().targetingPos(user, dst);
	}
	
	private int targetPos;
	
	@Override
	public int damageRoll(Char owner) {
		int damage = augment.damageFactor(super.damageRoll(owner));
		
		if (owner instanceof Hero) {
			int exStr = ((Hero)owner).STR() - STRReq();
			if (exStr > 0) {
				damage += Hero.heroDamageIntRange( 0, exStr );
			}
		}

		if (sniperSpecial){
			damage = Math.round(damage * (1f + sniperSpecialBonusDamage));

			switch (augment){
				case NONE:
					damage = Math.round(damage * 0.667f);
					break;
				case SPEED:
					damage = Math.round(damage * 0.5f);
					break;
				case DAMAGE:
					//as distance increases so does damage, capping at 3x:
					//1.20x|1.35x|1.52x|1.71x|1.92x|2.16x|2.43x|2.74x|3.00x
					int distance = Dungeon.level.distance(owner.pos, targetPos) - 1;
					float multiplier = Math.min(3f, 1.2f * (float)Math.pow(1.125f, distance));
					damage = Math.round(damage * multiplier);
					break;
			}
		}
		
		return damage;
	}
	
	@Override
	protected float baseDelay(Char owner) {
		if (sniperSpecial){
			switch (augment){
				case NONE: default:
					return 0f;
				case SPEED:
					return 1f;
				case DAMAGE:
					return 2f;
			}
		} else{
			return super.baseDelay(owner);
		}
	}

	@Override
	protected float speedMultiplier(Char owner) {
		float speed = super.speedMultiplier(owner);
		if (owner.buff(NaturesPower.naturesPowerTracker.class) != null){
			// +33% speed to +50% speed, depending on talent points
			speed += ((8 + ((Hero)owner).pointsInTalent(Talent.GROWING_POWER)) / 24f);
		}
		return speed;
	}

	@Override
	public int level() {
		int level = Dungeon.hero == null ? 0 : Dungeon.hero.lvl/5;
		if (curseInfusionBonus) level += 1 + level/6;
		return level;
	}

	@Override
	public int buffedLvl() {
		//level isn't affected by buffs/debuffs
		return level();
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	public SpiritArrow knockArrow(){
		return new SpiritArrow();
	}
	
	public class SpiritArrow extends MissileWeapon {
		
		{
			image = ItemSpriteSheet.SPIRIT_ARROW;

			hitSound = Assets.Sounds.HIT_ARROW;
		}

		@Override
		public Emitter emitter() {
			if (Dungeon.hero.buff(NaturesPower.naturesPowerTracker.class) != null && !sniperSpecial){
				Emitter e = new Emitter();
				e.pos(5, 5);
				e.fillTarget = false;
				e.pour(LeafParticle.GENERAL, 0.01f);
				return e;
			} else {
				return super.emitter();
			}
		}

		@Override
		public int damageRoll(Char owner) {
			return SpiritBow.this.damageRoll(owner);
		}
		
		@Override
		public boolean hasEnchant(Class<? extends Enchantment> type, Char owner) {
			return SpiritBow.this.hasEnchant(type, owner);
		}
		
		@Override
		public int proc(Char attacker, Char defender, int damage) {
			return SpiritBow.this.proc(attacker, defender, damage);
		}
		
		@Override
		public float delayFactor(Char user) {
			return SpiritBow.this.delayFactor(user);
		}
		
		@Override
		public float accuracyFactor(Char owner, Char target) {
			float accFactor = super.accuracyFactor(owner, target);
			float distanceAccMultiplier = 1.0f + (0.1f * distanceAccBonus);
			accFactor *= monocularAccBonus * distanceAccMultiplier;

			if (sniperSpecial && SpiritBow.this.augment == Augment.DAMAGE){
				return Float.POSITIVE_INFINITY + accFactor;
			} else {
				return super.accuracyFactor(owner, target) + accFactor;
			}
		}
		
		@Override
		public int STRReq(int lvl) {
			return SpiritBow.this.STRReq();
		}

		@Override
		protected void onThrow( int cell ) {
			Char enemy = Actor.findChar( cell );
			if (enemy == null || enemy == curUser) {
				parent = null;
				Splash.at( cell, 0xCC99FFFF, 1 );
			} else {
				if (!curUser.shoot( enemy, this )) {
					Splash.at(cell, 0xCC99FFFF, 1);
				}
				if (sniperSpecial && SpiritBow.this.augment != Augment.SPEED) sniperSpecial = false;
			}
		}

		@Override
		public void throwSound() {
			Sample.INSTANCE.play( Assets.Sounds.ATK_SPIRITBOW, 1, Random.Float(0.87f, 1.15f) );
		}

		int flurryCount = -1;
		Actor flurryActor = null;

		@Override
		public void cast(final Hero user, final int dst) {
			final int cell = throwPos( user, dst );
			SpiritBow.this.targetPos = cell;
			if (sniperSpecial && SpiritBow.this.augment == Augment.SPEED){
				if (flurryCount == -1) flurryCount = 3;
				
				final Char enemy = Actor.findChar( cell );
				
				if (enemy == null){
					if (user.buff(Talent.LethalMomentumTracker.class) != null){
						user.buff(Talent.LethalMomentumTracker.class).detach();
						user.next();
					} else {
						user.spendAndNext(castDelay(user, dst));
					}
					sniperSpecial = false;
					flurryCount = -1;

					if (flurryActor != null){
						flurryActor.next();
						flurryActor = null;
					}
					return;
				}

				QuickSlotButton.target(enemy);
				
				user.busy();
				
				throwSound();

				user.sprite.zap(cell);
				((MissileSprite) user.sprite.parent.recycle(MissileSprite.class)).
						reset(user.sprite,
								cell,
								this,
								new Callback() {
									@Override
									public void call() {
										if (enemy.isAlive()) {
											curUser = user;
											onThrow(cell);
										}

										flurryCount--;
										if (flurryCount > 0){
											Actor.add(new Actor() {

												{
													actPriority = VFX_PRIO-1;
												}

												@Override
												protected boolean act() {
													flurryActor = this;
													int target = QuickSlotButton.autoAim(enemy, SpiritArrow.this);
													if (target == -1) target = cell;
													cast(user, target);
													Actor.remove(this);
													return false;
												}
											});
											curUser.next();
										} else {
											if (user.buff(Talent.LethalMomentumTracker.class) != null){
												user.buff(Talent.LethalMomentumTracker.class).detach();
												user.next();
											} else {
												user.spendAndNext(castDelay(user, dst));
											}
											sniperSpecial = false;
											flurryCount = -1;
										}

										if (flurryActor != null){
											flurryActor.next();
											flurryActor = null;
										}
									}
								});
				
			} else {

				if (user.hasTalent(Talent.SEER_SHOT)
						&& user.buff(Talent.SeerShotCooldown.class) == null){
					int shotPos = throwPos(user, dst);
					if (Actor.findChar(shotPos) == null) {
						RevealedArea a = Buff.affect(user, RevealedArea.class, 5 * user.pointsInTalent(Talent.SEER_SHOT));
						a.depth = Dungeon.depth;
						a.branch = Dungeon.branch;
						a.pos = shotPos;
						Buff.affect(
								user,
								Talent.SeerShotCooldown.class,
								Talent.SeerShotCooldown.duration(user)
						);
					}
				}

				super.cast(user, dst);
			}

			if (user.subClass == HeroSubClass.WARDEN) {
				tryGrowingShot(user, dst);
			}
		}
		/**
		 * 尝试触发“生息之矢”。
		 * 1点：在落点生成1格草。
		 * 2点：在落点左右生成3格横排草。
		 * 3点：在落点生成5格十字形草。
		 * 箭矢必须落在没有角色的可生草地面。
		 * 只有实际处理了地面之后，才会进入冷却。
		 */
		private void tryGrowingShot(Hero user, int dst) {
			int points = user.pointsInTalent(Talent.GROWING_SHOT);
			// 未学习天赋或仍在冷却中时，不触发效果
			if (points <= 0
					|| user.buff(Talent.GrowingShotCooldown.class) != null) {
				return;
			}
			// 把玩家选择的目标转换为箭矢的实际落点，
			int shotPos = throwPos(user, dst);
			// 落点本身必须是能生草的空地面。
			if (!canGrowGrassAt(shotPos)) {
				return;
			}
			// RejuvenatingStepsFurrow 是原版“复春步伐”使用的经验追踪器：
			// 生成高草时增加计数，获得经验时减少计数
			Talent.RejuvenatingStepsFurrow furrow =
					user.buff(Talent.RejuvenatingStepsFurrow.class);

			boolean highGrass = Regeneration.regenOn()
					&& (furrow == null || furrow.count() < 200);

			ArrayList<Integer> cells = getGrowingShotCells(shotPos, points);

			int grownCells = 0;
			int highGrassCells = 0;

			for (int cell : cells) {
				// 周围格子可能有墙、角色或不适合生草的地形，
				// 这类格子直接跳过，不会强行覆盖。
				if (!canGrowGrassAt(cell)) {
					continue;
				}

				Level.set(cell, highGrass
								? Terrain.HIGH_GRASS
								: Terrain.FURROWED_GRASS
				);

				GameScene.updateMap(cell);

				grownCells++;
				if (highGrass) {
					highGrassCells++;
				}
			}
				// 落点已经验证过，理论上至少会生成1格。
				if (grownCells == 0) {
					return;
				}

			// 生成正常高草时累计“长时间未获得经验”的计数器。
			if (highGrass) {
				Buff.count(
						user,
						Talent.RejuvenatingStepsFurrow.class,
						highGrassCells
								* (3 - user.pointsInTalent(Talent.REJUVENATING_STEPS))
				);
			}

			// 成功在地面生成草后开始冷却
			// 1点：25回合；2点：20回合；3点：15回合
			Buff.affect(
					user,
					Talent.GrowingShotCooldown.class,
					30f - 5f * points
			);
		}
		/**
		 * 根据天赋点数返回本次要处理的地图格。
		 * points = 1：[中心]
		 * points = 2：[左、中心、右]
		 * points = 3：[上、左、中心、右、下]
		 */
		private ArrayList<Integer> getGrowingShotCells(int center, int points) {

			ArrayList<Integer> cells = new ArrayList<>();
			cells.add(center);

			int width = Dungeon.level.width();

			if (points >= 2) {
				// 横排左右两格。
				// 如果中心格位于地图最左或最右列，则对应一侧跳过。
				if (center % width != 0) {
					cells.add(center - 1);
				}

				if ((center + 1) % width != 0) {
					cells.add(center + 1);
				}
			}

			if (points >= 3) {
				// 上下两格组成十字形。
				if (Dungeon.level.insideMap(center - width)) {
					cells.add(center - width);
				}

				if (Dungeon.level.insideMap(center + width)) {
					cells.add(center + width);
				}
			}

			return cells;
		}
		/**
		 * 判断指定格子是否允许生息之矢生成草。
		 * 这里只接受原本就可以承载草或改变地形的地面，
		 * 避免把水面、熔岩、门、墙等地形转换成草地。
		 */
		private boolean canGrowGrassAt(int cell) {

			if (!Dungeon.level.insideMap(cell)
					|| Actor.findChar(cell) != null) {
				return false;
			}

			int terrain = Dungeon.level.map[cell];

			return terrain == Terrain.EMPTY
					|| terrain == Terrain.EMPTY_DECO
					|| terrain == Terrain.EMBERS
					|| terrain == Terrain.HIGH_GRASS
					|| terrain == Terrain.FURROWED_GRASS;
		}
	}
	
	private CellSelector.Listener shooter = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer target ) {
			if (target != null) {
				knockArrow().cast(curUser, target);
			}
		}
		@Override
		public String prompt() {
			return Messages.get(SpiritBow.class, "prompt");
		}
	};
}
