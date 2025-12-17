package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.custom.utils.GameAPI;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class WndSelectEnchants extends Window {

    private static final int WIDTH = 120;
    private static final int HEIGHT = 160;
    private static final int GAP = 2;
    private static final int ITEM_HEIGHT = 18;
    private static final int BTN_HEIGHT = 18;

    private GameAPI api;
    private boolean isWeapon;
    private Component content;
    private ScrollPane list;
    private ArrayList<CheckBox> checkBoxes = new ArrayList<>();

    public WndSelectEnchants(GameAPI api, boolean isWeapon) {
        super();
        this.api = api;
        this.isWeapon = isWeapon;

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
        RenderedTextBlock title = PixelScene.renderTextBlock(
                Messages.get(this, isWeapon ? "weapon_title" : "armor_title"), 9);
        title.hardlight(TITLE_COLOR);
        title.maxWidth(WIDTH);
        title.setPos(0, pos);
        content.add(title);
        pos += title.height() + GAP;

        // 获取所有可用的魔咒类
        ArrayList<Class<?>> enchantClasses = new ArrayList<>();

        if (isWeapon) {
            // 添加武器魔咒
            enchantClasses.add(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blazing.class);
            enchantClasses.add(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blocking.class);
            enchantClasses.add(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Chilling.class);
            enchantClasses.add(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Kinetic.class);
            enchantClasses.add(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking.class);
            enchantClasses.add(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Vampiric.class);

            // 添加诅咒武器魔咒
            enchantClasses.add(com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Elastic.class);
        } else {
            // 添加护甲魔咒
            enchantClasses.add(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic.class);
            enchantClasses.add(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Brimstone.class);
            enchantClasses.add(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Camouflage.class);
            enchantClasses.add(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Flow.class);
            enchantClasses.add(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Potential.class);
            enchantClasses.add(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Stone.class);
            enchantClasses.add(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Swiftness.class);
            enchantClasses.add(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Thorns.class);
            enchantClasses.add(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity.class);

            // 添加诅咒护甲魔咒
            enchantClasses.add(com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Entanglement.class);
        }

        // 获取当前禁用的列表
        ArrayList<Class<?>> bannedList = isWeapon ?
                api.getBannedWeaponEnchantList() : api.getBannedArmorEnchantList();

        // 为每个魔咒创建复选框
        for (Class<?> enchantClass : enchantClasses) {
            final Class<?> finalClass = enchantClass;
            CheckBox cb = new CheckBox(Messages.titleCase(Messages.get(enchantClass, "name"))) {
                @Override
                protected void onClick() {
                    super.onClick();
                    if (checked()) {
                        if (!bannedList.contains(finalClass)) {
                            bannedList.add(finalClass);
                        }
                    } else {
                        bannedList.remove(finalClass);
                    }
                }
            };
            cb.checked(bannedList.contains(enchantClass));
            cb.setRect(0, pos, WIDTH, ITEM_HEIGHT);
            content.add(cb);
            checkBoxes.add(cb);
            pos += ITEM_HEIGHT + GAP;
        }

        // 保存按钮
        RedButton saveBtn = new RedButton(Messages.get(this, "save")) {
            @Override
            protected void onClick() {
                hide();
            }
        };
        saveBtn.setRect(0, pos, WIDTH, BTN_HEIGHT);
        content.add(saveBtn);
        pos += BTN_HEIGHT + GAP;

        content.setSize(WIDTH, pos);
        list.content();
    }

    private void layoutComponents() {
        list.setRect(0, 0, WIDTH, HEIGHT);
    }

    private void loadSettings() {
        // 设置已经在创建复选框时加载
    }
}
