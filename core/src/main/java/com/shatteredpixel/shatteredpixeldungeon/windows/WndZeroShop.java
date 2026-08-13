package com.shatteredpixel.shatteredpixeldungeon.windows;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ReloadShop;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.ZeroDreamShop;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.AnySkinSelect;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.SKINITEM;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZeroDreamSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemSlot;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.List;

public class WndZeroShop extends Window {
    // 常量统一提取
    private static final int WIDTH = 120;
    private static final int BTN_SIZE = 20;
    private static final int BTN_GAP = 3;
    private static final int GAP = 6;
    private static final int COL_COUNT = 5;

    // 皮肤映射表：道具类名 <-> 解锁Key，消除大量if判断
    private static final List<SkinMapping> SKIN_MAPPINGS = new ArrayList<>();
    static {
        // 一阶皮肤
        SKIN_MAPPINGS.add(new SkinMapping(SKINITEM.SKIN_WA.class, "avatars_warrior_1"));
        SKIN_MAPPINGS.add(new SkinMapping(SKINITEM.SKIN_MA.class, "avatars_mage_1"));
        SKIN_MAPPINGS.add(new SkinMapping(SKINITEM.SKIN_RA.class, "avatars_rogue_1"));
        SKIN_MAPPINGS.add(new SkinMapping(SKINITEM.SKIN_HA.class, "avatars_huntress_1"));
        SKIN_MAPPINGS.add(new SkinMapping(SKINITEM.SKIN_DA.class, "avatars_duelist_1"));
        // 二阶皮肤
        SKIN_MAPPINGS.add(new SkinMapping(SKINITEM.SKIN_WB.class, "avatars_warrior_2"));
        SKIN_MAPPINGS.add(new SkinMapping(SKINITEM.SKIN_MB.class, "avatars_mage_2"));
        SKIN_MAPPINGS.add(new SkinMapping(SKINITEM.SKIN_RB.class, "avatars_rogue_2"));
        SKIN_MAPPINGS.add(new SkinMapping(SKINITEM.SKIN_HB.class, "avatars_huntress_2"));
        SKIN_MAPPINGS.add(new SkinMapping(SKINITEM.SKIN_DB.class, "avatars_duelist_2"));
        // 高阶皮肤
        SKIN_MAPPINGS.add(new SkinMapping(SKINITEM.SKIN_MC.class, "avatars_mage_4"));
        SKIN_MAPPINGS.add(new SkinMapping(SKINITEM.SKIN_RC.class, "avatars_rogue_4"));
        SKIN_MAPPINGS.add(new SkinMapping(SKINITEM.SKIN_WC.class, "avatars_warrior_4"));

        SKIN_MAPPINGS.add(new SkinMapping(SKINITEM.SKIN_DC.class, "avatars_duelist_4"));

        SKIN_MAPPINGS.add(new SkinMapping(SKINITEM.SKIN_DD.class, "avatars_duelist_5"));
    }

    // 映射内部类
    private static class SkinMapping {
        Class<? extends SKINITEM> skinCls;
        String unlockKey;
        SkinMapping(Class<? extends SKINITEM> cls, String key) {
            skinCls = cls;
            unlockKey = key;
        }
    }

    public WndZeroShop() {
        Class<? extends SKINITEM>[] skinClasses = new Class[]{
                // 第1行 5格
                SKINITEM.SKIN_WA.class, SKINITEM.SKIN_MA.class, SKINITEM.SKIN_RA.class, SKINITEM.SKIN_HA.class, SKINITEM.SKIN_DA.class,
                // 第2行 5格 (累计10)
                SKINITEM.SKIN_WB.class, SKINITEM.SKIN_MB.class, SKINITEM.SKIN_RB.class, SKINITEM.SKIN_HB.class, SKINITEM.SKIN_DB.class,
                // 第3行 4格 (11,12,13,14) 补齐14个元素
                null,SKINITEM.SKIN_MC.class, SKINITEM.SKIN_RC.class, SKINITEM.SKIN_HC.class, SKINITEM.SKIN_DC.class,
                null,null,null,null,SKINITEM.SKIN_DD.class
        };
        // 强制长度校验，防止数组不匹配
        assert skinClasses.length == ZeroDreamShop.SHOP_ITEMS.length;

        for (int i = 0; i < ZeroDreamShop.SHOP_ITEMS.length; i++) {
            Class<? extends SKINITEM> cls = skinClasses[i];
            if (cls == null) {
                ZeroDreamShop.SHOP_ITEMS[i] = null;
                continue;
            }
            String unlockKey = getUnlockKey(cls);
            ZeroDreamShop.SHOP_ITEMS[i] = SPDSettings.isItemUnlock(unlockKey) ? null : createSkinItem(cls);
        }

        // 标题
        IconTitle titlebar = new IconTitle();
        titlebar.setRect(0, 0, WIDTH, 0);
        titlebar.icon(new ZeroDreamSprite());
        titlebar.label(Messages.get(ZeroDreamShop.class, "name"));
        add(titlebar);

        // 提示文本
        RenderedTextBlock message = PixelScene.renderTextBlock(Messages.get(ZeroDreamShop.class, "descx", hero.name()), 6);
        message.maxWidth(WIDTH);
        message.setPos(0, titlebar.bottom() + GAP);
        add(message);
        float startY = message.bottom() + BTN_GAP + 7;

        // 循环批量生成所有商品格子
        float currentX = 4;
        float currentY = startY;
        float maxBottom = startY;
        for (int i = 0; i < ZeroDreamShop.SHOP_ITEMS.length; i++) {
            Item item = ZeroDreamShop.SHOP_ITEMS[i];
            RewardButton btn = new RewardButton(item);
            btn.setRect(currentX, currentY, BTN_SIZE, BTN_SIZE);
            add(btn);

            // 记录最底部坐标，用于窗口高度计算
            maxBottom = btn.bottom();

            // 换行逻辑
            currentX += BTN_SIZE + BTN_GAP;
            if ((i + 1) % COL_COUNT == 0) {
                currentX = 4;
                currentY += BTN_SIZE + BTN_GAP;
            }
        }

        // ==========修复2：用真实最底部坐标resize，不再额外叠加高度==========
        resize(WIDTH, (int) maxBottom + GAP);
    }

    // 根据皮肤Class创建实例
    private SKINITEM createSkinItem(Class<? extends SKINITEM> cls) {
        try {
            return cls.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    // 根据皮肤Class获取解锁Key，替代大量if
    private String getUnlockKey(Class<? extends SKINITEM> skinCls) {
        for (SkinMapping map : SKIN_MAPPINGS) {
            if (map.skinCls == skinCls) return map.unlockKey;
        }
        return "";
    }

    // 解锁皮肤，删除超长if判断
    public void itemUnlock(Item item) {
        if (!(item instanceof SKINITEM)) return;
        Class<? extends SKINITEM> targetCls = (Class<? extends SKINITEM>) item.getClass();
        String unlockKey = getUnlockKey(targetCls);
        if (!unlockKey.isEmpty()) SPDSettings.unlockItem(unlockKey);
    }

    // 购买成功回调
    private void selectReward(Item reward) {
        hide();
        GLog.i(Messages.get(hero, "you_now_have", reward.name()));
    }

    // 商品弹窗
    private class RewardWindow extends WndInfoItem {
        public RewardWindow(Item item) {
            super(item);
            String key = getUnlockKey((Class<? extends SKINITEM>) item.getClass());
            boolean locked = SPDSettings.isItemUnlock(key);
            String buyText = Messages.get(WndIceTradeItem.class, locked ? "unlocked" : "buy", item.iceCoinValue());
            String giftText = Messages.get(WndIceTradeItem.class, locked ? "unlocked" : "gift");

            // 付费按钮
            StyledButton btnBuy = new StyledButton(locked ? Chrome.Type.SCROLL : Chrome.Type.RED_BUTTON, buyText) {
                @Override
                protected void onClick() {
                    if (SPDSettings.iceCoin() >= item.iceCoinValue()) {
                        SPDSettings.iceDownCoin(item.iceCoinValue());
                        selectReward(item);
                        itemUnlock(item);
                        item.cursed = true;
                        Buff.prolong(hero, ReloadShop.class, 1f);
                        hide();
                    } else {
                        GLog.n(Messages.get(ZeroDreamShop.class, "no"));
                        hide();
                    }
                }
            };
            btnBuy.active = !locked;
            btnBuy.setRect(0, height + 2, WIDTH, 16);
            add(btnBuy);

            // 兑换券按钮
            StyledButton btnGift = new StyledButton(locked ? Chrome.Type.SCROLL : Chrome.Type.RED_BUTTON, giftText) {
                @Override
                protected void onClick() {
                    AnySkinSelect skinSelect = hero.belongings.getItem(AnySkinSelect.class);
                    AnySkinSelect.AnySkinCustomSelect anySkinCustomSelect = hero.belongings.getItem(AnySkinSelect.AnySkinCustomSelect.class);
                    if (skinSelect != null || anySkinCustomSelect != null) {
                        selectReward(item);
                        itemUnlock(item);
                        SPDSettings.unlockItem("anyskin1");
                        if(skinSelect != null){
                            skinSelect.detach(hero.belongings.backpack);
                        } else {
                            anySkinCustomSelect.detach(hero.belongings.backpack);
                        }
                        Buff.prolong(hero, ReloadShop.class, 1f);
                        hide();
                    } else {
                        GLog.n(Messages.get(ZeroDreamShop.class, "no_skin"));
                        hide();
                    }
                }
            };
            btnGift.active = !locked;
            btnGift.setRect(0, btnBuy.bottom() + 2, WIDTH, 16);
            add(btnGift);

            resize(width, (int) btnGift.bottom());
        }
    }

    // 商品格子按钮
    public class RewardButton extends Component {
        protected NinePatch bg;
        protected ItemSlot slot;

        public RewardButton(Item item) {
            bg = Chrome.get(Chrome.Type.RED_BUTTON);
            add(bg);
            slot = new ItemSlot(item) {
                @Override
                protected void onPointerDown() {
                    bg.brightness(1.2f);
                    Sample.INSTANCE.play(Assets.Sounds.CLICK);
                }
                @Override
                protected void onPointerUp() {
                    bg.resetColor();
                }
                @Override
                protected void onClick() {
                    if (item != null) ShatteredPixelDungeon.scene().addToFront(new RewardWindow(item));
                }
            };
            add(slot);
        }

        @Override
        protected void layout() {
            super.layout();
            // 修复：NinePatch 无 setRect，拆分坐标与尺寸
            bg.x = x;
            bg.y = y;
            bg.size(width, height);

            slot.setRect(x + 2, y + 2, width - 4, height - 4);
        }
    }
}