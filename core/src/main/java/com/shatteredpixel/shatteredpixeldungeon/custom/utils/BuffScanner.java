package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessAnmy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessBossRushLow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessGoRead;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessGoodSTR;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessLing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessLingJing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessMixShiled;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessMobDied;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessNoDied;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessNoMoney;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessQinyue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessRedWhite;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessUnlock;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayCursed;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayKill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayMoneyMore;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayNoSTR;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSaySlowy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayTimeLast;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.ActivePoint;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.AnkhCount;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.BloodLoss;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.FoundChest;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.MagicAbsorb;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.NightorDay;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.OozeStatueDead;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.QuestGold;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.ScoreMiniGame;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.SliceDeadBless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.WhiteBlastSwordStatus;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.FireSuperDr;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.TowerParalysis;
import com.shatteredpixel.shatteredpixeldungeon.custom.buffs.AbsoluteBlindness;
import com.shatteredpixel.shatteredpixeldungeon.custom.buffs.ConsistBleeding;
import com.shatteredpixel.shatteredpixeldungeon.custom.buffs.IgnoreArmor;
import com.shatteredpixel.shatteredpixeldungeon.custom.buffs.ZeroAttack;
import com.shatteredpixel.shatteredpixeldungeon.custom.buffs.ZeroDefense;
import com.shatteredpixel.shatteredpixeldungeon.custom.ch.GameTracker;
import com.shatteredpixel.shatteredpixeldungeon.custom.ch.StrengthAndSacrifice;

import java.util.ArrayList;

public class BuffScanner {
    public static ArrayList<Class<? extends Buff>> getAllBuffClasses() {
        ArrayList<Class<? extends Buff>> buffClasses = new ArrayList<>();

        // 游戏内正常显示的Buff
        buffClasses.add(AbsoluteBlindness.class);
        buffClasses.add(AbundantMagic.class);
        buffClasses.add(ActivePoint.class);
        buffClasses.add(Adrenaline.class);
        buffClasses.add(AdrenalineSurge.class);
        buffClasses.add(Amok.class);
        buffClasses.add(AnkhCount.class);
        buffClasses.add(AntiLightShiled.class);
        buffClasses.add(ArcaneArmor.class);
        buffClasses.add(ArtifactRecharge.class);
        buffClasses.add(AscensionChallenge.class);

        buffClasses.add(Barkskin.class);
        buffClasses.add(Barrier.class);
        buffClasses.add(Berserk.class);
        buffClasses.add(Bleeding.class);
        buffClasses.add(Bless.class);
        buffClasses.add(BlessAnmy.class);
        buffClasses.add(BlessBossRushLow.class);
        buffClasses.add(BlessGoodSTR.class);
        buffClasses.add(BlessGoRead.class);
        buffClasses.add(BlessImmune.class);
        buffClasses.add(BlessLingJing.class);
        buffClasses.add(BlessMixShiled.class);
        buffClasses.add(BlessMobDied.class);
        buffClasses.add(BlessNoDied.class);
        buffClasses.add(BlessNoMoney.class);
        buffClasses.add(BlessQinyue.class);
        buffClasses.add(BlessUnlock.class);
        buffClasses.add(Blindness.class);
        buffClasses.add(BlobImmunity.class);
        buffClasses.add(BrokenArmor.class);
        buffClasses.add(Burning.class);

        buffClasses.add(Charm.class);
        buffClasses.add(Chill.class);
        buffClasses.add(Combo.class);
        buffClasses.add(ConsistBleeding.class);
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
        buffClasses.add(GreaterHaste.class);
        buffClasses.add(HaloFireImBlue.class);
        buffClasses.add(HalomethaneBurning.class);
        buffClasses.add(Haste.class);
        buffClasses.add(Healing.class);
        buffClasses.add(HeroDisguise.class);

        buffClasses.add(Hex.class);
        buffClasses.add(HoldFast.class);
        buffClasses.add(IceSwordDown.class);
        buffClasses.add(IgnoreArmor.class);
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

        buffClasses.add(MagicAbsorb.class);
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
        buffClasses.add(Nyctophobia.class);
        buffClasses.add(Ooze.class);
        buffClasses.add(OozeStatueDead.class);
        buffClasses.add(Paralysis.class);
        buffClasses.add(PhysicalEmpower.class);
        buffClasses.add(PinCushion.class);
        buffClasses.add(Poison.class);
        buffClasses.add(Preparation.class);
        buffClasses.add(PrismaticGuard.class);
        buffClasses.add(PureSoul.class);
        buffClasses.add(QuestGold.class);

        buffClasses.add(Recharging.class);
        buffClasses.add(RevealedArea.class);
        buffClasses.add(Roots.class);
        buffClasses.add(RoseShiled.class);
        buffClasses.add(SanityColdDown.class);
        buffClasses.add(ScoreMiniGame.class);
        buffClasses.add(ScrollEmpower.class);
        buffClasses.add(SendMessage.class);

        buffClasses.add(Shadows.class);

        buffClasses.add(SliceDeadBless.class);
        buffClasses.add(Slow.class);
        buffClasses.add(SmokeAlly.class);

        buffClasses.add(Smoking.class);
        buffClasses.add(SnipersMark.class);
        buffClasses.add(SoulMark.class);

        buffClasses.add(Stamina.class);
        buffClasses.add(StormCloudDied.class);
        buffClasses.add(StrengthAndSacrifice.class);
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
        buffClasses.add(ZeroAttack.class);
        buffClasses.add(ZeroDefense.class);

        // 中介隐藏的特殊Buff（无图标，仅逻辑调用）

        buffClasses.add(Awareness.class);
        buffClasses.add(BackgroundBeamCounter.class);
        buffClasses.add(BeamTowerAdbility.class);
        buffClasses.add(BlessLing.class);
        buffClasses.add(BlessRedWhite.class);
        buffClasses.add(BloodLoss.class);
        buffClasses.add(Butter.class);

        buffClasses.add(Cost.class);
        buffClasses.add(CrossTownProc.class);


        buffClasses.add(GameTracker.class);
        buffClasses.add(GravityChaosTracker.class);
        buffClasses.add(Hunger.class);
        buffClasses.add(IceHealHP.class);
        buffClasses.add(LightSan.class);

        buffClasses.add(Momentum.class);

        buffClasses.add(Regeneration.class);
        buffClasses.add(ReloadShop.class);
        buffClasses.add(ReloadShopTwo.class);

        buffClasses.add(SelectFoor.class);
        buffClasses.add(ShopLimitLock.class);
        buffClasses.add(Sleep.class);

        buffClasses.add(SuperNovaTracker.class);


        buffClasses.add(TestBatLock.class);
        buffClasses.add(TestDwarfMasterLock.class);

        buffClasses.add(Timer.class);
        buffClasses.add(TimeStasis.class);
        buffClasses.add(ToxicImbue.class);
        buffClasses.add(TrueInvisibiity.class);

        return buffClasses;
    }

}
