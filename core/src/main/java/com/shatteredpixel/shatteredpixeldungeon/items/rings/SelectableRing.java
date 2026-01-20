package com.shatteredpixel.shatteredpixeldungeon.items.rings;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
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

public class SelectableRing extends TestItem {
    {
        image = ItemSpriteSheet.RING_UNKNOWN;
        defaultAction = AC_SPAWN;
    }

    private static final String AC_SPAWN = "spawn";
    private int ring_id;

    public SelectableRing(){
        ring_id = 0;
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
            GameScene.show(new RingSetting());
        }
    }

    private Ring getRing(int ring_id){
        switch (ring_id) {
            case 1:
                return new RingOfArcana();
            case 2:
                return new RingOfElements();
            case 3:
                return new RingOfEnergy();
            case 4:
                return new RingOfEvasion();
            case 5:
                return new RingOfForce();
            case 6:
                return new RingOfFuror();
            case 7:
                return new RingOfHaste();
            case 8:
                return new RingOfMight();
            case 9:
                return new RingOfSharpshooting();
            case 10:
                return new RingOfTenacity();
            case 11:
                return new RingOfWealth();
            case 0:
            default:
                return new RingOfAccuracy();
        }
    }

    private Ring modifyRing(Ring ring) {
        ring.level = Random.Int(2,4);
        return ring;
    }

    private void createRing() {
        Ring ring = getRing(ring_id);
        if (Challenges.isItemBlocked(ring)) {
            return;
        }
        ring = modifyRing(ring);
        ring.identify();
        if (ring.collect()) {
            GameScene.pickUp(ring, hero.pos );
            Sample.INSTANCE.play( Assets.Sounds.ITEM );
            GLog.i(Messages.get(hero, "you_now_have", ring.name()));
        } else {
            ring.doDrop(curUser);
        }
        detach(hero.belongings.backpack);
    }

    private class RingSetting extends Window {

        // 定义常量
        private static final int WIDTH = 150; // 窗口宽度
        private static final int HEIGHT = 75; // 窗口高度
        private static final int GAP = 2; // 间隔大小
        private static final int BTN_SIZE = 18; // 按钮尺寸
        private static final int MAX_ICONS_PER_LINE = 4; // 每行最大图标数量

        // 成员变量
        protected RenderedTextBlock text;
        private Class[] AllRing; // 所有戒指的Class数组
        private RedButton RedButton_create; // 创建生成按钮
        private RedButton RedButton_cancel;
        private final ArrayList<IconButton> IconButtons = new ArrayList<>(); // 图标按钮列表

        /**
         * 构造函数，用于初始化窗口。
         */
        public RingSetting() {
            super();

            // 设置窗口尺寸
            resize(WIDTH, HEIGHT);

            // 创建戒指列表及图标
            createRingList();
            createRingImage(AllRing);

            text = PixelScene.renderTextBlock(new RingOfAccuracy().name(),10);

            // 创建生成戒指按钮
            RedButton_create = new RedButton(Messages.get(this, "create")) {
                @Override
                protected void onClick() {
                    createRing();
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
        private void createRingList() {
            AllRing = new Class<?>[]{
                    RingOfAccuracy.class,
                    RingOfArcana.class,
                    RingOfElements.class,
                    RingOfEnergy.class,
                    RingOfEvasion.class,
                    RingOfForce.class,
                    RingOfFuror.class,
                    RingOfHaste.class,
                    RingOfMight.class,
                    RingOfSharpshooting.class,
                    RingOfTenacity.class,
                    RingOfWealth.class
            };
        }

        /**
         * 创建戒指图标，并添加到窗口中。
         *
         * @param all 所有戒指的Class数组f
         */
        private void createRingImage(Class<? extends Ring>[] all) {
            float left = BTN_SIZE / 2f;
            float top = 0;
            int placed = 0;
            int length = all.length;
            for (int i = 0; i < length; ++i) {
                final int j = i;
                IconButton btn = new IconButton() {
                    @Override
                    protected void onClick() {
                        ring_id = Math.min(maxSlots(), j);
                        updateSelectedRingText();
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

        private void updateSelectedRingText() {
            Ring ring = Reflection.newInstance(getRing(ring_id).getClass());
            text.text(ring.name());
            layout();
        }

        private int maxSlots() {
            return 11;
        }
    }
}
