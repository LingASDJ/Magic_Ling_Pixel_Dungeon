package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.SmallLeafHardDungeon.WndChangeProp.cleanTrashItems;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.SmallLeaf;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.SmallLeafPlot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PropBag;
import com.shatteredpixel.shatteredpixeldungeon.items.props.Prop;
import com.shatteredpixel.shatteredpixeldungeon.items.props.Trash;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SmallLeafSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class SmallLeafHardDungeon extends NPC {
    {
        spriteClass = SmallLeafSprite.class;
        maxLvl = -1;
        flying = true;
    }

    @Override
    protected boolean act() {
        Chasm.isSmallLeaf = true;
        return super.act();
    }

    public boolean first = true;
    SmallLeafPlot.PropChange plot = new SmallLeafPlot.PropChange();

    @Override
    public void die(Object cause) {
        super.die(cause);
        Chasm.isSmallLeaf = false;
    }

    @Override
    public boolean interact(Char c){
        if(!first) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(new SmallLeafSprite(),
                            Messages.get(SmallLeafHardDungeon.class, "title"),
                            Messages.get(SmallLeafHardDungeon.class, "message"),
                            Messages.get(SmallLeafHardDungeon.class, "change2"),
                            Messages.get(SmallLeafHardDungeon.class, "change3"),
                            Messages.get(SmallLeafHardDungeon.class, "change1"),
                            Messages.get(SmallLeafHardDungeon.class, "change4")) {
                        @Override
                        protected void onSelect(int index) {
                            super.onSelect(index);
                            if (index == 0) {
                                plot.change =true;
                                Prop p1 = Prop.randomPropA(1);
                                Prop p2 = Prop.randomPropB(1);
                                p1.collect();
                                p2.collect();
                                GLog.i(Messages.get(hero, "you_now_have", p1.name()));
                                GLog.i(Messages.get(hero, "you_now_have", p2.name()));
                                cleanTrashItems();
                                die(this);
                                destroy();
                                GLog.b(Messages.get(SmallLeafHardDungeon.class, "pair_changed"));
                            } else if (index == 1) {
                                plot.change =true;
                                Prop p1 = Prop.randomPropA(2);
                                Prop p2 = Prop.randomPropB(2);
                                p1.collect();
                                p2.collect();
                                GLog.i(Messages.get(hero, "you_now_have", p1.name()));
                                GLog.i(Messages.get(hero, "you_now_have", p2.name()));
                                cleanTrashItems();
                                die(this);
                                destroy();
                                GLog.b(Messages.get(SmallLeafHardDungeon.class, "pair_changed"));
                            } else if (index == 2){
                                if(Random.Float()>=0.5f){
                                    Prop p1 = Prop.randomPropA(0);
                                    Prop p2 = Prop.randomPropB(0);
                                    p1.collect();
                                    p2.collect();
                                    GLog.i(Messages.get(hero, "you_now_have", p1.name()));
                                    GLog.i(Messages.get(hero, "you_now_have", p2.name()));
                                    cleanTrashItems();
                                } else {
                                    Prop p1 = Prop.randomPropA(1);
                                    Prop p2 = Prop.randomPropB(1);
                                    p1.collect();
                                    p2.collect();
                                    GLog.i(Messages.get(hero, "you_now_have", p1.name()));
                                    GLog.i(Messages.get(hero, "you_now_have", p2.name()));
                                    cleanTrashItems();
                                }
                                die(this);
                                destroy();
                                GLog.b(Messages.get(SmallLeafHardDungeon.class, "pair_changed"));
                            } else if (index == 3){
                                // 打开成对嬗变窗口
                                GameScene.show(new WndChangeProp(SmallLeafHardDungeon.this, null, 0));
                            }
                        }
                    });
                }
            });
        }else {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(plot,false));
                }
            });
            first=false;
        }
        return true;
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    public void damage(int dmg, Object src, DamageType type) {
        //do nothing
    }

    @Override
    public boolean add(Buff buff ) {
        return false;
    }

    private static final String FIRST = "first";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
    }

    public static class WndChangeProp extends Window {

        private static final int WIDTH = 120;
        private static final int BTN_SIZE = 32;
        private static final float GAP = 2;
        public int mode;

        private ItemButton btnPressed;
        private ItemButton btnItem1; // 第一个藏品：仅正面（kind=0）
        private ItemButton btnItem2; // 第二个藏品：仅负面（kind=1）
        private RedButton btnChange;

        // 存储第一个正面藏品的稀有度（用于限制第二个负面藏品）
        private int selectedPositiveRareness = -1;

        public WndChangeProp(SmallLeafHardDungeon smallLeaf, Window wndParent, int mo) {
            super();

            mode = mo;

            IconTitle titlebar = new IconTitle();
            titlebar.icon(new SmallLeaf().sprite());
            titlebar.label(Messages.titleCase(smallLeaf.name()));
            titlebar.setRect(0, 0, WIDTH, 0);
            add(titlebar);

            // 提示文本：使用多语言配置
            RenderedTextBlock message = PixelScene.renderTextBlock(Messages.get(this, "pair_message"), 6);
            message.maxWidth(WIDTH);
            message.setPos(0, titlebar.bottom() + GAP);
            add(message);

            // 第一个藏品按钮：仅选正面（kind=0）
            btnItem1 = new ItemButton() {
                @Override
                protected void onClick() {
                    btnPressed = btnItem1;
                    GameScene.selectItem(firstItemSelector);
                }
            };
            btnItem1.setRect(WIDTH/2 - BTN_SIZE - GAP, message.top() + message.height(), BTN_SIZE, BTN_SIZE);
            add(btnItem1);

            // 第二个藏品按钮：初始禁用，仅选负面（kind=1）
            btnItem2 = new ItemButton() {
                @Override
                protected void onClick() {
                    if (btnItem1.item() != null && selectedPositiveRareness != -1) {
                        btnPressed = btnItem2;
                        GameScene.selectItem(secondItemSelector);
                    }
                }
            };
            btnItem2.setRect(WIDTH/2 + GAP, message.top() + message.height(), BTN_SIZE, BTN_SIZE);
            btnItem2.active = false; // 初始禁用
            add(btnItem2);

            // 嬗变按钮：使用多语言配置，初始禁用
            btnChange = new RedButton(Messages.get(this, "change")) {
                @Override
                protected void onClick() {
                    if (btnItem1.item() instanceof Prop && btnItem2.item() instanceof Prop) {
                        Prop positiveProp = (Prop) btnItem1.item();
                        Prop negativeProp = (Prop) btnItem2.item();

                        if (mode == 0) {
                            handleNormalPairTransformation(positiveProp, negativeProp);
                        } else {
                            handleDistortedPairTransformation(positiveProp, negativeProp);
                        }

                        cleanTrashItems();

                        hide();
                        if (wndParent != null) {
                            wndParent.hide();
                        }
                        GLog.b(Messages.get(SmallLeafHardDungeon.class, "pair_changed"));
                        smallLeaf.die(smallLeaf);
                        smallLeaf.destroy();
                    }
                }
            };
            btnChange.enable(false);
            btnChange.setRect(0, btnItem2.bottom() + GAP, WIDTH, 20);
            add(btnChange);

            resize(WIDTH, (int) btnChange.bottom());
        }

        /**
         * 检查藏品是否已存在于背包中（防重复）
         */
        private boolean isPropExistsInBackpack(Class<? extends Prop> propClass) {
            if (propClass == null) return false;
            for (Prop p : Dungeon.hero.belongings.getAllItems(Prop.class)) {
                if (p.getClass() == propClass) {
                    return true;
                }
            }
            return false;
        }

        /**
         * 生成不重复的新藏品
         */
        private Prop generateUniqueProp(int targetKind, int targetRareness, Class<? extends Prop> excludeClass) {
            Prop newProp = null;
            int maxAttempts = 100;
            int attempts = 0;

            while (attempts < maxAttempts) {
                if (targetKind == 0) {
                    newProp = Prop.randomPropA(targetRareness);
                } else {
                    newProp = Prop.randomPropB(targetRareness);
                }

                if (newProp != null
                        && newProp.getClass() != excludeClass
                        && !isPropExistsInBackpack(newProp.getClass())) {
                    break;
                }
                attempts++;
            }

            if (newProp == null || newProp.getClass() == excludeClass) {
                newProp = targetKind == 0 ? Prop.randomPropA(targetRareness) : Prop.randomPropB(targetRareness);
                while (newProp != null && newProp.getClass() == excludeClass) {
                    newProp = targetKind == 0 ? Prop.randomPropA(targetRareness) : Prop.randomPropB(targetRareness);
                }
            }

            return newProp;
        }

        /**
         * 常规成对嬗变（正面+负面同阶）
         */
        private void handleNormalPairTransformation(Prop positiveOld, Prop negativeOld) {
            int rareness = positiveOld.rareness;

            // 生成新藏品：正面→负面，负面→正面，且同阶、不重复
            Prop newNegativeProp = generateUniqueProp(1, rareness, positiveOld.getClass());
            Prop newPositiveProp = generateUniqueProp(0, rareness, negativeOld.getClass());

            // 避免新藏品之间重复
            if (newNegativeProp != null && newPositiveProp != null && newNegativeProp.getClass() == newPositiveProp.getClass()) {
                newPositiveProp = generateUniqueProp(0, rareness, newNegativeProp.getClass());
            }

            // 移除旧藏品
            positiveOld.detach(Dungeon.hero.belongings.backpack);
            negativeOld.detach(Dungeon.hero.belongings.backpack);
            Statistics.removeProp(positiveOld);
            Statistics.removeProp(negativeOld);

            // 添加新藏品，使用多语言提示
            if (newNegativeProp != null) {
                newNegativeProp.collect();
                GLog.i(Messages.get(Dungeon.hero, "you_now_have", newNegativeProp.name()));
            }
            if (newPositiveProp != null) {
                newPositiveProp.collect();
                GLog.i(Messages.get(Dungeon.hero, "you_now_have", newPositiveProp.name()));
            }
        }

        /**
         * 畸变成对嬗变
         */
        private void handleDistortedPairTransformation(Prop positiveOld, Prop negativeOld) {
            int rareness = positiveOld.rareness;

            // 移除旧藏品
            positiveOld.detach(Dungeon.hero.belongings.backpack);
            negativeOld.detach(Dungeon.hero.belongings.backpack);
            Statistics.removeProp(positiveOld);
            Statistics.removeProp(negativeOld);

            if (Random.Float() > 0.66f) {
                Prop newPositive1 = generateUniqueProp(0, rareness + 1, positiveOld.getClass());
                Prop newPositive2 = generateUniqueProp(0, rareness, positiveOld.getClass());

                if (newPositive1 != null && newPositive2 != null && newPositive1.getClass() == newPositive2.getClass()) {
                    newPositive2 = generateUniqueProp(0, rareness, newPositive1.getClass());
                }

                if (newPositive1 != null) {
                    newPositive1.collect();
                    GLog.i(Messages.get(Dungeon.hero, "you_now_have", newPositive1.name()));
                }
                if (newPositive2 != null) {
                    newPositive2.collect();
                    GLog.i(Messages.get(Dungeon.hero, "you_now_have", newPositive2.name()));
                }
            } else {
                Prop newNegative1 = generateUniqueProp(1, rareness, negativeOld.getClass());
                Prop newNegative2 = generateUniqueProp(1, rareness + 1, negativeOld.getClass());

                if (newNegative1 != null && newNegative2 != null && newNegative1.getClass() == newNegative2.getClass()) {
                    newNegative2 = generateUniqueProp(1, rareness + 1, newNegative1.getClass());
                }

                if (newNegative1 != null) {
                    newNegative1.collect();
                    GLog.i(Messages.get(Dungeon.hero, "you_now_have", newNegative1.name()));
                }
                if (newNegative2 != null) {
                    newNegative2.collect();
                    GLog.i(Messages.get(Dungeon.hero, "you_now_have", newNegative2.name()));
                }
            }
        }

        /**
         * 清理垃圾物品（保留多语言）
         */
        public static void cleanTrashItems() {
            if (Dungeon.hero.belongings.getItem(Trash.class) != null) {
                for (Trash t : Dungeon.hero.belongings.getAllItems(Trash.class)) {
                    t.detach(Dungeon.hero.belongings.backpack);
                }
                GLog.b(Messages.get(Trash.class, "trash"));
            }
        }

        /**
         * 第一个选择器：仅允许选择正面藏品（kind=0），使用多语言提示
         */
        protected WndBag.ItemSelector firstItemSelector = new WndBag.ItemSelector() {
            @Override
            public String textPrompt() {
                return Messages.get(this, "select_first_positive_prop");
            }

            @Override
            public Class<? extends Bag> preferredBag() {
                return PropBag.class;
            }

            @Override
            public boolean itemSelectable(Item item) {
                // 严格限定：必须是Prop + kind=0（正面）
                return item instanceof Prop && ((Prop) item).kind == 0;
            }

            @Override
            public void onSelect(Item item) {
                if (item != null && btnPressed.parent != null) {
                    Prop positiveProp = (Prop) item;
                    btnItem1.item(positiveProp);
                    selectedPositiveRareness = positiveProp.rareness;
                    btnItem2.active = true;
                    btnChange.enable(false);
                }
            }
        };

        /**
         * 第二个选择器：仅允许选择同阶负面藏品（kind=1 + 稀有度匹配），使用多语言提示
         */
        protected WndBag.ItemSelector secondItemSelector = new WndBag.ItemSelector() {
            @Override
            public String textPrompt() {
                // 多语言提示，传入阶数参数
                return Messages.get(this, "select_second_negative_prop", selectedPositiveRareness+1);
            }

            @Override
            public Class<? extends Bag> preferredBag() {
                return PropBag.class;
            }

            @Override
            public boolean itemSelectable(Item item) {
                // 严格限定：必须是Prop + kind=1（负面） + 稀有度=正面藏品的稀有度
                return item instanceof Prop
                        && ((Prop) item).kind == 1
                        && ((Prop) item).rareness == selectedPositiveRareness;
            }

            @Override
            public void onSelect(Item item) {
                if (item != null && btnPressed.parent != null) {
                    btnItem2.item(item);
                    // 两个藏品都满足条件，启用嬗变按钮
                    btnChange.enable(true);
                }
            }
        };
    }
}