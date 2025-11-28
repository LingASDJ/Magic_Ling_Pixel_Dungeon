package com.shatteredpixel.shatteredpixeldungeon.windows;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation.changeSeed;
import static com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation.changeWand;
import static com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation.changeWeapon;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invulnerability;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Transmuting;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CapeOfThorns;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EtherealChains;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SandalsOfNature;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.WraithAmulet;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.dlcitem.RushMobScrollOfRandom;
import com.shatteredpixel.shatteredpixeldungeon.items.lightblack.OilLantern;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.Brew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.Elixir;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfFlameCursed;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfGolems;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfRoseShiled;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.extra.ScrollOfTeleTation;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.Trinket;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LockSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WndGoldBurrety extends Window {

    private final int WIDTH = 140;

    private final int BTN_SIZE = 25;
    private final float GAP = 2;
    private final float BTN_GAP = 5;

    private ItemButton btnPressed;

    private ItemButton btnItem1;
    private ItemButton btnItem2;
    private ItemButton btnItem3;
    private ItemButton btnItem5;
    private ItemButton btnItem6;
    private ItemButton btnItem7;
    private ItemButton btnItem8;
    private ItemButton btnItem9;
    private ItemButton btnItemGold;

    private RedButton btnReforge;

    // 统一管理物品槽按钮的数组，避免重复定义
    private final ItemButton[] itemButtons = {
            btnItem1, btnItem2, btnItem3,
            btnItem5, btnItem6, btnItem7,
            btnItem8, btnItem9
    };

    public WndGoldBurrety(Mob troll, Window wndParent) {
        super();

        IconTitle titlebar = new IconTitle();
        titlebar.icon(troll.sprite());
        titlebar.label(Messages.titleCase(troll.name()));
        titlebar.setRect(0, 0, WIDTH, 0);
        add(titlebar);

        RenderedTextBlock message = PixelScene.renderTextBlock(Messages.get(this, "message"), 6);
        message.maxWidth(WIDTH);
        message.setPos(0, titlebar.bottom() + GAP);
        add(message);

        // 初始化物品槽按钮
        initItemButtons(message);

        btnReforge = new RedButton(Messages.get(this, "reforge")) {
            @Override
            protected void onClick() {
                // 使用HashMap存储物品按钮与物品的映射
                Map<ItemButton, Item> buttonToItemMap = new HashMap<>();
                for (ItemButton button : itemButtons) {
                    Item item = button.item();
                    if (item != null) {
                        buttonToItemMap.put(button, item);
                    }
                }

                // 获取所有不为null的物品
                List<Item> nonNullItems = new ArrayList<>(buttonToItemMap.values());

                // 进行嬗变处理
                Item[] newItems = GoldBuretteMode(nonNullItems.toArray(new Item[0]));

                // 计算回合数，与嬗变物品的数量相关
                int turns = newItems.length;

                // 施加Invulnerability Buff
                Buff.affect(hero, Invulnerability.class, turns - 1);

                // 更新物品按钮
                Iterator<Map.Entry<ItemButton, Item>> iterator = buttonToItemMap.entrySet().iterator();
                int index = 0;
                while (iterator.hasNext() && index < newItems.length) {
                    Map.Entry<ItemButton, Item> entry = iterator.next();
                    ItemButton button = entry.getKey();
                    button.item(newItems[index]);
                    index++;
                }

                // 显示转换效果
                Transmuting.show(Dungeon.hero, new RushMobScrollOfRandom(), new RushMobScrollOfRandom());
                Dungeon.hero.sprite.emitter().start(Speck.factory(Speck.CHANGE), 0.2f, 10);
                GLog.p(Messages.get(RushMobScrollOfRandom.class, "recycled"));

                Statistics.goldRefogreCount--;

                hide();
                if (wndParent != null) {
                    wndParent.hide();
                }
            }
        };
        btnReforge.enable(false);
        btnReforge.setRect(0, btnItem9.bottom() + BTN_GAP, WIDTH, 20);
        add(btnReforge);

        resize(WIDTH, (int) btnReforge.bottom());
    }

    // 初始化物品槽按钮的位置和点击事件
    private void initItemButtons(RenderedTextBlock message) {
        btnItem1 = createItemButton((WIDTH - BTN_GAP) / 3 - BTN_SIZE, message.top() + message.height() + BTN_GAP);
        btnItem2 = createItemButton(btnItem1.right() + BTN_GAP, btnItem1.top());
        btnItem3 = createItemButton(btnItem2.right() + BTN_GAP, btnItem1.top());

        btnItem5 = createItemButton((WIDTH - BTN_GAP) / 3 - BTN_SIZE, btnItem1.bottom());
        btnItem6 = createItemButton(btnItem2.right() + BTN_GAP, btnItem1.bottom());

        btnItemGold = new ItemButton() {
            @Override
            public Chrome.Type getType() {
                return Chrome.Type.GREY_BUTTON_TR;
            }
            @Override
            protected void onClick() {
                btnPressed = btnItemGold;
                GameScene.selectItem(GolditemSelector);
            }
        };
        btnItemGold.setRect(btnItem1.right() + BTN_GAP, btnItem1.bottom(), BTN_SIZE, BTN_SIZE);
        add(btnItemGold);

        btnItem8 = createItemButton((WIDTH - BTN_GAP) / 3 - BTN_SIZE, btnItem5.bottom());
        btnItem9 = createItemButton(btnItem5.right() + BTN_GAP, btnItem5.bottom());
        btnItem7 = createItemButton(btnItemGold.right() + BTN_GAP, btnItem5.bottom());

        // 更新物品按钮数组的引用
        itemButtons[0] = btnItem1;
        itemButtons[1] = btnItem2;
        itemButtons[2] = btnItem3;
        itemButtons[3] = btnItem5;
        itemButtons[4] = btnItem6;
        itemButtons[5] = btnItem7;
        itemButtons[6] = btnItem8;
        itemButtons[7] = btnItem9;
    }

    // 创建物品按钮的工具方法，减少重复代码
    private ItemButton createItemButton(float x, float y) {
        ItemButton button = new ItemButton() {
            @Override
            protected void onClick() {
                btnPressed = this;
                GameScene.selectItem(itemSelector);
            }
        };
        button.setRect(x, y, BTN_SIZE, BTN_SIZE);
        add(button);
        return button;
    }

    /**
     * 金蝶模式2.0逻辑
     * @param items 传入的物品
     * @return 返回传出的物品
     */
    private Item[] GoldBuretteMode(Item[] items) {
        Item[] results = new Item[items.length];

        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            Item result = item;

            if (item instanceof MeleeWeapon && !(item instanceof MagesStaff)) {

                if (item == hero.belongings.weapon()) {
                    hero.belongings.weapon = changeWeapon((Weapon) hero.belongings.weapon);
                    Dungeon.hero.belongings.weapon.detachAll(Dungeon.hero.belongings.backpack);
                    hero.belongings.weapon.identify();

                    // 修复升级条件：只要还有升级次数就升级
                    if (Statistics.upgradeGold > 0) {
                        hero.belongings.weapon.upgrade();
                        hero.belongings.weapon.noUpgrade = true;
                        Statistics.upgradeGold--; // 移动到这里确保只减一次
                    }
                } else {
                    result = changeWeapon((Weapon) item);
                    result.noUpgrade = true;

                    // 修复升级条件
                    if (Statistics.upgradeGold > 0) {
                        result.upgrade();
                        Statistics.upgradeGold--; // 移动到这里确保只减一次
                    }
                }

                result.collect();
                item.detach(Dungeon.hero.belongings.backpack);
            } else if (item instanceof MissileWeapon) {
                result = changeWeapon((MissileWeapon) item);
                result.noUpgrade = true;
                result.upgrade();
                result.collect();
                result.quantity(item.quantity);
                item.detachAll(Dungeon.hero.belongings.backpack);
            } else if (item instanceof MagesStaff && hero.belongings.weapon() == item) {
                if (Statistics.magestaffUpgrade == 0) {
                    Statistics.magestaffUpgrade++;
                    result.noUpgrade = true;
                    result.upgrade();
                    changeStaff((MagesStaff) item);
                    ((MagesStaff) result).activate(hero);
                    item.detachAll(Dungeon.hero.belongings.backpack);
                }
                Dungeon.quickslot.setSlot(0, result);
            } else if (item instanceof MagesStaff) {
                changeStaff((MagesStaff) item);
                if (Statistics.magestaffUpgrade == 0) {
                    Statistics.magestaffUpgrade++;
                    item.noUpgrade = true;
                    item.upgrade();
                }
            } else if (item instanceof Scroll && !(item instanceof ScrollOfFlameCursed ||
                    item instanceof ScrollOfRoseShiled || item instanceof ScrollOfGolems)) {
                result = changeScroll((Scroll) item);
                result.collect();
                result.quantity(item.quantity);
                item.detachAll(Dungeon.hero.belongings.backpack);
            } else if (item instanceof Potion) {
                result = changePotion((Potion) item);
                result.collect();
                result.quantity(item.quantity);
                item.detachAll(Dungeon.hero.belongings.backpack);
            } else if (item instanceof Wand) {
                result = changeWand((Wand) item);

                // 修复升级条件
                if (Statistics.upgradeGold > 0) {
                    result.upgrade();
                    result.noUpgrade = true;
                    ((Wand) result).updateLevel();
                    Statistics.upgradeGold--;
                }

                result.collect();
                item.detach(Dungeon.hero.belongings.backpack);
            } else if (item instanceof Plant.Seed) {
                result = changeSeed((Plant.Seed) item);
                item.detach(Dungeon.hero.belongings.backpack);
            } else if (item instanceof Trinket) {
                result = processTrinket(item);
            } else if (item instanceof Runestone) {
                result = changeStone((Runestone) item);
                result.collect();
                item.detach(Dungeon.hero.belongings.backpack);
            } else if (item instanceof Artifact) {
                if (item == hero.belongings.artifact()) {
                    hero.belongings.artifact = (Artifact) processArtifact(hero.belongings.artifact);
                    hero.belongings.artifact.detachAll(Dungeon.hero.belongings.backpack);
                } else {
                    result = processArtifact(item);
                }
            } else if (item instanceof Ring) {
                if (item == hero.belongings.ring()) {
                    if (hero.belongings.ring.buff != null) {
                        hero.belongings.ring.buff.detach();
                    }
                    hero.belongings.ring = changeRing(hero.belongings.ring);
                    hero.belongings.ring.detach(Dungeon.hero.belongings.backpack);

                    // 修复升级条件
                    if (Statistics.upgradeGold > 0) {
                        hero.belongings.ring.upgrade();
                        Statistics.upgradeGold--;
                        hero.belongings.ring.noUpgrade = true;
                        hero.belongings.ring.activate(hero);
                    }
                } else {
                    result = changeRing((Ring) item);
                    // 修复升级条件
                    if (Statistics.upgradeGold > 0) {
                        result.upgrade();
                        Statistics.upgradeGold--;
                        result.noUpgrade = true;
                    }
                }
            }

            results[i] = result;
        }

        return results;
    }

    public static Ring changeRing(Ring r) {
        Ring n;
        do {
            n = (Ring) Generator.randomUsingDefaults(Generator.Category.RING);
        } while (Challenges.isItemBlocked(n) || n.getClass() == r.getClass());

        n.level(0);

        int level = r.level();
        if (level > 0) {
            n.upgrade(level);
        } else if (level < 0) {
            n.degrade(-level);
        }

        n.levelKnown = r.levelKnown;
        n.cursedKnown = r.cursedKnown;
        n.cursed = r.cursed;
        n.collect();

        r.detach(Dungeon.hero.belongings.backpack);

        return n;
    }

    public static Runestone changeStone(Runestone r) {
        Runestone n;

        do {
            n = (Runestone) Generator.randomUsingDefaults(Generator.Category.STONE);
        } while (n.getClass() == r.getClass());

        return n;
    }

    private Potion changePotion(Potion p) {
        if (p instanceof ExoticPotion) {
            return Reflection.newInstance(ExoticPotion.exoToReg.get(p.getClass()));
        } else {
            return Reflection.newInstance(ExoticPotion.regToExo.get(p.getClass()));
        }
    }

    private Scroll changeScroll(Scroll s) {
        if (s instanceof ExoticScroll) {
            return Reflection.newInstance(ExoticScroll.exoToReg.get(s.getClass()));
        } else {
            return Reflection.newInstance(ExoticScroll.regToExo.get(s.getClass()));
        }
    }

    public static MagesStaff changeStaff(MagesStaff staff) {
        Class<? extends Wand> wandClass = staff.wandClass();

        Wand n;
        do {
            n = (Wand) Generator.randomUsingDefaults(Generator.Category.WAND);
        } while (Challenges.isItemBlocked(n) || n.getClass() == wandClass);
        n.level(0);
        n.identify();
        staff.imbueWand(n, null);

        return staff;
    }

    private static final Set<Class<? extends Trinket>> generatedTrinkets = new HashSet<>();

    public static Trinket changeTrinket(Trinket t) {
        Trinket n;
        do {
            n = (Trinket)Generator.random(Generator.Category.TRINKET);
        } while ((Challenges.isItemBlocked(n) || n.getClass() == t.getClass()) && generatedTrinkets.contains(n.getClass()));

        generatedTrinkets.add(n.getClass());

        n.level(t.trueLevel());
        n.levelKnown = t.levelKnown;
        n.cursedKnown = t.cursedKnown;
        n.cursed = t.cursed;

        return n;
    }

    private Item processTrinket(Item item) {
        if (item.level() < 6) {
            Item result = changeTrinket((Trinket) item);
            if(Statistics.upgradeGold>0){
                result.upgrade();
                Statistics.upgradeGold--;
            }
            result.collect();
            item.detach(Dungeon.hero.belongings.backpack);
            return result;
        } else {
            Item result = changeTrinket((Trinket) item);
            result.collect();
            item.detach(Dungeon.hero.belongings.backpack);
            return result;
        }
    }

    private Artifact changeArtifact(Artifact a) {
        Artifact n = Normal();

        if (a instanceof DriedRose) {
            if (((DriedRose) a).ghostWeapon() != null) {
                Dungeon.level.drop(((DriedRose) a).ghostWeapon(), Dungeon.hero.pos);
            }
            if (((DriedRose) a).ghostArmor() != null) {
                Dungeon.level.drop(((DriedRose) a).ghostArmor(), Dungeon.hero.pos);
            }
        }
        n.cursedKnown = a.cursedKnown;
        n.cursed = a.cursed;
        n.levelKnown = a.levelKnown;
        n.collect();
        n.transferUpgrade(a.visiblyUpgraded());
        a.detach(Dungeon.hero.belongings.backpack);
        return n;
    }

    private Artifact Normal() {
        Artifact artifact;

        switch (Random.NormalIntRange(0, 10)) {
            case 0: artifact = new UnstableSpellbook(); break;
            case 2: artifact = new HornOfPlenty(); break;
            case 3: artifact = new SandalsOfNature(); break;
            case 4: artifact = new TalismanOfForesight(); break;
            case 5: artifact = new TimekeepersHourglass(); break;
            case 6: artifact = new AlchemistsToolkit(); break;
            case 7: artifact = new DriedRose(); break;
            case 8: artifact = new EtherealChains(); break;
            case 9: artifact = new WraithAmulet(); break;
            case 10: artifact = new CapeOfThorns(); break;
            default: artifact = new ChaliceOfBlood(); break;
        }
        return artifact;
    }

    private Item processArtifact(Item item) {
        return changeArtifact((Artifact) item);
    }

    // 统一的物品计数检查方法
    private boolean areAtLeastTwoItemsSelected() {
        int selectedItemCount = 0;
        for (ItemButton button : itemButtons) {
            if (button != null && button.item() != null) {
                selectedItemCount++;
            }
        }
        // 包含黄金槽位的物品计数
        if (btnItemGold.item() != null) {
            selectedItemCount++;
        }
        return selectedItemCount >= 2;
    }

    protected WndBag.ItemSelector GolditemSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return Messages.get(WndBlacksmith.WndReforge.class, "prompt");
        }

        @Override
        public Class<? extends Bag> preferredBag() {
            return Belongings.Backpack.class;
        }

        @Override
        public boolean itemSelectable(Item item) {
            return item instanceof RushMobScrollOfRandom && areAtLeastTwoItemsSelected();
        }

        @Override
        public void onSelect(Item item) {
            if (item != null && btnPressed.parent != null) {
                btnPressed.item(item);
            }
            btnReforge.enable(areAtLeastTwoItemsSelected() && (item == null || itemSelectable(item)));
        }
    };

    protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return Messages.get(WndBlacksmith.WndReforge.class, "prompt");
        }

        @Override
        public Class<? extends Bag> preferredBag() {
            return Belongings.Backpack.class;
        }

        @Override
        public boolean itemSelectable(Item item) {
            if (item.noUpgrade || item == hero.belongings.misc() || item instanceof ScrollOfTeleTation) {
                return false;
            }
            if (item instanceof MeleeWeapon) {
                if (item instanceof LockSword)
                    return false;

                Generator.Category c = Generator.wepTiers[((MeleeWeapon) item).tier - 1];
                int canChangeWeapon = 0;
                int lastWeaponIndex = 0;
                for (int i = 0; i < c.probs.length; i++) {
                    if (c.probs[i] > 0) {
                        canChangeWeapon++;
                        lastWeaponIndex = i;
                    }
                }
                if (canChangeWeapon > 1)
                    return true;
                else if (canChangeWeapon == 1) {
                    return !item.getClass().getSimpleName().equals(c.classes[lastWeaponIndex].getSimpleName());
                } else {
                    return false;
                }
            }
            return item instanceof MissileWeapon && (!(item instanceof Dart) || item instanceof TippedDart) ||
                    item instanceof Potion && !(item instanceof Elixir || item instanceof Brew) ||
                    item instanceof Scroll && (!(item instanceof ScrollOfTransmutation) || item.quantity() > 1) ||
                    item instanceof Ring || item instanceof Trinket || item instanceof Wand ||
                    item instanceof Plant.Seed || item instanceof Runestone ||
                    item instanceof Artifact && !(item instanceof OilLantern);
        }

        @Override
        public void onSelect(Item item) {
            if (item != null && btnPressed.parent != null) {
                if (!isItemAlreadyInSlots(item)) {
                    btnPressed.item(item);
                } else {
                    clearItemFromSlots(item);
                    btnPressed.item(item);
                }
            }
            btnReforge.enable(areAtLeastTwoItemsSelected() && (item == null || GolditemSelector.itemSelectable(item)));
        }

        private boolean isItemAlreadyInSlots(Item item) {
            for (ItemButton button : itemButtons) {
                if (button != null && button.item() != null && button.item().equals(item)) {
                    return true;
                }
            }
            return btnItemGold.item() != null && btnItemGold.item().equals(item);
        }
    };

    private void clearItemFromSlots(Item item) {
        for (ItemButton button : itemButtons) {
            if (button != null && button.item() != null && button.item().equals(item)) {
                button.clear();
            }
        }
        if (btnItemGold.item() != null && btnItemGold.item().equals(item)) {
            btnItemGold.clear();
        }
    }
}
