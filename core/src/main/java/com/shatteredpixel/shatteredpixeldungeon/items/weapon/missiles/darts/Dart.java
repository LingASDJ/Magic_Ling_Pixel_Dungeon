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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Crossbow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.DiedCrossBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.ForestBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Dart extends MissileWeapon {

	{
		image = ItemSpriteSheet.DART;
		hitSound = Assets.Sounds.HIT_ARROW;
		hitSoundPitch = 1.3f;
		
		tier = 1;
		
		//infinite, even with penalties
		baseUses = 1000;
	}

	@Override
	public Emitter emitter() {
		if(Dungeon.hero != null){
			if (Dungeon.hero.buff(ForestBow.ChargedShot.class) != null){
				Emitter e = new Emitter();
				e.pos(5, 5);
				e.fillTarget = false;
				e.pour(LeafParticle.GENERAL, 0.05f);
				return e;
			}
		}
		return super.emitter();
    }

	
	protected static final String AC_TIP = "TIP";
	
	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_TIP );
		return actions;
	}
	
	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);
		if (action.equals(AC_TIP)){
			GameScene.selectItem(itemSelector);
		}
	}
	
	@Override
	public int min(int lvl) {
		if (bow != null){
			if(Dungeon.hero != null){
				if (!(this instanceof TippedDart) && Dungeon.hero.buff(Crossbow.ChargedShot.class) != null){
					//ability increases base dmg by 37.5%, scaling by 50%
					return  7 +                     //7 base
							2*bow.buffedLvl() + lvl;//+2 per bow level, +1 per level
				} else {
					return  4 +                     //4 base
							bow.buffedLvl() + lvl;  //+1 per level or bow level
				}
			} else {
				return  4 + lvl;  //+1 per level or bow level
			}
		}
		if(diedCrossBow != null ){
			if(Dungeon.hero != null) {
				if (!(this instanceof TippedDart) && Dungeon.hero.buff(Crossbow.ChargedShot.class) != null) {
					//ability increases base dmg by 37.5%, scaling by 50%
					return 7 +                     //7 base
							2 * diedCrossBow.buffedLvl() + lvl;//+2 per bow level, +1 per level
				} else {
					return 4 +                     //4 base
							diedCrossBow.buffedLvl() + lvl;
				}
			} else {
				return 4 + lvl;
			}
		}

		if(forestBow != null ){
			int orgrinDamage = 3 + forestBow.level();
			if(Dungeon.hero != null) {
				if (!(this instanceof TippedDart) && Dungeon.hero.buff(ForestBow.ChargedShot.class) != null) {
					return (int) (orgrinDamage * 0.9f);
				} else {
					return  orgrinDamage;
				}
			} else {
				return 4 + lvl;
			}
		}
		return  1 + lvl;
	}

	@Override
	public int max(int lvl) {
		if (bow != null){
			if (!(this instanceof TippedDart) && Dungeon.hero.buff(Crossbow.ChargedShot.class) != null){
				//ability increases base dmg by 37.5%, scaling by 50%
				return  15 +                       //15 base
						4*bow.buffedLvl() +
						2*lvl; //+4 per bow level, +2 per level
			} else {
				return  12 +                       //12 base
						3*bow.buffedLvl() +
						2*lvl; //+3 per bow level, +2 per level
			}
		}
		if(diedCrossBow != null){
			if (!(this instanceof TippedDart) && Dungeon.hero.buff(Crossbow.ChargedShot.class) != null){
				//ability increases base dmg by 37.5%, scaling by 50%
				return  15 +                       //15 base
						4*diedCrossBow.buffedLvl() +
						2*lvl; //+4 per bow level, +2 per level
			} else {
				return  12 +                       //12 base
						3*diedCrossBow.buffedLvl() +
						2*lvl; //+3 per bow level, +2 per level
			}
		}

		if(forestBow != null ){
			int orgrinDamage = (int) (6 + forestBow.level() * 2.5f);
			if(Dungeon.hero != null) {
				if (!(this instanceof TippedDart) && Dungeon.hero.buff(ForestBow.ChargedShot.class) != null) {
					return (int) (orgrinDamage * 0.9f);
				} else {
					return  orgrinDamage;
				}
			} else {
				return 4 + lvl;
			}
		}

		return  2 + 2*lvl;

	}

	public static Crossbow bow;

	public static ForestBow forestBow;

	public static DiedCrossBow diedCrossBow;
	
	private void updateCrossbow(){
		if (Dungeon.hero == null) {
			bow = null;
			diedCrossBow = null;
			forestBow = null;
		} else if (Dungeon.hero.belongings.weapon() instanceof Crossbow){
			bow = (Crossbow) Dungeon.hero.belongings.weapon();
		} else if (Dungeon.hero.belongings.secondWep() instanceof Crossbow) {
			bow = (Crossbow) Dungeon.hero.belongings.secondWep();
		} else if(Dungeon.hero.belongings.weapon() instanceof DiedCrossBow){
			diedCrossBow = (DiedCrossBow) Dungeon.hero.belongings.weapon();
		} else if(Dungeon.hero.belongings.secondWep() instanceof DiedCrossBow){
			diedCrossBow = (DiedCrossBow) Dungeon.hero.belongings.weapon();
		} else if(Dungeon.hero.belongings.weapon() instanceof ForestBow){
			forestBow = (ForestBow) Dungeon.hero.belongings.weapon();
		} else if(Dungeon.hero.belongings.secondWep() instanceof ForestBow){
			forestBow = (ForestBow) Dungeon.hero.belongings.weapon();
		} else {
			bow = null;
			diedCrossBow = null;
			forestBow = null;
		}
	}

	public boolean crossbowHasEnchant( Char owner ){
		return (bow != null && bow.enchantment != null && owner.buff(MagicImmune.class) == null) || ( diedCrossBow != null && diedCrossBow.enchantment != null && owner.buff(MagicImmune.class) == null) || (forestBow != null && forestBow.enchantment != null && owner.buff(MagicImmune.class) == null);
	}
	
	@Override
	public boolean hasEnchant(Class<? extends Enchantment> type, Char owner) {
		if (bow != null && bow.hasEnchant(type, owner)){
			return true;
		} else if(diedCrossBow != null && diedCrossBow.hasEnchant(type, owner)) {
			return true;
		} else if(forestBow != null && forestBow.hasEnchant(type, owner)) {
			return true;
		} else{
			return super.hasEnchant(type, owner);
		}
	}

	@Override
	public float accuracyFactor(Char owner, Char target) {
		//don't update xbow here, as dart is the active weapon atm
		if (forestBow != null && owner.buff(ForestBow.ChargedShot.class) != null) {
			return Char.INFINITE_ACCURACY;
		} else if (bow != null && owner.buff(Crossbow.ChargedShot.class) != null){
			return Char.INFINITE_ACCURACY;
		} else {
			return super.accuracyFactor(owner, target);
		}
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		if (bow != null && !processingChargedShot){
			damage = bow.proc(attacker, defender, damage);
		}

		if (diedCrossBow != null && !processingChargedShot){
			damage = diedCrossBow.proc(attacker, defender, damage);
		}

		if(forestBow != null && !processingChargedShot){
			damage = forestBow.proc(attacker, defender, damage);

			// ForestBow 特殊效果
			int triggerChance = 15 + 4 * level();
			if (Random.Int(100) < triggerChance) {
				// 计算效果持续时间
				int duration = 3 + (level() / 4);
				int effectCount = (level() >= 9) ? 2 : 1; // +9以上时施加两种效果

				// 可选效果列表
				Class<?>[] effects = {
						Poison.class,
						Blindness.class,
						Frost.class
				};

				// 随机选择效果
				ArrayList<Class<?>> selectedEffects = new ArrayList<>();
				for (int i = 0; i < effectCount; i++) {
					Class<?> effect = Random.element(effects);
					selectedEffects.add(effect);
				}

				// 应用选中的效果
				for (Class<?> effect : selectedEffects) {
					if (effect == Poison.class) {
						Buff.affect(defender, Poison.class).set(duration);
					} else if (effect == Blindness.class) {
						Buff.prolong(defender, Blindness.class, duration);
					} else if (effect == Frost.class) {
						Buff.prolong(defender, Frost.class, duration);
					}
				}
			}
		}

		int dmg = super.proc(attacker, defender, damage);
		if (!processingChargedShot) {
			processChargedShot(defender, damage);
		}
		return dmg;
	}

	@Override
	public int throwPos(Hero user, int dst) {
		updateCrossbow();
		return super.throwPos(user, dst);
	}

	@Override
	protected void onThrow(int cell) {
		updateCrossbow();
		//we have to set this here, as on-hit effects can move the target we aim at
		chargedShotPos = cell;
		super.onThrow(cell);
	}

	protected boolean processingChargedShot = false;
	private int chargedShotPos;
	protected void processChargedShot( Char target, int dmg ){
		//don't update xbow here, as dart may be the active weapon atm
		processingChargedShot = true;

		if (chargedShotPos != -1) {
			if( bow != null  && (Dungeon.hero.buff(Crossbow.ChargedShot.class) != null )){
				PathFinder.buildDistanceMap(chargedShotPos, Dungeon.level.passable, 2);
				//necessary to clone as some on-hit effects use Pathfinder
				int[] distance = PathFinder.distance.clone();
				for (Char ch : Actor.chars()){
					if (ch == target){
						Actor.add(new Actor() {
							{ actPriority = VFX_PRIO; }
							@Override
							protected boolean act() {
								if (!ch.isAlive()){
									bow.onAbilityKill(Dungeon.hero, ch);
								}
								Actor.remove(this);
								return true;
							}
						});
					} else if (distance[ch.pos] != Integer.MAX_VALUE){
						proc(Dungeon.hero, ch, dmg);
					}
				}
			}
			} else if ( forestBow != null && (Dungeon.hero.buff(ForestBow.ChargedShot.class) != null )) {
			PathFinder.buildDistanceMap(chargedShotPos, Dungeon.level.passable, 2);
			//necessary to clone as some on-hit effects use Pathfinder
			int[] distance = PathFinder.distance.clone();
			for (Char ch : Actor.chars()){
				if (ch == target){
					Actor.add(new Actor() {
						{ actPriority = VFX_PRIO; }
						@Override
						protected boolean act() {
							if (!ch.isAlive()){
								forestBow.onAbilityKill(Dungeon.hero, ch);
							}
							Actor.remove(this);
							return true;
						}
					});
				} else if (distance[ch.pos] != Integer.MAX_VALUE){
					proc(Dungeon.hero, ch, dmg);
				}
			}
		}
		chargedShotPos = -1;
		processingChargedShot = false;
	}

	@Override
	protected void decrementDurability() {
		super.decrementDurability();
		if (Dungeon.hero.buff(Crossbow.ChargedShot.class) != null) {
			Dungeon.hero.buff(Crossbow.ChargedShot.class).detach();
		}
		if (Dungeon.hero.buff(ForestBow.ChargedShot.class) != null) {
			Dungeon.hero.buff(ForestBow.ChargedShot.class).detach();
		}
	}

	@Override
	public void throwSound() {
		updateCrossbow();
		if (bow != null || forestBow != null) {
			Sample.INSTANCE.play(Assets.Sounds.ATK_CROSSBOW, 1, Random.Float(0.87f, 1.15f));
		} else {
			super.throwSound();
		}
	}
	
	@Override
	public String info() {
		updateCrossbow();
		if (bow != null && !bow.isIdentified()){
			int level = bow.level();
			//temporarily sets the level of the bow to 0 for IDing purposes
			bow.level(0);
			String info = super.info();
			bow.level(level);
			return info;
		} else if (forestBow != null && !forestBow.isIdentified()){
			int level = forestBow.level();
			//temporarily sets the level of the bow to 0 for IDing purposes
			forestBow.level(0);
			String info = super.info();
			forestBow.level(level);
			return info;
		} else if (diedCrossBow != null && !diedCrossBow.isIdentified()){
			int level = diedCrossBow.level();
			//temporarily sets the level of the bow to 0 for IDing purposes
			diedCrossBow.level(0);
			String info = super.info();
			diedCrossBow.level(level);
			return info;
		}else {
			return super.info();
		}
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public int value() {
		return super.value()/2; //half normal value
	}
	
	private final WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return Messages.get(Dart.class, "prompt");
		}

		@Override
		public Class<?extends Bag> preferredBag(){
			return VelvetPouch.class;
		}

		@Override
		public boolean itemSelectable(Item item) {
			return item instanceof Plant.Seed;
		}

		@Override
		public void onSelect(final Item item) {
			
			if (item == null) return;
			
			final int maxToTip = Math.min(curItem.quantity(), item.quantity()*2);
			final int maxSeedsToUse = (maxToTip+1)/2;
			
			final int singleSeedDarts;
			
			final String[] options;
			
			if (curItem.quantity() == 1){
				singleSeedDarts = 1;
				options = new String[]{
						Messages.get(Dart.class, "tip_one"),
						Messages.get(Dart.class, "tip_cancel")};
			} else {
				singleSeedDarts = 2;
				if (maxToTip <= 2){
					options = new String[]{
							Messages.get(Dart.class, "tip_two"),
							Messages.get(Dart.class, "tip_cancel")};
				} else {
					options = new String[]{
							Messages.get(Dart.class, "tip_all", maxToTip, maxSeedsToUse),
							Messages.get(Dart.class, "tip_two"),
							Messages.get(Dart.class, "tip_cancel")};
				}
			}
			
			TippedDart tipResult = TippedDart.getTipped((Plant.Seed) item, 1);
			
			GameScene.show(new WndOptions( new ItemSprite(item),
					Messages.titleCase(item.name()),
					Messages.get(Dart.class, "tip_desc", tipResult.name()) + "\n\n" + tipResult.desc(),
					options){
				
				@Override
				protected void onSelect(int index) {
					super.onSelect(index);
					
					if (index == 0 && options.length == 3){
						if (item.quantity() <= maxSeedsToUse){
							item.detachAll( curUser.belongings.backpack );
						} else {
							item.quantity(item.quantity() - maxSeedsToUse);
						}
						
						if (maxToTip < curItem.quantity()){
							curItem.quantity(curItem.quantity() - maxToTip);
						} else {
							curItem.detachAll(curUser.belongings.backpack);
						}
						
						TippedDart newDart = TippedDart.getTipped((Plant.Seed) item, maxToTip);
						if (!newDart.collect()) Dungeon.level.drop(newDart, curUser.pos).sprite.drop();
						
						curUser.spend( 1f );
						curUser.busy();
						curUser.sprite.operate(curUser.pos);

					} else if ((index == 1 && options.length == 3) || (index == 0 && options.length == 2)){
						item.detach( curUser.belongings.backpack );
						
						if (curItem.quantity() <= singleSeedDarts){
							curItem.detachAll( curUser.belongings.backpack );
						} else {
							curItem.quantity(curItem.quantity() - singleSeedDarts);
						}
						
						TippedDart newDart = TippedDart.getTipped((Plant.Seed) item, singleSeedDarts);
						if (!newDart.collect()) Dungeon.level.drop(newDart, curUser.pos).sprite.drop();
						
						curUser.spend( 1f );
						curUser.busy();
						curUser.sprite.operate(curUser.pos);
					}
				}
			});
			
		}
		
	};
}
/*

 */