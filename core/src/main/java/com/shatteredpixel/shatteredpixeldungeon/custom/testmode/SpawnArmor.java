package com.shatteredpixel.shatteredpixeldungeon.custom.testmode;


import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LamellarArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.MailArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ScaleArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.AntiEntropy;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.Bulk;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.Displacement;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.Metabolism;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.Multiplicity;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.Overgrowth;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.Stench;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.custom.AncityArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Affection;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Brimstone;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Camouflage;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Entanglement;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Flow;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Obfuscation;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Potential;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Repulsion;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Stone;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Swiftness;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Thorns;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.OptionSlider;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.WndTextNumberInput;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndError;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Objects;

public class SpawnArmor extends TestItem {
    {
        image = ItemSpriteSheet.ARMOR_HOLDER;
        defaultAction = AC_SPAWN;
    }

    private static final String AC_SPAWN = "spawn";
    private int armor_id;
    private boolean cursed;
    private int armor_level;
    private int enchant_id;
    private int enchant_rarity;

    public SpawnArmor(){
        this.armor_id = 1;
        this.cursed =false;
        this.armor_level = 0;
        this.enchant_id = 0;
        this.enchant_rarity = 0;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_SPAWN);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_SPAWN)) {
            GameScene.show(new ArmorSetting());
        }
    }

    private Armor getArmor(int armor_id){
        switch (armor_id) {
            case 1:
                return new LeatherArmor();
            case 2:
                return new MailArmor();
            case 3:
                return new ScaleArmor();
            case 4:
                return new PlateArmor();
            case 5:
                return new LamellarArmor();
            case 6:
                return new AncityArmor();
            default:
                return new ClothArmor();
        }
    }

    private void createArmor() {
        Armor armor = getArmor(armor_id);
        if (Challenges.isItemBlocked(armor)) {
            return;
        }
        armor = modifyArmor(armor);
        armor.identify();
        if (armor.collect()) {
            GameScene.pickUp( armor, hero.pos );
            Sample.INSTANCE.play( Assets.Sounds.ITEM );
            GLog.i(Messages.get(hero, "you_now_have", armor.name()));
        } else {
            armor.doDrop(curUser);
        }
    }

    private Armor modifyArmor(Armor armor) {
        if (armor_level >= 0)
            armor.level(armor_level);
        armor.cursed = cursed;
        if (generateEnchant(enchant_rarity, enchant_id) == null) {
            armor.inscribe(null);
        } else {
            armor.inscribe(Reflection.newInstance(generateEnchant(enchant_rarity, enchant_id)));
        }
        return armor;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("armor_id", armor_id);
        bundle.put("cursed", cursed);
        bundle.put("armor_level", armor_level);
        bundle.put("enchant_rarity", enchant_rarity);
        bundle.put("enchant_id", enchant_id);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        armor_id = bundle.getInt("armor_id");
        cursed = bundle.getBoolean("cursed");
        armor_level = bundle.getInt("armor_level");
        enchant_rarity = bundle.getInt("enchant_rarity");
        enchant_id = bundle.getInt("enchant_id");
    }

    private String currentGlyphName(Class<? extends Armor.Glyph> glyph) {
        if (enchant_rarity < 4)
            return currentGlyphName(glyph, Messages.get(Armor.Glyph.class, "glyph"));
        else
            return currentGlyphName(glyph, Messages.get(Item.class, "curse"));
    }

    private String currentGlyphName(Class<? extends Armor.Glyph> glyph, String armorName) {
        return Messages.get(glyph, "name", armorName);
    }

    private Class<? extends Armor.Glyph> generateEnchant(int enc_type, int enc_id) {
        if (enc_type == 1) switch (enc_id) {
            case 0:
                return Obfuscation.class;
            case 1:
                return Swiftness.class;
            case 2:
                return Viscosity.class;
            case 3:
                return Potential.class;
            default:
                return null;
        }
        else if (enc_type == 2) switch (enc_id) {
            case 0:
                return Stone.class;
            case 1:
                return Brimstone.class;
            case 2:
                return Entanglement.class;
            case 3:
                return Repulsion.class;
            case 4:
                return Camouflage.class;
            case 5:
                return Flow.class;
            default:
                return null;
        }
        else if (enc_type == 3) switch (enc_id) {
            case 0:
                return AntiMagic.class;
            case 1:
                return Thorns.class;
            case 2:
                return Affection.class;
            default:
                return null;
        }
        else if (enc_type == 4) switch (enc_id) {
            case 0:
                return AntiEntropy.class;
            case 1:
                return Bulk.class;
            case 2:
                return Corrosion.class;
            case 3:
                return Displacement.class;
            case 4:
                return Metabolism.class;
            case 5:
                return Multiplicity.class;
            case 6:
                return Overgrowth.class;
            case 7:
                return Stench.class;
        }

        return null;
    }

    /**
     * 护甲设置窗口类，继承自Window类。
     */
    private class ArmorSetting extends Window {

        // 定义常量
        private static final int WIDTH = 150; // 窗口宽度
        private static final int HEIGHT = 220; // 窗口高度
        private static final int GAP = 2; // 间隔大小
        private static final int BTN_SIZE = 18; // 按钮尺寸
        private static final int MAX_ICONS_PER_LINE = 4; // 每行最大图标数量

        // 成员变量
        private Class[] AllArmor; // 所有护甲的Class数组
        private CheckBox CheckBox_curse; // 诅咒物品复选框
        private RenderedTextBlock RenderedTextBlock_enchantInfo; // 附魔信息文本块
        private OptionSlider OptionSlider_enchantId; // 附魔编号选项滑块
        private OptionSlider OptionSlider_enchantRarity; // 附魔种类选项滑块
        private RedButton RedButton_create; // 创建武器按钮
        private final ArrayList<IconButton> IconButtons = new ArrayList<>(); // 图标按钮列表
        private final RedButton Button_Level; // 武器等级按钮

        /**
         * 构造函数，用于初始化窗口。
         */
        public ArmorSetting() {
            super();

            // 设置窗口尺寸
            resize(WIDTH, HEIGHT);

            // 创建护甲列表及图标
            createArmorList();
            createArmorImage(AllArmor);

            // 创建护甲等级按钮
            Button_Level = new RedButton(Messages.get(this, "select_armor")) {
                @Override
                protected void onClick() {
                    if(!Button_Level.text().equals(Messages.get(SpawnArmor.ArmorSetting.class, "select_armor"))){ // 修改此行代码
                        Game.runOnRenderThread(() ->GameScene.show(new WndTextNumberInput(
                                Messages.get(SpawnArmor.ArmorSetting.class, "armor_level"), Messages.get(SpawnArmor.ArmorSetting.class, "armor_level_desc"),
                                Integer.toString(armor_level),
                                4, false, Messages.get(SpawnArmor.ArmorSetting.class, "confirm"),
                                Messages.get(SpawnArmor.ArmorSetting.class, "cancel"),false) {
                            @Override
                            public void onSelect(boolean check, String text) {

                                if (check && text.matches("\\d+")) {
                                    int level = Integer.parseInt(text);
                                    armor_level = Math.min(level, 6666);
                                }
                            }
                        }));
                    } else {
                        Game.scene().add( new WndError( Messages.get(SpawnArmor.ArmorSetting.class, "armor_level_error") ) );
                    }
                }
            };
            Button_Level.text(((Armor) Reflection.newInstance(getArmor(armor_id).getClass())).name());
            add(Button_Level);

            // 创建附魔信息文本块
            RenderedTextBlock_enchantInfo = PixelScene.renderTextBlock("", 6);
            RenderedTextBlock_enchantInfo.visible = true;
            RenderedTextBlock_enchantInfo.maxWidth(WIDTH);
            updateEnchantText();
            add(RenderedTextBlock_enchantInfo);

            // 创建附魔种类选项滑块
            OptionSlider_enchantRarity = new OptionSlider(Messages.get(this, "enchant_rarity"), "0", "4", 0, 4) {
                @Override
                protected void onChange() {
                    enchant_rarity = getSelectedValue();
                    updateEnchantText();
                }
            };
            OptionSlider_enchantRarity.setSelectedValue(enchant_rarity);
            add(OptionSlider_enchantRarity);

            // 创建附魔编号选项滑块
            OptionSlider_enchantId = new OptionSlider(Messages.get(this, "enchant_id"), "0", "7", 0, 7) {
                @Override
                protected void onChange() {
                    enchant_id = getSelectedValue();
                    updateEnchantText();
                }
            };
            OptionSlider_enchantId.setSelectedValue(enchant_id);
            add(OptionSlider_enchantId);

            // 创建诅咒物品复选框
            CheckBox_curse = new CheckBox(Messages.get(this, "cursed")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    cursed = checked();
                }
            };
            CheckBox_curse.checked(cursed);
            add(CheckBox_curse);

            // 创建生成护甲按钮
            RedButton_create = new RedButton(Messages.get(this, "create")) {
                @Override
                protected void onClick() {
                    createArmor();
                }
            };
            add(RedButton_create);

            layout();
        }

        /**
         * 封装一个同步UI的方法，用于调整UI组件的位置和大小。
         */
        private void layout() {

            //
            int numLines = (int) Math.ceil(AllArmor.length / (float) MAX_ICONS_PER_LINE);
            float totalHeight = 2;
            if (numLines > 0) {
                totalHeight += numLines * (BTN_SIZE + GAP);
            }

            Button_Level.setRect(0, totalHeight , WIDTH, 24);
            RenderedTextBlock_enchantInfo.setPos(0, GAP + Button_Level.top() + Button_Level.height());
            OptionSlider_enchantRarity.setRect(0, GAP + RenderedTextBlock_enchantInfo.bottom(), WIDTH, 24);
            OptionSlider_enchantId.setRect(0, GAP + OptionSlider_enchantRarity.top() + OptionSlider_enchantRarity.height(), WIDTH, 24);
            CheckBox_curse.setRect(0, GAP + OptionSlider_enchantId.top() + OptionSlider_enchantId.height(), WIDTH / 2f - GAP / 2f, 16);
            RedButton_create.setRect(WIDTH / 2f + GAP / 2f, OptionSlider_enchantId.bottom() + GAP, WIDTH / 2f - GAP / 2f, 16);
            resize(WIDTH, (int) RedButton_create.bottom());
        }

        @Override
        public synchronized void update() {
            super.update();
            // 实时同步UI
            layout();
        }

        /**
         * 创建护甲列表
         */
        private void createArmorList() {
            //AllArmor = Generator.Category.ARMOR.classes.clone();
            AllArmor = new Class<?>[]{
                    ClothArmor.class,
                    LeatherArmor.class,
                    MailArmor.class,
                    ScaleArmor.class,
                    PlateArmor.class,
                    LamellarArmor.class,
                    AncityArmor.class};
        }

        /**
         * 创建护甲图标，并添加到窗口中。
         *
         * @param all 所有护甲的Class数组f
         */
        private void createArmorImage(Class<? extends Armor>[] all) {
            float left = BTN_SIZE / 2f;
            float top = 0;
            int placed = 0;
            int length = all.length;
            for (int i = 0; i < length; ++i) {
                final int j = i;
                IconButton btn = new IconButton() {
                    @Override
                    protected void onClick() {
                        armor_id = Math.min(maxSlots(armor_id) - 1, j);
                        updateSelectedArmorText();
                        super.onClick();
                    }
                };
                Image im = new Image(Assets.Sprites.ITEMS);
                im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(all[i])).image));
                im.scale.set(1f);
                btn.icon(im);
                int row = placed / MAX_ICONS_PER_LINE;
                int col = placed % MAX_ICONS_PER_LINE;
                float x = left + col * (BTN_SIZE + GAP)*2;
                float y = top + row * (BTN_SIZE + GAP);
                btn.setRect(x, y, BTN_SIZE, BTN_SIZE);
                add(btn);
                placed++;
                IconButtons.add(btn);
            }
        }

         /**
         * 更新选中的武器文本。
         */
        private void updateSelectedArmorText() {
            Armor armor = Reflection.newInstance(getArmor(armor_id).getClass());
            Button_Level.text(armor.name());
            layout();
        }

        /**
        * 获取附魔信息文本。
        * @return 附魔信息文本 
        */
        private String getEnchantInfo(Class enchant) {
            return enchant==null?Messages.get(this, "no_enchant"):Messages.get(enchant, "name", Messages.get(this, "enchant"));
        }

        private int getEnchantCount(int rarity) {
            switch (rarity) {
                case 1:
                    return 4;
                case 2:
                    return 6;
                case 3:
                    return 3;
                case 4:
                    return 8;
            }
            return 0;
        }

        private void updateEnchantText() {
            StringBuilder info = new StringBuilder();
            if (enchant_rarity == 0) {
                info = new StringBuilder(Messages.get(this, "no_enchant"));
            } else {
                for (int i = 0; i < getEnchantCount(enchant_rarity); i++) {
                    info.append(i + 1).append(":").append(getEnchantInfo(generateEnchant(enchant_rarity, i))).append(" ");

                    // 添加换行判断
                    if ((i + 1) % 4 == 0 || i == (getEnchantCount(enchant_rarity)-1)) {
                        info.append("\n");
                    }
                }
                info.append(Messages.get(this, "current_enchant",getEnchantInfo(generateEnchant(enchant_rarity, enchant_id))));
            }
            RenderedTextBlock_enchantInfo.text(info.toString());
        }

        private int maxSlots(int t) {
            if (t <= 1) return 3;
            if (t == 2 || t == 3) return 8;
            else return 9;
        }
    }
}