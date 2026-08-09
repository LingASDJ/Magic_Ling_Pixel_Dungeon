package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CapeOfThorns extends Artifact {

	public static final String AC_THORNS = "THORNS";
	public static final String AC_THORNSCANCEL = "THORNS_CANCEL";

	{
		image = ItemSpriteSheet.ARTIFACT_CAPE;

		levelCap = 10;

		charge = 0;
		chargeCap = 100;
		cooldown = 0;

		defaultAction = AC_THORNS;
	}

	@Override
	public String defaultAction() {
		CapeOfThorns.ThornsTime thornst = hero.buff( CapeOfThorns.ThornsTime.class );
		if(thornst != null){
			return AC_THORNSCANCEL;
		} else {
			return AC_THORNS;
		}
	}

	@Override
	public ItemSprite.Glowing glowing() {
		int[] color = {0x660000,0x00CC00,0xCCCC00,0xF78C6C,0x89DDFF,0x147DAD,0xC3E88D,0xEE9D22,0xBB50A8,0xFF0000};

		if(charge >= 100){
			return new ItemSprite.Glowing( 0xFF0000, 0.75f );
		} else if( charge >= 10){
			return new ItemSprite.Glowing( color[charge/10], 0.75f );
		}
		return super.glowing();
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new Thorns();
	}

	public ArrayList<String> actions(Hero hero ) {
		ArrayList<String> actions = super.actions(hero);
		CapeOfThorns.ThornsTime thorns = hero.buff( CapeOfThorns.ThornsTime.class );
		if(thorns != null){
			actions.add(AC_THORNSCANCEL);
		} else {
			actions.remove(AC_THORNSCANCEL);
		}
		actions.add(AC_THORNS);
		return actions;
	}

	@Override
	public String desc() {
		String desc = super.desc();

		if (isEquipped (Dungeon.hero)){
			if (cursed)
				desc +=  "\n\n"+Messages.get(this, "desc_cursed");
		}
		return desc;
	}

	@Override
	public void execute( Hero hero, String action ) {

		super.execute(hero, action);

		CapeOfThorns.ThornsTime thornst = hero.buff( CapeOfThorns.ThornsTime.class );

		if (hero.buff(MagicImmune.class) != null) return;

		if(action.equals(AC_THORNSCANCEL)){
			if(thornst != null){
				GLog.p(Messages.get(this,"thorns_cancel"));
				thornst.detach();
			}
		}

		CapeOfThorns.Thorns thorns = hero.buff( CapeOfThorns.Thorns.class );
		if (action.equals( AC_THORNS )) {
			if(!isEquipped(hero)) {
				GLog.w(Messages.get(this, "thorns_equip"));
			}
			if (thorns != null) {
				if(cursed) {
					GLog.n(Messages.get(this, "thorns_cursed"));
				} else if(charge >= 10 && hero.buff(ThornsTime.class) == null){
					Buff.affect(hero,ThornsTime.class);
					int[] color = {0x660000,0x00CC00,0xCCCC00,0xF78C6C,0x89DDFF,0x147DAD,0xC3E88D,0xEE9D22,0xBB50A8,0xFF0000};
					if(charge >= 100){
						GameScene.flash(0xFF0000,true);
					} else if( charge >= 10){
						GameScene.flash(color[charge/10],true);
					}
					updateQuickslot();
					hero.spend( Actor.TICK );
					hero.busy();
					hero.sprite.operate( curUser.pos );
					GLog.p(Messages.get(this,"thorns"));
					Buff.detach(hero, Invisibility.class);
				} else if(charge < 10) {
					GLog.w(Messages.get(this, "thorns_wait"));
				} else {
					GLog.w(Messages.get(this, "thorns_active"));
				}
			}
		}
	}

	@Override
	public void charge(Hero target, float amount) {
		if (cooldown == 0) {
			charge += Math.round(4*amount);
			if (charge >= chargeCap) {
				charge = chargeCap;
				partialCharge = 0;
			}
			updateQuickslot();
		}
	}

	public static class ThornsTime extends Buff {
		{
			type = buffType.POSITIVE;
		}

		public Artifact getArtifact = null;

		int turnsToCost = 0;

		public float damageReductionPercent() {
			Artifact art = getEquippedArtifact();
			if (art == null) return 0f;

			int LV = art.level();
			float minPct = (20f + 2f * LV) / 100f;
			float maxPct = (30f + 4f * LV) / 100f;

			maxPct = Math.min(maxPct, 0.99f);
			if (minPct > maxPct) minPct = maxPct;

			return Random.Float(minPct, maxPct);
		}

		// 动态获取当前装备的荆棘斗篷
		public Artifact getEquippedArtifact(){
			if (getArtifact != null && getArtifact.isEquipped(Dungeon.hero)) return getArtifact;
			if (Dungeon.hero != null){
				Artifact art = Dungeon.hero.belongings.artifact();
				Item miscItem = Dungeon.hero.belongings.misc();
				if (art instanceof CapeOfThorns) {
					getArtifact = art;
				} else if (miscItem instanceof CapeOfThorns) {
					getArtifact = (Artifact) miscItem;
				} else {
					getArtifact = null;
				}
			}
			return getArtifact;
		}


		@Override
		public boolean attachTo(Char target) {
			if (super.attachTo(target)) {
				applyBleedingToNearby();
				return true;
			}
			return false;
		}

		private void applyBleedingToNearby() {
			Artifact art = getEquippedArtifact();
			if (art == null || target == null) return;

			int LV = art.level();
			int radius = Math.round(LV / 4f);

			if (radius <= 0) return;

			int bleedTurns = (int) Math.floor(1.5f * LV);
			if (bleedTurns <= 0) return;

			int centerPos = target.pos;
			int width = Dungeon.level.width();

			for (int dy = -radius; dy <= radius; dy++) {
				for (int dx = -radius; dx <= radius; dx++) {

					double distance = Math.sqrt(dx * dx + dy * dy);

					if (distance <= radius) {

						int offset = dy * width + dx;
						int pos = centerPos + offset;

						if (pos < 0 || pos >= Dungeon.level.length()) continue;
						if (dx < 0 && (centerPos % width == 0)) continue;
						if (dx > 0 && ((centerPos + 1) % width == 0)) continue;
						if (!Dungeon.level.passable[pos] && !Dungeon.level.avoid[pos]) continue;

						Char ch = Actor.findChar(pos);
						if (ch != null && ch.alignment != Char.Alignment.ALLY && ch.isAlive()) {

							float distanceFactor = (float) (1.0 - distance / radius);
							int actualBleedTurns = Math.max(1, Math.round(bleedTurns * distanceFactor));

							Bleeding existingBleeding = ch.buff(Bleeding.class);
							if (existingBleeding != null) {
								float newCooldown = Math.max(existingBleeding.visualcooldown(), actualBleedTurns);
								existingBleeding.detach();
								Buff.affect(ch, Bleeding.class).set(newCooldown);
							} else {
								Buff.affect(ch, Bleeding.class).set(actualBleedTurns);
							}
							ch.damage(Math.round(art.level()/2f), this, Char.DamageType.PHYSICAL);
						}
					}
				}
			}
		}

		@Override
		public boolean act(){
			Artifact art = getEquippedArtifact();
			if(art != null){
				turnsToCost--;

				if (turnsToCost <= 0){
					art.charge -= 5;
					if (art.charge < 1) {
						art.charge = 0;
						detach();
						GLog.w(Messages.get(this, "no_charge"));
						((Hero) target).interrupt();
					} else {
						int lvlDiffFromTarget = ((Hero) target).lvl - (1+art.level()*2);
						if (art.level() >= 7){
							lvlDiffFromTarget -= art.level()-6;
						}
						if (lvlDiffFromTarget >= 0){
							art.exp += (int) Math.round(5f * Math.pow(1.1f, lvlDiffFromTarget));
						} else {
							art.exp += (int) Math.round(5f * Math.pow(0.75f, -lvlDiffFromTarget));
						}

						if (art.exp >= (art.level() + 1) * 50 && art.level() < art.levelCap) {
							art.upgrade();
							Catalog.countUse(CapeOfThorns.class);
							art.exp -= art.level() * 50;
							GLog.p(Messages.get(this, "levelup"));
						}
						turnsToCost = 1;
					}
					updateQuickslot();
				}
			} else {
				detach();
			}

			applyBleedingToNearby();

			spend(TICK);
			return true;
		}

		@Override
		public String desc() {
			Artifact art = getEquippedArtifact();
			return Messages.get(this, "desc",
					art != null ? 20+art.level()*2 : 20,
					art != null ? 30+art.level()*4 : 30,
					art != null ? art.level()/2 : 0,
					dispTurns(art != null ? (float) art.charge /5 : 0));
		}

		@Override
		public int icon() {
			return BuffIndicator.THORNS;
		}

		private static final String TURNSTOCOST = "turnsToCost";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put( TURNSTOCOST , turnsToCost);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			turnsToCost = bundle.getInt( TURNSTOCOST );
		}

	}

	public static class ThornsStats extends Buff {
		{
			type = buffType.POSITIVE;
		}
		@Override
		public int icon() {
			return BuffIndicator.THORNS;
		}
	}

	public class Thorns extends ArtifactBuff{

		@Override
		public boolean act(){
			if (charge < chargeCap && !cursed && target.buff(MagicImmune.class) == null && target.buff(ThornsTime.class) == null) {

				if (activeBuff == null && Regeneration.regenOn()) {
					partialCharge += 0.25f * RingOfEnergy.artifactChargeMultiplier(target);
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

			updateQuickslot();

			spend(TICK);
			return true;
		}

		@Override
		public void charge(Hero target, float amount) {
			if (cursed || target.buff(MagicImmune.class) != null) return;
			if (charge < chargeCap) {
				partialCharge += amount;
				while (partialCharge >= 1f){
					charge+=4;
					partialCharge--;
				}
				if (charge >= chargeCap) {
					charge = chargeCap;
					partialCharge = 0;
				}
				updateQuickslot();
			}
		}

		public int proc(int damage, Char attacker){

			if (!cursed && target.buff(MagicImmune.class) == null) {
				if (attacker != null) {
					attacker.damage(damage, this, Char.DamageType.REAL);
					Buff.append(attacker, Bleeding.class).set(level());
				}
			}

			updateQuickslot();
			return damage;
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", dispTurns(cooldown));
		}

		@Override
		public void detach(){
			cooldown = 0;
			charge = 0;
			super.detach();
		}

		public void onDamageTaken(int damage) {
			if (charge < chargeCap && !cursed && target.buff(MagicImmune.class) == null) {
				float chargeToAdd = damage / 2f;
				partialCharge += chargeToAdd;

				while (partialCharge >= 1) {
					charge++;
					partialCharge -= 1;
					if (charge == chargeCap){
						partialCharge = 0;
					}
				}
				updateQuickslot();
			}
		}

	}



	public static class HeroThorns extends FlavourBuff {

		public int proc(int damage, Char attacker, Char defender){

			try {
				int deflected = Math.round(attacker.HT*0.09f);
				int deflectedHigh = Math.round(attacker.HT*0.12f);

				if(defender.isAlive()){
					if (hero.pointsInTalent(Talent.IRON_STOMACH) == 1){
						attacker.damage(deflected, this, Char.DamageType.REAL);
					} else if(hero.pointsInTalent(Talent.IRON_STOMACH) == 2) {
						attacker.damage(deflectedHigh, this, Char.DamageType.REAL);
					}
				}
			} catch (Exception e) {
				return 0;
			}

			return damage;
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", dispTurns());
		}

		@Override
		public int icon() {
			return BuffIndicator.THORNS;
		}

	}


}
