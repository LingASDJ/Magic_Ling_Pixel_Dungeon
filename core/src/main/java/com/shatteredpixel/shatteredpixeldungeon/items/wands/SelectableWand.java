package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.TestItem;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Objects;

public class SelectableWand extends TestItem {
    {
        image = ItemSpriteSheet.WAND_UNKNOWN;
        defaultAction = AC_SPAWN;
    }

    private static final String AC_SPAWN = "spawn";
    private int wand_id;

    public SelectableWand(){
        wand_id = 0;
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

    private Wand getWand(int wand_id){
        switch (wand_id) {
            case 1:
                return new WandOfCorrosion();
            case 2:
                return new WandOfCorruption();
            case 3:
                return new WandOfDisintegration();
            case 4:
                return new WandOfFireblast();
            case 5:
                return new WandOfFrost();
            case 6:
                return new WandOfLightning();
            case 7:
                return new WandOfLivingEarth();
            case 8:
                return new WandOfMagicMissile();
            case 9:
                return new WandOfPrismaticLight();
            case 10:
                return new WandOfRegrowth();
            case 11:
                return new WandOfScale();
            case 12:
                return new WandOfSun();
            case 13:
                return new WandOfTransfusion();
            case 14:
                return new WandOfWarding();
            case 0:
            default:
                return new WandOfBlastWave();
        }
    }

    private Wand modifyWand(Wand wand) {
        if(Dungeon.depth == 32){
            wand.level = 4;
        } else {
            wand.level = Random.Int(2,4);
        }
        wand.upgrade();
        return wand;
    }

    private void createWand() {
        Wand wand = getWand(wand_id);
        if (Challenges.isItemBlocked(wand)) {
            return;
        }

        wand = modifyWand(wand);
        wand.identify();
        if (wand.collect()) {
            GameScene.pickUp(wand, hero.pos );
            Sample.INSTANCE.play( Assets.Sounds.ITEM );
            GLog.i(Messages.get(hero, "you_now_have", wand.name()));
        } else {
            wand.doDrop(curUser);
        }

        detach(hero.belongings.backpack);
    }

    private class WandSetting extends Window {

        // 定义常量
        private static final int WIDTH = 150; // 窗口宽度
        private static final int HEIGHT = 100; // 窗口高度
        private static final int GAP = 2; // 间隔大小
        private static final int BTN_SIZE = 18; // 按钮尺寸
        private static final int MAX_ICONS_PER_LINE = 4; // 每行最大图标数量

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

            text = PixelScene.renderTextBlock(new WandOfBlastWave().name(),10);

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
                    WandOfBlastWave.class,
                    WandOfCorrosion.class,
                    WandOfCorruption.class,
                    WandOfDisintegration.class,
                    WandOfFireblast.class,
                    WandOfFrost.class,
                    WandOfLightning.class,
                    WandOfLivingEarth.class,
                    WandOfMagicMissile.class,
                    WandOfPrismaticLight.class,
                    WandOfRegrowth.class,
                    WandOfScale.class,
                    WandOfSun.class,
                    WandOfTransfusion.class,
                    WandOfWarding.class
            };
        }

        /**
         * 创建戒指图标，并添加到窗口中。
         *
         * @param all 所有戒指的Class数组f
         */
        private void createWandImage(Class<? extends Wand>[] all) {
            float left = BTN_SIZE / 2f;
            float top = 0;
            int placed = 0;
            int length = all.length;
            for (int i = 0; i < length; ++i) {
                final int j = i;
                IconButton btn = new IconButton() {
                    @Override
                    protected void onClick() {
                        wand_id = Math.min(maxSlots(), j);
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
            Wand wand = Reflection.newInstance(getWand(wand_id).getClass());
            text.text(wand.name());
            layout();
        }

        private int maxSlots() {
            return 15;
        }
    }
}
