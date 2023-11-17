package com.shatteredpixel.shatteredpixeldungeon.custom.testmode;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.OptionSlider;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.WndTextNumberInput;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndError;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Objects;

//Also contains wand
public class SpawnRingOrWand extends TestItem {
    {
        image = ItemSpriteSheet.DG25;
        defaultAction = AC_SPAWN;
    }

    private int item_level;
    private int category;
    private boolean cursed;
    private int item_id;
    private static final int RING_CAT = 0;
    private static final int WAND_CAT = 1;
    private static final String AC_SPAWN = "spawn";

    public SpawnRingOrWand(){
        this.item_level = 0;
        this.category = 0;
        this.cursed = false;
        this.item_id =0;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_SPAWN)) {
            GameScene.show(new SettingsWindow());
        }
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_SPAWN);
        return actions;
    }

    private void createItem(){
        boolean collect = false;
        if (category == RING_CAT) {
            Ring ring = (Ring) Reflection.newInstance(idToItem(item_id,RING_CAT));
            if(ring != null) {
                if(Challenges.isItemBlocked(ring))
                    return;
                modifyRing(ring);
                collect = ring.identify().collect();
                if(collect){
                    GLog.i(Messages.get(hero, "you_now_have", ring.name()));
                }else{
                    ring.doDrop(curUser);
                }
            }
        } else if (category == WAND_CAT) {
            Wand wand = (Wand) Reflection.newInstance(idToItem(item_id,WAND_CAT));
            if( wand != null) {
                if(Challenges.isItemBlocked(wand))
                    return;
                modifyWand(wand);
                collect = wand.identify().collect();
                if(collect){
                    GLog.i(Messages.get(hero, "you_now_have", wand.name()));
                }else{
                    wand.doDrop(curUser);
                }
            }
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("item_id", item_id);
        bundle.put("is_cursed", cursed);
        bundle.put("item_level", item_level);
        bundle.put("category", category);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        item_id = bundle.getInt("item_id");
        cursed = bundle.getBoolean("is_cursed");
        item_level = bundle.getInt("item_level");
        category = bundle.getInt("category");
    }

    private void modifyRing(Ring ring) {
        ring.level(item_level);
        ring.cursed = cursed;
    }

    private void modifyWand(Wand wand) {
        wand.level(item_level);
        wand.cursed = cursed;
    }

    private Class idToItem(int item_id,int categoryIndex) {
        return categoryIndex == RING_CAT?Generator.Category.RING.classes[item_id]:Generator.Category.WAND.classes[item_id];
    }

    private static ArrayList<Class<? extends Ring>> ringList = new ArrayList<>();
    private static ArrayList<Class<? extends Wand>> wandList = new ArrayList<>();

    private void buildRingList() {
        if (!ringList.isEmpty()) return;
        for (int i = 0; i < Generator.Category.RING.classes.length; ++i) {
            ringList.add(idToItem(i,RING_CAT));
        }
    }

    private void buildWandList() {
        if (!wandList.isEmpty()) return;
        for (int i = 0; i < Generator.Category.WAND.classes.length; ++i) {
            wandList.add(idToItem(i,WAND_CAT));
        }
    }

    private int total(int category){
        if (category == RING_CAT) return ringList.size();
        if (category == WAND_CAT) return wandList.size();
        return 0;
    }

    private class SettingsWindow extends Window {
        private static final int WIDTH = 120;
        private static final int BTN_SIZE = 17;
        private static final int GAP = 2;
        private final RedButton Button_Level;
        private OptionSlider OptionSlider_category;
        private CheckBox CheckBox_curse;
        private RedButton RedButton_create;
        private ArrayList<IconButton> IconButtons = new ArrayList<>();

        public SettingsWindow() {
            buildRingList();
            buildWandList();

            OptionSlider_category = new OptionSlider(Messages.get(SettingsWindow.class, "category"), Messages.get(SettingsWindow.class, "ring"), Messages.get(SpawnRingOrWand.SettingsWindow.class, "wand"), 0, 1) {
                @Override
                protected void onChange() {
                    category = getSelectedValue();
                    updateId();
                    clearImage();
                    createImage();
                    updateText();
                    layout();
                }
            };
            OptionSlider_category.setSelectedValue(category);
            OptionSlider_category.setRect(0, GAP, WIDTH, 24);
            add(OptionSlider_category);

            createImage();

            Button_Level = new RedButton(Messages.get(SettingsWindow.class, "select_item")) {
                @Override
                protected void onClick() {
                    if(!Button_Level.text().equals(Messages.get(SettingsWindow.class, "select_item"))){ // 修改此行代码
                        Game.runOnRenderThread(() -> ShatteredPixelDungeon.scene().add(new WndTextNumberInput(
                                Messages.get(SettingsWindow.class, "item_level"), Messages.get(SettingsWindow.class, "item_level_desc"),
                                Integer.toString(item_level),
                                4, false, Messages.get(SettingsWindow.class, "confirm"),
                                Messages.get(SettingsWindow.class, "cancel")) {
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
            add(Button_Level);

            CheckBox_curse = new CheckBox(Messages.get(SettingsWindow.class, "curse")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    cursed = checked();
                }
            };
            CheckBox_curse.checked(cursed);
            add(CheckBox_curse);

            RedButton_create = new RedButton(Messages.get(SettingsWindow.class, "create_button")) {
                @Override
                protected void onClick() {
                    createItem();
                }
            };
            add(RedButton_create);

            updateText();
        }

        private void layout() {
            OptionSlider_category.setRect(0, GAP, WIDTH, 24);
            Button_Level.setPos(0, OptionSlider_category.bottom() + 3 * GAP + 2 * BTN_SIZE);
            Button_Level.setRect(0, OptionSlider_category.bottom() + GAP, WIDTH, 24);
            CheckBox_curse.setRect(0, Button_Level.bottom() + GAP, WIDTH, 18);
            RedButton_create.setRect(0, CheckBox_curse.bottom()+GAP, WIDTH, 16);
            resize(WIDTH, (int) RedButton_create.bottom() + GAP);
        }

        private void updateId() {
            switch (category){
                case RING_CAT:
                    if(item_id >= Generator.Category.RING.classes.length )
                        item_id = 0;
                    break;
                case WAND_CAT:
                    if (item_id >= Generator.Category.WAND.classes.length)
                        item_id = 0;
                    break;
                default:
                    item_id = 0;
            }
        }
        private void updateText() {
            Class<? extends Item> item = ringList.get(0);
            if(category == RING_CAT){
                item = ringList.get(item_id);
            }
            else if (category == WAND_CAT) {
                item = wandList.get(item_id);
            }
            Button_Level.text(Messages.get(item, "name"));
            layout();
        }

        private void createImage() {
            float left;
            float top = GAP + OptionSlider_category.bottom();
            int placed = 0;
            int length = (category == RING_CAT ? ringList.size() : (category == WAND_CAT ? wandList.size() : 0));
            int firstRow = (length % 2 == 0 ? length / 2 : (length / 2 + 1));
            for (int i = 0; i < length; ++i) {
                final int j = i;
                IconButton btn = new IconButton() {
                    @Override
                    protected void onClick() {
                        item_id = Math.min(j,total(category)-1);
                        updateText();
                        super.onClick();
                    }
                };
                if (category == RING_CAT) {
                    Image im = new Image(Assets.Sprites.ITEM_ICONS);
                    im.frame(ItemSpriteSheet.Icons.film.get(Objects.requireNonNull(Reflection.newInstance(ringList.get(i))).icon));
                    im.scale.set(1.6f);
                    btn.icon(im);
                } else if (category == WAND_CAT) {
                    Image im = new Image(Assets.Sprites.ITEMS);
                    im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(wandList.get(i))).image));
                    im.scale.set(0.9f);
                    btn.icon(im);
                }

                if (i < firstRow) {
                    left = (WIDTH - BTN_SIZE * firstRow) / 2f;
                    btn.setRect(left + placed * BTN_SIZE, top, BTN_SIZE, BTN_SIZE);
                } else {
                    left = (WIDTH - BTN_SIZE * (length-firstRow)) / 2f;
                    btn.setRect(left + (placed - firstRow) * BTN_SIZE, top + GAP + BTN_SIZE, BTN_SIZE, BTN_SIZE);
                }
                add(btn);
                placed++;
                IconButtons.add(btn);
            }
        }

        private void clearImage() {
            for (int i = 0, len = IconButtons.size(); i < len; ++i) {
                IconButtons.get(i).destroy();
            }
            IconButtons.clear();
        }
    }
}
