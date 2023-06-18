package com.shatteredpixel.shatteredpixeldungeon.services.analytics;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.Game;
import com.watabou.utils.GameSettings;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class Analytics {
    public static AnalyticsService service;

    public static AnalyticsBelongingsData convertBelongingsToData(Hero hero) {
        AnalyticsBelongingsData analyticsBelongingsData = new AnalyticsBelongingsData();
        analyticsBelongingsData.items = new LinkedHashMap();
        Iterator it = hero.belongings.iterator();
        while (it.hasNext()) {
            Weapon weapon = (Item) it.next();
            if (weapon.visiblyUpgraded() > 22 || !Dungeon.customSeedText.isEmpty()) {
                analyticsBelongingsData.cheater = true;
            }
            if (weapon.isEquipped(hero) || Dungeon.quickslot.contains(weapon)) {
                boolean z = ((Item) weapon).levelKnown;
                ((Item) weapon).levelKnown = true;
                analyticsBelongingsData.items.put(weapon.analyticsName(), Integer.valueOf(weapon.visiblyUpgraded()));
                if (weapon instanceof Weapon) {
                    Weapon weapon2 = weapon;
                    Weapon.Enchantment enchantment = weapon2.enchantment;
                    if (enchantment != null) {
                        analyticsBelongingsData.items.put("Enchant: ".concat(enchantment.getClass().getSimpleName()), Integer.valueOf(weapon.visiblyUpgraded()));
                    }
                    if (weapon2.augment != null) {
                        if (weapon instanceof SpiritBow) {
                            LinkedHashMap linkedHashMap = analyticsBelongingsData.items;
                            linkedHashMap.put("BowAug: " + weapon2.augment.name(), Integer.valueOf(weapon.visiblyUpgraded()));
                        } else {
                            LinkedHashMap linkedHashMap2 = analyticsBelongingsData.items;
                            linkedHashMap2.put("WepAug: " + weapon2.augment.name(), Integer.valueOf(weapon.visiblyUpgraded()));
                        }
                    }
                }
                if (weapon instanceof Armor) {
                    Armor armor = (Armor) weapon;
                    Armor.Glyph glyph = armor.glyph;
                    if (glyph != null) {
                        analyticsBelongingsData.items.put("Glyph: ".concat(glyph.getClass().getSimpleName()), Integer.valueOf(weapon.visiblyUpgraded()));
                    }
                    if (armor.augment != null) {
                        LinkedHashMap linkedHashMap3 = analyticsBelongingsData.items;
                        linkedHashMap3.put("ArmAug: " + armor.augment.name(), Integer.valueOf(weapon.visiblyUpgraded()));
                    }
                }
                ((Item) weapon).levelKnown = z;
            }
        }
        return analyticsBelongingsData;
    }

    public static AnalyticsGameData convertGameToData() {
        AnalyticsGameData analyticsGameData = new AnalyticsGameData();
        analyticsGameData.gameVersion = Game.version;
        analyticsGameData.heroCls = Dungeon.hero.heroClass.name();
        analyticsGameData.heroSubCls = Dungeon.hero.subClass.name();
        ArmorAbility armorAbility = Dungeon.hero.armorAbility;
        if (armorAbility == null) {
            analyticsGameData.heroArmorAbility = "NONE";
        } else {
            analyticsGameData.heroArmorAbility = armorAbility.getClass().getSimpleName();
        }
        analyticsGameData.heroLvl = Dungeon.hero.lvl;
        analyticsGameData.depth = Dungeon.depth;
        analyticsGameData.deepest = Statistics.deepestFloor;
        analyticsGameData.ascent = Statistics.highestAscent;
        analyticsGameData.spawnersAlive = Statistics.spawnersAlive;
        analyticsGameData.duration = Statistics.duration;
        analyticsGameData.challengeMask = Dungeon.challenges;
        Hero hero = Dungeon.hero;
        analyticsGameData.upgrades = hero.upgrades;
        analyticsGameData.crafts = hero.crafted;
        analyticsGameData.talents = new LinkedHashMap();
        Iterator it = Dungeon.hero.talents.iterator();
        int i = 0;
        while (it.hasNext()) {
            LinkedHashMap linkedHashMap = (LinkedHashMap) it.next();
            i++;
            for (Talent talent : linkedHashMap.keySet()) {
                if (((Integer) linkedHashMap.get(talent)).intValue() > 0 && !Dungeon.hero.metamorphedTalents.containsValue(talent)) {
                    LinkedHashMap linkedHashMap2 = analyticsGameData.talents;
                    linkedHashMap2.put(i + ": " + talent.name(), (Integer) linkedHashMap.get(talent));
                }
            }
        }
        if (Badges.isUnlocked(Badges.Badge.CHAMPION_3)) {
            analyticsGameData.bestBossBeaten = "8.6+ Challenges";
        } else if (Badges.isUnlocked(Badges.Badge.CHAMPION_2)) {
            analyticsGameData.bestBossBeaten = "7.3+ Challenges";
        } else if (Badges.isUnlocked(Badges.Badge.CHAMPION_1)) {
            analyticsGameData.bestBossBeaten = "6.1+ Challenges";
        } else if (Badges.isUnlocked(Badges.Badge.VICTORY)) {
            analyticsGameData.bestBossBeaten = "5.Yog-Dzewa";
        } else if (Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_4)) {
            analyticsGameData.bestBossBeaten = "4.King of Dwarves";
        } else if (Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_3)) {
            analyticsGameData.bestBossBeaten = "3.DM-300";
        } else if (Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_2)) {
            analyticsGameData.bestBossBeaten = "2.Tengu";
        } else if (Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_1)) {
            analyticsGameData.bestBossBeaten = "1.Goo";
        } else {
            analyticsGameData.bestBossBeaten = "0.None";
        }
        analyticsGameData.ratMogrifyFound = Badges.isUnlocked(Badges.Badge.FOUND_RATMOGRIFY);
        if (!Dungeon.customSeedText.isEmpty()) {
            analyticsGameData.cheater = true;
        }
        if (analyticsGameData.heroLvl > 30) {
            analyticsGameData.cheater = true;
        }
        for (Integer num : analyticsGameData.upgrades.values()) {
            if (num.intValue() > 22) {
                analyticsGameData.cheater = true;
            }
        }
        return analyticsGameData;
    }

    public static void disable() {
        SPDSettings.analytics(false);
        if (supportsAnalytics()) {
            service.disable();
        }
    }

    public static void enable() {
        if (supportsAnalytics()) {
            service.enable();
            SPDSettings.analytics(true);
            trackGameSettings();
        }
    }

    public static boolean supportsAnalytics() {
        if (service != null) {
            return true;
        }
        return false;
    }

    public static void trackBossBeaten(Char r3) {
        if (supportsAnalytics() && SPDSettings.analytics()) {
            AnalyticsGameData convertGameToData = convertGameToData();
            AnalyticsBelongingsData convertBelongingsToData = convertBelongingsToData(Dungeon.hero);
            if (!convertGameToData.cheater && !convertBelongingsToData.cheater) {
                service.trackBossBeaten(convertGameToData, convertBelongingsToData, r3.getClass().getSimpleName());
            }
        }
    }

    public static void trackException(Throwable th) {
        if (supportsAnalytics() && SPDSettings.analytics()) {
            service.trackException(th);
        }
    }

    public static void trackGameSettings() {
        String str;
        if (supportsAnalytics() && SPDSettings.analytics()) {
            Boolean landscape = SPDSettings.landscape();
            if (landscape == null) {
                if (PixelScene.landscape()) {
                    str = "landscape_default";
                } else {
                    str = "portrait_default";
                }
            } else if (landscape.booleanValue()) {
                str = "landscape_forced";
            } else {
                str = "portrait_forced";
            }
            service.trackGameSettings(SPDSettings.powerSaver(), SPDSettings.brightness(), SPDSettings.visualGrid(), SPDSettings.toolbarMode(), SPDSettings.flipToolbar(), SPDSettings.flipTags(), SPDSettings.quickSwapper(), GameSettings.getBoolean("system_font", false), str);
        }
    }

    public static void trackRunEnd(String str) {
        if (supportsAnalytics() && SPDSettings.analytics()) {
            AnalyticsGameData convertGameToData = convertGameToData();
            if (!convertGameToData.cheater) {
                service.trackRunEnd(convertGameToData, str);
            }
        }
    }

    public static void trackScreen(String str) {
        if (supportsAnalytics() && SPDSettings.analytics()) {
            service.trackScreen(str);
        }
    }
}
