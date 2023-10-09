package com.shatteredpixel.shatteredpixeldungeon.custom.testmode;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.custom.messages.M;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
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
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSadGhost;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Objects;

@SuppressWarnings("all")
public class SpawnWeapon extends TestItem{
    {
        image = ItemSpriteSheet.CANDLE;
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
            //createWeapon();
            //GLog.i(Messages.get(Dungeon.hero, "you_now_have", Messages.get(getEnchant(1,0),"name",new Object[]{"附魔"})));
            GameScene.show(new WeaponSetting());
        }
    }

    public void createWeapon(){
        try{
            Weapon wpn = (Weapon) Reflection.newInstance(getWeapon(tier)[weapon_id]);
            ((Item) wpn).level(weapon_level);
            Class enchantResult = getEnchant(enchant_rarity,enchant_id);
            if(enchantResult!=null){
                wpn.enchant((Weapon.Enchantment) Reflection.newInstance(enchantResult));
            }
            ((Item) wpn).cursed=cursed;
            wpn.identify();
            if(wpn.collect()) {
                GLog.i(Messages.get(Dungeon.hero, "you_now_have", wpn.name()));
            } else {
                wpn.doDrop(Item.curUser);
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

    private class WeaponSetting extends Window {

        private static final int WIDTH = 150;
        private static final int HEIGHT = 250;
        private static final int BTN_SIZE = 18;
        private static final int GAP = 2;

        private Class[] AllWeapon;
        private RedButton Button_Create;
        private CheckBox CheckBox_Curse;
        private ArrayList<IconButton> IconButtons = new ArrayList<>();
        private OptionSlider OptionSlider_EnchantId;

        private OptionSlider OptionSlider_EnchantRarity;
        private RedButton Button_Level;

        private OptionSlider OptionSlider_Tier;
        private RenderedTextBlock Text_EnchantInfo;
        private RenderedTextBlock Text_SelectedWeapon;

        public WeaponSetting(){
            super();

            resize(WIDTH, HEIGHT);
            createWeaponList(tier);

            OptionSlider_Tier = new OptionSlider("武器阶数","1","6",1,6) {
                @Override
                protected void onChange() {
                    tier = getSelectedValue();
                    clearImage();
                    createWeaponList(tier);
                    createWeaponImage(AllWeapon);
                }
            };
            add(OptionSlider_Tier);

            createWeaponImage(AllWeapon);

            Text_SelectedWeapon = PixelScene.renderTextBlock("", 10);
            updateWeaponText();
            add(Text_SelectedWeapon);

            Button_Level = new RedButton("test"){
                @Override
                protected void onClick() {
                    Game.runOnRenderThread(()-> ShatteredPixelDungeon.scene().add(new WndTextNumberInput(
                            "自定义武器等级","输入要生成的武器的等级",Integer.toString(weapon_level),
                            10,false,Messages.get(WndSadGhost.class,"confirm"),
                            Messages.get(WndSadGhost.class,"cancel")){
                        @Override
                        public void onSelect(boolean check, String text) {
                            if( check && text.matches("\\d+") ){
                                weapon_level = Integer.parseInt(text);
                            }
                        }
                    }));
                }
            };
            add(Button_Level);

            Text_EnchantInfo = PixelScene.renderTextBlock("", 3);
            Text_EnchantInfo.text(getEnchantInfo(getEnchant(enchant_rarity,enchant_id)));
            add(Text_EnchantInfo);

            OptionSlider_EnchantRarity = new OptionSlider("附魔种类","0","4",0,4) {
                @Override
                protected void onChange() {
                    enchant_rarity = getSelectedValue();
                    updateEnchantText();
                }
            };
            OptionSlider_EnchantRarity.setSelectedValue(enchant_rarity);
            add(OptionSlider_EnchantRarity);

            OptionSlider_EnchantId = new OptionSlider("附魔编号","0","7",0,7) {
                @Override
                protected void onChange() {
                    enchant_id = getSelectedValue();
                    updateEnchantText();
                }
            };
            OptionSlider_EnchantId.setSelectedValue(enchant_id);
            add(OptionSlider_EnchantId);

            CheckBox_Curse = new CheckBox("诅咒物品"){
                @Override
                protected void onClick() {
                    super.onClick();
                    cursed =checked();
                }
            };
            CheckBox_Curse.checked(cursed);
            add(CheckBox_Curse);

            Button_Create = new RedButton("生成武器"){
                @Override
                protected void onClick() {
                    createWeapon();
                }
            };
            add(Button_Create);
            layout();
        }

        private void layout(){
            //selectedPage.maxWidth(WIDTH / 2);
            //selectedPage.setPos((WIDTH - selectedPage.width())/2, 5);
            //selectedMob.maxWidth(WIDTH);
            //selectedMob.setPos((WIDTH - selectedMob.width())/2, 16);
//            textInput.offset(0,GAP);
            OptionSlider_Tier.setRect(0, GAP, WIDTH, 24);
            //createWeaponImage(all);
            Text_SelectedWeapon.setPos(0, GAP * 2 + OptionSlider_Tier.bottom() + BTN_SIZE);
            Button_Level.setRect(0,Text_SelectedWeapon.bottom() + GAP, WIDTH, 24);
            Text_EnchantInfo.setPos(0, GAP + Button_Level.bottom());
            OptionSlider_EnchantRarity.setRect(0, GAP + Text_EnchantInfo.bottom(), WIDTH, 24);
            OptionSlider_EnchantId.setRect(0, GAP + OptionSlider_EnchantRarity.bottom(), WIDTH, 24);
            CheckBox_Curse.setRect(0, GAP + OptionSlider_EnchantId.bottom(), WIDTH/2f - GAP/2f, 16);
            Button_Create.setRect(WIDTH/2f+GAP/2f, CheckBox_Curse.top(), WIDTH/2f - GAP/2f, 16);
            resize(WIDTH, (int) (CheckBox_Curse.bottom() + GAP));
        }

        private String getEnchantInfo(Class enchant){
            return Messages.get(enchant,"name", "附魔");
        }

        private void updateWeaponText(){
            Text_SelectedWeapon.text( M.L(AllWeapon[Math.min(weapon_id,AllWeapon.length-1)], "name") );
        }

        private int getEnchantCount(int rarity){
            switch (rarity){
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

        private void updateEnchantText(){
            String info = new String();
            if(enchant_rarity==0){
                info = "无附魔";
            }
            else{
                for(int i=0;i<getEnchantCount(enchant_rarity);i++){
                    info += (i+1)+":"+getEnchantInfo(getEnchant(enchant_rarity,i)) + "\n";
                }
            }
            Text_EnchantInfo.text( info );
        }

        private void createWeaponList(int tier){
            AllWeapon = getWeapon(tier);
        }

        private void updateSelectedWeaponText() {
            Text_SelectedWeapon.text(Messages.get(this, "selected", Messages.get(AllWeapon[Math.min(weapon_id, AllWeapon.length-1)]
                    , "name")));
            layout();
        }

        private void createWeaponImage(Class<? extends Weapon>[] all) {
            float left;
            float top = OptionSlider_Tier.bottom() + GAP;
            int placed = 0;
            int length = all.length;
            left = (WIDTH - BTN_SIZE * 6) / 2f; // 每行最多放置6个按钮
            int row = 0; // 记录当前所在行数
            for (int i = 0; i < length; ++i) {
                final int j = i;
                IconButton btn = new IconButton() {
                    @Override
                    protected void onClick() {
                        weapon_id = Math.min(maxSlots(tier)-1, j);
                        updateSelectedWeaponText();
                        super.onClick();
                    }
                };
                Image im = new Image(Assets.Sprites.ITEMS);
                im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(all[i])).image));
                im.scale.set(1f);
                btn.icon(im);

                // 计算按钮位置
                float btnLeft = left + placed * BTN_SIZE;
                float btnTop = top + row * BTN_SIZE;
                btn.setRect(btnLeft, btnTop, BTN_SIZE, BTN_SIZE);

                add(btn);
                placed++;
                IconButtons.add(btn);

                // 当按钮数量超过6个时，换行
                if (placed >= 6) {
                    placed = 0; // 重置计数
                    row++; // 移动到下一行
                }
            }
        }

        private int maxSlots(int t){
            if(t <= 1) return 5;
            if(t == 2 || t == 3) return 6;
            else return 7;
        }

        private void clearImage(){
            for(int i=0, len = IconButtons.size();i<len;++i){
                IconButtons.get(i).destroy();
            }
        }

        @Override
        public void onBackPressed() {
            super.onBackPressed();
        }
    }
}
