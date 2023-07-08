package com.shatteredpixel.shatteredpixeldungeon.services.analytics;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.watabou.noosa.Game;

import java.util.LinkedHashMap;

public class Analytics {
    public static AnalyticsService service;

    public static AnalyticsGameData convertGameToData() {
        AnalyticsGameData analyticsGameData = new AnalyticsGameData();
        analyticsGameData.gameVersion = Game.version;
        analyticsGameData.heroCls = Dungeon.hero.heroClass.name();
        analyticsGameData.heroSubCls = Dungeon.hero.subClass.name();
        analyticsGameData.heroLvl = Dungeon.hero.lvl;
        analyticsGameData.depth = Dungeon.depth;
        analyticsGameData.deepest = Statistics.deepestFloor;
        analyticsGameData.ascent = Statistics.highestAscent;
        analyticsGameData.spawnersAlive = Statistics.spawnersAlive;
        analyticsGameData.duration = Statistics.duration;
        analyticsGameData.challengeMask = Dungeon.challenges;
        analyticsGameData.talents = new LinkedHashMap<>();
        if (Badges.isUnlocked(Badges.Badge.CHAMPION_3X)) {
            analyticsGameData.bestBossBeaten = "8.6+ Challenges";
        } else if (Badges.isUnlocked(Badges.Badge.CHAMPION_2X)) {
            analyticsGameData.bestBossBeaten = "7.3+ Challenges";
        } else if (Badges.isUnlocked(Badges.Badge.CHAMPION_1X)) {
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
        return analyticsGameData;
    }

    public static void disable() {
        if (supportsAnalytics()) {
            service.disable();
        }
    }

    public static void enable() {
        if (supportsAnalytics()) {
            service.enable();
            service.trackGameSettings();
        }
    }

    public static void trackBossBeaten(Char ch) {
        if (supportsAnalytics()) {
            AnalyticsGameData convertGameToData = convertGameToData();
            AnalyticsBelongingsData convertBelongingsToData = convertBelongingsToData(Dungeon.hero);
            service.trackBossBeaten(convertGameToData, convertBelongingsToData, ch.getClass().getSimpleName());

        }
    }

    public static AnalyticsBelongingsData convertBelongingsToData(Hero hero) {
        AnalyticsBelongingsData analyticsBelongingsData = new AnalyticsBelongingsData();
        analyticsBelongingsData.items = new LinkedHashMap<>();
        for (Item item : hero.belongings) {
            Weapon weapon = (Weapon) item;
            if (weapon.visiblyUpgraded() > 22 || !Dungeon.customSeedText.isEmpty()) {
                analyticsBelongingsData.cheater = true;
            }
            if (weapon.isEquipped(hero) || Dungeon.quickslot.contains(weapon)) {
                boolean z = ((Item) weapon).levelKnown;
                ((Item) weapon).levelKnown = true;
                analyticsBelongingsData.items.put(weapon.name(), weapon.visiblyUpgraded());
                Weapon.Enchantment enchantment = weapon.enchantment;
                if (enchantment != null) {
                    analyticsBelongingsData.items.put("Enchant: ".concat(enchantment.getClass().getSimpleName()),
                            weapon.visiblyUpgraded());
                }
                if (weapon.augment != null) {
                    if (weapon instanceof SpiritBow) {
                        LinkedHashMap<String, Integer> linkedHashMap = analyticsBelongingsData.items;
                        linkedHashMap.put("BowAug: " + weapon.augment.name(), weapon.visiblyUpgraded());
                    } else {
                        LinkedHashMap<String, Integer> linkedHashMap2 = analyticsBelongingsData.items;
                        linkedHashMap2.put("WepAug: " + weapon.augment.name(), weapon.visiblyUpgraded());
                    }
                }
                ((Item) weapon).levelKnown = z;
            }
        }
        return analyticsBelongingsData;
    }


    public static boolean supportsAnalytics() {
        return service != null;
    }

}
