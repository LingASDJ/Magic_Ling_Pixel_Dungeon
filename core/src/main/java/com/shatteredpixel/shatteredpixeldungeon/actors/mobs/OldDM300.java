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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.MOREROOM;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.BGMPlayer;
import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM275Sprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class OldDM300 extends FlameC02 {
	
	{
		spriteClass =  DM275Sprite.class;
		state = PASSIVE;
		HP = HT = 270;
		EXP = 30;
		defenseSkill = 18;
		maxLvl=30;
		baseSpeed = 0.85f;

		properties.add(Property.LARGE);
		properties.add(Property.MINIBOSS);
		properties.add(Property.INORGANIC);
		properties.add(Property.FIERY);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 10, 20 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 28;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 10);
	}


	@Override
	public boolean act() {
		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);

		if (lock == null && Dungeon.level.heroFOV[pos]){
			CavesLevel level = (CavesLevel) Dungeon.level;
			level.seal();
			if(Dungeon.isChallenged(MOREROOM) && !(Dungeon.isDLC(Conducts.Conduct.BOSSRUSH))) {
				AlarmTrap alarmTrap = new AlarmTrap();
				alarmTrap.pos = pos;
				alarmTrap.activate();
				ScrollOfTeleportation.appear(hero, pos+8);
			}
		}

		int evaporatedTiles = Random.chances(new float[]{0, 1, 2});

		for (int i = 0; i < evaporatedTiles; i++) {
			int cell = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
			if (Dungeon.level.map[cell] == Terrain.WATER){
				Level.set( cell, Terrain.EMPTY);
				GameScene.updateMap( cell );
				CellEmitter.get( cell ).burst( Speck.factory( Speck.STEAM ), 10 );
			}
		}
		
		return super.act();
	}
	
	@Override
	public void move( int step ) {
		super.move( step );
		
		if (Dungeon.level.map[step] == Terrain.WATER && HP < HT) {
			
			HP += Random.Int( 1, (HT - HP)/3 );
			sprite.emitter().burst( ElmoParticle.FACTORY, 5 );
			
			if (Dungeon.level.heroFOV[step] && Dungeon.hero.isAlive()) {
				sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "repair"));
				gasTankPressure += 20;
			}
		}
		
		int[] cells = {
			step-1, step+1, step-Dungeon.level.width(), step+Dungeon.level.width(),
			step-1-Dungeon.level.width(),
			step-1+Dungeon.level.width(),
			step+1-Dungeon.level.width(),
			step+1+Dungeon.level.width()
		};
		int cell = cells[Random.Int( cells.length )];
		
		if (Dungeon.level.heroFOV[cell]) {
			CellEmitter.get( cell ).start( Speck.factory( Speck.ROCK ), 0.07f, 10 );
			Camera.main.shake( 3, 0.7f );
			Sample.INSTANCE.play( Assets.Sounds.ROCKS );
			
			if (Dungeon.level.water[cell]) {
				GameScene.ripple( cell );
			} else if (Dungeon.level.map[cell] == Terrain.EMPTY) {
				Level.set( cell, Terrain.EMPTY_DECO );
				GameScene.updateMap( cell );
			}
		}

		Char ch = Actor.findChar( cell );
		if (ch != null && ch != this) {
			Buff.prolong( ch, Paralysis.class, 2 );
		}
	}

	@Override
	public void notice() {
		super.notice();
		if (!BossHealthBar.isAssigned()) {
			BossHealthBar.assignBoss(this);
			yell(Messages.get(this, "notice"));
			GameScene.bossReady();
		}
	}

	@Override
	public void die( Object cause ) {
		super.die( cause );

		CavesLevel level = (CavesLevel) Dungeon.level;

		level.unseal();
		BGMPlayer.playBGMWithDepth();
		//60% chance of 2 shards, 30% chance of 3, 10% chance for 4. Average of 2.5
		int shards = Random.chances(new float[]{0, 0, 6, 3, 1});
		for (int i = 0; i < shards; i++){
			int ofs;
			do {
				ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
			} while (!Dungeon.level.passable[pos + ofs]);
			Dungeon.level.drop( Generator.randomUsingDefaults(Generator.Category.MIS_T3), pos + ofs ).sprite.drop( pos );
			Dungeon.level.drop( Generator.randomUsingDefaults(Generator.Category.POTION), pos + ofs ).sprite.drop( pos );
			Dungeon.level.drop( Generator.randomUsingDefaults(Generator.Category.FOOD), pos + ofs ).sprite.drop( pos );
			Dungeon.level.drop( Generator.randomUsingDefaults(Generator.Category.SEED), pos + ofs ).sprite.drop( pos );
			Dungeon.level.drop( Generator.randomUsingDefaults(Generator.Category.WAND), pos).sprite.drop( pos );

		}
		GameScene.bossSlain();
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
			if (mob instanceof DM201 ) {
				mob.die( cause );
			}
		}
		yell( Messages.get(this, "defeated") );
	}

	@Override
	public void damage(int dmg, Object src) {
		super.damage(dmg, src);
		if (state == PASSIVE) {
			state = HUNTING;
			notice();
			ScrollOfTeleportation.appear(hero, pos+8);
			CavesLevel level = (CavesLevel) Dungeon.level;
			level.seal();
		}

		if(HP<50){
			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
				if (mob instanceof DM201 ) {
					mob.die(true);
					Buff.affect(this, Barrier.class).setShield(140);
				}
			}
		}


		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if (lock != null && !isImmune(src.getClass())) lock.addTime(dmg*1.5f);
	}
	
	{
		immunities.add( ToxicGas.class );
		immunities.add( Terror.class );
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (state == HUNTING){
			BossHealthBar.assignBoss(this);
		}

	}
}


