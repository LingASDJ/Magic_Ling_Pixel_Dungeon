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

package com.shatteredpixel.shatteredpixeldungeon.plants;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion.SeedToPotion.types;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfRegrowth;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public abstract class Plant implements Bundlable {
	
	public int image;
	public int pos;

	protected Class<? extends Plant.Seed> seedClass;

	public void trigger(){

		Char ch = Actor.findChar(pos);

		if (ch instanceof Hero){
			((Hero) ch).interrupt();
		}

		if (Dungeon.level.heroFOV[pos] && hero.hasTalent(Talent.NATURES_AID)){
			// 3/5 turns based on talent points spent
			Barkskin.conditionallyAppend(hero, 2, 1 + 2*(hero.pointsInTalent(Talent.NATURES_AID)));
		}

		wither();
		activate( ch );
		Bestiary.setSeen(getClass());
		Bestiary.countEncounter(getClass());
	}
	
	public abstract void activate( Char ch );
	
	public void wither() {
		Dungeon.level.uproot( pos );

		if (Dungeon.level.heroFOV[pos]) {
			CellEmitter.get( pos ).burst( LeafParticle.GENERAL, 6 );
		}

		float seedChance = 0f;
		for (Char c : Actor.chars()){
			if (c instanceof WandOfRegrowth.Lotus){
				WandOfRegrowth.Lotus l = (WandOfRegrowth.Lotus) c;
				if (l.inRange(pos)){
					seedChance = Math.max(seedChance, l.seedPreservation());
				}
			}
		}

		if (Random.Float() < seedChance){
			if (seedClass != null && seedClass != Rotberry.Seed.class) {
				Dungeon.level.drop(Reflection.newInstance(seedClass), pos).sprite.drop();
			}
		}
		
	}
	
	private static final String POS	= "pos";

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		pos = bundle.getInt( POS );
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put( POS, pos );
	}

	public String name(){
		return Messages.get(this, "name");
	}

	public String desc() {
		String desc = Messages.get(this, "desc");
		if (Dungeon.hero != null && Dungeon.hero.subClass == HeroSubClass.WARDEN){
			desc += "\n\n" + Messages.get(this, "warden_desc");
		}
		return desc;
	}
	
	public static class Seed extends Item {

		public static final String AC_PLANT	= "PLANT";

		public static final String AC_EATSEED	= "EATSEED";
		
		private static final float TIME_TO_PLANT = 1f;


		@Override
		public String actionName(String action, Hero hero){
			return Messages.get(Seed.class, "ac_" + action);
		}

		@Override
		public String defaultAction() {
			if(hero.heroClass== HeroClass.WARRIOR){
				return AC_EATSEED;
			} else {
				return AC_THROW;
			}
		}

		{
			stackable = true;
			defaultAction = AC_THROW;
		}
		
		protected Class<? extends Plant> plantClass;
		
		@Override
		public ArrayList<String> actions( Hero hero ) {
			ArrayList<String> actions = super.actions( hero );
			actions.add( AC_PLANT );
			if(hero.heroClass== HeroClass.WARRIOR){
				actions.add(AC_EATSEED);
			}
			return actions;
		}

		protected void satisfy( Hero hero ){
			float foodVal = 30f;

			Artifact.ArtifactBuff buff = hero.buff( HornOfPlenty.hornRecharge.class );
			if (buff != null && buff.isCursed()){
				foodVal *= 0.67f;
				GLog.n( Messages.get(Hunger.class, "cursedhorn") );
			}

			Buff.affect(hero, Hunger.class).satisfy(foodVal);
		}
		
		@Override
		protected void onThrow( int cell ) {
			if (Dungeon.level.map[cell] == Terrain.ALCHEMY
					|| Dungeon.level.pit[cell]
					|| Dungeon.level.traps.get(cell) != null
					|| Dungeon.isChallenged(Challenges.NO_HERBALISM)) {
				super.onThrow( cell );
			} else {
				Catalog.countUse(getClass());
				Dungeon.level.plant( this, cell );
				if (hero.subClass == HeroSubClass.WARDEN) {
					for (int i : PathFinder.NEIGHBOURS8) {
						int c = Dungeon.level.map[cell + i];
						if ( c == Terrain.EMPTY || c == Terrain.EMPTY_DECO
								|| c == Terrain.EMBERS || c == Terrain.GRASS){
							Level.set(cell + i, Terrain.FURROWED_GRASS);
							GameScene.updateMap(cell + i);
							CellEmitter.get( cell + i ).burst( LeafParticle.LEVEL_SPECIFIC, 4 );
						}
					}
				}
			}
		}
		public static final float TIME_TO_EAT	= 3f;
		protected float eatingTime(){
			if (Dungeon.hero.hasTalent(Talent.IRON_STOMACH)
					|| Dungeon.hero.hasTalent(Talent.ENERGIZING_MEAL)
					|| Dungeon.hero.hasTalent(Talent.MYSTICAL_MEAL)
					|| Dungeon.hero.hasTalent(Talent.INVIGORATING_MEAL)
					|| Dungeon.hero.hasTalent(Talent.FOCUSED_MEAL)){
				return TIME_TO_EAT - 2;
			} else {
				return TIME_TO_EAT;
			}
		}

		@Override
		public void execute( Hero hero, String action ) {

			super.execute (hero, action );

			if (action.equals( AC_PLANT )) {

				hero.busy();
				((Seed)detach( hero.belongings.backpack )).onThrow( hero.pos );
				hero.spend( TIME_TO_PLANT );

				hero.sprite.operate( hero.pos );
				
			} else if (action.equals( AC_EATSEED )) {
				Potion potion = Reflection.newInstance(types.get(getClass()));
				if(potion != null){
					satisfy(hero);

					boolean isNoSeedAdd =
							curItem instanceof Firebloom.Seed || curItem instanceof Icecap.Seed
									|| curItem instanceof Sorrowmoss.Seed || curItem instanceof SkyBlueFireBloom.Seed ||
									curItem instanceof Earthroot.Seed;
					if(!isNoSeedAdd && !Dungeon.isChallenged(Challenges.EXSG)){
						if(hero.pointsInTalent(Talent.HEARTY_MEAL) == 1 && Random.Float()<=0.25f) {
							potion.drink(hero);
						} else if(hero.pointsInTalent(Talent.HEARTY_MEAL) == 2 && Random.Float()<=0.35f) {
							potion.drink(hero);
						}
					}

					detach( hero.belongings.backpack );
					GLog.i( Messages.get(Food.class, "eat_msg") );

					hero.sprite.operate( hero.pos );
					hero.busy();
					SpellSprite.show( hero, SpellSprite.FOOD );
					Sample.INSTANCE.play( Assets.Sounds.EAT );

					hero.spend( eatingTime() );

					Talent.onFoodEaten(hero, 10f, this);

					Statistics.foodEaten++;
					Badges.validateFoodEaten();
				}

			}
		}

		public Plant couch( int pos, Level level ) {
			if (level != null && level.heroFOV != null && level.heroFOV[pos]) {
				Sample.INSTANCE.play(Assets.Sounds.PLANT);
			}
			Plant plant = Reflection.newInstance(plantClass);
			plant.pos = pos;
			return plant;
		}
		
		@Override
		public boolean isUpgradable() {
			return false;
		}
		
		@Override
		public boolean isIdentified() {
			return true;
		}
		
		@Override
		public int value() {
			return 10 * quantity;
		}

		@Override
		public int energyVal() {
			return 2 * quantity;
		}

		@Override
		public String desc() {
			String desc = Messages.get(plantClass, "desc");
			if (Dungeon.hero != null && Dungeon.hero.subClass == HeroSubClass.WARDEN){
				desc += "\n\n" + Messages.get(plantClass, "warden_desc");
			}
			return desc;
		}

		@Override
		public String info() {
			return Messages.get( Seed.class, "info", super.info() );
		}
		
		public static class PlaceHolder extends Seed {
			
			{
				image = ItemSpriteSheet.SEED_HOLDER;
			}
			
			@Override
			public boolean isSimilar(Item item) {
				return item instanceof Plant.Seed;
			}
			
			@Override
			public String info() {
				return "";
			}
		}
	}
}
