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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.CS;
import static com.shatteredpixel.shatteredpixeldungeon.Challenges.DHXD;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.bossLevel;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.lanterfireactive;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.BGMPlayer;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MonkEnergy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Preparation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.Feint;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DirectableAlly;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.Pets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.SmallLight;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Surprise;
import com.shatteredpixel.shatteredpixeldungeon.effects.Wound;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MasterThievesArmband;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ExoticCrystals;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Lucky;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import net.iharder.Base64;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public abstract class Mob extends Char {
    protected static final String TXT_NOTICE1 = "?!";
    protected static final String TXT_RAGE = "#$%^";
    private static final String TXT_DIED = "You hear something died in the distance";
    private static ArrayList<Mob> heldAllies = new ArrayList<>();
    //FIXME this is sort of a band-aid correction for allies needing more intelligent behaviour
	protected boolean intelligentAlly = false;
	protected static final String TXT_EXP		= "%+dEXP";
	public static ArrayList<Mob> Mobs = new ArrayList<>();
	public boolean discovered = false;
	public AiState SLEEPING     = new Sleeping();
	public AiState HUNTING		= new Hunting();
	public AiState WANDERING	= new Wandering();
	public AiState FLEEING		= new Fleeing();
	public AiState PASSIVE		= new Passive();
	public AiState state = SLEEPING;

	public boolean isStupid;
	public HashSet<Class> beneficialPlants;


	public Class<? extends CharSprite> spriteClass;

	protected int target = -1;

	public int defenseSkill = 0;

	public int EXP = 1;
	public int maxLvl = Hero.MAX_LEVEL-1;

	protected Char enemy;
	protected int enemyID = -1; //used for save/restore
	protected boolean enemySeen;
	protected boolean alerted = false;

	//whether the hero should interact with the mob (true) or attack it (false)
	public boolean heroShouldInteract(){
		return alignment != Alignment.ENEMY && buff(Amok.class) == null;
	}

	protected static final float TIME_TO_WAKE_UP = 1f;
	@Override
	public int attackProc(Char enemy, int damage) {

		if(Dungeon.isChallenged(DHXD)||lanterfireactive){
			damageAttackProcLanterMob();
		}

		return super.attackProc(enemy, damage);
	}

	private void damageAttackProcLanterMob() {
		// 近战判定，如果不是英雄直接返回
		if (!(enemy instanceof Hero)) return;

		float chance = Random.Float();
		boolean GhostQuestMob = this instanceof GreatCrab || this instanceof GnollTrickster || this instanceof FetidRat;
		Hero hero = (Hero) enemy;

		// 使用策略模式或类似机制来处理不同怪物的逻辑
		if (this instanceof Rat && chance <= 0.15f && hero.lanterfire < 95) {
			hero.damageLantern(1);
		} else if (this instanceof Guard && chance <= 0.25f && hero.lanterfire < 90) {
			hero.damageLantern(1);
		} else if (this instanceof Shaman && chance <= 0.15f && hero.lanterfire < 80) {
			hero.damageLantern(2);
		} else if (GhostQuestMob && chance <= 0.50f) {
			hero.damageLantern(4);
		} else if (this instanceof Wraith) {
			if (chance <= 0.75f) {
				hero.damageLantern(6);
				this.die(true);
			}
		} else if (this instanceof Elemental.NewbornFireElemental && chance <= 0.25f && hero.lanterfire < 80) {
			hero.damageLantern(6);
		} else if (this instanceof Warlock && chance <= 0.85f && hero.lanterfire < 70) {
			hero.damageLantern(5);
		} else if (this instanceof BlackHost && chance <= 0.85f && hero.lanterfire < 90) {
			hero.damageLantern(5);
		}
	}


	protected boolean firstAdded = true;
	protected void onAdd(){
		if (firstAdded) {
			//modify health for ascension challenge if applicable, only on first add
			float percent = HP / (float) HT;
			HT = Math.round(HT * AscensionChallenge.statModifier(this));
			if(Dungeon.isChallenged(CS) && Dungeon.depth>2 && Dungeon.depth<25 && !properties.contains(Property.NPC) && !bossLevel()){
				HT = Math.round(HT * ChampionEnemy.AloneCity.statModifier(this));
			}
			HP = Math.round(HT * percent);
			firstAdded = false;
		}
	}

	private static final String STATE	= "state";
	private static final String SEEN	= "seen";
	private static final String TARGET	= "target";
	private static final String MAX_LVL	= "max_lvl";
	private static final String ENEMY_ID= "enemy_id";
	private static final String STUPID	= "stupid";

    protected Object loot = null;

    {
        actPriority = MOB_PRIO;

        alignment = Alignment.ENEMY;
    }

	//mobs need to remember their targets after every actor is added
	public void restoreEnemy(){
		if (enemyID != -1 && enemy == null) enemy = (Char)Actor.findById(enemyID);
	}

	public CharSprite sprite() {
		return Reflection.newInstance(spriteClass);
	}

	public static void holdAllies( Level level, int holdFromPos ){
		heldAllies.clear();
		for (Mob mob : level.mobs.toArray( new Mob[0] )) {
			//preserve directable allies no matter where they are
			if (mob instanceof DirectableAlly) {
				((DirectableAlly) mob).clearDefensingPos();
				level.mobs.remove(mob);
				heldAllies.add(mob);

			} else if(mob instanceof Pets){
				((Pets) mob).clearDefensingPos();
				level.mobs.remove(mob);
				heldAllies.add(mob);
			} else if(mob instanceof BloodBat){
				((BloodBat) mob).clearDefensingPos();
				level.mobs.remove(mob);
				heldAllies.add(mob);
            } else if (mob.alignment == Alignment.ALLY
					&& mob.intelligentAlly
					&& Dungeon.level.distance(holdFromPos, mob.pos) <= 5){
				level.mobs.remove( mob );
				heldAllies.add(mob);
			}
		}
	}

	public static void restoreAllies( Level level, int pos, int gravitatePos ){
		if (!heldAllies.isEmpty()){

            ArrayList<Integer> candidatePositions = new ArrayList<>();
			for (int i : PathFinder.NEIGHBOURS8) {
				if (!Dungeon.level.solid[i+pos] && level.findMob(i+pos) == null){
					candidatePositions.add(i+pos);
				}
			}

			//gravitate pos sets a preferred location for allies to be closer to
			if (gravitatePos == -1) {
				Collections.shuffle(candidatePositions);
			} else {
				Collections.sort(candidatePositions, new Comparator<Integer>() {
					@Override
					public int compare(Integer t1, Integer t2) {
						return Dungeon.level.distance(gravitatePos, t1) -
								Dungeon.level.distance(gravitatePos, t2);
					}
				});
			}

            for (Mob ally : heldAllies) {
				level.mobs.add(ally);
				ally.state = ally.WANDERING;

                if (!candidatePositions.isEmpty()){
					ally.pos = candidatePositions.remove(0);
				} else {
					ally.pos = pos;
				}
				if (ally.sprite != null) ally.sprite.place(ally.pos);

				if (ally.fieldOfView == null || ally.fieldOfView.length != level.length()){
					ally.fieldOfView = new boolean[level.length()];
				}
				Dungeon.level.updateFieldOfView( ally, ally.fieldOfView );

            }
		}
		heldAllies.clear();
	}

    public static void clearHeldAllies(){
		heldAllies.clear();
	}

    public void onZapComplete() {
        next();
    }

	@Override
	public void storeInBundle( Bundle bundle ) {

		super.storeInBundle( bundle );

		if (state == SLEEPING) {
			bundle.put( STATE, Sleeping.TAG );
		} else if (state == WANDERING) {
			bundle.put( STATE, Wandering.TAG );
		} else if (state == HUNTING) {
			bundle.put( STATE, Hunting.TAG );
		} else if (state == FLEEING) {
			bundle.put( STATE, Fleeing.TAG );
		} else if (state == PASSIVE) {
			bundle.put( STATE, Passive.TAG );
		}
		bundle.put( SEEN, enemySeen );
		bundle.put( TARGET, target );
		bundle.put( MAX_LVL, maxLvl );

		if (enemy != null) {
			bundle.put(ENEMY_ID, enemy.id() );
		}

		bundle.put( STUPID, isStupid );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {

		super.restoreFromBundle( bundle );
		String state = bundle.getString( STATE );
		if (state.equals( Sleeping.TAG )) {
			this.state = SLEEPING;
		} else if (state.equals( Wandering.TAG )) {
			this.state = WANDERING;
		} else if (state.equals( Hunting.TAG )) {
			this.state = HUNTING;
		} else if (state.equals( Fleeing.TAG )) {
			this.state = FLEEING;
		} else if (state.equals( Passive.TAG )) {
			this.state = PASSIVE;
		}

		enemySeen = bundle.getBoolean( SEEN );

		target = bundle.getInt( TARGET );

		if (bundle.contains(MAX_LVL)) maxLvl = bundle.getInt(MAX_LVL);

		if (bundle.contains(ENEMY_ID)) {
			enemyID = bundle.getInt(ENEMY_ID);
		}

		//no need to actually save this, must be false
		firstAdded = false;

		isStupid = bundle.getBoolean( STUPID );
	}

	private boolean cellIsPathable( int cell ){
		if (!Dungeon.level.passable[cell]){
			if (flying || buff(Amok.class) != null){
				if (!Dungeon.level.avoid[cell]){
					return false;
				}
			} else {
				return false;
			}
		}
		if (Char.hasProp(this, Char.Property.LARGE) && !Dungeon.level.openSpace[cell]){
			return false;
		}
		Char c = Actor.findChar(cell);
		if (c != null){
			if(this instanceof SmallLight){
				if(c instanceof Mob && !(c instanceof SmallLight) && ((SmallLight) this).canTele(c.pos)){
					((SmallLight) this).teleportEnemy(c.pos);
					Buff.affect(c, MagicalSleep.class);
					c.sprite.centerEmitter().start( Speck.factory( Speck.NOTE ), 0.3f, 5 );
					return true;
				}
			}
			return false;
		}

		return true;
	}

	public boolean focusingHero() {
		return enemySeen && Dungeon.level.heroFOV[pos];
	}

	@Override
	protected boolean act() {

		super.act();

		//相位体
		if (!Dungeon.level.heroFOV[pos] && HP < HT && buff(ChampionEnemy.HealRight.class) != null) {
			HP += Math.min(1, HT);
			spend(12f);
			return true;
		}

		boolean justAlerted = alerted;
		alerted = false;

        if (justAlerted){
			sprite.showAlert();
		} else {
			sprite.hideAlert();
			sprite.hideLost();
		}

        if (paralysed > 0) {
			enemySeen = false;
			spend( TICK );
			return true;
		}

		if (buff(Terror.class) != null || buff(Dread.class) != null ){
			state = FLEEING;
		}

        enemy = chooseEnemy();

        boolean enemyInFOV = enemy != null && enemy.isAlive() && fieldOfView[enemy.pos] && enemy.invisible <= 0;

		//prevents action, but still updates enemy seen status
		if (buff(Feint.AfterImage.FeintConfusion.class) != null){
			enemySeen = enemyInFOV;
			spend( TICK );
			return true;
		}

		return state.act( enemyInFOV, justAlerted );
	}

    protected Char chooseEnemy() {

		Dread dread = buff( Dread.class );
		if (dread != null) {
			Char source = (Char)Actor.findById( dread.object );
			if (source != null) {
				return source;
			}
		}

		Terror terror = buff( Terror.class );
		if (terror != null) {
			Char source = (Char)Actor.findById( terror.object );
			if (source != null) {
				return source;
			}
		}

        //if we are an alert enemy, auto-hunt a target that is affected by aggression, even another enemy
		if (alignment == Alignment.ENEMY && state != PASSIVE && state != SLEEPING) {
			if (enemy != null && enemy.buff(StoneOfAggression.Aggression.class) != null){
				state = HUNTING;
				return enemy;
			}
			for (Char ch : Actor.chars()) {
				if (ch != this && fieldOfView[ch.pos] &&
						ch.buff(StoneOfAggression.Aggression.class) != null) {
					state = HUNTING;
					return ch;
				}
			}
		}

		//find a new enemy if..
		boolean newEnemy = false;
		//we have no enemy, or the current one is dead/missing
		if ( enemy == null || !enemy.isAlive() || !Actor.chars().contains(enemy) || state == WANDERING) {
			newEnemy = true;
            //We are amoked and current enemy is the hero
		} else if (buff( Amok.class ) != null && enemy == hero) {
			newEnemy = true;
            //We are charmed and current enemy is what charmed us
		} else if (buff(Charm.class) != null && buff(Charm.class).object == enemy.id()) {
			newEnemy = true;
		}

		//additionally, if we are an ally, find a new enemy if...
		if (!newEnemy && alignment == Alignment.ALLY){
			//current enemy is also an ally
			if (enemy.alignment == Alignment.ALLY){
				newEnemy = true;
                //current enemy is invulnerable
			} else if (enemy.isInvulnerable(getClass())){
				newEnemy = true;
			}
		}

		if ( newEnemy ) {

			HashSet<Char> enemies = new HashSet<>();

			//if we are amoked...
			if ( buff(Amok.class) != null) {
				//try to find an enemy mob to attack first.
				for (Mob mob : Dungeon.level.mobs)
					if (mob.alignment == Alignment.ENEMY && mob != this
							&& fieldOfView[mob.pos] && mob.invisible <= 0) {
						enemies.add(mob);
					}

                if (enemies.isEmpty()) {
					//try to find ally mobs to attack second.
					for (Mob mob : Dungeon.level.mobs)
						if (mob.alignment == Alignment.ALLY && mob != this
								&& fieldOfView[mob.pos] && mob.invisible <= 0) {
                            enemies.add(mob);
                        }

                    if (enemies.isEmpty()) {
                        //try to find the hero third
                        if (fieldOfView[hero.pos] && hero.invisible <= 0) {
                            enemies.add(hero);
                        }
                    }
                }

                //if we are an ally...
            } else if ( alignment == Alignment.ALLY ) {
                //look for hostile mobs to attack
                for (Mob mob : Dungeon.level.mobs)
                    if (mob.alignment == Alignment.ENEMY && fieldOfView[mob.pos]
                            && mob.invisible <= 0 && !mob.isInvulnerable(getClass()))
                        //intelligent allies do not target mobs which are passive, wandering, or asleep
                        if (!intelligentAlly ||
                                (mob.state != mob.SLEEPING && mob.state != mob.PASSIVE && mob.state != mob.WANDERING)) {
                            enemies.add(mob);
                        }

                //if we are an enemy...
            } else if (alignment == Alignment.ENEMY) {
				//look for ally mobs to attack
				for (Mob mob : Dungeon.level.mobs)
					if (mob.alignment == Alignment.ALLY && fieldOfView[mob.pos] && mob.invisible <= 0)
						enemies.add(mob);

				//and look for the hero
				if (fieldOfView[hero.pos] && hero.invisible <= 0) {
					enemies.add(hero);
				}

            }

			//do not target anything that's charming us
			Charm charm = buff( Charm.class );
			if (charm != null){
				Char source = (Char)Actor.findById( charm.object );
				if (source != null && enemies.contains(source) && enemies.size() > 1){
					enemies.remove(source);
				}
			}

			//neutral characters in particular do not choose enemies.
			if (enemies.isEmpty()){
				return null;
			} else {
				//go after the closest potential enemy, preferring enemies that can be reached/attacked, and the hero if two are equidistant
				PathFinder.buildDistanceMap(pos, Dungeon.findPassable(this, Dungeon.level.passable, fieldOfView, true));
				Char closest = null;

				for (Char curr : enemies){
					if (closest == null){
						closest = curr;
					} else if (canAttack(closest) && !canAttack(curr)){
						continue;
					} else if ((canAttack(curr) && !canAttack(closest))
							|| (PathFinder.distance[curr.pos] < PathFinder.distance[closest.pos])){
						closest = curr;
					} else if ( curr == hero &&
							(PathFinder.distance[curr.pos] == PathFinder.distance[closest.pos]) || (canAttack(curr) && canAttack(closest))){
						closest = curr;
					}
				}
				//if we were going to target the hero, but an afterimage exists, target that instead
				if (closest == hero){
					for (Char ch : enemies){
						if (ch instanceof Feint.AfterImage){
							closest = ch;
							break;
						}
					}
				}

				return closest;
			}

		} else
			return enemy;
	}

	@Override
	public void updateSpriteState() {
		super.updateSpriteState();
		if (hero.buff(TimekeepersHourglass.timeFreeze.class) != null
				|| hero.buff(Swiftthistle.TimeBubble.class) != null)
			sprite.add( CharSprite.State.PARALYSED );
	}

    @Override
	public boolean add( Buff buff ) {
		if (super.add( buff )) {
			if (buff instanceof Amok || buff instanceof AllyBuff) {
				state = HUNTING;
			} else if (buff instanceof Terror || buff instanceof Dread) {
				state = FLEEING;
			} else if (buff instanceof Sleep) {
				state = SLEEPING;
				postpone(Sleep.SWS);
			}
			return true;
		}
		return false;
	}

    @Override
	public boolean remove( Buff buff ) {
		if (super.remove( buff )) {
			if ((buff instanceof Terror && buff(Dread.class) == null)
					|| (buff instanceof Dread && buff(Terror.class) == null)) {
				if (enemySeen) {
					sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "rage"));
					state = HUNTING;
				} else {
					state = WANDERING;
				}
			}
			return true;
		}
		return false;
	}

    protected boolean canAttack( Char enemy ) {
		if (Dungeon.level.adjacent( pos, enemy.pos )){
			return true;
		}

		for (ChampionEnemy buff : buffs(ChampionEnemy.class)){
			if (buff.canAttackWithExtraReach( enemy )){
				return true;
			}
		}
		return false;
	}

	protected boolean getCloser( int target ) {

        if (rooted || target == pos) {
			return false;
		}

		int step = -1;

		if (Dungeon.level.adjacent( pos, target )) {

			path = null;

			if (cellIsPathable(target)) {
				step = target;
			}

		} else {

			boolean newPath = false;
			//scrap the current path if it's empty, no longer connects to the current location
			//or if it's extremely inefficient and checking again may result in a much better path
			if (path == null || path.isEmpty()
					|| !Dungeon.level.adjacent(pos, path.getFirst())
					|| path.size() > 2*Dungeon.level.distance(pos, target))
				newPath = true;
			else if (path.getLast() != target) {
				//if the new target is adjacent to the end of the path, adjust for that
				//rather than scrapping the whole path.
				if (Dungeon.level.adjacent(target, path.getLast())) {
					int last = path.removeLast();

					if (path.isEmpty()) {

						//shorten for a closer one
						if (Dungeon.level.adjacent(target, pos)) {
							path.add(target);
                            //extend the path for a further target
						} else {
							path.add(last);
							path.add(target);
						}

					} else {
						//if the new target is simply 1 earlier in the path shorten the path
						if (path.getLast() == target) {

                            //if the new target is closer/same, need to modify end of path
						} else if (Dungeon.level.adjacent(target, path.getLast())) {
							path.add(target);

                            //if the new target is further away, need to extend the path
						} else {
							path.add(last);
							path.add(target);
						}
					}

				} else {
					newPath = true;
				}

			}

			//checks if the next cell along the current path can be stepped into
			if (!newPath) {
				int nextCell = path.removeFirst();
				if (!cellIsPathable(nextCell)) {

					newPath = true;
					//If the next cell on the path can't be moved into, see if there is another cell that could replace it
					if (!path.isEmpty()) {
						for (int i : PathFinder.NEIGHBOURS8) {
							if (Dungeon.level.adjacent(pos, nextCell + i) && Dungeon.level.adjacent(nextCell + i, path.getFirst())) {
								if (cellIsPathable(nextCell+i)){
									path.addFirst(nextCell+i);
									newPath = false;
									break;
								}
							}
						}
					}
				} else {
					path.addFirst(nextCell);
				}
			}

			//generate a new path
			if (newPath) {
				//If we aren't hunting, always take a full path
				PathFinder.Path full = Dungeon.findPath(this, target, Dungeon.level.passable, fieldOfView, true);
				if (state != HUNTING){
					path = full;
				} else {
					//otherwise, check if other characters are forcing us to take a very slow route
					// and don't try to go around them yet in response, basically assume their blockage is temporary
					PathFinder.Path ignoreChars = Dungeon.findPath(this, target, Dungeon.level.passable, fieldOfView, false);
					if (ignoreChars != null && (full == null || full.size() > 2*ignoreChars.size())){
						//check if first cell of shorter path is valid. If it is, use new shorter path. Otherwise do nothing and wait.
						path = ignoreChars;
						if (!cellIsPathable(ignoreChars.getFirst())) {
							return false;
						}
					} else {
						path = full;
					}
				}
			}

			if (path != null) {
				step = path.removeFirst();
			} else {
				return false;
			}
		}
		if (step != -1) {
			move( step );
			return true;
		} else {
			return false;
		}
	}

    protected boolean getFurther( int target ) {
		if (rooted || target == pos) {
			return false;
		}

        int step = Dungeon.flee( this, target, Dungeon.level.passable, fieldOfView, true );
		if (step != -1) {
			move( step );
			return true;
		} else {
			return false;
		}
	}

	@Override
	public float speed() {
		return super.speed() * AscensionChallenge.enemySpeedModifier(this);
	}

	public final boolean surprisedBy( Char enemy ){
		return surprisedBy( enemy, true);
	}

	public boolean surprisedBy( Char enemy, boolean attacking ){
		return enemy == hero
				&& (enemy.invisible > 0 || !enemySeen || (fieldOfView != null && fieldOfView.length == Dungeon.level.length() && !fieldOfView[enemy.pos]))
				&& (!attacking || enemy.canSurpriseAttack());
	}

	public void aggro( Char ch ) {
		enemy = ch;
		if (state != PASSIVE){
			state = HUNTING;
		}
	}

	public void clearEnemy(){
		enemy = null;
		enemySeen = false;
		if (state == HUNTING) state = WANDERING;
	}

    public float attackDelay() {
		float delay = 1f;
		if ( buff(Adrenaline.class) != null) delay /= 1.5f;
		if(buff(ChampionEnemy.Sider.class) != null){
			delay = 4.0f;
		}



		return delay;
	}

    protected boolean doAttack( Char enemy ) {

        if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
			sprite.attack( enemy.pos );
			return false;

        } else {
			attack( enemy );
			Invisibility.dispel(this);
			spend( attackDelay() );
			return true;
		}
	}

    @Override
	public void onAttackComplete() {
		attack( enemy );
		Invisibility.dispel(this);
		spend( attackDelay() );
		super.onAttackComplete();
	}

    @Override
	public int defenseSkill( Char enemy ) {
		if ( !surprisedBy(enemy)
				&& paralysed == 0
				&& !(alignment == Alignment.ALLY && enemy == hero)) {
			return this.defenseSkill;
		} else {
			return 0;
		}
	}

	public float lootChance(){
		float lootChance = this.lootChance;

		float dropBonus = RingOfWealth.dropChanceMultiplier( hero );

		Talent.BountyHunterTracker bhTracker = hero.buff(Talent.BountyHunterTracker.class);
		if (bhTracker != null){
			Preparation prep = hero.buff(Preparation.class);
			if (prep != null){
				// 2/4/8/16% per prep level, multiplied by talent points
				float bhBonus = 0.02f * (float)Math.pow(2, prep.attackLevel()-1);
				bhBonus *= hero.pointsInTalent(Talent.BOUNTY_HUNTER);
				dropBonus += bhBonus;
			}
		}

		return lootChance * dropBonus;
	}

    @Override
	public int defenseProc( Char enemy, int damage ) {

		if (enemy instanceof Hero
				&& ((Hero) enemy).belongings.attackingWeapon() instanceof MissileWeapon){
			Statistics.thrownAssists++;
			Badges.validateHuntressUnlock();
		}

        if (surprisedBy(enemy)) {
            Statistics.sneakAttacks++;
            Badges.validateRogueUnlock();
            //TODO this is somewhat messy, it would be nicer to not have to manually handle delays here
            // playing the strong hit sound might work best as another property of weapon?
            if (hero.belongings.attackingWeapon() instanceof SpiritBow.SpiritArrow
                    || hero.belongings.attackingWeapon() instanceof Dart) {
                Sample.INSTANCE.playDelayed(Assets.Sounds.HIT_STRONG, 0.125f);
            } else {
                Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
            }
            if (enemy.buff(Preparation.class) != null) {
                Wound.hit(this);
            } else {
                Surprise.hit(this);
            }
		}

		//if attacked by something else than current target, and that thing is closer, switch targets
		if (this.enemy == null
				|| (enemy != this.enemy && (Dungeon.level.distance(pos, enemy.pos) < Dungeon.level.distance(pos, this.enemy.pos)))) {
			aggro(enemy);
			target = enemy.pos;
		}

		if (buff(SoulMark.class) != null) {
			int restoration = Math.min(damage, HP+shielding());

            //physical damage that doesn't come from the hero is less effective
			if (enemy != hero){
				restoration = Math.round(restoration * 0.4f* hero.pointsInTalent(Talent.SOUL_SIPHON)/3f);
			}
			if (restoration > 0) {
				Buff.affect(hero, Hunger.class).affectHunger(restoration* hero.pointsInTalent(Talent.SOUL_EATER)/3f);
				hero.HP = (int) Math.ceil(Math.min(hero.HT, hero.HP + (restoration * 0.4f)));
				hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
			}
		}

		return super.defenseProc(enemy, damage);
	}

    public boolean isTargeting( Char ch){
		return enemy == ch;
	}
	protected float lootChance = 0;

	@Override
	public void damage( int dmg, Object src ) {

		if (state == SLEEPING) {
			state = WANDERING;
		}



		if (state != HUNTING && !(src instanceof Corruption)) {
			alerted = true;
		}

		if(this.buff(ChampionEnemy.DelayMob.class) != null && dmg> 0){
			Viscosity.DeferedDamage deferred = Buff.affect( this, Viscosity.DeferedDamage.class );
			deferred.prolong( dmg/6 );
		}

        super.damage( dmg, src );
	}

	//how many mobs this one should count as when determining spawning totals
	public float spawningWeight(){
		return 1;
	}

    @Override
	public void destroy() {

        super.destroy();

        Dungeon.level.mobs.remove( this );

		if (hero.buff(MindVision.class) != null){
			Dungeon.observe();
			GameScene.updateFog(pos, 2);
		}

		if (hero.isAlive()) {

            if (alignment == Alignment.ENEMY) {
				Statistics.enemiesSlain++;
				Badges.validateMonstersSlain();
				Statistics.qualifiedForNoKilling = false;

				AscensionChallenge.processEnemyKill(this);

                int exp = hero.lvl <= maxLvl ? EXP : 0;

				//during ascent, under-levelled enemies grant 10 xp each until level 30
				// after this enemy kills which reduce the amulet curse still grant 10 effective xp
				// for the purposes of on-exp effects, see AscensionChallenge.processEnemyKill
				if (hero.buff(AscensionChallenge.class) != null &&
						exp == 0 && maxLvl > 0 && EXP > 0 && hero.lvl < Hero.MAX_LEVEL){
					exp = Math.round(10 * spawningWeight());
				}

				if (exp > 0) {
					hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "exp", exp));
				}
				hero.earnExp(exp, getClass());

				if (hero.subClass == HeroSubClass.MONK){
					Buff.affect(hero, MonkEnergy.class).gainEnergy(this);
				}
			}
		}
	}

    @Override
	public void die( Object cause ) {

		discovered = true;



		if (cause == Chasm.class){
			//50% chance to round up, 50% to round down
			if (EXP % 2 == 1) EXP += Random.Int(2);
			EXP /= 2;
		}

		if(Dungeon.level.feeling == Level.Feeling.SKYCITY && alignment == Alignment.ENEMY){
			rollToDropLoot();
		} else if (alignment == Alignment.ENEMY){
			rollToDropLoot();

			if (cause == hero) {
				//New 钢铁意志
				if (hero.hasTalent(Talent.IRON_WILL)) {
					Buff.affect(hero, Barrier.class).setShield(1 + hero.pointsInTalent(Talent.IRON_WILL));
				}
			}

			if (cause == hero || cause instanceof Weapon || cause instanceof Weapon.Enchantment){
				if (hero.hasTalent(Talent.LETHAL_MOMENTUM)
						&& Random.Float() < 0.34f + 0.33f* hero.pointsInTalent(Talent.LETHAL_MOMENTUM)){
					Buff.affect(hero, Talent.LethalMomentumTracker.class, 0f);
				}
				if (hero.heroClass != HeroClass.DUELIST
						&& hero.hasTalent(Talent.LETHAL_HASTE)
						&& hero.buff(Talent.LethalHasteCooldown.class) == null){
					Buff.affect(hero, Talent.LethalHasteCooldown.class, 100f);
					Buff.affect(hero, Haste.class, 1.67f + hero.pointsInTalent(Talent.LETHAL_HASTE));
				}
			}

		}

		if (hero.isAlive() && !Dungeon.level.heroFOV[pos]) {
			GLog.i( Messages.get(this, "died") );
		}

		boolean soulMarked = buff(SoulMark.class) != null;

		super.die( cause );

		if (!(this instanceof Wraith)
				&& soulMarked
				&& Random.Float() < (0.4f* hero.pointsInTalent(Talent.NECROMANCERS_MINIONS)/3f)){
			Wraith w = Wraith.spawnAt(pos, Wraith.class);
			if (w != null) {
				Buff.affect(w, Corruption.class);
				if (Dungeon.level.heroFOV[pos]) {
					CellEmitter.get(pos).burst(ShadowParticle.CURSE, 6);
					Sample.INSTANCE.play(Assets.Sounds.CURSED);
				}
			}
		}
	}

    public void rollToDropLoot(){
		if (hero.lvl > maxLvl + 2) return;

		MasterThievesArmband.StolenTracker stolen = buff(MasterThievesArmband.StolenTracker.class);
		if (stolen == null || !stolen.itemWasStolen()) {
			if (Random.Float() < lootChance()) {
				Item loot = createLoot();
				if (loot != null) {
					Dungeon.level.drop(loot, pos).sprite.drop();
				}
			}
		}

        //ring of wealth logic
		if (Ring.getBuffedBonus(hero, RingOfWealth.Wealth.class) > 0) {
			int rolls = 1;
			if (properties.contains(Property.BOSS)) rolls = 15;
			else if (properties.contains(Property.MINIBOSS)) rolls = 5;
			ArrayList<Item> bonus = RingOfWealth.tryForBonusDrop(hero, rolls);
			if (bonus != null && !bonus.isEmpty()) {
				for (Item b : bonus) Dungeon.level.drop(b, pos).sprite.drop();
				RingOfWealth.showFlareForBonusDrop(sprite);
			}
		}

        //lucky enchant logic
		if (buff(Lucky.LuckProc.class) != null){
			Dungeon.level.drop(buff(Lucky.LuckProc.class).genLoot(), pos).sprite.drop();
			Lucky.showFlare(sprite);
		}

		//soul eater talent
		if (buff(SoulMark.class) != null &&
				Random.Int(10) < hero.pointsInTalent(Talent.SOUL_EATER)){
			Talent.onFoodEaten(hero, 0, null);
		}

	}


	public String encodeWithLineBreak(String text) {
		byte[] bytes = text.getBytes();
		byte[] encodedBytes = Base64.encodeBytes(bytes).getBytes();

		StringBuilder sb = new StringBuilder();
		int index = 0;
		while (index < encodedBytes.length) {
			sb.append(new String(encodedBytes, index, Math.min(32, encodedBytes.length - index)));
			sb.append("\n");
			index += 32;
		}

		return sb.toString();
	}

	public String info(){
		String desc = description();

		if(buff(ChampionEnemy.NoCode.class) != null){
			try {
				desc = encodeWithLineBreak(description());
			} catch (Exception ignored) {
			}
		}

		for (Buff b : buffs(ChampionEnemy.class)){
			desc += "\n\n_" + Messages.titleCase(b.name()) + "_\n" + b.desc();
		}

		String intelligence = isStupid ? Messages.get(this, "stupid", name()) : Messages.get(this, "smart",name());

		return desc;
	}

    @SuppressWarnings("unchecked")
	public Item createLoot() {
		Item item;
		if (loot instanceof Generator.Category) {

			item = Generator.randomUsingDefaults( (Generator.Category)loot );

		} else if (loot instanceof Class<?>) {

			if (ExoticPotion.regToExo.containsKey(loot)){
				if (Random.Float() < ExoticCrystals.consumableExoticChance()){
					return Generator.random(ExoticPotion.regToExo.get(loot));
				}
			} else if (ExoticScroll.regToExo.containsKey(loot)){
				if (Random.Float() < ExoticCrystals.consumableExoticChance()){
					return Generator.random(ExoticScroll.regToExo.get(loot));
				}
			}
			item = Generator.random( (Class<? extends Item>)loot );

		} else {

			item = (Item)loot;

		}
		return item;
	}

    public boolean reset() {
		return false;
	}

	public interface AiState {
		boolean act( boolean enemyInFOV, boolean justAlerted );
	}

	protected class Sleeping implements AiState {

		public static final String TAG	= "SLEEPING";

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {

			//debuffs cause mobs to wake as well
			for (Buff b : buffs()){
				if (b.type == Buff.buffType.NEGATIVE){
					awaken(enemyInFOV);
					if (state == SLEEPING){
						spend(TICK); //wait if we can't wake up for some reason
					}
					return true;
				}
			}

			if (enemyInFOV) {

				float enemyStealth = enemy.stealth();

				if (enemy instanceof Hero && ((Hero) enemy).hasTalent(Talent.SILENT_STEPS)){
					if (Dungeon.level.distance(pos, enemy.pos) >= 4 - ((Hero) enemy).pointsInTalent(Talent.SILENT_STEPS)) {
						enemyStealth = Float.POSITIVE_INFINITY;
					}
				}

				if (Random.Float( distance( enemy ) + enemyStealth ) < 1) {
					awaken(enemyInFOV);
					if (state == SLEEPING){
						spend(TICK); //wait if we can't wake up for some reason
					}
					return true;
				}

			}

			enemySeen = false;
			spend( TICK );

			return true;
		}

		protected void awaken( boolean enemyInFOV ){
			if (enemyInFOV) {
				enemySeen = true;
				notice();
				state = HUNTING;
				target = enemy.pos;
			} else {
				notice();
				state = WANDERING;
				target = Dungeon.level.randomDestination( Mob.this );
			}

			if (alignment == Alignment.ENEMY && Dungeon.isChallenged(Challenges.SWARM_INTELLIGENCE)) {
				for (Mob mob : Dungeon.level.mobs) {
					if (mob.paralysed <= 0
							&& Dungeon.level.distance(pos, mob.pos) <= 8
							&& mob.state != mob.HUNTING) {
						mob.beckon(target);
					}
				}
			}
			spend(TIME_TO_WAKE_UP);
		}
	}

    public void beckon( int cell ) {

        notice();

        if (state != HUNTING && state != FLEEING) {
			state = WANDERING;
		}
		target = cell;
	}

    public String description() {
		return Messages.get(this, "desc");
	}

    public void notice() {
		sprite.showAlert();
		if (Dungeon.level.locked) {
			switch (Dungeon.depth) {
				case 25:
					Dungeon.level.playLevelMusic();
					break;
				default:
					BGMPlayer.playBoss();
					break;
			}
		}
	}

	protected class Passive implements AiState {

		public static final String TAG	= "PASSIVE";

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			enemySeen = enemyInFOV;
			spend( TICK );
			return true;
		}
	}

    public void yell( String str ) {
		GLog.newLine();
		GLog.n( "%s: \"%s\" ", Messages.titleCase(name()), str );
	}

	public static void holdAllies( Level level ){
		holdAllies(level, hero.pos);
	}

	protected class Wandering implements AiState {

		public static final String TAG	= "WANDERING";


		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			if (enemyInFOV && (justAlerted || Random.Float( distance( enemy ) / 2f + enemy.stealth() ) < 1)) {

				return noticeEnemy();

			} else {

				return continueWandering();

			}
		}

		protected boolean noticeEnemy(){
			enemySeen = true;

			notice();
			alerted = true;
			state = HUNTING;
			target = enemy.pos;

			if (Dungeon.isChallenged( Challenges.SWARM_INTELLIGENCE )) {
				for (Mob mob : Dungeon.level.mobs) {
					if (Dungeon.level.distance(pos, mob.pos) <= 8 && mob.state != mob.HUNTING) {
						mob.beckon( target );
					}
				}
			}

			return true;
		}

		protected boolean continueWandering(){
			enemySeen = false;

			//愚蠢的怪物会跟随聪明的怪物
			if (isStupid) {
				for (Mob mob : Dungeon.level.mobs) {
					//他们追随的怪物必须是聪明的，也必须是在巡查状态，以免围攻玩家，而且必须是相同的怪物类
					if (!mob.isStupid) {
						Dungeon.level.distance(pos, mob.pos);
					}
				}
			}

			int oldPos = pos;
			if (target != -1 && getCloser( target )) {
				spend( 1 / speed() );
				return moveSprite( oldPos, pos );
			} else {
				target = Dungeon.level.randomDestination(Mob.this);
				spend( TICK );
			}

			return true;
		}

		protected int randomDestination(){
			return Dungeon.level.randomDestination( Mob.this );
		}
	}



	public static void restoreAllies( Level level, int pos ){
		restoreAllies(level, pos, -1);
	}

	protected class Hunting implements AiState {

		public static final String TAG	= "HUNTING";

		//prevents rare infinite loop cases
		private boolean recursing = false;

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			enemySeen = enemyInFOV;
			if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {

				target = enemy.pos;
				return doAttack( enemy );

			} else {

				if (enemyInFOV) {
					target = enemy.pos;
				} else if (enemy == null) {
					sprite.showLost();
					state = WANDERING;
					target = Dungeon.level.randomDestination( Mob.this );
					spend( TICK );
					return true;
				}

                int oldPos = pos;
				if (target != -1 && getCloser( target )) {

                    spend( 1 / speed() );
					return moveSprite( oldPos,  pos );

				} else {

					//if moving towards an enemy isn't possible, try to switch targets to another enemy that is closer
					//unless we have already done that and still can't move toward them, then move on.
					if (!recursing) {
						Char oldEnemy = enemy;
						enemy = null;
						enemy = chooseEnemy();
						if (enemy != null && enemy != oldEnemy) {
							recursing = true;
							boolean result = act(enemyInFOV, justAlerted);
							recursing = false;
							return result;
						}
					}

					spend( TICK );
					if (!enemyInFOV) {
						sprite.showLost();
						state = WANDERING;
						target = Dungeon.level.randomDestination( Mob.this );
					}
					return true;
				}
			}
		}
	}

	protected class Fleeing implements AiState {

		public static final String TAG	= "FLEEING";

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			enemySeen = enemyInFOV;
			//triggers escape logic when 0-dist rolls a 6 or greater.
			if (enemy == null || !enemyInFOV && 1 + Random.Int(Dungeon.level.distance(pos, target)) >= 6) {
                escaped();
                if (state != FLEEING) {
                    spend(TICK);
                    return true;
                }

                //if enemy isn't in FOV, keep running from their previous position.
            } else if (enemyInFOV) {
				target = enemy.pos;
			}

			int oldPos = pos;
			if (target != -1 && getFurther( target )) {

				spend( 1 / speed() );
				return moveSprite( oldPos, pos );

			} else {

				spend( TICK );
				nowhereToRun();

				return true;
			}
		}

		protected void escaped(){
			//does nothing by default, some enemies have special logic for this
		}

		//enemies will turn and fight if they have nowhere to run and aren't affect by terror
		protected void nowhereToRun() {
			if (buff( Terror.class ) == null
					&& buffs( AllyBuff.class ).isEmpty()
					&& buff( Dread.class ) == null) {
				if (enemySeen) {
					sprite.showStatus(CharSprite.NEGATIVE, Messages.get(Mob.class, "rage"));
					state = HUNTING;
				} else {
					state = WANDERING;
				}
			}
		}
	}


	//MLPD
	//Boss Rush 掉落规则
	public void GetBossLoot(){
		int flakes = Random.chances(new float[]{0, 0, 6, 3, 1});
		for (int i = 0; i < flakes; i++){
			int ofs;
			do {
				ofs = PathFinder.NEIGHBOURS9[Random.Int(4)];
			} while (!(Dungeon.level.passable[pos + ofs] || pos + ofs == this.pos));
			switch (Random.Int(5)) {
				case 0:
					Dungeon.level.drop( ( Generator.random(Generator.Category.POTION)), pos+ofs );
					break;
				case 1:
					Dungeon.level.drop( ( Generator.randomMissile() ), pos+ofs );
					break;
				case 2:
					Dungeon.level.drop( ( Generator.randomArmor() ), pos+ofs );
					break;
				case 3:
					Dungeon.level.drop( ( Generator.randomWeapon() ), pos+ofs );
					break;
				case 4:
					Dungeon.level.drop( ( Generator.random(Generator.Category.RING) ), pos+ofs );
					break;
			}
		}
		Dungeon.level.drop( new Food(), pos ).sprite.drop();
		Dungeon.level.drop( new PotionOfExperience(), pos ).sprite.drop();
	}


//	@Override
//	/**
//	 * 移动方法
//	 *
//	 * @param step 移动的步数
//	 */
//	public void move(int step) {
//		//npcs永远不会死！
//		if(!(this instanceof NPC) && Dungeon.isChallenged(WARLING)){
//			//部分属性生物不需要植物
//			if(!properties.contains(Property.INORGANIC) || !properties.contains(Property.IMMOVABLE)){
//				//检查布尔值和增益，因为每次对每个怪物调用的CP最少 且眩晕 飞行不会生效
//				if(!flying && buff( Vertigo.class ) == null){
//					//只有当HP达到50%或更低时才寻求踩在植物上
//					if(HP <= HT / 2){
//						//检查所有周围的瓷砖
//						for(int p : PathFinder.NEIGHBOURS8){
//							if(Dungeon.level.plants.get(pos+p) != null){
//								if(!isStupid){
//									//如果怪物很聪明，它只会寻找其.class中指定的有益植物
//									if(beneficialPlants.contains(Dungeon.level.plants.get(pos+p).getClass())){
//										//这个变量使得一切更容易输入
//										int newPos = pos+p;
//										//不能让两个怪物同时去同一个植物
//										if(Actor.findChar(newPos) == null){
//											triggerPlant(newPos);
//											return;
//										}
//									}
//									//只有在飞行怪物以某种方式失去了飞行状态时，才会发生这种情况，在这种情况下，我们假设它没有在地面上进化，也不知道什么是植物。
//									if(beneficialPlants.isEmpty()){
//										//这个变量使得一切更容易输入
//										int newPos = pos+p;
//										//不能让两个怪物同时去同一个植物
//										if(Actor.findChar(newPos) == null){
//											triggerPlant(newPos);
//											return;
//										} else {
//											super.move(step);
//										}
//									}
//								} else {
//									//如果怪物很笨，它会走进任何植物
//									//这个变量使得一切更容易输入
//									int newPos = pos+p;
//									//不能让两个怪物同时去同一个植物
//									if(Actor.findChar(newPos) == null){
//										triggerPlant(newPos);
//										return;
//									} else {
//										super.move(step);
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		super.move(step);
//	}

	private void triggerPlant(int newPos){
		//当位于水瓦片上时
		if(Dungeon.level.map[newPos] == Terrain.WATER){
			//将位置设置为新位置，更新瓦片并触发植物，然后从此方法返回，跳过最后的正常移动
			pos = newPos;
			Level.set(newPos, Terrain.WATER);
			GameScene.updateMap(newPos);
			Dungeon.level.plants.get(newPos).trigger();
		}
		//当位于其他瓦片上时
		if(Dungeon.level.map[newPos] == Terrain.GRASS){
			//将位置设置为新位置，更新瓦片并触发植物，然后从此方法返回，跳过最后的正常移动
			pos = newPos;
			Level.set(newPos, Terrain.WATER);
			GameScene.updateMap(newPos);
			Dungeon.level.plants.get(newPos).trigger();
		}
	}


}