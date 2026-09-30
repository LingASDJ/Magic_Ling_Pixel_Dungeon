package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.galaxy.SliverLockSword;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.normal.DogDogMusic;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CapeOfThorns;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CommRelay;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.LloydsBeacon;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MagneticCrown;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MasterThievesArmband;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfGodIce;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.FiveRen;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.JunglePoison;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.KingSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RedBloodMoon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SDBSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.hollow.DeathRongBoat;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.ClearSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.DiedCrossBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.GoldLongGun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.KingAxe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.MoonDao;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.RiceSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.SaiPlus;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.CrossReback;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * 查种器物品过滤表。
 * 从 NewSeedFinder 拆出：查种执行已全部走旧 SeedFinder，
 * 这里只保留选择网格需要的静态判定。
 */
public class SeedItemFilters {

    // ===== 图鉴中存在、但正常对局永远不会生成的物品（选择网格直接隐藏） =====
    public static final HashSet<Class<? extends Item>> UNGENERATED = new HashSet<>(Arrays.asList(
            MasterThievesArmband.class,
            LloydsBeacon.class,
            CommRelay.class,
            SliverLockSword.class,
            CloakOfShadows.class// 隐身披风不进生成池
            //还有任何不会生成的物品直接加在这里就行
            
        
    ));

    public static boolean isUngenerated(Class<?> cls) {
        for (Class<? extends Item> c : UNGENERATED) {
            if (c.isAssignableFrom(cls)) return true;
        }
        return false;
    }

    // ===== 需要满足解锁条件才会进入生成池的物品 =====
    // 映射表与 Generator.initGeneral() 中的概率门控一一对应（条件不满足时概率为 0）。
    // 选择网格将这些物品置灰不可选，避免玩家查找当前存档根本不可能生成的物品。
    // 条件在每次查询时实时求值，解锁进度变化后重新打开界面即生效。
    public static final HashMap<Class<? extends Item>, java.util.function.BooleanSupplier> LOCK_CONDITIONS = new HashMap<>();
    static {
        // 商店购买/图鉴解锁类（SPDSettings.isItemUnlock）
        LOCK_CONDITIONS.put(RedBloodMoon.class, () -> SPDSettings.isItemUnlock(RedBloodMoon.class.getSimpleName()));
        LOCK_CONDITIONS.put(MoonDao.class, () -> SPDSettings.isItemUnlock(MoonDao.class.getSimpleName()));
        LOCK_CONDITIONS.put(GoldLongGun.class, () -> SPDSettings.isItemUnlock(GoldLongGun.class.getSimpleName()));
        LOCK_CONDITIONS.put(DogDogMusic.CICREMUSIC.class, () -> SPDSettings.isItemUnlock("DogDogLingDang"));
        LOCK_CONDITIONS.put(DiedCrossBow.class, () -> SPDSettings.isItemUnlock(DiedCrossBow.class.getSimpleName()));
        LOCK_CONDITIONS.put(SaiPlus.class, () -> SPDSettings.isItemUnlock(SaiPlus.class.getSimpleName()));
        LOCK_CONDITIONS.put(ClearSword.class, () -> SPDSettings.isItemUnlock(ClearSword.class.getSimpleName()));
        // 徽章解锁类
        LOCK_CONDITIONS.put(WandOfGodIce.class, () -> Badges.isUnlocked(Badges.Badge.KILL_MG));
        LOCK_CONDITIONS.put(JunglePoison.class, () -> Badges.isUnlocked(Badges.Badge.KILL_CLSISTER));
        LOCK_CONDITIONS.put(SDBSword.class, () -> Badges.isUnlocked(Badges.Badge.KILL_SM));
        LOCK_CONDITIONS.put(KingSword.class, () -> Badges.isUnlocked(Badges.Badge.BOSS_CHALLENGE_4));
        LOCK_CONDITIONS.put(KingAxe.class, SPDSettings::KillDwarf);
        LOCK_CONDITIONS.put(DeathRongBoat.class, () -> Badges.isUnlocked(Badges.Badge.KILL_DOG));
        LOCK_CONDITIONS.put(CapeOfThorns.class, () -> Badges.isUnlocked(Badges.Badge.KILL_DM720));
        LOCK_CONDITIONS.put(MagneticCrown.class, () -> Badges.isUnlocked(Badges.Badge.YASD));
        // 密码徽章解锁类
        LOCK_CONDITIONS.put(RiceSword.class, () ->
                PaswordBadges.filtered(true).contains(PaswordBadges.Badge.UNLOCK_RICESWORD)
                        || SPDSettings.isItemUnlock(RiceSword.class.getSimpleName()));
        LOCK_CONDITIONS.put(FiveRen.class, () ->
                PaswordBadges.filtered(true).contains(PaswordBadges.Badge.ZQJ_GHOST));
        LOCK_CONDITIONS.put(CrossReback.class, () ->
                PaswordBadges.filtered(true).contains(PaswordBadges.Badge.VAMGHOST_DEAD));
    }

    /** 该物品当前存档尚未满足生成条件（网格中置灰） */
    public static boolean isLocked(Class<?> cls) {
        java.util.function.BooleanSupplier cond = LOCK_CONDITIONS.get(cls);
        return cond != null && !cond.getAsBoolean();
    }
}
