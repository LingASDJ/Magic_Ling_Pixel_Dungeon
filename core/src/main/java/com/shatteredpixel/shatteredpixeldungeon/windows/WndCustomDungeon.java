package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.GameAPI;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.ui.Component;

public class WndCustomDungeon extends Window {

    private static final int WIDTH = 120;
    private static final int HEIGHT = 160;
    private static final int GAP = 2;
    private static final int ITEM_HEIGHT = 18;
    private static final int BTN_HEIGHT = 18;

    private GameAPI api;
    private Component content;
    private ScrollPane list;

    // 基础设置
    private CheckBox weaponCursed;
    private CheckBox armorCursed;
    private CheckBox noWater;
    private CheckBox noGrass;
    private CheckBox noChasm;

    // 物品禁用按钮
    private RedButton bannedItemsBtn;
    private RedButton bannedWeaponEnchantsBtn;
    private RedButton bannedArmorEnchantsBtn;
    private RedButton bannedMobsBtn;
    private RedButton bannedNpcsBtn;

    // 地牢氛围选择
    private RedButton feelingBtn;

    // 保存和重置按钮
    private RedButton saveButton;
    private RedButton resetButton;

    public WndCustomDungeon(GameAPI api) {
        super();
        this.api = api;

        resize(WIDTH, HEIGHT);

        content = new Component();
        list = new ScrollPane(content);

        add(list);

        createUI();
        layoutComponents();
        loadSettings();
    }

    private void createUI() {
        float pos = 0;

        // 标题
        RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
        title.hardlight(TITLE_COLOR);
        title.maxWidth(WIDTH);
        title.setPos(0, pos);
        content.add(title);
        pos += title.height() + GAP;

        // 基础设置标题
        RenderedTextBlock basicTitle = PixelScene.renderTextBlock(Messages.get(this, "basic_settings"), 6);
        basicTitle.hardlight(TITLE_COLOR);
        basicTitle.maxWidth(WIDTH);
        basicTitle.setPos(0, pos);
        content.add(basicTitle);
        pos += basicTitle.height() + GAP;

        // 武器诅咒
        weaponCursed = new CheckBox(Messages.get(this, "weapon_cursed")) {
            @Override
            protected void onClick() {
                super.onClick();
                api.setShouldWeaponCursed(checked());
            }
        };
        weaponCursed.setRect(0, pos, WIDTH, ITEM_HEIGHT);
        content.add(weaponCursed);
        pos += ITEM_HEIGHT + GAP;

        // 护甲诅咒
        armorCursed = new CheckBox(Messages.get(this, "armor_cursed")) {
            @Override
            protected void onClick() {
                super.onClick();
                //TODO
                // api.setShouldArmorCursed(checked());
            }
        };
        armorCursed.setRect(0, pos, WIDTH, ITEM_HEIGHT);
        content.add(armorCursed);
        pos += ITEM_HEIGHT + GAP;

        // 地形设置标题
        RenderedTextBlock terrainTitle = PixelScene.renderTextBlock(Messages.get(this, "terrain_settings"), 6);
        terrainTitle.hardlight(TITLE_COLOR);
        terrainTitle.maxWidth(WIDTH);
        terrainTitle.setPos(0, pos);
        content.add(terrainTitle);
        pos += terrainTitle.height() + GAP;

        // 禁用水域
        noWater = new CheckBox(Messages.get(this, "no_water")) {
            @Override
            protected void onClick() {
                super.onClick();
                api.setCustomLevelNoWater(checked());
            }
        };
        noWater.setRect(0, pos, WIDTH, ITEM_HEIGHT);
        content.add(noWater);
        pos += ITEM_HEIGHT + GAP;

        // 禁用草地
        noGrass = new CheckBox(Messages.get(this, "no_grass")) {
            @Override
            protected void onClick() {
                super.onClick();
                api.setCustomLevelNoGrass(checked());
            }
        };
        noGrass.setRect(0, pos, WIDTH, ITEM_HEIGHT);
        content.add(noGrass);
        pos += ITEM_HEIGHT + GAP;

        // 禁用深渊
        noChasm = new CheckBox(Messages.get(this, "no_chasm")) {
            @Override
            protected void onClick() {
                super.onClick();
                api.setCustomLevelNoChasm(checked());
            }
        };
        noChasm.setRect(0, pos, WIDTH, ITEM_HEIGHT);
        content.add(noChasm);
        pos += ITEM_HEIGHT + GAP;

        // 禁用列表标题
        RenderedTextBlock bannedTitle = PixelScene.renderTextBlock(Messages.get(this, "banned_lists"), 6);
        bannedTitle.hardlight(TITLE_COLOR);
        bannedTitle.maxWidth(WIDTH);
        bannedTitle.setPos(0, pos);
        content.add(bannedTitle);
        pos += bannedTitle.height() + GAP;

        // 禁用物品
        bannedItemsBtn = new RedButton(Messages.get(this, "banned_items")) {
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.scene().add(new WndBag(Dungeon.hero.belongings.backpack,
                        new WndBag.ItemSelector() {
                            @Override
                            public String textPrompt() {
                                return Messages.get(WndCustomDungeon.this, "select_items");
                            }
                            @Override
                            public boolean itemSelectable(Item item) {
                                return true;
                            }
                            @Override
                            public void onSelect(Item item) {
                                if (item != null) {
                                    api.getBanedItemList().add(item.getClass());
                                    updateButtonLabels();
                                }
                            }
                        }));
            }
        };
        bannedItemsBtn.setRect(0, pos, WIDTH, BTN_HEIGHT);
        content.add(bannedItemsBtn);
        pos += BTN_HEIGHT + GAP;

        // 禁用武器魔咒
        bannedWeaponEnchantsBtn = new RedButton(Messages.get(this, "banned_weapon_enchants")) {
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.scene().add(new WndSelectEnchants(api, true));
            }
        };
        bannedWeaponEnchantsBtn.setRect(0, pos, WIDTH, BTN_HEIGHT);
        content.add(bannedWeaponEnchantsBtn);
        pos += BTN_HEIGHT + GAP;

        // 禁用护甲魔咒
        bannedArmorEnchantsBtn = new RedButton(Messages.get(this, "banned_armor_enchants")) {
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.scene().add(new WndSelectEnchants(api, false));
            }
        };
        bannedArmorEnchantsBtn.setRect(0, pos, WIDTH, BTN_HEIGHT);
        content.add(bannedArmorEnchantsBtn);
        pos += BTN_HEIGHT + GAP;

        // 禁用怪物
        bannedMobsBtn = new RedButton(Messages.get(this, "banned_mobs")) {
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.scene().add(new WndSelectMobs(api));
            }
        };
        bannedMobsBtn.setRect(0, pos, WIDTH, BTN_HEIGHT);
        content.add(bannedMobsBtn);
        pos += BTN_HEIGHT + GAP;

        // 禁用NPC
        bannedNpcsBtn = new RedButton(Messages.get(this, "banned_npcs")) {
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.scene().add(new WndSelectNPCs(api));
            }
        };
        bannedNpcsBtn.setRect(0, pos, WIDTH, BTN_HEIGHT);
        content.add(bannedNpcsBtn);
        pos += BTN_HEIGHT + GAP;

        // 地牢氛围
        feelingBtn = new RedButton(Messages.get(this, "level_feeling")) {
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.scene().add(new WndSelectFeeling(api));
            }
        };
        feelingBtn.setRect(0, pos, WIDTH, BTN_HEIGHT);
        content.add(feelingBtn);
        pos += BTN_HEIGHT + GAP * 2;

        // 保存和重置按钮
        saveButton = new RedButton(Messages.get(this, "save")) {
            @Override
            protected void onClick() {
                saveSettings();
                hide();
            }
        };
        saveButton.setRect(0, pos, WIDTH/2 - GAP/2, BTN_HEIGHT);
        content.add(saveButton);

        resetButton = new RedButton(Messages.get(this, "reset")) {
            @Override
            protected void onClick() {
                resetSettings();
            }
        };
        resetButton.setRect(WIDTH/2 + GAP/2, pos, WIDTH/2 - GAP/2, BTN_HEIGHT);
        content.add(resetButton);

        content.setSize(WIDTH, pos + BTN_HEIGHT);
    }

    private void layoutComponents() {
        list.setRect(0, 0, WIDTH, HEIGHT);
    }

    private void loadSettings() {
        weaponCursed.checked(api.getShouldWeaponCursed());
        armorCursed.checked(api.getShouldArmorCursed());
        noWater.checked(api.getCustomLevelNoWater());
        noGrass.checked(api.getCustomLevelNoGrass());
        noChasm.checked(api.getCustomLevelNoChasm());
        updateButtonLabels();
    }

    private void saveSettings() {
        // 设置已经通过回调保存
    }

    private void resetSettings() {
        weaponCursed.checked(false);
        armorCursed.checked(false);
        noWater.checked(false);
        noGrass.checked(false);
        noChasm.checked(false);

        api.setShouldWeaponCursed(false);

        api.setCustomLevelNoWater(false);
        api.setCustomLevelNoGrass(false);
        api.setCustomLevelNoChasm(false);

        api.getBanedItemList().clear();
        api.getBannedWeaponEnchantList().clear();
        api.getBannedArmorEnchantList().clear();
        api.getBannedMobList().clear();
        api.getBannedTrapList().clear();
        api.getBannedNpcList().clear();

        updateButtonLabels();
    }

    private void updateButtonLabels() {
        String count = String.format(" (%d)", api.getBanedItemList().size());
        bannedItemsBtn.text(Messages.get(this, "banned_items") + count);

        count = String.format(" (%d)", api.getBannedWeaponEnchantList().size());
        bannedWeaponEnchantsBtn.text(Messages.get(this, "banned_weapon_enchants") + count);

        count = String.format(" (%d)", api.getBannedArmorEnchantList().size());
        bannedArmorEnchantsBtn.text(Messages.get(this, "banned_armor_enchants") + count);

        count = String.format(" (%d)", api.getBannedMobList().size());
        bannedMobsBtn.text(Messages.get(this, "banned_mobs") + count);

        count = String.format(" (%d)", api.getBannedNpcList().size());
        bannedNpcsBtn.text(Messages.get(this, "banned_npcs") + count);
    }
}
