package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ArmoredStatue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CrystalMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GoldenMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost.Quest;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RedDragon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.shatteredpixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredpixeldungeon.items.EnergyCrystal;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap.Type;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CeremonialCandle;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Embers;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Pickaxe;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RedBloodMoon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.ClearSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.DiedCrossBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.ForestBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.GoldLongGun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.MoonDao;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.RiceSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.SaiPlus;
import com.shatteredpixel.shatteredpixeldungeon.levels.DeadEndLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

public class SeedFinder implements Runnable {

    @Override
    public void run() {
        resetTest();
        String str;
        if (wantedArr.length == 0)
            str = logSeedItems(DungeonSeed.convertFromText(SeedFindScene.seedCode));
        else
            str = findSeed();
        SeedFindScene.INSTANCE.text = str;
        SeedFindScene.INSTANCE.needUpdate = true;
    }

    public static volatile boolean running;
    public static volatile boolean SeedFinding = false;
    // 命中复查次数：世界生成已验证确定性（同种子任意次生成结果一致），3 次足以兜底
    public static final int CONFIRM_REPS = 3;

    /** 查种行为开关（沿用旧版：WndSettings 中"检查支线"开关读写此配置） */
    public static class Options {
        public static boolean checkBranches = SPDSettings.logBranch();
        public static final int[] BRANCH_IDS = {1, 2, 3};
    }

    //【临时·性能测试】单次查找的耗时上限（毫秒）与已扫描种子数统计仅用于性能评估，后续移除
    public static volatile long SEARCH_LIMIT_MS = 200000L;

    /**
     * 侦察种子文本行 → WantedTarget 列表（"物品名+等级"，等级缺省 0）。
     * 解析失败的行直接跳过（与旧版文本匹配行为一致：找不到的物品不参与匹配）。
     */
    public static ArrayList<WantedTarget> parseWanted(String[] lines) {
        ArrayList<WantedTarget> out = new ArrayList<>();
        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) continue;
            String name = line.trim();
            // 兼容旧版引号精确匹配语法："名字"（去掉引号后按精确名匹配）
            name = name.replaceAll("\"", "").trim();
            int level = 0;
            int plus = name.lastIndexOf('+');
            if (plus > 0) {
                try {
                    level = Integer.parseInt(name.substring(plus + 1).trim());
                    name = name.substring(0, plus).trim();
                } catch (NumberFormatException ignored) {
                }
            }
            Class<? extends Item> cls = findItemClassByName(name);
            if (cls != null)
                out.add(new WantedTarget(cls, level, null));
        }
        return out;
    }

    /** 在全部生成池中按本地化名称查找物品类（去空格后子串双向匹配，容忍前后缀差异） */
    public static Class<? extends Item> findItemClassByName(String name) {
        String clean = name.replaceAll("\\s+", "").toLowerCase();
        if (clean.isEmpty()) return null;
        for (Generator.Category cat : Generator.Category.values()) {
            if (cat.classes == null) continue;
            for (Class<?> cl : cat.classes) {
                if (cl == null || !Item.class.isAssignableFrom(cl)) continue;
                Class<? extends Item> ic = (Class<? extends Item>) cl;
                if (matches(ic, clean))
                    return ic;
            }
        }
        // 任务奖励/传说武器（可能不在 Generator 品类池中，红龙五选一与传说武器必须可查）
        for (Class<? extends Item> ic : EXTRA_ITEM_CLASSES)
            if (matches(ic, clean))
                return ic;
        if (RedDragon.Quest.weapon != null && matches(RedDragon.Quest.weapon.getClass(), clean))
            return (Class<? extends Item>) RedDragon.Quest.weapon.getClass();
        if (RedDragon.Quest.armor != null && matches(RedDragon.Quest.armor.getClass(), clean))
            return (Class<? extends Item>) RedDragon.Quest.armor.getClass();
        if (RedDragon.Quest.RingT != null && matches(RedDragon.Quest.RingT.getClass(), clean))
            return (Class<? extends Item>) RedDragon.Quest.RingT.getClass();
        if (RedDragon.Quest.food != null && matches(RedDragon.Quest.food.getClass(), clean))
            return (Class<? extends Item>) RedDragon.Quest.food.getClass();
        if (RedDragon.Quest.scrolls != null && matches(RedDragon.Quest.scrolls.getClass(), clean))
            return (Class<? extends Item>) RedDragon.Quest.scrolls.getClass();
        return null;
    }

    /** 传说武器（7 件紫色 lengds 标注）与红月大剑：文本输入时必须可查 */
    private static final Class<? extends Item>[] EXTRA_ITEM_CLASSES = new Class[]{
            RedBloodMoon.class,
            ClearSword.class, DiedCrossBow.class, ForestBow.class,
            GoldLongGun.class, MoonDao.class, RiceSword.class, SaiPlus.class
    };

    private static boolean matches(Class<? extends Item> ic, String clean) {
        String title = identifiedTitle(ic);
        return title != null && (title.equals(clean) || title.contains(clean) || clean.contains(title));
    }

    /** 识别后的本地化标题（去空格小写）；未识别物品标题是"未知的XX"，无法与输入名匹配 */
    private static String identifiedTitle(Class<? extends Item> ic) {
        try {
            Item it = ic.newInstance();
            it.identify();
            return it.title().replaceAll("\\s+", "").toLowerCase();
        } catch (Exception ignored) {
            return null;
        }
    }

    // 查种前复位地牢内存态：清 hero/楼层引用并启用 SEED 测试规则，避免污染正常游戏
    static void resetTest() {
        Dungeon.hero = null;
        Dungeon.depth = 0;
        Dungeon.branch = 0;
        Dungeon.level = null;
        Dungeon.overrideSeed = -1;
        Dungeon.isDLC(Conducts.Conduct.SEED);
    }

    protected final WantedTarget[] wantedArr;
    // Class → 目标下标数组：tryMatch 先查 map 取候选目标，跳过无关物品
    private final HashMap<Class<? extends Item>, int[]> matchIndex;
    // 预筛下标：循环内直接遍历，避免对全量 wantedArr 逐条 isAssignableFrom
    protected final int floor;
    protected final HeroClass heroClass;
    protected SeedFinder(ArrayList<WantedTarget> wanted, int fl, HeroClass cl) {
        wantedArr = wanted.toArray(new WantedTarget[0]);
        matchIndex = buildMatchIndex(wantedArr);
        floor = fl;
        heroClass = cl;
        for (WantedTarget w : wanted) {
            if (Wand.class.isAssignableFrom(w.cls) && w.minLevel >= 3)
                wand = w;
            else if (Ring.class.isAssignableFrom(w.cls) && w.minLevel >= 3)
                ring = w;
        }
    }
    WantedTarget wand;
    WantedTarget ring;

    // 构造时按 cls 分组目标下标，供 tryMatch 做 O(1) 跳查
    private static HashMap<Class<? extends Item>, int[]> buildMatchIndex(WantedTarget[] arr) {
        HashMap<Class<? extends Item>, ArrayList<Integer>> temp = new HashMap<>();
        for (int j = 0; j < arr.length; j++) {
            Class<? extends Item> cls = arr[j].cls;
            ArrayList<Integer> list = temp.get(cls);
            if (list == null) {
                list = new ArrayList<>();
                temp.put(cls, list);
            }
            list.add(j);
        }
        HashMap<Class<? extends Item>, int[]> idx = new HashMap<>();
        for (Map.Entry<Class<? extends Item>, ArrayList<Integer>> e : temp.entrySet()) {
            ArrayList<Integer> list = e.getValue();
            int[] indices = new int[list.size()];
            for (int k = 0; k < list.size(); k++) {
                indices[k] = list.get(k);
            }
            idx.put(e.getKey(), indices);
        }
        return idx;
    }
    public final String findSeed() {
        String result = "NONE";
        SeedFinding = true;
        running = true;

        long seedDigits = DungeonSeed.randomSeed();
        if (seedDigits > 200000) {
            seedDigits -= 100000;
        }

        //【临时·性能测试】
        long startMs = System.currentTimeMillis();
        long scanned = 0;
        boolean timedOut = false;

        for (int i = Random.Int(99999); (long) i < DungeonSeed.TOTAL_SEEDS
                && running && SeedFinding; ++i) {
            long currentSeed = seedDigits + i;

            if (SeedFindScene.INSTANCE != null) {
                SeedFindScene.INSTANCE.updateCurrentSeed(currentSeed);
                SeedFindScene.INSTANCE.scannedSeeds = scanned;//【临时·性能测试】
            }

            //【临时·性能测试】满上限仍未命中则中断，如实报告已扫描的种子数
            if (System.currentTimeMillis() - startMs >= SEARCH_LIMIT_MS) {
                timedOut = true;
                break;
            }

            // 复查：命中目标必须在该种子反复生成下稳定出现
            boolean confirmed = true;
            for (int r = 0; r < CONFIRM_REPS; r++) {
                if (!testSeed(currentSeed)) {
                    confirmed = false;
                    break;
                }
            }
            scanned++;//【临时·性能测试】
            if (confirmed) {
                //【临时·性能测试】命中结果附带耗时与已扫描种子数
                result = logSeedItems(currentSeed) + scanStats(scanned, startMs);
                break;
            }

            if (Thread.currentThread().isInterrupted()) {
                running = false;
                break;
            }
        }
        SeedFinding = false;
        if (timedOut) {//【临时·性能测试】
            if (SeedFindScene.INSTANCE != null) SeedFindScene.INSTANCE.scannedSeeds = scanned;
            result = Messages.get(SeedFinder.class, "not_found")
                    + scanStats(scanned, startMs) + Messages.get(SeedFinder.class, "time_limit");
        }
        return result;
    }

    //【临时·性能测试】统计文案：共扫描 N 个种子、用时 X 秒（后续移除）
    public static String scanStats(long scanned, long startMs) {
        long ms = System.currentTimeMillis() - startMs;
        return "\n\n" + Messages.get(SeedFinder.class, "scan_stats", scanned, (ms / 100) / 10.0);
    }

    protected boolean testSeed(long seed) {
        Dungeon.hero = null;
        Dungeon.overrideSeed = seed;
        GamesInProgress.selectedClass = heroClass;
        Dungeon.isDLC(Conducts.Conduct.SEED);
        Dungeon.init();
        boolean[] itemsFound = new boolean[wantedArr.length];
        int foundCount = 0;
        int n = wantedArr.length;
        boolean ghostSeen = false, redDragonSeen = false, impSeen = false, wandmakerSeen = false;
        try {
            // 统一楼层语义：遍历 0..floor（含第 0 层与最深一层），与现行查种器一致
            for (int i = 0; i <= floor; i++) {
                // 线程版查种：被打断时在当前楼层边界尽快让出（楼层内不检查，保证 RNG 生成器栈平衡）
                if (Thread.currentThread().isInterrupted()) return false;
                int originalBranch = Dungeon.branch;
                Dungeon.branch = 0;

                Level l = Dungeon.newLevel();
                if (l == null || l instanceof DeadEndLevel) {
                    Dungeon.branch = originalBranch;
                    Dungeon.depth++;
                    continue;
                }

                // 地面物品：遇物即匹配，不建中间表、不 identify
                // level()/enchantment/glyph 均为生成时定型的字段/方法，无需 identify 即可读取
                // values() 直接遍历 IntMap 的 Values 迭代器，省去 valueList() 的数组拷贝+List包装
                for (Heap h : l.heaps.values())
                    for (Item item : h.items)
                        if (tryMatch(item, itemsFound) && ++foundCount == n)
                            return true;

                // 怪物掉落：直接取物，不包装 Heap
                for (Mob m : l.mobs) {
                    if (m.getClass() == ArmoredStatue.class) {
                        if (tryMatch(((ArmoredStatue) m).armor(), itemsFound) && ++foundCount == n)
                            return true;
                        if (tryMatch(((ArmoredStatue) m).weapon(), itemsFound) && ++foundCount == n)
                            return true;
                    }
                    else if (m.getClass() == Statue.class) {
                        if (tryMatch(((Statue) m).weapon(), itemsFound) && ++foundCount == n)
                            return true;
                    }
                    else if (m instanceof Mimic) {
                        for (Item item : ((Mimic) m).items)
                            if (tryMatch(item, itemsFound) && ++foundCount == n)
                                return true;
                    }
                }
                if (!ghostSeen && Quest.armor != null) {
                    ghostSeen = true;
                    if ((tryMatch(Quest.armor, itemsFound)
                            || tryMatch(Quest.weapon, itemsFound)) && ++foundCount == n)
                        return true;
                }
                // 红龙之王任务奖励（weapon/armor/RingT/food/scrolls 五选一，与日志展示一致）
                if (!redDragonSeen && RedDragon.Quest.armor != null) {
                    redDragonSeen = true;
                    Item[] redDragonRewards = {RedDragon.Quest.weapon, RedDragon.Quest.armor,
                            RedDragon.Quest.RingT, RedDragon.Quest.food, RedDragon.Quest.scrolls};
                    for (Item rr : redDragonRewards) {
                        if (rr != null && tryMatch(rr, itemsFound) && ++foundCount == n)
                            return true;
                    }
                }
                if (!wandmakerSeen && Wandmaker.Quest.wand1 != null) {
                    wandmakerSeen = true;
                    Item w1 = Wandmaker.Quest.wand1;
                    Item w2 = Wandmaker.Quest.wand2;
                    if (wand != null && !wand.matches(w1) && !wand.matches(w2))
                        return false;
                    if ((tryMatch(w1, itemsFound) || tryMatch(w2, itemsFound)) && ++foundCount == n)
                        return true;
                }
                if (!impSeen && Imp.Quest.reward != null) {
                    impSeen = true;
                    if (ring != null && !ring.matches(Imp.Quest.reward))
                        return false;
                    if (tryMatch(Imp.Quest.reward, itemsFound) && ++foundCount == n)
                        return true;
                }

                // 支线检查（WndSettings 开关）：主楼层未全部命中时尝试 1-3 号支线楼层
                if (Options.checkBranches && foundCount < n) {
                    for (int br : Options.BRANCH_IDS) {
                        checkBranchLevel(br, itemsFound);
                        foundCount = 0;
                        for (boolean b : itemsFound) if (b) foundCount++;
                        if (foundCount == n) return true;
                    }
                }

                Dungeon.branch = originalBranch;
                Dungeon.depth++;
            }
            return false;
        } finally {
            // 清理Dungeon状态，释放楼层对象引用，防止长时间搜索 GC 压力堆积
            Dungeon.depth = 0;
            Dungeon.branch = 0;
            Dungeon.level = null;
        }
    }

    private boolean tryMatch(Item item, boolean[] itemsFound) {
        int[] candidates = matchIndex.get(item.getClass());
        if (candidates == null) return false;
        for (int idx : candidates) {
            //只查找这个类所能在的位置
            if (!itemsFound[idx] && wantedArr[idx].matches(item)) {
                itemsFound[idx] = true;
                return true;
            }
        }
        return false;
    }

    /** 支线楼层命中检查：生成指定支线的楼层并匹配物品（itemsFound 由 tryMatch 增量更新） */
    private boolean checkBranchLevel(int branch, boolean[] itemsFound) {
        if (Thread.currentThread().isInterrupted()) return false;
        int originalBranch = Dungeon.branch;
        try {
            Dungeon.branch = branch;
            Level branchLevel = Dungeon.newLevel();
            if (branchLevel == null || branchLevel instanceof DeadEndLevel) {
                return false;
            }
            for (Heap h : branchLevel.heaps.values())
                for (Item item : h.items)
                    tryMatch(item, itemsFound);
            for (Mob m : branchLevel.mobs) {
                if (m.getClass() == ArmoredStatue.class) {
                    tryMatch(((ArmoredStatue) m).armor(), itemsFound);
                    tryMatch(((ArmoredStatue) m).weapon(), itemsFound);
                }
                else if (m.getClass() == Statue.class) {
                    tryMatch(((Statue) m).weapon(), itemsFound);
                }
                else if (m instanceof Mimic) {
                    for (Item item : ((Mimic) m).items)
                        tryMatch(item, itemsFound);
                }
            }
            return true;
        } finally {
            Dungeon.branch = originalBranch;
        }
    }

    private ArrayList<Heap> getMobDrops(Level l) {
        // mobs 是 HashSet，迭代顺序依赖实例哈希，每次渲染的 mob 对象都是新实例，顺序会漂移；
        // 不能用 Mob::id 排序：Actor.id() 是懒分配（首次调用才取 nextID++），排序本身会污染 id 分配。
        // 改用 pos（同层内唯一、层生成时确定）作为确定性排序键，保证同种子多次渲染输出一致
        ArrayList<Mob> mobs = new ArrayList<>(l.mobs);
        mobs.sort(java.util.Comparator.comparingInt(m -> m.pos));
        ArrayList<Heap> heaps = new ArrayList<>();
        for (Mob m : mobs) {
            if (m instanceof Statue && !(m instanceof ArmoredStatue)) {
                Heap h = new Heap();
                h.items = new LinkedList<>();
                h.items.add(((Statue) m).weapon().identify());
                h.type = Type.HEAP;
                heaps.add(h);
            } else if (m instanceof ArmoredStatue) {
                Heap h = new Heap();
                h.items = new LinkedList<>();
                h.items.add(((ArmoredStatue) m).armor().identify());
                h.items.add(((ArmoredStatue) m).weapon().identify());
                h.type = Type.HEAP;
                heaps.add(h);
            } else if (m instanceof Mimic) {
                Heap h = new Heap();
                h.items = new LinkedList<>();
                for (Item item : ((Mimic) m).items) {
                    h.items.add(item.identify());
                }
                if (m instanceof GoldenMimic) {
                    h.type = Type.GOLDEN_MIMIC;
                } else if (m instanceof CrystalMimic) {
                    h.type = Type.CRYSTAL_MIMIC;
                } else {
                    h.type = Type.MIMIC;
                }
                heaps.add(h);
            }
        }
        return heaps;
    }

    String logSeedItems(long seed) {
        return buildLog(seed, null);
    }

    /** 日志模式 + 目标匹配楼层收集（供老查种弹窗显示“匹配物品层数”） */
    String logSeedItemsWithMatches(long seed, ArrayList<String> matchedInfo) {
        return buildLog(seed, matchedInfo);
    }

    private String buildLog(long seed, ArrayList<String> matchedInfo) {
        String seedCode = DungeonSeed.convertToCode(seed);
        SeedFindScene.seedCode = seedCode;
        Dungeon.hero = null;
        GamesInProgress.selectedClass = heroClass;
        Dungeon.overrideSeed = seed;
        Dungeon.isDLC(Conducts.Conduct.SEED);
        Dungeon.init();
        HashSet<Class<? extends Item>> blacklist = new HashSet<>(Arrays.asList(Dewdrop.class, IronKey.class, GoldenKey.class, CrystalKey.class, EnergyCrystal.class, CorpseDust.class, Embers.class, CeremonialCandle.class, Pickaxe.class));

        // Phase 1: 遍历所有楼层，收集物品（不 identify），任务奖励在出现层一次性收取并 complete
        ArrayList<FloorData> floorDataList = new ArrayList<>();
        SeedFinding = true;
        // 统一楼层语义：遍历 0..floor（含第 0 层与最深一层），与现行查种器一致
        for (int i = 0; i <= floor; i++) {
            int originalBranch = Dungeon.branch;
            Dungeon.branch = 0;

            Level l = Dungeon.newLevel();
            if (l == null || l instanceof DeadEndLevel) {
                Dungeon.branch = originalBranch;
                Dungeon.depth++;
                continue;
            }

            FloorData fd = new FloorData(Dungeon.depth);

            // 地面物品
            for (Heap h : l.heaps.valueList())
                for (Item item : h.items)
                    fd.heapItems.add(new HeapItem(item, h));

            // 怪物掉落
            for (Heap h : getMobDrops(l))
                for (Item item : h.items)
                    fd.heapItems.add(new HeapItem(item, h));

            // 鬼魂任务奖励
            if (Quest.armor != null) {
                ArrayList<Item> rewards = new ArrayList<>();
                rewards.add(Quest.armor);
                rewards.add(Quest.weapon);
                Quest.armor = null;   // 手动清状态：不调 complete()（无头环境下 Game.scene()==null 会 NPE），
                Quest.weapon = null;  // 清空即可阻止后续楼层重复收集
                fd.ghostRewards = rewards;
            }
            // 红龙之王任务奖励（weapon/armor/RingT/food/scrolls 五选一，仅出现者非 null）
            if (RedDragon.Quest.armor != null) {
                ArrayList<Item> rewards = new ArrayList<>();
                for (Item rr : new Item[]{RedDragon.Quest.weapon, RedDragon.Quest.armor,
                        RedDragon.Quest.RingT, RedDragon.Quest.food, RedDragon.Quest.scrolls})
                    if (rr != null) rewards.add(rr);
                RedDragon.Quest.weapon = null;
                RedDragon.Quest.RingT = null;
                RedDragon.Quest.armor = null;
                RedDragon.Quest.food = null;
                RedDragon.Quest.scrolls = null;
                fd.redDragonRewards = rewards;
            }
            // 工匠任务奖励（type 在清空前捕获）
            if (Wandmaker.Quest.wand1 != null) {
                ArrayList<Item> rewards = new ArrayList<>();
                rewards.add(Wandmaker.Quest.wand1);
                rewards.add(Wandmaker.Quest.wand2);
                fd.wandmakerType = Wandmaker.Quest.type();
                Wandmaker.Quest.wand1 = Wandmaker.Quest.wand2 = null;
                fd.wandmakerRewards = rewards;
            }
            // 小恶魔任务奖励
            if (Imp.Quest.reward != null) {
                ArrayList<Item> rewards = new ArrayList<>();
                rewards.add(Imp.Quest.reward);
                Imp.Quest.reward = null;
                fd.impRewards = rewards;
            }

            // 支线楼层物品（WndSettings 开关：检查支线时并入本层展示）
            if (Options.checkBranches) {
                for (int br : Options.BRANCH_IDS) {
                    int ob = Dungeon.branch;
                    Dungeon.branch = br;
                    Level bl = Dungeon.newLevel();
                    if (bl == null || bl instanceof DeadEndLevel) {
                        Dungeon.branch = ob;
                        continue;
                    }
                    for (Heap h : bl.heaps.valueList())
                        for (Item item : h.items)
                            fd.heapItems.add(new HeapItem(item, h));
                    for (Heap h : getMobDrops(bl))
                        for (Item item : h.items)
                            fd.heapItems.add(new HeapItem(item, h));
                    Dungeon.branch = ob;
                }
            }

            floorDataList.add(fd);

            Dungeon.branch = originalBranch;
            Dungeon.depth++;
        }
        SeedFinding = false;

        // Phase 2: 统一 identify 所有收集到的物品
        for (FloorData fd : floorDataList) {
            for (HeapItem hi : fd.heapItems)
                hi.item.identify();

            if (fd.ghostRewards != null)
                for (Item i : fd.ghostRewards) i.identify();

            if (fd.redDragonRewards != null)
                for (Item i : fd.redDragonRewards) i.identify();

            if (fd.wandmakerRewards != null)
                for (Item i : fd.wandmakerRewards) i.identify();

            if (fd.impRewards != null)
                for (Item i : fd.impRewards) i.identify();

            // 目标匹配楼层收集（仅老查种报告模式启用）
            if (matchedInfo != null)
                collectMatches(fd, matchedInfo);
        }

        // Phase 3: 生成文本
        StringBuilder result = new StringBuilder(Messages.get(SeedFinder.class, "seed") + seedCode + " (" + seed + ") " + Messages.get(SeedFinder.class, "items") + ":\n\n");
        for (FloorData fd : floorDataList) {
            result.append("\n_----- ").append((long) fd.depth).append(" ").append(Messages.get(SeedFinder.class, "floor")).append(" -----_\n\n");
            StringBuilder builder = new StringBuilder();
            ArrayList<HeapItem> scrolls = new ArrayList<>();
            ArrayList<HeapItem> potions = new ArrayList<>();
            ArrayList<HeapItem> equipment = new ArrayList<>();
            ArrayList<HeapItem> rings = new ArrayList<>();
            ArrayList<HeapItem> artifacts = new ArrayList<>();
            ArrayList<HeapItem> wands = new ArrayList<>();
            ArrayList<HeapItem> others = new ArrayList<>();
            ArrayList<HeapItem> forSales = new ArrayList<>();

            // 任务奖励（在地面物品之前展示）
            if (fd.ghostRewards != null) {
                this.addTextQuest("【 " + Messages.get(SeedFinder.class, "sad_ghost_reward") + " 】", fd.ghostRewards, builder);
            }
            if (fd.redDragonRewards != null) {
                this.addTextQuest("【 " + Messages.get(SeedFinder.class, "red_dragon_reward") + " 】", fd.redDragonRewards, builder);
            }
            if (fd.wandmakerRewards != null) {
                builder.append("【 ").append(Messages.get(SeedFinder.class, "wandmaker_need")).append(" ]:\n ");
                switch (fd.wandmakerType) {
                    case 1:
                    default:
                        builder.append(Messages.get(SeedFinder.class, "corpseDust")).append("\n\n");
                        break;
                    case 2:
                        builder.append(Messages.get(SeedFinder.class, "embers")).append("\n\n");
                        break;
                    case 3:
                        builder.append(Messages.get(SeedFinder.class, "rotBerry")).append("\n\n");
                }
                addTextQuest("【 " + Messages.get(SeedFinder.class, "wandmaker_reward") + " 】", fd.wandmakerRewards, builder);
            }
            if (fd.impRewards != null) {
                addTextQuest("【 " + Messages.get(SeedFinder.class, "imp_reward") + " 】", fd.impRewards, builder);
            }

            // 分类地面物品
            int gold = 0;
            for (HeapItem hi : fd.heapItems) {
                Item item = hi.item;
                Heap h = hi.heap;
                if (h.type == Type.FOR_SALE) {
                    forSales.add(hi);
                } else if (!blacklist.contains(item.getClass())) {
                    if (item instanceof Scroll)
                        scrolls.add(hi);
                    else if (item instanceof Potion)
                        potions.add(hi);
                    else if (!(item instanceof MeleeWeapon) && !(item instanceof Armor)) {
                        if (item instanceof Ring)
                            rings.add(hi);
                        else if (item instanceof Artifact)
                            artifacts.add(hi);
                        else if (item instanceof Wand)
                            wands.add(hi);
                        else if (item instanceof Gold)
                            gold += item.quantity();
                        else
                            others.add(hi);
                    } else
                        equipment.add(hi);
                }
            }
            if (gold != 0) {
                Gold goldA = new Gold(gold);
                Heap heapA = new Heap();
                heapA.items = new LinkedList<>();
                heapA.items.add(goldA);
                others.add(new HeapItem(goldA, heapA));
            }
            addTextItems("【 " + Messages.get(SeedFinder.class, "scrolls") +   " 】", scrolls, builder);
            addTextItems("【 " + Messages.get(SeedFinder.class, "potions") +   " 】", potions, builder);
            addTextItems("【 " + Messages.get(SeedFinder.class, "equipment") + " 】", equipment, builder);
            addTextItems("【 " + Messages.get(SeedFinder.class, "rings") +     " 】", rings, builder);
            addTextItems("【 " + Messages.get(SeedFinder.class, "artifacts") + " 】", artifacts, builder);
            addTextItems("【 " + Messages.get(SeedFinder.class, "wands")     + " 】", wands, builder);
            addTextItems("【 " + Messages.get(SeedFinder.class, "for_sales") + " 】", forSales, builder);
            addTextItems("【 " + Messages.get(SeedFinder.class, "others") +    " 】", others, builder);
            result.append(builder);
        }
        return result.toString();
    }

    /** 统计本层命中的目标物品（类精确 + 等级），写入 matchedInfo（每目标一条，取首次命中楼层） */
    private void collectMatches(FloorData fd, ArrayList<String> matchedInfo) {
        if (wantedArr.length == 0) return;
        boolean[] matched = new boolean[wantedArr.length];
        ArrayList<Item> all = new ArrayList<>();
        for (HeapItem hi : fd.heapItems) all.add(hi.item);
        if (fd.ghostRewards != null) all.addAll(fd.ghostRewards);
        if (fd.redDragonRewards != null) all.addAll(fd.redDragonRewards);
        if (fd.wandmakerRewards != null) all.addAll(fd.wandmakerRewards);
        if (fd.impRewards != null) all.addAll(fd.impRewards);
        for (Item item : all) {
            int[] candidates = matchIndex.get(item.getClass());
            if (candidates == null) continue;
            for (int idx : candidates) {
                if (!matched[idx] && wantedArr[idx].matches(item)) {
                    matched[idx] = true;
                    matchedInfo.add(item.toString() + " - "
                            + Messages.get(SeedFinder.class, "floor_at", fd.depth));
                }
            }
        }
    }

    private void addTextItems(String caption, ArrayList<HeapItem> items, StringBuilder builder) {
        if (!items.isEmpty()) {
            builder.append(caption).append(":\n");

            for (HeapItem item : items) {
                Item i = item.item;
                Heap h = item.heap;
                if (i instanceof DiedCrossBow || i instanceof MoonDao
                        || i instanceof SaiPlus || i instanceof RiceSword
                        || i instanceof RedBloodMoon || i instanceof GoldLongGun ||
                        i instanceof ClearSword || i instanceof ForestBow) {
                    builder.append("<#df00ff>" + Messages.get(this, "lengds") + "<RGB> - ").append(i.title().toLowerCase());
                } else if (((i instanceof Armor && ((Armor) i).hasGoodGlyph()) ||
                        (i instanceof Weapon && ((Weapon) i).hasGoodEnchant()) ||
                        (i instanceof Ring) || (i instanceof Wand)) && i.cursed && i.level <= 0)
                    builder.append("- " + Messages.get(this, "cursed")).append(i.title().toLowerCase());
                else if (i.cursed && i.level <= 0)
                    builder.append("- ").append(Messages.get(this, "cursed")).append(i.title().toLowerCase()).append("\n");
                else if ((i.level > 0) && i.cursed) {
                    builder.append("<#808080>").append(Messages.get(this, "cursed")).append(i.title().toLowerCase()).append("<RGB>\n");
                } else if ((i.level > 4)) {
                    builder.append("<#FFA500>").append(i.title().toLowerCase()).append("<RGB>\n");
                } else if ((i.level == 4)) {
                    builder.append("<#F00>").append(i.title().toLowerCase()).append("<RGB>\n");
                } else if ((i.level == 3)) {
                    builder.append("<#FF1493>").append(i.title().toLowerCase()).append("<RGB>\n");
                } else if ((i.level == 2)) {
                    builder.append("<#0F0>").append(i.title().toLowerCase()).append("<RGB>\n");
                } else if ((i.level == 1)) {
                    builder.append("_").append(i.title().toLowerCase()).append("_ \n");
                } else
                    builder.append("- ").append(i.title().toLowerCase());


                if (h != null) {
                    if (h.type != Type.HEAP) {
                        builder.append(" (").append(h.toString().toLowerCase()).append(")");
                    }
                }

                builder.append("\n");
            }

            builder.append("\n");
        }
    }

    private void addTextQuest(String caption, ArrayList<Item> items, StringBuilder builder) {
        if (!items.isEmpty()) {
            builder.append(caption).append(":\n");
            for (Item i : items) {
                if (i.cursed && i.level<=0)
                    builder.append("- ").append(Messages.get(this, "cursed")).append(i.title().toLowerCase()).append("\n");
                else if ((i.level>0) && i.cursed) {
                    builder.append("<#808080>").append(Messages.get(this, "cursed")).append(i.title().toLowerCase()).append("<RGB>\n");
                } else if ((i.level>4)) {
                    builder.append("<#FFA500>").append(i.title().toLowerCase()).append("<RGB>\n");
                } else if ((i.level==4)) {
                    builder.append("<#F00>").append(i.title().toLowerCase()).append("<RGB>\n");
                } else if ((i.level==3)) {
                    builder.append("<#FF1493>").append(i.title().toLowerCase()).append("<RGB>\n");
                } else if ((i.level==2)) {
                    builder.append("<#0F0>").append(i.title().toLowerCase()).append("<RGB>\n");
                } else if ((i.level==1)) {
                    builder.append("_").append(i.title().toLowerCase()).append("_ \n");
                }
                else
                    builder.append("- ").append(i.title().toLowerCase()).append("\n");
            }

            builder.append("\n");
        }
    }

    // 单层数据载体：Phase 1 收集、Phase 2 identify、Phase 3 展示
    private static final class FloorData {
        final int depth;
        final ArrayList<HeapItem> heapItems = new ArrayList<>();
        ArrayList<Item> ghostRewards = null;
        ArrayList<Item> redDragonRewards = null;
        ArrayList<Item> wandmakerRewards = null;
        int wandmakerType = 0;
        ArrayList<Item> impRewards = null;

        FloorData(int depth) {
            this.depth = depth;
        }
    }

    public static class HeapItem {
        public Item item;
        public Heap heap;

        public HeapItem(Item item, Heap heap) {
            this.item = item;
            this.heap = heap;
        }
    }
}
