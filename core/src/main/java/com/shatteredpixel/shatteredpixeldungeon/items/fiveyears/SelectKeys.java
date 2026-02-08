package com.shatteredpixel.shatteredpixeldungeon.items.fiveyears;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.TestItem;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.Key;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndJournal;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Objects;

public class SelectKeys extends TestItem {
    {
        image = ItemSpriteSheet.KEYS_UNKNOWN;
        defaultAction = AC_SPAWN;
    }

    private static final String AC_SPAWN = "spawn";
    private int keys_id;

    public SelectKeys(){
        keys_id = 0;
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
            GameScene.show(new WandSetting());
        }
    }

    private Key getKeys(int keys_id){
        switch (keys_id) {
            case 1:
                return new CrystalKey();
            case 2:
                return new GoldenKey();
            default:
                return new IronKey();
        }
    }

    private void createWand() {
        Key keys = getKeys(keys_id);
        if (Challenges.isItemBlocked(keys)) {
            return;
        }
        
        keys.identify();
        Catalog.setSeen(getClass());
        GameScene.pickUpJournal(keys, hero.pos);
        WndJournal.last_index = 0;
        Notes.add(keys);
        Sample.INSTANCE.play( Assets.Sounds.ITEM );
        hero.spendAndNext( TIME_TO_PICK_UP );
        GameScene.updateKeyDisplay();

        detach(hero.belongings.backpack);
    }

    private class WandSetting extends Window {

        // 定义常量
        private static final int WIDTH = 120; // 窗口宽度
        private static final int HEIGHT = 40; // 窗口高度
        private static final int GAP = 2; // 间隔大小
        private static final int BTN_SIZE = 18; // 按钮尺寸
        private static final int MAX_ICONS_PER_LINE = 3; // 每行最大图标数量

        // 成员变量
        protected RenderedTextBlock text;
        private Class[] AllWand; // 所有戒指的Class数组
        private RedButton RedButton_create; // 创建生成按钮
        private RedButton RedButton_cancel;
        private final ArrayList<IconButton> IconButtons = new ArrayList<>(); // 图标按钮列表

        /**
         * 构造函数，用于初始化窗口。
         */
        public WandSetting() {
            super();

            // 设置窗口尺寸
            resize(WIDTH, HEIGHT);

            // 创建法杖列表及图标
            createWandList();
            createWandImage(AllWand);

            text = PixelScene.renderTextBlock(new IronKey().name(),10);

            // 创建生成法杖按钮
            RedButton_create = new RedButton(Messages.get(this, "create")) {
                @Override
                protected void onClick() {
                    createWand();
                    hide();
                }
            };

            RedButton_cancel = new RedButton(Messages.get(this, "cancel")) {
                @Override
                protected void onClick() {
                    hide();
                }
            };

            add(text);

            add(RedButton_create);
            add(RedButton_cancel);

            layout();
        }

        private void layout() {

            text.setRect(WIDTH/2-20,RedButton_cancel.top()-16,WIDTH/2,16);

            RedButton_create.setRect(WIDTH / 2f + GAP / 2f, HEIGHT - GAP, WIDTH / 2f - GAP / 2f, 16);
            RedButton_cancel.setRect(GAP/2,HEIGHT - GAP, WIDTH / 2f - GAP / 2f, 16);
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
        private void createWandList() {
            AllWand = new Class<?>[]{
                   IronKey.class,
                   CrystalKey.class,
                   GoldenKey.class
            };
        }

        /**
         * 创建戒指图标，并添加到窗口中。
         *
         * @param all 所有戒指的Class数组f
         */
        private void createWandImage(Class<? extends Key>[] all) {
            float left = BTN_SIZE / 2f;
            float top = 0;
            int placed = 0;
            int length = all.length;
            for (int i = 0; i < length; ++i) {
                final int j = i;
                IconButton btn = new IconButton() {
                    @Override
                    protected void onClick() {
                        keys_id = Math.min(maxSlots(), j);
                        updateSelectedWandText();
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

        private void updateSelectedWandText() {
            Key keys = Reflection.newInstance(getKeys(keys_id).getClass());
            text.text(keys.name());
            layout();
        }

        private int maxSlots() {
            return 15;
        }
    }

}
