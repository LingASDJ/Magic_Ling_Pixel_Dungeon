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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.shatteredpixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredpixeldungeon.items.EnergyCrystal;
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
import com.shatteredpixel.shatteredpixeldungeon.levels.DeadEndLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
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

            if (SeedFindScene.INSTANCE != null)
                SeedFindScene.INSTANCE.updateCurrentSeed(currentSeed);

            // 10 连复查：命中目标必须落在该种子各楼层生成变体的交集内
            boolean confirmed = true;
            for (int r = 0; r < 10; r++) {
                if (!testSeed(currentSeed)) {
                    confirmed = false;
                    break;
                }
            }
            if (confirmed) {
                result = logSeedItems(currentSeed);
                break;
            }

            if (Thread.currentThread().isInterrupted()) {
                running = false;
                break;
            }
        }
        SeedFinding = false;
        return result;
    }

    protected boolean testSeed(long seed) {
        initRunWithSeed(seed);
        boolean[] itemsFound = new boolean[wantedArr.length];
        int foundCount = 0;
        int n = wantedArr.length;
        boolean ghostSeen = false, impSeen = false, wandmakerSeen = false;

        Dungeon.depth = 0;
        while (Dungeon.depth <= floor) {
            Dungeon.branch = 0;
            Level l = Dungeon.newLevel();
            if (l instanceof DeadEndLevel) {
                Dungeon.depth++;
                continue;
            }

            // 地面物品：遇物即匹配，不建中间表、不 identify
            for (Heap h : l.heaps.valueList())
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
            if (!ghostSeen && Ghost.Quest.armor != null) {
                ghostSeen = true;
                if ((tryMatch(Ghost.Quest.armor, itemsFound)
                        || tryMatch(Ghost.Quest.weapon, itemsFound)) && ++foundCount == n)
                    return true;
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

            Dungeon.depth++;
        }
        return false;
    }

    private void initRunWithSeed(long seed) {
        SPDSettings.customSeed(DungeonSeed.convertToCode(seed));
        GamesInProgress.selectedClass = heroClass;
        Dungeon.init();
    }

    private boolean tryMatch(Item item, boolean[] itemsFound) {
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
        SPDSettings.customSeed(seedCode);
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

            if (fd.wandmakerRewards != null)
                for (Item i : fd.wandmakerRewards) i.identify();

            if (fd.impRewards != null)
                for (Item i : fd.impRewards) i.identify();
        }

        // Phase 3: 生成文本
        StringBuilder result = new StringBuilder("种子 " + seedCode + " (" + seed + ") 物品列表：\n\n");
        for (FloorData fd : floorDataList) {
            result.append("\n----- 第").append(fd.depth).append("层 -----\n\n");
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
                this.addTextQuest("[ 伤心幽灵的奖励 ]", fd.ghostRewards, builder);
            }
            if (fd.wandmakerRewards != null) {
                builder.append("[ 工匠的需求 ]:\n ");
                switch (fd.wandmakerType) {
                    case 1:
                    default:
                        builder.append("腐尸尘土").append("\n\n");
                        break;
                    case 2:
                        builder.append("余烬").append("\n\n");
                        break;
                    case 3:
                        builder.append("腐烂浆果").append("\n\n");
                }
                addTextQuest("[ 工匠的奖励 ]", fd.wandmakerRewards, builder);
            }
            if (fd.impRewards != null) {
                addTextQuest("[ 小恶魔的奖励 ]", fd.impRewards, builder);
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
            addTextItems("[ 卷轴 ]", scrolls, builder);
            addTextItems("[ 药水 ]", potions, builder);
            addTextItems("[ 装备 ]", equipment, builder);
            addTextItems("[ 戒指 ]", rings, builder);
            addTextItems("[ 神器 ]", artifacts, builder);
            addTextItems("[ 法杖 ]", wands, builder);
            addTextItems("[ 商店 ]", forSales, builder);
            addTextItems("[ 其他 ]", others, builder);
            result.append(builder);
        }

        Dungeon.depth = 0;
        Dungeon.branch = 0;
        return result.toString();
    }

    private void addTextItems(String caption, ArrayList<HeapItem> items, StringBuilder builder) {
        if (!items.isEmpty()) {
            builder.append(caption).append(":\n");
            for (HeapItem item : items) {
                Item i = item.item;
                Heap h = item.heap;
                if (!(i instanceof Armor && ((Armor) i).hasCurseGlyph()
                        || i instanceof Weapon && ((Weapon) i).hasCurseEnchant()) && i.cursed)
                    builder.append("- 诅咒的").append(i);
                else
                    builder.append("- ").append(i);
                if (h.type != Type.HEAP) {
                    String heap = h.toString();
                    if (h.type == Type.FOR_SALE)
                        heap = Shopkeeper.sellPrice(h.peek()) + "金币";
                    builder.append("(").append(heap).append(")");
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
                if (i.cursed)
                    builder.append("- 诅咒的").append(i).append("\n");
                else
                    builder.append("- ").append(i).append("\n");
            builder.append("\n");
        }
    }

    // 单层数据载体：Phase 1 收集、Phase 2 identify、Phase 3 展示
    private static final class FloorData {
        final int depth;
        final ArrayList<HeapItem> heapItems = new ArrayList<>();
        ArrayList<Item> ghostRewards = null;
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
