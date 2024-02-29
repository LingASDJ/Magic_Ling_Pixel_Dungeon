package com.shatteredpixel.shatteredpixeldungeon.custom.testmode;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Bolas;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.FishingSpear;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ForceCube;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.HeavyBoomerang;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Javelin;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Kunai;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.RedBlock;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.StreamerKnife;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Shuriken;
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
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
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
            RedBlock.class
    };

    public SpawnMissile(){
        this.item_id = 0;
        this.item_quantity = 1;
        this.item_level = 0;
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
        bundle.put("bow_generated", bowGenerated);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        item_quantity = bundle.getInt("item_quantity");
        item_id = bundle.getInt("item_id");
        item_level = bundle.getInt("item_level");
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

    private class SettingsWindow extends Window {
        private final RedButton RedButton_level;
        private final RedButton RedButton_quantity;
        private RedButton b_create;
        private ArrayList<IconButton> IconButton = new ArrayList<>();
        private static final int WIDTH = 120;
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

        private void layout() {
            RedButton_level.setRect(0, IconButton.get(IconButton.size() - 1).bottom() + GAP + 2 * GAP, WIDTH, 24);
            RedButton_quantity.setRect(0, RedButton_level.bottom() + GAP, WIDTH, 24);
            b_create.setRect(0, RedButton_quantity.bottom() + GAP, WIDTH, 16);
            resize(WIDTH, (int) b_create.bottom() );
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
