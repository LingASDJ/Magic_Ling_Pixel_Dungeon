package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AbundantMagic;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AdrenalineSurge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArcaneArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Berserk;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BrokenArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessAnmy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessBossRushLow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessGoRead;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessGoodSTR;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessLingJing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessMixShiled;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessMobDied;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessNoMoney;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessQinyue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessUnlock;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Combo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Daze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.DeadSoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Drowsy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.Immunities.ScaryImmunitiesBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EnhancedRings;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Foresight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Fury;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GoodLuck;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GreaterHaste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HaloFireImBlue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HeroDisguise;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HoldFast;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invulnerability;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Killer;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LanFireStats;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LethalDefense;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Levitation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LifeLink;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LighS;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayCursed;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayKill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayMoneyMore;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayNoSTR;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSaySlowy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayTimeLast;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Momentum;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MonkEnergy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PhysicalEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PinCushion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Preparation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PrismaticGuard;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RevealedArea;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RoseShiled;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ScrollEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Shadows;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SmokeAlly;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Smoking;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SnipersMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Stamina;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.StormCloudDied;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SunFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ToxicImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Venom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WandEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WaterSoulX;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WellFed;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WorstBlizzard;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.ActivePoint;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.AnkhCount;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.FoundChest;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.NightorDay;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.OozeStatueDead;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.QuestGold;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.ScoreMiniGame;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.SliceDeadBless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.WhiteBlastSwordStatus;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.Challenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.NaturesPower;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.DeathMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.Endure;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BruteBot;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Monk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ShieldHuntsman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DwarfGeneral;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DwarfSolider;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.FireDragon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.DeadDogCerberus;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.FireSuperDr;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.TowerGods;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.TowerParalysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.YogSoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync.CrivusStarFruits;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.Gorgon;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CapeOfThorns;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.hollow.PacManQuest;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLivingEarth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.DeadBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Kinetic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Crossbow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Flail;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagicTorch;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Quarterstaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RoundShield;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sai;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Scimitar;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.ForestBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd.TragicCode;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd.TreeList;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;

import java.util.ArrayList;

public class BuffScanner {
    public static ArrayList<Class<? extends Buff>> getAllBuffClasses() {
        ArrayList<Class<? extends Buff>> buffClasses = new ArrayList<>();

        buffClasses.add(ActivePoint.class);
        buffClasses.add(Adrenaline.class);
        buffClasses.add(AdrenalineSurge.class);
        buffClasses.add(Amok.class);
        buffClasses.add(AnkhCount.AnkhCountStats.class);
        buffClasses.add(ArcaneArmor.class);
        buffClasses.add(ArtifactRecharge.class);
        buffClasses.add(AscensionChallenge.class);

        buffClasses.add(Barkskin.class);
        buffClasses.add(Barrier.class);
        buffClasses.add(Bleeding.class);
        buffClasses.add(Bless.class);
        buffClasses.add(BlessBossRushLow.class);
        buffClasses.add(BlessAnmy.class);
        buffClasses.add(BlessGoodSTR.class);
        buffClasses.add(BlessGoRead.BlessGoReadStats.class);
        buffClasses.add(BlessImmune.class);
        buffClasses.add(BlessMixShiled.class);
        buffClasses.add(BlessMobDied.class);
        buffClasses.add(BlessNoMoney.class);
        buffClasses.add(BlessQinyue.class);
        buffClasses.add(BlessUnlock.class);
        buffClasses.add(Blindness.class);
        buffClasses.add(BlobImmunity.class);
        buffClasses.add(BrokenArmor.class);
        buffClasses.add(Burning.class);

        buffClasses.add(GreaterHaste.class);

        buffClasses.add(Charm.class);
        buffClasses.add(Chill.class);
        buffClasses.add(Combo.class);
        buffClasses.add(Corrosion.class);
        buffClasses.add(Corruption.class);
        buffClasses.add(Cripple.class);
        buffClasses.add(Daze.class);
        buffClasses.add(DeadSoul.class);

        buffClasses.add(Degrade.class);
        buffClasses.add(Doom.class);
        buffClasses.add(Dread.class);
        buffClasses.add(Drowsy.class);

        buffClasses.add(EnhancedRings.class);

        buffClasses.add(FireImbue.class);
        buffClasses.add(FireSuperDr.class);
        buffClasses.add(Foresight.class);
        buffClasses.add(FoundChest.class);
        buffClasses.add(Frost.class);
        buffClasses.add(FrostBurning.class);
        buffClasses.add(FrostImbue.class);
        buffClasses.add(Fury.class);
        buffClasses.add(GoodLuck.class);
        buffClasses.add(HaloFireImBlue.class);
        buffClasses.add(HalomethaneBurning.class);
        buffClasses.add(Haste.class);
        buffClasses.add(Healing.class);
        buffClasses.add(HeroDisguise.class);

        buffClasses.add(Hex.class);
        buffClasses.add(HoldFast.class);

        buffClasses.add(Invisibility.class);
        buffClasses.add(Invulnerability.class);
        buffClasses.add(Killer.class);

        buffClasses.add(LanFireStats.class);
        buffClasses.add(LethalDefense.class);
        buffClasses.add(Levitation.class);
        buffClasses.add(LifeLink.class);
        buffClasses.add(LighS.class);
        buffClasses.add(Light.class);
        buffClasses.add(LockedFloor.class);
        buffClasses.add(LostInventory.class);

        buffClasses.add(MagicalSight.class);
        buffClasses.add(MagicalSleep.class);
        buffClasses.add(MagicGirlSayCursed.class);
        buffClasses.add(MagicGirlSayKill.class);
        buffClasses.add(MagicGirlSayMoneyMore.class);
        buffClasses.add(MagicGirlSayNoSTR.class);
        buffClasses.add(MagicGirlSaySlowy.class);
        buffClasses.add(MagicGirlSayTimeLast.class);
        buffClasses.add(MagicImmune.class);
        buffClasses.add(MindVision.class);
        buffClasses.add(MonkEnergy.class);

        buffClasses.add(NightorDay.class);
        buffClasses.add(Ooze.class);
        buffClasses.add(OozeStatueDead.OozeStatueDeadStats.class);
        buffClasses.add(Paralysis.class);
        buffClasses.add(PhysicalEmpower.class);
        buffClasses.add(PinCushion.class);
        buffClasses.add(Poison.class);
        buffClasses.add(Preparation.class);
        buffClasses.add(PrismaticGuard.class);
        buffClasses.add(QuestGold.class);

        buffClasses.add(Recharging.class);
        buffClasses.add(RevealedArea.class);
        buffClasses.add(Roots.class);
        buffClasses.add(RoseShiled.class);
        buffClasses.add(ScoreMiniGame.class);
        buffClasses.add(ScrollEmpower.class);

        buffClasses.add(Shadows.class);

        buffClasses.add(SliceDeadBless.class);
        buffClasses.add(Slow.class);
        buffClasses.add(SmokeAlly.class);

        buffClasses.add(Smoking.class);
        buffClasses.add(SnipersMark.class);
        buffClasses.add(SoulMark.class);

        buffClasses.add(Stamina.class);
        buffClasses.add(StormCloudDied.class);
        buffClasses.add(SunFire.class);
        buffClasses.add(Terror.class);
        buffClasses.add(TowerParalysis.class);
        buffClasses.add(Venom.class);
        buffClasses.add(Vertigo.class);

        buffClasses.add(Vulnerable.class);
        buffClasses.add(WandEmpower.class);


        buffClasses.add(WaterSoulX.class);
        buffClasses.add(Weakness.class);
        buffClasses.add(WellFed.class);
        buffClasses.add(WhiteBlastSwordStatus.class);

        buffClasses.add(WorstBlizzard.class);
        buffClasses.add(Hunger.class);

        buffClasses.add(Momentum.class);
        buffClasses.add(ToxicImbue.class);

        //法杖组
        buffClasses.add(WandOfLivingEarth.RockArmor.class);
        buffClasses.add(WandOfMagicMissile.MagicCharge.class);
        buffClasses.add(AbundantMagic.class);

        //天赋
        buffClasses.add(Talent.PreciseAssaultTracker.class);
        buffClasses.add(Talent.LiquidAgilACCTracker.class);
        buffClasses.add(MonkEnergy.MonkAbility.Focus.FocusBuff.class);
        buffClasses.add(Talent.PatientStrikeTracker.class);

        //神器组
        buffClasses.add(TimekeepersHourglass.timeFreezeStats.class);
        buffClasses.add(TalismanOfForesight.ForesightStats.class);
        buffClasses.add(CapeOfThorns.ThornsStats.class);
        buffClasses.add(CloakOfShadows.cloakStealthStats.class);

        //植物组
        buffClasses.add(Swiftthistle.TimeBubble.class);

        //Boss组
        buffClasses.add(CrivusStarFruits.Rage.class);
        buffClasses.add(FireDragon.ToxicGasEffect.class);
        buffClasses.add(FireDragon.HasteEffect.class);
        buffClasses.add(FireDragon.DamageUpEffect.class);
        buffClasses.add(FireDragon.VertigoEffect.class);
        buffClasses.add(FireDragon.BleedingEffect.class);
        buffClasses.add(DwarfSolider.Focus.class);
        buffClasses.add(DwarfGeneral.MagicAttack.class);
        buffClasses.add(DwarfGeneral.NoHealDied.class);
        buffClasses.add(DeadDogCerberus.CerberusBless.class);
        buffClasses.add(DeadDogCerberus.SuperAttack.class);
        buffClasses.add(DeadDogCerberus.NoArmorDamage_BleedingNomalAttack.class);
        buffClasses.add(DeadDogCerberus.SoulDead.class);
        buffClasses.add(PacManQuest.AntiAttack.class);
        buffClasses.add(TowerGods.AttackUP_Palf.class);
        buffClasses.add(YogSoul.AttackDamageMagic.class);

        //武器组
        buffClasses.add(TreeList.TreeBarrier.class);
        buffClasses.add(Crossbow.ChargedShot.class);
        buffClasses.add(ForestBow.ChargedShot.class);
        buffClasses.add(Flail.SpinAbilityTracker.class);
        buffClasses.add(Sai.ComboStrikeTrackerStats.class);
        buffClasses.add(Scimitar.SwordDance.class);
        buffClasses.add(Quarterstaff.DefensiveStance.class);
        buffClasses.add(RingOfForce.BrawlersStanceStats.class);
        buffClasses.add(TragicCode.CleaveTracker.class);
        buffClasses.add(RoundShield.GuardTracker.class);
        buffClasses.add(MagicTorch.MagicLight.class);


        //MiSC
        buffClasses.add(ElixirOfMight.HTBoost.class);
        buffClasses.add(Blocking.BlockBuff.class);
        buffClasses.add(Kinetic.ConservedDamage.class);
        buffClasses.add(DeadBomb.TargetDead.class);
        buffClasses.add(Berserk.class);
        buffClasses.add(PhysicalEmpower.class);
        buffClasses.add(ScaryImmunitiesBuff.class);

        //护甲战技
        buffClasses.add(Challenge.DuelParticipant.class);
        buffClasses.add(Endure.EndureTrackerStats.class);
        buffClasses.add(NaturesPower.naturesPowerTracker.class);
        buffClasses.add(DeathMark.DeathMarkTracker.class);

        //其他
        buffClasses.add(BlessLingJing.XSBarrier.class);
        buffClasses.add(BlessLingJing.class);

        //英雄祝福
        buffClasses.add(ChampionHero.Light.class);
        buffClasses.add(ChampionHero.Giant.class);
        buffClasses.add(ChampionHero.Growing.class);
        buffClasses.add(ChampionHero.Halo.class);
        buffClasses.add(ChampionHero.Blazing.class);
        buffClasses.add(ChampionHero.Projecting.class);
        buffClasses.add(ChampionHero.Blessed.class);
        buffClasses.add(ChampionHero.AntiMagic.class);

        //怪物的所有精英与突变
        buffClasses.add(ChampionEnemy.Blazing.class);
        buffClasses.add(ChampionEnemy.Projecting.class);
        buffClasses.add(ChampionEnemy.AntiMagic.class);
        buffClasses.add(ChampionEnemy.Giant.class);
        buffClasses.add(ChampionEnemy.Blessed.class);
        buffClasses.add(ChampionEnemy.Growing.class);
        buffClasses.add(ChampionEnemy.Halo.class);
        buffClasses.add(ChampionEnemy.DelayMob.class);
        buffClasses.add(ChampionEnemy.King.class);

        buffClasses.add(ChampionEnemy.Small.class);
        buffClasses.add(ChampionEnemy.Bomber.class);
        buffClasses.add(ChampionEnemy.Middle.class);
        buffClasses.add(ChampionEnemy.Big.class);
        buffClasses.add(ChampionEnemy.Sider.class);
        buffClasses.add(ChampionEnemy.LongSider.class);
        buffClasses.add(ChampionEnemy.HealRight.class);

        buffClasses.add(ChampionEnemy.AloneCity.class);

        //敌人各种效果
        buffClasses.add(Monk.Focus.class);
        buffClasses.add(BruteBot.Focus.class);
        buffClasses.add(ShieldHuntsman.Focus.class);
        buffClasses.add(Gorgon.Petrification.class);
        buffClasses.add(Charm.CharmLing.class);
        return buffClasses;
    }

}
