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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.bossrush;

import static com.shatteredpixel.shatteredpixeldungeon.BGMPlayer.playBGM;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.nosync.SkyGooBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.StormTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GooSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.Iterator;

@SuppressWarnings("all")
public class SkyGoo extends Boss implements Callback {

	{
		HT = 280;
		HP = 280;
		EXP = 30;
		defenseSkill = 5;
		spriteClass = GooSprite.class;
		properties.add(Char.Property.BOSS);
		properties.add(Char.Property.DEMONIC);
		properties.add(Char.Property.ACIDIC);
		pumpedUp = 0;
		PUMPEDUP = "pumpedup";

		HUNTING = new Hunting();
	}

	private final String PUMPEDUP;
	private int pumpedUp;
	private int var2;

    @Override
    public void call() {
        next();
    }

    public static class LightningBolt {}
	private int healInc = 1;
	private int lastPos = 0;
	private boolean skill1 = false;
	private boolean skill3 = false;
	private boolean skill2 = true;

	@Override
	public int damageRoll() {
		int min = 1;
		int max = (HP*2 <= HT) ? 16 : 10;
		if (pumpedUp > 0) {
			pumpedUp = 0;
			return Random.NormalIntRange( min*3, max*3 );
		} else {
			return Random.NormalIntRange( min, max );
		}
	}

	@Override
	public int attackSkill( Char target ) {
		int attack = 10;
		if (HP*2 <= HT) attack = 15;
		if (pumpedUp > 0) attack *= 2;
		return attack;
	}

	@Override
	public int defenseSkill(Char enemy) {
		return (int)(super.defenseSkill(enemy) * ((HP*2 <= HT)? 1.5 : 1));
	}

	@Override
	public void move( int step ) {
		super.move( step );
		if(HP <= 150){
			GameScene.add(Blob.seed(pos, 10, SkyGoo.SkyGooBlob.class));
		}
	}

	@Override
	public boolean act() {
		if (Dungeon.level.water[this.pos] && this.HP < this.HT) {
			if (Dungeon.level.heroFOV[this.pos]) {
				this.sprite.emitter().burst(Speck.factory(0), 1);
			}
			if (this.HP * 2 == this.HT) {
				BossHealthBar.bleed(false);
				((GooSprite) this.sprite).spray(false);
			}
			this.HP++;
		}
		if (this.state != this.SLEEPING) {
			Dungeon.level.seal();
		}

		AiState lastState = state;
		boolean result = super.act();
		if (paralysed <= 0) leapCooldown --;

		//if state changed from wandering to hunting, we haven't acted yet, don't update.
		if (!(lastState == WANDERING && state == HUNTING)) {
			if (enemy != null) {
				lastEnemyPos = enemy.pos;
			} else {
				lastEnemyPos = hero.pos;
			}
		}

		lastPos = pos;

		if(HP <= 200 && !skill3){
			for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
				int p = pos + PathFinder.NEIGHBOURS8[i];
				if (Actor.findChar( p ) == null && Dungeon.level.passable[p]) {
					Mob testActor = Reflection.newInstance(Bestiary.getMobRotation(Random.NormalIntRange(3,9)).get(0));
					Buff.affect(testActor, ChampionEnemy.Bomber.class);
					testActor.state = testActor.HUNTING;
					GameScene.add( testActor );
					ScrollOfTeleportation.appear( testActor, p );
					ScrollOfTeleportation.appear( hero, 388 );
					ScrollOfTeleportation.appear( this, 451 );
				}
			}
			skill3 =true;
		}
		return result;
	}

	@Override
	protected boolean canAttack( Char enemy ) {
		if (Dungeon.level.distance(this.pos, enemy.pos) <= 1) {
			return this.pumpedUp > 0 ? distance(enemy) <= 2 : super.canAttack(enemy);
		}
		spend(1.0f);
		if (hit(this, enemy, true)) {
			int dmg = Random.NormalIntRange(3, 6);
			enemy.damage(dmg, new LightningBolt());
			if (enemy.sprite.visible) {
				enemy.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
				enemy.sprite.flash();
			}
			if (enemy == Dungeon.hero) {
				Camera.main.shake(2.0f, 0.3f);
				if (!enemy.isAlive()) {
					Dungeon.fail(getClass());
					GLog.n(Messages.get(this, "zap_kill"));
				}
			}
		} else {
			enemy.sprite.showStatus(16776960, enemy.defenseVerb());
		}
		if (this.sprite == null || !(this.sprite.visible || enemy.sprite.visible)) {
			return true;
		}
		this.sprite.zap(enemy.pos);
		return false;
	}

	@Override
	public int attackProc( Char enemy, int damage ) {
		int damage2 = super.attackProc(enemy, damage);
		if (Random.Int(3) == 0) {
			((Ooze) Buff.affect(enemy, Ooze.class)).set(20.0f);
			((Poison) Buff.affect(enemy, Poison.class)).set(10.0f);
			enemy.sprite.burst(61166, 9);
		} else {
			((Blindness) Buff.affect(enemy, Blindness.class)).set(10.0f);
			enemy.sprite.burst(15597568, 9);
		}
		if (this.pumpedUp > 0) {
			Camera.main.shake(3.0f, 0.2f);
		}
		int i = this.var2;
		if (Random.Int(3) == 0 && HP<=200) {
			int i2 = this.var2 + 10;
			StormTrap one = new StormTrap();
			one.pos = super.pos;
			one.activate();
			AlarmTrap two = new AlarmTrap();
			two.pos = super.pos;
			two.activate();
			yell(Messages.get(this, "zl"));
		}
		int reg = Math.min(2, this.HT - this.HP);
		if (reg > 0) {
			this.HP += reg;
			this.sprite.emitter().burst(Speck.factory(0), 1);
		}
		yell(Messages.get(this, "ss"));
		return damage2;
	}

	@Override
	public void updateSpriteState() {
		super.updateSpriteState();
		if (this.pumpedUp > 0) {
			((GooSprite) this.sprite).pumpUp(this.pumpedUp);
		}
	}

	@Override
	protected boolean doAttack( Char enemy ) {
		int i = this.pumpedUp;
		if (i == 1) {
			((GooSprite) this.sprite).pumpUp(2);
			this.pumpedUp++;
			spend(attackDelay());
			return true;
		}
		if (i < 2) {
			if (Random.Int(this.HP * 2 <= this.HT ? 2 : 5) <= 0) {
				this.pumpedUp++;
				((GooSprite) this.sprite).pumpUp(1);
				if (Dungeon.level.heroFOV[this.pos]) {
					this.sprite.showStatus(16711680, Messages.get(this, "!!!"));
					GLog.n(Messages.get(this, "pumpup"));
				}
				spend(attackDelay());
				return true;
			}
		}
		boolean visible = Dungeon.level.heroFOV[this.pos];
		if (visible) {
			if (this.pumpedUp >= 2) {
				((GooSprite) this.sprite).pumpAttack();
				((GooSprite) this.sprite).pumpAttack();
			} else {
				this.sprite.attack(enemy.pos);
			}
		} else {
			if (this.pumpedUp >= 2) {
				((GooSprite) this.sprite).triggerEmitters();
			}
			attack(enemy);
		}
		spend(attackDelay());
		return !visible;
	}

	@Override
	public boolean attack( Char enemy, float dmgMulti, float dmgBonus, float accMulti ) {
		boolean result = super.attack( enemy, dmgMulti, dmgBonus, accMulti );
		if (pumpedUp > 0) {
			pumpedUp = 0;
			if (enemy == hero) {
				Statistics.qualifiedForBossChallengeBadge = false;
				Statistics.bossScores[0] -= 100;
			}
		}
		return result;
	}

	@Override
	protected boolean getCloser( int target ) {
		if (this.pumpedUp != 0) {
			this.pumpedUp = 0;
			this.sprite.idle();
		}
		return super.getCloser( target );
	}

	@Override
	public void damage(int dmg, Object src) {
		if (!BossHealthBar.isAssigned()) {
			BossHealthBar.assignBoss(this);
		}

		if( Random.Int(10) >= 5 && src.getClass() == SkyGoo.SkyGooBlob.class ) {
			Buff.affect(this, SkyGoo.SkyGooBarrior.class).setShield(25);
			return;
		}

		if(HP<=120){
			dmg = (int) Math.ceil( dmg * Math.max( Math.pow( 0.7, 120/HP ), 0.5 ) );
		}
		boolean bleeding = (this.HP*2 <= this.HT);
		super.damage(dmg, src);
		if (this.HP * 2 <= this.HT && !bleeding) {
			BossHealthBar.bleed(true);
			this.sprite.showStatus(16711680, Messages.get(this, "enraged"));
			((GooSprite) this.sprite).spray(true);
			yell(Messages.get(this, "gluuurp"));
		}
		LockedFloor lock = (LockedFloor) Dungeon.hero.buff(LockedFloor.class);
		if (lock != null) {
			lock.addTime(dmg * 2);
		}
	}

	@Override
	public void die( Object cause ) {
		super.die( cause );
		SkyGooBossLevel level = (SkyGooBossLevel) Dungeon.level;
		level.unseal();
		int blobs = Random.chances(new float[]{0, 0, 6, 3, 1});
		for (int i = 0; i < blobs; i++){
			int ofs;
			do {
				ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
			} while (!Dungeon.level.passable[pos + ofs]);
			Dungeon.level.drop( new GooBlob(), pos + ofs ).sprite.drop( pos );
		}
		playBGM(Assets.BGM_1, true);
		GameScene.bossSlain();
		Dungeon.level.drop( new CrystalKey( Dungeon.depth ), pos ).sprite.drop();
		Dungeon.level.drop( new CrystalKey( Dungeon.depth ), pos ).sprite.drop();
		Dungeon.level.drop( new IronKey( Dungeon.depth ), hero.pos ).sprite.drop();
	}

	@Override
	public void notice() {
		super.notice();
		if (!BossHealthBar.isAssigned()) {
			BossHealthBar.assignBoss(this);
			yell(Messages.get(this, "notice"));
			Dungeon.level.seal();
			Music.INSTANCE.play(Assets.BGM_BOSSA, true);
			Iterator<Char> it = Actor.chars().iterator();
			while (it.hasNext()) {
				Char ch = it.next();
				if (ch instanceof DriedRose.GhostHero) {
					((DriedRose.GhostHero) ch).sayBoss();
				}
			}
		}
	}

	private final String HEALINC = "healinc";
	private static final String LAST_ENEMY_POS = "last_enemy_pos";
	private static final String LEAP_POS = "leap_pos";
	private static final String LEAP_CD = "leap_cd";
	private static final String LASTPOS = "lastPos";
	private static final String SKILL1 = "skill1";
	private static final String SKILL2 = "skill2";
	private static final String SKILL3 = "skill3";

	@Override
	public void storeInBundle( Bundle bundle ) {

		super.storeInBundle( bundle );

		bundle.put( PUMPEDUP , this.pumpedUp );
		bundle.put( HEALINC, healInc );
		bundle.put(LAST_ENEMY_POS, lastEnemyPos);
		bundle.put(LEAP_POS, leapPos);
		bundle.put(LEAP_CD, leapCooldown);

		bundle.put(LASTPOS, lastPos);
		bundle.put(SKILL1, skill1);
		bundle.put(SKILL2, skill2);
		bundle.put(SKILL3, skill3);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {

		super.restoreFromBundle( bundle );

		this.pumpedUp = bundle.getInt( PUMPEDUP );
		if (this.state != this.SLEEPING) BossHealthBar.assignBoss(this);
		if ((this.HP*2 <= this.HT)) BossHealthBar.bleed(true);

		healInc = bundle.getInt(HEALINC);

		lastEnemyPos = bundle.getInt(LAST_ENEMY_POS);
		leapPos = bundle.getInt(LEAP_POS);
		leapCooldown = bundle.getFloat(LEAP_CD);

		lastPos = bundle.getInt(LASTPOS);
		skill1 = bundle.getBoolean(SKILL1);
		skill2 = bundle.getBoolean(SKILL2);
		skill2 = bundle.getBoolean(SKILL3);
	}

	private int lastEnemyPos = -1;
	private int leapPos = -1;
	private float leapCooldown = 0;

	public class Hunting extends Mob.Hunting {

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {

			if (leapPos != -1){

				leapCooldown = Random.NormalIntRange(2, 4);

				if (rooted){
					leapPos = -1;
					return true;
				}

				Ballistica b = new Ballistica(pos, leapPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
				leapPos = b.collisionPos;

				final Char leapVictim = Actor.findChar(leapPos);
				final int endPos;

				//ensure there is somewhere to land after leaping
				if (leapVictim != null){
					int bouncepos = -1;
					for (int i : PathFinder.NEIGHBOURS8){
						if ((bouncepos == -1 || Dungeon.level.trueDistance(pos, leapPos+i) < Dungeon.level.trueDistance(pos, bouncepos))
								&& Actor.findChar(leapPos+i) == null && Dungeon.level.passable[leapPos+i]){
							bouncepos = leapPos+i;
						}
					}
					if (bouncepos == -1) {
						leapPos = -1;
						return true;
					} else {
						endPos = bouncepos;
					}
				} else {
					endPos = leapPos;
				}

				//do leap

				for(int i:PathFinder.NEIGHBOURS9){
					Char ch;
					if ((ch = Actor.findChar( leapPos+ i )) != null) {
						if ( ch == enemy && !ch.isImmune(this.getClass()) ) {
							Buff.affect(ch, Ooze.class).set(8f);
							GameScene.add(Blob.seed(pos, 4, Fire.class));
							if(Random.Int(100)>=74){ Buff.affect(ch, Poison.class).set(6f); }
						}
					}
				}

				Char ch;
				ch = Actor.findChar(leapPos);
				if(ch!=null){
					for(int i:PathFinder.NEIGHBOURS8){
						if (Actor.findChar( leapPos+ i ) == null) {
							Actor.addDelayed(new Pushing(ch,leapPos,leapPos+i),-1);
						}
					}
				}

				ScrollOfTeleportation.appear(SkyGoo.this,leapPos);
				//pos = endPos;
				leapPos = -1;
				sprite.idle();
				Dungeon.level.occupyCell(SkyGoo.this);
				next();
				return false;
			}

			enemySeen = enemyInFOV;
			if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {
				return doAttack( enemy );

			} else {

				if (enemyInFOV) {
					target = enemy.pos;
				} else if (enemy == null) {
					state = WANDERING;
					target = Dungeon.level.randomDestination( SkyGoo.this );
					return true;
				}

				if (leapCooldown <= 0 && enemyInFOV && !rooted
						&& Dungeon.level.distance(pos, enemy.pos) >= 3) {

					int targetPos = enemy.pos;
					if (lastEnemyPos != enemy.pos){
						int closestIdx = 0;
						for (int i = 1; i < PathFinder.CIRCLE8.length; i++){
							if (Dungeon.level.trueDistance(lastEnemyPos, enemy.pos+PathFinder.CIRCLE8[i])
									< Dungeon.level.trueDistance(lastEnemyPos, enemy.pos+PathFinder.CIRCLE8[closestIdx])){
								closestIdx = i;
							}
						}
						targetPos = enemy.pos + PathFinder.CIRCLE8[(closestIdx+4)%8];
					}

					Ballistica b = new Ballistica(pos, targetPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
					//try aiming directly at hero if aiming near them doesn't work
					if (b.collisionPos != targetPos && targetPos != enemy.pos){
						targetPos = enemy.pos;
						b = new Ballistica(pos, targetPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
					}
					if (b.collisionPos == targetPos){
						//get ready to leap
						leapPos = targetPos;
						//don't want to overly punish players with slow move or attack speed
						spend(GameMath.gate(attackDelay(), (int)Math.ceil(enemy.cooldown()), 3*attackDelay()));
						if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[leapPos]){
							yell(Messages.get(SkyGoo.this, "leap"));
							sprite.parent.addToBack(new TargetedCell(leapPos, 0xFF0000));
							//((RatSprite)sprite).leapPrep( leapPos );
							hero.interrupt();
						}
						return true;
					}
				}

				int oldPos = pos;
				if (target != -1 && getCloser( target )) {

					spend( 1 / speed() );
					return moveSprite( oldPos,  pos );

				} else {
					spend( TICK );
					if (!enemyInFOV) {
						sprite.showLost();
						state = WANDERING;
						target = Dungeon.level.randomDestination( SkyGoo.this );
					}
					return true;
				}
			}
		}

	}

	public static class SkyGooBlob extends Blob {
		{
			alwaysVisible = true;
		}
		@Override
		protected void evolve() {

			//super.evolve();
			Char ch;
			int cell;

			for (int i = area.left-2; i <= area.right; i++) {
				for (int j = area.top-2; j <= area.bottom; j++) {
					cell = i + j*Dungeon.level.width();
					if (cur[cell] > 0) {

						if ((ch = Actor.findChar( cell )) != null) {
							if (!ch.isImmune(this.getClass())) {
								ch.damage(5, this);
							}
						}

						off[cell] = cur[cell] - 1;
						volume += off[cell];
					} else {
						off[cell] = 0;
					}
				}
			}
		}

		@Override
		public void use( BlobEmitter emitter ) {
			super.use( emitter );

			emitter.pour( Speck.factory( Speck.DIED ), 0.4f );
		}

		@Override
		public String tileDesc() {
			return Messages.get(SkyGoo.class,"blob");
		}
	}

	public static class SkyGooBarrior extends Barrier {

		@Override
		public boolean act() {
			incShield();
			return super.act();
		}

		@Override
		public int icon() {
			return BuffIndicator.NONE;
		}
	}

	{
		//all harmful blobs
		immunities.add( SkyGooBlob.class );
	}

}
