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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.HalomethaneFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StormCloud;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.DM275;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.GnollHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.GreenSlting;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.SuccubusQueen;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sickle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GeyserTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FistSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class YogFist extends Mob {

	{
		HP = HT = 300;
		defenseSkill = 20;

		viewDistance = Light.DISTANCE;

		//for doomed resistance
		EXP = 25;
		maxLvl = -2;

		state = HUNTING;

		properties.add(Property.BOSS);
		properties.add(Property.DEMONIC);
	}

	private float rangedCooldown;
	protected boolean canRangedInMelee = true;

	protected void incrementRangedCooldown(){
		rangedCooldown += Random.NormalFloat(8, 12);
	}

	@Override
	protected boolean act() {
		if (paralysed <= 0 && rangedCooldown > 0) rangedCooldown--;

		if (Dungeon.hero.invisible <= 0 && state == WANDERING){
			beckon(Dungeon.hero.pos);
			state = HUNTING;
			enemy = Dungeon.hero;
		}

		return super.act();
	}

	@Override
	protected boolean canAttack(Char enemy) {
		if (rangedCooldown <= 0){
			return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
		} else {
			return super.canAttack(enemy);
		}
	}

	private boolean invulnWarned = false;

	protected boolean isNearYog(){
		int yogPos = Dungeon.level.exit() + 3*Dungeon.level.width();
		return Dungeon.level.distance(pos, yogPos) <= 4;
	}

	@Override
	public boolean isInvulnerable(Class effect) {
		if (isNearYog() && !invulnWarned){
			invulnWarned = true;
			GLog.w(Messages.get(this, "invuln_warn"));
		}
		return isNearYog() || super.isInvulnerable(effect);
	}

	@Override
	protected boolean doAttack( Char enemy ) {

		if (Dungeon.level.adjacent( pos, enemy.pos ) && (!canRangedInMelee || rangedCooldown > 0)) {

			return super.doAttack( enemy );

		} else {

			incrementRangedCooldown();
			if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
				sprite.zap( enemy.pos );
				return false;
			} else {
				zap();
				return true;
			}
		}
	}

	@Override
	public void damage(int dmg, Object src) {
		int preHP = HP;
		super.damage(dmg, src);
		int dmgTaken = HP - preHP;

		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if (dmgTaken > 0 && lock != null && !isImmune(src.getClass()) && !isInvulnerable(src.getClass())){
			if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES))   lock.addTime(dmgTaken/4f);
			else                                                    lock.addTime(dmgTaken/2f);
		}
	}

	protected abstract void zap();

	public void onZapComplete(){
		zap();
		next();
	}

	@Override
	public int attackSkill( Char target ) {
		return 36;
	}

	@Override
	public int damageRoll() {
		return Char.combatRoll( 18, 36 );
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Char.combatRoll(0, 15);
	}

	{
		immunities.add( Sleep.class );
	}

	@Override
	public String description() {
		return Messages.get(YogFist.class, "desc") + "\n\n" + Messages.get(this, "desc");
	}

	public static final String RANGED_COOLDOWN = "ranged_cooldown";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(RANGED_COOLDOWN, rangedCooldown);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		rangedCooldown = bundle.getFloat(RANGED_COOLDOWN);
	}

	public static class BurningFist extends YogFist {

		{
			spriteClass = FistSprite.Burning.class;

			properties.add(Property.FIERY);
		}

		@Override
		public boolean act() {

			boolean result = super.act();

			if (Dungeon.level.map[pos] == Terrain.WATER){
				Level.set( pos, Terrain.EMPTY);
				GameScene.updateMap( pos );
				CellEmitter.get( pos ).burst( Speck.factory( Speck.STEAM ), 10 );
			}

			//1.67 evaporated tiles on average
			int evaporatedTiles = Random.chances(new float[]{0, 1, 2});

			for (int i = 0; i < evaporatedTiles; i++) {
				int cell = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
				if (Dungeon.level.map[cell] == Terrain.WATER){
					Level.set( cell, Terrain.EMPTY);
					GameScene.updateMap( cell );
					CellEmitter.get( cell ).burst( Speck.factory( Speck.STEAM ), 10 );
				}
			}

			for (int i : PathFinder.NEIGHBOURS9) {
				int vol = Fire.volumeAt(pos+i, Fire.class);
				if (vol < 4 && !Dungeon.level.water[pos + i] && !Dungeon.level.solid[pos + i]){
					GameScene.add( Blob.seed( pos + i, 4 - vol, Fire.class ) );
				}
			}

			return result;
		}

		@Override
		protected void zap() {
			spend( 1f );

			if (Dungeon.level.map[enemy.pos] == Terrain.WATER){
				Level.set( enemy.pos, Terrain.EMPTY);
				GameScene.updateMap( enemy.pos );
				CellEmitter.get( enemy.pos ).burst( Speck.factory( Speck.STEAM ), 10 );
			} else {
				Buff.affect( enemy, Burning.class ).reignite( enemy );
			}

			for (int i : PathFinder.NEIGHBOURS9){
				if (!Dungeon.level.water[enemy.pos+i] && !Dungeon.level.solid[enemy.pos+i]){
					int vol = Fire.volumeAt(enemy.pos+i, Fire.class);
					if (vol < 4){
						GameScene.add( Blob.seed( enemy.pos + i, 4 - vol, Fire.class ) );
					}
				}
			}

		}

		{
			immunities.add(Frost.class);

			resistances.add(StormCloud.class);
			resistances.add(GeyserTrap.class);
		}

	}

	public static class SoiledFist extends YogFist {

		{
			spriteClass = FistSprite.Soiled.class;
		}

		@Override
		public boolean act() {

			boolean result = super.act();

			//1.33 grass tiles on average
			int furrowedTiles = Random.chances(new float[]{0, 2, 1});

			for (int i = 0; i < furrowedTiles; i++) {
				int cell = pos + PathFinder.NEIGHBOURS9[Random.Int(9)];
				if (Dungeon.level.map[cell] == Terrain.GRASS) {
					Level.set(cell, Terrain.FURROWED_GRASS);
					GameScene.updateMap(cell);
					CellEmitter.get(cell).burst(LeafParticle.GENERAL, 10);
				}
			}

			Dungeon.observe();

			for (int i : PathFinder.NEIGHBOURS9) {
				int cell = pos + i;
				if (canSpreadGrass(cell)){
					Level.set(pos+i, Terrain.GRASS);
					GameScene.updateMap( pos + i );
				}
			}

			return result;
		}

		@Override
		public void damage(int dmg, Object src) {
			int grassCells = 0;
			for (int i : PathFinder.NEIGHBOURS9) {
				if (Dungeon.level.map[pos+i] == Terrain.FURROWED_GRASS
						|| Dungeon.level.map[pos+i] == Terrain.HIGH_GRASS){
					grassCells++;
				}
			}
			if (grassCells > 0) dmg = Math.round(dmg * (6-grassCells)/6f);

			//can be ignited, but takes no damage from burning
			if (src.getClass() == Burning.class){
				return;
			}

			super.damage(dmg, src);
		}

		@Override
		protected void zap() {
			spend( 1f );

			Invisibility.dispel(this);
			Char enemy = this.enemy;
			if (hit( this, enemy, true )) {

				Buff.affect( enemy, Roots.class, 3f );

			} else {

				enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
			}

			for (int i : PathFinder.NEIGHBOURS9){
				int cell = enemy.pos + i;
				if (canSpreadGrass(cell)){
					if (Random.Int(5) == 0){
						Level.set(cell, Terrain.FURROWED_GRASS);
						GameScene.updateMap( cell );
					} else {
						Level.set(cell, Terrain.GRASS);
						GameScene.updateMap( cell );
					}
					CellEmitter.get( cell ).burst( LeafParticle.GENERAL, 10 );
				}
			}
			Dungeon.observe();

		}

		private boolean canSpreadGrass(int cell){
			int yogPos = Dungeon.level.exit() + Dungeon.level.width()*3;
			return Dungeon.level.distance(cell, yogPos) > 4 && !Dungeon.level.solid[cell]
					&& !(Dungeon.level.map[cell] == Terrain.FURROWED_GRASS || Dungeon.level.map[cell] == Terrain.HIGH_GRASS);
		}

	}

	public static class RottingFist extends YogFist {

		{
			spriteClass = FistSprite.Rotting.class;

			properties.add(Property.ACIDIC);
		}

		@Override
		protected boolean act() {
			//ensures toxic gas acts at the appropriate time when added
			GameScene.add(Blob.seed(pos, 0, ToxicGas.class));

			if (Dungeon.level.water[pos] && HP < HT) {
				sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(HT/50), FloatingText.HEALING);
				HP = Math.min(HT, HP + HT/50);
			}

			return super.act();
		}

		@Override
		public void damage(int dmg, Object src) {
			if (!isInvulnerable(src.getClass())
					&& !(src instanceof Bleeding)
					&& buff(Sickle.HarvestBleedTracker.class) == null){
				dmg = Math.round( dmg * resist( src.getClass() ));
				if (dmg < 0){
					return;
				}
				Bleeding b = buff(Bleeding.class);
				if (b == null){
					b = new Bleeding();
				}
				b.announced = false;
				b.set(dmg*.6f);
				b.attachTo(this);
				sprite.showStatus(CharSprite.WARNING, Messages.titleCase(b.name()) + " " + (int)b.level());
			} else{
				super.damage(dmg, src);
			}
		}

		@Override
		protected void zap() {
			spend( 1f );
			GameScene.add(Blob.seed(enemy.pos, 100, ToxicGas.class));
		}

		@Override
		public int attackProc( Char enemy, int damage ) {
			damage = super.attackProc( enemy, damage );

			if (Random.Int( 2 ) == 0) {
				Buff.affect( enemy, Ooze.class ).set( Ooze.DURATION );
				enemy.sprite.burst( 0xFF000000, 5 );
			}

			return damage;
		}

		{
			immunities.add(ToxicGas.class);
		}

	}

	public static class RustedFist extends YogFist {

		{
			spriteClass = FistSprite.Rusted.class;

			properties.add(Property.LARGE);
			properties.add(Property.INORGANIC);
		}

		@Override
		public int damageRoll() {
			return Char.combatRoll( 22, 44 );
		}

		@Override
		public void damage(int dmg, Object src) {
			if (!isInvulnerable(src.getClass()) && !(src instanceof Viscosity.DeferedDamage)){
				dmg = Math.round( dmg * resist( src.getClass() ));
				if (dmg >= 0) {
					Buff.affect(this, Viscosity.DeferedDamage.class).prolong(dmg);
					sprite.showStatus(CharSprite.WARNING, Messages.get(Viscosity.class, "deferred", dmg));
				}
			} else{
				super.damage(dmg, src);
			}
		}

		@Override
		protected void zap() {
			spend( 1f );
			Buff.affect(enemy, Cripple.class, 4f);
		}

	}

	public static class BrightFist extends YogFist {

		{
			spriteClass = FistSprite.Bright.class;

			properties.add(Property.ELECTRIC);

			canRangedInMelee = false;
		}

		@Override
		protected void incrementRangedCooldown() {
			//ranged attack has no cooldown
		}

		//used so resistances can differentiate between melee and magical attacks
		public static class LightBeam{}

		@Override
		protected void zap() {
			spend( 1f );

			Invisibility.dispel(this);
			Char enemy = this.enemy;
			if (hit( this, enemy, true )) {

				enemy.damage( Char.combatRoll(10, 20), new LightBeam() );
				Buff.prolong( enemy, Blindness.class, Blindness.DURATION/2f );

				if (!enemy.isAlive() && enemy == Dungeon.hero) {
					Badges.validateDeathFromEnemyMagic();
					Dungeon.fail( this );
					GLog.n( Messages.get(Char.class, "kill", name()) );
				}

			} else {

				enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
			}

		}

		@Override
		public void damage(int dmg, Object src) {
			int beforeHP = HP;
			super.damage(dmg, src);
			if (isAlive() && beforeHP > HT/2 && HP < HT/2){
				HP = HT/2;
				Buff.prolong( Dungeon.hero, Blindness.class, Blindness.DURATION*1.5f );
				int i;
				do {
					i = Random.Int(Dungeon.level.length());
				} while (Dungeon.level.heroFOV[i]
						|| Dungeon.level.solid[i]
						|| Actor.findChar(i) != null
						|| PathFinder.getStep(i, Dungeon.level.exit(), Dungeon.level.passable) == -1);
				ScrollOfTeleportation.appear(this, i);
				state = WANDERING;
				GameScene.flash(0x80FFFFFF);
				GLog.w( Messages.get( this, "teleport" ));
			} else if (!isAlive()){
				Buff.prolong( Dungeon.hero, Blindness.class, Blindness.DURATION*3f );
				GameScene.flash(0x80FFFFFF);
			}
		}

	}

	public static class DarkFist extends YogFist {

		{
			spriteClass = FistSprite.Dark.class;

			canRangedInMelee = false;
		}

		@Override
		protected void incrementRangedCooldown() {
			//ranged attack has no cooldown
		}

		//used so resistances can differentiate between melee and magical attacks
		public static class DarkBolt{}

		@Override
		protected void zap() {
			spend( 1f );

			Invisibility.dispel(this);
			Char enemy = this.enemy;
			if (hit( this, enemy, true )) {

				enemy.damage( Char.combatRoll(10, 20), new DarkBolt() );

				Light l = enemy.buff(Light.class);
				if (l != null){
					l.weaken(50);
				}

				if (!enemy.isAlive() && enemy == Dungeon.hero) {
					Badges.validateDeathFromEnemyMagic();
					Dungeon.fail( this );
					GLog.n( Messages.get(Char.class, "kill", name()) );
				}

			} else {

				enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
			}

		}

		@Override
		public void damage(int dmg, Object src) {
			int beforeHP = HP;
			super.damage(dmg, src);
			if (isAlive() && beforeHP > HT/2 && HP < HT/2){
				HP = HT/2;
				Light l = Dungeon.hero.buff(Light.class);
				if (l != null){
					l.detach();
				}
				int i;
				do {
					i = Random.Int(Dungeon.level.length());
				} while (Dungeon.level.heroFOV[i]
						|| Dungeon.level.solid[i]
						|| Actor.findChar(i) != null
						|| PathFinder.getStep(i, Dungeon.level.exit(), Dungeon.level.passable) == -1);
				ScrollOfTeleportation.appear(this, i);
				state = WANDERING;
				GameScene.flash(0, false);
				GLog.w( Messages.get( this, "teleport" ));
			} else if (!isAlive()){
				Light l = Dungeon.hero.buff(Light.class);
				if (l != null){
					l.detach();
				}
				GameScene.flash(0, false);
			}
		}

	}

	public static class FreezingFist extends YogFist {

		{
			spriteClass = FistSprite.Ice.class;
			HP = HT = 150;
			properties.add(Property.ICY);

			properties.add(Property.FIERY);
			immunities.add(FrostBurning.class);
			immunities.add(HalomethaneBurning.class);
			immunities.add(Terror.class);


			canRangedInMelee = false;
		}

		@Override
		public boolean act() {
			GameScene.add(Blob.seed(pos, 20, Freezing.class));
			return super.act();
		}


		@Override
		protected void incrementRangedCooldown() {
			//ranged attack has no cooldown
		}

		//used so resistances can differentiate between melee and magical attacks
		public static class LightBeam{}

		@Override
		protected void zap() {
			spend( 3f );

			if (hit( this, enemy, true )) {

				enemy.damage( Random.NormalIntRange(10, 20), new LightBeam() );
				Buff.prolong( enemy, Chill.class, Chill.DURATION/2f );

				if (!enemy.isAlive() && enemy == Dungeon.hero) {
					Dungeon.fail( getClass() );
					GLog.n( Messages.get(Char.class, "kill", name()) );
				}

			} else {

				enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
			}

		}

		@Override
		public void damage(int dmg, Object src) {
			super.damage(dmg, src);
			LockedFloor buff = Dungeon.hero.buff(LockedFloor.class);
			if (buff != null) {
				buff.addTime(dmg * 0.5f);
			}
		}

	}

	public static class HaloFist extends YogFist {

		{
			HP = HT = 320;
			spriteClass = FistSprite.HaloFist.class;
			properties.add(Property.FIERY);
			immunities.add(FrostBurning.class);
			immunities.add(HalomethaneBurning.class);
			immunities.add(Terror.class);
		}

		private int phase = 1;

		private int summonsMade = 0;
		private static final int MIN_ABILITY_CD = 7;
		private int lastHeroPos;
		private static final int MAX_ABILITY_CD = 12;
		private float summonCooldown = 0;
		private float abilityCooldown = 3;

		private static float[] chanceMap = {0f, 100f, 100f, 100f, 100f, 100f, 100f};
		private int wave=0;

		private int lastAbility = 0;

		private static final String PHASE = "phase";
		private static final String SUMMONS_MADE = "summons_made";

		private static final String SUMMON_CD = "summon_cd";
		private static final String ABILITY_CD = "ability_cd";
		private static final String LAST_ABILITY = "last_ability";

		private static final String TARGETED_CELLS = "targeted_cells";
		private ArrayList<Integer> targetedCells = new ArrayList<>();
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put( PHASE, phase );
			bundle.put( SUMMONS_MADE, summonsMade );
			bundle.put( SUMMON_CD, summonCooldown );
			bundle.put( ABILITY_CD, abilityCooldown );
			bundle.put( LAST_ABILITY, lastAbility );
			bundle.put("wavePhase2", wave);

			//暴力Boss
			int[] bundleArr = new int[targetedCells.size()];
			for (int i = 0; i < targetedCells.size(); i++){
				bundleArr[i] = targetedCells.get(i);
			}
			bundle.put(TARGETED_CELLS, bundleArr);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			phase = bundle.getInt( PHASE );
			summonsMade = bundle.getInt( SUMMONS_MADE );
			summonCooldown = bundle.getFloat( SUMMON_CD );
			abilityCooldown = bundle.getFloat( ABILITY_CD );
			lastAbility = bundle.getInt( LAST_ABILITY );
			wave = bundle.getInt("wavePhase2");

			if (phase == 2) properties.add(Property.IMMOVABLE);

			//暴力Boss
			int[] bundleArr = new int[targetedCells.size()];
			for (int i = 0; i < targetedCells.size(); i++){
				bundleArr[i] = targetedCells.get(i);
			}
			bundle.put(TARGETED_CELLS, bundleArr);
		}

		@Override
		public void die( Object cause ) {
			GLog.n(Messages.get(YogFist.class,"HaloFist"));
			for (Mob boss : Dungeon.level.mobs.toArray(new Mob[0])) {
				if (boss instanceof FreezingFist) {
					boss.properties.remove(Property.FIERY);
					boss.remove(FrostBurning.class);
					boss.remove(HalomethaneBurning.class);
				}
			}

			for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
				if (mob.alignment == Alignment.ENEMY && mob != this && !(mob instanceof YogReal || mob instanceof YogDzewa || mob instanceof YogFist.FreezingFist||mob instanceof SuccubusQueen||mob instanceof GreenSlting||
						mob instanceof DM275||mob instanceof GnollHero) ) {
					mob.die( cause );
				}
			}

			super.die( cause );
		}
		public static class DarkBolt{}
		public void doDiedLasers(){
			boolean terrainAffected = false;
			HashSet<Char> affected = new HashSet<>();
			//delay fire on a rooted hero
			if(enemy != null) {
				if (!enemy.rooted) {
					for (int i : targetedCells) {
						Ballistica b = new Ballistica(i, lastHeroPos, Ballistica.WONT_STOP);
						//shoot beams
						sprite.parent.add(new Beam.DeathRay(DungeonTilemap.raisedTileCenterToWorld(i),
								DungeonTilemap.raisedTileCenterToWorld(b.collisionPos)));
						for (int p : b.path) {
							Char ch = Actor.findChar(p);
							if (ch != null && (ch.alignment != alignment || ch instanceof Bee)) {
								affected.add(ch);
							}
							if (Dungeon.level.flamable[p]) {
								Dungeon.level.destroy(p);
								GameScene.updateMap(p);
								terrainAffected = true;
							}
						}
					}
					if (terrainAffected) {
						Dungeon.observe();
					}
					for (Char ch : affected) {
						ch.damage(Random.NormalIntRange(28, 42),new DarkBolt());

						if (Dungeon.level.heroFOV[pos]) {
							ch.sprite.flash();
							CellEmitter.center(pos).burst(Speck.factory(Speck.COIN), Random.IntRange(6,12));
						}
						if (!ch.isAlive() && ch == Dungeon.hero) {
							Dungeon.fail(getClass());
							GLog.n(Messages.get(Char.class, "kill", name()));
						}
					}
					targetedCells.clear();
				}
			}
			if(enemy != null) {
				if (abilityCooldown <= 0 && HP < HT * 0.4f) {
					lastHeroPos = enemy.pos;

					int beams = (int) (4 + (HP * 1.0f / HT) * 2);
					for (int i = 0; i < beams; i++) {
						int randompos = Random.Int(Dungeon.level.width()) + Dungeon.level.width() * 2;
						targetedCells.add(randompos);
					}

					for (int i : targetedCells) {
						Ballistica b = new Ballistica(i, enemy.pos, Ballistica.WONT_STOP);

						for (int p : b.path) {
							Game.scene().addToFront(new TargetedCell(p, Window.GDX_COLOR));
						}
					}

					spend(TICK * 1.5f);
					Dungeon.hero.interrupt();
					abilityCooldown += Random.NormalFloat(MIN_ABILITY_CD - 2 * (1 - (HP * 1f / HT)),
							MAX_ABILITY_CD - 5 * (1 - (HP * 1f / HT)));
				} else {
					spend(TICK);
				}
			}
			if (abilityCooldown > 0) abilityCooldown-= 5;
		}

		@Override
		public boolean act() {
			GameScene.add(Blob.seed(pos, 20, HalomethaneFire.class));
			doDiedLasers();
			boolean result = super.act();

			if (Dungeon.level.map[pos] == Terrain.WATER){
				Level.set( pos, Terrain.EMPTY);
				GameScene.updateMap( pos );
				CellEmitter.get( pos ).burst( Speck.factory( Speck.STEAM ), 10 );
			}

			//1.67 evaporated tiles on average
			int evaporatedTiles = Random.chances(new float[]{0, 1, 2});

			for (int i = 0; i < evaporatedTiles; i++) {
				int cell = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
				if (Dungeon.level.map[cell] == Terrain.WATER){
					Level.set( cell, Terrain.EMPTY);
					GameScene.updateMap( cell );
					CellEmitter.get( cell ).burst( Speck.factory( Speck.STEAM ), 10 );
				}
			}

			for (int i : PathFinder.NEIGHBOURS9) {
				int vol = HalomethaneFire.volumeAt(pos+i, HalomethaneFire.class);
				if (vol < 4 && !Dungeon.level.water[pos + i] && !Dungeon.level.solid[pos + i]){
					GameScene.add( Blob.seed( pos + i, 4 - vol, HalomethaneFire.class ) );
				}
			}

			return result;
		}

		@Override
		protected void zap() {
			spend( 1f );

			if (Dungeon.level.map[enemy.pos] == Terrain.WATER){
				Level.set( enemy.pos, Terrain.EMPTY);
				GameScene.updateMap( enemy.pos );
				CellEmitter.get( enemy.pos ).burst( Speck.factory( Speck.STEAM ), 10 );
			} else {
				Buff.affect( enemy, HalomethaneBurning.class ).reignite( enemy );
			}

			for (int i : PathFinder.NEIGHBOURS9){
				if (!Dungeon.level.water[enemy.pos+i] && !Dungeon.level.solid[enemy.pos+i]){
					int vol = HalomethaneFire.volumeAt(enemy.pos+i, HalomethaneFire.class);
					if (vol < 4){
						GameScene.add( Blob.seed( enemy.pos + i, 4 - vol, HalomethaneFire.class ) );
					}
				}
			}

		}

		{
			immunities.add(Frost.class);
		}

	}



}
