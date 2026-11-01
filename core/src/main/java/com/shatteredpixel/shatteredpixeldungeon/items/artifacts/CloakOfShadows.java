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

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;


import static com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune.isMagicImmuned;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Preparation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.TestBatLock;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BloodBat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CloakOfShadows extends Artifact {

	{
		image = ItemSpriteSheet.ARTIFACT_CLOAK;

		exp = 0;
		levelCap = 10;

		charge = Math.min(level()+3, 10);
		partialCharge = 0;
		chargeCap = Math.min(level()+3, 10);

		defaultAction = AC_STEALTH;

		unique = true;
		bones = false;
	}

	public static final String AC_STEALTH = "STEALTH";

	public static final String AC_BloodBat = "BloodBat";

	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if ((isEquipped( hero ) || hero.hasTalent(Talent.LIGHT_CLOAK))
				&& !cursed && (charge > 0 || activeBuff != null)) {
			actions.add(AC_STEALTH);
		}
		boolean needToSpawn = true;

		for (Mob mob : Dungeon.level.mobs){
			if (mob instanceof BloodBat) {
				needToSpawn = false;
				break;
			}
		}

		if (needToSpawn) {
			if (hero.buff(BloodBat.BloodBatRecharge.class) != null)
				needToSpawn = false;
		}

		if (needToSpawn && Dungeon.hero.buff(TestBatLock.class) == null){
			actions.add(AC_BloodBat);
		}
		return actions;
	}


	@Override
	public void execute( Hero hero, String action ) {

		super.execute(hero, action);

		if (isMagicImmuned(hero)) return;

		if (action.equals( AC_STEALTH )) {

			if (activeBuff == null){
				if (!isEquipped(hero) && !hero.hasTalent(Talent.LIGHT_CLOAK)) GLog.i( Messages.get(Artifact.class, "need_to_equip") );
				else if (cursed)       GLog.i( Messages.get(this, "cursed") );
				else if (charge <= 0)  GLog.i( Messages.get(this, "no_charge") );
				else {
					//幽冥斗篷使用不消耗回合
					if (!phantomMode) {
						hero.spend(1f);
					}
					hero.busy();
					Sample.INSTANCE.play(Assets.Sounds.MELD);
					activeBuff = activeBuff();
					activeBuff.attachTo(hero);
					Talent.onArtifactUsed(Dungeon.hero);
					hero.sprite.operate(hero.pos);
				}
			} else {
				activeBuff.detach();
				activeBuff = null;
				if (hero.invisible <= 0 && hero.buff(Preparation.class) != null){
					hero.buff(Preparation.class).detach();
				}
				hero.sprite.operate( hero.pos );
			}
		} else if (action.equals(AC_BloodBat)){
			Buff.affect(hero, TestBatLock.class).set( (1), 1 );
			hero.sprite.operate(hero.pos, new Callback() {
				@Override
				public void call() {
					ArrayList<Integer> respawnPoints = new ArrayList<>();
					for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
						int p = hero.pos + PathFinder.NEIGHBOURS8[i];
						if (Actor.findChar( p ) == null && Dungeon.level.passable[p]) {
							respawnPoints.add( p );
						}
					}
					if (respawnPoints.size() > 0){
						BloodBat bat = new BloodBat();
						bat.pos = respawnPoints.get(Random.index( respawnPoints ));
						bat.HP = bat.HT = 18 + BloodBat.level * 2;
						bat.defenseSkill = 4 + BloodBat.level*2;
						bat.state = bat.WANDERING;
						GameScene.add(bat);
						bat.sprite.emitter().burst(Speck.factory(Speck.STAR), 10);
						hero.sprite.idle();
					}
				}
			});
		}

	}

	@Override
	public void activate(Char ch){
		super.activate(ch);
		if (activeBuff != null && activeBuff.target == null){
			activeBuff.attachTo(ch);
		}
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)){
			if (!collect || !hero.hasTalent(Talent.LIGHT_CLOAK)){
				if (activeBuff != null){
					activeBuff.detach();
					activeBuff = null;
				}
			} else {
				activate(hero);
			}

			return true;
		} else
			return false;
	}

	@Override
	public boolean collect( Bag container ) {
		if (super.collect(container)){
			//转职幽冥猎手时，自动将进入背包的暗影斗篷转换成幽冥斗篷
			if (container.owner instanceof Hero) {
				Hero owner = (Hero) container.owner;
				if (owner.subClass == HeroSubClass.PHANTOM_HUNTER) {
					becomePhantomCloak();
				}
				if (passiveBuff == null
						&& owner.hasTalent(Talent.LIGHT_CLOAK)) {
					activate(owner);
				}
			}
			return true;
		} else{
			return false;
		}
	}

	@Override
	protected void onDetach() {
		if (passiveBuff != null){
			passiveBuff.detach();
			passiveBuff = null;
		}
		if (activeBuff != null && !isEquipped((Hero) activeBuff.target)){
			activeBuff.detach();
			activeBuff = null;
		}
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new cloakRecharge();
	}

	@Override
	protected ArtifactBuff activeBuff( ) {
		return new cloakStealth();
	}
	
	@Override
	public void charge(Hero target, float amount) {
		if (cursed || isMagicImmuned(target)) return;

		if (charge < chargeCap) {
			if (!isEquipped(target)) amount *= 0.75f*target.pointsInTalent(Talent.LIGHT_CLOAK)/3f;
			//幽冥斗篷神器充能效率更高
			float chargeFactor = phantomMode ? 0.5f : 0.25f;
			partialCharge += chargeFactor * amount;
			while (partialCharge >= 1f) {
				charge++;
				partialCharge--;
			}
			if (charge >= chargeCap){
				partialCharge = 0;
				charge = chargeCap;
			}
			updateQuickslot();
		}
	}

	public void directCharge(int amount){
		charge = Math.min(charge+amount, chargeCap);
		updateQuickslot();
	}
	
	@Override
	public Item upgrade() {
		chargeCap = Math.min(chargeCap + 1, 10);
		return super.upgrade();
	}

	private static final String BUFF = "buff";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);

		bundle.put(PHANTOM_MODE, phantomMode);

		if (activeBuff != null) {
			bundle.put(BUFF, activeBuff);
		}
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);

		phantomMode = bundle.contains(PHANTOM_MODE)
				&& bundle.getBoolean(PHANTOM_MODE);

		// 物品初始化时会先设置成暗影斗篷，所以恢复后要重新设置图标
		image = phantomMode
				? ItemSpriteSheet.ARTIFACT_PHANTOM_CLOAK
				: ItemSpriteSheet.ARTIFACT_CLOAK;

		if (bundle.contains(BUFF)) {
			activeBuff = new cloakStealth();
			activeBuff.restoreFromBundle(bundle.getBundle(BUFF));
		}
	}

	@Override
	public int value() {
		return 0;
	}

	public class cloakRecharge extends ArtifactBuff{
		@Override
		public boolean act() {
			if (charge < chargeCap && !cursed && !isMagicImmuned(target)) {
				if (activeBuff == null && Regeneration.regenOn()) {
					float missing = (chargeCap - charge);
					if (level() > 7) missing += 5*(level() - 7)/3f;
					float turnsToCharge = (45 - missing);
					turnsToCharge /= RingOfEnergy.artifactChargeMultiplier(target);
					float chargeToGain = (1f / turnsToCharge);
					if (!isEquipped(Dungeon.hero)){
						chargeToGain *= 0.75f*Dungeon.hero.pointsInTalent(Talent.LIGHT_CLOAK)/3f;
					}
					partialCharge += chargeToGain;
				}

				while (partialCharge >= 1) {
					charge++;
					partialCharge -= 1;
					if (charge == chargeCap){
						partialCharge = 0;
					}

				}
			} else {
				partialCharge = 0;
			}

			if (cooldown > 0)
				cooldown --;

			updateQuickslot();

			spend( TICK );

			return true;
		}

	}

	public static class cloakStealthStats extends Buff {

		{
			type = buffType.POSITIVE;
		}

		@Override
		public int icon() {
			return BuffIndicator.INVISIBLE;
		}
		@Override
		public void tintIcon(Image icon) {
			icon.brightness(0.6f);
		}

	}

	public class cloakStealth extends ArtifactBuff{
		
		{
			type = buffType.POSITIVE;
		}
		
		int turnsToCost = 0;

		@Override
		public int icon() {
			return BuffIndicator.INVISIBLE;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.brightness(0.6f);
		}

		@Override
		public float iconFadePercent() {
			float duration = stealthTurnsPerCharge(target,chargesSpentThisUse);
			return Math.max(0f, (duration - turnsToCost) / duration);
		}
		//记录隐身时间
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);

			bundle.put(TURNSTOCOST, turnsToCost);
			bundle.put(CHARGES_SPENT_THIS_USE, chargesSpentThisUse);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);

			if (bundle.contains(TURNSTOCOST)) {
				turnsToCost = bundle.getInt(TURNSTOCOST);
			}

			if (bundle.contains(CHARGES_SPENT_THIS_USE)) {
				chargesSpentThisUse =
						bundle.getInt(CHARGES_SPENT_THIS_USE);
			}
		}
		/**
		 * 幽冥漫步：记录本次连续隐身已经完整消耗的充能数。
		 * 每结束一段充能，这个值就增加1。
		 * 它有两个用途：
		 * 1. 为下一段隐身增加相同数量的持续回合；
		 * 2. 隐身结束时判断是否达到4/3/2点返还门槛。
		 */
		private int chargesSpentThisUse = 0;
		// 一次隐身可能从主动关闭、自然耗尽等多个出口结束，确保只返还一次。
		private boolean refundedThisUse = false;
		/**
		 * 结算幽冥漫步的充能返还。
		 * 天赋+1/+2/+3分别需要本次累计消耗4/3/2点充能。
		 * 达到门槛后返还1点，但神器已经满充能时不继续增加。
		 */
		private void refundIfEligible() {
			if (refundedThisUse || !(target instanceof Hero)) {
				return;
			}

			refundedThisUse = true;

			Hero hero = (Hero) target;
			int points = hero.pointsInTalent(Talent.PHANTOM_WALK);

			int requiredCharges = 5 - points;
			// 1点需要4，2点需要3，3点需要2

			if (points > 0
					&& chargesSpentThisUse >= requiredCharges
					&& charge < chargeCap) {

				charge++;
				updateQuickslot();
			}
		}

		@Override
		public String iconTextDisplay() {
			return Integer.toString(turnsToCost);
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", turnsToCost);
		}

		@Override
		public boolean attachTo( Char target ) {
			if (super.attachTo( target )) {
				target.invisible++;
				if (target instanceof Hero && ((Hero) target).subClass == HeroSubClass.ASSASSIN){
					Buff.affect(target, Preparation.class);
				}
				if (target instanceof Hero && ((Hero) target).hasTalent(Talent.PROTECTIVE_SHADOWS)){
					Buff.affect(target, Talent.ProtectiveShadowsTracker.class);
				}
				return true;
			} else {
				return false;
			}
		}

		@Override
		public boolean act(){
			turnsToCost--;
			
			if (turnsToCost <= 0){
				charge--;
				if (charge < 0) {
					charge = 0;
					detach();
					GLog.w(Messages.get(this, "no_charge"));
					((Hero) target).interrupt();
				} else {
					// 当前这段充能已经耗尽，先记录消耗数，再计算下一段隐身时长。
					chargesSpentThisUse++;
					//target hero level is 1 + 2*cloak level
					int lvlDiffFromTarget = ((Hero) target).lvl - (1+level()*2);
					//plus an extra one for each level after 6
					if (level() >= 7){
						lvlDiffFromTarget -= level()-6;
					}
					if (lvlDiffFromTarget >= 0){
						exp += Math.round(10f * Math.pow(1.1f, lvlDiffFromTarget));
					} else {
						exp += Math.round(10f * Math.pow(0.75f, -lvlDiffFromTarget));
					}
					
					if (exp >= (level() + 1) * 50 && level() < levelCap) {
						upgrade();
						Catalog.countUse(CloakOfShadows.class);
						exp -= level() * 50;
						GLog.p(Messages.get(this, "levelup"));
						
					}
					turnsToCost = stealthTurnsPerCharge(target,chargesSpentThisUse);
				}
				updateQuickslot();
			}

			spend( TICK );

			return true;
		}

		public void dispel(){
			if (turnsToCost <= 0 && charge > 0){
				charge--;
			}
			updateQuickslot();
			detach();
		}

		@Override
		public void fx(boolean on) {
			if (on) target.sprite.add( CharSprite.State.INVISIBLE );
			else if (target.invisible == 0) target.sprite.remove( CharSprite.State.INVISIBLE );
		}

		@Override
		public void detach() {
			/*
			 * 主动关闭、自然耗尽、受击显形等结束方式最终都会进入detach()。
			 * 因此把幽冥漫步的返还结算统一放在这里。
			 */
			refundIfEligible();
			activeBuff = null;

			if (target.invisible > 0)   target.invisible--;

			updateQuickslot();
			super.detach();
		}
		
		private static final String TURNSTOCOST = "turnsToCost";
		private static final String CHARGES_SPENT_THIS_USE = "charges_spent_this_use";

	}
	//幽冥斗篷
	private boolean phantomMode = false;
	private static final String PHANTOM_MODE = "phantom_mode";

	public void becomePhantomCloak() {
		if (!phantomMode) {
			phantomMode = true;
			image = ItemSpriteSheet.ARTIFACT_PHANTOM_CLOAK;
			updateQuickslot();
		}
	}
		//幽冥/隐身斗篷时，每点充能5/4回合隐身
	protected int stealthTurnsPerCharge(Char wearer, int chargesSpentThisUse) {
		int duration = phantomMode ? 5 : 4;

		if (phantomMode && wearer instanceof Hero) {
			int points = ((Hero) wearer).pointsInTalent(Talent.PHANTOM_WALK);

			if (points > 0) {
				// 本次已消耗1点，就为下一段增加1回合
				duration += chargesSpentThisUse;
			}
		}

		return duration;
	}
	//斗篷的动态文本
	@Override
	public String name() {
		return phantomMode
				? Messages.get(this, "phantom_name")
				: super.name();
	}

	@Override
	public String desc() {
		return phantomMode
				? Messages.get(this, "phantom_desc")
				: super.desc();
	}
}
