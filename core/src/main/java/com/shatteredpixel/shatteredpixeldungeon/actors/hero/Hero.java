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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.AQUAPHOBIA;
import static com.shatteredpixel.shatteredpixeldungeon.Challenges.CS;
import static com.shatteredpixel.shatteredpixeldungeon.Challenges.DHXD;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.branch;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;
import static com.shatteredpixel.shatteredpixeldungeon.SPDSettings.HelpSettings;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.bossRushMode;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.gameNight;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.gameTime;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.lanterfireactive;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.seedCustom;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.youNoItem;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.zeroItemLevel;
import static com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel.holiday;
import static java.lang.Math.min;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SacrificialFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AdrenalineSurge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Awareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Berserk;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessAnmy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessBossRushLow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessGoRead;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessGoodSTR;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessLing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessLingJing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessMixShiled;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessMobDied;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessNoMoney;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessQinyue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessRedWhite;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessUnlock;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Combo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.DeadSoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Drowsy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff.ScaryBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.DamageBuff.ScaryDamageBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.Immunities.ScaryImmunitiesBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Foresight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostImbueEX;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GreaterHaste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HasteLing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HellBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HoldFast;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.InvisibilityRing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invulnerability;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LanFireStats;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Levitation;
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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Momentum;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MonkEnergy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Nyctophobia;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PhysicalEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PropBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RoseShiled;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SnipersMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WaterSoulX;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.AncientStats;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.BloodLoss;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.MagicAbsorb;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.NightorDay;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.OozeStatueDead;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.QuestGold;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.ScoreBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.SliceDeadBless;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.Challenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.ElementalStrike;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.NaturesPower;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.Endure;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spellsoword.MagicPower;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BloodBat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Monk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Snake;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.galaxy.ServantAvgomon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.galaxy.Sothoth;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.galaxy.SothothEyeDied;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.galaxy.SothothLasher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.DeadDogCerberus;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.Nyarlathotep;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.lb.BlackSoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MageHand;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.WhiteLingLand;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.BzmdrNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.normal.DogDogMusic;
import com.shatteredpixel.shatteredpixeldungeon.custom.ch.GameTracker;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.CustomPlayer;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CheckedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap.Type;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.KindofMisc;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AlowGlyph.AncityStone;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Brimstone;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CapeOfThorns;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ElectricalSmoke;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EtherealChains;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MasterThievesArmband;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.journal.Guidebook;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.BlackKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GreenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.Key;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.items.lightblack.OilLantern;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDivineInspiration;
import com.shatteredpixel.shatteredpixeldungeon.items.props.ArmorScalesOfBzmdr;
import com.shatteredpixel.shatteredpixeldungeon.items.props.BrokenRing;
import com.shatteredpixel.shatteredpixeldungeon.items.props.CatGirlCosplay;
import com.shatteredpixel.shatteredpixeldungeon.items.props.CloakFragmentsOfBzmdr;
import com.shatteredpixel.shatteredpixeldungeon.items.props.DeadOrAlive;
import com.shatteredpixel.shatteredpixeldungeon.items.props.EmotionalAggregation;
import com.shatteredpixel.shatteredpixeldungeon.items.props.EmotionalAggregationB;
import com.shatteredpixel.shatteredpixeldungeon.items.props.FaintGlimmer;
import com.shatteredpixel.shatteredpixeldungeon.items.props.HeartOfCrystalFractal;
import com.shatteredpixel.shatteredpixeldungeon.items.props.HellButterfly;
import com.shatteredpixel.shatteredpixeldungeon.items.props.KnightStabbingSword;
import com.shatteredpixel.shatteredpixeldungeon.items.props.Monocular;
import com.shatteredpixel.shatteredpixeldungeon.items.props.NoteOfBzmdr;
import com.shatteredpixel.shatteredpixeldungeon.items.props.PortableWhetstone;
import com.shatteredpixel.shatteredpixeldungeon.items.props.Prop;
import com.shatteredpixel.shatteredpixeldungeon.items.props.PureRouge;
import com.shatteredpixel.shatteredpixeldungeon.items.props.StarDust;
import com.shatteredpixel.shatteredpixeldungeon.items.props.StarSachet;
import com.shatteredpixel.shatteredpixeldungeon.items.props.TerrorDoll;
import com.shatteredpixel.shatteredpixeldungeon.items.props.TerrorDollB;
import com.shatteredpixel.shatteredpixeldungeon.items.props.WenStudyingPaperOne;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.AnySkinSelect;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DarkGold;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DevItem.CrystalLing;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Empty;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.LanFireGo;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.LingJing;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MIME;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Pickaxe;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Red;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.RedWhiteRose;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.SmallLightHeader;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.UnlessFlower;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.hollow.PacManQuest;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfAccuracy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEvasion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfFuror;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfTenacity;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfChallenge;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.extra.ScrollOfSoul;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.extra.ScrollOfTeleTation;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ThirteenLeafClover;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfAnmy;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLivingEarth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfSun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Chilling;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Crossbow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Flail;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagicTorch;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RoundShield;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sai;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Scimitar;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.ForestBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd.Break;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd.TreeList;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.AncientMysteryCityBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.AncientMysteryCityLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.MiningLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.NewZeroFiveLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.NormalZeroFiveLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.UnlessEndFlowerLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.hollow.MorpheusBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.hollow.MoveBoxHollowActorLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.minilevels.DragonFestivalMiniLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.WeakFloorRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.BigEyeRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ShadowCaster;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.AlchemyScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SunSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.StatusPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WinAllSearchStatus;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndHero;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndIceTradeItem;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndResurrect;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndRushTradeItem;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndStory;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTradeItem;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.Delayer;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Objects;

public class Hero extends Char {

	public boolean isClass(HeroClass clazz){
		if (heroClass == HeroClass.ROGUE) return true;
		return clazz == this.heroClass;
	}

	//damage rolls that come from the hero can have their RNG influenced
	public static int heroDamageIntRange(int min, int max ){
		if (Random.Float() < ThirteenLeafClover.combatDistributionInverseChance()){
			return ThirteenLeafClover.invCombatRoll(min, max);
		} else {
			return Random.NormalIntRange(min, max);
		}
	}

	public boolean isSubclass(HeroSubClass subClass) {
		if (this.subClass == HeroSubClass.ASSASSIN || this.subClass == HeroSubClass.FREERUNNER) return true;
		return subClass == this.subClass;
	}

	public ArrayList<Mob> visibleEnemiesList() {
		return visibleEnemies;
	}

	{
		actPriority = HERO_PRIO;

		alignment = Alignment.ALLY;
	}

	public static final int MAX_LEVEL = 30;

	public static final int STARTING_STR = 10;

	private static final float TIME_TO_REST		    = 1f;
	private static final float TIME_TO_SEARCH	    = 2f;
	private static final float HUNGER_FOR_SEARCH	= 6f;

	public HeroClass heroClass = HeroClass.ROGUE;
	public HeroSubClass subClass = HeroSubClass.NONE;
	public ArmorAbility armorAbility = null;
	public ArrayList<LinkedHashMap<Talent, Integer>> talents = new ArrayList<>();
	public LinkedHashMap<Talent, Talent> metamorphedTalents = new LinkedHashMap<>();

	public int attackSkill = 10;
	private int defenseSkill = 5;

	public boolean ready = false;
	public boolean damageInterrupt = true;
	public HeroAction curAction = null;
	public HeroAction lastAction = null;

	private Char enemy;

	private static final String CLASS = "class";
	private static final String SUBCLASS = "subClass";
	private static final String ABILITY = "armorAbility";

	public int STR;

	public float awareness;

	private int resistHealth = 0;
	private int originalHT = 20;
    private boolean chargeAnmy = false;

	public int lvl = 1;
	public int chargesUsed = 0;
	private static final String ATTACK = "attackSkill";
	private static final String DEFENSE = "defenseSkill";
	private static final String STRENGTH = "STR";
	private static final String LEVEL = "lvl";
	private static final String EXPERIENCE = "exp";
	private static final String HTBOOST = "htboost";
	private static final String LANTERFTR = "lanterfire";
	private static final String ICEHP = "icehp";
	private static final String RESIST = "resistHealth";
	private static final String CHARGES = "chargesUsed";

	public void updateHT( boolean boostHP ){
		int curHT = HT;

		originalHT = 20 + 5*(lvl-1);

		HT = 20 + 5*(lvl-1) + HTBoost;
		float multiplier = RingOfMight.HTMultiplier(this);
		HT = Math.round(multiplier * HT);

		if (buff(ElixirOfMight.HTBoost.class) != null){
			HT += buff(ElixirOfMight.HTBoost.class).boost();
		}

		if (boostHP){
			HP += Math.max(HT - curHT, 0);
		}

		HT -= resistHealth;

		HP = min(HP, HT);
	}

	public int STR() {
		int strBonus = 0;

		strBonus += RingOfMight.strengthBonus( this );

		AdrenalineSurge buff = buff(AdrenalineSurge.class);
		if (buff != null){
			strBonus += buff.boost();
		}

		//无力 本大层-3力量 : 坚毅 本大层力量+2
		if(hero.buff(MagicGirlSayNoSTR.class) != null){
			strBonus -= 1;
		} else if(hero.buff(BlessGoodSTR.class) != null) {
			strBonus += 2;
		}

		BzmdrNewYears.BzmdrGift bzmdrGift = hero.belongings.getItem(BzmdrNewYears.BzmdrGift.class);
		if(bzmdrGift != null){
			strBonus += Statistics.BzmdrCJHeroSTR;
		}

		if(hero.buff(SliceDeadBless.class)!=null && Dungeon.depth>29){
			strBonus += 3;
		}

		if(hero.buff(BlessRedWhite.class) != null) {
			strBonus += 2;
		}

		if(hero.buff(ScaryDamageBuff.class) != null) {
			strBonus -= 4;
		}

		ScaryBuff scaryBuff = hero.buff(ScaryBuff.class);
		if(scaryBuff != null){
			if(scaryBuff.Scary>80){
				strBonus -= 2;
			}
		}


		return STR + strBonus;
	}
	private static final String CAKEUSED = "cakeused";
	public boolean resting = false;
	public Belongings belongings;
	public int exp = 0;

	public boolean withElectricalSmoke = false;

	public int HTBoost = 0;
	//This list is maintained so that some logic checks can be skipped
	// for enemies we know we aren't seeing normally, resulting in better performance
	public ArrayList<Mob> mindVisionEnemies = new ArrayList<>();
	//灯火前行
	public int lanterfire;
	public int icehp;
	//蛋糕
	public int CakeUsed;
	private String name = SPDSettings.heroName();
	private ArrayList<Mob> visibleEnemies;
	//effectively cache this buff to prevent having to call buff(...) a bunch.
	//This is relevant because we call isAlive during drawing, which has both performance
	//and thread coordination implications if that method calls buff(...) frequently
	private Berserk berserk;

	public Hero() {
		super();

		HP = HT = 20;
		STR = STARTING_STR;

		belongings = new Belongings(this);

		visibleEnemies = new ArrayList<>();
	}

	public static void reallyDie( Object cause ) {

		int length = level.length();
		int[] map = level.map;
		boolean[] visited = level.visited;
		boolean[] discoverable = level.discoverable;

		for (int i=0; i < length; i++) {

			int terr = map[i];

			if (discoverable[i]) {

				visited[i] = true;
				if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {
					level.discover( i );
				}
			}
		}

		Bones.leave();

		Dungeon.observe();
		GameScene.updateFog();

		hero.belongings.identify();

		int pos = hero.pos;

		ArrayList<Integer> passable = new ArrayList<>();
		for (Integer ofs : PathFinder.NEIGHBOURS8) {
			int cell = pos + ofs;
			if ((level.passable[cell] || level.avoid[cell]) && level.heaps.get( cell ) == null) {
				passable.add( cell );
			}
		}
		Collections.shuffle( passable );

		ArrayList<Item> items = new ArrayList<>(hero.belongings.backpack.items);
		for (Integer cell : passable) {
			if (items.isEmpty()) {
				break;
			}

			Item item = Random.element( items );
			level.drop( item, cell ).sprite.drop( pos );

			items.remove( item );
		}

		for (Char c : Actor.chars()){
			if (c instanceof DriedRose.GhostHero){
				((DriedRose.GhostHero) c).sayHeroKilled();
			}
		}

		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				GameScene.gameOver();
				Sample.INSTANCE.play( Assets.Sounds.DEATH );
			}
		});

		if (cause instanceof Doom) {
			((Doom)cause).onDeath();
		}

		Dungeon.deleteGame(GamesInProgress.curSlot, true);
	}

	public static void preview( GamesInProgress.Info info, Bundle bundle ) {
		info.level = bundle.getInt( LEVEL );
		info.str = bundle.getInt( STRENGTH );
		info.exp = bundle.getInt( EXPERIENCE );
		info.hp = bundle.getInt( Char.TAG_HP );
		info.ht = bundle.getInt( Char.TAG_HT );
		info.shld = bundle.getInt( Char.TAG_SHLD );
		info.heroClass = bundle.getEnum( CLASS, HeroClass.class );
		info.subClass = bundle.getEnum( SUBCLASS, HeroSubClass.class );
		Belongings.preview( info, bundle );

		info.name = bundle.contains("name") ? bundle.getString("name") : "";
	}

	public boolean hasTalent( Talent talent ){
		return pointsInTalent(talent) > 0;
	}

	public int pointsInTalent( Talent talent ){
		for (LinkedHashMap<Talent, Integer> tier : talents){
			for (Talent f : tier.keySet()){
				if (f == talent) return tier.get(f);
			}
		}
		return 0;
	}

	public void upgradeTalent( Talent talent ){
		for (LinkedHashMap<Talent, Integer> tier : talents){
			for (Talent f : tier.keySet()){
				if (f == talent) tier.put(talent, tier.get(talent)+1);
			}
		}
		Talent.onTalentUpgraded(this, talent);
	}

	public int talentPointsSpent(int tier){
		int total = 0;
		for (int i : talents.get(tier-1).values()){
			total += i;
		}
		return total;
	}

	public int talentPointsAvailable(int tier){
		if (lvl < (Talent.tierLevelThresholds[tier] - 1)
				|| (tier == 3 && subClass == HeroSubClass.NONE)
				|| (tier == 4 && armorAbility == null)) {
			return 0;
		} else if (lvl >= Talent.tierLevelThresholds[tier+1]){
			return Talent.tierLevelThresholds[tier+1] - Talent.tierLevelThresholds[tier] - talentPointsSpent(tier) + bonusTalentPoints(tier);
		} else {
			return 1 + lvl - Talent.tierLevelThresholds[tier] - talentPointsSpent(tier) + bonusTalentPoints(tier);
		}
	}

	public int bonusTalentPoints(int tier){
		if (lvl < (Talent.tierLevelThresholds[tier]-1)
				|| (tier == 3 && subClass == HeroSubClass.NONE)
				|| (tier == 4 && armorAbility == null)) {
			return 0;
		} else if (buff(PotionOfDivineInspiration.DivineInspirationTracker.class) != null
				&& buff(PotionOfDivineInspiration.DivineInspirationTracker.class).isBoosted(tier)) {
			return 2;
		} else {
			return 0;
		}
	}

	public String className() {
		HeroSubClass heroSubClass = this.subClass;
		return (heroSubClass == null || heroSubClass == HeroSubClass.NONE) ? this.heroClass.title() : heroSubClass.title();
	}


	@Override
	public String name() {
		return this.name.equals("") ? className() : this.name;
	}

	@Override
	public void hitSound(float pitch) {
		if (!RingOfForce.fightingUnarmed(this)) {
			belongings.attackingWeapon().hitSound(pitch);
		} else if (RingOfForce.getBuffedBonus(this, RingOfForce.Force.class) > 0) {
			//pitch deepens by 2.5% (additive) per point of strength, down to 75%
			super.hitSound( pitch * GameMath.gate( 0.75f, 1.25f - 0.025f*STR(), 1f) );
		} else {
			super.hitSound(pitch * 1.1f);
		}
	}

	@Override
	public boolean blockSound(float pitch) {
		if ( belongings.weapon() != null && belongings.weapon().defenseFactor(this) >= 4 ){
			Sample.INSTANCE.play( Assets.Sounds.HIT_PARRY, 1, pitch);
			return true;
		}
		return super.blockSound(pitch);
	}

	public void live() {
		for (Buff b : buffs()){
			if (!b.revivePersists) b.detach();
		}
		Buff.affect( this, Regeneration.class );
		Buff.affect( this, Hunger.class );

		if(Statistics.RandMode){
			Buff.affect(this, QuestGold.class).set((100), 1);
		}

		ArrayList<Prop> AllProps = hero.belongings.getAllItems(Prop.class);
		if(AllProps!=null){
			if(Dungeon.isDLC(Conducts.Conduct.HARD)){
				Buff.affect(hero, PropBuff.class);
			}
		}

		if(HelpSettings()) {
			Buff.affect(this, GameTracker.class);
		}

		if( lanterfireactive || Dungeon.isChallenged(DHXD)){
			Buff.affect( this, Nyctophobia.class );
			Buff.affect( this, LanFireStats.class );
			if(Dungeon.depth != 0){
				Buff.affect( this, LighS.class );
			}
		}

		if(Dungeon.isChallenged(CS)){
			Buff.affect( this, NightorDay.class ).set((100), 1);
		}

		//春游模式
		if(Statistics.difficultyDLCEXLevel == 1){
			Buff.affect(hero, BlessBossRushLow.class, ChampionHero.DURATION*123456f);
		}

		/** 魔剑士 **/
		if(hero.heroClass == HeroClass.SPELLSWORD){
			Buff.affect(this, MagicPower.class).set(20f,20f);
		}

	}

	public int tier() {
		Armor armor = belongings.armor();

		//TODO 临时皮肤策略
		switch (hero.heroClass.GetSkin()){
			case 1:
				return 9;
			case 2:
				return 11;
			case 3:
				return 10;
			case 4:
				return 13;
			case 5:
				return 14;
		}

		if (armor instanceof ClassArmor){
			return 7;
		} else if (armor != null){
			return armor.tier;
		} else {
			return 0;
		}
	}

	public boolean shoot( Char enemy, MissileWeapon wep ) {

		this.enemy = enemy;
		boolean wasEnemy = enemy.alignment == Alignment.ENEMY
				|| (enemy instanceof Mimic && enemy.alignment == Alignment.NEUTRAL);

		//temporarily set the hero's weapon to the missile weapon being used
		//TODO improve this!
		belongings.thrownWeapon = wep;
		boolean hit = attack( enemy );
		Invisibility.dispel();
		belongings.thrownWeapon = null;

		if (hit && subClass == HeroSubClass.GLADIATOR && wasEnemy){
			Buff.affect( this, Combo.class ).hit(enemy);
		}

		if (hit && heroClass == HeroClass.DUELIST && wasEnemy){
			Buff.affect( this, Sai.ComboStrikeTracker.class).addHit();
		}

		return hit;
	}

	@Override
	public int attackSkill( Char target ) {
		KindOfWeapon wep = belongings.attackingWeapon();

		float accuracy = 1;

		if( Dungeon.isDLC(Conducts.Conduct.DEV) && CustomPlayer.overrideGame && !CustomPlayer.shouldOverride ){
			accuracy = CustomPlayer.baseAccuracy;
		}

		accuracy *= RingOfAccuracy.accuracyMultiplier( this );

		//precise assault and liquid agility
		if (!(wep instanceof MissileWeapon)) {
			if ((hasTalent(Talent.PRECISE_ASSAULT) || hasTalent(Talent.LIQUID_AGILITY))
					//does not trigger on ability attacks
					&& belongings.abilityWeapon != wep && buff(MonkEnergy.MonkAbility.UnarmedAbilityTracker.class) == null){

				//non-duelist benefit for precise assault, can stack with liquid agility
				if (heroClass != HeroClass.DUELIST) {
					//persistent +10%/20%/30% ACC for other heroes
					accuracy *= 1f + 0.1f * pointsInTalent(Talent.PRECISE_ASSAULT);
				}

				if (wep instanceof Flail && buff(Flail.SpinAbilityTracker.class) != null){
					//do nothing, this is not a regular attack so don't consume talent fx
				} else if (wep instanceof Crossbow && buff(Crossbow.ChargedShot.class) != null || wep instanceof ForestBow && buff(ForestBow.ChargedShot.class) != null){
					//do nothing, this is not a regular attack so don't consume talent fx
				} else if (buff(Talent.PreciseAssaultTracker.class) != null) {
					// 2x/5x/inf. ACC for duelist if she just used a weapon ability
					switch (pointsInTalent(Talent.PRECISE_ASSAULT)){
						default: case 1:
							accuracy *= 2; break;
						case 2:
							accuracy *= 5; break;
						case 3:
							accuracy *= Float.POSITIVE_INFINITY;
							break;
					}
					buff(Talent.PreciseAssaultTracker.class).detach();
				} else if (buff(Talent.LiquidAgilACCTracker.class) != null){
					// 3x/inf. ACC, depending on talent level
					accuracy *= pointsInTalent(Talent.LIQUID_AGILITY) == 2 ? Float.POSITIVE_INFINITY : 3f;
					Talent.LiquidAgilACCTracker buff = buff(Talent.LiquidAgilACCTracker.class);
					buff.uses--;
					if (buff.uses <= 0) {
						buff.detach();
					}
				}
			}
		}

		if (buff(Scimitar.SwordDance.class) != null){
			accuracy *= 1.50f;
		}

		if(attackDelay() > 1 && hasTalent(Talent.STRONGMAN)){
			int points = pointsInTalent(Talent.STRONGMAN);
			float excessDelay = attackDelay() - 1f;
			float accMulti = points * 0.01f;
			float accBonus = excessDelay * accMulti;
			accuracy += accuracy * Math.min(accBonus, 0.75f);
		}

		for(StarSachet star : belongings.getAllItems(StarSachet.class)) {
			if(star!=null){
				accuracy += (float) getZone();
			}
		}

        if(belongings.getItem(TerrorDoll.class) != null || belongings.getItem(TerrorDollB.class) != null) accuracy *= 0.75f;

		if( Dungeon.isDLC(Conducts.Conduct.DEV) && CustomPlayer.overrideGame &&CustomPlayer.shouldOverride ) {
			return CustomPlayer.baseAccuracy;
		}

		if( Dungeon.isDLC(Conducts.Conduct.DEV) && CustomPlayer.overrideGame &&CustomPlayer.shouldOverride ){
			return  CustomPlayer.baseAccuracy;
		} else if  (!RingOfForce.fightingUnarmed(this)) {
			return (int)(attackSkill * accuracy * wep.accuracyFactor( this, target ));
		} else {
			return (int)(attackSkill * accuracy);
		}
	}

	public double getZone(){
		return Math.floor((double) Dungeon.scalingDepth() /5 + 1 );
	}

	@Override
	public int defenseSkill( Char enemy ) {

		if (buff(Combo.ParryTracker.class) != null){
			if (canAttack(enemy) && !isCharmedBy(enemy)){
				Buff.affect(this, Combo.RiposteTracker.class).enemy = enemy;
			}
			return INFINITE_EVASION;
		}

		if (buff(RoundShield.GuardTracker.class) != null){
			return INFINITE_EVASION;
		}

		float evasion = defenseSkill;
		if( Dungeon.isDLC(Conducts.Conduct.DEV) && CustomPlayer.overrideGame && !CustomPlayer.shouldOverride ){
			evasion = CustomPlayer.baseEvasion;
		}

		evasion *= RingOfEvasion.evasionMultiplier( this );

		if (buff(Talent.RestoredAgilityTracker.class) != null){
			if (pointsInTalent(Talent.LIQUID_AGILITY) == 1){
				evasion *= 4f;
			} else if (pointsInTalent(Talent.LIQUID_AGILITY) == 2){
				return INFINITE_EVASION;
			}
		}

		if (buff(TreeList.DefensiveStance.class) != null){
			evasion *= 3;
		}

		if (paralysed > 0) {
			evasion /= 2;
		}

		if (belongings.armor() != null) {
			evasion = belongings.armor().evasionFactor(this, evasion);
		}

		//提升10%的闪避
		if (buff(BlessQinyue.class) != null){
			evasion = evasion * 1.1f;
		}


		if( Dungeon.isDLC(Conducts.Conduct.DEV) && CustomPlayer.overrideGame && CustomPlayer.shouldOverride ){
			return CustomPlayer.baseEvasion;
		}

		if(belongings.getItem(StarSachet.class)!=null) {
			evasion += (float) getZone();
		}

        if( belongings.getItem(HeartOfCrystalFractal.class)!=null){
			evasion *= 0.85f;
		}

		return Math.round(evasion);
	}

	@Override
	public String defenseVerb() {
		Combo.ParryTracker parry = buff(Combo.ParryTracker.class);
		if (parry != null){
			parry.parry();
			return Messages.get(Monk.class, "parried");
		}

		
		if (buff(MonkEnergy.MonkAbility.Focus.FocusBuff.class) != null){
			buff(MonkEnergy.MonkAbility.Focus.FocusBuff.class).detach();
			if (sprite != null && sprite.visible) {
				Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY, 1, Random.Float(0.96f, 1.05f));
			}
			return Messages.get(Monk.class, "parried");
		}

		if (buff(RoundShield.GuardTracker.class) != null){
			buff(RoundShield.GuardTracker.class).detach();
			Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY, 1, Random.Float(0.96f, 1.05f));
			return Messages.get(RoundShield.GuardTracker.class, "guarded");
		}

		if (buff(MonkEnergy.MonkAbility.Focus.FocusActivation.class) != null){
			buff(MonkEnergy.MonkAbility.Focus.FocusActivation.class).detach();
			if (sprite != null && sprite.visible) {
				Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY, 1, Random.Float(0.96f, 1.05f));
			}
			return Messages.get(Monk.class, "parried");
		}

		return super.defenseVerb();
	}

	@Override
	public int drRoll() {
		int dr = super.drRoll();
		if( Dungeon.isDLC(Conducts.Conduct.DEV) &&CustomPlayer.overrideGame &&!CustomPlayer.shouldOverride ){
			dr += CustomPlayer.baseArmor;
		}

		if (belongings.armor() != null) {
			int armDr = Random.NormalIntRange( belongings.armor().DRMin(), belongings.armor().DRMax());
			if (STR() < belongings.armor().STRReq()){
				armDr -= 2*(belongings.armor().STRReq() - STR());
			}
			if (armDr > 0) dr += armDr;
		}
		if (belongings.weapon() != null && !RingOfForce.fightingUnarmed(this))  {
			int wepDr = Random.NormalIntRange( 0 , belongings.weapon().defenseFactor( this ) );
			if (STR() < ((Weapon)belongings.weapon()).STRReq()){
				wepDr -= 2*(((Weapon)belongings.weapon()).STRReq() - STR());
			}
			if (wepDr > 0) dr += wepDr;
		}

//		if (hasTalent(Talent.HOLD_FAST)){
//			int drBouns = Char.combatRoll(0, 2* pointsInTalent(Talent.HOLD_FAST));
//			if(buff(Chill.class) != null || buff(Frost.class) != null || buff(Slow.class) != null || buff(Roots.class) != null || buff(Paralysis.class) != null || buff(Cripple.class) != null){
//				dr += drBouns * 3;
//			}else{
//				dr += drBouns;
//			}
//		}

		if( Dungeon.isDLC(Conducts.Conduct.DEV) &&CustomPlayer.overrideGame &&CustomPlayer.shouldOverride ){
			dr = CustomPlayer.baseArmor;
		}

		return dr;
	}

	@Override
	public int damageRoll() {
		KindOfWeapon wep = belongings.attackingWeapon();
		int dmg=0;
		if( Dungeon.isDLC(Conducts.Conduct.DEV) &&CustomPlayer.overrideGame &&!CustomPlayer.shouldOverride ){
			dmg = CustomPlayer.baseDamage;
		}

		if (belongings.getItem(Monocular.class) != null && wep instanceof MissileWeapon) {
			MissileWeapon missileWep = (MissileWeapon) wep;
			missileWep.monocularAccBonus = 1.0f;
			missileWep.distanceAccBonus = 0;
			int distance = distance(enemy);
			while (distance > 1) {
				missileWep.distanceAccBonus += 2;
				distance -= 1;
			}
		}

		if (!RingOfForce.fightingUnarmed(this)) {
			dmg = wep.damageRoll( this );

			if (!(wep instanceof MissileWeapon)) dmg += RingOfForce.armedDamageBonus(this);
		} else {
			dmg = RingOfForce.damageRoll(this);
			if (RingOfForce.unarmedGetsWeaponAugment(this)){
				dmg = ((Weapon)belongings.attackingWeapon()).augment.damageFactor(dmg);
			}
		}

		PhysicalEmpower emp = buff(PhysicalEmpower.class);
		if (emp != null){
			dmg += emp.dmgBoost;
			emp.left--;
			if (emp.left <= 0) {
				emp.detach();
			}
			Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG, 0.75f, 1.2f);
		}

		if (heroClass != HeroClass.DUELIST
				&& hasTalent(Talent.WEAPON_RECHARGING)
				&& (buff(Recharging.class) != null || buff(ArtifactRecharge.class) != null)){
			dmg = Math.round(dmg * 1.025f + (.025f*pointsInTalent(Talent.WEAPON_RECHARGING)));
		}

		if( Dungeon.isDLC(Conducts.Conduct.DEV) &&CustomPlayer.overrideGame &&CustomPlayer.shouldOverride ){
			dmg = CustomPlayer.baseDamage;
		}

		if( attackDelay() > 1 && hasTalent(Talent.STRONGMAN) && !(wep instanceof SpiritBow)){
			int points = pointsInTalent(Talent.STRONGMAN);
			float excessDelay = attackDelay() - 1f;
			float dmgMulti = points * (0.5f / 3f);
			float dmgBonus = excessDelay * dmgMulti;
			dmg += (int) (dmg * Math.min(dmgBonus, 0.5f));
		}

		if(belongings.getItem(PortableWhetstone.class)!=null){
			dmg += StoneDamage();
		}

		if(hero.belongings.getItem(DeadOrAlive.class)!=null){
			dmg = (int) (dmg * 1.1f);
		}

		if(belongings.getItem(CloakFragmentsOfBzmdr.class)!=null) {
			if(getZone() == 1){
				dmg --;
			} else if (getZone() == 5) {
				dmg -= 15;
			}else{
				dmg -= (int) (getZone()-1) *3;
			}
		}

		if (dmg < 0) dmg = 0;
		return dmg;
	}

	private int StoneDamage() {
		int depthSegment = Dungeon.depth / 5;
		int[] damageMap = {2, 3, 4, 5, 7, 9};
		if (depthSegment >= damageMap.length) {
			return damageMap[damageMap.length - 1];
		}
		return damageMap[depthSegment];
	}

	@Override
	public float speed() {
		float speed = 0;
		StarDust starDust = hero.belongings.getItem(StarDust.class);

		if( Dungeon.isDLC(Conducts.Conduct.DEV) && CustomPlayer.overrideGame && !CustomPlayer.shouldOverride ){
			speed += CustomPlayer.baseSpeed;
		}



		speed += super.speed();

		speed *= RingOfHaste.speedMultiplier(this);

		if (buff(BlessQinyue.class) != null) speed *= 1.25f;

		if(buff(ScrollOfSoul.UpgradeSoul.class)!=null){
			speed *= 2;
		}

		//提升20%移速
		MIME.GOLD_THREE getSpeed = hero.belongings.getItem(MIME.GOLD_THREE.class);
		if (getSpeed!=null) {
			speed *= 1.2f;
		}

		if (starDust!=null) {
			speed *= 0.75f;
			speed = min(speed,1.5f);
		}

		if(hero.buff(SliceDeadBless.class)!=null && Dungeon.depth>28){
			speed *= 1.26f;
		}

		if(Dungeon.isChallenged(CS) && !gameNight){
			speed *= 1.10f;
		} else if(gameNight) {
			speed *= 1.05f;
		}

		if(hero.buff(BlessRedWhite.class) != null) {
			speed *= 1.1f;
		}

		if (belongings.armor() != null) {
			speed = belongings.armor().speedFactor(this, speed);
		}

		Momentum momentum = buff(Momentum.class);
		if (momentum != null){
			((HeroSprite)sprite).sprint( momentum.freerunning() ? 1.5f : 1f );
			speed *= momentum.speedMultiplier();
		} else {
			((HeroSprite)sprite).sprint( 1f );
		}

		NaturesPower.naturesPowerTracker natStrength = buff(NaturesPower.naturesPowerTracker.class);
		if (natStrength != null){
			speed *= (2f + 0.25f*pointsInTalent(Talent.GROWING_POWER));
		}

		speed = AscensionChallenge.modifyHeroSpeed(speed);

		if( Dungeon.isDLC(Conducts.Conduct.DEV) && CustomPlayer.overrideGame && CustomPlayer.shouldOverride ){
			speed = CustomPlayer.baseSpeed;
		}

		if ( buff( DeadDogCerberus.SoulDead.class ) != null || Dungeon.depth == 31 && (branch == 1 || branch == 2) ){
			speed = 1f;
		}

		return speed;

	}

	@Override
	public boolean canSurpriseAttack(){
		KindOfWeapon w = belongings.attackingWeapon();
		if (!(w instanceof Weapon))             return true;
		if (RingOfForce.fightingUnarmed(this))  return true;
		if (STR() < ((Weapon)w).STRReq())       return false;
		if (w instanceof Flail || w instanceof MagicTorch)	return false;

		return super.canSurpriseAttack();
	}

	public boolean canAttack(Char enemy){
		if (enemy == null || pos == enemy.pos || !Actor.chars().contains(enemy)) {
			return false;
		}

		for (ChampionHero buff : buffs(ChampionHero.class)){
			if (buff.canAttackWithExtraReach( enemy )){
				return true;
			}
		}

		//can always attack adjacent enemies
		if (level.adjacent(pos, enemy.pos)) {
			return true;
		}

		KindOfWeapon wep = hero.belongings.attackingWeapon();

		if (wep != null){
			return wep.canReach(this, enemy.pos);
		} else {
			return false;
		}
	}

	public float attackDelay() {
		if (buff(Talent.LethalMomentumTracker.class) != null){
			buff(Talent.LethalMomentumTracker.class).detach();
			return 0;
		}

		if (buff(KnightStabbingSword.NoRoundTracker.class) != null){
			return 0;
		}

		float delay = 1f;


		if(buff(ScrollOfSoul.UpgradeSoul.class)!=null){
			delay /= 2;
		}

		if( Dungeon.isDLC(Conducts.Conduct.DEV) && CustomPlayer.overrideGame ){
			if(!CustomPlayer.shouldOverride)
				delay = CustomPlayer.baseAttackDelay;
			else return CustomPlayer.baseAttackDelay;
		}

		if (!RingOfForce.fightingUnarmed(this)) {

			return delay * belongings.attackingWeapon().delayFactor( this );

		} else {
			//Normally putting furor speed on unarmed attacks would be unnecessary
			//But there's going to be that one guy who gets a furor+force ring combo
			//This is for that one guy, you shall get your fists of fury!
			float speed = RingOfFuror.attackSpeedMultiplier(this);

			//ditto for furor + sword dance!
			if (buff(Scimitar.SwordDance.class) != null){
				speed += 0.6f;
			}

			//and augments + brawler's stance! My goodness, so many options now compared to 2014!
			if (RingOfForce.unarmedGetsWeaponAugment(this)){
				delay = ((Weapon)belongings.weapon).augment.delayFactor(delay);
			}

			return delay/speed;
		}
	}

	@Override
	public void spend( float time ) {
		super.spend(time);
	}

	@Override
	public void spendConstant(float time) {
		justMoved = false;
		super.spendConstant(time);
	}

	public void spendAndNextConstant(float time ) {
		busy();
		spendConstant( time );
		next();
	}

	public void spendAndNext( float time ) {
		busy();
		spend( time );
		next();
	}

	@Override
	public boolean act() {
		PropBuff propBuffbuff = buff(PropBuff.class);
		if (propBuffbuff != null) {
			int remainingLevel = Math.max(0, propBuffbuff.levelA);
			if (remainingLevel > 0) {
				int count = 0;
				boolean isNegative = false;
				if (isAlive() && this.buffs() != null) {
					for (Buff b : this.buffs()) {
						if (remainingLevel <= 0) break;
						if (b.type == Buff.buffType.NEGATIVE
								&& !(b instanceof AllyBuff)
								&& !(b instanceof LostInventory)) {
							b.detach();
							remainingLevel--;
							isNegative = true;
							count++;
						}
					}
				}
				propBuffbuff.levelA = remainingLevel;
				if (isNegative) {
					GLog.p(Messages.get(FaintGlimmer.class, "light", count,remainingLevel));
				}
			}
		}

		if(level instanceof AncientMysteryCityLevel || level instanceof AncientMysteryCityBossLevel){
			if(buff(AncientStats.class) == null){
				Buff.affect(this, AncientStats.class).set((100), 1);
			}
		}

		UnlessEndFlowerLevel.UnlessAbyss unlessAbyss = hero.buff(UnlessEndFlowerLevel.UnlessAbyss.class);
		if(unlessAbyss != null){
			if (!unlessAbyss.isCollapsing) {
				unlessAbyss.Time++;
				if (unlessAbyss.Time >= 500) {
					unlessAbyss.isCollapsing = true;
					Buff.affect(hero, Levitation.class,100f);
				}
			} else {
				unlessAbyss.Time++;
				if (unlessAbyss.Time % 5 == 0) {
					Buff.affect(hero, Levitation.class,100f);
					if(level instanceof UnlessEndFlowerLevel){
						((UnlessEndFlowerLevel) level).triggerTerrainCollapse();
					}
				}
			}
		}


		if (belongings.getItem(UnlessFlower.class) != null){
			//No Effect
		} else if (belongings.getItem(MIME.GOLD_FIVE.class) != null) {
			if(HT/4 > HP){
				die(true);
			}
		}

		//降神Buff
		if(buff(UnlessFlower.UnlessFlowerTime.class)!=null){
			for (Buff b : this.buffs()) {
				if (b.type == Buff.buffType.NEGATIVE
						&& !(b instanceof AllyBuff)
						&& !(b instanceof LostInventory)) {
					b.detach();
				}
			}
		}

		// 耐寒体质：2级天赋 → 获得寒冷免疫
		if (Dungeon.hero.pointsInTalent(Talent.COLD_HARDY_CONSTITUTION) == 2) {
			immunities.add(Chill.class);
		}

		BrokenRing brokenRing = hero.belongings.getItem(BrokenRing.class);
		if(brokenRing != null){
			if(belongings.misc() != null){
				KindofMisc misc = belongings.misc();
				if(misc.cursed){
					misc.cursed = false;
				}
				misc.doUnequip(this,true);
				GLog.n(Messages.get(brokenRing,"lock"));
			}
			if(HT * 0.6f >= HP){
				if(belongings.artifact() != null) {
					Artifact artifact = belongings.artifact();
					if (artifact.cursed) {
						artifact.cursed = false;
					}
					artifact.doUnequip(this, true);
				}
			}
			if(HT * 0.4f >= HP){
				if(belongings.ring() != null) {
					Ring ring = belongings.ring();
					if (ring.cursed) {
						ring.cursed = false;
					}
					ring.doUnequip(this, true);
				}
			}
			if(HT * 0.2f >= HP){
				if(belongings.armor() != null) {
					Armor armor = belongings.armor();
					if (armor.cursed) {
						armor.cursed = false;
					}
					armor.doUnequip(this, true);
				}
			}
		}

		CatGirlCosplay catGirlCosplay = hero.belongings.getItem(CatGirlCosplay.class);
		if(catGirlCosplay != null){
			if(Random.Float()<=0.0325f){
				int mapLength = level.length();
				for (int i : PathFinder.CIRCLE7) {
					int targetingPos = hero.pos + i;
					if (targetingPos >= 0 && targetingPos < mapLength && !level.solid[targetingPos]) {
						GameScene.add(Blob.seed(targetingPos, 1, CatGirlCosplay.NoSeenBlobs.class));
					}
				}
			}
		}

		if(belongings.weapon instanceof TreeList){
			Buff.affect(this, TreeList.TreeBarrier.class);
		}

		HellButterfly hellButterfly = Dungeon.hero.belongings.getItem(HellButterfly.class);
		if(hellButterfly != null){
			immunities.add(HalomethaneBurning.class);
			immunities.add(HellBurning.class);
		} else {
			immunities.remove(HalomethaneBurning.class);
			immunities.remove(HellBurning.class);
		}

		BzmdrNewYears.BzmdrGift bzmdrGift = hero.belongings.getItem(BzmdrNewYears.BzmdrGift.class);
		if(bzmdrGift != null){
			viewDistance = 8 + Statistics.BzmdrCJHeroViewDistance;
		}

		MageHand.MageHandControl m = hero.belongings.getItem(MageHand.MageHandControl.class);
		if(m != null){
			for (Mob mob : level.mobs.toArray(new Mob[0])) {
				if (mob instanceof MageHand) {
					if(m.mageHand == null){
						m.mageHand = (MageHand) mob;
					}
				}
			}
		}


		if(hasTalent(Talent.MAGIC_ABSORB)){
			if(buff(MagicAbsorb.class) == null){
				Buff.affect(hero, MagicAbsorb.class).set(100, 1);
			}
		}

		if(level instanceof NewZeroFiveLevel || level instanceof NormalZeroFiveLevel){
			for (Mob mob : level.mobs.toArray(new Mob[0])) {
				if(mob instanceof WhiteLingLand){
					if(Dungeon.level.distance(mob.pos, pos) <= 2){
						Statistics.snow = true;
					} else {
						Statistics.snow = level.distance(pos, 961) > 13;
					}
				}
			}
		}

		if(!Statistics.onlyLing){
			Statistics.snow = level.distance(pos, 961) > 13;
		}


		//水中祝福 但在BR不生效
		if((branch == 0 || branch == 10) && !bossRushMode){
			MoveWater();
		}

		if(level instanceof MoveBoxHollowActorLevel){
			for (Mob mob : level.mobs.toArray(new Mob[0])) {
				if(!(mob instanceof MoveBoxHollowActorLevel.Box)){
					mob.damage(mob.HP, this,DamageType.MAGIC);
				}
			}
		}

		boolean isNyzAlive = false;
		if(level instanceof MorpheusBossLevel){
			if(level.map[pos] == Terrain.TRAP){
				for (Mob mob : level.mobs.toArray(new Mob[0])){
					if (mob instanceof Nyarlathotep) {
						isNyzAlive = true;
						break;
					}
				}
				if(isNyzAlive){
					for (Buff buff : buffs()) {
						if(buff instanceof ScaryDamageBuff || buff instanceof ScaryImmunitiesBuff){
							int heartDamage = (int) (20 * Random.NormalFloat(0.5f, 1));
							enemy.damage(heartDamage, new DM100.LightningBolt());
							for (Mob mob : level.mobs.toArray(new Mob[0])){
								mob.HP += Math.max(HT,heartDamage);
							}
						} else if (buff instanceof ScaryBuff) {
							if(((ScaryBuff) buff).Scary > 100){
								damage(20,this,DamageType.MAGIC);
							} else {
								int heartDamage = (int) (20 * Random.NormalFloat(0.5f, 1));
								enemy.damage(heartDamage, new DM100.LightningBolt());
								for (Mob mob : level.mobs.toArray(new Mob[0])){
									mob.HP += Math.max(HT,heartDamage);
								}
								((ScaryBuff) buff).damgeScary(20);
							}
						} else {
							Buff.affect(this, ScaryBuff.class).set((100), 5);
						}
					}
				} else {
					for (Buff buff : buffs()) {
						if(buff instanceof ScaryDamageBuff || buff instanceof ScaryImmunitiesBuff) {
							damage(10,this,DamageType.MAGIC);
						} else if (buff instanceof ScaryBuff) {
							if(((ScaryBuff) buff).Scary > 100){
								damage(10,this,DamageType.MAGIC);
							} else {
								((ScaryBuff) buff).damgeScary(10);
							}
						} else {
							Buff.affect(this, ScaryBuff.class).set((100), 5);
						}
					}
				}
			}
		}


		if (Dungeon.isChallenged(AQUAPHOBIA) && Dungeon.depth>0 && !Dungeon.bossLevel()){
			if(level.map[pos] == Terrain.SALT_WATER && !flying && hero.buff(WaterSoulX.class) == null){
				for (Buff buff : hero.buffs()) {
					if(buff.type == Buff.buffType.NEGATIVE && buff instanceof FlavourBuff && paralysed == 0 && !hero.rooted && !(buff instanceof Vertigo)) {
						Buff.prolong(this, (Class<? extends FlavourBuff>) buff.getClass(), 5f);
					}
					Buff.affect(this, OozeStatueDead.class);
				}
			} else if(level.water[pos] && !flying && level.map[pos] == Terrain.WATER && hero.buff(WaterSoulX.class) == null){
				Level.set(pos, Terrain.SALT_WATER);
				GameScene.updateMap(pos);
			}
		}

		if(buff(ElectricalSmoke.SmokingAlloy.class) != null && !buff(ElectricalSmoke.SmokingAlloy.class).isCursed() && !withElectricalSmoke){
			withElectricalSmoke = true;
		}else if(buff(ElectricalSmoke.SmokingAlloy.class) == null || buff(ElectricalSmoke.SmokingAlloy.class).isCursed()){
			withElectricalSmoke = false;
		}


		for(Actor actor : Actor.all()){
			if(actor instanceof WandOfSun.MiniSun){
				WandOfSun.MiniSun s = (WandOfSun.MiniSun) actor;
				if(s.sprite.parent == null){
					s.sprite = new SunSprite();
					s.sprite.place(s.pos);
					s.sprite.parent = level.addVisuals();
					GameScene.scene.add(s);
				}
			}
		}


		LanFireGo lanFireGo = hero.belongings.getItem(LanFireGo.class);
		if (lanFireGo != null) {
			lanFireGo.detachAll(hero.belongings.backpack);
			if(Dungeon.isChallenged(DHXD)){
				hero.lanterfire = 60;
				Buff.detach( this, MagicGirlSayCursed.class );
				Buff.detach( this, MagicGirlSayKill.class );
				Buff.detach( this, MagicGirlSayMoneyMore.class );
				Buff.detach( this, MagicGirlSaySlowy.class );
				Buff.detach( this, MagicGirlSayNoSTR.class );
				Buff.detach( this, MagicGirlSayTimeLast.class );
				Buff.detach( this, Nyctophobia.NoRoadMobs.class);
			}
		}

		Empty empty = hero.belongings.getItem(Empty.class);
		if (empty != null) {
			empty.detachAll(hero.belongings.backpack);
		}

		if(zeroItemLevel == 8 && Dungeon.depth == 0){
			PaswordBadges.WHATSUP();
			zeroItemLevel++;
		}

		if(!youNoItem && zeroItemLevel >= 4){

			Game.runOnRenderThread(new Callback() {
				@Override
				public void call() {
					GameScene.show( new WndMessage(Messages.get(Hero.class,"no_item")));
					GLog.p(Messages.get(Hero.class,"no_item"));
					youNoItem = true;
				}
			});
		}

		if (Challenges.activeChallenges() >= 10 && !lanterfireactive && !Dungeon.isDLC(Conducts.Conduct.DEV)) {
			GLog.n(Messages.get(WndStory.class, "warning"));
		}

		ScrollOfTeleTation potionOfPurityLing = hero.belongings.getItem(ScrollOfTeleTation.class);
				if(Dungeon.depth != 0) {
					if (potionOfPurityLing != null) potionOfPurityLing.detach(belongings.backpack);
				}

		AnySkinSelect anySkinSelect = hero.belongings.getItem(AnySkinSelect.class);
		if(anySkinSelect != null){
			if(SPDSettings.isItemUnlock("anyskin1")){
				anySkinSelect.detach(hero.belongings.backpack);
			}
		}

		if (Challenges.activeChallenges() >= 10 && !lanterfireactive && !Dungeon.isDLC(Conducts.Conduct.DEV) || Dungeon.isChallenged(DHXD) && !lanterfireactive) {
			//灯火前行 4.0
			if(Dungeon.isChallenged(DHXD)){
				hero.lanterfire = 60 - min(Challenges.activeChallenges() * 4, 5);
			} else {
				hero.lanterfire = 100 - min(Challenges.activeChallenges() * 4, 45);
			}

			new OilLantern().quantity(1).identify().collect();

			lanterfireactive = true;

			Buff.affect( this, Nyctophobia.class );
			Buff.affect( this, LanFireStats.class );
			if(lanterfire>50){
				switch (Random.Int(5)) {
					case 0:
					default:
						Buff.affect(hero, BlessMobDied.class).set((100), 1);
						break;
					case 1:
						Buff.affect(hero, BlessMixShiled.class).set((100), 1);
						break;
					case 2:
						Buff.affect(hero, BlessImmune.class).set((100), 1);
						break;
					case 3:
						Buff.affect(hero, BlessGoRead.class).set((100), 1);
						break;
					case 4:
						new WandOfAnmy().quantity(1).identify().collect();
						Buff.affect(hero, BlessAnmy.class).set((100), 1);
				}
				GLog.b(Messages.get(WndStory.class, "letxz"));
			}


		}

		//calls to dungeon.observe will also update hero's local FOV.
		fieldOfView = level.heroFOV;

		if (buff(Endure.EndureTracker.class) != null){
			buff(Endure.EndureTracker.class).endEnduring();
		}

		if (!ready) {
			//do a full observe (including fog update) if not resting.
			if (!resting || buff(MindVision.class) != null || buff(Awareness.class) != null || buff(SmallLightHeader.SAwareness.class)!=null || buff(MageHand.HandWareness.class)!=null){
				Dungeon.observe();
			} else {
				//otherwise just directly re-calculate FOV
				level.updateFieldOfView(this, fieldOfView);
			}
		}

		checkVisibleMobs();
		BuffIndicator.refreshHero();

		if (paralysed > 0) {

			curAction = null;

			spendAndNext( TICK );
			return false;
		}

		boolean actResult;
		if (curAction == null) {

			if (resting) {
				spendConstant( TIME_TO_REST );
				next();
			} else {
				ready();
			}

			actResult = false;

		} else {

			resting = false;

			ready = false;

			if (curAction instanceof HeroAction.Move) {
				actResult = actMove( (HeroAction.Move)curAction );

			} else if (curAction instanceof HeroAction.Interact) {
				actResult = actInteract( (HeroAction.Interact)curAction );

			} else if (curAction instanceof HeroAction.Buy) {
				actResult = actBuy( (HeroAction.Buy)curAction );

			} else if (curAction instanceof HeroAction.BuyIce) {
				actResult = actBuyIce( (HeroAction.BuyIce)curAction );

			} else if (curAction instanceof HeroAction.BuyRush) {
				actResult = actBuyRush( (HeroAction.BuyRush)curAction );

			}else if (curAction instanceof HeroAction.PickUp) {
				actResult = actPickUp( (HeroAction.PickUp)curAction );

			} else if (curAction instanceof HeroAction.OpenChest) {
				actResult = actOpenChest( (HeroAction.OpenChest)curAction );

			} else if (curAction instanceof HeroAction.Unlock) {
				actResult = actUnlock((HeroAction.Unlock) curAction);

			} else if (curAction instanceof HeroAction.Mine) {
				actResult = actMine( (HeroAction.Mine)curAction );

			}else if (curAction instanceof HeroAction.LvlTransition) {
				actResult = actTransition( (HeroAction.LvlTransition)curAction );

			} else if (curAction instanceof HeroAction.Attack) {
				actResult = actAttack( (HeroAction.Attack)curAction );

			} else if (curAction instanceof HeroAction.Alchemy) {
				actResult = actAlchemy( (HeroAction.Alchemy)curAction );

			} else {
				actResult = false;
			}
		}

		if(hasTalent(Talent.BARKSKIN) && level.map[pos] == Terrain.FURROWED_GRASS){
			Barkskin.conditionallyAppend(this, (lvl*pointsInTalent(Talent.BARKSKIN))/2, 1 );
		}

		return actResult;
	}

	public void busy() {
		ready = false;
	}

	public void ready() {
		if (sprite.looping()) sprite.idle();
		curAction = null;
		damageInterrupt = true;
		waitOrPickup = false;
		ready = true;
		canSelfTrample = true;

		AttackIndicator.updateState();

		GameScene.ready();
	}

	public void interrupt() {
		if (isAlive() && curAction != null &&
				((curAction instanceof HeroAction.Move && curAction.dst != pos) ||
						(curAction instanceof HeroAction.LvlTransition))) {
			lastAction = curAction;
		}
		curAction = null;
		GameScene.resetKeyHold();
	}

	public void resume() {
		curAction = lastAction;
		lastAction = null;
		damageInterrupt = false;
		next();
	}

	private boolean canSelfTrample = false;
	public boolean canSelfTrample(){
		return canSelfTrample && !rooted && !flying &&
				//standing in high grass
				(level.map[pos] == Terrain.HIGH_GRASS ||
						//standing in furrowed grass and not huntress
						(heroClass != HeroClass.HUNTRESS && level.map[pos] == Terrain.FURROWED_GRASS) ||
						//standing on a plant
						level.plants.get(pos) != null);
	}

	public static void goodLanterFire() {
		switch (Random.Int(5)) {
			case 1:
				Buff.affect(hero, BlessGoodSTR.class).set((100), 1);
				break;
			case 2:
				Buff.affect(hero, BlessMobDied.class).set((100), 1);
				break;
			case 3:
				Buff.affect(hero, BlessMixShiled.class).set((100), 1);
				break;
			case 4:
				if(Dungeon.depth < 20){
					Buff.affect(hero, BlessImmune.class).set((100), 1);
				}
				break;
			default:
				if(Dungeon.depth < 20){
					Buff.affect(hero, BlessNoMoney.class).set((100), 1);
				} else {
					Buff.affect(hero, BlessMixShiled.class).set((100), 1);
				}
				break;
		}
		GLog.p(Messages.get(WndStory.class, "good"));
	}



	private boolean actInteract( HeroAction.Interact action ) {

		Char ch = action.ch;

		if (ch.isAlive() && ch.canInteract(this)) {

			ready();
			sprite.turnTo( pos, ch.pos );
			return ch.interact(this);

		} else {

			if (fieldOfView[ch.pos] && getCloser( ch.pos )) {

				return true;

			} else {
				ready();
				return false;
			}

		}
	}

	private boolean actBuy( HeroAction.Buy action ) {
		int dst = action.dst;
		if (pos == dst) {

			ready();

			Heap heap = level.heaps.get( dst );
			if (heap != null && heap.type == Type.FOR_SALE && heap.size() == 1 ) {
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						if(bossRushMode){
							GameScene.show( new WndRushTradeItem( heap ) );
						} else {
							GameScene.show( new WndTradeItem( heap ) );
						}

					}
				});
			}

			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actBuyIce( HeroAction.BuyIce action ) {
		int dst = action.dst;
		if (pos == dst) {

			ready();

			Heap heap = level.heaps.get( dst );
			if (heap != null && heap.type == Type.FOR_ICE && heap.size() == 1) {
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						GameScene.show( new WndIceTradeItem( heap ) );
					}
				});
			}

			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actBuyRush( HeroAction.BuyRush action ) {
		int dst = action.dst;
		if (pos == dst) {

			ready();

			Heap heap = level.heaps.get( dst );
			if (heap != null && heap.type == Type.FOR_RUSH && heap.size() == 1) {
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						GameScene.show( new WndRushTradeItem( heap ) );
					}
				});
			}

			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actAlchemy( HeroAction.Alchemy action ) {
		int dst = action.dst;
		if (level.distance(dst, pos) <= 1) {

			ready();

			AlchemistsToolkit.kitEnergy kit = buff(AlchemistsToolkit.kitEnergy.class);
			if (kit != null && kit.isCursed()){
				GLog.w( Messages.get(AlchemistsToolkit.class, "cursed"));
				return false;
			}

			AlchemyScene.clearToolkit();
			ShatteredPixelDungeon.switchScene(AlchemyScene.class);
			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	//used to keep track if the wait/pickup action was used
	// so that the hero spends a turn even if the fail to pick up an item
	public boolean waitOrPickup = false;

	private boolean actPickUp( HeroAction.PickUp action ) {
		int dst = action.dst;
		if (pos == dst) {

			Heap heap = level.heaps.get( pos );
			if (heap != null) {
				Item item = heap.peek();
				if (item.doPickUp( this )) {
					heap.pickUp();

					if (item instanceof Dewdrop
							|| item instanceof TimekeepersHourglass.sandBag
							|| item instanceof DriedRose.Petal
							|| item instanceof Key
							|| item instanceof Guidebook) {
						//Do Nothing
					} else {

						//TODO make all unique items important? or just POS / SOU?
						boolean important = item.unique && item.isIdentified() &&
								(item instanceof Scroll || item instanceof Potion);
						if (important) {
							GLog.p( Messages.capitalize(Messages.get(this, "you_now_have", item.name())) );
						} else {
							GLog.i( Messages.capitalize(Messages.get(this, "you_now_have", item.name())) );
						}
					}

					curAction = null;
				} else {

					if (waitOrPickup) {
						spendAndNextConstant(TIME_TO_REST);
					}

					//allow the hero to move between levels even if they can't collect the item
					if (level.getTransition(pos) != null){
						throwItems();
					} else {
						heap.sprite.drop();
					}

					if (item instanceof Dewdrop
							|| item instanceof TimekeepersHourglass.sandBag
							|| item instanceof DriedRose.Petal
							|| item instanceof Key) {
						//Do Nothing
					} else {
						GLog.newLine();
						GLog.n(Messages.capitalize(Messages.get(this, "you_cant_have", item.name())));
					}

					ready();
				}
			} else {
				ready();
			}

			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actOpenChest( HeroAction.OpenChest action ) {
		int dst = action.dst;
		if (level.adjacent( pos, dst ) || pos == dst) {
			path = null;

			Heap heap = level.heaps.get( dst );
			if (heap != null && (heap.type != Type.HEAP && heap.type != Type.FOR_SALE && heap.type != Type.FOR_ICE && heap.type != Type.FOR_RUSH)) {

				//TODO 搜打撤后续需要优化
				if (((heap.type == Type.LOCKED_CHEST && Notes.keyCount(new GoldenKey(Dungeon.depth)) < 1)
						|| (heap.type == Type.CRYSTAL_CHEST && Notes.keyCount(new CrystalKey(Dungeon.depth)) < 1)|| (heap.type == Type.BLACK && Notes.keyCount(new BlackKey(Dungeon.depth)) < 1) || (heap.type == Type.GREEN_CHSET && Notes.keyCount(new GreenKey(Dungeon.depth)) < 1)) && Dungeon.depth != 31 && branch != 3){

					GLog.w( Messages.get(this, "locked_chest") );
					ready();
					return false;

				}

				switch (heap.type) {
					case TOMB:
						Sample.INSTANCE.play( Assets.Sounds.TOMB );
						PixelScene.shake( 1, 0.5f );
						break;
					case SKELETON:
					case REMAINS:
						break;
					default:
						Sample.INSTANCE.play( Assets.Sounds.UNLOCK );
				}

				sprite.operate( dst );

			} else {
				ready();
			}

			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actUnlock( HeroAction.Unlock action ) {
		int doorCell = action.dst;
		if (level.adjacent( pos, doorCell )) {
			path = null;

			boolean hasKey = false;
			int door = level.map[doorCell];

			if (door == Terrain.LOCKED_DOOR && Notes.keyCount(new IronKey(Dungeon.depth)) > 0 || checkUnlocked()) {

				hasKey = true;

			} else if (door == Terrain.CRYSTAL_DOOR
					&& Notes.keyCount(new CrystalKey(Dungeon.depth)) > 0 || checkUnlocked()) {

				hasKey = true;
			} else if (door == Terrain.GOLDEN_DOOR
					&& Notes.keyCount(new GoldenKey(Dungeon.depth)) > 0 || checkUnlocked()) {

				hasKey = true;

			} else if (door == Terrain.LOCKED_EXIT
					&& Notes.keyCount(new SkeletonKey(Dungeon.depth)) > 0) {

				hasKey = true;

			}

			if (hasKey) {

				sprite.operate( doorCell );

				Sample.INSTANCE.play( Assets.Sounds.UNLOCK );

			} else {
				GLog.w( Messages.get(this, "locked_door") );
				ready();
			}

			return false;

		} else if (getCloser( doorCell )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	public boolean actMine(HeroAction.Mine action){
		if (level.adjacent(pos, action.dst)){
			path = null;
			if ((level.map[action.dst] == Terrain.WALL
					|| level.map[action.dst] == Terrain.WALL_DECO
					|| level.map[action.dst] == Terrain.MINE_CRYSTAL
					|| level.map[action.dst] == Terrain.MINE_BOULDER)
					&& level.insideMap(action.dst)){
				sprite.attack(action.dst, new Callback() {
					@Override
					public void call() {

						boolean crystalAdjacent = false;
						for (int i : PathFinder.NEIGHBOURS8) {
							if (level.map[action.dst + i] == Terrain.MINE_CRYSTAL){
								crystalAdjacent = true;
								break;
							}
						}

						//1 hunger spent total
						if (level.map[action.dst] == Terrain.WALL_DECO){
							DarkGold gold = new DarkGold();
							if (gold.doPickUp( hero )) {
								DarkGold existing = hero.belongings.getItem(DarkGold.class);
								if (existing != null && existing.quantity()%5 == 0){
									if (existing.quantity() >= 40) {
										GLog.p(Messages.get(DarkGold.class, "you_now_have", existing.quantity()));
									} else {
										GLog.i(Messages.get(DarkGold.class, "you_now_have", existing.quantity()));
									}
								}
								spend(-Actor.TICK); //picking up the gold doesn't spend a turn here
							} else {
								level.drop( gold, pos ).sprite.drop();
							}
							PixelScene.shake(0.5f, 0.5f);
							CellEmitter.center( action.dst ).burst( Speck.factory( Speck.STAR ), 7 );
							Sample.INSTANCE.play( Assets.Sounds.EVOKE );
							Level.set( action.dst, Terrain.EMPTY_DECO );

							//mining gold doesn't break crystals
							crystalAdjacent = false;

							//4 hunger spent total
						} else if (level.map[action.dst] == Terrain.WALL){
							buff(Hunger.class).affectHunger(-3);
							PixelScene.shake(0.5f, 0.5f);
							CellEmitter.get( action.dst ).burst( Speck.factory( Speck.ROCK ), 2 );
							Sample.INSTANCE.play( Assets.Sounds.MINE );
							Level.set( action.dst, Terrain.EMPTY_DECO );

							//1 hunger spent total
						} else if (level.map[action.dst] == Terrain.MINE_CRYSTAL){
							//开采水晶获得回合数
							LockedFloor lock = hero.buff(LockedFloor.class);
							if (lock != null){
								if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES))
									lock.addTime(0.1f);
								else
									lock.addTime(0.05f);
							}
							Splash.at(action.dst, 0xFFFFFF, 5);
							Sample.INSTANCE.play( Assets.Sounds.SHATTER );
							Level.set( action.dst, Terrain.EMPTY );

							//1 hunger spent total
						} else if (level.map[action.dst] == Terrain.MINE_BOULDER){
							Splash.at(action.dst, ColorMath.random( 0x444444, 0x777766 ), 5);
							Sample.INSTANCE.play( Assets.Sounds.MINE, 0.6f );
							Level.set( action.dst, Terrain.EMPTY );
						}

						for (int i : PathFinder.NEIGHBOURS9) {
							level.discoverable[action.dst + i] = true;
						}
						for (int i : PathFinder.NEIGHBOURS9) {
							GameScene.updateMap( action.dst+i );
						}

						if (crystalAdjacent){
							sprite.parent.add(new Delayer(0.2f){
								@Override
								protected void onComplete() {
									boolean broke = false;
									for (int i : PathFinder.NEIGHBOURS8) {
										if (level.map[action.dst+i] == Terrain.MINE_CRYSTAL){
											Splash.at(action.dst+i, 0xFFFFFF, 5);
											Level.set( action.dst+i, Terrain.EMPTY );
											broke = true;
										}
									}
									if (broke){
										Sample.INSTANCE.play( Assets.Sounds.SHATTER );
									}

									for (int i : PathFinder.NEIGHBOURS9) {
										GameScene.updateMap( action.dst+i );
									}
									spendAndNext(TICK);
									ready();
								}
							});
						} else {
							spendAndNext(TICK);
							ready();
						}

						Dungeon.observe();
					}
				});
			} else {
				ready();
			}
			return false;
		} else if (getCloser( action.dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actTransition(HeroAction.LvlTransition action ) {
		int stairs = action.dst;
		LevelTransition transition = level.getTransition(stairs);

		if (rooted) {
			PixelScene.shake(1, 1f);
			ready();
			return false;

		} else if (!level.locked && transition != null && transition.inside(pos)) {

			if (level.activateTransition(this, transition)){
				curAction = null;
			} else {
				ready();
			}

			return false;

		} else if (getCloser( stairs )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actAttack( HeroAction.Attack action ) {

		enemy = action.target;

		if (enemy.isAlive() && canAttack( enemy ) && !isCharmedBy( enemy ) && enemy.invisible == 0) {

			if (heroClass != HeroClass.DUELIST
					&& hasTalent(Talent.AGGRESSIVE_BARRIER)
					&& buff(Talent.AggressiveBarrierCooldown.class) == null
					&& (HP / (float)HT) < 0.20f*(1+pointsInTalent(Talent.AGGRESSIVE_BARRIER))){
				Buff.affect(this, Barrier.class).setShield(3);
				Buff.affect(this, Talent.AggressiveBarrierCooldown.class, 50f);
			}
			sprite.attack( enemy.pos );

			return false;

		} else {

			if (fieldOfView[enemy.pos] && getCloser( enemy.pos )) {

				return true;

			} else {
				ready();
				return false;
			}

		}
	}

	public Char enemy(){
		return enemy;
	}

	public void rest( boolean fullRest ) {
		spendAndNextConstant( TIME_TO_REST );
		if (hasTalent(Talent.HOLD_FAST)){
			Buff.affect(this, HoldFast.class).pos = pos;
		}
		if (hasTalent(Talent.PATIENT_STRIKE)){
			Buff.affect(hero, Talent.PatientStrikeTracker.class).pos = hero.pos;
		}
		if (!fullRest) {
			if (sprite != null) {
				sprite.showStatus(CharSprite.DEFAULT, Messages.get(this, "wait"));
			}
		}
		resting = fullRest;
	}

	@Override
	public int attackProc( final Char enemy, int damage ) {

		damage = super.attackProc( enemy, damage );

		if(Dungeon.isChallenged(CS) && !gameNight) {
			damage *= 1.1f;
		} else if(gameTime>350 && gameTime<400) {
			damage *=1.05f;
		}

		KindOfWeapon wep;
		if (RingOfForce.fightingUnarmed(this) && !RingOfForce.unarmedGetsWeaponEnchantment(this)){
			wep = null;
		} else {
			wep = belongings.attackingWeapon();
			if(buff(MagicPower.MagicPowerIceMagic.class) != null){
				int dmg;
				dmg = (new Chilling()).proc((Weapon) wep, this, enemy, 0);
				enemy.damage(dmg,this);
			}
		}

		if(hasTalent(Talent.MAGICDAMAGE_MELEE)){
			// 法尔塔娅：+1=6%，+2=12% 额外魔法伤害
			enemy.damage((int) (damage * 0.06f * hero.pointsInTalent(Talent.MAGICDAMAGE_MELEE)), this, DamageType.MAGIC);
		}



		if(hasTalent(Talent.MAGIC_ABSORB)){
			MagicAbsorb buff = hero.buff(MagicAbsorb.class);
			if(buff != null){
				buff.downAbsord(hero.pointsInTalent(Talent.MAGIC_ABSORB));
			}
		}

		if(hasTalent(Talent.EMPOWERED_STRIKE)){
			MagesStaff staff = hero.belongings.getItem(MagesStaff.class);
			if(staff == null){
				for (Mob mob : level.mobs.toArray(new Mob[0])){
					if (mob instanceof MageHand) {
						staff = ((MageHand) mob).magesStaff;
					}
				}
			}

			if (staff != null && hero.subClass == HeroSubClass.BATTLEMAGE) {
				int battleMageLevel = hero.pointsInTalent(Talent.EMPOWERED_STRIKE);
				float triggerChance = min(1.0f, battleMageLevel * 0.33f);
				if (Random.Float() < triggerChance) {
					staff.wand.onHit(staff, hero, enemy, damage);
				}
			}
		}

		if (hero.belongings.weapon() instanceof Break) {
			if (enemy != null && enemy.HP <= enemy.HT * 0.5f) {
				float damageMultiplier = 1.0f + (0.3f + (0.03f * hero.belongings.weapon().level()));

				damage *= Math.round(damageMultiplier);

				hero.sprite.showStatus(Window.SKYBULE_COLOR, "+%d%%", Math.round((damageMultiplier - 1.0f) * 100));
			}
		}

		if (wep != null) damage = wep.proc( this, enemy, damage );

		damage = Talent.onAttackProc( this, enemy, damage );

		//225% 伤害
		if ( buff( Invulnerability.GodDied.class ) != null ){
			damage *= (int) 2.25f;
		}

		if (Objects.requireNonNull(subClass) == HeroSubClass.SNIPER) {
			if (wep instanceof MissileWeapon && !(wep instanceof SpiritBow.SpiritArrow) && enemy != this) {
				Actor.add(new Actor() {

					{
						actPriority = VFX_PRIO;
					}

					@Override
					protected boolean act() {
						if (enemy.isAlive()) {
							int bonusTurns = hasTalent(Talent.SHARED_UPGRADES) ? wep.buffedLvl() : 0;
							Buff.prolong(Hero.this, SnipersMark.class, SnipersMark.DURATION + bonusTurns).set(enemy.id(), bonusTurns);
						}
						Actor.remove(this);
						return true;
					}
				});
			}
		}

		if (damage > 0 && subClass == HeroSubClass.BERSERKER){
			Berserk berserk = Buff.affect(this, Berserk.class);
			berserk.damage(damage);
		}

		return damage;
	}

	@Override
	public int defenseProc( Char enemy, int damage ) {

		if (belongings.armor() != null) {
			damage = belongings.armor().proc( enemy, this, damage );
		}

		CapeOfThorns.Thorns recharge = buff(CapeOfThorns.Thorns.class);
		if (recharge != null) {
			recharge.onDamageTaken(damage);
		}

		WandOfLivingEarth.RockArmor rockArmor = buff(WandOfLivingEarth.RockArmor.class);
		if (rockArmor != null) {
			damage = rockArmor.absorb(damage);
		}

		if(enemy instanceof Mob){
			if(hero.belongings.getItem(PureRouge.class)!=null){
				if(!((Mob) enemy).firstAttack){
					PureRouge pr = hero.belongings.getItem(PureRouge.class);
					pr.PureRougeEffect(enemy,this,true);
					((Mob) enemy).firstAttack = true;
				}
			}

			if(hero.belongings.getItem(NoteOfBzmdr.class)!=null){
				Light l = hero.buff(Light.class);
				if (l != null){
					Buff.affect(this,Light.class,-1f);
				} else {
					damage = (int) (damage * 1.25f);
				}
			}
		}

		return super.defenseProc( enemy, damage );
	}

	@Override
	public void damage( int dmg, Object src ) {
		damage(dmg, src , null);
	}

	@Override
	public void damage( int dmg, Object src, DamageType type ) {

		if(hero.belongings.getItem(EmotionalAggregation.class)!=null && Random.Float()>0.90f ){
			GLog.n(Messages.get(EmotionalAggregation.class,"block"));
			return;
		}

		ChampionHero.Element doubleBuff = buff(ChampionHero.Element.class);
		if (doubleBuff != null) {
			boolean isMagicDamage = type == DamageType.MAGIC || type == DamageType.Element;
			if (isMagicDamage) {
				return;
			}
		}

		if(hasTalent(Talent.BLOOD_RIVER)){
			// 血河天赋：+1=10%最大生命，+2=15%最大生命
			float threshold = 0.05f + 0.05f * pointsInTalent(Talent.BLOOD_RIVER);
			if(dmg >= HT * threshold){
				if(buff(Talent.BloodRiverDealy.class) == null){
					Buff.affect(hero,Talent.BloodRiverDealy.class,3f);
				}
			}
		}

		if(hero.belongings.getItem(DeadOrAlive.class)!=null){
			dmg = (int) (dmg * 1.1f);
		}

		if(hero.belongings.getItem(WenStudyingPaperOne.class)!=null) {
			PropBuff props = hero.buff(PropBuff.class);
			if(props != null) {
				if(props.timeB >=7 && (type != DamageType.HG)){
					if (HT / 2 >= HP) {
						Buff.affect(hero, Swiftthistle.TimeBubble.class).setLeft(5f);
					} else {
						Buff.affect(hero, Swiftthistle.TimeBubble.class).setLeft(2f);
					}
					props.timeB = 0;
				}
			}

		}

		if(Dungeon.isChallenged(CS) && !gameNight) {
			dmg = (int) Math.ceil(dmg * 0.92);
		} else if(gameTime>350 && gameTime<400) {
			dmg = (int) Math.ceil(dmg * 0.96);
		}

		if(hero.belongings.getItem(EmotionalAggregationB.class)!=null && !(src instanceof Buff) && !(src instanceof Blob) && !(AntiMagic.RESISTS.contains(src.getClass()))){
			dmg += (int) getZone()*2 -1;
		}

		if (buff(TimekeepersHourglass.timeStasis.class) != null)
			return;

		//regular damage interrupt, triggers on any damage except specific mild DOT effects
		// unless the player recently hit 'continue moving', in which case this is ignored
		if (!(src instanceof Hunger || src instanceof Viscosity.DeferedDamage || src instanceof BloodLoss) && damageInterrupt ) {
			interrupt();
			resting = false;
		}

		if (this.buff(Drowsy.class) != null){
			Buff.detach(this, Drowsy.class);
			GLog.w( Messages.get(this, "pain_resist") );
		}

		Endure.EndureTracker endure = buff(Endure.EndureTracker.class);
		if (!(src instanceof Char)){
			//reduce damage here if it isn't coming from a character (if it is we already reduced it)
			if (endure != null){
				dmg = Math.round(endure.adjustDamageTaken(dmg));
			}
			//the same also applies to challenge scroll damage reduction
			if (buff(ScrollOfChallenge.ChallengeArena.class) != null){
				dmg *= 0.67f;
			}
			//and to monk meditate damage reduction
			if (buff(MonkEnergy.MonkAbility.Meditate.MeditateResistance.class) != null){
				dmg *= 0.2f;
			}
		}

		ScrollOfSoul.UpgradeSoul upgradeSoul = hero.buff(ScrollOfSoul.UpgradeSoul.class);
		if(upgradeSoul != null){
			float atkMul = 1f + upgradeSoul.shieldDamageMulti / 100f;
			atkMul = Math.min(0.1f, atkMul);
			dmg *= atkMul;
		}

		CapeOfThorns.Thorns thorns = buff( CapeOfThorns.Thorns.class );

		CapeOfThorns.ThornsTime thornsTime = buff(CapeOfThorns.ThornsTime.class);

		if (thorns != null) {
			if(thornsTime != null){
				dmg = thorns.proc(dmg, (src instanceof Char ? (Char)src : null));
			}
		}

		Talent.WarriorFoodImmunity thornsTalent = buff( Talent.WarriorFoodImmunity.class );
		if (thornsTalent != null) {
			dmg = thornsTalent.proc(dmg, (src instanceof Char ? (Char)src : null),  this);
		}

		dmg = (int)Math.ceil(dmg * RingOfTenacity.damageMultiplier( this ));

		//TODO improve this when I have proper damage source logic
		if (belongings.armor() != null && belongings.armor().hasGlyph(AntiMagic.class, this)
				&& AntiMagic.RESISTS.contains(src.getClass())){
			dmg -= AntiMagic.drRoll(this, belongings.armor().buffedLvl());
		}

		if (buff(Talent.WarriorFoodImmunity.class) != null){
			if (pointsInTalent(Talent.IRON_STOMACH) == 1){
				dmg = Math.round(dmg*0.25f);
				//Buff.affect(hero, CapeOfThorns.HeroThorns.class,1);
			} else if(pointsInTalent(Talent.IRON_STOMACH) == 2) {
				dmg = Math.round(dmg * 0.00f);
				//Buff.affect(hero, CapeOfThorns.HeroThorns.class,2);
			}
		}


		if(hasTalent(Talent.PAIN_SCAR) && HP+ shielding() -dmg<=0){
			int point = pointsInTalent(Talent.PAIN_SCAR);
			float ber = 0;

			if(buff(Berserk.class)!=null)
				ber = buff(Berserk.class).getPower();

			Ankh ankh = null;
			for (Ankh i : belongings.getAllItems(Ankh.class)) {
				if (ankh == null || i.isBlessed()) {
					ankh = i;
				}
			}

			boolean canResist = false;

			switch (point){

				case 1:
					if(ber>=0.2f&&(originalHT-resistHealth)>20) {
						canResist = true;
					}
					break;

				case 2:

					if(ber>=0.15f&&(originalHT-resistHealth)>15) {
						canResist = true;
					}
					break;

				case 3:

					if(ber>=0.1f&&(originalHT-resistHealth)>10) {
						canResist = true;
					}
					break;
			}

			int RH = resistHealth;

			if(ankh != null && canResist){
				WarriorDead();
				return;
			} else if (canResist) {
				WarriorHPLimit();
			}
			if(RH < resistHealth) return;

		}

		int preHP = HP + shielding();

		int preTrueHP = HP;

		if (src instanceof Hunger) preHP -= shielding();
		super.damage( dmg, src , type);
		int postHP = HP + shielding();
		if (src instanceof Hunger) postHP -= shielding();
		int effectiveDamage = preHP - postHP;

		if(belongings.getItem(ArmorScalesOfBzmdr.class)!=null) effectiveDamage -= (int) getZone();

		if (effectiveDamage <= 0) return;

		int trueDamage=preTrueHP-HP;

		if (trueDamage>0){
			if (this.hasTalent(Talent.LIQUID_WILLPOWER )){

				/*
				Class<?> srcClass = src.getClass();
				HashSet<Class> resists = new HashSet<>(RingOfElements.RESISTS);
				boolean flag = true;
				for (Class c : resists){
					if (c.isAssignableFrom(srcClass)){
						flag=false;
						break;
					}
				}
				*/
				boolean flag = (type == DamageType.PHYSICAL);
				if (flag) {
					Buff.affect(this,Barrier.class).setShield(2*pointsInTalent(Talent.LIQUID_WILLPOWER));
				}
			}
		}

		if (buff(Challenge.DuelParticipant.class) != null){
			buff(Challenge.DuelParticipant.class).addDamage(effectiveDamage);
		}

		//flash red when hit for serious damage.
		float percentDMG = effectiveDamage / (float)preHP; //percent of current HP that was taken
		float percentHP = 1 - ((HT - postHP) / (float)HT); //percent health after damage was taken
		// The flash intensity increases primarily based on damage taken and secondarily on missing HP.
		float flashIntensity = 0.25f * (percentDMG * percentDMG) / percentHP;
		//if the intensity is very low don't flash at all
		if (flashIntensity >= 0.05f){
			flashIntensity = min(1/3f, flashIntensity); //cap intensity at 1/3
			GameScene.flash( (int)(0xFF*flashIntensity) << 16 );
			if (isAlive()) {
				if (flashIntensity >= 1/6f) {
					Sample.INSTANCE.play(Assets.Sounds.HEALTH_CRITICAL, 1/3f + flashIntensity * 2f);
				} else {
					Sample.INSTANCE.play(Assets.Sounds.HEALTH_WARN, 1/3f + flashIntensity * 4f);
				}
				//hero gets interrupted on taking serious damage, regardless of any other factor
				interrupt();
				resting = false;
				damageInterrupt = true;
			}
		}
	}

	public void checkVisibleMobs() {
		ArrayList<Mob> visible = new ArrayList<>();

		boolean newMob = false;

		Mob target = null;
		for (Mob m : level.mobs.toArray(new Mob[0])) {
			if (fieldOfView[ m.pos ] && m.landmark() != null){
				Notes.add(m.landmark());
			}

			if (fieldOfView[ m.pos ] && m.alignment == Alignment.ENEMY) {
				visible.add(m);
				if (!visibleEnemies.contains( m )) {
					newMob = true;
				}

				//only do a simple check for mind visioned enemies, better performance
				if ((!mindVisionEnemies.contains(m) && QuickSlotButton.autoAim(m) != -1)
						|| (mindVisionEnemies.contains(m) && new Ballistica( pos, m.pos, Ballistica.PROJECTILE ).collisionPos == m.pos)) {
					if (target == null) {
						target = m;
					} else if (distance(target) > distance(m)) {
						target = m;
					}
					if (m instanceof Snake && level.distance(m.pos, pos) <= 4
							&& !Document.ADVENTURERS_GUIDE.isPageRead(Document.GUIDE_EXAMINING)){
						GameScene.flashForDocument(Document.ADVENTURERS_GUIDE, Document.GUIDE_EXAMINING);
						//we set to read here to prevent this message popping up a bunch
						Document.ADVENTURERS_GUIDE.readPage(Document.GUIDE_EXAMINING);
					}
				}
			}
		}

		Char lastTarget = QuickSlotButton.lastTarget;
		if (target != null && (lastTarget == null ||
				!lastTarget.isAlive() || !lastTarget.isActive() ||
				lastTarget.alignment == Alignment.ALLY ||
				!fieldOfView[lastTarget.pos])){
			QuickSlotButton.target(target);
		}

		if (newMob) {
			if (resting){
				Dungeon.observe();
			}
			interrupt();
		}

		visibleEnemies = visible;

		//we also scan for blob landmarks here
		for (Blob b : level.blobs.values().toArray(new Blob[0])){
			if (b.volume > 0 && b.landmark() != null && !Notes.contains(b.landmark())){
				int cell;
				boolean found = false;
				//if a single cell within the blob is visible, we add the landmark
				for (int i=b.area.top; i < b.area.bottom; i++) {
					for (int j = b.area.left; j < b.area.right; j++) {
						cell = j + i* level.width();
						if (fieldOfView[cell] && b.cur[cell] > 0) {
							Notes.add( b.landmark() );
							found = true;
							break;
						}
					}
					if (found) break;
				}

				//Clear blobs that only exist for landmarks.
				// Might want to make this a properly if it's used more
				if (found && b instanceof WeakFloorRoom.WellID){
					b.fullyClear();
				}
			}
		}
	}

	public int visibleEnemies() {
		return visibleEnemies.size();
	}

	public Mob visibleEnemy( int index ) {
		return visibleEnemies.get(index % visibleEnemies.size());
	}

	public ArrayList<Mob> getVisibleEnemies(){
		return new ArrayList<>(visibleEnemies);
	}

	private boolean walkingToVisibleTrapInFog = false;

	//FIXME this is a fairly crude way to track this, really it would be nice to have a short
	//history of hero actions
	public boolean justMoved = false;

	private boolean getCloser( final int target ) {

		if (target == pos)
			return false;

		if (rooted) {
			PixelScene.shake( 1, 1f );
			return false;
		}

		int step = -1;

		if(Dungeon.onlyBoxMovement()){
			int w = level.width();
			int dx = (target % w) - (pos % w);
			int dy = (target / w) - (pos / w);
			boolean adjacent = (Math.abs(dx) + Math.abs(dy)) == 1;
			if(!adjacent){
				sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "not_move"));
				spend(0f);
				return false;
			}
		}

		if (level.adjacent( pos, target )) {

			path = null;

			if (Actor.findChar( target ) == null) {
				if (level.passable[target] || level.avoid[target]) {
					step = target;
				}
				if (walkingToVisibleTrapInFog
						&& level.traps.get(target) != null
						&& level.traps.get(target).visible){
					return false;
				}
			}

		} else {

			boolean newPath = false;
			if (path == null || path.isEmpty() || !level.adjacent(pos, path.getFirst()))
				newPath = true;
			else if (path.getLast() != target)
				newPath = true;
			else {
				if (!level.passable[path.get(0)] || Actor.findChar(path.get(0)) != null) {
					newPath = true;
				}
			}

			if (newPath) {

				int len = level.length();
				boolean[] p = level.passable;
				boolean[] v = level.visited;
				boolean[] m = level.mapped;
				boolean[] passable = new boolean[len];
				for (int i = 0; i < len; i++) {
					passable[i] = p[i] && (v[i] || m[i]);
				}

				PathFinder.Path newpath = Dungeon.findPath(this, target, passable, fieldOfView, true);
				if (newpath != null && path != null && newpath.size() > 2*path.size()){
					path = null;
				} else {
					path = newpath;
				}
			}

			if (path == null) return false;
			step = path.removeFirst();

		}

		if (step != -1) {


			float delay = 1 / speed();

			if (buff(GreaterHaste.class) != null){
				delay = 0;
			}

			if (level.pit[step] && !level.solid[step]
					&& (!flying || buff(Levitation.class) != null && buff(Levitation.class).detachesWithinDelay(delay))){
				if (!Chasm.jumpConfirmed){
					Chasm.heroJump(this);
					interrupt();
				} else {
					flying = false;
					remove(buff(Levitation.class)); //directly remove to prevent cell pressing
					Chasm.heroFall(target);
				}
				canSelfTrample = false;
				return false;
			}

			if (buff(GreaterHaste.class) != null){
				buff(GreaterHaste.class).spendMove();
			}

			if (subClass == HeroSubClass.FREERUNNER){
				Buff.affect(this, Momentum.class).gainStack();
			}

			sprite.move(pos, step);
			move(step);

			spend( delay );
			justMoved = true;

			search(false);

			return true;

		} else {

			return false;

		}

	}

	public boolean handle( int cell ) {

		if (cell == -1) {
			return false;
		}

		if (fieldOfView == null || fieldOfView.length != level.length()){
			fieldOfView = new boolean[level.length()];
			level.updateFieldOfView( this, fieldOfView );
		}

		Char ch = Actor.findChar( cell );
		Heap heap = level.heaps.get( cell );

		if (level.map[cell] == Terrain.ALCHEMY && cell != pos) {

			curAction = new HeroAction.Alchemy( cell );

		} else if (fieldOfView[cell] && ch instanceof Mob) {

			if (((Mob) ch).heroShouldInteract()) {
				curAction = new HeroAction.Interact( ch );
			} else {
				curAction = new HeroAction.Attack( ch );
			}

			//TODO perhaps only trigger this if hero is already adjacent? reducing mistaps
		} else if ((level instanceof MiningLevel || level instanceof DragonFestivalMiniLevel) &&
				belongings.getItem(Pickaxe.class) != null &&
				(level.map[cell] == Terrain.WALL
						|| level.map[cell] == Terrain.WALL_DECO
						|| level.map[cell] == Terrain.MINE_CRYSTAL
						|| level.map[cell] == Terrain.MINE_BOULDER)){

			curAction = new HeroAction.Mine( cell );

		} else if (heap != null
				//moving to an item doesn't auto-pickup when enemies are near...
				&& (visibleEnemies.isEmpty() || cell == pos ||
				//...but only for standard heaps. Chests and similar open as normal.
				(heap.type != Type.HEAP && heap.type != Type.FOR_SALE && heap.type != Type.FOR_ICE && heap.type != Type.FOR_RUSH))) {

			switch (heap.type) {
				case HEAP:
					curAction = new HeroAction.PickUp( cell );
					break;
				case FOR_SALE:
					curAction = heap.size() == 1 && heap.peek().value() > 0 ?
							new HeroAction.Buy( cell ) :
							new HeroAction.PickUp( cell );
					break;
				case FOR_ICE:
					curAction = heap.size() == 1 && heap.peek().iceCoinValue() > 0 ?
							new HeroAction.BuyIce( cell ) :
							new HeroAction.PickUp( cell );
					break;
				case FOR_RUSH:
					curAction = heap.size() == 1 && heap.peek().RushValue() > 0 ?
							new HeroAction.BuyRush( cell ) :
							new HeroAction.PickUp( cell );
					break;
				default:
					curAction = new HeroAction.OpenChest( cell );
			}

		} else if (level.map[cell] == Terrain.LOCKED_DOOR || level.map[cell] == Terrain.CRYSTAL_DOOR || level.map[cell] == Terrain.LOCKED_EXIT || level.map[cell] == Terrain.GOLDEN_DOOR) {

			curAction = new HeroAction.Unlock( cell );

		} else if (level.getTransition(cell) != null
				&& (visibleEnemies.isEmpty() || cell == pos)
				&& !level.locked
				&& ( HolidayEvent() ||
				level.getTransition(cell).type == LevelTransition.Type.REGULAR_ENTRANCE) ) {

			curAction = new HeroAction.LvlTransition( cell );

		}  else {

			walkingToVisibleTrapInFog = !level.visited[cell] && !level.mapped[cell]
					&& level.traps.get(cell) != null && level.traps.get(cell).visible;

			curAction = new HeroAction.Move( cell );
			lastAction = null;

		}

		return true;
	}

	/** 节日深度 */
	private boolean HolidayEvent() {
		boolean result;
		if(bossRushMode){
			result = Dungeon.depth < 43;
		} else if(Statistics.Hollow_Holiday) {
			result = Dungeon.depth < 35;
		} else if(holiday == RegularLevel.WestHoliday.XMAS) {
			result = Dungeon.depth < 31;
		} else {
			result = Dungeon.depth < 27;
		}
		return result;
	}

	public void earnExp( int exp, Class source ) {

		//xp granted by ascension challenge is only for on-exp gain effects
		if (source != AscensionChallenge.class) {
			this.exp += exp;
		}



		float percent = exp/(float)maxExp();

		EtherealChains.chainsRecharge chains = buff(EtherealChains.chainsRecharge.class);
		if (chains != null) chains.gainExp(percent);

		HornOfPlenty.hornRecharge horn = buff(HornOfPlenty.hornRecharge.class);
		if (horn != null) horn.gainCharge(percent);

		AlchemistsToolkit.kitEnergy kit = buff(AlchemistsToolkit.kitEnergy.class);
		if (kit != null) kit.gainCharge(percent);

		MasterThievesArmband.Thievery armband = buff(MasterThievesArmband.Thievery.class);
		if (armband != null) armband.gainCharge(percent);

		Berserk berserk = buff(Berserk.class);
		if (berserk != null) berserk.recover(percent);

		if (source != PotionOfExperience.class) {
			for (Item i : belongings) {
				i.onHeroGainExp(percent, this);
			}
			if (buff(Talent.RejuvenatingStepsFurrow.class) != null){
				buff(Talent.RejuvenatingStepsFurrow.class).countDown(percent*200f);
				if (buff(Talent.RejuvenatingStepsFurrow.class).count() <= 0){
					buff(Talent.RejuvenatingStepsFurrow.class).detach();
				}
			}
			if (buff(ElementalStrike.ElementalStrikeFurrowCounter.class) != null){
				buff(ElementalStrike.ElementalStrikeFurrowCounter.class).countDown(percent*20f);
				if (buff(ElementalStrike.ElementalStrikeFurrowCounter.class).count() <= 0){
					buff(ElementalStrike.ElementalStrikeFurrowCounter.class).detach();
				}
			}
		}

		boolean levelUp = false;
		while (this.exp >= maxExp()) {
			this.exp -= maxExp();

			if (buff(Talent.WandPreservationCounter.class) != null
					&& pointsInTalent(Talent.WAND_PRESERVATION) == 2){
				buff(Talent.WandPreservationCounter.class).detach();
			}

			if (lvl < (Statistics.Hollow_Holiday ? 35 : MAX_LEVEL)) {
				lvl++;
				levelUp = true;

				if (buff(ElixirOfMight.HTBoost.class) != null){
					buff(ElixirOfMight.HTBoost.class).onLevelUp();
				}

				updateHT( true );
				attackSkill++;
				defenseSkill++;

			} else {
				Buff.prolong(this, Bless.class, Bless.DURATION);
				this.exp = 0;

				GLog.newLine();
				GLog.p( Messages.get(this, "level_cap"));
				Sample.INSTANCE.play( Assets.Sounds.LEVELUP );
			}

		}

		if (levelUp) {

			if (sprite != null) {
				GLog.newLine();
				GLog.p( Messages.get(this, "new_level") );
				sprite.showStatus( CharSprite.POSITIVE, Messages.get(Hero.class, "level_up") );
				Sample.INSTANCE.play( Assets.Sounds.LEVELUP );
				if(lvl<31){
					if (lvl < Talent.tierLevelThresholds[Talent.MAX_TALENT_TIERS+1]){
						GLog.newLine();
						GLog.p( Messages.get(this, "new_talent") );
						StatusPane.talentBlink = 10f;
						WndHero.lastIdx = 1;
					}
				}
			}

            if (((float) this.exp /maxExp()) >= 0.5f && belongings.getItem(WandOfAnmy.class) != null && !chargeAnmy) {
                belongings.getItem(WandOfAnmy.class).expCharge(this);
                chargeAnmy = true;
            }

            chargeAnmy = false;

			Item.updateQuickslot();
			BloodBat.updateHP();
			Badges.validateLevelReached();
		}else {

            if (((float) this.exp /maxExp()) >= 0.5f && belongings.getItem(WandOfAnmy.class) != null && !chargeAnmy) {
                belongings.getItem(WandOfAnmy.class).expCharge(this);
                chargeAnmy = true;
            }
        }
	}

	public int maxExp() {
		return maxExp( lvl );
	}

	public static int maxExp( int lvl ){
		return 5 + lvl * 5;
	}

	public boolean isStarving() {
		return Buff.affect(this, Hunger.class).isStarving();
	}

	public boolean isSmallHunger() {
		return Buff.affect(this, Hunger.class).isSmallHunger();
	}

	@Override
	public boolean add( Buff buff ) {

		if (buff(TimekeepersHourglass.timeStasis.class) != null) {
			return false;
		}

		boolean added = super.add( buff );

		if (sprite != null && added) {
			String msg = buff.heroMessage();
			if (msg != null && !msg.isEmpty()){
				GLog.w(msg);
			}

			if (buff instanceof Paralysis || buff instanceof Vertigo) {
				interrupt();
			}

		}

		BuffIndicator.refreshHero();

		return added;
	}

	@Override
	public boolean remove( Buff buff ) {
		if (super.remove( buff )) {
			BuffIndicator.refreshHero();
			return true;
		}
		return false;
	}

	@Override
	public float stealth() {
		float stealth = super.stealth();

		if (belongings.armor() != null){
			stealth = belongings.armor().stealthFactor(this, stealth);
		}

		return stealth;
	}

	public static void badLanterFire() {
		switch (Random.Int(5)) {
			case 0:
			default:
				if(Dungeon.shopOnLevel()){
					Buff.affect(hero, MagicGirlSayMoneyMore.class).set((100), 1);
				}
				break;
			case 1:
				Buff.affect(hero, MagicGirlSayCursed.class).set((100), 1);
				break;
			case 2:
				Buff.affect(hero, MagicGirlSaySlowy.class).set((100), 1);
				break;
			case 3:
				Buff.affect(hero, MagicGirlSayKill.class).set((100), 1);
				break;
			case 4:
				Buff.affect(hero, MagicGirlSayNoSTR.class).set((100), 1);
				break;
		}
		GLog.n(Messages.get(WndStory.class, "bad"));
	}

	@Override
	public void storeInBundle(Bundle bundle) {

		super.storeInBundle(bundle);

		bundle.put(CLASS, heroClass);
		bundle.put(SUBCLASS, subClass);
		bundle.put(ABILITY, armorAbility);
		Talent.storeTalentsInBundle(bundle, this);

		bundle.put(ATTACK, attackSkill);
		bundle.put(DEFENSE, defenseSkill);
		bundle.put(RESIST, resistHealth);

		bundle.put(STRENGTH, STR);

		bundle.put(LEVEL, lvl);
		bundle.put(EXPERIENCE, exp);

		bundle.put(HTBOOST, HTBoost);

		bundle.put(ICEHP, icehp);

		bundle.put(LANTERFTR, lanterfire);

		bundle.put(CAKEUSED, CakeUsed);
		bundle.put(CHARGES,chargesUsed);

		if (!this.name.equals("")) {
			bundle.put("name", this.name);
		}

		belongings.storeInBundle(bundle);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {

		lvl = bundle.getInt( LEVEL );
		exp = bundle.getInt( EXPERIENCE );

		HTBoost = bundle.getInt(HTBOOST);

		super.restoreFromBundle( bundle );

		heroClass = bundle.getEnum(CLASS, HeroClass.class);
		subClass = bundle.getEnum(SUBCLASS, HeroSubClass.class);
		armorAbility = (ArmorAbility) bundle.get(ABILITY);
		Talent.restoreTalentsFromBundle(bundle, this);

		attackSkill = bundle.getInt(ATTACK);
		defenseSkill = bundle.getInt(DEFENSE);
		resistHealth = bundle.getInt(RESIST);

		STR = bundle.getInt(STRENGTH);

		lanterfire = bundle.getInt(LANTERFTR);

		icehp = bundle.getInt(ICEHP);

		CakeUsed = bundle.getInt(CAKEUSED);
		chargesUsed = bundle.getInt(CHARGES);

		String name;
		if (bundle.contains("name")) {
			name = bundle.getString("name");
		} else {
			name = "";
		}

		//This Custom Name Logic
		this.name = name;
		int csname;
		if (bundle.contains("up_names")) {
			String[] unames = bundle.getStringArray("up_names");
			int[] csname2 = bundle.getIntArray("up_vals");

			for (csname = 0; csname < unames.length && csname < csname2.length; ++csname) {
				this.upgrades.put(unames[csname], csname2[csname]);
			}
		}

		//May Be Working
		for (csname = 1; csname <= 5; ++csname) {
			StringBuilder str = new StringBuilder();
			str.append("craft_names_");
			str.append(csname);
			if (bundle.contains(str.toString())) {
				str = new StringBuilder();
				str.append("craft_names_");
				str.append(csname);
				String[] enus = bundle.getStringArray(str.toString());
				String pre = "craft_vals_" + csname;
				int[] trys = bundle.getIntArray(pre);
				LinkedHashMap<String, Integer> lname = new LinkedHashMap<>();

				for (int lisx = 0; lisx < enus.length && lisx < trys.length; ++lisx) {
					lname.put(enus[lisx], trys[lisx]);
				}

				this.crafted.put(csname, lname);
			}
		}

		belongings.restoreFromBundle(bundle);
	}

	private void MoveWater(){

		if(Dungeon.depth == 26 && branch == 10 && level.water[pos] && flying){
			Buff.prolong( hero, Slow.class, 2f);
		} else {
			Buff.detach( hero, Slow.class);
		}

		//新 污泥浊水--密林效果
		if (Dungeon.isChallenged(AQUAPHOBIA) && hero.buff(WaterSoulX.class) != null && level.water[pos] && Dungeon.GodWaterLevel()){
			Buff.affect(hero, Barkskin.class).set( 2 + hero.lvl/2, 5 );
			Buff.prolong(this, Bless.class, Bless.GODSPOERF);
		} else if (Dungeon.GodWaterLevel() && level.water[pos] && !Dungeon.isChallenged(AQUAPHOBIA)){
			Buff.affect(hero, Barkskin.class).set( 2 + hero.lvl/4, 10 );
			Buff.prolong(this, Bless.class, Bless.GODSPOERF);
		}

		//新 污泥浊水--雪狱效果
		if (Dungeon.PrisonWaterLevel()&& level.water[pos] && Dungeon.isChallenged(AQUAPHOBIA) && hero.buff(WaterSoulX.class) != null){
			Buff.affect(this, HasteLing.class, Haste.DURATION/10);
		} else if (Dungeon.PrisonWaterLevel() && level.water[pos]&& !Dungeon.isChallenged(AQUAPHOBIA)){
			Buff.affect(this, HasteLing.class, Haste.DURATION/20);
		} else if(Dungeon.PrisonWaterLevel()&& !level.water[pos])
			for (Buff buff : hero.buffs()) {
				if (buff instanceof InvisibilityRing||buff instanceof HasteLing) {
					buff.detach();
				}
			}
		if(Dungeon.ColdWaterLevel() && level.water[pos] && flying && Dungeon.isChallenged(AQUAPHOBIA)) {
			for (Buff buff : hero.buffs()) {
				if (buff instanceof Chill) {
					buff.detach();
				}
			}
		} else if (Dungeon.ColdWaterLevel()&& level.water[pos] && hero.buff(WaterSoulX.class) != null && Dungeon.isChallenged(AQUAPHOBIA)){
			Buff.affect(this, FrostImbueEX.class, FrostImbueEX.DURATION*0.5f);
		} else if (Dungeon.ColdWaterLevel()&& level.water[pos] && !Dungeon.isChallenged(AQUAPHOBIA)){
			Buff.affect(this, FrostImbueEX.class, FrostImbueEX.DURATION*0.3f);
		} else if(Dungeon.ColdWaterLevel()&& !level.water[pos])
			for (Buff buff : hero.buffs()) {
				if (buff instanceof FrostImbueEX) {
					buff.detach();
				}
			}
	}


	public void moves(int step) {
		ScrollOfTeleportation.appear(hero, step);
	}

	@SuppressWarnings("unchecked")
	private boolean actMove( HeroAction.Move action ) {
		collectSpecialItems();
		CapeOfThorns.HeroThorns thornsTalent = buff( CapeOfThorns.HeroThorns.class );
		if(thornsTalent != null){
			thornsTalent.detach();
		}

		if(!seedCustom && !Dungeon.customSeedText.isEmpty()){
			seedCustom = true;
		}

		PotionOfPurity.PotionOfPurityLing potionOfPurityLing = hero.belongings.getItem(PotionOfPurity.PotionOfPurityLing.class);
		if (potionOfPurityLing != null && !level.locked) {potionOfPurityLing.detachAll(hero.belongings.backpack);}

		RedWhiteRose redWhiteRose = hero.belongings.getItem(RedWhiteRose.class);
		if (redWhiteRose != null) {
			Buff.affect(hero, BlessRedWhite.class).set((100), 1);
		} else {
			Buff.detach(hero, BlessRedWhite.class);
		}

		LingJing lingJing = hero.belongings.getItem(LingJing.class);
		if(lingJing != null && Statistics.deepestFloor <11) {
			Buff.affect(hero, BlessLingJing.class).set((100), 1);
		} else {
			Buff.detach(hero, BlessLingJing.class);
			Buff.detach(hero, BlessLingJing.LanterBarrier.class);
		}

		DriedRose rose = hero.belongings.getItem(DriedRose.class);
		Red red = hero.belongings.getItem(Red.class);
		if (red != null && Statistics.deadGo) {
			red.detachAll(hero.belongings.backpack);
		}
		if (rose != null && Statistics.deadGo) {
			rose.detachAll(hero.belongings.backpack);
		}

		CrystalLing crystalLing = hero.belongings.getItem(CrystalLing.class);
		if (crystalLing != null) {
			Buff.affect(hero, BlessLing.class).set((100), 1);
		} else {
			Buff.detach(hero, BlessLing.class);
		}

		for (Buff buff : hero.buffs()) {
			if (HelpSettings() && !(buff instanceof GameTracker)) {
				Buff.affect(this, GameTracker.class);
			}
		}

		if (hero.exp < 0 && !(Dungeon.isDLC(Conducts.Conduct.DEV))) {
			exp = Random.NormalIntRange(10, 20);
		}

		MIME.GOLD_TWO getFalseBody = hero.belongings.getItem(MIME.GOLD_TWO.class);
		if ( getFalseBody != null && HT / 5 > HP && hero.buff(MIME.GoldTwoCooldown.class) == null ) {
			//给予一个看不见的隐形Buff,继承至Invisibility
			Buff.affect(this, InvisibilityRing.class, 40f );
			Buff.affect(this, MIME.GoldTwoCooldown.class, 300f );
		}

		if (getCloser(action.dst)) {
			canSelfTrample = false;

			if(belongings.weapon() instanceof DogDogMusic.CICREMUSIC){
				DogDogMusic.CicreStats cicreStats = buff(DogDogMusic.CicreStats.class);
				if(cicreStats != null && cicreStats.attackStats !=0){
					cicreStats.attackStats = 0;
				}
			}

			return true;

			//Hero moves in place if there is grass to trample
		} else if (pos == action.dst && canSelfTrample()) {
			canSelfTrample = false;
			level.pressCell(pos);
			spendAndNext(1 / speed());
			return false;
		} else {
			ready();
			return false;
		}
	}

	private void collectSpecialItems() {
		Heap heap = level.heaps.get(pos);
		if (heap != null) {
			ArrayList<Item> itemsToCollect = new ArrayList<>();

			for (Item item : heap.items) {
				if (item instanceof PacManQuest) {
					if (Dungeon.depth == 31 && branch == 1) {
						itemsToCollect.add(item);
					}
				}
			}

			for (Item item : itemsToCollect) {
				if (item instanceof PacManQuest) {
					((PacManQuest) item).autocollect(item, pos);
					heap.remove(item);
				}
			}

			if (heap.isEmpty()) {
				level.heaps.remove(pos);
			}
		}
	}



	@Override
	public void die( Object cause ) {

		curAction = null;

		Ankh ankh = null;

		UnlessFlower unlessFlower = hero.belongings.getItem(UnlessFlower.class);

		if(unlessFlower != null){
			ankh = unlessFlower;
		} else {
			for (Ankh i : belongings.getAllItems(Ankh.class)) {
					if (i instanceof MIME.GOLD_FIVE) {
						ankh = i;
						break;
					}
					if (ankh == null || i.isBlessed()) {
						ankh = i;
					}
				}
		}



		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (mob instanceof BlackSoul) {
				Buff.affect(mob, Dread.class);
			}
		}

		if( buff(ElectricalSmoke.SmokingAlloy.class) != null)
			GLog.n(Messages.get(ElectricalSmoke.class,"die"));

		boolean OnlySummonAlive = false;
		//灯火值低于40 死亡生成自己的邪恶面，并清空金币，背包也一并带走。（灵感：空洞骑士）
		for (Ankh i : belongings.getAllItems(Ankh.class)) {
			if (ankh != null && !(i.isBlessed()) && !OnlySummonAlive) {
				if (lanterfireactive && hero.lanterfire <= 40 && !i.isBlessed() || hero.buff(LostInventory.class) != null) {
					BlackSoul s = new BlackSoul();
					if(Statistics.ankhToExit){
						s.pos = Dungeon.level.entrance();
					} else {
						s.pos = Dungeon.hero.pos;
					}
					s.gold = Dungeon.gold;
					Dungeon.gold = 0;
					s.state = s.WANDERING;
					GameScene.add(s);
					Buff.affect(s, ChampionEnemy.DeadSoulSX.class);
					Buff.affect(s, DeadSoul.class);
					OnlySummonAlive = true;
					GameScene.flash(0x80FF0000);
				}
			}
		}

		// 深度31特殊复活（保留原逻辑）
		if(Dungeon.depth == 31 && (Statistics.Hollow_Holiday || Dungeon.isDLC(Conducts.Conduct.DEV)) && branch != 0){
			interrupt();
			resting = false;
			this.HP = HT / 4;
			PotionOfHealing.cure(this);
			Buff.prolong(this, Invulnerability.class, Invulnerability.DURATION);
			SpellSprite.show(this, SpellSprite.ANKH);
			GameScene.flash(0x80FFFF40);

			if(branch == 3){
				Game.runOnRenderThread(() -> GameScene.show(new WinAllSearchStatus()));
				if(hero.buff(ScoreBuff.class)!=null) {
					ScoreBuff buffs = hero.buff(ScoreBuff.class);
					SPDSettings.AllSearchScore(buffs.score/2);
					Statistics.getAlLSearchScore = buffs.score/2;
				}
			}
			return;
		}

		// ============== 十字章复活逻辑（顺序不变：MIME → 祝福 → 普通） ==============
		else if (ankh != null) {
			interrupt();
			resting = false;

			if(ankh instanceof UnlessFlower){
				this.HP = HT;
				interrupt();
				PotionOfHealing.cure(this);
				Buff.affect(this, UnlessFlower.UnlessFlowerTime.class).set(10000, 1 );
				SpellSprite.show(this, SpellSprite.ANKH);
				GameScene.flash(0x80FFFF40);
				GLog.w(Messages.get(this, "heart_god"));
				ankh.detach(belongings.backpack);
			} else if(ankh instanceof MIME.GOLD_FIVE) {
				this.HP = HT;
				interrupt();
				PotionOfHealing.cure(this);
				Buff.prolong(this, Invulnerability.GodDied.class, Invulnerability.DURATION*10f);
				SpellSprite.show(this, SpellSprite.ANKH);
				GameScene.flash(0x80FFFF40);
				Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
				GLog.w(Messages.get(this, "heartdied"));
				ankh.detach(belongings.backpack);
				Buff.detach(this,UnlessFlower.UnlessFlowerTime.class);
			} else if (ankh.isBlessed()) {
				this.HP = HT / 4;
				PotionOfHealing.cure(this);
				Buff.prolong(this, Invulnerability.class, Invulnerability.DURATION);
				SpellSprite.show(this, SpellSprite.ANKH);
				GameScene.flash(0x80FFFF40);
				Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
				GLog.w(Messages.get(this, "revive"));
				Statistics.ankhsUsed++;
				Buff.detach(this,UnlessFlower.UnlessFlowerTime.class);
				if(branch == 10 && Dungeon.depth == 26){
					GLog.w("索托斯：谨慎一点，再失误一次可就危险了。");
				}

				ankh.detach(belongings.backpack);

				for (Char ch : Actor.chars()) {
					if (ch instanceof DriedRose.GhostHero) {
						((DriedRose.GhostHero) ch).sayAnhk();
						return;
					}
				}
			} else {
				WndResurrect.instance = new Object();
				Ankh finalAnkh = ankh;
				Game.runOnRenderThread(() -> GameScene.show( new WndResurrect(finalAnkh) ));

				if (cause instanceof Doom) {
					((Doom)cause).onDeath();
				}

				SacrificialFire.Marked sacMark = buff(SacrificialFire.Marked.class);
				if (sacMark != null){
					sacMark.detach();
				}
			}
			return;
		}
		// 索托斯特殊关卡复活（保留原逻辑）
		else if(branch == 10 && Dungeon.depth == 26){
			this.HP = HT / 4;
			PotionOfHealing.cure(this);
			Buff.prolong(this, Invulnerability.class, Invulnerability.DURATION);
			SpellSprite.show(this, SpellSprite.ANKH);
			GameScene.flash(0x80FFFF40);
			Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
			ScrollOfTeleportation.appear(hero, 91);
			Statistics.TrueYogNoDied = true;
			GLog.w(Messages.get(Sothoth.class,"dead"));

			for (Mob mob : level.mobs.toArray(new Mob[0])) {
				if (mob instanceof Sothoth || mob instanceof SothothEyeDied) {
					mob.destroy();
				}
			}

			for (Mob mob : level.mobs.toArray(new Mob[0])){
				if (mob instanceof SothothLasher || mob instanceof ServantAvgomon) {
					mob.die(null);
				}
			}

			level.unseal();
			return;
		}

		Actor.fixTime();
		super.die( cause );
		reallyDie( cause );
	}

	@Override
	public boolean isAlive() {
		if (HP <= 0){
			if (berserk == null) berserk = buff(Berserk.class);
			return berserk != null && berserk.berserking();
		} else {
			berserk = null;
			return super.isAlive();
		}
	}

	@Override
	public void move(int step, boolean travelling) {
		boolean wasHighGrass = level.map[step] == Terrain.HIGH_GRASS;

		super.move( step, travelling);

		if (!flying && travelling) {
			if (level.water[pos]) {
				Sample.INSTANCE.play( Assets.Sounds.WATER, 1, Random.Float( 0.8f, 1.25f ) );
			} else if (level.map[pos] == Terrain.EMPTY_SP) {
				Sample.INSTANCE.play( Assets.Sounds.STURDY, 1, Random.Float( 0.96f, 1.05f ) );
			} else if (level.map[pos] == Terrain.GRASS
					|| level.map[pos] == Terrain.EMBERS
					|| level.map[pos] == Terrain.FURROWED_GRASS){
				if (step == pos && wasHighGrass) {
					Sample.INSTANCE.play(Assets.Sounds.TRAMPLE, 1, Random.Float( 0.96f, 1.05f ) );
				} else {
					Sample.INSTANCE.play( Assets.Sounds.GRASS, 1, Random.Float( 0.96f, 1.05f ) );
				}
			} else {
				Sample.INSTANCE.play( Assets.Sounds.STEP, 1, Random.Float( 0.96f, 1.05f ) );
			}
		}
	}

	@Override
	public void onAttackComplete() {

		if (enemy == null){
			curAction = null;
			super.onAttackComplete();
			return;
		}

		AttackIndicator.target(enemy);
		boolean wasEnemy = enemy.alignment == Alignment.ENEMY
				|| (enemy instanceof Mimic && enemy.alignment == Alignment.NEUTRAL);

		boolean hit = attack( enemy );

		Invisibility.dispel();
		spend( attackDelay() );

		Buff.detach(this, KnightStabbingSword.NoRoundTracker.class);

		if (hit && subClass == HeroSubClass.GLADIATOR && wasEnemy){
			Buff.affect( this, Combo.class ).hit(enemy);
		}

		if (hit && heroClass == HeroClass.DUELIST && wasEnemy){
			Buff.affect( this, Sai.ComboStrikeTracker.class).addHit();
		}

//		RingOfForce.BrawlersStance brawlStance = buff(RingOfForce.BrawlersStance.class);
//		if (brawlStance != null && brawlStance.hitsLeft() > 0){
//			MeleeWeapon.Charger charger = Buff.affect(this, MeleeWeapon.Charger.class);
//			charger.partialCharge -= RingOfForce.BrawlersStance.HIT_CHARGE_USE;
//			while (charger.partialCharge < 0) {
//				charger.charges--;
//				charger.partialCharge++;
//			}
//			BuffIndicator.refreshHero();
//			Item.updateQuickslot();
//		}

		curAction = null;

		super.onAttackComplete();
	}

	@Override
	public void onMotionComplete() {
		GameScene.checkKeyHold();
	}

	//万能解锁
	private boolean checkUnlocked() {
		return hero.buff(BlessUnlock.class) != null;
	}

	@Override
	public void onOperateComplete() {

		if (curAction instanceof HeroAction.Unlock) {

			int doorCell = curAction.dst;
			int door = level.map[doorCell];

			if (level.distance(pos, doorCell) <= 1) {
				boolean hasKey;
				if (door == Terrain.LOCKED_DOOR) {
					hasKey = Notes.remove(new IronKey(Dungeon.depth)) || checkUnlocked();
					if (hasKey) Level.set(doorCell, Terrain.DOOR);
					if(checkUnlocked()) Buff.detach(this,BlessUnlock.class);
				} else if (door == Terrain.CRYSTAL_DOOR) {
					hasKey = Notes.remove(new CrystalKey(Dungeon.depth)) || checkUnlocked();
					if (hasKey) {
						Level.set(doorCell, Terrain.EMPTY);
						Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
						CellEmitter.get( doorCell ).start( Speck.factory( Speck.DISCOVER ), 0.025f, 20 );
					}
					if(checkUnlocked()) Buff.detach(this,BlessUnlock.class);
				} else if (door == Terrain.GOLDEN_DOOR) {
					hasKey = Notes.remove(new GoldenKey(Dungeon.depth)) || checkUnlocked();
					if (hasKey) {
						Level.set(doorCell, Terrain.EMPTY);
						Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
						CellEmitter.get( doorCell ).start( Speck.factory( Speck.DISCOVER ), 0.025f, 20 );
					}
					if(checkUnlocked()) Buff.detach(this,BlessUnlock.class);
				} else {
					hasKey = Notes.remove(new SkeletonKey(Dungeon.depth));
					if (hasKey) Level.set(doorCell, Terrain.UNLOCKED_EXIT);
				}

				if (hasKey) {
					GameScene.updateKeyDisplay();
					GameScene.updateMap(doorCell);
					spend(Key.TIME_TO_UNLOCK);
				}
			}

		} else if (curAction instanceof HeroAction.OpenChest) {

			Heap heap = level.heaps.get( curAction.dst );

			if (level.distance(pos, heap.pos) <= 1){
				boolean hasKey = true;
				boolean noNeedKey = Dungeon.depth != 31 && branch != 3;

				if(noNeedKey){
					if (heap.type == Type.SKELETON || heap.type == Type.REMAINS) {
						Sample.INSTANCE.play( Assets.Sounds.BONES );
					} else if (heap.type == Type.LOCKED_CHEST){
						hasKey = Notes.remove(new GoldenKey(Dungeon.depth));
					} else if (heap.type == Type.CRYSTAL_CHEST){
						hasKey = Notes.remove(new CrystalKey(Dungeon.depth));
					} else if (heap.type == Type.BLACK){
						hasKey = Notes.remove(new BlackKey(Dungeon.depth));
					} else if(heap.type == Type.GREEN_CHSET){
						hasKey = Notes.remove(new GreenKey(Dungeon.depth));
					}
				}

				if(hasKey && heap.type == Type.WHITETOMB && Dungeon.depth>25) {
					GameScene.show(new WndOptions(new ItemSprite(heap),
							Messages.titleCase(Messages.get(BigEyeRoom.class, "name")),
							Messages.get(BigEyeRoom.class, "start_prompt"),
							Messages.get(BigEyeRoom.class, "enter_yes"),
							Messages.get(BigEyeRoom.class, "enter_no")) {
						@Override
						protected void onSelect(int index) {
							if (index == 0) {
								GameScene.updateKeyDisplay();
								heap.open(hero);
								Badges.WOC();
								spend(Key.TIME_TO_UNLOCK);
							}
						}
					});
				//TODO 全面搜查-需要优化
				} else if( Dungeon.depth == 31 && branch == 3){
					switch (heap.type){
						case GREEN_CHSET:
							hero.spendAndNext( 8f );
							GameScene.updateKeyDisplay();
							heap.open(this);
							spend(Key.TIME_TO_UNLOCK);
						break;
						case CRYSTAL_CHEST:
							hero.spendAndNext( 6f );
							GameScene.updateKeyDisplay();
							heap.open(this);
							spend(Key.TIME_TO_UNLOCK);
						break;
						case LOCKED_CHEST:
							hero.spendAndNext( 4f );
							GameScene.updateKeyDisplay();
							heap.open(this);
							spend(Key.TIME_TO_UNLOCK);
						break;
						case CHEST:
							hero.spendAndNext( 2f );
							GameScene.updateKeyDisplay();
							heap.open(this);
							spend(Key.TIME_TO_UNLOCK);
						break;
						case SKELETON: case REMAINS:case TOMB: case BLACK:
							GameScene.updateKeyDisplay();
							heap.open(this);
							spend(Key.TIME_TO_UNLOCK);
						break;
					}
				} else if (hasKey) {
					GameScene.updateKeyDisplay();
					heap.open(this);
					spend(Key.TIME_TO_UNLOCK);
				}
			}

		}
		curAction = null;

		if (!ready) {
			super.onOperateComplete();
		}
	}

	@Override
	public boolean isImmune(Class effect) {
		if (effect == Burning.class
				&& belongings.armor() != null
				&& belongings.armor().hasGlyph(Brimstone.class, this)){
			return true;
		}

		//远古免疫
		if (effect == ToxicGas.class && belongings.armor() != null && belongings.armor().hasAlowGlyph(AncityStone.class,
				this)){
			return true;
		}
		if (effect == Corrosion.class && belongings.armor() != null && belongings.armor().hasAlowGlyph(AncityStone.class, this)){
			return true;
		}
		if (effect == Poison.class && belongings.armor() != null && belongings.armor().hasAlowGlyph(AncityStone.class,
				this)){
			return true;
		}
		return super.isImmune(effect);
	}

	@Override
	public boolean isInvulnerable(Class effect) {
		return super.isInvulnerable(effect) || buff(Invulnerability.class) != null || buff(Invulnerability.GodDied.class) != null || buff(RoseShiled.class) != null || this.buff(RoseShiled.class) != null;
	}

	public boolean search( boolean intentional ) {

		if (!isAlive()) return false;

		boolean smthFound = false;

		boolean circular = pointsInTalent(Talent.WIDE_SEARCH) == 1;
		int distance = heroClass == HeroClass.ROGUE ? 2 : 1;
		if (hasTalent(Talent.WIDE_SEARCH)) distance++;

		boolean foresight = buff(Foresight.class) != null;
		boolean foresightScan = foresight && !level.mapped[pos];

		if (foresightScan){
			level.mapped[pos] = true;
		}

		if (foresight) {
			distance = Foresight.DISTANCE;
			circular = true;
		}

		Point c = level.cellToPoint(pos);

		TalismanOfForesight.Foresight talisman = buff( TalismanOfForesight.Foresight.class );
		boolean cursed = talisman != null && talisman.isCursed();

		int[] rounding = ShadowCaster.rounding[distance];

		int left, right;
		int curr;
		for (int y = Math.max(0, c.y - distance); y <= min(level.height()-1, c.y + distance); y++) {
			if (!circular){
				left = c.x - distance;
			} else if (rounding[Math.abs(c.y - y)] < Math.abs(c.y - y)) {
				left = c.x - rounding[Math.abs(c.y - y)];
			} else {
				left = distance;
				while (rounding[left] < rounding[Math.abs(c.y - y)]){
					left--;
				}
				left = c.x - left;
			}
			right = min(level.width()-1, c.x + c.x - left);
			left = Math.max(0, left);
			for (curr = left + y * level.width(); curr <= right + y * level.width(); curr++){

				if ((foresight || fieldOfView[curr]) && curr != pos) {

					if ((foresight && (!level.mapped[curr] || foresightScan))){
						GameScene.effectOverFog(new CheckedCell(curr, foresightScan ? pos : curr));
					} else if (intentional) {
						GameScene.effectOverFog(new CheckedCell(curr, pos));
					}

					if (foresight){
						level.mapped[curr] = true;
					}

					if (level.secret[curr]){

						Trap trap = level.traps.get( curr );
						float chance;

						//searches aided by foresight always succeed, even if trap isn't searchable
						if (foresight){
							chance = 1f;

							//otherwise if the trap isn't searchable, searching always fails
						} else if (trap != null && !trap.canBeSearched){
							chance = 0f;

							//intentional searches always succeed against regular traps and doors
						} else if (intentional){
							chance = 1f;

							//unintentional searches always fail with a cursed talisman
						} else if (cursed) {
							chance = 0f;

							//unintentional trap detection scales from 40% at floor 0 to 30% at floor 25
						} else if (level.map[curr] == Terrain.SECRET_TRAP) {
							chance = 0.4f - (Dungeon.depth / 250f);

							//unintentional door detection scales from 20% at floor 0 to 0% at floor 20
						} else {
							chance = 0.2f - (Dungeon.depth / 100f);
						}

						//don't want to let the player search though hidden doors in tutorial
						if (SPDSettings.intro()){
							chance = 0;
						}

						if (Random.Float() < chance) {

							int oldValue = level.map[curr];

							GameScene.discoverTile( curr, oldValue );

							level.discover( curr );

							ScrollOfMagicMapping.discover( curr );

							if (fieldOfView[curr]) smthFound = true;

							if (talisman != null){
								if (oldValue == Terrain.SECRET_TRAP){
									talisman.charge(2);
								} else if (oldValue == Terrain.SECRET_DOOR){
									talisman.charge(10);
								}
							}
						}
					}
				}
			}
		}

		if (intentional) {
			sprite.showStatus( CharSprite.DEFAULT, Messages.get(this, "search") );
			sprite.operate( pos );
			if (!level.locked) {
				if (cursed) {
					GLog.n(Messages.get(this, "search_distracted"));
					Buff.affect(this, Hunger.class).affectHunger(TIME_TO_SEARCH - (2 * HUNGER_FOR_SEARCH));
				} else {
					Buff.affect(this, Hunger.class).affectHunger(TIME_TO_SEARCH - HUNGER_FOR_SEARCH);
				}
			}
			spendAndNext(TIME_TO_SEARCH);

		}

		if (smthFound) {
			GLog.w( Messages.get(this, "noticed_smth") );
			Sample.INSTANCE.play( Assets.Sounds.SECRET );
			interrupt();
		}

		if (foresight){
			GameScene.updateFog(pos, Foresight.DISTANCE+1);
		}

		return smthFound;
	}

	public void resurrect() {

		HP = HT;
		live();

		if (lanterfireactive) {
			if (hero.lanterfire == 100) {
				goodLanterFire();
			} else if (hero.lanterfire <= 99 && hero.lanterfire >= 90) {
				goodLanterFire();
			} else if (hero.lanterfire <= 89 && hero.lanterfire >= 80 && Random.Float() <= 0.05f) {
				badLanterFire();
			} else if (hero.lanterfire <= 89 && hero.lanterfire >= 80 && Random.Float() <= 0.85f) {
				goodLanterFire();
			} else if (hero.lanterfire <= 89 && hero.lanterfire >= 80) {
				GLog.b(Messages.get(WndStory.class, "normoal"));
			} else if (hero.lanterfire <= 79 && hero.lanterfire >= 60 && Random.Float() <= 0.25f) {
				badLanterFire();
			} else if (hero.lanterfire <= 79 && hero.lanterfire >= 60 && Random.Float() <= 0.70f) {
				goodLanterFire();
			} else if (hero.lanterfire <= 79 && hero.lanterfire >= 60) {
				GLog.b(Messages.get(WndStory.class, "normoal"));
			} else if (hero.lanterfire <= 59 && hero.lanterfire >= 35 && Random.Float() <= 0.40f) {
				badLanterFire();
			} else if (hero.lanterfire <= 59 && hero.lanterfire >= 35 && Random.Float() <= 0.20f) {
				goodLanterFire();
			} else if (hero.lanterfire <= 59 && hero.lanterfire >= 35) {
				GLog.b(Messages.get(WndStory.class, "normoal"));
			} else if (hero.lanterfire <= 34 && hero.lanterfire >= 1 && Random.Float() <= 0.40f) {
				badLanterFire();
			} else if (hero.lanterfire <= 34 && hero.lanterfire >= 1) {
				GLog.b(Messages.get(WndStory.class, "normoal"));
			} else {
				badLanterFire();
			}
		}

		MagicalHolster holster = belongings.getItem(MagicalHolster.class);

		Buff.affect(this, LostInventory.class);
		Buff.affect(this, Invisibility.class, 3f);
		//lost inventory is dropped in interlevelscene

		//activate items that persist after lost inventory
		//FIXME this is very messy, maybe it would be better to just have one buff that
		// handled all items that recharge over time?
		for (Item i : belongings){
			if (i instanceof EquipableItem && i.isEquipped(this)){
				((EquipableItem) i).activate(this);
			} else if (i instanceof CloakOfShadows && i.keptThroughLostInventory() && hasTalent(Talent.LIGHT_CLOAK)){
				((CloakOfShadows) i).activate(this);
			} else if (i instanceof Wand && i.keptThroughLostInventory()){
				if (holster != null && holster.contains(i)){
					((Wand) i).charge(this, MagicalHolster.HOLSTER_SCALE_FACTOR);
				} else {
					((Wand) i).charge(this);
				}
			} else if (i instanceof MagesStaff && i.keptThroughLostInventory()){
				((MagesStaff) i).applyWandChargeBuff(this);
			}
		}

		resistHealth *= 0.8f;
		updateHT(false);
	}

	@Override
	public void next() {
		if (isAlive())
			super.next();
	}

	//灯火前行逻辑
	public void damageLantern(int value) {

		if (lanterfire < 0) {
			lanterfire = 0;
		}
		lanterfire -= value;
		hero.sprite.showStatus(0x808080, String.valueOf(value));
	}

	public void healLantern(int value) {
		//寂灭灯火1.0
		lanterfire = min(lanterfire + value, Dungeon.isChallenged(DHXD) ? 72 : 100);
		hero.sprite.showStatus(0x00ff00, String.valueOf(value));
	}

	//寒冰值系统
	public void damageIcehp(int value) {
		icehp += value;
		hero.sprite.showStatus(0x009999, "-" + value);
	}

	public void healIcehp(int value) {
		if (icehp > 0) {
			icehp -= value;
		}
		hero.sprite.showStatus(0x00ffff, "+" + value);
	}

	@Override
	public float talentProc(){
		if (hasTalent(Talent.RUNIC_TRANSFERENCE) && (pointsInTalent(Talent.RUNIC_TRANSFERENCE)>1)) return 1.25f;
		return super.talentProc();
	}

	private boolean warriorDeathWindowShown = false;

	private void WarriorDead() {
		if (warriorDeathWindowShown) {
			return;
		}

		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				if (warriorDeathWindowShown) {
					return;
				}

				GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.ANKH),
						Messages.get(Talent.PAIN_SCAR,"title"),
						Messages.get(Talent.PAIN_SCAR,"desc"),
						Messages.get(Talent.PAIN_SCAR,"prompt"),
						Messages.get(Talent.PAIN_SCAR,"cancel")){
					@Override
					public void onBackPressed() {}

					@Override
					protected void onSelect(int index){
						super.onSelect(index);
						if( index == 0 ){
							Buff buff = buff(Berserk.class);
							if(buff != null){
								switch(pointsInTalent(Talent.PAIN_SCAR)){
									case 1:
										HT -= 20;
										buff(Berserk.class).reducePower(0.2f);
										GLog.n(Messages.get(Talent.PAIN_SCAR,"resistDeath"));
										resistHealth +=20;
										break;
									case 2:
										HT -= 15;
										buff(Berserk.class).reducePower(0.15f);
										GLog.n(Messages.get(Talent.PAIN_SCAR,"resistDeath"));
										resistHealth += 15;
										break;
									case 3:
										HT -= 10;
										buff(Berserk.class).reducePower(0.1f);
										GLog.n(Messages.get(Talent.PAIN_SCAR,"resistDeath"));
										resistHealth += 10;
										break;
								}
							}
						} else if(index == 1 ){
							die(false);
						}
						warriorDeathWindowShown = false;
					}
				});
				warriorDeathWindowShown = true;
			}
		});
	}

	private void WarriorHPLimit(){
		switch(pointsInTalent(Talent.PAIN_SCAR)){
			case 1:
				HT -= 20;
				buff(Berserk.class).reducePower(0.2f);
				GLog.n(Messages.get(Talent.PAIN_SCAR,"resistDeath"));
				resistHealth +=20;
				return;
			case 2:
				HT -= 15;
				buff(Berserk.class).reducePower(0.15f);
				GLog.n(Messages.get(Talent.PAIN_SCAR,"resistDeath"));
				resistHealth += 15;
				return;
			case 3:
				HT -= 10;
				buff(Berserk.class).reducePower(0.1f);
				GLog.n(Messages.get(Talent.PAIN_SCAR,"resistDeath"));
				resistHealth += 10;
        }
	}

	public interface Doom {
		void onDeath();
	}
}
