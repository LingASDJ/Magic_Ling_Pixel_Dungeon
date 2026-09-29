package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RedDragon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.shatteredpixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredpixeldungeon.items.EnergyCrystal;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap.Type;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CommRelay;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.LloydsBeacon;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MasterThievesArmband;
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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.galaxy.SliverLockSword;
import com.shatteredpixel.shatteredpixeldungeon.levels.DeadEndLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Map;

public class NewSeedFinder implements Runnable {

    @Override
    public void run() {
        try {
            String str;
            if (wantedArr.length == 0)
                str = logSeedItems(DungeonSeed.convertFromText(SeedFindScene.seedCode));
            else
                str = findSeed();
            SeedFindScene.INSTANCE.text = str;
            SeedFindScene.INSTANCE.needUpdate = true;
        } finally {
            // 复位内存态种子，避免污染正常游戏
            Dungeon.overrideSeed = -1;
        }
    }

    public static volatile boolean running;
    public static volatile boolean SeedFinding = false;

    // ===== 图鉴中存在、但正常对局永远不会生成的物品（选择网格与匹配都应跳过） =====
    public static final HashSet<Class<? extends Item>> UNGENERATED = new HashSet<>(Arrays.asList(
            MasterThievesArmband.class,
            LloydsBeacon.class,
            CommRelay.class,
            SliverLockSword.class
    ));

    public static boolean isUngenerated(Class<?> cls) {
        for (Class<? extends Item> c : UNGENERATED) {
            if (c.isAssignableFrom(cls)) return true;
        }
        return false;
    }

    // 子层开关沿用旧查种器设置；遍历子层 1/2/3
    private static final boolean checkBranches = SPDSettings.logBranch();
    private static final int[] BRANCH_IDS = {1, 2, 3};

    protected final WantedTarget[] wantedArr;
    // Class → 目标下标数组：tryMatch 先查 map 取候选目标，跳过无关物品
    private final HashMap<Class<? extends Item>, int[]> matchIndex;
    protected final int floor;
    protected final HeroClass heroClass;
    protected NewSeedFinder(ArrayList<WantedTarget> wanted, int fl, HeroClass cl) {
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

    // 单线程环形扫描：随机起点 + 取模，覆盖整个种子域 [0, TOTAL_SEEDS)
    public final String findSeed() {
        String result = "NONE";
        SeedFinding = true;
        running = true;

        final long start = Random.Long(DungeonSeed.TOTAL_SEEDS);

        for (long j = 0; j < DungeonSeed.TOTAL_SEEDS
                && running && SeedFinding; ++j) {
            long currentSeed = (start + j) % DungeonSeed.TOTAL_SEEDS;

            if (Thread.currentThread().isInterrupted()) {
                running = false;
                break;
            }

            if (SeedFindScene.INSTANCE != null)
                SeedFindScene.INSTANCE.updateCurrentSeed(currentSeed);

            // 先单次命中，命中后才做 10 连复查（确认该种子多次生成结果一致）。
            // 旧实现对每个候选种子都跑 10 次 testSeed，且每次 testSeed 都会触发一次
            // Preferences 磁盘写，把搜索成本放大 10 倍以上——这是"看起来卡死、永不
            // 完成"的直接原因之一。复查只应作用于首轮命中的种子。
            if (testSeed(currentSeed)) {
                boolean confirmed = true;
                for (int r = 1; r < 10; r++) {
                    if (!testSeed(currentSeed)) {
                        confirmed = false;
                        break;
                    }
                }
                if (confirmed) {
                    result = logSeedItems(currentSeed);
                    break;
                }
            }
        }
        SeedFinding = false;
        return result;
    }

    protected boolean testSeed(long seed) {
        initRunWithSeed(seed);
        boolean[] itemsFound = new boolean[wantedArr.length];
        boolean ghostSeen = false, impSeen = false, wandmakerSeen = false, redDragonSeen = false;

        Dungeon.depth = 0;
        while (Dungeon.depth <= floor) {
            Dungeon.branch = 0;
            Level l = Dungeon.newLevel();
            if (l instanceof DeadEndLevel) {
                Dungeon.depth++;
                continue;
            }

            // 地面物品：遇物即匹配，不建中间表、不 identify
            if (matchHeaps(l, itemsFound)) return true;

            // 怪物掉落：雕像/铠甲雕像/宝箱怪
            if (matchMobs(l, itemsFound)) return true;

            if (!ghostSeen && Ghost.Quest.armor != null) {
                ghostSeen = true;
                if ((tryMatch(Ghost.Quest.armor, itemsFound)
                        || tryMatch(Ghost.Quest.weapon, itemsFound)) && allFound(itemsFound))
                    return true;
            }
            // 红龙之王任务奖励：戒指 / 神器或法杖 / 法杖 / 异国卷轴 五选一
            if (!redDragonSeen && RedDragon.Quest.armor != null) {
                redDragonSeen = true;
                Item[] rewards = {RedDragon.Quest.weapon, RedDragon.Quest.armor,
                        RedDragon.Quest.RingT, RedDragon.Quest.food, RedDragon.Quest.scrolls};
                if (tryMatchAny(rewards, itemsFound) && allFound(itemsFound))
                    return true;
            }
            if (!wandmakerSeen && Wandmaker.Quest.wand1 != null) {
                wandmakerSeen = true;
                Item w1 = Wandmaker.Quest.wand1;
                Item w2 = Wandmaker.Quest.wand2;
                if (wand != null && !wand.matches(w1) && !wand.matches(w2))
                    return false;
                if ((tryMatch(w1, itemsFound) || tryMatch(w2, itemsFound)) && allFound(itemsFound))
                    return true;
            }
            if (!impSeen && Imp.Quest.reward != null) {
                impSeen = true;
                if (ring != null && !ring.matches(Imp.Quest.reward))
                    return false;
                if (tryMatch(Imp.Quest.reward, itemsFound) && allFound(itemsFound))
                    return true;
            }

            // 子层：与主层同一深度，命中任意子层即算数
            if (checkBranches) {
                int curDepth = Dungeon.depth;
                for (int br : BRANCH_IDS) {
                    if (checkBranchLevel(curDepth, br, itemsFound)) return true;
                }
            }

            Dungeon.depth++;
        }
        return false;
    }

    // 主层/子层共用：匹配地面堆
    private boolean matchHeaps(Level l, boolean[] itemsFound) {
        for (Heap h : l.heaps.valueList())
            for (Item item : h.items)
                if (tryMatch(item, itemsFound) && allFound(itemsFound))
                    return true;
        return false;
    }

    // 主层/子层共用：匹配雕像/铠甲雕像/宝箱怪掉落
    private boolean matchMobs(Level l, boolean[] itemsFound) {
        for (Mob m : l.mobs) {
            if (m.getClass() == ArmoredStatue.class) {
                if (tryMatch(((ArmoredStatue) m).armor(), itemsFound) && allFound(itemsFound))
                    return true;
                if (tryMatch(((ArmoredStatue) m).weapon(), itemsFound) && allFound(itemsFound))
                    return true;
            }
            else if (m.getClass() == Statue.class) {
                if (tryMatch(((Statue) m).weapon(), itemsFound) && allFound(itemsFound))
                    return true;
            }
            else if (m instanceof Mimic) {
                for (Item item : ((Mimic) m).items)
                    if (tryMatch(item, itemsFound) && allFound(itemsFound))
                        return true;
            }
        }
        return false;
    }

    // 子层查种：保存 depth/branch → 生成子层 → 匹配地面与怪物掉落 → 还原
    private boolean checkBranchLevel(int depth, int branch, boolean[] itemsFound) {
        if (Thread.currentThread().isInterrupted()) return false;
        int originalBranch = Dungeon.branch;
        int originalDepth = Dungeon.depth;
        try {
            Dungeon.branch = branch;
            Dungeon.depth = depth;

            Level branchLevel = Dungeon.newLevel();
            if (branchLevel == null || branchLevel instanceof DeadEndLevel) {
                return false;
            }

            if (matchHeaps(branchLevel, itemsFound)) return true;
            return matchMobs(branchLevel, itemsFound);
        } catch (Exception e) {
            return false;
        } finally {
            Dungeon.branch = originalBranch;
            Dungeon.depth = originalDepth;
        }
    }

    private void initRunWithSeed(long seed) {
        // 内存态种子：直接设置 Dungeon.overrideSeed，避免每次测试写 Preferences 磁盘 flush。
        // Dungeon.init() 内部会重置 depth/branch/Quest 等静态状态，newLevel() 也会释放
        // 上一层的引用，连续测试不会累积旧楼层对象。
        Dungeon.overrideSeed = seed;
        GamesInProgress.selectedClass = heroClass;
        Dungeon.init();
    }

    private boolean tryMatch(Item item, boolean[] itemsFound) {
        if (item == null) return false;
        int[] candidates = matchIndex.get(item.getClass());
        if (candidates == null) return false;
        for (int idx : candidates) {
            if (!itemsFound[idx] && wantedArr[idx].matches(item)) {
                itemsFound[idx] = true;
                return true;
            }
        }
        return false;
    }

    private boolean tryMatchAny(Item[] items, boolean[] itemsFound) {
        boolean matched = false;
        for (Item item : items) {
            if (tryMatch(item, itemsFound)) matched = true;
        }
        return matched;
    }

    private static boolean allFound(boolean[] itemsFound) {
        for (boolean b : itemsFound) if (!b) return false;
        return true;
    }

    private ArrayList<Heap> getMobDrops(Level l) {
        ArrayList<Heap> heaps = new ArrayList<>();
        for (Mob m : l.mobs) {
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

    protected String logSeedItems(long seed) {
        String seedCode = DungeonSeed.convertToCode(seed);
        SeedFindScene.seedCode = seedCode;
        // 内存态种子：不写 Preferences（避免磁盘 IO），出口恢复调用前值
        long prevOverride = Dungeon.overrideSeed;
        Dungeon.overrideSeed = seed;
        GamesInProgress.selectedClass = heroClass;
        Dungeon.init();
        HashSet<Class<? extends Item>> blacklist = new HashSet<>(Arrays.asList(Dewdrop.class, IronKey.class, GoldenKey.class, CrystalKey.class, EnergyCrystal.class, CorpseDust.class, Embers.class, CeremonialCandle.class, Pickaxe.class));

        // Phase 1: 遍历所有楼层，收集物品（不 identify），任务奖励在出现层一次性收取并 complete
        ArrayList<FloorData> floorDataList = new ArrayList<>();
        Dungeon.depth = 0;
        SeedFinding = true;
        while (Dungeon.depth <= floor) {
            Dungeon.branch = 0;
            Level l = Dungeon.newLevel();
            int curDepth = Dungeon.depth;
            if (l instanceof DeadEndLevel) {
                Dungeon.depth++;
                continue;
            }

            FloorData fd = new FloorData(curDepth);

            // 地面物品
            for (Heap h : l.heaps.valueList())
                for (Item item : h.items)
                    fd.heapItems.add(new HeapItem(item, h));

            // 怪物掉落
            for (Heap h : getMobDrops(l))
                for (Item item : h.items)
                    fd.heapItems.add(new HeapItem(item, h));

            // 鬼魂任务奖励
            if (Ghost.Quest.armor != null) {
                ArrayList<Item> rewards = new ArrayList<>();
                rewards.add(Ghost.Quest.armor);
                rewards.add(Ghost.Quest.weapon);
                Ghost.Quest.complete();
                fd.ghostRewards = rewards;
            }
            // 红龙之王任务奖励（先收引用再 complete：complete 会清空 weapon/armor 静态字段）
            if (RedDragon.Quest.armor != null) {
                ArrayList<Item> rewards = new ArrayList<>();
                rewards.add(RedDragon.Quest.weapon);
                rewards.add(RedDragon.Quest.armor);
                rewards.add(RedDragon.Quest.RingT);
                rewards.add(RedDragon.Quest.food);
                rewards.add(RedDragon.Quest.scrolls);
                RedDragon.Quest.complete();
                fd.redDragonRewards = rewards;
            }
            // 工匠任务奖励（type 在 complete 前捕获）
            if (Wandmaker.Quest.wand1 != null) {
                ArrayList<Item> rewards = new ArrayList<>();
                rewards.add(Wandmaker.Quest.wand1);
                rewards.add(Wandmaker.Quest.wand2);
                fd.wandmakerType = Wandmaker.Quest.type();
                Wandmaker.Quest.complete();
                fd.wandmakerRewards = rewards;
            }
            // 小恶魔任务奖励
            if (Imp.Quest.reward != null) {
                ArrayList<Item> rewards = new ArrayList<>();
                rewards.add(Imp.Quest.reward);
                Imp.Quest.complete();
                fd.impRewards = rewards;
            }

            // 子层物品收集（同一深度）
            if (checkBranches) {
                for (int br : BRANCH_IDS) {
                    BranchData bd = collectBranch(curDepth, br);
                    if (bd != null) fd.branches.add(bd);
                }
            }

            floorDataList.add(fd);
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

            for (BranchData bd : fd.branches)
                for (HeapItem hi : bd.heapItems)
                    hi.item.identify();
        }

        // Phase 3: 生成旧版样式文本（彩色等级、传说紫字、诅咒前缀）
        StringBuilder result = new StringBuilder();
        result.append(Messages.get(SeedFinder.class, "seed")).append(seedCode)
                .append(" (").append(seed).append(") ")
                .append(Messages.get(SeedFinder.class, "items")).append(":\n\n");
        result.append(Messages.get(SeedFindScene.class, "hero_info",
                Messages.capitalize(heroClass.title()))).append("\n");
        result.append(Messages.get(SeedFinder.class, "css"))
                .append(Dungeon.challenges).append("\n\n");

        for (FloorData fd : floorDataList) {
            result.append("\n----- ").append(fd.depth).append(' ')
                    .append(Messages.get(SeedFinder.class, "floor")).append(" -----\n\n");

            StringBuilder builder = new StringBuilder();

            // 任务奖励（在地面物品之前展示）
            if (fd.ghostRewards != null)
                addTextQuest(caption("sad_ghost_reward"), fd.ghostRewards, builder);
            if (fd.redDragonRewards != null)
                addTextQuest(caption("red_dragon_reward"), fd.redDragonRewards, builder);
            if (fd.wandmakerRewards != null) {
                builder.append(caption("wandmaker_need")).append(":\n ");
                switch (fd.wandmakerType) {
                    case 2:
                        builder.append(Messages.get(SeedFinder.class, "embers")).append("\n\n");
                        break;
                    case 3:
                        builder.append(Messages.get(SeedFinder.class, "rotberry")).append("\n\n");
                        break;
                    case 1:
                    default:
                        builder.append(Messages.get(SeedFinder.class, "corpsedust")).append("\n\n");
                        break;
                }
                addTextQuest(caption("wandmaker_reward"), fd.wandmakerRewards, builder);
            }
            if (fd.impRewards != null)
                addTextQuest(caption("imp_reward"), fd.impRewards, builder);

            // 主层分类地面物品
            appendCategories(builder, categorize(fd.heapItems, blacklist));

            // 子层：每个子层独立标题与分类
            for (BranchData bd : fd.branches) {
                builder.append("\n----- ").append(fd.depth).append(' ')
                        .append(Messages.get(SeedFinder.class, "floor")).append(" (")
                        .append(Messages.get(SeedFinder.class, "branch_h")).append(bd.branch)
                        .append(Messages.get(SeedFinder.class, "branch_e")).append(") -----\n\n");
                appendCategories(builder, categorize(bd.heapItems, blacklist));
            }

            result.append(builder);
        }

        Dungeon.depth = 0;
        Dungeon.branch = 0;
        Dungeon.level = null;
        Dungeon.overrideSeed = prevOverride;
        return result.toString();
    }

    // 子层物品收集：保存/设置 depth+branch → newLevel → 收物 → 还原；死路返回 null
    private BranchData collectBranch(int depth, int branch) {
        int originalBranch = Dungeon.branch;
        int originalDepth = Dungeon.depth;
        try {
            Dungeon.branch = branch;
            Dungeon.depth = depth;

            Level branchLevel = Dungeon.newLevel();
            if (branchLevel == null || branchLevel instanceof DeadEndLevel) {
                return null;
            }

            BranchData bd = new BranchData(branch);
            for (Heap h : branchLevel.heaps.valueList())
                for (Item item : h.items)
                    bd.heapItems.add(new HeapItem(item, h));
            for (Heap h : getMobDrops(branchLevel))
                for (Item item : h.items)
                    bd.heapItems.add(new HeapItem(item, h));
            return bd;
        } catch (Exception e) {
            return null;
        } finally {
            Dungeon.branch = originalBranch;
            Dungeon.depth = originalDepth;
        }
    }

    private static String caption(String key) {
        return "【 " + Messages.get(SeedFinder.class, key) + " 】";
    }

    // 把物品按旧版分类归档
    private Categorized categorize(ArrayList<HeapItem> items, HashSet<Class<? extends Item>> blacklist) {
        Categorized c = new Categorized();
        for (HeapItem hi : items) {
            Item item = hi.item;
            Heap h = hi.heap;
            if (h.type == Type.FOR_SALE) {
                c.forSales.add(hi);
            } else if (!blacklist.contains(item.getClass())) {
                if (item instanceof Scroll)
                    c.scrolls.add(hi);
                else if (item instanceof Potion)
                    c.potions.add(hi);
                else if (!(item instanceof MeleeWeapon) && !(item instanceof Armor)) {
                    if (item instanceof Ring)
                        c.rings.add(hi);
                    else if (item instanceof Artifact)
                        c.artifacts.add(hi);
                    else if (item instanceof Wand)
                        c.wands.add(hi);
                    else if (item instanceof Gold)
                        c.gold += item.quantity();
                    else
                        c.others.add(hi);
                } else
                    c.equipment.add(hi);
            }
        }
        return c;
    }

    // 追加全部旧版分类段
    private void appendCategories(StringBuilder builder, Categorized c) {
        if (c.gold != 0) {
            Gold goldA = new Gold(c.gold);
            Heap heapA = new Heap();
            heapA.items = new LinkedList<>();
            heapA.items.add(goldA);
            c.others.add(new HeapItem(goldA, heapA));
        }
        addTextItems(caption("scrolls"), c.scrolls, builder);
        addTextItems(caption("potions"), c.potions, builder);
        addTextItems(caption("equipment"), c.equipment, builder);
        addTextItems(caption("rings"), c.rings, builder);
        addTextItems(caption("artifacts"), c.artifacts, builder);
        addTextItems(caption("wands"), c.wands, builder);
        addTextItems(caption("for_sales"), c.forSales, builder);
        addTextItems(caption("others"), c.others, builder);
    }

    private static boolean isLegend(Item i) {
        return i instanceof DiedCrossBow || i instanceof MoonDao || i instanceof SaiPlus
                || i instanceof RiceSword || i instanceof RedBloodMoon || i instanceof GoldLongGun
                || i instanceof ClearSword || i instanceof ForestBow;
    }

    // 地面/怪物掉落物品的旧版彩色命名（不自带末尾换行，由调用方补）
    private void appendStyledName(StringBuilder builder, Item i) {
        String name = i.title().toLowerCase();
        if (isLegend(i)) {
            builder.append("<#df00ff>").append(Messages.get(SeedFinder.class, "lengds"))
                    .append("<RGB> - ").append(name);
        } else if (((i instanceof Armor && ((Armor) i).hasGoodGlyph())
                || (i instanceof Weapon && ((Weapon) i).hasGoodEnchant())
                || (i instanceof Ring) || (i instanceof Wand)) && i.cursed && i.level <= 0) {
            builder.append("- ").append(Messages.get(SeedFinder.class, "cursed")).append(name);
        } else if (i.cursed && i.level <= 0) {
            builder.append("- ").append(Messages.get(SeedFinder.class, "cursed")).append(name).append("\n");
        } else if (i.level > 0 && i.cursed) {
            builder.append("<#808080>").append(Messages.get(SeedFinder.class, "cursed"))
                    .append(name).append("<RGB>\n");
        } else if (i.level > 4) {
            builder.append("<#FFA500>").append(name).append("<RGB>\n");
        } else if (i.level == 4) {
            builder.append("<#F00>").append(name).append("<RGB>\n");
        } else if (i.level == 3) {
            builder.append("<#FF1493>").append(name).append("<RGB>\n");
        } else if (i.level == 2) {
            builder.append("<#0F0>").append(name).append("<RGB>\n");
        } else if (i.level == 1) {
            builder.append("_").append(name).append("_ \n");
        } else {
            builder.append("- ").append(name);
        }
    }

    // 任务奖励物品的旧版彩色命名（每行自带换行）
    private void appendStyledQuestName(StringBuilder builder, Item i) {
        String name = i.title().toLowerCase();
        if (i.cursed && i.level <= 0) {
            builder.append("- ").append(Messages.get(SeedFinder.class, "cursed")).append(name).append("\n");
        } else if (i.level > 0 && i.cursed) {
            builder.append("<#808080>").append(Messages.get(SeedFinder.class, "cursed"))
                    .append(name).append("<RGB>\n");
        } else if (i.level > 4) {
            builder.append("<#FFA500>").append(name).append("<RGB>\n");
        } else if (i.level == 4) {
            builder.append("<#F00>").append(name).append("<RGB>\n");
        } else if (i.level == 3) {
            builder.append("<#FF1493>").append(name).append("<RGB>\n");
        } else if (i.level == 2) {
            builder.append("<#0F0>").append(name).append("<RGB>\n");
        } else if (i.level == 1) {
            builder.append("_").append(name).append("_ \n");
        } else {
            builder.append("- ").append(name).append("\n");
        }
    }

    private void addTextItems(String caption, ArrayList<HeapItem> items, StringBuilder builder) {
        if (!items.isEmpty()) {
            builder.append(caption).append(":\n");
            for (HeapItem item : items) {
                Item i = item.item;
                Heap h = item.heap;
                appendStyledName(builder, i);
                if (h != null && h.type != Type.HEAP) {
                    builder.append(" (").append(h.toString().toLowerCase()).append(")");
                }
                builder.append("\n");
            }
            builder.append("\n");
        }
    }

    private void addTextQuest(String caption, ArrayList<Item> items, StringBuilder builder) {
        if (!items.isEmpty()) {
            builder.append(caption).append(":\n");
            for (Item i : items)
                appendStyledQuestName(builder, i);
            builder.append("\n");
        }
    }

    // 分类容器
    private static final class Categorized {
        final ArrayList<HeapItem> scrolls = new ArrayList<>();
        final ArrayList<HeapItem> potions = new ArrayList<>();
        final ArrayList<HeapItem> equipment = new ArrayList<>();
        final ArrayList<HeapItem> rings = new ArrayList<>();
        final ArrayList<HeapItem> artifacts = new ArrayList<>();
        final ArrayList<HeapItem> wands = new ArrayList<>();
        final ArrayList<HeapItem> forSales = new ArrayList<>();
        final ArrayList<HeapItem> others = new ArrayList<>();
        int gold = 0;
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
        final ArrayList<BranchData> branches = new ArrayList<>();

        FloorData(int depth) {
            this.depth = depth;
        }
    }

    // 子层数据载体
    private static final class BranchData {
        final int branch;
        final ArrayList<HeapItem> heapItems = new ArrayList<>();

        BranchData(int branch) {
            this.branch = branch;
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
