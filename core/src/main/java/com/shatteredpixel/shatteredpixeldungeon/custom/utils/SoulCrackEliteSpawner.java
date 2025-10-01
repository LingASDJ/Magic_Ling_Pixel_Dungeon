package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.SoulCrack;
import com.watabou.utils.Random;

import java.util.HashMap;
import java.util.Map;

/**
 * 古堡魂灵精英生成工具类
 * 核心逻辑：
 * 1. 特定深度（4/9/14/19/23层）：检测玩家是否缺失对应灵魂碎片，缺失则生成魂灵精英
 * 2. 非特定深度（4-24层）：35%概率生成，且仅生成一次
 */
public class SoulCrackEliteSpawner {

    // ======================== 配置常量（可根据需求调整）========================
    /** 魂灵精英生成总开关（true=启用，false=关闭） */
    private static final boolean ENABLE_SOUL_ELITE_SPAWN = true;

    /** 非特定深度的生成概率（百分比，如35=35%） */
    private static final int NORMAL_SPAWN_CHANCE = 65;

    /** 非特定深度的生成范围（最小层-最大层） */
    private static final int NORMAL_MIN_DEPTH = 4;
    private static final int NORMAL_MAX_DEPTH = 24;

    /** 特定深度（LimitSoulLevel返回true）与对应检测的灵魂碎片类型映射 */
    private static final Map<Integer, Class<? extends SoulCrack>> DEPTH_TARGET_SOUL_MAP;

    // 静态初始化深度-碎片映射关系
    static {
        DEPTH_TARGET_SOUL_MAP = new HashMap<>();
        DEPTH_TARGET_SOUL_MAP.put(4, SoulCrack.RedSoulCrack.class);    // 4层 → 检测红色灵魂碎片
        DEPTH_TARGET_SOUL_MAP.put(9, SoulCrack.BlueSoulCrack.class);   // 9层 → 检测蓝色灵魂碎片
        DEPTH_TARGET_SOUL_MAP.put(14, SoulCrack.GreenSoulCrack.class);  // 14层 → 检测绿色灵魂碎片
        DEPTH_TARGET_SOUL_MAP.put(19, SoulCrack.YellowSoulCrack.class); // 19层 → 检测黄色灵魂碎片
        DEPTH_TARGET_SOUL_MAP.put(23, SoulCrack.PinkSoulCrack.class);   // 23层 → 检测粉色灵魂碎片
    }


    // ======================== 核心生成逻辑（外部调用入口）========================
    /**
     * 处理魂灵精英生成逻辑
     * @param hero 当前玩家实例（用于检测背包中的灵魂碎片）
     * @param monster 待判断是否添加魂灵精英buff的怪物实例
     * @param currentDepth 当前地牢层数（用于判断生成场景）
     */
    public static void handleSoulEliteSpawn(Hero hero, Mob monster, int currentDepth) {
        // 1. 先判断总开关是否启用，关闭则直接返回
        if (!ENABLE_SOUL_ELITE_SPAWN || Statistics.soulsSpawn) {
            return;
        }

        // 2. 场景1：当前层是「特定检测层」（4/9/14/19/23层）
        if (LimitSoulLevel(currentDepth)) {
            handleSpecificDepthSpawn(hero, monster, currentDepth);
        }
        // 3. 场景2：当前层是「非特定深度」（4-24层，且不在特定检测层内）
        else if (isInNormalSpawnDepth(currentDepth)) {
            handleNormalDepthSpawn(monster);
        }
    }


    // ======================== 场景1：特定深度（碎片缺失检测）生成逻辑========================
    /**
     * 特定深度（4/9/14/19/23层）的生成逻辑：检测碎片缺失则生成
     */
    private static void handleSpecificDepthSpawn(Hero hero, Mob mob, int currentDepth) {
        // 根据当前深度获取需要检测的「目标灵魂碎片类型」
        Class<? extends SoulCrack> targetSoulClass = DEPTH_TARGET_SOUL_MAP.get(currentDepth);

        // 防御性判断：若映射中无该深度的碎片类型（避免配置错误导致空指针）
        if (targetSoulClass == null) {
            return;
        }

        // 检测玩家是否「缺失」该目标碎片，缺失则给怪物添加魂灵精英buff
        if (isMissingSoulCrack(hero, targetSoulClass)) {
            Buff.affect(mob, ChampionEnemy.DeadSoulCrack.class);
            Statistics.soulsSpawn = true;
        }
    }


    // ======================== 场景2：非特定深度（概率）生成逻辑========================
    /**
     * 非特定深度（4-24层）的生成逻辑：35%概率+仅生成一次
     */
    private static void handleNormalDepthSpawn(Mob mob) {
        // 条件：35%概率触发 + 尚未生成过魂灵精英
        if (Random.Int(100) >= NORMAL_SPAWN_CHANCE && !Statistics.soulsSpawn) {
            Buff.affect(mob, ChampionEnemy.DeadSoulCrack.class);
            Statistics.soulsSpawn = true;
        }
    }


    // ======================== 辅助方法（封装重复逻辑）========================
    /**
     * 检测玩家是否缺失「指定类型」的灵魂碎片
     * @param hero 玩家实例
     * @param soulClass 要检测的灵魂碎片类型
     * @return true=缺失，false=已拥有
     */
    private static boolean isMissingSoulCrack(Hero hero, Class<? extends SoulCrack> soulClass) {
        // 玩家背包中找不到该类型碎片 → 缺失
        return hero.belongings.getItem(soulClass) == null;
    }

    /**
     * 判断当前深度是否在「非特定深度生成范围」内（4-24层）
     */
    private static boolean isInNormalSpawnDepth(int depth) {
        return depth >= NORMAL_MIN_DEPTH && depth <= NORMAL_MAX_DEPTH;
    }

    /**
     * 判断当前深度是否为「特定检测层」（4/9/14/19/23层）
     * （保留你原有的深度判断逻辑）
     */
    public static boolean LimitSoulLevel(int depth) {
        return depth == 4 || depth == 9 || depth == 14 || depth == 19 || depth == 23;
    }

}
