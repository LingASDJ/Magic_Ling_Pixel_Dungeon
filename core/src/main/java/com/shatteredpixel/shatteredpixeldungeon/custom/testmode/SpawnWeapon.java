package com.shatteredpixel.shatteredpixeldungeon.custom.testmode;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.custom.messages.M;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Annoying;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Displacing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Exhausting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Fragile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Friendly;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Polarized;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Sacrificial;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Wayward;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blazing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blooming;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Chilling;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Corrupting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Crushing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Elastic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.HaloBlazing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Kinetic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Lucky;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Projecting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Unstable;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Vampiric;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
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


public class SpawnWeapon extends TestItem{
    {
        image = ItemSpriteSheet.DG25;
        defaultAction = AC_SPAWN;
    }

    private static final String AC_SPAWN = "spawn";

    private int tier;
    private boolean cursed;
    private int weapon_level;
    private int enchant_id;
    private int enchant_rarity;
    private int weapon_id;

    public SpawnWeapon(){
        this.tier = 1;
        this.cursed = false;
        this.weapon_level = 0;
        this.enchant_id = 0;
        this.enchant_rarity = 0;
        this.weapon_id = 0;
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
        if (action.equals(AC_SPAWN)){
            GameScene.show(new WeaponSetting());
        }
    }

    public void createWeapon(){
        try{
            Weapon wpn = (Weapon) Reflection.newInstance(getWeapon(tier)[weapon_id]);
            wpn.level(weapon_level);
            Class enchantResult = getEnchant(enchant_rarity,enchant_id);
            if(enchantResult!=null){
                wpn.enchant((Weapon.Enchantment) Reflection.newInstance(enchantResult));
            }
            wpn.cursed=cursed;
            wpn.identify();
            if(wpn instanceof MagesStaff){
              wpn=new MagesStaff(new WandOfMagicMissile());
              wpn.identify();
              GameScene.pickUp(wpn,hero.pos);
              Sample.INSTANCE.play(Assets.Sounds.ITEM);
            }
            if(wpn.collect()) {
                GameScene.pickUp( wpn, hero.pos );
                Sample.INSTANCE.play( Assets.Sounds.ITEM );
                GLog.i(Messages.get(hero, "you_now_have", wpn.name()));
            } else {
                wpn.doDrop(curUser);
            }
        }catch (Exception e){
            GLog.w(M.L(MobPlacer.class, "forbidden"));
        }
    }

    private Class[] getWeapon(int tier){
        switch (tier){
            case 1:
                return Generator.Category.WEP_T1.classes.clone();
            case 2:
                return Generator.Category.WEP_T2.classes.clone();
            case 3:
                return Generator.Category.WEP_T3.classes.clone();
            case 4:
                return Generator.Category.WEP_T4.classes.clone();
            case 5:
                return Generator.Category.WEP_T5.classes.clone();
            case 6:
                return Generator.Category.WEP_T6.classes.clone();
        }
        return Generator.Category.WEP_T1.classes.clone();
    }

    private Class getEnchant(int rarity,int id){
        switch (rarity){
            case 1:
                switch (id){
                    case 0:
                        return Blazing.class;
                    case 1:
                        return Chilling.class;
                    case 2:
                        return Kinetic.class;
                    case 3:
                        return Shocking.class;
                    default:
                        return null;
                }
            case 2:
                switch (id){
                    case 0:
                        return Blocking.class;
                    case 1:
                        return Blooming.class;
                    case 2:
                        return Elastic.class;
                    case 3:
                        return Lucky.class;
                    case 4:
                        return Projecting.class;
                    case 5:
                        return Unstable.class;
                    default:
                        return null;
                }
            case 3:
                switch (id){
                    case 0:
                        return Corrupting.class;
                    case 1:
                        return Grim.class;
                    case 2:
                        return Vampiric.class;
                    case 3:
                        return HaloBlazing.class;
                    case 4:
                        return Crushing.class;
                    default:
                        return null;
                }
            case 4:
                switch (id){
                    case 0:
                        return Annoying.class;
                    case 1:
                        return Displacing.class;
                    case 2:
                        return Exhausting.class;
                    case 3:
                        return Fragile.class;
                    case 4:
                        return Sacrificial.class;
                    case 5:
                        return Wayward.class;
                    case 6:
                        return Polarized.class;
                    case 7:
                        return Friendly.class;
                    default:
                        return null;
                }
        }
        return null;
    }

    @Override
    public void storeInBundle(Bundle b){
        super.storeInBundle(b);
        b.put("tier", tier);
        b.put("cursed", cursed);
        b.put("weapon_level", weapon_level);
        b.put("enchant_id", enchant_id);
        b.put("enchant_rarity", enchant_rarity);
        b.put("weapon_id", weapon_id);
    }

    @Override
    public void restoreFromBundle(Bundle b){
        super.restoreFromBundle(b);
        tier = b.getInt("tier");
        cursed = b.getBoolean("cursed");
        weapon_level = b.getInt("weapon_level");
        enchant_id = b.getInt("enchant_id");
        enchant_rarity = b.getInt("enchant_rarity");
        weapon_id = b.getInt("weapon_id");
    }

    /**
     * 武器设置窗口类，继承自Window类。
     */
    private class WeaponSetting extends Window {

        // 定义常量
        private static final int WIDTH = 150; // 窗口宽度
        private static final int HEIGHT = 250; // 窗口高度
        private static final int BTN_SIZE = 18; // 按钮尺寸
        private static final int GAP = 2; // 间隔大小
        private static final int MAX_ICONS_PER_LINE = 7; // 每行最大图标数量

        // 成员变量
        private Class[] AllWeapon; // 所有武器的Class数组
        private final RedButton Button_Create; // 创建武器按钮
        private final CheckBox CheckBox_Curse; // 诅咒物品复选框
        private final ArrayList<IconButton> IconButtons = new ArrayList<>(); // 图标按钮列表
        private final OptionSlider OptionSlider_EnchantId; // 附魔编号选项滑块
        private final OptionSlider OptionSlider_EnchantRarity; // 附魔种类选项滑块
        private final RedButton Button_Level; // 武器等级按钮
        private final OptionSlider OptionSlider_Tier; // 武器阶数选项滑块
        private final RenderedTextBlock Text_EnchantInfo; // 附魔信息文本块

        /**
         * 构造函数，用于初始化窗口。
         */
        public WeaponSetting() {
            super();

            // 设置窗口尺寸
            resize(WIDTH, HEIGHT);

            // 创建武器列表
            createWeaponList(tier);

            // 创建武器阶数选项滑块
            OptionSlider_Tier = new OptionSlider(Messages.get(this, "weapon_tier"), "1", "6", 1, 6) {
                @Override
                protected void onChange() {
                    tier = getSelectedValue();
                    clearImage();
                    createWeaponList(tier);
                    createWeaponImage(AllWeapon);
                }
            };
            OptionSlider_Tier.setSelectedValue(tier);
            add(OptionSlider_Tier);

            // 创建武器图标
            createWeaponImage(AllWeapon);

            // 创建附魔信息文本块
            Text_EnchantInfo = PixelScene.renderTextBlock("", 6);
            updateEnchantText();
            add(Text_EnchantInfo);

            // 创建附魔种类选项滑块
            OptionSlider_EnchantRarity = new OptionSlider(Messages.get(this, "enchant_rarity"), "1", "5", 0, 4) {
                @Override
                protected void onChange() {
                    enchant_rarity = getSelectedValue();
                    updateEnchantText();
                }
            };
            OptionSlider_EnchantRarity.setSelectedValue(enchant_rarity);
            add(OptionSlider_EnchantRarity);

            // 创建附魔编号选项滑块
            OptionSlider_EnchantId = new OptionSlider(Messages.get(this, "enchant_id"), "1", "8", 0, 7) {
                @Override
                protected void onChange() {
                    enchant_id = getSelectedValue();
                    updateEnchantText();
                }
            };
            OptionSlider_EnchantId.setSelectedValue(enchant_id);
            add(OptionSlider_EnchantId);

            // 创建诅咒物品复选框
            CheckBox_Curse = new CheckBox(Messages.get(this, "cursed")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    cursed = checked();
                }
            };
            CheckBox_Curse.checked(cursed);
            add(CheckBox_Curse);

            // 创建武器等级按钮
            Button_Level = new RedButton(Messages.get(this, "select_weapon")) {
                @Override
                protected void onClick() {
                    if(!Button_Level.text().equals(Messages.get(SpawnWeapon.WeaponSetting.class, "select_weapon"))){ // 修改此行代码
                        Game.runOnRenderThread(() -> ShatteredPixelDungeon.scene().add(new WndTextNumberInput(
                                Messages.get(SpawnWeapon.WeaponSetting.class, "weapon_level"), Messages.get(SpawnWeapon.WeaponSetting.class, "weapon_level_desc"),
                                Integer.toString(weapon_level),
                                4, false, Messages.get(SpawnWeapon.WeaponSetting.class, "confirm"),
                                Messages.get(SpawnWeapon.WeaponSetting.class, "cancel")) {
                            @Override
                            public void onSelect(boolean check, String text) {

                                if (check && text.matches("\\d+")) {
                                    int level = Integer.parseInt(text);
                                    weapon_level = Math.min(level, 6666);
                                }
                            }
                        }));
                    } else {
                        Game.scene().add( new WndError( Messages.get(SpawnWeapon.WeaponSetting.class, "weapon_level_error") ) );
                    }
                }
            };
            Button_Level.text(((Weapon) Reflection.newInstance(getWeapon(tier)[weapon_id])).name());
            add(Button_Level);

            // 创建生成武器按钮
            Button_Create = new RedButton(Messages.get(this, "create")) {
                @Override
                protected void onClick() {
                    createWeapon();
                }
            };
            add(Button_Create);

            layout();
        }

        /**
         * 封装一个同步UI的方法，用于调整UI组件的位置和大小。
         */
        private void SyncUI() {
            OptionSlider_Tier.setRect(0, GAP, WIDTH, 24);
            int numLines = (int) Math.ceil(AllWeapon.length / (float) MAX_ICONS_PER_LINE);
            float totalHeight = 30;
            if (numLines > 0) {
                totalHeight += numLines * (BTN_SIZE + GAP);
            }
            Button_Level.setRect(0, totalHeight, WIDTH, 24);
            Text_EnchantInfo.setPos(0, GAP + Button_Level.bottom());
            OptionSlider_EnchantRarity.setRect(0, GAP + Text_EnchantInfo.bottom(), WIDTH, 24);
            OptionSlider_EnchantId.setRect(0, GAP + OptionSlider_EnchantRarity.bottom(), WIDTH, 24);
            CheckBox_Curse.setRect(0, GAP + OptionSlider_EnchantId.bottom(), WIDTH / 2f - GAP / 2f, 16);
            Button_Create.setRect(WIDTH / 2f + GAP / 2f, CheckBox_Curse.top(), WIDTH / 2f - GAP / 2f, 16);
            resize(WIDTH, (int) (CheckBox_Curse.bottom() + GAP));
        }

        @Override
        public synchronized void update() {
            super.update();
            // 实时同步UI
            SyncUI();
        }

        private void layout(){
            SyncUI();
        }

        /**
         * 创建武器列表，并根据阶数筛选相应的武器类。
         *
         * @param tier 武器的阶数
         */
        private void createWeaponList(int tier) {
            AllWeapon = getWeapon(tier);
        }

        /**
         * 更新选中的武器文本。
         */
        private void updateSelectedWeaponText() {
            Weapon wpn = (Weapon) Reflection.newInstance(getWeapon(tier)[weapon_id]);
            Button_Level.text(wpn.name());
            layout();
        }

        /**
         * 创建武器图标，并添加到窗口中。
         *
         * @param all 所有武器的Class数组
         */
        private void createWeaponImage(Class<? extends Weapon>[] all) {
            float left = BTN_SIZE / 2f;
            float top = 27;
            int placed = 0;
            int length = all.length;
            for (int i = 0; i < length; ++i) {
                final int j = i;
                IconButton btn = new IconButton() {
                    @Override
                    protected void onClick() {
                        weapon_id = Math.min(maxSlots(tier) - 1, j);
                        updateSelectedWeaponText();
                        super.onClick();
                    }
                };
                Image im = new Image(Assets.Sprites.ITEMS);
                im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(all[i])).image));
                im.scale.set(1f);
                btn.icon(im);
                int row = placed / MAX_ICONS_PER_LINE;
                int col = placed % MAX_ICONS_PER_LINE;
                float x = left + col * (BTN_SIZE + GAP);
                float y = top + row * (BTN_SIZE + GAP);
                btn.setRect(x, y, BTN_SIZE, BTN_SIZE);
                add(btn);
                placed++;
                IconButtons.add(btn);
            }
        }

        /**
         * 清除武器图标。
         */
        private void clearImage() {
            for (int i = 0, len = IconButtons.size(); i < len; ++i) {
                IconButtons.get(i).destroy();
            }
            IconButtons.clear();
        }
    /**
    * 获取附魔信息文本。
    *
     * @param enchant 附魔类
    * @return 附魔信息文本 */
        private String getEnchantInfo(Class enchant) {
            return enchant==null?Messages.get(this, "no_enchant"):Messages.get(enchant, "name", Messages.get(this, "enchant"));
        }

        private int maxSlots(int t) {
            if (t <= 1) return 5;
            if (t == 2 || t == 3) return 1145;
            else return 8;
        }


        private int getEnchantCount(int rarity) {
            switch (rarity) {
                case 1:
                    return 4;
                case 2:
                    return 6;
                case 3:
                    return 5;
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
                    info.append(i + 1).append(":").append(getEnchantInfo(getEnchant(enchant_rarity, i))).append(" ");

                    // 添加换行判断
                    if ((i + 1) % 4 == 0 || i == (getEnchantCount(enchant_rarity)-1)) {
                        info.append("\n");
                    }
                }
                info.append(Messages.get(this, "current_enchant",getEnchantInfo(getEnchant(enchant_rarity, enchant_id))));
            }
            Text_EnchantInfo.text(info.toString());
        }

        @Override
        public void onBackPressed() {
            super.onBackPressed();
        }
    }

}
