package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import com.badlogic.gdx.Gdx;
import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ArmoredStatue;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SeedFinder {
	enum Condition {ANY, ALL}
	enum FINDING {STOP, CONTINUE}

	public static volatile FINDING findingStatus = FINDING.STOP;

	public static class Options {
		public static int floors;
		public static Condition condition;
		public static long seed;
		public static boolean checkBranches = SPDSettings.logBranch();
		public static int[] BRANCH_IDS = {1, 2, 3};
	}

	static class HeapItem {
		public Item item;
		public Heap heap;

		public HeapItem(Item item, Heap heap) {
			this.item = item;
			this.heap = heap;
		}
	}

	List<Class<? extends Item>> blacklist;
	ArrayList<String> itemList;
	private final List<String> matchedFloorInfo = new ArrayList<>();

	private long startTime;
	private volatile boolean running;

	/** 场景轮询显示的计时快照（跨线程只读，不依赖搜索实例） */
	public static volatile long uiStartTime = 0;

	public static String getUiElapsedTime() {
		if (uiStartTime == 0) {
			return "00:00:00";
		}
		long elapsedMillis = System.currentTimeMillis() - uiStartTime;
		long seconds = (elapsedMillis / 1000) % 60;
		long minutes = (elapsedMillis / (1000 * 60)) % 60;
		long hours = (elapsedMillis / (1000 * 60 * 60)) % 24;
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	public void startTimer() {
		startTime = System.currentTimeMillis();
		uiStartTime = startTime;
		running = true;
	}

	@SuppressWarnings("DefaultLocale")
	public String getElapsedTime() {
		if (!running) {
			return "ElaseTime NoLauncher";
		}
		long elapsedMillis = System.currentTimeMillis() - startTime;
		long seconds = (elapsedMillis / 1000) % 60;
		long minutes = (elapsedMillis / (1000 * 60)) % 60;
		long hours = (elapsedMillis / (1000 * 60 * 60)) % 24;
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	public SeedResult findSeed(String[] wanted, int floor) {
		itemList = new ArrayList<>(Arrays.asList(wanted));
		findingStatus = FINDING.CONTINUE;
		Options.condition = SPDSettings.seedfinderConditionANY() ? Condition.ANY : Condition.ALL;
		startTimer();
		matchedFloorInfo.clear();

		// 单线程搜索：进度由 SeedFindLogScene.update() 轮询显示，写槽 0
		searchThreadCount = 1;
		parallelSeeds.set(0, -1);

		SeedResult emptyResult = new SeedResult("NONE", "", new ArrayList<>(), false);
		long startSeed = Random.Long(DungeonSeed.TOTAL_SEEDS);

		for (long i = 0; i < DungeonSeed.TOTAL_SEEDS && findingStatus == FINDING.CONTINUE; i++) {
			if (Thread.currentThread().isInterrupted()) {
				running = false;
				return emptyResult;
			}

			long seedValue = (startSeed + i) % DungeonSeed.TOTAL_SEEDS;
			String seedStr = Long.toString(seedValue);

			// 只写共享状态，不再向渲染线程投递 postRunnable
			parallelSeeds.set(0, seedValue);

			matchedFloorInfo.clear();
			if (testSeedALL(seedStr, floor)) {
				String logText = logSeedItems(seedStr, floor, SPDSettings.challenges());
				SeedResult successResult = new SeedResult(logText, seedStr, new ArrayList<>(matchedFloorInfo), true);
				running = false;
				findingStatus = FINDING.STOP;
				return successResult;
			}
		}

		running = false;
		findingStatus = FINDING.STOP;
		return emptyResult;
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
				for (Item item : ((Mimic) m).items)
					h.items.add(item.identify());
				h.type = Type.HEAP;
				heaps.add(h);
			}
		}
		return heaps;
	}

	private boolean checkBranchLevel(int depth, int branch, boolean[] itemsFound) {
		if (Thread.currentThread().isInterrupted()) return false;
		try {
			int originalBranch = Dungeon.branch;
			int originalDepth = Dungeon.depth;

			Dungeon.branch = branch;
			Dungeon.depth = depth;

			Level branchLevel = Dungeon.newLevel();
			if (branchLevel == null || branchLevel instanceof DeadEndLevel) {
				Dungeon.branch = originalBranch;
				Dungeon.depth = originalDepth;
				return false;
			}

			ArrayList<Heap> branchHeaps = new ArrayList<>(branchLevel.heaps.valueList());
			branchHeaps.addAll(getMobDrops(branchLevel));

			for (Heap h : branchHeaps) {
				for (Item item : h.items) {
					item.identify();
					String itemName = item.title().toLowerCase();

					for (int j = 0; j < itemList.size(); j++) {
						if (itemsFound[j]) continue;
						String wantingItem = itemList.get(j);
						boolean precise = wantingItem.startsWith("\"") && wantingItem.endsWith("\"");
						String cleanWant = wantingItem.replaceAll("\"", "");

						boolean match;
						if (precise) {
							match = itemName.equals(cleanWant);
						} else {
							match = itemName.replaceAll(" ", "").contains(cleanWant.replaceAll(" ", ""));
						}

						if (match) {
							itemsFound[j] = true;
							matchedFloorInfo.add(cleanWant + " → 第" + Dungeon.depth + "层");
							if (areAllTrue(itemsFound)) {
								Dungeon.branch = originalBranch;
								Dungeon.depth = originalDepth;
								return true;
							}
							break;
						}
					}
				}
			}

			Dungeon.branch = originalBranch;
			Dungeon.depth = originalDepth;
			return areAllTrue(itemsFound);
		} catch (Exception e) {
			Gdx.app.log("SeedFinder", "Error checking branch " + branch + " depth " + depth + ": " + e.getMessage());
			return false;
		}
	}

	private boolean testSeedALL(String seed, int floors) {
		if (Thread.currentThread().isInterrupted()) return false;
		try {
			Dungeon.isDLC(Conducts.Conduct.SEED);
			SPDSettings.customSeed(seed);
			Dungeon.initSeed();
			GamesInProgress.selectedClass = HeroClass.WARRIOR;
			Dungeon.init();

			boolean[] itemsFound = new boolean[itemList.size()];
			Arrays.fill(itemsFound, false);

			for (int i = 0; i < floors; i++) {
				if (Thread.currentThread().isInterrupted()) return false;
				int originalBranch = Dungeon.branch;
				Dungeon.branch = 0;

				Level l = Dungeon.newLevel();
				if (l == null || l instanceof DeadEndLevel) {
					Dungeon.branch = originalBranch;
					Dungeon.depth++;
					continue;
				}

				ArrayList<Heap> heaps = new ArrayList<>(l.heaps.valueList());
				heaps.addAll(getMobDrops(l));

				// Ghost任务装备
				if (Ghost.Quest.armor != null) {
					Item target = Ghost.Quest.armor.identify();
					String targetName = target.title().toLowerCase();
					for (int j = 0; j < itemList.size(); j++) {
						if (itemsFound[j]) continue;
						String want = itemList.get(j);
						boolean precise = want.startsWith("\"") && want.endsWith("\"");
						String cleanWant = want.replaceAll("\"", "");
						boolean match = precise ? targetName.equals(cleanWant) : targetName.replaceAll(" ", "").contains(cleanWant.replaceAll(" ", ""));
						if (match) {
							itemsFound[j] = true;
							matchedFloorInfo.add(cleanWant + " → 第" + Dungeon.depth + "层");
							break;
						}
					}
				}

				// 制杖师任务
				if (Wandmaker.Quest.wand1 != null) {
					Item w1 = Wandmaker.Quest.wand1.identify();
					Item w2 = Wandmaker.Quest.wand2.identify();
					String w1Name = w1.title().toLowerCase();
					String w2Name = w2.title().toLowerCase();
					String questMat = "";
					int questType = Wandmaker.Quest.type();
					if (questType == 1) {
						questMat = Messages.get(this, "corpsedust");
					} else if (questType == 2) {
						questMat = Messages.get(this, "embers");
					} else if (questType == 3) {
						questMat = Messages.get(this, "rotberry");
					}

					for (int j = 0; j < itemList.size(); j++) {
						if (itemsFound[j]) continue;
						String want = itemList.get(j);
						boolean precise = want.startsWith("\"") && want.endsWith("\"");
						String cleanWant = want.replaceAll("\"", "");
						String cWant = cleanWant.replaceAll(" ", "");
						String cW1 = w1Name.replaceAll(" ", "");
						String cW2 = w2Name.replaceAll(" ", "");

						boolean matchWand = precise
								? (w1Name.equals(cleanWant) || w2Name.equals(cleanWant))
								: (cW1.contains(cWant) || cW2.contains(cWant));
						boolean matchMat = !precise && questMat.replaceAll(" ", "").contains(cWant);

						if (matchWand || matchMat) {
							itemsFound[j] = true;
							matchedFloorInfo.add(cleanWant + " → 第" + Dungeon.depth + "层");
							break;
						}
					}
				}

				// 小恶魔戒指奖励
				if (Imp.Quest.reward != null) {
					Item ring = Imp.Quest.reward.identify();
					String ringName = ring.title().toLowerCase();
					for (int j = 0; j < itemList.size(); j++) {
						if (itemsFound[j]) continue;
						String want = itemList.get(j);
						boolean precise = want.startsWith("\"") && want.endsWith("\"");
						String cleanWant = want.replaceAll("\"", "");
						boolean match = precise ? ringName.equals(cleanWant) : ringName.replaceAll(" ", "").contains(cleanWant.replaceAll(" ", ""));
						if (match) {
							itemsFound[j] = true;
							matchedFloorInfo.add(cleanWant + " → 第" + Dungeon.depth + "层");
							break;
						}
					}
				}

				// 普通堆物品
				for (Heap h : heaps) {
					for (Item item : h.items) {
						item.identify();
						String itemName = item.title().toLowerCase();
						for (int j = 0; j < itemList.size(); j++) {
							if (itemsFound[j]) continue;
							String want = itemList.get(j);
							boolean precise = want.startsWith("\"") && want.endsWith("\"");
							String cleanWant = want.replaceAll("\"", "");
							boolean match = precise
									? itemName.equals(cleanWant)
									: itemName.replaceAll(" ", "").contains(cleanWant.replaceAll(" ", ""));
							if (match) {
								itemsFound[j] = true;
								matchedFloorInfo.add(cleanWant + " → 第" + Dungeon.depth + "层");
								break;
							}
						}
					}
				}

				// 检查分支
				if (Options.checkBranches && !areAllTrue(itemsFound)) {
					int curDepth = Dungeon.depth;
					for (int br : Options.BRANCH_IDS) {
						if (checkBranchLevel(curDepth, br, itemsFound)) {
							return true;
						}
					}
				}

				if (areAllTrue(itemsFound)) return true;

				Dungeon.branch = originalBranch;
				Dungeon.depth++;
			}

			if (Options.condition == Condition.ANY) {
				for (boolean b : itemsFound) if (b) return true;
				return false;
			} else {
				return areAllTrue(itemsFound);
			}
		} finally {
			// 清理Dungeon状态，防止内存堆积卡死
			Dungeon.depth = 0;
			Dungeon.branch = 0;
		}
	}

	private void logBranchItems(int depth, int branch, StringBuilder builder) {
		try {
			int originalBranch = Dungeon.branch;
			int originalDepth = Dungeon.depth;
			Dungeon.branch = branch;
			Dungeon.depth = depth;

			Level branchLevel = Dungeon.newLevel();
			if (branchLevel == null || branchLevel instanceof DeadEndLevel) {
				Dungeon.branch = originalBranch;
				Dungeon.depth = originalDepth;
				return;
			}

			builder.append("\n----- ").append(depth).append(" ").append(Messages.get(this, "floor"))
					.append(" (").append(Messages.get(this, "branch_h")).append(branch).append(Messages.get(this, "branch_e")).append(") -----\n\n");

			ArrayList<Heap> branchHeaps = new ArrayList<>(branchLevel.heaps.valueList());
			branchHeaps.addAll(getMobDrops(branchLevel));

			ArrayList<HeapItem> scrolls = new ArrayList<>();
			ArrayList<HeapItem> potions = new ArrayList<>();
			ArrayList<HeapItem> equipment = new ArrayList<>();
			ArrayList<HeapItem> rings = new ArrayList<>();
			ArrayList<HeapItem> artifacts = new ArrayList<>();
			ArrayList<HeapItem> wands = new ArrayList<>();
			ArrayList<HeapItem> others = new ArrayList<>();
			ArrayList<HeapItem> forSales = new ArrayList<>();

			for (Heap h : branchHeaps) {
				for (Item item : h.items) {
					item.identify();
					if (h.type == Type.FOR_SALE) {
						forSales.add(new HeapItem(item, h));
					} else if (blacklist.contains(item.getClass())) {
						continue;
					} else if (item instanceof Scroll) scrolls.add(new HeapItem(item, h));
					else if (item instanceof Potion) potions.add(new HeapItem(item, h));
					else if (item instanceof MeleeWeapon || item instanceof Armor) equipment.add(new HeapItem(item, h));
					else if (item instanceof Ring) rings.add(new HeapItem(item, h));
					else if (item instanceof Artifact) artifacts.add(new HeapItem(item, h));
					else if (item instanceof Wand) wands.add(new HeapItem(item, h));
					else others.add(new HeapItem(item, h));
				}
			}

			addTextItems("【 " + Messages.get(this, "scrolls") + " 】", scrolls, builder);
			addTextItems("【 " + Messages.get(this, "potions") + " 】", potions, builder);
			addTextItems("【 " + Messages.get(this, "equipment") + " 】", equipment, builder);
			addTextItems("【 " + Messages.get(this, "rings") + " 】", rings, builder);
			addTextItems("【 " + Messages.get(this, "artifacts") + " 】", artifacts, builder);
			addTextItems("【 " + Messages.get(this, "wands") + " 】", wands, builder);
			addTextItems("【 " + Messages.get(this, "for_sales") + " 】", forSales, builder);
			addTextItems("【 " + Messages.get(this, "others") + " 】", others, builder);

			Dungeon.branch = originalBranch;
			Dungeon.depth = originalDepth;
		} catch (Exception e) {
			Gdx.app.log("SeedFinder", "Log branch err br:" + branch + " " + e.getMessage());
		}
	}

	public String logSeedItems(String seed, int floors, int challenges) {
		String text = DungeonSeed.formatText(seed);
		SPDSettings.customSeed(text);
		GamesInProgress.selectedClass = HeroClass.WARRIOR;
		SPDSettings.challenges(challenges);
		Dungeon.init();

		StringBuilder result = new StringBuilder(Messages.get(this, "seed") + DungeonSeed.convertToCode(Dungeon.seed) + " (" + Dungeon.seed + ") " + Messages.get(this, "items") + ":\n\n" + Messages.get(this, "css") + Dungeon.challenges + "\n\n");

		blacklist = Arrays.asList(
				Gold.class, Dewdrop.class, IronKey.class, GoldenKey.class, CrystalKey.class, EnergyCrystal.class,
				CorpseDust.class, Embers.class, CeremonialCandle.class, Pickaxe.class
		);

		for (int i = 0; i < floors; i++) {
			int originalBranch = Dungeon.branch;
			Dungeon.branch = 0;
			Level l = Dungeon.newLevel();
			if (l == null || l instanceof DeadEndLevel) {
				Dungeon.branch = originalBranch;
				Dungeon.depth++;
				continue;
			}

			result.append("\n----- ").append(Dungeon.depth).append(" ").append(Messages.get(this, "floor")).append(" -----\n\n");
			ArrayList<Heap> heaps = new ArrayList<>(l.heaps.valueList());
			StringBuilder builder = new StringBuilder();

			ArrayList<HeapItem> scrolls = new ArrayList<>();
			ArrayList<HeapItem> potions = new ArrayList<>();
			ArrayList<HeapItem> equipment = new ArrayList<>();
			ArrayList<HeapItem> rings = new ArrayList<>();
			ArrayList<HeapItem> artifacts = new ArrayList<>();
			ArrayList<HeapItem> wands = new ArrayList<>();
			ArrayList<HeapItem> others = new ArrayList<>();
			ArrayList<HeapItem> forSales = new ArrayList<>();

			// 幽灵任务
			if (Ghost.Quest.armor != null) {
				ArrayList<Item> rewards = new ArrayList<>();
				rewards.add(Ghost.Quest.armor.identify());
				rewards.add(Ghost.Quest.weapon.identify());
				Ghost.Quest.complete();
				addTextQuest("【 " + Messages.get(this, "sad_ghost_reward") + " 】", rewards, builder);
			}

			// 红龙任务
			if (RedDragon.Quest.armor != null) {
				ArrayList<Item> rewards = new ArrayList<>();
				rewards.add(RedDragon.Quest.weapon.identify());
				rewards.add(RedDragon.Quest.RingT.identify());
				rewards.add(RedDragon.Quest.food.identify());
				rewards.add(RedDragon.Quest.scrolls.identify());
				RedDragon.Quest.complete();
				addTextQuest("【 " + Messages.get(this, "red_dragon_reward") + " 】", rewards, builder);
			}

			// 制杖师
			if (Wandmaker.Quest.wand1 != null) {
				ArrayList<Item> rewards = new ArrayList<>();
				rewards.add(Wandmaker.Quest.wand1.identify());
				rewards.add(Wandmaker.Quest.wand2.identify());
				Wandmaker.Quest.complete();
				builder.append("【 " + Messages.get(this, "wandmaker_need") + " 】:\n ");
				switch (Wandmaker.Quest.type()) {
					case 1: default: builder.append(Messages.get(this, "corpsedust")).append("\n\n"); break;
					case 2: builder.append(Messages.get(this, "embers")).append("\n\n"); break;
					case 3: builder.append(Messages.get(this, "rotberry")).append("\n\n"); break;
				}
				addTextQuest("【 " + Messages.get(this, "wandmaker_reward") + " 】", rewards, builder);
			}

			// 小恶魔
			if (Imp.Quest.reward != null) {
				ArrayList<Item> rewards = new ArrayList<>();
				rewards.add(Imp.Quest.reward.identify());
				Imp.Quest.complete();
				addTextQuest("【 " + Messages.get(this, "imp_reward") + " 】", rewards, builder);
			}

			heaps.addAll(getMobDrops(l));
			for (Heap h : heaps) {
				for (Item item : h.items) {
					item.identify();
					if (h.type == Type.FOR_SALE) {
						forSales.add(new HeapItem(item, h));
					} else if (blacklist.contains(item.getClass())) {
						continue;
					} else if (item instanceof Scroll) scrolls.add(new HeapItem(item, h));
					else if (item instanceof Potion) potions.add(new HeapItem(item, h));
					else if (item instanceof MeleeWeapon || item instanceof Armor) equipment.add(new HeapItem(item, h));
					else if (item instanceof Ring) rings.add(new HeapItem(item, h));
					else if (item instanceof Artifact) artifacts.add(new HeapItem(item, h));
					else if (item instanceof Wand) wands.add(new HeapItem(item, h));
					else others.add(new HeapItem(item, h));
				}
			}

			addTextItems("【 " + Messages.get(this, "scrolls") + " 】", scrolls, builder);
			addTextItems("【 " + Messages.get(this, "potions") + " 】", potions, builder);
			addTextItems("【 " + Messages.get(this, "equipment") + " 】", equipment, builder);
			addTextItems("【 " + Messages.get(this, "rings") + " 】", rings, builder);
			addTextItems("【 " + Messages.get(this, "artifacts") + " 】", artifacts, builder);
			addTextItems("【 " + Messages.get(this, "wands") + " 】", wands, builder);
			addTextItems("【 " + Messages.get(this, "for_sales") + " 】", forSales, builder);
			addTextItems("【 " + Messages.get(this, "others") + " 】", others, builder);

			if (Options.checkBranches) {
				int curD = Dungeon.depth;
				for (int br : Options.BRANCH_IDS) logBranchItems(curD, br, builder);
			}

			result.append("\n").append(builder);
			Dungeon.branch = originalBranch;
			Dungeon.depth++;
		}
		return result.toString();
	}

	private void addTextItems(String caption, ArrayList<HeapItem> items, StringBuilder builder) {
		if (!items.isEmpty()) {
			builder.append(caption).append(":\n");

			for (HeapItem item : items) {
				Item i = item.item;
				Heap h = item.heap;
				if(i instanceof DiedCrossBow || i instanceof MoonDao
						|| i instanceof SaiPlus || i instanceof RiceSword
						|| i instanceof RedBloodMoon || i instanceof GoldLongGun ||
						i instanceof ClearSword || i instanceof ForestBow){
					builder.append("<#df00ff>"+Messages.get(this,"lengds")+"<RGB> - ").append(i.title().toLowerCase());
				} else if (((i instanceof Armor && ((Armor) i).hasGoodGlyph()) ||
						(i instanceof Weapon && ((Weapon) i).hasGoodEnchant()) ||
						(i instanceof Ring) || (i instanceof Wand)) && i.cursed && i.level<=0)
					builder.append("- " + Messages.get(this, "cursed")).append(i.title().toLowerCase());
				else if (i.cursed && i.level<=0)
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
				} else
					builder.append("- ").append(i.title().toLowerCase());


				if(h != null){
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

	private static boolean areAllTrue(boolean[] array) {
		for (boolean b : array) if (!b) return false;
		return true;
	}

	// ===== 新增：强力查种（3 线程分片） =====
	public static volatile boolean parallelFound = false;
	/** Dungeon 是全局静态状态，多线程操作必须串行化 */
	public static final Object DUNGEON_LOCK = new Object();

	/** 当前搜索线程数，供 SeedFindLogScene 轮询显示 */
	public static volatile int searchThreadCount = 1;

	public static final java.util.concurrent.atomic.AtomicLongArray parallelSeeds = new java.util.concurrent.atomic.AtomicLongArray(4);

	public SeedResult findSeedParallel(String[] wanted, int floors, int threadCount) {
		itemList = new ArrayList<>(Arrays.asList(wanted));
		findingStatus = FINDING.CONTINUE;
		parallelFound = false;
		Options.condition = SPDSettings.seedfinderConditionANY() ? Condition.ANY : Condition.ALL;
		startTimer();
		matchedFloorInfo.clear();

		// 清零每个线程的当前种子槽位
		searchThreadCount = threadCount;
		for (int t = 0; t < parallelSeeds.length(); t++) parallelSeeds.set(t, -1);

		final SeedResult emptyResult = new SeedResult("NONE", "", new ArrayList<>(), false);
		long startSeed = Random.Long(DungeonSeed.TOTAL_SEEDS);
		final long total = DungeonSeed.TOTAL_SEEDS;

		final java.util.concurrent.atomic.AtomicReference<SeedResult> resultRef =
				new java.util.concurrent.atomic.AtomicReference<>();

		Thread[] workers = new Thread[threadCount];
		for (int t = 0; t < threadCount; t++) {
			final int tid = t;

			// 把 total 均匀切成 threadCount 个连续大段，段与段之间不重叠、间隔很大
			long seg = total / threadCount;
			final long segStart = tid * seg;
			final long segEnd = (tid == threadCount - 1) ? total : (tid + 1) * seg;

			workers[t] = new Thread(() -> {
				for (long i = segStart; i < segEnd
						&& !parallelFound && findingStatus == FINDING.CONTINUE; i++) {

					if (Thread.currentThread().isInterrupted()) return;

					final long seedValue = (startSeed + i) % total;
					final String seedStr = Long.toString(seedValue);

					parallelSeeds.set(tid, seedValue);

					// 只写共享状态，UI 由 SeedFindLogScene.update() 统一轮询，杜绝投递洪峰
					synchronized (DUNGEON_LOCK) {
						if (parallelFound || findingStatus != FINDING.CONTINUE) return;
						matchedFloorInfo.clear();
						if (testSeedALL(seedStr, floors)) {
							parallelFound = true;
							String logText = logSeedItems(seedStr, floors, SPDSettings.challenges());
							Map<Integer, List<String>> floorItems = collectFloorItems(seedStr, floors);
							SeedResult r = new SeedResult(logText, seedStr, new ArrayList<>(matchedFloorInfo), true);
							r.floorItems = floorItems;
							resultRef.set(r);
							return;
						}
					}
				}
			});
			workers[t].setName("SeedFinder-Worker-" + tid);
			workers[t].setDaemon(true);
		}

		for (Thread w : workers) w.start();
		try {
			for (Thread w : workers) w.join();
		} catch (InterruptedException e) {
			findingStatus = FINDING.STOP;
			for (Thread w : workers) w.interrupt();
			running = false;
			return emptyResult;
		}

		running = false;
		findingStatus = FINDING.STOP;
		SeedResult r = resultRef.get();
		return r != null ? r : emptyResult;
	}


	/** 为对比用，结构化收集每层物品名（不含黑名单） */
	public Map<Integer, List<String>> collectFloorItems(String seed, int floors) {
		Map<Integer, List<String>> result = new HashMap<>();
		try {
			Dungeon.isDLC(Conducts.Conduct.SEED);
			SPDSettings.customSeed(seed);
			Dungeon.initSeed();
			GamesInProgress.selectedClass = HeroClass.WARRIOR;
			Dungeon.init();

			if (blacklist == null) {
				blacklist = Arrays.asList(
						Gold.class, Dewdrop.class, IronKey.class, GoldenKey.class, CrystalKey.class, EnergyCrystal.class,
						CorpseDust.class, Embers.class, CeremonialCandle.class, Pickaxe.class);
			}

			for (int i = 0; i < floors; i++) {
				int originalBranch = Dungeon.branch;
				Dungeon.branch = 0;
				Level l = Dungeon.newLevel();
				if (l == null || l instanceof DeadEndLevel) {
					Dungeon.branch = originalBranch;
					Dungeon.depth++;
					continue;
				}
				List<String> items = new ArrayList<>();
				ArrayList<Heap> heaps = new ArrayList<>(l.heaps.valueList());
				heaps.addAll(getMobDrops(l));
				for (Heap h : heaps) {
					for (Item item : h.items) {
						item.identify();
						if (blacklist.contains(item.getClass())) continue;
						items.add(item.title().toLowerCase());
					}
				}
				result.put(Dungeon.depth, items);
				Dungeon.branch = originalBranch;
				Dungeon.depth++;
			}
		} finally {
			Dungeon.depth = 0;
			Dungeon.branch = 0;
		}
		return result;
	}


}