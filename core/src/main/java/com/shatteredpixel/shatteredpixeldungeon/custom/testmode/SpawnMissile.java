package com.shatteredpixel.shatteredpixeldungeon.custom.testmode;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
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
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.DeadBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Elastic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.HaloBlazing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Kinetic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Lucky;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Projecting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.TimeReset;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Unstable;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Vampiric;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Bolas;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.CrossReback;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.FishingSpear;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ForceCube;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.HeavyBoomerang;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Javelin;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Kunai;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.RedBlock;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Shuriken;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.StreamerKnife;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingClub;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingHammer;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingSpear;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Tomahawk;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Trident;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart;
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

public class SpawnMissile extends TestItem {
    {
        image = ItemSpriteSheet.MISSILE_HOLDER;
        defaultAction = AC_SPAWN;
    }

    private int item_id;
    private int item_quantity;
    private int item_level;
    private boolean cursed;
    private int enchant_rarity;
    private int enchant_id;
    private boolean bowGenerated = false;
    private static final String AC_SPAWN = "spawn";
    private static final String AC_BOW = "bow";

    private final Class<?>[] missileWeapon = new Class<?>[]{
            Dart.class,
            ThrowingStone.class,
            ThrowingKnife.class,
            FishingSpear.class,
            Shuriken.class,
            ThrowingClub.class,
            ThrowingSpear.class,
            Kunai.class,
            Bolas.class,
            Javelin.class,
            HeavyBoomerang.class,
            Tomahawk.class,
            ThrowingHammer.class,
            Trident.class,
            ForceCube.class,
            StreamerKnife.class,
            RedBlock.class,
            CrossReback.class
    };

    public SpawnMissile(){
        this.item_id = 0;
        this.item_quantity = 1;
        this.item_level = 0;
        this.cursed = false;
        this.enchant_rarity = 0;
        this.enchant_id = 0;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_SPAWN);
        actions.add(AC_BOW);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_SPAWN)) {
            GameScene.show(new SettingsWindow());
        } else if (action.equals(AC_BOW)) {
            SpiritBow bow = new SpiritBow();
            bow.identify().collect();
            bowGenerated = true;
        }
    }

    private void createMissiles(){
        MissileWeapon missile = Reflection.newInstance(missileList.get(item_id));
        missile.level(item_level);
        missile.quantity(item_quantity);

        //参照 SpawnWeapon：支持附魔与诅咒（测试工具不受正常游戏规则限制）
        Class enchantResult = getEnchant(enchant_rarity, enchant_id);
        if (enchantResult != null) {
            missile.enchant((Weapon.Enchantment) Reflection.newInstance(enchantResult));
        }
        missile.cursed = cursed;
        missile.identify();

        if(missile.collect()){
            GLog.i(Messages.get(hero, "you_now_have", missile.name()));
            Sample.INSTANCE.play( Assets.Sounds.ITEM );
            GameScene.pickUp( missile, hero.pos );
        }else{
            missile.doDrop(curUser);
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("item_quantity", item_quantity);
        bundle.put("item_id", item_id);
        bundle.put("item_level", item_level);
        bundle.put("cursed", cursed);
        bundle.put("enchant_rarity", enchant_rarity);
        bundle.put("enchant_id", enchant_id);
        bundle.put("bow_generated", bowGenerated);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        item_quantity = bundle.getInt("item_quantity");
        item_id = bundle.getInt("item_id");
        item_level = bundle.getInt("item_level");
        cursed = bundle.getBoolean("cursed");
        enchant_rarity = bundle.getInt("enchant_rarity");
        enchant_id = bundle.getInt("enchant_id");
        bowGenerated = bundle.getBoolean("bow_generated");
    }

    private static ArrayList<Class<? extends MissileWeapon>> missileList = new ArrayList<>();

    private Class idToMissile(int id) {
        return missileWeapon[id];
    }

    private void buildList() {
        if (missileList.isEmpty()) {
            for (int i = 0; i < missileWeapon.length; ++i) {
                missileList.add(idToMissile(i));
            }
        }
    }

    /**
     * 与 SpawnWeapon 相同的附魔表：
     * rarity 1=常见, 2=稀有, 3=罕见, 4=诅咒
     */
    private Class getEnchant(int rarity, int id){
        switch (rarity){
            case 1:
                switch (id){
                    case 0: return Blazing.class;
                    case 1: return Chilling.class;
                    case 2: return Kinetic.class;
                    case 3: return Shocking.class;
                    default: return null;
                }
            case 2:
                switch (id){
                    case 0: return Blocking.class;
                    case 1: return Blooming.class;
                    case 2: return Elastic.class;
                    case 3: return Lucky.class;
                    case 4: return Projecting.class;
                    case 5: return Unstable.class;
                    default: return null;
                }
            case 3:
                switch (id){
                    case 0: return Corrupting.class;
                    case 1: return Grim.class;
                    case 2: return Vampiric.class;
                    case 3: return HaloBlazing.class;
                    case 4: return Crushing.class;
                    case 5: return TimeReset.class;
                    case 6: return DeadBomb.class;
                    default: return null;
                }
            case 4:
                switch (id){
                    case 0: return Annoying.class;
                    case 1: return Displacing.class;
                    case 2: return Exhausting.class;
                    case 3: return Fragile.class;
                    case 4: return Sacrificial.class;
                    case 5: return Wayward.class;
                    case 6: return Polarized.class;
                    case 7: return Friendly.class;
                    default: return null;
                }
        }
        return null;
    }

    private int getEnchantCount(int rarity) {
        switch (rarity) {
            case 1: return 4;
            case 2: return 6;
            case 3: return 7;
            case 4: return 8;
        }
        return 0;
    }

    private class SettingsWindow extends Window {
        private final RedButton RedButton_level;
        private final RedButton RedButton_quantity;
        private RedButton b_create;
        private final CheckBox CheckBox_curse;
        private final OptionSlider OptionSlider_enchantRarity;
        private final OptionSlider OptionSlider_enchantId;
        private final RenderedTextBlock Text_enchantInfo;
        private ArrayList<IconButton> IconButton = new ArrayList<>();
        private static final int WIDTH = 130;
        private static final int BTN_SIZE = 19;
        private static final int GAP = 2;

        public SettingsWindow() {
            buildList();
            createImage();

            RedButton_level = new RedButton(Messages.get(this, "select_item")) {
                @Override
                protected void onClick() {
                    if(!RedButton_level.text().equals(Messages.get(SettingsWindow.class, "select_item"))){ // 修改此行代码
                        Game.runOnRenderThread(() ->GameScene.show(new WndTextNumberInput(
                                Messages.get(SettingsWindow.class, "item_level"), Messages.get(SettingsWindow.class, "item_level_desc"),
                                Integer.toString(item_level),
                                4, false, Messages.get(SettingsWindow.class, "confirm"),
                                Messages.get(SettingsWindow.class, "cancel"),false) {
                            @Override
                            public void onSelect(boolean check, String text) {

                                if (check && text.matches("\\d+")) {
                                    int level = Integer.parseInt(text);
                                    item_level = Math.min(level, 6666);
                                }
                            }
                        }));
                    } else {
                        Game.scene().add( new WndError( Messages.get(SettingsWindow.class, "item_level_error") ) );
                    }
                }
            };
            RedButton_level.text(((MissileWeapon) Reflection.newInstance(idToMissile(item_id))).name());
            add(RedButton_level);

            RedButton_quantity = new RedButton(Messages.get(this, "select_item")) {
                @Override
                protected void onClick() {
                    if(!RedButton_quantity.text().equals(Messages.get(SettingsWindow.class, "select_item"))){ // 修改此行代码
                        Game.runOnRenderThread(() ->GameScene.show(new WndTextNumberInput(
                                Messages.get(SettingsWindow.class, "item_level"), Messages.get(SettingsWindow.class, "item_level_desc"),
                                Integer.toString(item_quantity),
                                4, false, Messages.get(SettingsWindow.class, "confirm"),
                                Messages.get(SettingsWindow.class, "cancel"),false) {
                            @Override
                            public void onSelect(boolean check, String text) {
                                if (check && item_quantity > 0 &&text.matches("^[1-9]\\d*$")) {
                                    int quantity = Integer.parseInt(text);
                                    item_quantity = Math.min(quantity, 6666);
                                }
                                updateQuantityText();
                            }
                        }));
                    } else {
                        Game.scene().add( new WndError( Messages.get(SettingsWindow.class, "item_level_error") ) );
                    }
                }
            };
            updateQuantityText();
            add(RedButton_quantity);

            //附魔信息文本块（参照 SpawnWeapon）
            Text_enchantInfo = PixelScene.renderTextBlock("", 6);
            updateEnchantText();
            add(Text_enchantInfo);

            //附魔种类滑块：0=无附魔, 1=常见, 2=稀有, 3=罕见, 4=诅咒
            OptionSlider_enchantRarity = new OptionSlider(Messages.get(this, "enchant_rarity"), "1", "5", 0, 4) {
                @Override
                protected void onChange() {
                    enchant_rarity = getSelectedValue();
                    updateEnchantText();
                }
            };
            OptionSlider_enchantRarity.setSelectedValue(enchant_rarity);
            add(OptionSlider_enchantRarity);

            //附魔编号滑块
            OptionSlider_enchantId = new OptionSlider(Messages.get(this, "enchant_id"), "1", "8", 0, 7) {
                @Override
                protected void onChange() {
                    enchant_id = getSelectedValue();
                    updateEnchantText();
                }
            };
            OptionSlider_enchantId.setSelectedValue(enchant_id);
            add(OptionSlider_enchantId);

            //诅咒物品复选框
            CheckBox_curse = new CheckBox(Messages.get(this, "cursed")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    cursed = checked();
                }
            };
            CheckBox_curse.checked(cursed);
            add(CheckBox_curse);

            b_create = new RedButton(Messages.get(this, "create_button")) {
                @Override
                protected void onClick() {
                    createMissiles();
                }
            };
            add(b_create);
            layout();
        }

        private void updateQuantityText() {
            RedButton_quantity.text(Messages.get(this, "item_quantity",item_quantity));
        }

        private void updateLevelText() {
            RedButton_level.text(Messages.get(missileList.get(item_id), "name"));
        }

        private void updateEnchantText() {
            StringBuilder info = new StringBuilder();
            if (enchant_rarity == 0) {
                info = new StringBuilder(Messages.get(this, "no_enchant"));
            } else {
                for (int i = 0; i < getEnchantCount(enchant_rarity); i++) {
                    info.append(i + 1).append(":").append(getEnchantInfo(getEnchant(enchant_rarity, i))).append(" ");
                    if ((i + 1) % 4 == 0 || i == (getEnchantCount(enchant_rarity) - 1)) {
                        info.append("\n");
                    }
                }
                info.append(Messages.get(this, "current_enchant", getEnchantInfo(getEnchant(enchant_rarity, enchant_id))));
            }
            Text_enchantInfo.text(info.toString());
        }

        private String getEnchantInfo(Class enchant) {
            return enchant == null ? Messages.get(this, "no_enchant") : Messages.get(enchant, "name", Messages.get(this, "enchant"));
        }

        private void layout() {
            RedButton_level.setRect(0, IconButton.get(IconButton.size() - 1).bottom() + GAP + 2 * GAP, WIDTH, 24);
            RedButton_quantity.setRect(0, RedButton_level.bottom() + GAP, WIDTH, 24);
            Text_enchantInfo.setPos(0, RedButton_quantity.bottom() + GAP);
            OptionSlider_enchantRarity.setRect(0, GAP + Text_enchantInfo.bottom(), WIDTH, 24);
            OptionSlider_enchantId.setRect(0, GAP + OptionSlider_enchantRarity.bottom(), WIDTH, 24);
            CheckBox_curse.setRect(0, GAP + OptionSlider_enchantId.bottom()+10, WIDTH / 2f - GAP / 2f, 16);
            b_create.setRect(WIDTH / 2f + GAP / 2f, CheckBox_curse.top(), WIDTH / 2f - GAP / 2f, 16);
            resize(WIDTH, (int) (b_create.bottom() + GAP));
        }

        private void createImage() {
            float left;
            float top = GAP;
            int placed = 0;
            int row = 1;
            int picPerRow = 6;
            int len = missileList.size();
            left = (WIDTH - BTN_SIZE * Math.min(len, picPerRow)) / 2f;
            for (int i = 0; i < len; ++i) {
                final int j = i;
                IconButton btn = new IconButton() {
                    @Override
                    protected void onClick() {
                        item_id = Math.min(j, missileWeapon.length);
                        super.onClick();
                        updateLevelText();
                    }
                };
                Image im = new Image(Assets.Sprites.ITEMS);
                im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(missileList.get(i))).image));
                im.scale.set(1.0f);
                btn.icon(im);
                btn.setRect(left + placed * BTN_SIZE, top + (row - 1) * (BTN_SIZE + GAP), BTN_SIZE, BTN_SIZE);
                add(btn);
                placed++;
                if (placed > 0 && placed % picPerRow == 0) {
                    placed = 0;
                    left = (WIDTH - BTN_SIZE * Math.min(len - row * picPerRow, picPerRow)) / 2f;
                    row++;
                }
                IconButton.add(btn);
            }
        }


    }
}
